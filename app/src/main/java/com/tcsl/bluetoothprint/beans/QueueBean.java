package com.tcsl.bluetoothprint.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 描述:排队表
 * <p/>作者：wjx
 * <p/>创建时间: 2017/5/5 10:16
 */
public class QueueBean implements Parcelable {
    public static final int TYPE_PASS = 4;
    public static final int TYPE_CALL = 2;
    public static final int TYPE_CANCEL = 1;
    public static final int TYPE_EAT = 3;
    public static final int TYPE_DEL = 6;
    public static final int TYPE_INIT = 0;
    /**
     * 批次号
     */
    String id;
    /**
     * 排队名称
     */
    String queueName;
    /**
     * 排号
     */
    String queueNo;
    /**
     * 会员卡名称
     */
    String crmName;
    /**
     * 客位类型
     */
    String tableId;
    /**
     * 人数
     */
    int count;
    /**
     * 等待时间
     */
    String duration;
    /**
     * 来源标识  0：线下，1：线上
     */
    int source;
    /**
     * 状态标识  0：排队，1：就餐，2：过号，3：放弃
     */
    int state;
    /**
     * 号码归零标识  0：未归零  1：归零
     */
    int queueClear;
    /**
     * 日期
     */
    String date;
    /**
     * 叫号次数
     */
    int callTime;
    /**
     * 上传标志 0：未上传 1：已上传
     */
    int upload;
    /**
     * 插入时间
     */
    String time;
    /**
     * 手机号
     */
    String phoneNum;
    /**
     * 是否保留 0：不保留  1：保留
     */
    int keep = 0;
    /**
     * 最后一次操作时间
     */
    String operateTime;
    /**
     * 线上获取到的二维码
     */
    String qrcode;
    /**
     * 备注信息
     */
    String remark;

    /**
     * 线上订单号
     */
    String onlineId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public String getQueueNo() {
        return queueNo;
    }

    public void setQueueNo(String queueNo) {
        this.queueNo = queueNo;
    }

    public String getCrmName() {
        return crmName;
    }

    public void setCrmName(String crmName) {
        this.crmName = crmName;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getQueueClear() {
        return queueClear;
    }

    public void setQueueClear(int queueClear) {
        this.queueClear = queueClear;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getCallTime() {
        return callTime;
    }

    public void setCallTime(int callTime) {
        this.callTime = callTime;
    }

    public int getUpload() {
        return upload;
    }

    public void setUpload(int upload) {
        this.upload = upload;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public int getKeep() {
        return keep;
    }

    public void setKeep(int keep) {
        this.keep = keep;
    }

    public String getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(String operateTime) {
        this.operateTime = operateTime;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOnlineId() {
        return onlineId;
    }

    public void setOnlineId(String onlineId) {
        this.onlineId = onlineId;
    }

    @Override
    public String toString() {
        return "QueueBean{" +
                "id='" + id + '\'' +
                ", queueName='" + queueName + '\'' +
                ", queueNo='" + queueNo + '\'' +
                ", crmName='" + crmName + '\'' +
                ", tableId='" + tableId + '\'' +
                ", count=" + count +
                ", duration='" + duration + '\'' +
                ", source=" + source +
                ", state=" + state +
                ", queueClear=" + queueClear +
                ", date='" + date + '\'' +
                ", callTime=" + callTime +
                ", upload=" + upload +
                ", time='" + time + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", keep=" + keep +
                ", operateTime='" + operateTime + '\'' +
                ", qrcode='" + qrcode + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }

    public QueueBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.queueName);
        dest.writeString(this.queueNo);
        dest.writeString(this.crmName);
        dest.writeString(this.tableId);
        dest.writeInt(this.count);
        dest.writeString(this.duration);
        dest.writeInt(this.source);
        dest.writeInt(this.state);
        dest.writeInt(this.queueClear);
        dest.writeString(this.date);
        dest.writeInt(this.callTime);
        dest.writeInt(this.upload);
        dest.writeString(this.time);
        dest.writeString(this.phoneNum);
        dest.writeInt(this.keep);
        dest.writeString(this.operateTime);
        dest.writeString(this.qrcode);
        dest.writeString(this.remark);
    }

    protected QueueBean(Parcel in) {
        this.id = in.readString();
        this.queueName = in.readString();
        this.queueNo = in.readString();
        this.crmName = in.readString();
        this.tableId = in.readString();
        this.count = in.readInt();
        this.duration = in.readString();
        this.source = in.readInt();
        this.state = in.readInt();
        this.queueClear = in.readInt();
        this.date = in.readString();
        this.callTime = in.readInt();
        this.upload = in.readInt();
        this.time = in.readString();
        this.phoneNum = in.readString();
        this.keep = in.readInt();
        this.operateTime = in.readString();
        this.qrcode = in.readString();
        this.remark = in.readString();
    }

    public static final Creator<QueueBean> CREATOR = new Creator<QueueBean>() {
        @Override
        public QueueBean createFromParcel(Parcel source) {
            return new QueueBean(source);
        }

        @Override
        public QueueBean[] newArray(int size) {
            return new QueueBean[size];
        }
    };
}
