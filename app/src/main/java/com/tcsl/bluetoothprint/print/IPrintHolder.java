package com.tcsl.bluetoothprint.print;


import com.tcsl.bluetoothprint.beans.QueueBean;

/**
 * 描述: 打印排队小票
 * <p/>作者：景阳
 * <p/>创建时间: 2017/6/6 11:49
 */
public interface IPrintHolder {
    void printQueue(QueueBean bean);

    boolean isPrinting();
}
