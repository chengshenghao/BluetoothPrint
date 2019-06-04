package com.tcsl.bluetoothprint.utils;


import android.content.Context;
import android.content.SharedPreferences;

import com.tcsl.bluetoothprint.MyApplication;


/**
 * 描述:本地保存在xml里的数据
 * <p/>作者：李纬杰
 * <p/>创建时间: 2017/5/5
 */

public class LocalXml {
    private static final String share_xml = "bluetooth";
    private static SharedPreferences per;

    static {
        per = MyApplication.getInstance().getSharedPreferences(share_xml, 0);
    }


    /**
     * 蓝牙打印机地址
     *
     * @return
     */
    public static String getDefaultBluethoothDeviceAddress() {
        return per.getString("default_bluetooth_device_address", "");
    }

    /**
     * 蓝牙打印机地址
     *
     * @param mContext
     * @return
     */
    public static void setDefaultBluetoothDeviceAddress(Context mContext, String value) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(share_xml, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("default_bluetooth_device_address", value);
        editor.apply();
    }

    /**
     * 蓝牙打印机名称
     *
     * @param mContext
     * @return
     */
    public static String getDefaultBluetoothDeviceName(Context mContext) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(share_xml, Context.MODE_PRIVATE);
        return sharedPreferences.getString("default_bluetooth_device_name", "");
    }

    /**
     * 蓝牙打印机名称
     *
     * @param mContext
     * @return
     */
    public static void setDefaultBluetoothDeviceName(Context mContext, String value) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(share_xml, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("default_bluetooth_device_name", value);
        editor.apply();
    }

    /**
     * 网口打印机地址
     *
     * @return
     */
    public static String getDefaultNetAddress() {
        return per.getString("default_net_address", "");
    }

    /**
     * 网口打印机地址
     *
     * @param mContext
     * @return
     */
    public static void setDefaultNetAddress(Context mContext, String value) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(share_xml, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("default_net_address", value);
        editor.apply();
    }

}
