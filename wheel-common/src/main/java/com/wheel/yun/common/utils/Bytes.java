package com.wheel.yun.common.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author jack_yun
 * @version 1.0
 * @description: TODO
 * @date 2022/7/4 17:23
 */
public class Bytes {
    private static ThreadLocal<MessageDigest> MD = new ThreadLocal<MessageDigest>();

    /**
     * get md5.
     *
     * @param str input string.
     * @return MD5 byte array.
     */
    public static byte[] getMD5(String str) {
        return getMD5(str.getBytes());
    }

    /**
     * get md5.
     *
     * @param source byte array source.
     * @return MD5 byte array.
     */
    public static byte[] getMD5(byte[] source) {
        MessageDigest md = getMessageDigest();
        return md.digest(source);
    }

    private static MessageDigest getMessageDigest() {
        MessageDigest ret = MD.get();
        if (ret == null) {
            try {
                ret = MessageDigest.getInstance("MD5");
                MD.set(ret);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }
        return ret;
    }
}
