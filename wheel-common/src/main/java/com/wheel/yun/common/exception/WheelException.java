package com.wheel.yun.common.exception;

/**
 * @author jack_yun
 * @version 1.0
 * @description: TODO
 * @date 2022/5/31 13:36
 */
public class WheelException extends RuntimeException{
    public WheelException(){
        super();
    }
    public WheelException(String messages){
        super(messages);
    }
    public WheelException(String message,Throwable cause){
        super(message,cause);
    }
}
