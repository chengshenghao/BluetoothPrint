package cn.raise.app.print.model;

/**
 * 描述: 二维码，如果是蓝牙打印机，则调用这个方法
 * <p/>作者：景阳
 * <p/>创建时间: 2017/5/11 16:34
 */
public class FakeQrCodeModel extends BasePrintModel {

    private String qrCode;

    private int width;

    private int height;

    private int align;

    private int paperType;

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
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
