package com.wheel.yun.remote.netty.client;

import java.util.concurrent.*;

/**
 *  结果异步返回
 * @Author: changjiu.wang
 * @Date: 2021/7/25 14:23
 */
public class RpcFuture<T> implements Future<T> {

    /**
     *  响应结果
     */
    private T response;

    /**
     * 因为请求和响应是一一对应的，所以这里是1
     * 使用 CountDownLatch 等待线程
     */
    private CountDownLatch countDownLatch = new CountDownLatch(1);


    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    /**
     *  响应数据不为空 表示完成
     * @return
     */
    @Override
    public boolean isDone() {
        return this.response != null;
    }

    /**
     *  等待获取数据，直到有结果 也就是 countDownLatch 的值减到 0
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     */
    @Override
    public T get() throws InterruptedException {
        // 进入阻塞等待 countDownLatch减少值为0 返回下面结果
        countDownLatch.await();
        return response;
    }

    /**
     *  超时等待 获取数据
     * @param timeout
     * @param unit
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws TimeoutException
     */
    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException {
        if (countDownLatch.await(timeout,unit)){
            return response;
        }
        return null;
    }


    public void setResponse(T response){
        this.response = response;
        countDownLatch.countDown();
    }
}
