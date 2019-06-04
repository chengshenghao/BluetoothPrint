package cn.raise.app.print.common1;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import cn.raise.app.print.PrinterCmd;
import cn.raise.app.print.model.BasePrintModel;
import cn.raise.app.print.model.BitmapModel;
import cn.raise.app.print.model.FakeQrCodeModel;
import cn.raise.app.print.model.LineModel;
import cn.raise.app.print.model.PageEndModel;
import cn.raise.app.print.model.QrCodeModel;
import cn.raise.app.print.model.TextModel;

import static cn.raise.app.print.utils.ByteUtils.getBytesASCII;
import static cn.raise.app.print.utils.ByteUtils.getBytesGB2312;

/**
 * 描述: 蓝牙、网口外接通用打印机
 * <p/>作者：景阳
 * <p/>创建时间: 2017/4/25 16:52
 */
public abstract class CommonPrinter implements ICommonPrinter, Handler.Callback {

    private final int CANVAS_58 = 370;
    private final int CANVAS_80 = 600;

    private final int MSG_CONNECT_SUCCESS = 1;
    private final int MSG_CONNECT_ERROR = 2;
    private final int MSG_NO_DEVICE = 3;
    private final int MSG_PRINT_ERROR = 4;
    private final int MSG_PRINT_CMD_DONE = 5;

    protected OutputStream mOutputStream;
    protected PrinterCmd mPrinterCmd;
    protected PrintCallback printCallback;

    private Handler mHandler;

    public CommonPrinter() {
        mPrinterCmd = new PrinterCmd();
        mHandler = new Handler(this);
    }

    public void setPrintCallback(PrintCallback printCallback) {
        this.printCallback = printCallback;
    }

    @Override
    public void openConnect() {
        new Thread() {
            public void run() {
                boolean result = connectToDevice();
                mHandler.sendEmptyMessage(result ? MSG_CONNECT_SUCCESS : MSG_CONNECT_ERROR);
            }
        }.start();
    }

    protected abstract boolean connectToDevice();

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_CONNECT_ERROR:
                printCallback.onConnectFailed();
                break;
            case MSG_CONNECT_SUCCESS:
                printCallback.onConnectSuccess();
                break;
            case MSG_NO_DEVICE:
                printCallback.onDeviceNotBound();
                break;
            case MSG_PRINT_CMD_DONE:
                printCallback.onPrintCmdOver();
                break;
        }
        return false;
    }

    @Override
    public void print(final List<BasePrintModel> basePrintModel) {
        new Thread() {
            public void run() {
                for (BasePrintModel model : basePrintModel) {
                    try {
                        print(model);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                mHandler.sendEmptyMessage(MSG_PRINT_CMD_DONE);
            }
        }.start();
    }

    @Override
    public void print(BasePrintModel basePrintModel) throws IOException {
        if (basePrintModel.getSpace() > 0) {
            mOutputStream.write(getBytesASCII(mPrinterCmd.paddingLeft(basePrintModel.getSpace())));
        }

        if (basePrintModel instanceof TextModel) {
            TextModel textModel = (TextModel) basePrintModel;
            printText(textModel.getTextStr(), textModel.getFontAlign(), textModel.getFontSize());
        } else if (basePrintModel instanceof LineModel) {
            printText(((LineModel) basePrintModel).getLine(), 0, 0);
        } else if (basePrintModel instanceof BitmapModel) {
            printBitmap((BitmapModel) basePrintModel);
        } else if (basePrintModel instanceof QrCodeModel) {
            printQrCode((QrCodeModel) basePrintModel);
        } else if (basePrintModel instanceof FakeQrCodeModel) {
            printFakeQrCode((FakeQrCodeModel) basePrintModel);
        } else if (basePrintModel instanceof PageEndModel) {
            // 通用蓝牙、网口打印机经测试3就够了
            basePrintModel.setPageGo(3);
        }
        if (basePrintModel.getPageGo() > 0) {
            mOutputStream.write(getBytesASCII(mPrinterCmd.pageGo(basePrintModel.getPageGo())));
        }
        if (basePrintModel.isEnter()) {
            mOutputStream.write(getBytesASCII(mPrinterCmd.enter()));
        }
        if (basePrintModel.isCutPage()) {
            mOutputStream.write(getBytesASCII(mPrinterCmd.cutPage()));
        }
        mOutputStream.flush();
    }

    private void printFakeQrCode(FakeQrCodeModel model) {
        int moduleSize = 8;
        try {
            mOutputStream.write(new StringBuffer().append((char) 27).append((char) 97)
                    .append((char) 1).toString().getBytes("ASCII"));
            mOutputStream.write(new StringBuffer().append((char) 29).append((char) 119)
                    .append((char) 3).toString().getBytes("ASCII"));
            int length = model.getQrCode().getBytes("utf-8").length;
            //打印二维码矩阵
            mOutputStream.write(0x1D);// init
            mOutputStream.write("(k".getBytes());// adjust height of barcode
            mOutputStream.write(length + 3); // pl
            mOutputStream.write(0); // ph
            mOutputStream.write(49); // cn
            mOutputStream.write(80); // fn
            mOutputStream.write(48); // m
            mOutputStream.write(model.getQrCode().getBytes());

            mOutputStream.write(0x1D);
            mOutputStream.write("(k".getBytes());
            mOutputStream.write(3);//pl
            mOutputStream.write(0);//ph
            mOutputStream.write(49);//cn
            mOutputStream.write(69);//fn
            mOutputStream.write(48);//m

            mOutputStream.write(0x1D);
            mOutputStream.write("(k".getBytes());
            mOutputStream.write(3);
            mOutputStream.write(0);
            mOutputStream.write(49);
            mOutputStream.write(67);
            mOutputStream.write(moduleSize);

            mOutputStream.write(0x1D);
            mOutputStream.write("(k".getBytes());
            mOutputStream.write(3); // pl
            mOutputStream.write(0); // ph
            mOutputStream.write(49); // cn
            mOutputStream.write(81); // fn
            mOutputStream.write(48); // m
            mOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param pszString  打印的数据
     * @param mFontAlign 0:居左 1:居中 2:居右
     * @param mFontSize  字体大小0:正常大小 1:两倍高 2:两倍宽 3:两倍大小 4:三倍高 5:三倍宽 6:三倍大小 7:四倍高 8:四倍宽 9:四倍大小 10:五倍高 11:五倍宽 12:五倍大小
     */
    private void printText(String pszString, int mFontAlign, int mFontSize) {
        try {
            mOutputStream.write(getBytesASCII(mPrinterCmd.textAlign(mFontAlign)));
            mOutputStream.write(getBytesASCII(mPrinterCmd.fontSize(mFontSize)));
            mOutputStream.write(getBytesGB2312(pszString));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打印图片
     *
     * @param bitmapModel
     */
    public void printBitmap(BitmapModel bitmapModel) {
        byte[] bytes = BitmapUtil.addRastBitImage(bitmapModel.getBitmap(), bitmapModel.getWidth(), 0);
        try {
            mOutputStream.write(getBytesASCII(mPrinterCmd.textAlign(1)));
            mOutputStream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打印二维码
     *
     * @param model
     */
    public void printQrCode(QrCodeModel model) {
        Bitmap bitmap = BitmapUtil.createQRBitmap(model.getQrCode(), model.getWidth(), model.getHeight());
        byte[] bytes = BitmapUtil.addRastBitImage(bitmap, getCanvasWidth(model.getPaperType()), 0);
        try {
            mOutputStream.write(getBytesASCII(mPrinterCmd.textAlign(1)));
            mOutputStream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getCanvasWidth(int paperType) {
        int width;
        switch (paperType) {
            case PAPER_80:
                width = 200;
                break;
            case PAPER_58:
            default:
                width = 200;
                break;
        }
        return width;
    }
    /**
     * 暂时关闭,因为蓝牙每次打印后并不会关闭连接，而网口打印机每次都关闭连接，所以通过这个方法来进行不同处理
     */
    public abstract void momentClose();
}
