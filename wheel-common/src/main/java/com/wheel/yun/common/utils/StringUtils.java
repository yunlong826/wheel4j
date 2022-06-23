package com.wheel.yun.common.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author jack_yun
 * @version 1.0
 * @description: TODO
 * @date 2022/5/31 13:34
 */
public class StringUtils {
    public static String toString(Throwable e) {
        StringWriter w = new StringWriter();
        PrintWriter p = new PrintWriter(w);
        p.print(e.getClass().getName());
        if (e.getMessage() != null) {
            p.print(": " + e.getMessage());
        }
        p.println();
        try {
            e.printStackTrace(p);
            return w.toString();
        } finally {
            p.close();
        }
    }

    public static boolean isEmpty(String s) {
        if(s == null||s.length() == 0)
            return true;
        return false;
    }
}
