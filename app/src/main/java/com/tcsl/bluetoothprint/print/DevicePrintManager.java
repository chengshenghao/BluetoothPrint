package com.tcsl.bluetoothprint.print;

import android.content.Context;

import java.util.List;

import cn.raise.app.print.BuildConfig;
import cn.raise.app.print.common.PrintCallback;
import cn.raise.app.print.device.DevicePrinter;
import cn.raise.app.print.device.HeziPrinter;
import cn.raise.app.print.device.LklPrinter;
import cn.raise.app.print.device.SunmiPrinter;
import cn.raise.app.print.device.UmsPrinter;
import cn.raise.app.print.device.WangPosPrinter;
import cn.raise.app.print.model.BasePrintModel;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/5/12 16:59
 */
public class DevicePrintManager {

    private DevicePrinter printer;

    public void initEngine(Context context) {
//        switch (BuildConfig.DEVICE_TYPE) {
//            case DeviceUtil.BUILD_CONFIG_SUNMI:
//                printer = new SunmiPrinter();
//                ((SunmiPrinter) printer).bindSMService(context);
//                break;
//            case DeviceUtil.BUILD_CONFIG_WANGPOS:
//                printer = new WangPosPrinter();
//                ((WangPosPrinter) printer).bindWangPos();
//                break;
//            case DeviceUtil.BUILD_CONFIG_UMS:
//                printer = new UmsPrinter(context);
//                break;
//            case DeviceUtil.BUILD_CONFIG_HEZI:
//                printer = new HeziPrinter(context);
//                break;
//            case DeviceUtil.BUILD_CONFIG_LKL:
//                printer = new LklPrinter(context);
//                break;
//            default:
//                break;
//        }
    }

    public void unbind(Context context) {
        printer.unBind(context);
    }

    public void print(final List<BasePrintModel> list, final OnPrinterListener listener) {
        if (printer == null) {
            if (listener != null) {
                listener.onDeviceNotBound();
            }
            return;
        }
        printer.setPrintCallback(new PrintCallback() {
            @Override
            public void onDeviceNotBound() {
                if (listener != null) {
                    listener.onDeviceNotBound();
                }
            }

            @Override
            public void onConnectSuccess() {
            }

            @Override
            public void onConnectFailed() {
                if (listener != null) {
                    listener.onConnectFailed();
                }
            }

            @Override
            public void onPrintCmdOver() {

            }

            @Override
            public void onPrintError(String msg) {
                if (listener != null) {
                    listener.onPrintError(msg);
                }
            }
        });
        try {
            printer.print(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
