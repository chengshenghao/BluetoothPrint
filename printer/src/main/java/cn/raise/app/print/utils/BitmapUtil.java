package cn.raise.app.print.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;

import java.io.File;

import id.zelory.compressor.Compressor;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/6/2 14:08
 */
public class BitmapUtil {

    /**
     * 将图片文件处理为适合打印的大小
     *
     * @param context
     * @param filePath
     * @return
     */
    public static Bitmap compressPrintBitmap(Context context, String filePath) {
        return new Compressor.Builder(context)
                .setMaxHeight(160)
                .setMaxWidth(160)
                .setQuality(100)
                .build().compressToBitmap(new File(filePath));
    }

    /**
     * 将图片黑白化
     * @param mBitmap
     * @param thresholdBW 黑白阈值0-255，表示如果平均值大于该参数就会处理为白色，否则黑色
     * @param thresholdAlpha alpha阈值0-255，表示如果alpha小于该参数就会处理为白色，否则黑色
     * @return
     */
    public static Bitmap toHeibai(Bitmap mBitmap, int thresholdBW, int thresholdAlpha) {
        int mBitmapWidth = 0;
        int mBitmapHeight = 0;

        mBitmapWidth = mBitmap.getWidth();
        mBitmapHeight = mBitmap.getHeight();
        Bitmap bmpReturn = Bitmap.createBitmap(mBitmapWidth, mBitmapHeight,
                Bitmap.Config.ARGB_8888);
        int iPixel = 0;
        for (int i = 0; i < mBitmapWidth; i++) {
            for (int j = 0; j < mBitmapHeight; j++) {
                int curr_color = mBitmap.getPixel(i, j);

                int alpha = Color.alpha(curr_color);
                int avg = (Color.red(curr_color) + Color.green(curr_color) + Color
                        .blue(curr_color)) / 3;
                // 平均值大于黑白阈值，或者透明程度小于
                if (avg >= thresholdBW || alpha < thresholdAlpha) {
                    iPixel = 255;
                } else {
                    iPixel = 0;
                }
                int modif_color = Color.argb(255, iPixel, iPixel, iPixel);

                bmpReturn.setPixel(i, j, modif_color);
            }
        }
        return bmpReturn;
    }
}
