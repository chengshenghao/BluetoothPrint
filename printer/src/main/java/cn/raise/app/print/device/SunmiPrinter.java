package cn.raise.app.print.device;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import cn.raise.app.print.common.BitmapUtil;
import cn.raise.app.print.model.BasePrintModel;
import cn.raise.app.print.model.BitmapModel;
import cn.raise.app.print.model.PageEndModel;
import cn.raise.app.print.model.QrCodeModel;
import cn.raise.app.print.model.TextModel;
import woyou.aidlservice.jiuiv5.ICallback;
import woyou.aidlservice.jiuiv5.IWoyouService;

/**
 * 描述: 商米打印
 * <p/>作者：景阳
 * <p/>创建时间: 2017/5/11 10:25
 */
public class SunmiPrinter extends DevicePrinter {

    private final String TAG = "SunmiPrinter";

    /**
     * 商米打印的驱动
     */
    private IWoyouService woyouService;
    /**
     * 商米打印的回调
     */
    private ICallback smCallback = null;

    /**
     * 绑定商米服务
     */
    public void bindSMService(Context context) {
        Log.e(TAG, "bindSMService");
        Intent intent = new Intent();
        intent.setPackage("woyou.aidlservice.jiuiv5");
        intent.setAction("woyou.aidlservice.jiuiv5.IWoyouService");
        context.startService(intent);
        context.bindService(intent, connService, Context.BIND_AUTO_CREATE);
        smCallback = new SunmiCallback();
    }

    /**
     * 解绑商米服务
     *
     * @param context
     */
    @Override
    public void unBind(Context context) {
        super.unBind(context);
        Log.e(TAG, "unBindSMService");
        try {
            context.unbindService(connService);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 商米绑定服务
     */
    private ServiceConnection connService = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e(TAG, "商米服务断开了");
            woyouService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e(TAG, "商米服务连接成功");
            woyouService = IWoyouService.Stub.asInterface(service);
        }
    };

    /**
     * 商米默认字体 24，中文为 24*24 的矩阵，英文为 12*24 的矩阵
     *
     * @param size
     * @return
     */
    private int formatFontSize(int size) {
        int fontSize = 24;
        if (size == 3) {
            fontSize = 40;
        }
        if (size > 3) {
            fontSize = 65;
        }
        return fontSize;
    }

    @Override
    protected void printText(TextModel model) {
        try {
            woyouService.setAlignment(model.getFontAlign(), null);
            int fontSize = formatFontSize(model.getFontSize());
            woyouService.printTextWithFont(model.getTextStr(), "", fontSize, null);
            if (model.isEnter()) {
                woyouService.printTextWithFont(" \n", "", fontSize, null);
            }
            printPageGo(model);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void printPageGo(BasePrintModel model) throws RemoteException {
        if (model.getPageGo() > 0) {
            for (int i = 0; i < model.getPageGo(); i++) {
                woyouService.printText(" \n", null);
            }
        }
    }

    @Override
    protected void printBitmap(BitmapModel model) {
        printBitmap(model.getBitmap(), model.getAlign(), model.getPageGo());
    }

    private void printBitmap(Bitmap bitmap, int align, int lineWrap) {
        try {
            woyouService.setAlignment(align, null);
            woyouService.printBitmap(bitmap, null);
            if (lineWrap > 0) {
                // 换行
                woyouService.lineWrap(lineWrap, null);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void printQrCode(QrCodeModel model) {
        Bitmap bitmap = BitmapUtil.createQRBitmap(model.getQrCode(), model.getWidth(), model.getHeight(), 10);
        if (bitmap != null) {
            printBitmap(bitmap, model.getAlign(), model.getPageGo());
        }
    }

    @Override
    protected void printPageEnd(PageEndModel model) {
        try {
            model.setPageGo(3);
            printPageGo(model);
            if (model.isCutPage()) {
                woyouService.cutPaper(null);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean closeConnect() {
        return true;
    }

}
