package com.wheel.yun.rpc.common;



import lombok.Data;

import java.io.Serializable;

/**
 * @Author: changjiu.wang
 * @Date: 2021/7/24 22:56
 */
@Data
public class RpcResponse implements Serializable {

    private Object result;
    private String message;

    private long id;
    private boolean isException;
    private boolean isEvent;
//    public Object getData() {
//        return data;
//    }
//
//    public void setData(Object data) {
//        this.data = data;
//    }
//
//    public String getMessage() {
//        return message;
//    }
//
//    public void setMessage(String message) {
//        this.message = message;
//    }
}
