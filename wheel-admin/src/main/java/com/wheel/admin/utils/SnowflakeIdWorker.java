package com.wheel.admin.utils;

import com.baomidou.mybatisplus.core.toolkit.SystemClock;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Description: 雪花算法生成工具类
 * 详细原理,可阅读该文章: https://blog.csdn.net/jiaomubai/article/details/124385324
 *
 * @author longyun
 * @version 1.0
 * @date 2022/7/31 14:46
 */
public class SnowflakeIdWorker {
    private static final Logger log = LoggerFactory.getLogger(SnowflakeIdWorker.class);
    private static final long TWEPOCH = 1288834974657L;
    private static final long WORKER_ID_BITS = 5L;
    private static final long DATACENTER_ID_BITS = 5L;
    private static final long MAX_WORKER_ID = 31L;
    private static final long MAX_DATACENTER_ID = 31L;
    private static final long SEQUENCE_BITS = 12L;
    private static final long WORKER_ID_SHIFT = 12L;
    private static final long DATACENTER_ID_SHIFT = 17L;
    private static final long TIMESTAMPLE_LEFT_SHIFT = 22L;
    private static final long SEQUENCE_MASK = 4095L;
    private final long workerId;
    private final long datacenterId;
    private long sequence = 0L;
    private long lastTimestamp = -1L;

    public SnowflakeIdWorker() {
        this.datacenterId = getDatacenterId(31L);
        this.workerId = getMaxWorkerId(this.datacenterId, 31L);
    }

    public SnowflakeIdWorker(long workerId, long datacenterId) {
        if (workerId <= 31L && workerId >= 0L) {
            if (datacenterId <= 31L && datacenterId >= 0L) {
                this.workerId = workerId;
                this.datacenterId = datacenterId;
            } else {
                throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", 31L));
            }
        } else {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", 31L));
        }
    }

    public SnowflakeIdWorker(long datacenterId) {
        if (datacenterId <= 31L && datacenterId >= 0L) {
            this.datacenterId = datacenterId;
            this.workerId = getMaxWorkerId(datacenterId, 31L);
        } else {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", 31L));
        }
    }

    protected static long getMaxWorkerId(long datacenterId, long maxWorkerId) {
        StringBuilder mpid = new StringBuilder();
        mpid.append(datacenterId);
        String name = ManagementFactory.getRuntimeMXBean().getName();
        if (StringUtils.isNotBlank(name)) {
            mpid.append(name.split("@")[0]);
        }

        return (long)(mpid.toString().hashCode() & '\uffff') % (maxWorkerId + 1L);
    }

    protected static long getDatacenterId(long maxDatacenterId) {
        long id = 0L;

        try {
            InetAddress ip = InetAddress.getLocalHost();
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            if (network == null) {
                id = 1L;
            } else {
                byte[] mac = network.getHardwareAddress();
                if (null != mac) {
                    id = (255L & (long)mac[mac.length - 1] | 65280L & (long)mac[mac.length - 2] << 8) >> 6;
                    id %= maxDatacenterId + 1L;
                }
            }
        } catch (Exception var7) {
            log.warn(" getDatacenterId: " + var7.getMessage());
        }

        return id;
    }

    public synchronized long nextId() {
        long timestamp = this.timeGen();
        if (timestamp < this.lastTimestamp) {
            long offset = this.lastTimestamp - timestamp;
            if (offset > 5L) {
                throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", offset));
            }

            try {
                this.wait(offset << 1);
                timestamp = this.timeGen();
                if (timestamp < this.lastTimestamp) {
                    throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", offset));
                }
            } catch (Exception var6) {
                throw new RuntimeException(var6);
            }
        }

        if (this.lastTimestamp == timestamp) {
            this.sequence = this.sequence + 1L & 4095L;
            if (this.sequence == 0L) {
                timestamp = this.tilNextMillis(this.lastTimestamp);
            }
        } else {
            this.sequence = ThreadLocalRandom.current().nextLong(1L, 3L);
        }

        this.lastTimestamp = timestamp;
        return timestamp - 1288834974657L << 22 | this.datacenterId << 17 | this.workerId << 12 | this.sequence;
    }

    protected long tilNextMillis(long lastTimestamp) {
        long timestamp;
        for(timestamp = this.timeGen(); timestamp <= lastTimestamp; timestamp = this.timeGen()) {
        }

        return timestamp;
    }

    protected long timeGen() {
        return SystemClock.now();
    }
}
