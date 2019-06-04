package com.tcsl.bluetoothprint.utils;


import android.content.Context;
import android.content.SharedPreferences;

import com.tcsl.bluetoothprint.MyApplication;
import com.tcsl.bluetoothprint.print.QueuePrintManager;

import cn.raise.app.print.IPrinter;


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

    /**
     * 获取默认打印机类型
     */
    public static int getPrinterType() {
        return per.getInt("printer_type", QueuePrintManager.ENGINE_BT);
    }

    /**
     * 设置默认打印机类型
     */
    public static void setPrinterType(int mode) {
        per.edit().putInt("printer_type", mode).commit();
    }

    /**
     * 获取打印纸宽度
     */
    public static int getPrintPaperMode() {
        return per.getInt("print_paper_width", IPrinter.PAPER_58);
    }

    /**
     * 设置打印纸宽度
     */
    public static void setPrintPaperMode(Context context, int mode) {
        SharedPreferences sp;
        sp = context.getSharedPreferences(share_xml, 0);
        sp.edit().putInt("print_paper_width", mode).commit();
    }

    /**
     * 企业LOGO开关
     */
    public static boolean getLogoSwitch() {
        return per.getBoolean("logo_switch", false);
    }

    /**
     * 企业LOGO开关
     */
    public static void setLogoSwitch(Context context, boolean isOn) {
        SharedPreferences sp;
        sp = context.getSharedPreferences(share_xml, 0);
        sp.edit().putBoolean("logo_switch", isOn).commit();
    }

    /**
     * 优惠引导语开关
     */
    public static boolean getPreferSwitch() {
        return per.getBoolean("prefer_switch", false);
    }

    /**
     * 优惠引导语开关
     */
    public static void setPreferSwitch(Context context, boolean isOn) {
        SharedPreferences sp;
        sp = context.getSharedPreferences(share_xml, 0);
        sp.edit().putBoolean("prefer_switch", isOn).commit();
    }

    /**
     * 打印二维码的类型
     *
     * @param b true:二维码方式   false:图片方式
     */
    public static void setPrintQrType(boolean b) {
        per.edit().putBoolean("PrintQrType", b).apply();
    }

    /**
     * 打印二维码的类型
     */
    public static boolean getPrintQrType() {
        return false;
    }

    /**
     * 广告语开关
     */
    public static boolean getAdvertiseSwitch() {
        return per.getBoolean("advertise_switch", false);
    }

    /**
     * 广告语开关
     */
    public static void setAdvertiseSwitch(Context context, boolean isOn) {
        per.edit().putBoolean("advertise_switch", isOn).commit();
    }
}
