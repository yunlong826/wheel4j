package com.wheel.yun.test.provider.impl;

import com.wheel.yun.test.api.ImitationTestApi;
import com.wheel.yun.test.api.entity.TestObject;

import java.util.List;

/**
 * @author jack_yun
 * @version 1.0
 * @description:
 * @date 2023-03-21 10:43
 */
public class ImitationTestImpl implements ImitationTestApi {
    @Override
    public String transferStr(String str) {
        return str;
    }

    @Override
    public TestObject transferSingleObject(TestObject t) {
        return t;
    }

    @Override
    public List<TestObject> transferMultipleObject(List<TestObject> list) {
        return list;
    }
}
