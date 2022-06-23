package com.wheel.yun.common;


import com.wheel.yun.common.component.PathURLAddress;
import com.wheel.yun.common.component.URLAddress;
import com.wheel.yun.common.utils.LRUCache;
import com.wheel.yun.common.utils.StringUtils;


import java.io.Serializable;
import java.util.Map;

/**
 * @author jack_yun
 * @version 1.0
 * @description: TODO
 * @date 2022/5/28 19:00
 */
public class URL implements Serializable {
    private static Map<String,URL> cachedURLs = new LRUCache<String, URL>();

    private final URLAddress urlAddress;
    private transient String serviceKey;

    public String getUserName(){
        return urlAddress.getUsername();
    }
    public String getPassWord(){
        return urlAddress.getPassword();
    }
    public String getProtocol(){
        return urlAddress.getProtocol();
    }
    public int getPort(){
        return urlAddress.getPort();
    }
    public String getHost(){
        return urlAddress.getHost();
    }
    public String getPath(){
        return urlAddress.getPath();
    }

    protected URL(){
        this.urlAddress = null;
    }
    public URL(String protocol, String host, int port) {
        this(protocol, null, null, host, port, null);
    }
    public URL(String protocol, String host, int port, String path) {
        this(protocol, null, null, host, port, path);
    }

    public URL(String protocol,
               String username,
               String password,
               String host,
               int port,
               String path) {
        if (StringUtils.isEmpty(username)&&!StringUtils.isEmpty(password)) {
            throw new IllegalArgumentException("Invalid url, password without username!");
        }
        this.urlAddress = new PathURLAddress(protocol, username, password, path, host, port);
    }
    /**
     * parse decoded url string, formatted dubbo://host:port/path?param=value, into strutted URL.
     *
     * @param url, decoded url string
     * @return
     */
    public static URL valueOf(String url) {
        return URLStrParser.parseDecodedStr(url);
    }

}
