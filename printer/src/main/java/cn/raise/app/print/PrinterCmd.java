package cn.raise.app.print;


public class PrinterCmd {

    public PrinterCmd() {

    }

    /**
     * 初始化打印机
     *
     * @return 返回初始化打印机的命令
     */
    public String resetPos() {
        return new StringBuffer().append((char) 27).append((char) 64).toString();
    }

    /**
     * 换行（回车）
     *
     * @return
     */
    public String enter() {
        return new StringBuffer().append((char) 10).toString();
    }

    /**
     * 对齐模式
     *
     * @param align 0:左对齐 1:中对齐 2:右对齐
     * @return
     */
    public String textAlign(int align) {
        return new StringBuffer().append((char) 27).append((char) 97).append((char) align).toString();
    }

    /**
     * 字体的大小
     *
     * @param fontSize 0:正常大小 1:两倍高 2:两倍宽 3:两倍大小 4:三倍高 5:三倍宽 6:三倍大小 7:四倍高
     *                 8:四倍宽 9:四倍大小 10:五倍高 11:五倍宽 12:五倍大小
     *                 80mm纸打印3号汉字为12个，1、2号汉字为24个。数字英文个数为汉字二倍
     * @return
     */
    public String fontSize(int fontSize) {
        String cmdStr = "";

        //设置字体大小
        switch (fontSize) {
            case -1:
                cmdStr = new StringBuffer().append((char) 29).append((char) 33).append((char) 0).toString();//29 33
                break;
            case 0:
                cmdStr = new StringBuffer().append((char) 29).append((char) 33).append((char) 0).toString();//29 33
                break;
            case 1:
                cmdStr = new StringBuffer().append((char) 29).append((char) 33).append((char) 1).toString();
                break;
            case 2:
                cmdStr = new StringBuffer().append((char) 29).append((char) 33).append((char) 16).toString();
                break;
            case 3:
                cmdStr = new StringBuffer().append((char) 29).append((char) 33).append((char) 17).toString();
                break;
            case 4:
                cmdStr = new StringBuffer().append((char) 29).append((char) 33).append((char) 2).toString();
                break;
            case 5:
                cmdStr = new StringBuffer().append((char) 29).append((char) 33).append((char) 32).toString();
                break;
            case 6:
                cmdStr = new StringBuffer().append((char) 29).append((char) 33).append((char) 34).toString();
                break;
            case 7:
                cmdStr = new StringBuffer().append((char) 29).append((char) 33).append((char) 3).toString();
                break;
            case 8:
                cmdStr = new StringBuffer().append((char) 29).append((char) 33).append((char) 48).toString();
                break;
            case 9:
                cmdStr = new StringBuffer().append((char) 29).append((char) 33).append((char) 51).toString();
                break;
            case 10:
                cmdStr = new StringBuffer().append((char) 29).append((char) 33).append((char) 4).toString();
                break;
            case 11:
                cmdStr = new StringBuffer().append((char) 29).append((char) 33).append((char) 64).toString();
                break;
            case 12:
                cmdStr = new StringBuffer().append((char) 29).append((char) 33).append((char) 68).toString();
                break;
        }
        return cmdStr;
    }

    /**
     * BTP-M280(针打) 倍宽倍高
     *
     * @param size 0:取消倍宽倍高模式  1:倍高模式 2:倍宽模式 3:两倍大小
     * @return
     */
    public String fontSizeBTPM280(int size) {
        String cmdStr = "";
        //只有0和1两种模式
        int fontsize = size;

        switch (fontsize) {
            case 1:
                cmdStr = new StringBuffer().append((char) 28).append((char) 33).append((char) 8).toString();
                break;
            case 2:
                cmdStr = new StringBuffer().append((char) 28).append((char) 33).append((char) 4).toString();
                break;
            case 3:
                cmdStr = new StringBuffer().append((char) 28).append((char) 87).append((char) 1).toString();
                break;
            default:
                cmdStr = new StringBuffer().append((char) 28).append((char) 87).append((char) 0).toString();
                break;
        }
        return cmdStr;
    }

    /**
     * BTP-M280(针打) 倍宽倍高
     *
     * @param size 0:取消倍宽倍高模式  1:倍高模式 2:倍宽模式 3:两倍大小
     * @return
     */
    public String fontSizeBTPM2801(int size) {
        String cmdStr = "";
        //只有0和1两种模式
        int fontsize = size;

        switch (fontsize) {
            case 1:
                cmdStr = new StringBuffer().append((char) 27).append((char) 33).append((char) 17).toString();
                break;
            case 2:
                cmdStr = new StringBuffer().append((char) 27).append((char) 33).append((char) 33).toString();
                break;
            case 3:
                cmdStr = new StringBuffer().append((char) 27).append((char) 33).append((char) 49).toString();
                break;
            default:
                cmdStr = new StringBuffer().append((char) 27).append((char) 33).append((char) 1).toString();
                break;
        }
        return cmdStr;
    }

    /**
     * 走纸
     *
     * @param line 走纸的行数
     * @return
     */
    public String pageGo(int line) {
        return new StringBuffer().append((char) 27).append((char) 100).append((char) line).toString();
    }

    /**
     * 左间距
     *
     * @param space
     * @return
     */
    public String paddingLeft(int space) {
        return new StringBuffer().append((char) 27).append((char) 36).append((char) (space % 256)).append((char) (space / 256)).toString();
    }

    /**
     * 切割
     *
     * @return
     */
    public String cutPage() {
        return new StringBuffer().append((char) 27).append((char) 109).toString();
    }

    /**
     * 返回状态(返回8位的二进制)
     *
     * @param num 1:打印机状态 2:脱机状态 3:错误状态 4:传送纸状态
     *            返回值 类型介绍
     *            第一位：固定为0
     *            第二位：固定为1
     *            第三位：0:一个或两个钱箱打开  1:两个钱箱都关闭
     *            第四位：0:联机  1:脱机
     *            第五位：固定为1
     *            第六位：未定义
     *            第七位：未定义
     *            第八位：固定为0
     *            返回脱机状态如下：
     *            第一位：固定为0
     *            第二位：固定为1
     *            第三位：0:上盖关  1:上盖开
     *            第四位：0:未按走纸键  1:按下走纸键
     *            第五位：固定为1
     *            第六位：0:打印机不缺纸  1: 打印机缺纸
     *            第七位：0:没有出错情况  1:有错误情况
     *            第八位：固定为0
     *            返回错误状态如下：
     *            第一位：固定为0
     *            第二位：固定为1
     *            第三位：未定义
     *            第四位：0:切刀无错误  1:切刀有错误
     *            第五位：固定为1
     *            第六位：0:无不可恢复错误  1: 有不可恢复错误
     *            第七位：0:打印头温度和电压正常  1:打印头温度或电压超出范围
     *            第八位：固定为0
     *            返回传送纸状态如下：
     *            第一位：固定为0
     *            第二位：固定为1
     *            第三位：0:有纸  1:纸将尽
     *            第四位：0:有纸  1:纸将尽
     *            第五位：固定为1
     *            第六位：0:有纸  1:纸尽
     *            第七位：0:有纸  1:纸尽
     *            第八位：固定为0
     * @return
     */
    public String returnStatus(int num) {
        return new StringBuffer().append((char) 16).append((char) 4).append((char) num).toString();
    }

    /**
     * 条码高宽
     *
     * @param num
     * @return
     */
    public String tiaoMaHeight(int num) {
        //return ((char)29).append"h" + ((char)num).toString();
        return new StringBuffer().append((char) 29).append((char) 104).append((char) num).toString();
    }

    /**
     * 条码宽度
     *
     * @param num
     * @return
     */
    public String tiaoMaWidth(int num) {
        return new StringBuffer().append((char) 29).append((char) 119).append((char) num).toString();
    }


    /**
     * 条码数字打印的位置
     *
     * @param num 1:上方  2:下方  0:不打印数字
     * @return
     */
    public String tiaoMaWeiZi(int num) {
        return new StringBuffer().append((char) 29).append("H").append((char) num).toString();
    }

    /**
     * 开始打印(条码类型为CODE39)
     *
     * @param numstr
     * @return
     */
    public String tiaoMaPrint(String numstr) {
        return new StringBuffer().append((char) 29).append((char) 107).append((char) 4).append(numstr).append((char) 0).toString();
    }

    /**
     * 设置行间距
     *
     * @param spec 间距大小
     * @return
     */
    public String spec(int spec) {
        return new StringBuffer().append((char) 27).append((char) 51).append(String.valueOf(spec)).toString();
    }

    /**
     * 打开钱箱
     *
     * @return
     */
    public String cashbox() {
        return new StringBuffer().append((char) 27).append((char) 112).append((char) 0).append((char) 60).append((char) 255).toString();
    }

    /**
     * 加粗
     *
     * @return
     */
    public String bold(int flag) {
        return new StringBuffer().append((char) 27).append((char) 69).append((char) flag).toString();
    }

    /**
     * 打印空白(一个Tab的位置，约4个空格)
     *
     * @param length 需要打印空白的长度,
     */
    public String tabSpace(int length) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < length; i++) {
            buffer.append("\t");
        }
        return buffer.toString();
    }

    /**
     * 打印空白
     *
     * @param length 需要打印空白空格长度
     */
    public String space(int length) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < length; i++) {
            buffer.append(" ");
        }
        return buffer.toString();
    }

    /**
     * 设置字体为标准还是压缩。 0：标准，1：压缩
     *
     * @return
     */
    public String textCompress(int flag) {
        return new StringBuffer().append((char) 27).append((char) 77).append((char) flag).toString();
    }

    /**
     * 设置行间距
     *
     * @param spacing 0 ≤ spacing ≤ 255
     * @return
     */
    public String lineSpacing(int spacing) {
        return new StringBuffer().append((char) 27).append((char) 51).append((char) spacing).toString();
    }

    /**
     * 默认行间距
     *
     * @return
     */
    public String defautLineSpacing() {
        return new StringBuffer().append((char) 27).append((char) 50).toString();
    }

    /**
     * 初始化打印机。清除打印缓冲区数据，打印模式被设为上电时的默认值模式。
     *
     * @return
     */
    public String initPrinter() {
        return new StringBuffer().append((char) 27).append((char) 64).toString();
    }

    /**
     * 标准模式
     *
     * @return
     */
    public String standardMode() {
        return new StringBuffer().append((char) 27).append((char) 64).toString();
    }

    /**
     * 页模式
     *
     * @return
     */
    public String pageMode() {
        return new StringBuffer().append((char) 27).append((char) 76).toString();
    }
}
