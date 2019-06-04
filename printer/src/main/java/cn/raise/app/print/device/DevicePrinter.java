package cn.raise.app.print.device;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.raise.app.print.IPrinter;
import cn.raise.app.print.common.PrintCallback;
import cn.raise.app.print.model.BasePrintModel;
import cn.raise.app.print.model.BitmapModel;
import cn.raise.app.print.model.LineModel;
import cn.raise.app.print.model.PageEndModel;
import cn.raise.app.print.model.QrCodeModel;
import cn.raise.app.print.model.TextModel;

/**
 * 描述: 特殊设备
 * <p/>作者：景阳
 * <p/>创建时间: 2017/5/11 17:01
 */
public abstract class DevicePrinter implements IPrinter {

    private PrintCallback printCallback;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private WriteThread mThread;

    public void print(final BasePrintModel basePrintModel) {
        if (basePrintModel instanceof TextModel) {
            printText((TextModel) basePrintModel);
        } else if (basePrintModel instanceof LineModel) {
            LineModel lineModel = (LineModel) basePrintModel;
            TextModel model = new TextModel(0, 0, lineModel.getLine());
            printText(model);
        } else if (basePrintModel instanceof BitmapModel) {
            printBitmap((BitmapModel) basePrintModel);
        } else if (basePrintModel instanceof QrCodeModel) {
            printQrCode((QrCodeModel) basePrintModel);
        } else if (basePrintModel instanceof PageEndModel) {
            printPageEnd((PageEndModel) basePrintModel);
        }
    }

    public void print(final List<BasePrintModel> basePrintModel) throws IOException {
        if (mThread == null) {
            mThread = new WriteThread();
            mThread.start();
        }
        mThread.add(basePrintModel);
    }

    protected abstract void printText(TextModel model);

    protected abstract void printBitmap(BitmapModel model);

    protected abstract void printQrCode(QrCodeModel model);

    protected abstract void printPageEnd(PageEndModel basePrintModel);


    protected abstract boolean closeConnect();

    public void setPrintCallback(PrintCallback callback) {
        this.printCallback = callback;
    }

    protected void notSupport() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
            }
        });
    }

    protected void printError(final String str) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (printCallback != null) {
                    printCallback.onPrintError(str);
                }
            }
        });
    }

    protected void printOk() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (printCallback != null) {
                    printCallback.onPrintCmdOver();
                }
            }
        });
    }

    protected void printNotBound() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (printCallback != null) {
                    printCallback.onDeviceNotBound();
                }
            }
        });
    }

    public void unBind(Context context) {
        if (mThread != null) {
            mThread.flag = false;
            mThread = null;
        }
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
