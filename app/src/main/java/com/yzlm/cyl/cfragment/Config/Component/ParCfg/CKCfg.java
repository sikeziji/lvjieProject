package com.yzlm.cyl.cfragment.Config.Component.ParCfg;

import android.content.Context;

import com.yzlm.cyl.cfragment.Config.MyDBHelper.CompoentHelper.ConfigDBHelper;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config;

/**
 * Created by zwj on 2019/04/26
 */

public class CKCfg extends Config {

    private void InitCKCfg() {

        /*比色LED灯状态**/
        configMap.put("BS_LED", "close");
        configMap.put("CFG_Flow_Switch", "close");// 配置流程开关
    }

    public CKCfg(Context context, String point) {
        configDBHelper = ConfigDBHelper.getInstance(context);
        this.point = point;
        InitCKCfg();
        syncDB();
    }
}
