package cn.raise.app.print.device;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * 关于打印的字符串的处理辅助类
 * Created by wjx on 2016/5/11 0011.
 */
public class BasePrintStringUtils {

    /**
     * 获得实际占位的字节数
     *
     * @param s
     * @return
     */
    public static int getWordCountRegex(String s) {
        s = s.replaceAll("[^\\x00-\\xff]", "**");
        int length = s.length();
        return length;
    }

    /**
     * 打印时，格式化菜品的相关信息，使显示样式统一
     *
     * @param name 　菜品名称
     * @param num  菜品数量
     * @return
     */
    public static String formatToLeftAndRight(String name, String num) {
        String resultString = "";
        // System.out.println(name.getBytes().length);
        // 比例按3:1 一共32个字节
        // 格式化菜品名称
        if (getWordCountRegex(name) < 24) {
            String spaceString = "";
            for (int i = 0; i < (24 - getWordCountRegex(name)); i++) {
                spaceString = spaceString + " ";
            }
            name = name + spaceString;
        } else if (getWordCountRegex(name) > 24) {
            if (getWordCountRegex(name) % 2 == 0) {
                name = name.substring(0, 12);
            } else {
                name = name.substring(0, 12) + " ";
            }
        }
        // 格式化数量
        if (getWordCountRegex(num) < 8) {
            String spaceString = "";
            for (int i = 0; i < (8 - getWordCountRegex(num)); i++) {
                spaceString = spaceString + " ";
            }
            num = spaceString + num;
        }
        resultString = name + num;
        return resultString;
    }

    /**
     * 打印时，格式化菜品的相关信息，使显示样式统一，打印菜品时，菜品名称单独占用一行显示
     * 8空格+8+8+8
     *
     * @param num   　数量
     * @param price 菜品价格
     * @param total 菜品总价
     * @return
     */
    public static String formatOutPutItemString(String num, String price,
                                                String total) {
        String resultString = "";
        // 格式化数量
        if (getWordCountRegex(num) < 8) {
            String spaceString = "";
            for (int i = 0; i < (8 - getWordCountRegex(num)); i++) {
                spaceString = spaceString + " ";
            }
            num = "        " + num + spaceString;
        }
        // 格式化单价
        if (getWordCountRegex(price) < 8) {
            String spaceString = "";
            for (int i = 0; i < (8 - getWordCountRegex(price)); i++) {
                spaceString = spaceString + " ";
            }
            price = price + spaceString;
        }
        // 格式化小计
        if (getWordCountRegex(total) < 8) {
            String spaceString = "";
            for (int i = 0; i < (8 - getWordCountRegex(total)); i++) {
                spaceString = spaceString + " ";
            }
            total = total + spaceString;
        }
        resultString = num + price + total;
        return resultString;
    }

    /**
     * 打印时，格式化菜品的相关信息，使显示样式统一，打印菜品时，菜品名称单独占用一行显示
     * 6空格  +4+7+8+7
     *
     * @param num      　数量
     * @param price    菜品价格
     * @param total    菜品小计
     * @param vipprice 菜品会员价
     * @return
     */
    public static String formatOutPutItemInfo(String num, String price,
                                              String total, String vipprice) {
        String resultString = "";
        // 格式化数量
        if (getWordCountRegex(num) < 4) {
            String spaceString = "";
            for (int i = 0; i < (4 - getWordCountRegex(num)); i++) {
                spaceString = spaceString + " ";
            }
            num = "      " + num + spaceString;
        }
        // 格式化单价
        if (getWordCountRegex(price) < 7) {
            String spaceString = "";
            for (int i = 0; i < (7 - getWordCountRegex(price)); i++) {
                spaceString = spaceString + " ";
            }
            price = price + spaceString;
        }
        // 格式化小计
        if (getWordCountRegex(total) < 8) {
            String spaceString = "";
            for (int i = 0; i < (8 - getWordCountRegex(total)); i++) {
                spaceString = spaceString + " ";
            }
            total = total + spaceString;
        }
        // 格式化注释
        if (getWordCountRegex(vipprice) < 7) {
            String spaceString = "";
            for (int i = 0; i < (7 - getWordCountRegex(vipprice)); i++) {
                spaceString = spaceString + " ";
            }
            vipprice = vipprice + spaceString;
        }
        resultString = num + price + total + vipprice;
        return resultString;
    }

    /**
     * 打印时，格式化菜品的相关信息，使显示样式统一，打印菜品时，菜品名称单独占用一行显示
     * 8空格  +6+6+8+4
     *
     * @param num   　数量
     * @param price 菜品价格
     * @param total 菜品总价
     * @param info  注释
     * @return
     */
    public static String formatOutPutItemString(String num, String price,
                                                String total, String info) {
        String resultString = "";
        // 格式化数量
        if (getWordCountRegex(num) < 6) {
            String spaceString = "";
            for (int i = 0; i < (6 - getWordCountRegex(num)); i++) {
                spaceString = spaceString + " ";
            }
            num = "        " + num + spaceString;
        }
        // 格式化单价
        if (getWordCountRegex(price) < 6) {
            String spaceString = "";
            for (int i = 0; i < (6 - getWordCountRegex(price)); i++) {
                spaceString = spaceString + " ";
            }
            price = price + spaceString;
        }
        // 格式化小计
        if (getWordCountRegex(total) < 8) {
            String spaceString = "";
            for (int i = 0; i < (8 - getWordCountRegex(total)); i++) {
                spaceString = spaceString + " ";
            }
            total = total + spaceString;
        }
        // 格式化注释
        if (getWordCountRegex(info) < 4) {
            String spaceString = "";
            for (int i = 0; i < (4 - getWordCountRegex(info)); i++) {
                spaceString = spaceString + " ";
            }
            info = info + spaceString;
        }
        resultString = num + price + total + info;
        return resultString;
    }

    /**
     * 通过空格，格式化文字，使数据居中
     *
     * @param size  能存放的字符的个数
     * @param title 居中显示的文字
     * @return
     */
    public static String formatCenter(int size, String title) {
        int sizeTitle = size - getWordCountRegex(title);
        String blank = "";
        for (int i = 0; i < sizeTitle / 2; i++) {
            blank += " ";

        }
        return blank + title;
    }

    /**
     * 将list 转为以 s 为分隔符的字符串
     *
     * @param stringList
     * @param s
     * @return
     */
    public static String listToString(List<String> stringList, String s) {
        if (stringList == null) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        boolean flag = false;
        for (String string : stringList) {
            if (flag) {
                result.append(s);
            } else {
                flag = true;
            }
            result.append(string);
        }
        return result.toString();
    }

    /**
     * 银联商务居中格式化
     *
     * @param text 文字内容
     * @param size 字号（normal，big）
     * @return
     */
    public static String formatCenterForUMS(String text, DeviceFontSize size) {
        int charCount = 0;
        try {
            charCount = text.getBytes("gbk").length;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        int spaceCount = 0;
        switch (size) {
            case normal:
                spaceCount = 32 - charCount;
                break;
            case big:
                spaceCount = 24 - charCount;
                break;
        }
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < spaceCount / 2; i++) {
            buf.append(" ");
        }
        buf.append(text);
        return buf.toString();
    }

    /**
     * 商米打印加单时，大字体，36号字体
     *
     * @param name
     * @param num
     * @return
     */
    public static String formatOutPutPayWayStringForShangmi(String name, String num) {
        String resultString = "";
        // System.out.println(name.getBytes().length);
        // 比例按3:1 一共32个字节
        // 格式化菜品名称
        if (getWordCountRegex(name) < 16) {
            String spaceString = "";
            for (int i = 0; i < (16 - getWordCountRegex(name)); i++) {
                spaceString = spaceString + " ";
            }
            name = name + spaceString;
        } else if (getWordCountRegex(name) > 15) {
            if (getWordCountRegex(name) % 2 == 0) {
                name = name.substring(0, 12);
            } else {
                name = name.substring(0, 12) + " ";
            }
        }
        // 格式化数量
        if (getWordCountRegex(num) < 5) {
            String spaceString = "";
            for (int i = 0; i < (5 - getWordCountRegex(num)); i++) {
                spaceString = spaceString + " ";
            }
            num = spaceString + num;
        }
        resultString = name + num;
        return resultString;
    }
}
