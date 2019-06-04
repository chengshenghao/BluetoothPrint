package com.tcsl.bluetoothprint.print;



import com.tcsl.bluetoothprint.utils.LocalXml;

import java.util.List;

import cn.raise.app.print.common.BluetoothPrinter;
import cn.raise.app.print.common.ICommonPrinter;
import cn.raise.app.print.common.NetPrinter;
import cn.raise.app.print.model.BasePrintModel;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 描述: 打印业务
 * <p/>作者：景阳
 * <p/>创建时间: 2017/4/27 14:08
 */
public class CommonPrintManager {
    private ICommonPrinter currentPrinter;
    private BluetoothPrinter btPrinter;
    private NetPrinter netPrinter;
    private int engine;

    public CommonPrintManager() {
        btPrinter = new BluetoothPrinter();
        netPrinter = new NetPrinter();
        setEngine(LocalXml.getPrinterType());
    }

    /**
     * 切换引擎，关闭上一次使用的引擎
     *
     * @param engine
     */
    public void setEngine(int engine) {
        if (this.engine != engine && currentPrinter != null) {
            currentPrinter.closeConnect();
        }
        this.engine = engine;
        if (engine == QueuePrintManager.ENGINE_BT) {
            currentPrinter = btPrinter;
        } else if (engine == QueuePrintManager.ENGINE_NET) {
            currentPrinter = netPrinter;
        }
    }

    /**
     * 打印数据
     *
     * @param list
     */
    public synchronized void print(final List<BasePrintModel> list, final OnPrinterListener listener) {
        currentPrinter.printList(list, getAddress())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onPrintError(e.getMessage());
                    }

                    @Override
                    public void onNext(String s) {

                    }
                });
    }

    private String getAddress() {
        if (currentPrinter == btPrinter) {
            return LocalXml.getDefaultBluethoothDeviceAddress();
        } else {
            return LocalXml.getDefaultNetAddress();
        }
    }

    /**
     * 关闭连接
     */
    public void closeConnect() {
        if (currentPrinter != null) {
            currentPrinter.closeConnect();
        }
    }
}
