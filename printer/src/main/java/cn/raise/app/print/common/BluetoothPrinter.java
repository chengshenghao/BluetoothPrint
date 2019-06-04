package cn.raise.app.print.common;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import cn.raise.app.print.model.BasePrintModel;
import cn.raise.app.print.model.BitmapModel;
import cn.raise.app.print.model.FakeQrCodeModel;
import cn.raise.app.print.model.LineModel;
import cn.raise.app.print.model.PageEndModel;
import cn.raise.app.print.model.QrCodeModel;
import cn.raise.app.print.model.TextModel;
import cn.raise.app.print.utils.PrintUtils;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by Administrator on 2017/3/19.
 */

public class BluetoothPrinter implements ICommonPrinter {
    private final static String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";
    /**
     * bluetooth service
     */
    private BtService mBtService;
    private String mAddress;
    private BluetoothAdapter mAdapter;
    private OutputStream mOutputStream;
    private WriteThread mThread;

    public BluetoothPrinter() {
        if (null == mAdapter) {
            mAdapter = BluetoothAdapter.getDefaultAdapter();
        }
    }

    public void print(BasePrintModel basePrintModel) {
        if (basePrintModel.getSpace() > 0) {
            PrintUtils.printSpace(mOutputStream, basePrintModel.getSpace());
        }
        if (basePrintModel instanceof TextModel) {
            TextModel textModel = (TextModel) basePrintModel;
            PrintUtils.printText(mOutputStream, textModel.getTextStr(), textModel.getFontAlign(), textModel.getFontSize());
        } else if (basePrintModel instanceof LineModel) {
            PrintUtils.printText(mOutputStream, ((LineModel) basePrintModel).getLine(), 0, 0);
        } else if (basePrintModel instanceof BitmapModel) {
            PrintUtils.printBitmap(mOutputStream, (BitmapModel) basePrintModel);
        } else if (basePrintModel instanceof QrCodeModel) {
            PrintUtils.printQrCode(mOutputStream, (QrCodeModel) basePrintModel);
        } else if (basePrintModel instanceof FakeQrCodeModel) {
            PrintUtils.printFakeQrCode(mOutputStream, (FakeQrCodeModel) basePrintModel);
        } else if (basePrintModel instanceof PageEndModel) {
            // 通用蓝牙、网口打印机经测试3就够了
            basePrintModel.setPageGo(3);
        }
        if (basePrintModel.getPageGo() > 0) {
            PrintUtils.printPageGo(mOutputStream, basePrintModel.getPageGo());
        }
        if (basePrintModel.isEnter()) {
            PrintUtils.printEnter(mOutputStream);
        }
        if (basePrintModel.isCutPage()) {
            PrintUtils.printCut(mOutputStream);
        }
        try {
            mOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void closeConnect() {
        Log.e("[Print]BluetoothPrinter", "closeConnect");
        try {
            if (mBtService != null) {
                mBtService.stop();
                mBtService = null;
            }
            if (mThread != null) {
                mThread.flag = false;
                mThread = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Observable<String> printList(final List<BasePrintModel> list, final String btAddress) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                if (TextUtils.isEmpty(btAddress)) {
                    throw new RuntimeException("未绑定打印机");
                }
                if (null == mBtService) {
                    mBtService = new BtService();
                }
                if (!btAddress.equals(mAddress) || mBtService.getState() != BtService.STATE_CONNECTED) {
                    //地址切换了,或者当前状态为未连接状态
                    mAddress = btAddress;
                    BluetoothDevice device = mAdapter.getRemoteDevice(btAddress);
                    mBtService.connect(device);
                    for (int i = 0; i < 50; i++) {
                        if (mBtService.getState() == BtService.STATE_CONNECTED) {
                            mOutputStream = mBtService.getmConnectedThread().getMmOutStream();
                            break;
                        }
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if (mBtService.getState() != BtService.STATE_CONNECTED) {
                        throw new RuntimeException("打印机连接失败");
                    }
                }
                if (mThread == null) {
                    mThread = new WriteThread();
                    mThread.start();
                }
                mThread.add(list);
            }
        });
    }

    /**
     * 描述:线程，遍历将要打印的数据
     *
     * @author wjx
     * @date 2018/8/2 12:33
     */
    private class WriteThread extends Thread {
        List<BasePrintModel> printList = new ArrayList<>();
        private boolean flag = true;

        public void add(List<BasePrintModel> list) {
            printList.addAll(list);
        }

        @Override
        public void run() {
            while (flag) {
                Log.d("WriteThread", "run: " + "外层循环");
                while (printList.size() > 0) {
                    Log.d("WriteThread", "run: " + "内层循环");
                    print(printList.get(0));
                    printList.remove(0);
                }
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        public void setFlag(boolean flag) {
            this.flag = flag;
        }
    }
}
