package cn.raise.app.print.model;

/**
 * Created by Administrator on 2017/3/19.
 */

public class TextModel extends BasePrintModel {

    /**
     * 对齐方式 0:居左 1:居中 2:居右
     */
    private int fontAlign;

    /**
     * 通用打印机：字体大小0:正常大小 1:两倍高 2:两倍宽 3:两倍大小 4:三倍高 5:三倍宽 6:三倍大小 7:四倍高 8:四倍宽 9:四倍大小 10:五倍高 11:五倍宽 12:五倍大小
     * 特殊设备以设备参数为准，根据通用参数自行转换
     */
    private int fontSize;

    /**
     * 打印文本行
     */
    private String textStr;

    public TextModel(String textStr) {
        this(0, 0, textStr);
    }

    public TextModel(int fontAlign, int fontSize, String textStr) {
        this.fontAlign = fontAlign;
        this.fontSize = fontSize;
        this.textStr = textStr;
    }

    public int getFontAlign() {
        return fontAlign;
    }

    public void setFontAlign(int fontAlign) {
        this.fontAlign = fontAlign;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public String getTextStr() {
        return textStr;
    }

    public void setTextStr(String textStr) {
        this.textStr = textStr;
    }
}
