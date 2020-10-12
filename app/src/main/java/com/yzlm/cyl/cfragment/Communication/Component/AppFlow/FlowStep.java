package com.yzlm.cyl.cfragment.Communication.Component.AppFlow;

/**
 * Created by caoyiliang on 2017/2/18.
 * 某个组分的某一流程的流程步骤封装类（WL）
 */

public class FlowStep {
    /*流程步骤的代码（WL）*/
    private String code;
    /*流程步骤的参数一，如循环流程步骤的次数等（WL）*/
    private int sampleCount;
    /*流程步骤的参数一，如该流程步骤的运行时间等（WL）*/
    private int measurement;
    /*流程步骤的运行顺序号（WL）*/
    private int step;

    private int actionCoding = 0;

    public int getActionCoding() {
        return actionCoding;
    }

    public void setActionCoding(int coding) {
        actionCoding = coding;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getSampleCount() {
        return sampleCount;
    }

    public void setSampleCount(int sampleCount) {
        this.sampleCount = sampleCount;
    }

    public int getMeasurement() {
        return measurement;
    }

    public void setMeasurement(int measurement) {
        this.measurement = measurement;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }
}
