package cn.raise.app.print.model;

import android.graphics.Bitmap;

/**
 * 描述: 图片
 * <p/>作者：景阳
 * <p/>创建时间: 2017/5/11 16:34
 */
public class BitmapModel extends BasePrintModel {

    private Bitmap bitmap;

    private int width;

    private int height;

    private int align;

    private int paperType;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getAlign() {
        return align;
    }

    public void setAlign(int align) {
        this.align = align;
    }

    public int getPaperType() {
        return paperType;
    }

    public void setPaperType(int paperType) {
        this.paperType = paperType;
    }
}
