package com.tcsl.bluetoothprint.print;

import android.text.TextUtils;

import com.tcsl.bluetoothprint.beans.PrintBean;
import com.tcsl.bluetoothprint.utils.LineCreater;
import com.tcsl.bluetoothprint.utils.LocalXml;

import java.util.ArrayList;
import java.util.List;

import cn.raise.app.print.IPrinter;
import cn.raise.app.print.model.BasePrintModel;
import cn.raise.app.print.model.BitmapModel;
import cn.raise.app.print.model.FakeQrCodeModel;
import cn.raise.app.print.model.LineModel;
import cn.raise.app.print.model.PageEndModel;
import cn.raise.app.print.model.QrCodeModel;
import cn.raise.app.print.model.TextModel;
import cn.raise.app.print.utils.BitmapUtil;

import static com.tcsl.bluetoothprint.print.QueuePrintManager.ENGINE_BT;
import static com.tcsl.bluetoothprint.print.QueuePrintManager.ENGINE_NET;


/**
 * 描述:
 *
 * @author wjx
 * @date 2018/7/27 10:56
 */
public class PrintUtils {
    /**
     * 组装单据文字内容
     *
     * @param bean
     * @return
     */
    public static List<BasePrintModel> getTextList(PrintBean bean) {
        List<BasePrintModel> list = new ArrayList<>();

        int paperType = LocalXml.getPrintPaperMode();
        // 受setting控制
        if (LocalXml.getLogoSwitch() && bean.getLogo() != null) {
            // logo
            BitmapModel bModel = new BitmapModel();
            // print模块里的打印代码只对黑白二值进行打印处理，所以为控制图像黑白程度，先将图片黑白化
            bModel.setBitmap(BitmapUtil.toHeibai(bean.getLogo(), 220, 10));
            bModel.setAlign(1);
            /*if (BuildConfig.DEVICE_TYPE == DeviceUtil.BUILD_CONFIG_SUNMI) {
                bModel.setPageGo(2);
            } else {
                bModel.setPageGo(1);
            }*/
            bModel.setPageGo(1);
            bModel.setWidth(196);
            bModel.setHeight(196);
            bModel.setPaperType(paperType);
            list.add(bModel);
        }

        // 门店名称
        TextModel model = new TextModel(bean.getMcName());
        model.setFontAlign(1);
        model.setFontSize(3);
        model.setEnter(true);
        list.add(model);
        // line
        LineModel lModel = LineCreater.create();
        lModel.setEnter(true);
        list.add(lModel);
        model = new TextModel("");
        model.setFontAlign(1);
        model.setEnter(true);
        list.add(model);
        // 排号
        model = new TextModel(bean.getTableType() + "：" + bean.getNumber());
        model.setFontAlign(1);
        model.setFontSize(6);
        model.setEnter(true);
        list.add(model);
        model = new TextModel("");
        model.setFontAlign(1);
        model.setEnter(true);
        list.add(model);
        int engine = LocalXml.getPrinterType();
//        if (engine == ENGINE_BT || engine == ENGINE_NET||BuildConfig.FLAVOR.equals("sunmi")) {
        if (engine == ENGINE_BT || engine == ENGINE_NET) {
            model = new TextModel("人数:");
            model.setFontAlign(1);
            list.add(model);
            model = new TextModel(bean.getPerson());
            model.setFontAlign(1);
            model.setFontSize(3);
            list.add(model);
            model = new TextModel(",前面还有 ");
            model.setFontAlign(1);
            list.add(model);
            model = new TextModel(bean.getWaiting());
            model.setFontAlign(1);
            model.setFontSize(3);
            list.add(model);
            model = new TextModel(" 桌等候");
            model.setFontAlign(1);
            model.setEnter(true);
            list.add(model);
        } else {
            model = new TextModel("人数" + bean.getPerson() + ",前面还有 " + bean.getWaiting() + " 桌等候");
            model.setFontAlign(1);
            model.setEnter(true);
            list.add(model);
        }
        // 取号时间
        if (bean.getDate().length() > 20) {
            model = new TextModel(bean.getDate().substring(0, 20));
        } else {
            model = new TextModel(bean.getDate());
        }
        model.setFontAlign(1);
        model.setEnter(true);
        list.add(model);
        if (bean.isPrintPassTip()) {
            //提示语
            model = new TextModel(bean.getPassTip());
            model.setFontAlign(1);
            model.setEnter(true);
            list.add(model);
        }
        // line
        lModel = LineCreater.create();
        lModel.setEnter(true);
        list.add(lModel);
        // 受setting控制
        if (LocalXml.getPreferSwitch()) {
            //优惠引导语
            model = new TextModel(bean.getPreferGuide());
            model.setEnter(true);
            list.add(model);
        }

        // 商家没有公众号则不打印二维码
        if (!TextUtils.isEmpty(bean.getQrCode())) {
            if (engine == ENGINE_BT && LocalXml.getPrintQrType()) {
                //蓝牙模式且选择的是指令打印方式，调用二维码的打印方式
                // 二维码
                FakeQrCodeModel qModel = new FakeQrCodeModel();
                qModel.setQrCode(bean.getQrCode());
                qModel.setPaperType(paperType);
                // 必须是1，否则不换行就打下一行了
                qModel.setPageGo(1);
                if (paperType == IPrinter.PAPER_58) {
                    qModel.setWidth(240);
                    qModel.setHeight(240);
                } else {// 80
                    qModel.setWidth(280);
                    qModel.setHeight(280);
                }
                qModel.setAlign(1);
                list.add(qModel);
            } else {
                // 二维码
                QrCodeModel qModel = new QrCodeModel();
                qModel.setQrCode(bean.getQrCode());
                qModel.setPaperType(paperType);
                // 必须是1，否则不换行就打下一行了
                qModel.setPageGo(1);
                if (paperType == IPrinter.PAPER_58) {
                    qModel.setWidth(240);
                    qModel.setHeight(240);
                } else {// 80
                    qModel.setWidth(280);
                    qModel.setHeight(280);
                }
                qModel.setAlign(1);
                list.add(qModel);
            }
        }
        // 受setting控制
        if (LocalXml.getAdvertiseSwitch()) {
            // 广告语
            TextModel tModel = new TextModel(bean.getAdvertise());
            tModel.setEnter(true);
            list.add(tModel);
        }
        // 走纸、切纸
        list.add(new PageEndModel());
        return list;
    }
}
