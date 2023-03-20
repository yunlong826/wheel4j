/*
 * Copyright (c) 2011-2018, Meituan Dianping. All Rights Reserved.
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wheel.yun.json;



import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.ParserConfig;
import com.wheel.serial.api.RpcSerialization;
import com.wheel.yun.common.exporter.WheelExporter;
import com.wheel.yun.common.invoker.RpcInvocation;
import com.wheel.yun.rpc.common.DefaultFuture;
import com.wheel.yun.rpc.common.RpcRequest;
import com.wheel.yun.rpc.common.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletableFuture;



/**
 * @Author: changjiu.wang
 * @Date: 2021/7/4 13:36
 */
@Slf4j
public class JsonSerialization implements RpcSerialization {

    private static ParserConfig parserConfig = new ParserConfig();

    @Override
    public <T> byte[] serialize(T obj) throws IOException {
        try {
            // 序列化时，把类信息带上，保证泛化信息也带上，正常的话需要带上的
            // 例如Map<String, UserParam>，则value也会带上UserParam信息，可以反序列化成功
            //         String jsonStr = JSON.toJSONString(msg, SerializerFeature.WriteClassName);
            String jsonStr = JSON.toJSONString(obj);
            byte[] wordBytes = jsonStr.getBytes(StandardCharsets.UTF_8.name());
            return wordBytes;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("serial error", e);
        }
    }

    @Override
    public <T> T deserialize(byte[] realData, Class<T> target) throws IOException {
        try {
            String realStr = new String(realData, StandardCharsets.UTF_8.name());
            // 反序列化时，支持识别传递的类，保证有泛型信息时也能成功，例如Map<String, UserParam>，则value的类型是UserParam，而不是JSONObject
            T data = JSON.parseObject(realStr, target, parserConfig);
            if (target == RpcRequest.class) {
                RpcRequest request = ((RpcRequest)data);
                if (!request.isEvent()) {
                    RpcInvocation rpcInvocation = request.getRpcInvocation();
                    Object obj = WheelExporter.getService(rpcInvocation);
                    if (obj != null) {
                        Method method = obj.getClass().getMethod(rpcInvocation.getMethodName(), rpcInvocation.getParameterType());
                        Object[] newArgs = convert(rpcInvocation.getArgs(), method.getParameterTypes(), method.getGenericParameterTypes());
                        rpcInvocation.setArgs(newArgs);
                    }
                }
            } else if (target == RpcResponse.class) {
                RpcResponse response = ((RpcResponse)data);
                if (!response.isEvent()) {
                    DefaultFuture.getRequest(response.getId()).ifPresent(
                            request -> {
                                Method method = request.getRpcInvocation().getMethod();
                                // todo CompletableFuture这里跟序列化无关，需要提到外层
                                // 对于返回future类型，服务端返回的是具体的值，这里客户端需要特殊处理下
                                if (CompletableFuture.class.isAssignableFrom(method.getReturnType())) {
                                    Type genericType = method.getGenericReturnType();
                                    Type valueType = getGenericClassByIndex(genericType, 0);
                                    // CompletableFuture<ValueType>
                                    Object newResult;
                                    if (valueType != null) {
                                        newResult = convert(response.getResult(), null, valueType);
                                    } else {
                                        // 没有泛型信息，不做处理，依赖@type实现反序列化正确
                                        newResult = response.getResult();
                                    }
                                    response.setResult(newResult);
                                } else {
                                    Object newResult = convert(response.getResult(), method.getReturnType(), method.getGenericReturnType());
                                    response.setResult(newResult);
                                }
                            }
                    );
                }
            }
            return data;
        } catch (Exception e) {
            throw new RuntimeException("deserialize error", e);
        }
    }


    /**
     * 参数对象在json反序列化时，得到的是JSONObject/JSONArray/int/string等对象，
     * 原始参数信息已经丢失，例如UserParam/List<UserParam>，需要根据class转换回原来的才能反射调用
     *
     * 这里支持第一层collection/map，大于一层不支持，
     * 例如不支持list<list<UserParam>>，map<string, map<string, userparam>
     * @param args 参数值
     * @param clazzTypes 方法上的参数类型，原来json序列后的参数类型已经丢失泛型类型
     * @param genericTypes  用于获取参数类型，带有泛型类型
     * @return
     */
    public Object[] convert(Object[] args, Class<?>[] clazzTypes, Type[] genericTypes) {
        if (args == null || args.length == 0) {
            return args;
        }
        Object[] result = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            result[i] = convert(args[i], clazzTypes[i], genericTypes[i]);
        }
        return result;
    }

    /**
     * 常见类型可以序列化，其他的自定义序列化器
     * @param arg
     * @param clazzType
     * @param genericType
     * @return
     */
    public Object convert(Object arg, Class clazzType, Type genericType) {
        if (arg == null || clazzType == Void.class) {
            return null;
        }
        if (clazzType == null) {
            if (genericType instanceof Class) {
                clazzType = (Class)genericType;
            } else if (genericType instanceof ParameterizedType) {
                // todo 一定是class?
                clazzType = (Class)((ParameterizedType)genericType).getRawType();
            } else {
                clazzType = arg.getClass();
            }
        }
        // 通过上面第一次转换后，如果还是JSON对象，则需要进一步转化，如果确实传递的是JSONObject，存在重复操作
        if (arg instanceof JSONObject) {
            if (Map.class.isAssignableFrom(clazzType)) {
                Type keyType = getGenericClassByIndex(genericType, 0);
                Type valueType = getGenericClassByIndex(genericType, 1);
                Map oldMap = (Map)arg;
                Map newMap = createMap(clazzType, oldMap.size());
                oldMap.forEach((key, value) -> {
                    Object newKey = convert(key, null, keyType);
                    Object newValue = convert(value, null, valueType);
                    newMap.put(newKey, newValue);
                });
                return newMap;
            } else if (clazzType.isInterface()) {
                // todo 如果是接口，泛化调用的话，使用的是jdk代理。这里暂时使用autotype，但是provider未必有这个类，可能会报错
                return arg;
            } else {
                // 如果类型是hashMap，会报错，http://blog.kail.xyz/post/2019-06-02/other/json.toJavaObject.html
                // result[i] = ((JSONObject) arg).toJavaObject(clazzType);
                // beanUtils.copy???MapStruct
                // 复杂对象也可能带有泛型信息
                Object dest;
                if (genericType != null) {
                    dest = JSON.parseObject(JSON.toJSONString(arg), genericType);
                } else {
                    dest = JSON.parseObject(JSON.toJSONString(arg), clazzType);
                }
                return dest;
            }
        } else if (arg instanceof JSONArray) {
            // 需要看类型是array/Collection
            if (clazzType.isArray()) {
                Class elementType = clazzType.getComponentType();
                int len = ((JSONArray)arg).size();
                Object dest = Array.newInstance(elementType, len);

                Type elementGenericType = null;
                if (genericType instanceof GenericArrayType) {
                    elementGenericType = ((GenericArrayType)genericType).getGenericComponentType();
                }
                for (int i = 0; i < len; i++) {
                    Object newResult = convert(((JSONArray)arg).get(i), elementType, elementGenericType);
                    Array.set(dest, i, newResult);
                }
                return dest;
            } else if (Collection.class.isAssignableFrom(clazzType)) {
                Type elementType = getGenericClassByIndex(genericType, 0);
                Collection collection = createCollection(clazzType, ((JSONArray)arg).size());
                ((JSONArray)arg).forEach(element -> {
                    Object newResult = convert(element, null, elementType);
                    collection.add(newResult);
                });
                return collection;
            } else {
                throw new RuntimeException("不可反序列化的数组类型：" + clazzType.getName());
            }
        } else if (arg instanceof String && clazzType.isEnum()) {
            return Enum.valueOf((Class<Enum>) clazzType, (String) arg);
        } else {
            // 原始类型
            return arg;
        }
    }

    private static Type getGenericClassByIndex(Type genericType, int index) {
        // 泛型已经被擦除，这里通过方法来获取参数的泛型类型
        Type clazz = null;
        // find parameterized type
        if (genericType instanceof ParameterizedType) {
            ParameterizedType t = (ParameterizedType) genericType;
            // todo 这个一定是class??
            log.info("原来类型：{}", t.getRawType());
            Type[] types = t.getActualTypeArguments();
            for(Type actualTypeArgument: types) {
                log.info("泛型类型：{}", actualTypeArgument);
            }
            clazz = types[index];
        }
        return clazz;
    }

    protected static Collection<Object> createCollection(Class<? extends Collection> collectionType, int initialCapacity) {
        if (!collectionType.isInterface()) {
            try {
                return collectionType.newInstance();
            }
            catch (Exception ex) {
                throw new IllegalArgumentException(
                        "Could not instantiate collection class [" + collectionType.getName() + "]: " + ex.getMessage());
            }
        }
        else if (List.class == collectionType) {
            return new ArrayList<Object>(initialCapacity);
        }
        else if (SortedSet.class == collectionType) {
            return new TreeSet<Object>();
        }
        else {
            return new LinkedHashSet<Object>(initialCapacity);
        }
    }

    protected static Map<Object, Object> createMap(Class<? extends Map> mapType, int initialCapacity) {
        if (!mapType.isInterface()) {
            try {
                return mapType.newInstance();
            }
            catch (Exception ex) {
                throw new IllegalArgumentException(
                        "Could not instantiate map class [" + mapType.getName() + "]: " + ex.getMessage());
            }
        }
        else if (SortedMap.class == mapType) {
            return new TreeMap<Object, Object>();
        }
        else {
            return new LinkedHashMap<Object, Object>(initialCapacity);
        }
    }
}
