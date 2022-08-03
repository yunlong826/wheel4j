package com.wheel.yun.config.spring.util;

import com.wheel.yun.common.utils.Pair;
import com.wheel.yun.config.spring.enums.ConfigStrategyEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 *
 * @author longyun
 * @version 1.0
 * @date 2022/8/2 21:55
 */
public  class RegistryXMLConfigs {
    public  static List<Pair<String,Class>> getRegistryXMLConfigs(){
        int len = ConfigStrategyEnum.values().length;
        List<Pair<String,Class>> pairs = new ArrayList<>();
        for(int i = 0;i<len; i++) {
            pairs.add(new Pair<>(ConfigStrategyEnum.values()[i].getConfigString()
                    , ConfigStrategyEnum.values()[i].getConfigClass()));
        }
        return pairs;
    }
}
