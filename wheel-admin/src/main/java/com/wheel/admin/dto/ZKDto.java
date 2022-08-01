package com.wheel.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jack_yun
 * @version 1.0
 * @description:
 * @date 2022/7/30 16:23
 * providerPath:"/wheel/"+group+"&"+clazzNames.get(i)+"&"+version+"&"+"providers"+"/"+ NetUtils.getServerIp() + ":"
 *                     +nettyPort+"@"+serviceBeans.get(i).getLoadbalance()+"_"+serviceBeans.get(i).getWeight()
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ZKDto {
    private String group;
    private String clazzName;
    private String version;
    private String ipAndPort;
    private String loadBalance;
    private String weight;
}
