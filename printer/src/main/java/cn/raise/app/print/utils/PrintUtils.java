package cn.raise.app.print.utils;

import android.graphics.Bitmap;

import java.io.OutputStream;

import cn.raise.app.print.PrinterCmd;
import cn.raise.app.print.common.BitmapUtil;
import cn.raise.app.print.model.BitmapModel;
import cn.raise.app.print.model.FakeQrCodeModel;
import cn.raise.app.print.model.QrCodeModel;

import static cn.raise.app.print.IPrinter.PAPER_58;
import static cn.raise.app.print.IPrinter.PAPER_80;
import static cn.raise.app.print.utils.ByteUtils.getBytesASCII;
import static cn.raise.app.print.utils.ByteUtils.getBytesGB2312;

/**
 * 描述：
 * <p/>作者：wu
 * <br/>创建时间：2018/9/11 11:24
 */
public class PrintUtils {
    private static PrinterCmd mPrinterCmd;

    static {
        mPrinterCmd = new PrinterCmd();
    }

    public static void printText(OutputStream mOutputStream, String pszString, int mFontAlign, int mFontSize) {
        try {
            mOutputStream.write(getBytesASCII(mPrinterCmd.textAlign(mFontAlign)));
            mOutputStream.write(getBytesASCII(mPrinterCmd.fontSize(mFontSize)));
            mOutputStream.write(getBytesGB2312(pszString));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void printBitmap(OutputStream mOutputStream, BitmapModel bitmapModel) {
        byte[] bytes = BitmapUtil.addRastBitImage(bitmapModel.getBitmap(), bitmapModel.getWidth(), 0);
        try {
            mOutputStream.write(getBytesASCII(mPrinterCmd.textAlign(1)));
            mOutputStream.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void printQrCode(OutputStream mOutputStream, QrCodeModel model) {
        Bitmap bitmap = BitmapUtil.createQRBitmap(model.getQrCode(), model.getWidth(), model.getHeight(),0);
        byte[] bytes = BitmapUtil.addRastBitImage(bitmap, getCanvasWidth(model.getPaperType()), 0);
        try {
            mOutputStream.write(getBytesASCII(mPrinterCmd.textAlign(1)));
            mOutputStream.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void printFakeQrCode(OutputStream mOutputStream, FakeQrCodeModel model) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int getCanvasWidth(int paperType) {
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

    public static void printSpace(OutputStream mOutputStream, int space) {
        try {
            mOutputStream.write(getBytesASCII(mPrinterCmd.paddingLeft(space)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void printPageGo(OutputStream mOutputStream, int pageGo) {
        try {
            mOutputStream.write(getBytesASCII(mPrinterCmd.pageGo(pageGo)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void printEnter(OutputStream mOutputStream) {
        try {
            mOutputStream.write(getBytesASCII(mPrinterCmd.enter()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void printCut(OutputStream mOutputStream) {
        try {
            mOutputStream.write(getBytesASCII(mPrinterCmd.cutPage()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
