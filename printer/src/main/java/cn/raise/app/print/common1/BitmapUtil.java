package cn.raise.app.print.common1;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Hashtable;

/**
 * 描述:蓝牙打印机处理图片信息
 *
 * @author wjx
 * @date 2018/7/31 13:29
 */
public class BitmapUtil {
    /**
     * 将bitmap对象保存成图片到sd卡中
     */
    public static void saveBitmapToSDCard(Bitmap bitmap) {
        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/tcslpaiduilog/a.png");
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            //设置PNG的话，透明区域不会变成黑色
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, ((OutputStream) fileOutputStream));
            fileOutputStream.close();
            System.out.println("----------save success-------------------");
        } catch (Exception v0) {
            v0.printStackTrace();
        }

    }

    /**
     * 组装bitmap数据
     *
     * @param bitmap
     * @param nWidth
     * @param nMode
     * @return
     */
    public static byte[] addRastBitImage(Bitmap bitmap, int nWidth, int nMode) {
        if (bitmap != null) {
            int width = (nWidth + 7) / 8 * 8;
            int height = bitmap.getHeight() * width / bitmap.getWidth();
            Bitmap grayBitmap = GpUtils.toGrayscale(bitmap);
            Bitmap rszBitmap = GpUtils.resizeImage(grayBitmap, width, height);
            byte[] src = GpUtils.bitmapToBWPix(rszBitmap);
            byte[] command = new byte[8];
            height = src.length / width;
            command[0] = 29;
            command[1] = 118;
            command[2] = 48;
            command[3] = (byte) (nMode & 1);
            command[4] = (byte) (width / 8 % 256);
            command[5] = (byte) (width / 8 / 256);
            command[6] = (byte) (height % 256);
            command[7] = (byte) (height / 256);
            byte[] codecontent = GpUtils.pixToEscRastBitImageCmd(src);
            byte[] bytes = new byte[codecontent.length + 8];
            int i = 0;
            for (i = 0; i < 8; i++) {
                bytes[i] = command[i];
            }
            for (int k = 0; k < codecontent.length; ++k, i++) {
                bytes[i] = codecontent[k];
            }
            return bytes;
        } else {
            Log.d("BMP", "bmp.  null ");
            return new byte[0];
        }
    }

    /**
     * 生成没有白边的二维码
     *
     * @param str
     * @param width
     * @param height
     * @return
     */
    public static Bitmap createQRBitmap(String str, int width, int height) {
        try {
            Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
            hints.put(EncodeHintType.MARGIN, 1);
            BitMatrix matrix = new QRCodeWriter().encode(str, BarcodeFormat.QR_CODE, width, height);
            matrix = deleteWhite(matrix);//删除白边
            width = matrix.getWidth();
            height = matrix.getHeight();
            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (matrix.get(x, y)) {
                        pixels[y * width + x] = Color.BLACK;
                    } else {
                        pixels[y * width + x] = Color.WHITE;
                    }
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return bitmap;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 删除白边
     * @param matrix
     * @return
     */
    private static BitMatrix deleteWhite(BitMatrix matrix) {
        int[] rec = matrix.getEnclosingRectangle();
        int resWidth = rec[2] + 1;
        int resHeight = rec[3] + 1;

        BitMatrix resMatrix = new BitMatrix(resWidth, resHeight);
        resMatrix.clear();
        for (int i = 0; i < resWidth; i++) {
            for (int j = 0; j < resHeight; j++) {
                if (matrix.get(i + rec[0], j + rec[1]))
                    resMatrix.set(i, j);
            }
        }
        return resMatrix;
    }
}
