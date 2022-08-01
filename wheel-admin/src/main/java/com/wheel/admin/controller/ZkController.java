package com.wheel.admin.controller;


import com.wheel.admin.dto.ResultDto;
import com.wheel.admin.dto.ZKDto;
import com.wheel.admin.enums.ResultEnumCode;
import com.wheel.admin.exception.RegistryException;
import com.wheel.admin.registry.zk.store.ZkDataStore;
import com.wheel.admin.utils.StringToZKDto;
import com.wheel.admin.wrapper.ResultWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author jack_yun
 * @version 1.0
 * @description: zk 节点的数据展示(存放在内存中)
 * @date 2022/7/30 16:14
 */
@RestController
@RequestMapping("/zk")
@Api(tags = "节点的数据展示")
public class ZkController {


    /**
     *
     *
     *    providerPath:"/wheel/"+group+"&"+clazzNames.get(i)+"&"+version+"&"+"providers"+"/"+ NetUtils.getServerIp() + ":"
     *                     +nettyPort+"@"+serviceBeans.get(i).getLoadbalance()+"_"+serviceBeans.get(i).getWeight()“
     *
     * @return com.wheel.admin.dto.ResultDto<java.util.List<com.wheel.admin.dto.ZKDto>>
     * @author long_yun
     * @date 2022/7/30 16:40
     * @describe
     */

    @ApiOperation(value = "节点的数据展示")
    @GetMapping("/zkLists")
    public ResultDto<List<ZKDto>> zkLists(){
        List<String> zkdata = ZkDataStore.ZKDATA;
        if(zkdata.size() == 0){
            throw new RegistryException(ResultEnumCode.ZKDATA_NULL.getCode()
                                        ,ResultEnumCode.ZKDATA_NULL.getMessage());
        }
        List<ZKDto> list = StringToZKDto.string2ZKDtoLists(zkdata);
        return ResultWrapper.success(list);
    }


}
