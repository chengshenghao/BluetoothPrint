package cn.raise.app.print.device.ums;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;

import com.ums.upos.sdk.exception.CallServiceException;
import com.ums.upos.sdk.exception.SdkException;
import com.ums.upos.sdk.printer.GrayLevelEnum;
import com.ums.upos.sdk.printer.OnPrintResultListener;
import com.ums.upos.sdk.printer.PrinterManager;
import com.ums.upos.sdk.system.BaseSystemManager;
import com.ums.upos.sdk.system.OnServiceStatusListener;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2018/2/28 15:09
 */
public class UmsManager implements OnServiceStatusListener {

    /**
     * png临时文件位置
     */
    private static final String TMP_FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/tmp.png";

    /**
     * POS机织带宽度（略小于实际尺寸）
     */
    private static final int BITMAP_WIDTH = 384;

    /**
     * 打印失败
     */
    public static final int RESULT_PRINT_FAILED = -999;

    /**
     * 业务类型，打印
     */
    public static final int BUSINESS_TYPE_PRINT = 11;
    /**
     * 大号字一行包含的字符数
     */
    public static final int MAX_COUNT_EXTRO_BIG = 16;
    /**
     * 大号字一行包含的字符数
     */
    public static final int MAX_COUNT_BIG = 24;

    /**
     * 小号字一行包含的字符数
     */
    public static final int MAX_COUNT_SMALL = 32;
    /**
     * 最大号字体
     */
    private static final float FONT_SIZE_EXTORBIG = 48f;
    /**
     * 大号字
     */
    private static final float FONT_SIZE_BIG = 32f;

    /**
     * 小号字
     */
    private static final float FONT_SIZE_SMALL = 24f;

    private final Context mContext;

    /**
     * 打印完成的回调
     */
    private OnPrintResultListener mOnPrintResultListener;

    /**
     * 银联SDK打印管理器
     */
    private PrinterManager mPrinterManager;

    private boolean mIsDeviceAvailable;

    private Paint mPaint;

    public UmsManager(Context context) {
        mContext = context;
        //初始化系统服务
        loginDeviceService(context);
    }

    /**
     * 登录系统服务
     *
     * @param context
     */
    private void loginDeviceService(Context context) {
        Log.e("UmsManager", "loginDeviceService ");
        try {
            BaseSystemManager.getInstance().deviceServiceLogin(context, null, "87654321", this);
        }
        //不支持的设备
        catch (SdkException e) {
            e.printStackTrace();
            if (mPrinterManager == null) {
                mIsDeviceAvailable = false;
            }
        }
        SystemClock.sleep(500);
    }

    /**
     * 注销系统服务
     */
    private void logoutDeviceService() {
        Log.e("UmsManager", "logoutDeviceService ");
        try {
            BaseSystemManager.getInstance().deviceServiceLogout();
        } catch (Exception e) {
            //首次刷卡有可能抛异常 Service not registered
            e.printStackTrace();
        }
    }

    @Override
    public void onStatus(int i) {
        Log.e("UmsManager", "onStatus " + i);
        switch (i) {
            //初始化系统服务成功
            case 0:
            case 2:
            case 100:
                if (mPrinterManager == null) {
                    //设置系统服务可用
                    mIsDeviceAvailable = true;
                    //初始化打印功能
                    initPrinterManager();
                }
                break;
            //不支持的设备
            default:
                if (mPrinterManager == null) {
                    mIsDeviceAvailable = false;
                }
                if (mOnPrintResultListener != null) {
                    mOnPrintResultListener.onPrintResult(RESULT_PRINT_FAILED);
                }
                break;
        }
    }

    /**
     * 初始化打印管理器（SDK的）
     */
    private void initPrinterManager() {
        mPrinterManager = new PrinterManager();
        try {
            if (mPrinterManager.initPrinter() == 0) {
                //深色
                mPrinterManager.setGray(GrayLevelEnum.LEVEL_10);
                //初始化画笔
                initPaint();
            }
        }
        //打印不可用
        catch (SdkException e) {
            e.printStackTrace();
        } catch (CallServiceException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        mPaint = new Paint();
        //设置字体(宋体)
        Typeface typeface = Typeface.createFromAsset(mContext.getAssets(), "simsun.ttc");
        mPaint.setTypeface(typeface);
        //黑色字
        mPaint.setColor(Color.BLACK);
    }

    public void print(List<UmsPrintItem> items, OnPrintResultListener listener) {
        initPaint();
        Bitmap bitmap = createBitmap(items);
        mOnPrintResultListener = listener;
        try {
            mPrinterManager.setBitmap(bitmap);
            mPrinterManager.startPrint(mOnPrintResultListener);
        } catch (SdkException e) {
            mOnPrintResultListener.onPrintResult(RESULT_PRINT_FAILED);
            e.printStackTrace();
        } catch (CallServiceException e) {
            mOnPrintResultListener.onPrintResult(RESULT_PRINT_FAILED);
            e.printStackTrace();
        }
    }

    /**
     * 生成打印用单据bitmap
     *
     * @param items
     * @return
     */
    private Bitmap createBitmap(List<UmsPrintItem> items) {
        //初始化Bitmap
        Bitmap bitmap = initBitmap(items);
        //绘制bitmap
        printBitmap(bitmap, mPaint, items);
        return bitmap;
    }

    /**
     * 初始化Bitmap
     *
     * @param items 打印条目列表
     * @return
     */
    private Bitmap initBitmap(List<UmsPrintItem> items) {
        //计算图片高度
        float bitmapHeight = getBitmapHeight(items, mPaint);
        //创建bitmap
        return Bitmap.createBitmap(BITMAP_WIDTH, (int) (bitmapHeight + 1), Bitmap.Config.RGB_565);
    }

    /**
     * 计算生成的bitmap的高度
     *
     * @return
     */
    private float getBitmapHeight(List<UmsPrintItem> items, Paint mPaint) {
        float height = 0;
        //计算每个条目所需高度，进行累加
        for (UmsPrintItem item : items) {
            height += getItemHeight(item, mPaint);
        }
        return height;
    }

    private float getItemHeight(UmsPrintItem item, Paint mPaint) {
        if (item.getBitmap() == null) {
            //设置字体
            switch (item.getFontSize()) {
                case big:
                    mPaint.setTextSize(FONT_SIZE_BIG);
                    break;
                case extraBig:
                    mPaint.setTextSize(FONT_SIZE_EXTORBIG);
                    break;
                default:
                    mPaint.setTextSize(FONT_SIZE_SMALL);
                    break;
            }
            //获取一行的高度
            float lineHeight = getLineHeight(item.getText(), mPaint);
            //获取行数
            int lineCount = getLineCount(item);
            //计算打印条目所需的高度
            return lineHeight * lineCount;
        } else {
            return item.getBitmap().getHeight() + item.getBitmapBottom();
        }
    }

    /**
     * 获取一条需要打印多少行
     *
     * @param item
     * @return
     */
    private int getLineCount(UmsPrintItem item) {
        //西文字符数
        int charCount = getByteCount(item);
        int lineCount = 0;
        int remainder = 0;
        switch (item.getFontSize()) {
            //大号
            case big:
                lineCount = charCount / MAX_COUNT_BIG;
                remainder = charCount % MAX_COUNT_BIG;
                break;
            //超大号
            case extraBig:
                lineCount = charCount / MAX_COUNT_EXTRO_BIG;
                remainder = charCount % MAX_COUNT_EXTRO_BIG;
                break;
            //小号
            default:
                lineCount = charCount / MAX_COUNT_SMALL;
                remainder = charCount % MAX_COUNT_SMALL;
                break;
        }
        //如果后面有不足一行的内容
        if (remainder != 0) {
            ++lineCount;
        }
        return lineCount;
    }


    /**
     * 获取一行的高度
     *
     * @param mPaint
     * @return
     */
    private float getLineHeight(String text, Paint mPaint) {
        //测量字符串的尺寸
        mPaint.measureText(text);
        Paint.FontMetrics metrics = mPaint.getFontMetrics();
        return metrics.bottom - metrics.top;
    }

    /**
     * 获取一行有的字节个数
     *
     * @param item 一行条目
     * @return
     */
    private int getByteCount(UmsPrintItem item) {
        try {
            return item.getText().getBytes("gbk").length;
        } catch (UnsupportedEncodingException e) {
            //can't reach
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 将文本条目写到bitmap中
     *
     * @param bitmap
     * @param items
     */
    private void printBitmap(Bitmap bitmap, Paint mPaint, List<UmsPrintItem> items) {
        //当前打印行的Y坐标
        float y = 0;
        //设置画布
        Canvas canvas = new Canvas(bitmap);
        //画布背景白色
        canvas.drawColor(Color.WHITE);
        //逐个条目绘制
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getBitmap() == null) {
                //设置字号
                switch (items.get(i).getFontSize()) {
                    //big (标题)
                    case big:
                        mPaint.setTextSize(FONT_SIZE_BIG);
                        break;
                    //超大号
                    case extraBig:
                        mPaint.setTextSize(FONT_SIZE_EXTORBIG);
                        break;
                    //normal （正文）
                    default:
                        mPaint.setTextSize(FONT_SIZE_SMALL);
                        break;
                }
            }
            //绘制条目
            y = printItem(y, canvas, mPaint, items.get(i));
        }
    }

    /**
     * 打印一条内容（自动换行）
     *
     * @param y      起始位置纵坐标
     * @param canvas 画布
     * @param mPaint 画笔
     * @param item   条目
     * @return 下一行的打印起始位置y坐标
     */
    private float printItem(float y, Canvas canvas, Paint mPaint, UmsPrintItem item) {
        if (item.getBitmap() == null) {
            //创建多行文本
            List<String> lineContents = createLineContents(item);
            //逐行绘制
            for (String str : lineContents) {
                //绘制当前
                canvas.drawText(str, 0, y - mPaint.getFontMetrics().top, mPaint);
                //计算下一行的起始位置
                y += getLineHeight(item.getText(), mPaint);
            }
        } else {
            int left = (BITMAP_WIDTH - item.getBitmap().getWidth()) / 2;
            canvas.drawBitmap(item.getBitmap(), left, y, mPaint);
            y += (item.getBitmap().getHeight() + item.getBitmapBottom());
        }
        //返回下一行的起始位置
        return y;
    }

    /**
     * 将一条分割为多行（如果不足2行，则保持原样）
     *
     * @param item 打印条目
     * @return
     */
    private List<String> createLineContents(UmsPrintItem item) {
        List<String> lineContents = new ArrayList<String>();
        int maxLineCount = 0;
        switch (item.getFontSize()) {
            case big:
                maxLineCount = MAX_COUNT_BIG;
                break;
            case extraBig:
                maxLineCount = MAX_COUNT_EXTRO_BIG;
                break;
            default:
                maxLineCount = MAX_COUNT_SMALL;
                break;
        }
        addLineContents(item, lineContents, maxLineCount);
        return lineContents;
    }

    /**
     * 向list中添加每行需要打印的字符串
     *
     * @param item
     * @param lineContents
     * @param maxLineCount 必须为MAX_COUNT_BIG或者MAX_COUNT_SMALL
     */
    private void addLineContents(UmsPrintItem item, List<String> lineContents, int maxLineCount) {
        //如果item中只有一个字符
        String text = item.getText();
        if (text.length() == 1) {
            lineContents.add(text);
            return;
        }
        //如果数量多于一个字符
        boolean out = false;
        try {
            for (int i = 0; i < text.length(); i++) {
                for (int j = i + 1; j <= text.length(); j++) {
                    //正好
                    if (text.substring(i, j).getBytes("gbk").length == maxLineCount) {
                        lineContents.add(text.substring(i, j));
                        i = j - 1;
                        break;
                    }
                    //多一个字符超出行边界，则少放一个字符（西文+中文交接的地方会出现这种情况）
                    if (text.substring(i, j).getBytes("gbk").length == maxLineCount - 1) {
                        if (text.substring(i, j + 1).getBytes("gbk").length == maxLineCount + 1) {
                            lineContents.add(text.substring(i, j));
                            i = j - 1;
                            break;
                        }
                    }
                    //如果到了尾部
                    if (j == text.length() - 1) {
                        lineContents.add(text.substring(i));
                        out = true;
                        break;
                    }
                    if (j == text.length()) {
                        //只有一个字符了
                        lineContents.add(text.substring(i));
                        out = true;
                        break;
                    }
                }
                if (out) {
                    break;
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void destroy() {
        logoutDeviceService();
    }
}
