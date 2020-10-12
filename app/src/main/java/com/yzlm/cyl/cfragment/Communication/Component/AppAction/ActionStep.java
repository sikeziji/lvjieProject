package com.yzlm.cyl.cfragment.Communication.Component.AppAction;

/**
 * Created by caoyiliang on 2017/2/21.
 */

public class ActionStep {
    private String name;
    private byte sampleCount;
    private byte measurement;
    private String window=""; //是:修改窗体 否:使用默认窗体

    public void setWindow(String window) {
        this.window = window;
    }

    public String getWindow() {
        return window;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte getSampleCount() {
        return sampleCount;
    }

    public void setSampleCount(byte sampleCount) {
        this.sampleCount = sampleCount;
    }

    public byte getMeasurement() {
        return measurement;
    }

    public void setMeasurement(byte measurement) {
        this.measurement = measurement;
    }
}
