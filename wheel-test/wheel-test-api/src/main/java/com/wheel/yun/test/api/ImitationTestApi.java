package com.wheel.yun.test.api;

import com.wheel.yun.test.api.entity.TestObject;

import java.util.List;

/**
 * @author jack_yun
 * @version 1.0
 * @description: 模仿测试的接口
 * @date 2023-03-21 10:38
 */
public interface ImitationTestApi {
    // 传输字符串，可以设置不同大小的字符串进行传输
    String transferStr(String str);

    // 传输单个测试对象
    TestObject transferSingleObject(TestObject t);

    List<TestObject> transferMultipleObject(List<TestObject> list);
}
