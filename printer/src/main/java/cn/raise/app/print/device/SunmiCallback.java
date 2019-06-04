package cn.raise.app.print.device;

import android.os.RemoteException;

import woyou.aidlservice.jiuiv5.ICallback;

/**
 * 描述: 商米回调
 * <p/>作者：景阳
 * <p/>创建时间: 2017/5/11 11:57
 */
public class SunmiCallback extends ICallback.Stub {
    @Override
    public void onRunResult(boolean b) throws RemoteException {

    }

    @Override
    public void onReturnString(String s) throws RemoteException {

    }

    @Override
    public void onRaiseException(int i, String s) throws RemoteException {

    }
}
