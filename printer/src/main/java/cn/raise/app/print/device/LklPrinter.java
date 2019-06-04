package cn.raise.app.print.device;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.lkl.cloudpos.aidl.AidlDeviceService;
import com.lkl.cloudpos.aidl.printer.AidlPrinter;
import com.lkl.cloudpos.aidl.printer.AidlPrinterListener;
import com.lkl.cloudpos.aidl.printer.PrintItemObj;

import java.util.ArrayList;

import cn.raise.app.print.common.BitmapUtil;
import cn.raise.app.print.model.BitmapModel;
import cn.raise.app.print.model.PageEndModel;
import cn.raise.app.print.model.QrCodeModel;
import cn.raise.app.print.model.TextModel;

/**
 * 描述:拉卡拉打印
 * <p/>作者：wjx
 * <p/>创建时间: 2017/5/25 8:13
 */
public class LklPrinter extends DevicePrinter {
    private static final String TAG = "LklPrinter";
    /**
     * 拉卡拉打印驱动
     **/
    private AidlPrinter mPriter;
    /**
     * 拉卡拉打印回调
     */
    private LKLPrintListener lklPrintListener = null;
    private Context mContext;

    public LklPrinter(Context mContext) {
        this.mContext = mContext;
        lklPrintListener = new LKLPrintListener();
        bindLKLService();
    }

    @Override
    public boolean closeConnect() {
        return false;
    }

    @Override
    protected void printText(TextModel model) {
        ArrayList<PrintItemObj> objs = new ArrayList<>();
        if (model.getFontSize() >= 3) {
            objs.add(new PrintItemObj(model.getTextStr(), 24, true, PrintItemObj.ALIGN.CENTER));
        } else {
            PrintItemObj.ALIGN fontAlign = getFontAlign(model);
            objs.add(new PrintItemObj(model.getTextStr(), 8, false, fontAlign));
        }
        try {
            mPriter.printText(objs, lklPrintListener);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private PrintItemObj.ALIGN getFontAlign(TextModel model) {
        switch (model.getFontAlign()) {
            case 1:
                return PrintItemObj.ALIGN.CENTER;
            case 2:
                return PrintItemObj.ALIGN.RIGHT;
            default:
                return PrintItemObj.ALIGN.LEFT;
        }
    }

    @Override
    protected void printBitmap(BitmapModel model) {
        try {
            mPriter.printBmp((384 - model.getWidth()) / 2, model.getWidth(), model.getHeight(), model.getBitmap(), lklPrintListener);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void printQrCode(QrCodeModel model) {
        try {
            Bitmap bitmap = BitmapUtil.createQRBitmap(model.getQrCode(), model.getWidth(), model.getHeight(),10);
            mPriter.printBmp((384 - model.getWidth()) / 2, model.getWidth(), model.getHeight(), bitmap, lklPrintListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void printPageEnd(PageEndModel basePrintModel) {
        ArrayList<PrintItemObj> objs = new ArrayList<>();
        for (int i = 0; i < basePrintModel.getPageGo(); i++) {
            objs.add(new PrintItemObj("\n", 8, true, PrintItemObj.ALIGN.LEFT));
        }
        try {
            mPriter.printText(objs, lklPrintListener);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unBind(Context context) {
        super.unBind(context);
        mContext.unbindService(connectionLKL);
    }

    /**
     * 拉卡拉设备服务连接桥
     */
    private ServiceConnection connectionLKL = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder serviceBinder) {
            Log.e(TAG, "aidlService服务连接成功");
            if (serviceBinder != null) { // 绑定成功
                AidlDeviceService serviceManager = AidlDeviceService.Stub.asInterface(serviceBinder);
                try {
                    mPriter = AidlPrinter.Stub.asInterface(serviceManager.getPrinter());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e(TAG, "AidlService服务断开了");
            mPriter = null;
        }
    };

    /**
     * 绑定服务
     */
    private void bindLKLService() {
        Intent intent = new Intent();
        intent.setAction("lkl_cloudpos_mid_service");
        intent.setPackage("com.lklcloudpos.midservice");
        boolean flag = mContext.bindService(intent, connectionLKL, Context.BIND_AUTO_CREATE);
        if (flag) {
            Log.e("服务绑定成功", "服务绑定成功");
        } else {
            Log.e("服务绑定失败", "服务绑定失败");
        }
    }

    /**
     * 打印回调
     */
    private class LKLPrintListener extends AidlPrinterListener.Stub {
        @Override
        public void onPrintFinish() throws RemoteException {
            Log.e(TAG, "打印成功");
        }

        @Override
        public void onError(int arg0) throws RemoteException {
            String msg = "";
            switch (arg0) {
                case 1:
                    break;
                case 2:
                    msg = "打印失败，设备高温异常。";
                    break;
                default:
                    msg = "打印失败，未知异常。";
            }
            Log.e(TAG, msg);
        }
    }
}
