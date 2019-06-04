package cn.raise.app.print.device;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.ByteArrayOutputStream;

import cn.raise.app.print.model.BitmapModel;
import cn.raise.app.print.model.PageEndModel;
import cn.raise.app.print.model.QrCodeModel;
import cn.raise.app.print.model.TextModel;
import cn.weipass.pos.sdk.IPrint;
import cn.weipass.pos.sdk.Printer;
import cn.weipass.pos.sdk.impl.WeiposImpl;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/7/17 11:39
 */
public class WangPosPrinter extends DevicePrinter {

    private final String TAG = "WangPosPrinter";

    /**
     * 旺Pos打印驱动
     */
    private Printer wpPrinterDev = null;

    /**
     * 旺pos打印回调
     */
    private WPossPrintListener wangPrintListener = null;

    /**
     * 旺pos小字体时存放的字符的个数
     */
    private final int smallSize = 24 * 2;
    /**
     * 旺pos中度字体能存放的字符的个数
     */
    private final int mediumSize = 16 * 2;
    /**
     * 旺pos大字体时存放的字符的个数
     */
    private final int largeSize = 12 * 2;
    /**
     * 旺pos超大字体时存放的字符的个数
     */
    private final int extralargeSize = 8 * 2;

    public void bindWangPos() {
        Log.e(TAG, "bindWangPos");
        try {
            wpPrinterDev = WeiposImpl.as().openPrinter();
            wangPrintListener = new WPossPrintListener();
            wpPrinterDev.setOnEventListener(wangPrintListener);
        } catch (Exception e) {
            wpPrinterDev = null;
            printNotBound();
        }
    }

    @Override
    public boolean closeConnect() {
        return true;
    }

    @Override
    protected void printText(TextModel model) {
        Printer.FontSize size = formatFontSize(model.getFontSize());

        String text = model.getTextStr();
        // 居中
        if (model.getFontAlign() == 1) {
            text = BasePrintStringUtils.formatCenter(formatSymbolSize(model.getFontSize()), text);
        }
        wpPrinterDev.printText(text, Printer.FontFamily.SONG, size
                , Printer.FontStyle.NORMAL, IPrint.Gravity.LEFT);
    }

    /**
     * 旺POS没有两倍宽和两倍高，只有同时两倍
     * 0对应normal，1,2,3对应medium
     *
     * @param size
     * @return
     */
    private Printer.FontSize formatFontSize(int size) {
        switch (size) {
            case 0:
                return Printer.FontSize.MEDIUM;
            case 1:
            case 2:
            case 3:
                return Printer.FontSize.LARGE;
            default:
                return Printer.FontSize.EXTRALARGE;
        }
    }

    private int formatSymbolSize(int size) {
        switch (size) {
            case 0:
                return mediumSize;
            case 1:
            case 2:
            case 3:
                return largeSize;
            default:
                return extralargeSize;
        }
    }

    @Override
    protected void printBitmap(BitmapModel model) {
        Bitmap bitmap = model.getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        wpPrinterDev.printImage(data, IPrint.Gravity.CENTER);

        // 换行
        wpPrinterDev.printText("", Printer.FontFamily.SONG, Printer.FontSize.SMALL
                , Printer.FontStyle.NORMAL, IPrint.Gravity.LEFT);
    }

    @Override
    protected void printQrCode(QrCodeModel model) {
        wpPrinterDev.printQrCode(model.getQrCode(), 400, IPrint.Gravity.CENTER);
        // 换行
        wpPrinterDev.printText("", Printer.FontFamily.SONG, Printer.FontSize.SMALL
                , Printer.FontStyle.NORMAL, IPrint.Gravity.LEFT);
    }

    @Override
    protected void printPageEnd(PageEndModel basePrintModel) {
        // 比通用设置的要多走1行
        for (int i = 0; i < basePrintModel.getPageGo() + 1; i++) {
            wpPrinterDev.printText("", Printer.FontFamily.SONG, Printer.FontSize.MEDIUM
                    , Printer.FontStyle.NORMAL, IPrint.Gravity.LEFT);
        }
    }

    /**
     * 旺Pos的打印回调
     */
    private class WPossPrintListener implements IPrint.OnEventListener {
        @Override
        public void onEvent(int what, String msg) {
            Log.e(TAG, "WPossPrintListener onEvent " + what + " " + msg);
            switch (what) {
                case IPrint.EVENT_OK:
                    printOk();
                    break;
                default:
                    printOk();
                    break;
            }
        }
    }

}
