package com.wheel.yun.test.consumer.test;

import com.wheel.yun.test.api.ImitationTestApi;
import com.wheel.yun.test.api.entity.TestObject;
import com.wheel.yun.test.api.utils.BuilderObject;
import com.wheel.yun.test.consumer.WheelTestConsumerApplication;
import org.openjdk.jmh.annotations.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author jack_yun
 * @version 1.0
 * @description: 基准测试类
 * @date 2023-03-21 12:25
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
@Warmup(iterations = 5,time = 1,timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 100,time = 1,timeUnit = TimeUnit.SECONDS)
public class TestBenchmark {

    private ImitationTestApi testApi;
    @Setup
    public void setup(){
        WheelTestConsumerApplication w = new WheelTestConsumerApplication();
        this.testApi = WheelTestConsumerApplication.t;
    }
    @Benchmark
    public void testSingleEntity(){
        TestObject testObject = BuilderObject.builderTestObject();
        testApi.transferSingleObject(testObject);
    }

    @Benchmark
    public void testMultipleEntity(){
        List<TestObject> testObjects = BuilderObject.builderTestObjectList(100);
        testApi.transferMultipleObject(testObjects);
    }

    @Benchmark
    public void test1KBString(){
        String string = BuilderObject.createString(1);
        testApi.transferStr(string);
    }

    @Benchmark
    public void test1MBString(){
        String string = BuilderObject.createString(1024);
        testApi.transferStr(string);
    }
}
