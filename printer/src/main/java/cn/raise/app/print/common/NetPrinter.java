package cn.raise.app.print.common;

import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
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
 * 描述: 网口打印
 * <p/>作者：景阳
 * <p/>创建时间: 2017/4/25 16:52
 */
public class NetPrinter implements ICommonPrinter {

    /**
     * 通用的网口打印机，打印端口都是9100
     */
    private final int PORT = 9100;
    private Socket socket;
    private OutputStream mOutputStream;
    private WriteThread mThread;
    private String mAddress;

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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void closeConnect() {
        Log.i("[Print]NetPrinter", "closeConnect");
        if (mThread != null) {
            mThread.flag = false;
            mThread = null;
        }
        mAddress = null;
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            socket = null;
        }
    }

    @Override
    public Observable<String> printList(final List<BasePrintModel> list, final String address) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                if (TextUtils.isEmpty(address)) {
                    throw new RuntimeException("未绑定可用打印机");
                } else {
                    try {
                        if (mAddress != null && !mAddress.equals(address)) {
                            //地址切换了,关闭原有连接
                            if (socket != null) {
                                try {
                                    socket.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        if (!address.equals(mAddress)) {
                            //建立新的连接
                            socket = new Socket(address, PORT);
                            mOutputStream = socket.getOutputStream();
                            mAddress = address;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new RuntimeException("打印机连接失败");
                    }
                    if (mThread == null) {
                        mThread = new WriteThread();
                        mThread.start();
                    }
                    mThread.add(list);
                    subscriber.onNext("");
                    subscriber.onCompleted();
                }
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
                while (printList.size() > 0) {
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
