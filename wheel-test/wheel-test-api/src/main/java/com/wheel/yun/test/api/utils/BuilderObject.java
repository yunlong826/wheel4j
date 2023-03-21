package com.wheel.yun.test.api.utils;

import com.wheel.yun.test.api.entity.TestObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jack_yun
 * @version 1.0
 * @description: 建造对象工具类
 * @date 2023-03-21 10:46
 */
public class BuilderObject {
    private static TestObject testObject = new TestObject();
    private static List<TestObject> list = new ArrayList<>();
    private static String s1k = "";
    private static String s1M = "";
    public static String createString(int sizeInKB) {
        if(sizeInKB == 1 && s1k.length() != 0){
            return s1k;
        }
        if(sizeInKB == 1024 && s1M.length() != 0)
            return s1M;
        int sizeInBytes = sizeInKB * 1024;
        StringBuilder sb = new StringBuilder(sizeInBytes);
        while (sb.length() < sizeInBytes) {
            sb.append("abcdefghijklmnopqrstuvwxyz");
        }
        if(sizeInKB == 1){
            s1k = sb.substring(0, sizeInBytes);
            return s1k;
        }else if(sizeInKB == 1024){
            s1M = sb.substring(0, sizeInBytes);
            return s1M;
        }
        return "";
    }

    // 构造测试对象，多例模式
    public static TestObject builderTestObject(){
        return testObject;
    }

    // 构造多个测试对象(集合)，多例模式
    public static List<TestObject> builderTestObjectList(int nums){
        if(nums<=0)
            return null;
        if(list.size()>0)
            return list;
        for(int i = 1;i<=nums;i++){
            list.add(new TestObject());
        }
        return list;
    }
}
