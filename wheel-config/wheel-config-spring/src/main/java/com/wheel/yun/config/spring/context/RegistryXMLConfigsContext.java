package com.wheel.yun.config.spring.context;

import com.wheel.yun.common.utils.Pair;
import com.wheel.yun.config.spring.util.RegistryXMLConfigs;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 *
 * @author longyun
 * @version 1.0
 * @date 2022/8/3 10:01
 */
public class RegistryXMLConfigsContext {
    private static List<Pair<String,Class>> ConfigsContext = new ArrayList<>();

    public static List<Pair<String,Class>> getConfigsContext(){
        RegistryXMLConfigsContext.ConfigsContext.addAll(RegistryXMLConfigs.getRegistryXMLConfigs());
        return RegistryXMLConfigsContext.ConfigsContext;
    }




}
