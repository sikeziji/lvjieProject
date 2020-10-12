package com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool;

/**
 * Created by zwj on 2017/5/15.
 */

public class DataStruct {

    private int year = 0;
    private int month = 0;
    private int day = 0;
    private int hour = 0;
    private int min = 0;
    private int sec = 0;

    private float data = 0;
    private String type;
    private String flow;
    private float ai=0;
    private String dataTag;// 数据标识
    String strEngs;//

    public void setData(float data) {
        this.data = data;
    }

    public float getData() {
        return data;
    }

    public void setDataTime(int year, int month, int day, int hour, int min, int sec) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.min = min;
        this.sec = sec;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public int getMin() {
        return min;
    }

    public int getSec() {
        return sec;
    }

    public String getDataTime() {
        return year + "-" + month + "-" + day + " " + hour + ":" + min + ":" + sec;
    }

    public void setFlow(String flow) {
        this.flow = flow;
    }

    public String getFlow() {
        return flow;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setAi(float ai) {
        this.ai = ai;
    }

    public float getAi() {
        return ai;
    }

    public String getDataTag() {
        return dataTag;
    }

    public void setDataTag(String dataTag) {
        this.dataTag = dataTag;
    }

    public void setStrEngs(String strEngs) {
        this.strEngs = strEngs;
    }

    public String getStrEngs() {
        return strEngs;
    }

}
