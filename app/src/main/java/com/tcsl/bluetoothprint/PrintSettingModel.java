package com.tcsl.bluetoothprint;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.tcsl.bluetoothprint.beans.PrintBean;

import cn.raise.app.print.utils.BitmapUtil;

/**
 * 描述: 测试打印页的业务
 * <p/>作者：景阳
 * <p/>创建时间: 2017/6/2 14:09
 */
public class PrintSettingModel {
    /**
     * 测试打印组装打印的测试数据
     *
     * @return
     */
    public PrintBean getTestPrintBean(String logoPath, String mcName, String guide, String advertise, String qrCode, String passTip, boolean printPassTip) {
        PrintBean bean = new PrintBean();
        try {
            Bitmap bitmap = BitmapUtil.compressPrintBitmap(MyApplication.getInstance(), logoPath);
            bean.setLogo(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
            bean.setLogo(null);
        }
        if (TextUtils.isEmpty(mcName)) {
            mcName = "未知商户";
        }
        bean.setMcName(mcName);
        bean.setPassTip(passTip);
        bean.setPreferGuide(guide);
        bean.setPrintPassTip(printPassTip);
        bean.setNumber("A102");
        bean.setPerson("1");
        bean.setWaiting("2");
        bean.setDate("2017-03-12 10:21:15");
        bean.setTableType("小桌");
        bean.setAdvertise(advertise);
        bean.setQrCode(qrCode);
        return bean;
    }

}
