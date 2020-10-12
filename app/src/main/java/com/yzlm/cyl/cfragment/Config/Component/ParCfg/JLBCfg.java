package com.yzlm.cyl.cfragment.Config.Component.ParCfg;

import android.content.Context;

import com.yzlm.cyl.cfragment.Config.MyDBHelper.CompoentHelper.ConfigDBHelper;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config;

/**
 * Created by caoyiliang on 2016/11/15.
 */

public class JLBCfg extends Config {

    private void InitJLBCfg() {

        /*计量板地址*/
        configMap.put("JLB_1_NAME", "");
        configMap.put("JLB_2_NAME", "");
        configMap.put("JLB_3_NAME", "");
        configMap.put("JLB_4_NAME", "");
        configMap.put("JLB_5_NAME", "");
        configMap.put("JLB_6_NAME", "");
        configMap.put("JLB_7_NAME", "");
        configMap.put("JLB_8_NAME", "");
        configMap.put("JLB_9_NAME", "");
        configMap.put("JLB_10_NAME", "");
        configMap.put("JLB_11_NAME", "");
        configMap.put("JLB_12_NAME", "");
        configMap.put("JLB_13_NAME", "");
        configMap.put("JLB_14_NAME", "");
        configMap.put("JLB_15_NAME", "");
        configMap.put("JLB_16_NAME", "");
        configMap.put("JLB_17_NAME", "");
        configMap.put("JLB_18_NAME", "");
        configMap.put("JLB_19_NAME", "");
        configMap.put("JLB_20_NAME", "");

    }

    public JLBCfg(Context context, String point) {
        configDBHelper = ConfigDBHelper.getInstance(context);
        this.point = point;
        InitJLBCfg();
        syncDB();
    }
}
