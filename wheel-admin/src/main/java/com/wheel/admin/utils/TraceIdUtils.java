package com.wheel.admin.utils;

import org.apache.skywalking.apm.toolkit.trace.TraceContext;
import org.springframework.util.StringUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

/**
 * Description:
 *
 * @author longyun
 * @version 1.0
 * @date 2022/7/31 14:45
 */
public class TraceIdUtils {
    public static final String X_TRACE_ID = "X-TraceId";
    public static final String X_PARENT_SPAN_ID = "X-ParentSpanId";
    public static final String X_SPAN_ID = "X-SpanId";
    public static final String X_SERVICE_TYPE = "X-Service-Type";
    public static final String X_SERVICE_NAME = "X-Service-Name";
    public static final String X_METHOD_NAME = "X-Method-Name";
    private static final ThreadLocal<String> TRACE_ID_LOCAL = new ThreadLocal();
    private static final ThreadLocal<String> PARENT_SPAN_ID_LOCAL = new ThreadLocal();
    private static final ThreadLocal<String> SPAN_ID_LOCAL = new ThreadLocal();
    private static volatile SnowflakeIdWorker worker;
    private static final Object lock = new Object();

    private TraceIdUtils() {
    }

    private static SnowflakeIdWorker getInstance() {
        SnowflakeIdWorker instance = worker;
        if (instance == null) {
            synchronized(lock) {
                instance = worker;
                if (instance == null) {
                    instance = new SnowflakeIdWorker(getRandomDatacenterId());
                    worker = instance;
                }
            }
        }

        return worker;
    }

    public static String genTraceId() {
        String traceId = genTraceIdNotCached();
        TRACE_ID_LOCAL.set(traceId);
        return traceId;
    }

    public static String genTraceIdWithSW() {
        String tid = TraceContext.traceId();
        if (StringUtils.isEmpty(tid) || "Ignored_Trace".equals(tid) || "N/A".equals(tid)) {
            tid = genTraceIdNotCached();
        }

        TRACE_ID_LOCAL.set(tid);
        return tid;
    }

    public static String genParentSpanId() {
        String traceId = genTraceIdNotCached();
        PARENT_SPAN_ID_LOCAL.set(traceId);
        return traceId;
    }

    public static String genSpanId() {
        String traceId = genTraceIdNotCached();
        SPAN_ID_LOCAL.set(traceId);
        return traceId;
    }

    public static String genTraceIdNotCached() {
        SnowflakeIdWorker worker = getInstance();
        String traceId = String.valueOf(worker.nextId());
        return traceId;
    }

    public static String getTraceId() {
        return (String)TRACE_ID_LOCAL.get();
    }

    public static String getParentSpanId() {
        return (String)PARENT_SPAN_ID_LOCAL.get();
    }

    public static String getSpanId() {
        return (String)SPAN_ID_LOCAL.get();
    }

    public static void setTraceId(String id) {
        TRACE_ID_LOCAL.set(id);
    }

    public static void setParentSpanId(String id) {
        PARENT_SPAN_ID_LOCAL.set(id);
    }

    public static void setSpanId(String id) {
        SPAN_ID_LOCAL.set(id);
    }

    public static void clear() {
        TRACE_ID_LOCAL.remove();
        PARENT_SPAN_ID_LOCAL.remove();
        SPAN_ID_LOCAL.remove();
    }

    static long getRandomDatacenterId() {
        String randomNumber = (new Random()).nextInt(100000) + "";

        try {
            String ipAddress = InetAddress.getLocalHost().getHostAddress();
            long result = 0L;
            String[] ipAddressInArray = ipAddress.split("\\.");

            for(int i = 3; i >= 0; --i) {
                long ip = Long.parseLong(ipAddressInArray[3 - i]);
                result |= ip << i * 8;
            }

            randomNumber = result + "";
        } catch (UnknownHostException var8) {
        }

        int hash = hash(randomNumber);
        return (long)(31 & hash);
    }

    private static final int hash(Object key) {
        int h;
        return key == null ? 0 : (h = key.hashCode()) ^ h >>> 16;
    }
}
