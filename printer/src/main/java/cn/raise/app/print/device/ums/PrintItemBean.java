package cn.raise.app.print.device.ums;

/**
 * 打印的每条数据
 */
public class PrintItemBean {
    private String text;
    private boolean isBold;
    private boolean isTitle;
    public fontSize size;


    /**
     *
     * @param text 文字
     */
    public PrintItemBean(String text) {
        this(text, false);
    }

    /**
     *
     * @param text 文字
     * @param isBold 是否加粗
     */
    public PrintItemBean(String text, boolean isBold) {
        this(text,isBold,false);
    }


    /**
     *
     * @param text 文字
     * @param isBold 是否加粗
     * @param isTitle 是否为表头
     */
    public PrintItemBean(String text, boolean isBold, boolean isTitle) {
        this(text,isBold,isTitle, fontSize.normal);
        this.text = text;
        this.isBold = isBold;
        this.isTitle = isTitle;
    }
    /**
     *
     * @param text 文字
     * @param isBold 是否加粗
     * @param isTitle 是否为表头
     * @param  size 字体大小
     */
    public PrintItemBean(String text, boolean isBold, boolean isTitle, fontSize size) {
        this.text = text;
        this.isBold = isBold;
        this.isTitle = isTitle;
        this.size = size;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isTitle() {
        return isTitle;
    }

    public void setTitle(boolean title) {
        isTitle = title;
    }

    public boolean isBold() {
        return isBold;
    }

    public void setBold(boolean bold) {
        isBold = bold;
    }

    public fontSize getSize() {
        return size;
    }

    public void setSize(fontSize size) {
        this.size = size;
    }

    public enum fontSize{
        small,normal,big,extraBig
    }
}
