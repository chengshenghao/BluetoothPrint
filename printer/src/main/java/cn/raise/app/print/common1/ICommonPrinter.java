package cn.raise.app.print.common1;


import cn.raise.app.print.IPrinter;

/**
 * Created by Administrator on 2017/3/19.
 */

public interface ICommonPrinter extends IPrinter1 {

    /**
     * 绑定设备
     *
     * @param address 设备地址
     * @return 找到指定地址设备返回true
     */
    boolean bindDevice(String address);

    /**
     * 打开连接
     *
     * @return
     */
    void openConnect();

    boolean isOpen();
}
