package com.yzlm.cyl.cfragment.Config.Component.ParCfg;

import android.content.Context;

import com.yzlm.cyl.cfragment.Config.MyDBHelper.CompoentHelper.ConfigDBHelper;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config;

/**
 * Created by caoyiliang on 2016/12/30.
 */

public class MeasureParameter extends Config {
    private void InitMeasureParameter() {
        configMap.put("xjwd", null);// 消解温度
        configMap.put("xjsc", null);// 消解时长
        configMap.put("xswd", null);// 显色温度
        configMap.put("xssc", null);// 显色时长
        configMap.put("xjjw", null); // 消解降温
        configMap.put("xsjw", null);// 显色降温
        configMap.put("xsjz", null);// 显色静置
        configMap.put("waitStanding", null);// 等待静置


        configMap.put("WATER_VOLUME", null);// 水样体积
        configMap.put("OXIDANT_VOLUME", null);// 氧化剂体积
        configMap.put("REDUCTANT_VOLUME", null);// 还原剂体积
        configMap.put("REDUCTANT_COC", null);// 还原剂浓度
        configMap.put("TITRATION_VOLUME", null);// 滴定液体积

        configMap.put("calibrationTemp", null);// 校准消解温度
        configMap.put("calibrationTime", null);// 校准消解时长

        configMap.put("catalystVolume", null);// 催化剂体积

        configMap.put("lyeVolume", null);// 碱液体积

        //TOC 参数
        configMap.put("BurningTemperature", "680");    // 燃烧温度
        configMap.put("BurningDownTemperature", "40");     // 下降温度

        configMap.put("ToxicityTestTime", "15");     // 毒性测试时间
    }


    public MeasureParameter(Context context, String point) {
        configDBHelper = ConfigDBHelper.getInstance(context);
        this.point = point;
        InitMeasureParameter();
        syncDB();
    }
}
