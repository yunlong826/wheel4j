package com.wheel.yun.test.consumer;

import com.wheel.yun.test.api.ImitationTestApi;
import com.wheel.yun.test.api.entity.TestObject;
import com.wheel.yun.test.api.utils.BuilderObject;
import com.wheel.yun.test.consumer.test.TestBenchmark;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;



public class WheelTestConsumerApplication {

    private static ApplicationContext applicationContext = new ClassPathXmlApplicationContext("wheel-test-consumer.xml");
    public static ImitationTestApi t = applicationContext.getBean("imitationTestApi",ImitationTestApi.class);

    public static void main(String[] args) throws RunnerException {

        TestObject testObject = BuilderObject.builderTestObject();
        BuilderObject.builderTestObjectList(100);     // 创建该方法，避免该方法在创建对象时而耗时
        BuilderObject.createString(1);
        BuilderObject.createString(100);
        t.transferSingleObject(testObject);   // 防止首次连接的开销

        Options opt = new OptionsBuilder()
                .resultFormat(ResultFormatType.JSON)
                .output("D:\\USER\\dubbo\\wheel4j\\jmhlog.json")
                .include(TestBenchmark.class.getSimpleName()+".*")
                .forks(4)
                .build();
        new Runner(opt).run();
//        // 在fastJson的序列化下进行测试
//        List<Float> fastJson = testFastJson(testApi,100,1,1024);
//        System.out.println("fastJson序列化下，传输单个对象平均耗时时间为:"+fastJson.get(0)+"毫秒 "+"传输100个对象平均耗时时间为"+fastJson.get(1)+"毫秒 "+
//                "传输1KB字符串平均耗时时间为:"+fastJson.get(2)+"毫秒 "+"传输1MB字符串平均耗时时间为:"+fastJson.get(3)+"毫秒 ");
//        List<Float> protoBuf = testProtobuf(testApi,100,1,1024);
//        System.out.println("protoBuf序列化下，传输单个对象平均耗时时间为:"+protoBuf.get(0)+"毫秒 "+"传输100个对象平均耗时时间为"+protoBuf.get(1)+"毫秒 "+
//                "传输1KB字符串平均耗时时间为:"+protoBuf.get(2)+"毫秒 "+"传输1MB字符串平均耗时时间为:"+protoBuf.get(3)+"毫秒 ");
//        System.out.println();
    }

//    private static List<Float> testFastJson(ImitationTestApi testApi,int loop,int sizeInKB1,int sizeInKB2){
//        List<Float> data = new ArrayList<>();
//        data.add(testSingleEntity(testApi,100));
//        data.add(testMultipleEntity(testApi,100));
//        data.add(testString(testApi,100,sizeInKB1));
//        data.add(testString(testApi,100,sizeInKB2));
//        return data;
//    }

//    private static List<Float> testProtobuf(ImitationTestApi testApi,int loop,int sizeInKB1,int sizeInKB2){
//        List<Float> data = new ArrayList<>();
//        data.add(testSingleEntity(testApi,100));
//        data.add(testMultipleEntity(testApi,100));
//        data.add(testString(testApi,100,sizeInKB1));
//        data.add(testString(testApi,100,sizeInKB2));
//        return data;
//    }

    // 测试传输单个测试对象时的耗时平均时间
//    private static float testSingleEntity(ImitationTestApi testApi,int loop){
//        long startMill,endMill;
//        TestObject testObject = BuilderObject.builderTestObject();
//
//        startMill = System.currentTimeMillis();
//        TestObject returnObject = null;
//        for(int i = 1;i<=loop;i++){
//            returnObject = testApi.transferSingleObject(testObject);
//        }
//        endMill = System.currentTimeMillis();
//        return ((endMill-startMill)/(float)loop);
//    }

    // 测试传输多个测试对象(集合)时的耗时平均时间
//    private static float testMultipleEntity(ImitationTestApi testApi,int loop){
//        long startMill,endMill;
//        List<TestObject> testObjects = BuilderObject.builderTestObjectList(100);
//
//        startMill = System.currentTimeMillis();
//        List<TestObject> returnObjects = null;
//        for(int i = 1;i<=loop;i++){
//            returnObjects = testApi.transferMultipleObject(testObjects);
//        }
//        endMill = System.currentTimeMillis();
//        return ((endMill-startMill)/(float)loop);
//    }
//
//    // 测试指定大小字符串时的耗时平均时间
//    private static float testString(ImitationTestApi testApi,int loop,int sizeInKB){
//        long startMill,endMill;
//        String string = BuilderObject.createString(sizeInKB);
//
//        startMill = System.currentTimeMillis();
//        String returnStr = null;
//        for(int i = 1;i<=loop;i++){
//            returnStr = testApi.transferStr(string);
//        }
//        endMill = System.currentTimeMillis();
//        return ((endMill-startMill)/(float)loop);
//    }
}
