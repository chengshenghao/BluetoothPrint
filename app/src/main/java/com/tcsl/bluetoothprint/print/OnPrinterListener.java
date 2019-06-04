package com.tcsl.bluetoothprint.print;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/5/12 16:19
 */
public interface OnPrinterListener {
    void onDeviceNotBound();
    void onConnectFailed();
    void onPrintError(String msg);
}
