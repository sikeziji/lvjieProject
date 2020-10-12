package com.yzlm.cyl.cfragment.Config.Component.ParCfg;

import android.content.Context;

import com.yzlm.cyl.cfragment.Config.MyDBHelper.CompoentHelper.ConfigDBHelper;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config;

/**
 * Created by caoyiliang on 2016/11/15.
 */

public class SurplusCfg extends Config {
    private void InitSurplusCfg() {

        configMap.put("surplusOpen", "false");

        /*满瓶*/
        configMap.put("fullBottle1", "0");
        configMap.put("fullBottle2", "0");
        configMap.put("fullBottle3", "0");
        configMap.put("fullBottle4", "0");
        configMap.put("fullBottle5", "0");
        configMap.put("fullBottle6", "0");
        configMap.put("fullBottle7", "0");

        /*试剂余量*/
        configMap.put("surplus1", "0");
        configMap.put("surplus2", "0");
        configMap.put("surplus3", "0");
        configMap.put("surplus4", "0");
        configMap.put("surplus5", "0");
        configMap.put("surplus6", "0");
        configMap.put("surplus7", "0");
        configMap.put("surplusAlarm", "10");
        configMap.put("surplusAlarm2", "80");//废液超标报警
    }

    public SurplusCfg(Context context, String point) {
        configDBHelper = ConfigDBHelper.getInstance(context);
        this.point = point;
        InitSurplusCfg();
        syncDB();
    }
}
