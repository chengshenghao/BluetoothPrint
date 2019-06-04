package cn.raise.app.print.common1;

/**
 * 描述: 打印回调
 * <p/>作者：景阳
 * <p/>创建时间: 2017/4/28 9:39
 */
public interface PrintCallback {
    void onDeviceNotBound();
    void onConnectSuccess();
    void onConnectFailed();
    void onPrintCmdOver();
    void onPrintError(String msg);
}
