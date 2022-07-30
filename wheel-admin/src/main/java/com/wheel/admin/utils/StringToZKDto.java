package com.wheel.admin.utils;

import com.wheel.admin.dto.ZKDto;

import java.util.ArrayList;
import java.util.List;

/**
 * providerPath:"/wheel/"+group+"&"+clazzNames.get(i)+"&"+version+"&"+"providers"+"/"+ NetUtils.getServerIp() + ":"
 *                      +nettyPort+"@"+serviceBeans.get(i).getLoadbalance()+"_"+serviceBeans.get(i).getWeight()
 * @author jack_yun
 * @version 1.0
 * @description: String ---> ZkDto
 * @date 2022/7/30 16:30
 */
public class StringToZKDto {
    public static List<ZKDto> string2ZKDtoLists(List<String> strs){
        List<ZKDto> list = new ArrayList<>();
        for(String s:strs){
            list.add(string2ZKDto(s));
        }
        return list;
    }
    public static ZKDto string2ZKDto(String str){
        str = str.substring(1);
        int idx = str.lastIndexOf("/");
        String lastStr = str.substring(idx+1);
        str = str.substring(0,idx);
        String[] split = str.split("&");
        String group = split[0];
        String clazzName = split[1];
        String version = split[2];
        String ipAndPort = lastStr.substring(0,lastStr.indexOf("@"));
        String loadBalance = lastStr.substring(lastStr.indexOf("@")+1,lastStr.indexOf("_"));
        String weight = lastStr.substring(lastStr.indexOf("_")+1);
        ZKDto zkDto = ZKDto.builder()
                .group(group)
                .clazzName(clazzName)
                .ipAndPort(ipAndPort)
                .loadBalance(loadBalance)
                .version(version)
                .weight(weight)
                .build();
        return zkDto;
    }
}
