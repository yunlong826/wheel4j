package com.wheel.admin.utils;

import com.wheel.admin.dto.ZKDto;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jack_yun
 * @version 1.0
 * @description: String ---> ZkDto
 * @date 2022/7/30 16:30
 */
public class StringToZKDto {
    public static List<ZKDto>  string2ZKDtoLists(List<String> strs){
        List<ZKDto> list = new ArrayList<>();
        for(String s:strs){
            list.add(string2ZKDto(s));
        }
        return list;
    }
    public static ZKDto string2ZKDto(String str){
        str = str.substring(1);
        String[] split = str.split("/");
        String group = split[0];
        String clazzName = split[1];
        String ipAndPort = split[3].substring(0,split[3].indexOf("@"));
        String loadBalance = split[3].substring(split[3].indexOf("@")+1,split[3].indexOf("_"));
        String weight = split[3].substring(split[3].indexOf("_")+1);
        ZKDto zkDto = ZKDto.builder()
                .group(group)
                .clazzName(clazzName)
                .ipAndPort(ipAndPort)
                .loadBalance(loadBalance)
                .weight(weight)
                .build();
        return zkDto;
    }
}
