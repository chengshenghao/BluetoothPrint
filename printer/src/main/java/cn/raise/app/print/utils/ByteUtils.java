package cn.raise.app.print.utils;

import java.nio.charset.Charset;

/**
 * Created by Administrator on 2017/3/19.
 */

public class ByteUtils {

    public static byte[] getBytesGB2312(String str) {
        return str.getBytes(Charset.forName("GB2312"));
    }

    public static byte[] getBytesASCII(String str) {
        return str.getBytes(Charset.forName("ASCII"));
    }
}
