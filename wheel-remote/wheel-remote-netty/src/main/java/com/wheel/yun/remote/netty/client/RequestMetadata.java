package com.wheel.yun.remote.netty.client;



import com.wheel.yun.rpc.common.RpcRequest;
import com.wheel.yun.rpc.protocol.MessageProtocol;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @Classname RequestMetadata
 * @Description 请求元数据
 * @Date 2021/7/30 9:47
 * @Created by wangchangjiu
 */
@Data
@Builder
public class RequestMetadata implements Serializable {

    /**
     *  协议
     */
    private MessageProtocol<RpcRequest> protocol;

    /**
     *  地址
     */
    private String address;

    /**
     *  端口
     */
    private Integer port;

    /**
     *  服务调用超时
     */
    private Integer timeout;

}
