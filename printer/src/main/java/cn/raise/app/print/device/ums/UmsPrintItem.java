package cn.raise.app.print.device.ums;

import android.graphics.Bitmap;

/**
 * 描述:打印条目
 * <br/>作者：吴永弘
 * <br/>创建时间: 2016/12/6 18:49
 */
public class UmsPrintItem {


    /**
     * 文本内容
     */
    private String mText;

    /**
     * 字号
     */
    private PrintItemBean.fontSize mFontSize;

    private Bitmap bitmap;

    private int bitmapBottom;

    public UmsPrintItem(String text, PrintItemBean.fontSize fontSize) {
        mText = text;
        mFontSize = fontSize;
    }

    public UmsPrintItem(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getText() {
        //如果传入的是""，需要计算" "
        if ("".equals(mText)) {
            return " ";
        }
        return mText;
    }

    public void setText(String mText) {
        this.mText = mText;
    }

    public PrintItemBean.fontSize getFontSize() {
        return mFontSize;
    }

    public void setFontSize(PrintItemBean.fontSize mFontSize) {
        this.mFontSize = mFontSize;
    }


    @Override
    public String toString() {
        return "UmsPrintItem{" +
                "mText='" + mText + '\'' +
                ", mFontSize=" + mFontSize +
                '}';
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getBitmapBottom() {
        return bitmapBottom;
    }

    public void setBitmapBottom(int bitmapBottom) {
        this.bitmapBottom = bitmapBottom;
    }

    public enum UmsFontSize {
        BIG,
        SMALL,
    }
}
