package com.yzlm.cyl.cfragment.Adapter;

/**
 * Created by zwj on 2018/3/12.
 */

public class SurplusFileInfo {

    private final int RANGE_SUM = 4;

    private String reagent;// 试剂名称
    private Float[] dosage = new Float[RANGE_SUM];// 试剂量程下使用量
    private int fullBottle; //满瓶量
    private String reagentNum;//试剂阀位

    public void setReagentNum(String reagentNum) {
        this.reagentNum = reagentNum;
    }

    public String getReagentNum() {
        return reagentNum;
    }

    public void setFullBottle(int fullBottle) {
        this.fullBottle = fullBottle;
    }

    public int getFullBottle() {
        return fullBottle;
    }

    public void setReagent(String reagent) {
        this.reagent = reagent;
    }

    public String getReagent() {
        return reagent;
    }

    public void setDosage(int ch, float data) {
        if (ch < RANGE_SUM) {
            dosage[ch] = data;
        }
    }

    public Float getDosage(int ch) {
        if (ch < RANGE_SUM) {
            return dosage[ch];
        } else {
            return Float.valueOf(0);
        }
    }
}
