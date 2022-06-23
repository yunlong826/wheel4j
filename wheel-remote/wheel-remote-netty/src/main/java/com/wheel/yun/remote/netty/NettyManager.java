package com.wheel.yun.remote.netty;


import com.wheel.yun.common.utils.Pair;
import com.wheel.yun.remote.netty.client.NettyClient;
import com.wheel.yun.remote.netty.server.NettyServer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author jack_yun
 * @version 1.0
 * @description: TODO
 * @date 2022/5/31 11:18
 */
public class NettyManager {
    private static Map<Integer, Pair<NettyServer, AtomicInteger>> serverMap = new ConcurrentHashMap<>();
    private static Map<String, Pair<NettyClient, AtomicInteger>> clientMap = new ConcurrentHashMap<>();

    public static NettyServer getNettyServer(Integer port) {
        // 双检锁，任何对象都可以作为锁
        Pair<NettyServer, AtomicInteger> nettyServerIntegerPair = serverMap.get(port);
        if (nettyServerIntegerPair == null) {
            synchronized (serverMap) {
                nettyServerIntegerPair = serverMap.get(port);
                if (nettyServerIntegerPair == null) {
                    NettyServer nettyServer = new NettyServer(port);
                    nettyServerIntegerPair = new Pair<>(nettyServer, new AtomicInteger(0));
                    serverMap.put(port, nettyServerIntegerPair);
                }
            }
        }
        nettyServerIntegerPair.getRight().incrementAndGet();
        return nettyServerIntegerPair.getLeft();
    }
    public static void removeNettyServer(Integer port) {
        Pair<NettyServer, AtomicInteger> nettyServerIntegerPair = serverMap.get(port);
        if (nettyServerIntegerPair != null && nettyServerIntegerPair.getRight().decrementAndGet() == 0) {
            serverMap.remove(port);
            nettyServerIntegerPair.getLeft().close();
        }
    }

    /**
     * 需要计数，如果为0，可以移除，因为可能有多个接口使用同一个ipAndPort
     * @param ipPort
     * @return
     */
    public static NettyClient getNettyClient(String ipPort) {
        // 双检锁，任何对象都可以作为锁
        Pair<NettyClient, AtomicInteger> nettyServerIntegerPair = clientMap.get(ipPort);
        if (nettyServerIntegerPair == null) {
            synchronized (clientMap) {
                nettyServerIntegerPair = clientMap.get(ipPort);
                if (nettyServerIntegerPair == null) {
                    NettyClient nettyServer = new NettyClient(ipPort);
                    nettyServerIntegerPair = new Pair<>(nettyServer, new AtomicInteger(0));
                    clientMap.put(ipPort, nettyServerIntegerPair);
                }
            }
        }
        nettyServerIntegerPair.getRight().incrementAndGet();
        return nettyServerIntegerPair.getLeft();
    }

    public static void removeNettyClient(String ipPort) {
        Pair<NettyClient, AtomicInteger> nettyServerIntegerPair = clientMap.get(ipPort);
        if (nettyServerIntegerPair != null && nettyServerIntegerPair.getRight().decrementAndGet() == 0) {
            clientMap.remove(ipPort);
            nettyServerIntegerPair.getLeft().close();
        }
    }
}
