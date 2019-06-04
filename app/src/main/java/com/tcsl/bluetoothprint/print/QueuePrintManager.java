package com.tcsl.bluetoothprint.print;

import android.content.Context;

import com.tcsl.bluetoothprint.beans.PrintBean;
import com.tcsl.bluetoothprint.utils.LocalXml;

import java.util.List;

import cn.raise.app.print.model.BasePrintModel;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 描述: 打印管理，外界直接调用
 * <p/>作者：景阳
 * <p/>创建时间: 2017/5/12 17:00
 */
public class QueuePrintManager {

    private static QueuePrintManager instance;

    public static final int ENGINE_BT = 1;
    public static final int ENGINE_NET = 2;
    public static final int ENGINE_DEVICE = 3;

    private int engineType =1 ;

    private CommonPrintManager commonPrintManager;
    private DevicePrintManager devicePrintManager;

    private QueuePrintManager() {
        commonPrintManager = new CommonPrintManager();
        devicePrintManager = new DevicePrintManager();
        init();
    }

    public static synchronized QueuePrintManager getInstance() {
        if (instance == null) {
            instance = new QueuePrintManager();
        }
        return instance;
    }

    public void init() {
        int engine = LocalXml.getPrinterType();
        engineType = engine;
        switch (engine) {
            case ENGINE_BT:
            case ENGINE_NET:
                commonPrintManager.setEngine(engine);
                break;
            case ENGINE_DEVICE:
                break;
        }
        // 非普通设备
        /*if (BuildConfig.DEVICE_TYPE != DeviceUtil.BUILD_CONFIG_NORMAL) {
            // 商米需要绑定service
            if (BuildConfig.DEVICE_TYPE == DeviceUtil.BUILD_CONFIG_SUNMI
                    || BuildConfig.DEVICE_TYPE == DeviceUtil.BUILD_CONFIG_WANGPOS
                    || BuildConfig.DEVICE_TYPE == DeviceUtil.BUILD_CONFIG_UMS
                    || BuildConfig.DEVICE_TYPE == DeviceUtil.BUILD_CONFIG_HEZI
                    || BuildConfig.DEVICE_TYPE == DeviceUtil.BUILD_CONFIG_LKL) {
                devicePrintManager.initEngine(context);
            }
        }*/
    }

    public void unbind(Context context) {
        if (engineType == ENGINE_DEVICE) {
            devicePrintManager.unbind(context);
        }
    }

    public int getEngineType() {
        return engineType;
    }

    public void switchEngine(int engineType) {
        this.engineType = engineType;
        LocalXml.setPrinterType(engineType);
        if (engineType == ENGINE_DEVICE) {
        } else {
            commonPrintManager.setEngine(engineType);
        }
    }

    public boolean isPrinting() {
        return false;
    }

    /**
     * 打印自定义内容
     *
     * @param list
     * @param listener
     */
    public void print(List<BasePrintModel> list, OnPrinterListener listener) {
        if (engineType == ENGINE_DEVICE) {
            devicePrintManager.print(list, listener);
        } else {
            commonPrintManager.print(list, listener);
        }
    }

    /**
     * 取号打印取票内容
     *
     * @param printBean
     * @param listener
     */
    public void print(PrintBean printBean, OnPrinterListener listener) {
        printQueue(printBean, listener);
    }

    /**
     * 测试打印
     *
     * @param printBean
     * @param listener
     */
    public void testPrint(PrintBean printBean, OnPrinterListener listener) {
        printQueue(printBean, listener);
    }

    /**
     * 打印取票内容
     *
     * @param printBean
     * @param listener
     */
    private void printQueue(final PrintBean printBean, final OnPrinterListener listener) {
        getPrintList(printBean).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<BasePrintModel>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<BasePrintModel> basePrintModels) {
                        if (engineType == ENGINE_DEVICE) {
                            devicePrintManager.print(basePrintModels, listener);
                        } else {
                            commonPrintManager.print(basePrintModels, listener);
                        }
                    }
                });
    }

    private Observable<List<BasePrintModel>> getPrintList(final PrintBean bean) {
        return Observable.create(new Observable.OnSubscribe<List<BasePrintModel>>() {
            @Override
            public void call(Subscriber<? super List<BasePrintModel>> subscriber) {
                subscriber.onNext(PrintUtils.getTextList(bean));
                subscriber.onCompleted();
            }
        });
    }

    public void closeConnect() {
        if (commonPrintManager != null) {
            commonPrintManager.closeConnect();
        }
    }
}
