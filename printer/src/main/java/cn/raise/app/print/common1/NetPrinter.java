package cn.raise.app.print.common1;

import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.net.Socket;

import cn.raise.app.print.common.PrintCallback;

/**
 * 描述: 网口打印
 * <p/>作者：景阳
 * <p/>创建时间: 2017/4/25 16:52
 */
public class NetPrinter extends CommonPrinter {

    /**
     * 通用的网口打印机，打印端口都是9100
     */
    private final int PORT = 9100;
    private Socket socket;
    private String mAddress;

    /**
     *
     * @param address 设备IP地址
     * @return
     */
    @Override
    public boolean bindDevice(final String address) {
        this.mAddress = address;
        Log.i("[Print]NetPrinter", "bindDevice:" + address);
        if (TextUtils.isEmpty(mAddress)) {
            printCallback.onDeviceNotBound();
            return false;
        }
        return true;
    }

    @Override
    protected synchronized boolean connectToDevice() {
        boolean result = false;
        try {
            socket = new Socket(mAddress, PORT);
            Log.i("[Print]NetPrinter", mAddress + ":" + PORT + " connected");
            mOutputStream = socket.getOutputStream();
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean closeConnect() {
        Log.i("[Print]NetPrinter", "closeConnect");
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public void setPrintCallback(PrintCallback callback) {

    }

    @Override
    public void momentClose() {
        closeConnect();
    }

    @Override
    public boolean isOpen() {
        return socket != null && socket.isConnected();
    }
}
