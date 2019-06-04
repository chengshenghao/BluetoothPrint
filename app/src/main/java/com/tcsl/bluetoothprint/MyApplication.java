package com.tcsl.bluetoothprint;


import android.support.multidex.MultiDexApplication;


/**
 * 描述：
 * <p/>作者：wu
 * <br/>创建时间：2018/12/11 10:26
 */
public class MyApplication extends MultiDexApplication {
    private static MyApplication instance;

    public static MyApplication getInstance() {
        return instance;
    }

    public static void setInstance(MyApplication instance) {
        MyApplication.instance = instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initLogger();
        setInstance(this);
    }

    /**
     * 初始化logger信息
     */
    private void initLogger() {
    }
}
