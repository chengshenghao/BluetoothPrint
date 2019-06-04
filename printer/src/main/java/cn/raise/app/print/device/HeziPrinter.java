package cn.raise.app.print.device;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.RemoteException;
import android.util.Log;

import com.iboxpay.print.IPrintJobStatusCallback;
import com.iboxpay.print.PrintManager;
import com.iboxpay.print.model.CharacterParams;
import com.iboxpay.print.model.GraphParams;
import com.iboxpay.print.model.PrintItemJobInfo;
import com.iboxpay.print.model.PrintJobInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.raise.app.print.model.BasePrintModel;
import cn.raise.app.print.model.BitmapModel;
import cn.raise.app.print.model.LineModel;
import cn.raise.app.print.model.PageEndModel;
import cn.raise.app.print.model.QrCodeModel;
import cn.raise.app.print.model.TextModel;
import cn.raise.app.print.utils.QRCodeUtil;

/**
 * 描述: 盒子
 * <p/>作者：景阳
 * <p/>创建时间: 2018/3/1 9:53
 */
public class HeziPrinter extends DevicePrinter {

    /**
     * 经调试，盒子的总长大概是BITMAP_WIDTH
     */
    private final int BITMAP_WIDTH = 190;

    private HZPrintListener hzPrintListener;
    private PrintManager mPrinter;
    private WriteThread writeThread;

    @SuppressLint("WrongConstant")
    public HeziPrinter(Context context) {
        mPrinter = (PrintManager) context.getSystemService("iboxpay_print");
        hzPrintListener = new HZPrintListener();
    }

    @Override
    public boolean closeConnect() {
        return false;
    }

    /**
     * 覆盖父类方法
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

    /**
     * 将图片缩放并居中
     *
     * @param bm
     * @return
     */
    private Bitmap scaleIcon(Bitmap bm) {
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float max = 80 * 80;
        float scale = 1;
        if (width * height > max) {
            scale = max / ((float) width * height);
            scale = (float) Math.sqrt(scale);
        }
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
                true);

        return centerBitmap(newbm);
    }

    /**
     * 图片居中
     *
     * @param bitmap
     * @return
     */
    private Bitmap centerBitmap(Bitmap bitmap) {
        Bitmap bg = Bitmap.createBitmap(BITMAP_WIDTH, bitmap.getHeight(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bg);
        //画布背景白色
        canvas.drawColor(Color.WHITE);
        // 将图片居中
        float left = (BITMAP_WIDTH - bitmap.getWidth()) / 2;
        canvas.drawBitmap(bitmap, left, 0, new Paint());
        return bg;
    }

    /**
     * GraphParams(int width, int height)  构造一个有width height 的打印图片的参数，width、height单位是像素，会根据设置的值进行相应压缩，“第三方”需要根据自己的要求进行微调。
     *
     * @param receiptTask
     * @param bitmap
     */
    private void addBitmapItem(PrintJobInfo receiptTask, Bitmap bitmap) {
        GraphParams params = new GraphParams(bitmap.getWidth(), bitmap.getHeight());
        receiptTask.addPrintItemJobTask(new PrintItemJobInfo(bitmap, params));
    }

    /**
     * CharacterParams(fontWidth, fontHeight, fontAlign)
     * 文字大小调整说明： 文字大小采用的是：1*1，1*2，2*1，2*2四种比例大小，即：宽*高。默认为1*1小字体，大字体通常为2*2。不支持设置具体字体大小的方式
     *
     * @param receiptTask
     * @param model
     */
    private void addTextModel(PrintJobInfo receiptTask, TextModel model) {
        int fontWidth = 2;
        int fontHeight;
        int fontAlign = model.getFontAlign();
        if (model.getFontSize() >= 3) {
            fontHeight = 2;
            Log.e("addTextModel", "addTextModel: " + model.getTextStr());
        } else {
            fontHeight = 1;
        }
        receiptTask.addPrintItemJobTask(new PrintItemJobInfo(model.getTextStr() + "\n", new CharacterParams(fontWidth, fontHeight, fontAlign)));
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

    private class HZPrintListener extends IPrintJobStatusCallback.Stub {

        /**
         * status - 任务状态 0:打印任务完成 -1：打印任务失败 4：打印任务的时候出现缺纸 1:打印任务开始；taskId - 任务ID
         *
         * @param status
         * @param taskId
         * @throws RemoteException
         */
        @Override
        public void onPrintJobStatusChange(int status, String taskId) throws RemoteException {
            Log.e("HeziPrinter", "onPrintJobStatusChange " + status);
            // taskId:任务ID
            switch (status) {
                case 1:
                    break;
                case 0:
                    if (writeThread != null) {
                        synchronized (writeThread) {//通知下一个打印数据
                            writeThread.notify();
                        }
                    }
                    printOk();
                    break;
                case 4:
                    if (writeThread != null) {
                        synchronized (writeThread) {
                            writeThread.notify();
                        }
                    }
                    printError("打印缺纸");
                    break;
                default:
                    if (writeThread != null) {
                        synchronized (writeThread) {
                            writeThread.notify();
                        }
                    }
                    printError("打印任务失败");
                    break;
            }
        }
    }

    @Override
    public void unBind(Context context) {
        super.unBind(context);
        if (writeThread != null) {
            writeThread.flag = false;
            writeThread = null;
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
                    List<BasePrintModel> list = printList.get(0);
                    try {
                        PrintJobInfo receiptTask = new PrintJobInfo();
                        for (BasePrintModel basePrintModel : list) {
                            if (basePrintModel instanceof TextModel) {
                                TextModel model = (TextModel) basePrintModel;
                                addTextModel(receiptTask, model);
                            } else if (basePrintModel instanceof LineModel) {
                                LineModel lineModel = (LineModel) basePrintModel;
                                // 盒子的比较特殊
                                TextModel model = new TextModel(0, 0
                                        , "- - - - - - - - - - - - - - - - - - - - - - - - - - - -");
                                addTextModel(receiptTask, model);
                            } else if (basePrintModel instanceof BitmapModel) {
                                Bitmap bitmap = scaleIcon(((BitmapModel) basePrintModel).getBitmap());
                                addBitmapItem(receiptTask, bitmap);
                                receiptTask.addPrintItemJobTask(new PrintItemJobInfo("\n", new CharacterParams()));
                            } else if (basePrintModel instanceof QrCodeModel) {
                                // 经调试，padding并不完全有用，测试几个数值后，size=170是最理想的几乎没有padding，160,150边距很大
                                int size = 130;
                                QrCodeModel qm = (QrCodeModel) basePrintModel;
                                Bitmap bitmap = QRCodeUtil.createQRBitmap(qm.getQrCode(), size, size, null, 0);
                                bitmap = centerBitmap(bitmap);
                                addBitmapItem(receiptTask, bitmap);
                            } else if (basePrintModel instanceof PageEndModel) {

                            }
                        }
                        // 走纸
                        receiptTask.addPrintItemJobTask(new PrintItemJobInfo("\n", new CharacterParams()));
                        receiptTask.addPrintItemJobTask(new PrintItemJobInfo("\n", new CharacterParams()));
                        receiptTask.addPrintItemJobTask(new PrintItemJobInfo("\n", new CharacterParams()));
                        mPrinter.printLocaleJob(receiptTask, hzPrintListener);
                        synchronized (this) {//线程锁定，打印完成或者失败进行通知，然后进行遍历打印。
                            wait();
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    printList.remove(0);
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void setFlag(boolean flag) {
            this.flag = flag;
        }
    }
}
