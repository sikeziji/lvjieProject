package com.yzlm.cyl.cfragment.Communication.NoBoard.AppCalc;

import com.yzlm.cyl.cfragment.Config.NoBoard.CfgTool.NoBoardKbf;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static com.yzlm.cyl.cfragment.Global.context;

/**
 * K、B、F值对象封装类
 * Created by WL on 2017/4/10.
 */

public class NoBoardKbfVal {
    private short year;
    private byte month;
    private byte day;
    private byte hour;
    private byte minute;
    private byte second;

    private String component;

    public NoBoardKbfVal() {
        NoBoardKbf mKbf = new NoBoardKbf(context);
    }

    private double K;

    public double getK() {
        return K;
    }

    public void setK(double k){
        this.K=k;
    }

    private double B;

    public double getB() {
        return B;
    }

    public void setB(double b){
        this.B=b;
    }

    private double F;

    public double getF() {
        return F;
    }

    public void setF(double f){
        this.F=f;
    }

    private String Range;

    public String getRange() {
        return Range;
    }

    public void setRange(String range){
        this.Range=range;
    }

    public void setYear(short year) {
        this.year = year;
    }

    public void setMonth(byte month) {
        this.month = month;
    }

    public void setDay(byte day) {
        this.day = day;
    }

    public void setHour(byte hour) {
        this.hour = hour;
    }

    public void setMinute(byte minute) {
        this.minute = minute;
    }

    public void setSecond(byte second) {
        this.second = second;
    }

    public String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String temp = year + "-" + (int) month + "-" + (int) day + " " + (int) hour + ":" + (int) minute + ":" + (int) second;
        try {
            temp = new Timestamp(format.parse(temp).getTime()).toString();
            if (temp.contains(".")) {
                temp = temp.substring(0, temp.indexOf("."));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return temp;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

}
