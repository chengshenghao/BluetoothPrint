package com.tcsl.bluetoothprint.utils;

import android.content.Context;
import android.widget.Toast;

import com.tcsl.bluetoothprint.MyApplication;


/**
 * Toast统一管理类
 * create by wjx on 2016年11月8日 18:32:38
 */
public class Tip {
    private static Toast mToast;
    private static boolean isShow = true;

    private Tip() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 短时间显示Toast,请用showShort(CharSequence message)来代替
     *
     * @param context
     * @param message
     */
    @Deprecated
    public static void showShort(Context context, CharSequence message) {
        if (isShow) {
            if (mToast == null) {
                mToast = Toast.makeText(MyApplication.getInstance().getApplicationContext(), "", Toast.LENGTH_SHORT);
            }
            mToast.setText(message);
            mToast.show();
        }
    }
    /**
     * 短时间显示Toast
     *
     * @param message
     */
    public static void showShort(CharSequence message) {
        showShort(MyApplication.getInstance().getApplicationContext(),message);
    }

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showShort(Context context, int message) {
        if (isShow) {
            if (mToast == null) {
                mToast = Toast.makeText(MyApplication.getInstance().getApplicationContext(), "", Toast.LENGTH_SHORT);
            }
            mToast.setText(message);
            mToast.show();
        }

    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showLong(Context context, CharSequence message) {
        if (isShow) {
            if (mToast == null) {
                mToast = Toast.makeText(MyApplication.getInstance().getApplicationContext(), "", Toast.LENGTH_LONG);
            }
            mToast.setText(message);
            mToast.show();
        }
    }

    /**
     * 长时间显示Toast
     *
     * @param message
     */
    public static void showLong(int message) {
        if (isShow) {
            if (mToast == null) {
                mToast = Toast.makeText(MyApplication.getInstance().getApplicationContext(), "", Toast.LENGTH_LONG);
            }
            mToast.setText(message);
            mToast.show();
        }
    }

    /**
     * 自定义显示Toast时间
     *
     * @param message
     * @param duration
     */
    public static void show(CharSequence message, int duration) {
        if (isShow) {
            if (mToast == null) {
                mToast = Toast.makeText(MyApplication.getInstance().getApplicationContext(), "", duration);
            }
            mToast.setText(message);
            mToast.show();
        }
    }

    /**
     * 自定义显示Toast时间
     *
     * @param message
     * @param duration
     */
    public static void show(int message, int duration) {
        if (isShow) {
            if (mToast == null) {
                mToast = Toast.makeText(MyApplication.getInstance().getApplicationContext(), "", duration);
            }
            mToast.setText(message);
            mToast.show();
        }
    }
}
