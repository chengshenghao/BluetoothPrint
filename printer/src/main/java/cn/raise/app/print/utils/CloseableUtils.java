package cn.raise.app.print.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by Administrator on 2017/3/19.
 */

public class CloseableUtils {

    public static void closeable(Closeable closeable) {
        if (null != closeable) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
