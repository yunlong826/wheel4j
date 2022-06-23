package com.wheel.yun.rpc.protocol;



import java.io.Serializable;

/**
 * @Classname MessageProtocol
 * @Description 消息协议
 * @Date 2021/7/23 15:33
 * @Created by wangchangjiu
 */

public class MessageProtocol<T> implements Serializable {

    /**
     *  消息头
     */
    private MessageHeader header;

    /**
     *  消息体
     */
    private T body;

    public MessageHeader getHeader() {
        return header;
    }

    public void setHeader(MessageHeader header) {
        this.header = header;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }
}
