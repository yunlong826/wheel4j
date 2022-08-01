package com.wheel.yun.cluster.loadbalance;

import com.wheel.yun.common.invoker.RpcInvocation;
import com.wheel.yun.common.utils.Bytes;
import com.wheel.yun.invoker.WheelInvoker;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author jack_yun
 * @version 1.0
 * @description:
 * @date 2022/7/4 17:11
 * 算法原理参考：https://blog.csdn.net/kefengwang/article/details/81628977
 */
public class ConsistentHashLoadBalance extends AbstractLoadBalance{

    private final ConcurrentHashMap<String,ConsistentHashSelector> selectors = new ConcurrentHashMap<String, ConsistentHashSelector>();

    @Override
    protected  WheelInvoker doSelect(List<WheelInvoker> invokers, RpcInvocation rpcInvocation) {
        String key = invokers.get(0).getIpAndPort()+"/"+getURL(rpcInvocation);
        // using the hashcode of list to compute the hash only pay attention to the elements in the list
        int invokersHashCode = getCorrespondingHashCode(invokers);
        ConsistentHashSelector selector = (ConsistentHashSelector) selectors.get(key);
        if(selector == null || selector.identityHashCode != invokersHashCode){
            selectors.put(key,new ConsistentHashSelector<>(invokers,invokersHashCode,rpcInvocation));
            selector = (ConsistentHashSelector) selectors.get(key);
        }
        return selector.select(rpcInvocation);
    }
    public <T> int getCorrespondingHashCode(List<WheelInvoker> invokers){
        return invokers.hashCode();
    }
    private static final class ConsistentHashSelector<T>{
        // 虚拟节点
        private final TreeMap<Long,WheelInvoker> virtualInvokers;

        // 每个真实节点对应的虚拟节点数
        private final int replicaNumber;

        // 识别不同节点里的列表
        private final int identityHashCode;

        private final int[] argumentIndex;

        ConsistentHashSelector(List<WheelInvoker> invokers,int identityHashCode,RpcInvocation rpcInvocation){
            this.virtualInvokers = new TreeMap<>();
            this.replicaNumber = 160;
            this.identityHashCode = identityHashCode;
            argumentIndex = new int[rpcInvocation.getArgs().length];
            initCH(invokers);
        }

        private void initCH(List<WheelInvoker> invokers) {
            for(WheelInvoker wheelInvoker:invokers){
                String ipAndPort = wheelInvoker.getIpAndPort();
                for(int i = 0;i<replicaNumber/4;i++){
                    byte[] digest = Bytes.getMD5(ipAndPort+i);
                    for(int h = 0;h<4;h++){
                        long m = hash(digest,h);
                        virtualInvokers.put(m,wheelInvoker);
                    }
                }
            }
        }
        private long hash(byte[] digest, int number) {
            return (((long) (digest[3 + number * 4] & 0xFF) << 24)
                    | ((long) (digest[2 + number * 4] & 0xFF) << 16)
                    | ((long) (digest[1 + number * 4] & 0xFF) << 8)
                    | (digest[number * 4] & 0xFF))
                    & 0xFFFFFFFFL;
        }

        public WheelInvoker select(RpcInvocation rpcInvocation) {
            String key = toKey(rpcInvocation.getArgs());
            byte[] digest = Bytes.getMD5(key);
            return selectKey(hash(digest,0));
        }

        private WheelInvoker selectKey(long hash) {
            if(!virtualInvokers.containsKey(hash)){
                Map.Entry<Long, WheelInvoker> entry = virtualInvokers.ceilingEntry(hash);
                if(entry != null){
                    return entry.getValue();
                }
                return virtualInvokers.firstEntry().getValue();
            }
            return virtualInvokers.get(hash);
        }

        private String toKey(Object[] args) {
            StringBuilder buf = new StringBuilder();
            for (int i : argumentIndex) {
                if (i >= 0 && i < args.length) {
                    buf.append(args[i]);
                }
            }
            return buf.toString();
        }
    }
}
