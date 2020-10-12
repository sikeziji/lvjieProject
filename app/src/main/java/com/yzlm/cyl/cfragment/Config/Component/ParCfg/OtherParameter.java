package com.yzlm.cyl.cfragment.Config.Component.ParCfg;

import android.content.Context;

import com.yzlm.cyl.cfragment.Config.MyDBHelper.CompoentHelper.ConfigDBHelper;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config;

import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.QueryMeasCateg;

/**
 * Created by caoyiliang on 2017/3/22.
 */

public class OtherParameter extends Config {
    private void InitOtherParameter() {
        configMap.put("UNIT", QueryMeasCateg(this.point).equals("13") ? "%" : "mg/L");
        configMap.put("YXWS", "2"); // 有效位数
        configMap.put("RELAYCONF", "0");
        configMap.put("ALARM_L", "0");
        configMap.put("ALARM_H", "100");
        configMap.put("CAL_DATA_UPDATE", "false");
        configMap.put("ABSORB_UPDATE", "false");
        configMap.put("ALARM_TIME", "3");
        configMap.put("PRINTER", "false");
        configMap.put("CALIBRATION_HIT", "false");
        configMap.put("CALIBRATION_INTERVAL_DAYS", "0");
        configMap.put("RTU_ID", "1");
        configMap.put("GZ_RTU_ID", "1");
        configMap.put("W300_RTU_ID", "1");
        configMap.put("MeaDataLimitMode", "0");      // 0 无：所有流程防出零  1  负值显示 （开：防出零、定量下限都关）  2 定量下限显示   (开：防出零、定量下限都开)
        configMap.put("SiChuan_RTU_ID", "1");
    }


    public OtherParameter(Context context, String point) {
        configDBHelper = ConfigDBHelper.getInstance(context);
        this.point = point;
        InitOtherParameter();
        syncDB();
    }
}
