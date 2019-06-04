package com.tcsl.bluetoothprint.beans;

import android.graphics.Bitmap;

/**
 * 描述: 打印取票号实体
 * <p/>作者：景阳
 * <p/>创建时间: 2017/4/27 14:05
 */
public class PrintBean {

    private Bitmap logo;
    private String mcName;
    private String preferGuide;// 优惠信息
    private String number;//排队号
    private String person;//取号人数
    private String waiting;//前面还有几位
    private String date;
    private String qrCode;//queueUrl+?mcid=4991&orderno=A01
    private String advertise;// 广告语
    private String tableType;// 桌位类型
    private String passTip;//过号的提示
    private boolean printPassTip;//是否打印过号提示
    private String logoStr;//logo对应的字符串，用于进行数据的传输时，进行处理
    public Bitmap getLogo() {
        return logo;
    }

    public void setLogo(Bitmap logo) {
        this.logo = logo;
    }

    public String getMcName() {
        return mcName;
    }

    public void setMcName(String mcName) {
        this.mcName = mcName;
    }

    public String getPreferGuide() {
        return preferGuide;
    }

    public void setPreferGuide(String preferGuide) {
        this.preferGuide = preferGuide;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getWaiting() {
        return waiting;
    }

    public void setWaiting(String waiting) {
        this.waiting = waiting;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getAdvertise() {
        return advertise;
    }

    public void setAdvertise(String advertise) {
        this.advertise = advertise;
    }

    public String getTableType() {
        return tableType;
    }

    public void setTableType(String tableType) {
        this.tableType = tableType;
    }

    public String getPassTip() {
        return passTip;
    }

    public void setPassTip(String passTip) {
        this.passTip = passTip;
    }

    public boolean isPrintPassTip() {
        return printPassTip;
    }

    public void setPrintPassTip(boolean printPassTip) {
        this.printPassTip = printPassTip;
    }

    public String getLogoStr() {
        return logoStr;
    }

    public void setLogoStr(String logoStr) {
        this.logoStr = logoStr;
    }
}
