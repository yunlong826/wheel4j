package com.wheel.admin.registry.zk;

import com.google.common.collect.Maps;
import com.wheel.admin.registry.zk.service.ChildListener;
import com.wheel.admin.registry.zk.service.DataListener;
import com.wheel.admin.registry.zk.store.ZkDataStore;
import com.wheel.admin.registry.utils.Pair;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author jack_yun
 * @version 1.0
 * @description:
 * @date 2022/7/30 14:51
 */
@Slf4j
public class CuratorZookeeperClient {
    static final Charset charset = StandardCharsets.UTF_8;
    private final CuratorFramework client;
    private Map<String, TreeCache> treeCacheMap = new ConcurrentHashMap<>();

    private ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    private Map<String, Boolean> createPath = Maps.newConcurrentMap();
    private Map<String, CuratorWatcherImpl> childListenerMap = Maps.newConcurrentMap();
    private Map<String, Pair<CuratorWatcherImpl, Executor>> dataListenerMap = Maps.newConcurrentMap();

    public CuratorZookeeperClient(String zkAddress) {
        try {
            // 设置zk临时节点的有效时间sessionTimeout，连接时间和会话超时时间是不一样的
            int timeout = 3000;
            int retryTimeMillis = 1000;
            CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder()
                    .connectString(zkAddress)
                    .retryPolicy(new RetryNTimes(1, retryTimeMillis))
                    .connectionTimeoutMs(timeout)
                    .sessionTimeoutMs(timeout);
            client = builder.build();
            client.getConnectionStateListenable().addListener(new ConnectionStateListener() {
                @Override
                public void stateChanged(CuratorFramework client, ConnectionState state) {
                    if (state == ConnectionState.LOST) {
                        log.info("zk断开");
                        //   CuratorZookeeperClient.this.stateChanged(StateListener.DISCONNECTED);
                    } else if (state == ConnectionState.CONNECTED) {
                        log.info("zk连接成功");
                        //     CuratorZookeeperClient.this.stateChanged(StateListener.CONNECTED);
                    } else if (state == ConnectionState.RECONNECTED) {
                        log.info("zk重新连接");
                        //  这里有状态，重新连接时，这里需要重新注册关注的事件
                        recover();
                        //   CuratorZookeeperClient.this.stateChanged(StateListener.RECONNECTED);
                    }
                }
            });
            client.start();
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    private void recover() {
        long delayRecoverMillis = 30000;
        scheduledExecutorService.schedule(() -> {
            try {
                log.info("恢复zk现场");
                createPath.forEach(this::create);
                childListenerMap.forEach(this::addTargetChildListener);
                dataListenerMap.forEach((path, pair) -> {
                    addTargetDataListener(path, pair.getLeft(), pair.getRight());
                });
            } catch (Exception e) {
                log.error("恢复现场失败", e);
            }
        }, delayRecoverMillis, TimeUnit.MILLISECONDS);
    }

    public void create(String path, boolean ephemeral) {
        log.info("向zk注册节点：{}，是否临时：{}", path, ephemeral);
        // 重连时用于恢复现场
        createPath.put(path, ephemeral);
        if (!ephemeral) {
            if (checkExists(path)) {
                return;
            }
        }
        int i = path.lastIndexOf('/');
        if (i > 0) {
            // 递归调用
            create(path.substring(0, i), false);
        }
        if (ephemeral) {
            createEphemeral(path);
        } else {
            createPersistent(path);
        }
    }

    private void createEphemeral(String path) {
        try {
            client.create().withMode(CreateMode.EPHEMERAL).forPath(path);
        } catch (KeeperException.NodeExistsException e) {
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    private void createPersistent(String path) {
        try {
            client.create().forPath(path);
        } catch (KeeperException.NodeExistsException e) {
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }



    public List<String> getChildren(String path) {
        try {
            return client.getChildren().forPath(path);
        } catch (KeeperException.NoNodeException e) {
            return null;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public boolean checkExists(String path) {
        try {
            if (client.checkExists().forPath(path) != null) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }



    public CuratorWatcherImpl createTargetChildListener(String path, ChildListener listener) {
        return new CuratorWatcherImpl(client, listener);
    }

    /**
     * 子节点变化操作
     * @param path
     * @param listener
     * @return
     */
    public List<String> addTargetChildListener(String path, ChildListener listener) {
        return addTargetChildListener(path, createTargetChildListener(path, listener));
    }

    public List<String> addTargetChildListener(String path, CuratorWatcherImpl listener) {
        try {
            List<String> ret = client.getChildren().usingWatcher(listener).forPath(path);
            List<String> list = new ArrayList<>();
            ZkDataStore.ZKDATA.clear();
            String temp = path;
            setZkNodesPath(ZkDataStore.ZKDATA,temp,list);
            // todo 一个path暂时只能注册一个child watcher
            childListenerMap.put(path, listener);
            log.info("ZkDataStore.ZKDATA的数据:{}",ZkDataStore.ZKDATA);
            log.info("添加zk路径ChildListener：{}，得到子节点：{}", path, ret);
            // 第一次需要手动调用，恢复现场也需要调用
            listener.childListener.childChanged(path, ret);
            return ret;
        } catch (KeeperException.NoNodeException e) {
            log.info("KeeperException.NoNodeException e:{}",path);
            return null;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    /**
     *  一个多叉树递归小算法
     * @param zkdata
     * @param path
     * @param list
     */
    @SneakyThrows
    private void setZkNodesPath(List<String> zkdata, String path,List<String> list) {
        List<String> childrens = client.getChildren().forPath(path);
        if(childrens == null || childrens.size() == 0){
            zkdata.add(listToString(list));
            return;
        }
        for(String child:childrens){
            list.add("/"+child);
            setZkNodesPath(zkdata,path+"/"+child,list);
            list.remove(list.size()-1);
        }
    }
    private String listToString(List<String> list){
        String s = "";
        for(int i = 0;i<list.size();i++){
            s+=list.get(i);
        }
        return s;
    }

    public void doClose() {
        log.info("关闭zk");
        client.close();
    }





    protected void addTargetDataListener(String path, CuratorWatcherImpl treeCacheListener, Executor executor) {
        try {
            TreeCache treeCache = TreeCache.newBuilder(client, path).setCacheData(false).build();
            treeCacheMap.putIfAbsent(path, treeCache);
            treeCache.start();
            if (executor == null) {
                treeCache.getListenable().addListener(treeCacheListener);
            } else {
                treeCache.getListenable().addListener(treeCacheListener, executor);
            }
            dataListenerMap.put(path, new Pair(treeCacheListener, executor));
        } catch (Exception e) {
            throw new IllegalStateException("Add treeCache listener for path:" + path, e);
        }
    }



    static class CuratorWatcherImpl implements CuratorWatcher, TreeCacheListener {

        private CuratorFramework client;
        private volatile ChildListener childListener;
        private volatile DataListener dataListener;


        public CuratorWatcherImpl(CuratorFramework client, ChildListener listener) {
            this.client = client;
            this.childListener = listener;
        }

        public CuratorWatcherImpl(CuratorFramework client, DataListener dataListener) {
            this.dataListener = dataListener;
        }

        protected CuratorWatcherImpl() {
        }

        public void unwatch() {
            this.childListener = null;
        }

        /**
         * CuratorWatcher
         * @param event
         * @throws Exception
         */
        @Override
        public void process(WatchedEvent event) throws Exception {
            if (childListener != null) {
                String path = event.getPath() == null ? "" : event.getPath();
                childListener.childChanged(path,
                        // if path is null, curator using watcher will throw NullPointerException.
                        // if client connect or disconnect to server, zookeeper will queue
                        // watched event(Watcher.Event.EventType.None, .., path = null).
                        // 再次添加watcher，watcher需要重新添加，该方法返回子路径
                        path != null && path.length() > 0 ?
                                client.getChildren().usingWatcher(this).forPath(path) : Collections.<String>emptyList());
            }
        }

        /**
         * TreeCacheListener，数据变化
         * @param client
         * @param event
         * @throws Exception
         */
        @Override
        public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
            if (dataListener != null) {
                TreeCacheEvent.Type type = event.getType();
                String content = null;
                String path = null;
                switch (type) {
                    case NODE_ADDED:
                        path = event.getData().getPath();
                        content = new String(event.getData().getData(), charset);
                        break;
                    case NODE_UPDATED:
                        path = event.getData().getPath();
                        content = new String(event.getData().getData(), charset);
                        break;
                    case NODE_REMOVED:
                        path = event.getData().getPath();
                        break;
                    case INITIALIZED:
                        break;
                    case CONNECTION_LOST:
                        break;
                    case CONNECTION_RECONNECTED:
                        break;
                    case CONNECTION_SUSPENDED:
                        break;

                }
                dataListener.dataChanged(path, content, type);
            }
        }
    }
}
