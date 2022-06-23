package com.wheel.yun.common;

/**
 * @author jack_yun
 * @version 1.0
 * @description: TODO
 * @date 2022/5/28 19:14
 */
public class URLStrParser {
    /**
     *
     *
     * @param url
     * @return com.wheel.ctgu.common.URL
     * @author long_yun
     * @date 2022/5/29 13:25
     * @describe parse decoded url string, formatted protocol://username:password@host:port/path?k1=v1&k2=v2
     */

    public static URL parseDecodedStr(String url) {
        String protocol = url.substring(0,url.indexOf(":"));
        url = url.substring(url.indexOf("//")+2);
        String username = "";
        String password = "";
        String host = url.substring(0,url.indexOf(":"));
        url = url.substring(url.indexOf(":")+1);
        String port = url.substring(url.indexOf("/")+1);
        String path = "";
        URL url1 = new URL(protocol,username,password,host,Integer.valueOf(port),path);
        return url1;

    }
}
