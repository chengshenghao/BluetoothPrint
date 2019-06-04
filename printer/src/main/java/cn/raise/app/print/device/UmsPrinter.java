package cn.raise.app.print.device;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.ums.upos.sdk.printer.OnPrintResultListener;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cn.raise.app.print.common.BitmapUtil;
import cn.raise.app.print.device.ums.PrintItemBean;
import cn.raise.app.print.device.ums.UmsManager;
import cn.raise.app.print.device.ums.UmsPrintItem;
import cn.raise.app.print.model.BasePrintModel;
import cn.raise.app.print.model.BitmapModel;
import cn.raise.app.print.model.LineModel;
import cn.raise.app.print.model.PageEndModel;
import cn.raise.app.print.model.QrCodeModel;
import cn.raise.app.print.model.TextModel;

/**
 * 描述: 银联商务
 * <p/>作者：景阳
 * <p/>创建时间: 2018/2/28 13:49
 */
public class UmsPrinter extends DevicePrinter implements OnPrintResultListener {

    private final UmsManager mUmsDeviceManager;
    private WriteThread writeThread;

    public UmsPrinter(Context context) {
        mUmsDeviceManager = new UmsManager(context);
    }

    @Override
    public boolean closeConnect() {
        mUmsDeviceManager.destroy();
        return true;
    }

    /**
     * 覆盖父类的方法
     *
     * @param list
     * @throws IOException
     */
    @Override
    public synchronized void print(List<BasePrintModel> list) throws IOException {
        if (writeThread == null) {
            writeThread = new WriteThread();
            writeThread.start();
        }
        writeThread.add(list);
    }

    private UmsPrintItem parseTextItem(TextModel model) {
        PrintItemBean.fontSize fontSize;
        switch (model.getFontSize()) {
            case 0:
            case 1:
            case 2:
                fontSize = PrintItemBean.fontSize.normal;
                break;
            case 3:
                fontSize = PrintItemBean.fontSize.big;
                break;
            case 4:
            case 5:
            case 6:
                fontSize = PrintItemBean.fontSize.extraBig;
                break;
            default:
                Log.e("UmsPrintItem", "parseTextItem: " + model.getTextStr());
                fontSize = PrintItemBean.fontSize.big;
                break;
        }
        String text = model.getTextStr();
        if (model.getFontAlign() == 1) {
            text = formatCenter(text, fontSize);
        }
        UmsPrintItem item = new UmsPrintItem(text, fontSize);
        return item;
    }

    @Override
    protected void printText(TextModel model) {

    }

    @Override
    protected void printBitmap(BitmapModel model) {

    }

    @Override
    protected void printQrCode(QrCodeModel model) {

    }

    @Override
    protected void printPageEnd(PageEndModel basePrintModel) {

    }

    /**
     * 居中格式化
     *
     * @param text 文字内容
     * @param size 字号（normal，big）
     * @return
     */
    protected String formatCenter(String text, PrintItemBean.fontSize size) {
        int charCount = 0;
        try {
            charCount = text.getBytes("gbk").length;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        int spaceCount = 0;
        switch (size) {
            case normal:
                spaceCount = 32 - charCount;
                break;
            case big:
                spaceCount = 24 - charCount;
                break;
            case extraBig:
                spaceCount = 16 - charCount;
                break;
            default:
                spaceCount = 32 - charCount;
                break;
        }
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < spaceCount / 2; i++) {
            buf.append(" ");
        }
        buf.append(text);
        return buf.toString();
    }

    @Override
    public void onPrintResult(int i) {
        if (writeThread != null) {
            synchronized (writeThread) {
                writeThread.notify();
            }
        }
        Log.e("UmsPrinter", "onPrintResult " + i);
        switch (i) {
            //打印成功
            case 0:
                printOk();
                break;
            case -1004:
                printError("打印机繁忙");
                break;
            case -1005:
                printError("打印机缺纸");
                break;
            case -1007:
                printError("打印机故障");
                break;
            case -1008:
                printError("打印机过热");
                break;
            case -1009:
                printError("打印未完成");
                break;
            default:
                printError("打印失败");
                break;
        }
    }

    /**
     * 描述:线程，遍历将要打印的数据
     *
     * @author wjx
     * @date 2018/8/2 12:33
     */
    private class WriteThread extends Thread {
        List<List<BasePrintModel>> printList = new ArrayList<>();
        private boolean flag = true;

        public void add(List<BasePrintModel> list) {
            printList.add(list);
        }

        @Override
        public void run() {
            while (flag) {
                while (printList.size() > 0) {
                    List<BasePrintModel> listInfos = printList.get(0);
                    try {
                        final List<UmsPrintItem> list = new ArrayList<>();
                        for (BasePrintModel basePrintModel : listInfos) {
                            if (basePrintModel instanceof TextModel) {
                                list.add(parseTextItem((TextModel) basePrintModel));
                            } else if (basePrintModel instanceof LineModel) {
                                LineModel lineModel = (LineModel) basePrintModel;
                                TextModel tm = new TextModel(0, 0, lineModel.getLine());
                                list.add(parseTextItem(tm));
                            } else if (basePrintModel instanceof BitmapModel) {
                                BitmapModel bm = (BitmapModel) basePrintModel;
                                UmsPrintItem item = new UmsPrintItem(bm.getBitmap());
                                item.setBitmapBottom(20);
                                list.add(item);
                            } else if (basePrintModel instanceof QrCodeModel) {
                                QrCodeModel model = (QrCodeModel) basePrintModel;
                                Bitmap bitmap = BitmapUtil.createQRBitmap(model.getQrCode(), model.getWidth(), model.getHeight(), 10);
                                UmsPrintItem item = new UmsPrintItem(bitmap);
                                list.add(item);
                            } else if (basePrintModel instanceof PageEndModel) {
                                list.add(new UmsPrintItem("  ", PrintItemBean.fontSize.normal));
                            }
                        }
                        mUmsDeviceManager.print(list, UmsPrinter.this);
                        synchronized (this) {//线程锁定，打印完成或者失败进行通知，然后进行遍历打印。
                            wait();
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    printList.remove(0);
                }
            }
        }
    }
}
