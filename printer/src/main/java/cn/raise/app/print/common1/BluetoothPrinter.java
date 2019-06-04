package cn.raise.app.print.common1;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import cn.raise.app.print.common.PrintCallback;

/**
 * Created by Administrator on 2017/3/19.
 */

public class BluetoothPrinter extends CommonPrinter {
    private final static String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";
    /**
     * bluetooth service
     */
    private BtService mBtService;
    private String btAddress;
    private BluetoothAdapter mAdapter;

    public BluetoothPrinter() {
    }

    /**
     * @param address bluetooth ID
     * @return
     */
    @Override
    public boolean bindDevice(String address) {
        if (null == mBtService) {
            mBtService = new BtService();
        }
        this.btAddress = address;
        if (null == mAdapter) {
            mAdapter = BluetoothAdapter.getDefaultAdapter();
        }
        if (TextUtils.isEmpty(address)) {
            printCallback.onDeviceNotBound();
            return false;
        }
        if (mBtService.getState() != BtService.STATE_CONNECTED) {
            if (!TextUtils.isEmpty(btAddress)) {
                BluetoothDevice device = mAdapter.getRemoteDevice(btAddress);
                mBtService.connect(device);
            }
        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    protected synchronized boolean connectToDevice() {
        for (int i = 0; i < 25; i++) {
            if (mBtService.getState() == BtService.STATE_CONNECTED) {
                mOutputStream = mBtService.getmConnectedThread().getMmOutStream();
                return true;
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean closeConnect() {
        Log.e("[Print]BluetoothPrinter", "closeConnect");
        try {
            if (mBtService != null) {
                mBtService.stop();
                mBtService = null;
            }
            if (null != mAdapter) {
                mAdapter = null;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void setPrintCallback(PrintCallback callback) {

    }

    @Override
    public void momentClose() {

    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public boolean isOpen() {
        return mBtService.getState() == BtService.STATE_CONNECTED;
    }

}
