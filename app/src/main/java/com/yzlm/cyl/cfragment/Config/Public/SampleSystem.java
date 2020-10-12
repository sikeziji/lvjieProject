package com.yzlm.cyl.cfragment.Config.Public;

import android.content.Context;

import com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig;
import com.yzlm.cyl.cfragment.Config.MyDBHelper.CompoentHelper.ConfigDBHelper;



/*
 * Created by zwj on 2019/8/21.
 */

public class SampleSystem extends PublicConfig {
    private void InitCommPublic() {

        configMap.put("CONTROL_CHANNEL", "0");  /*反控测量，通道*/
        configMap.put("CONTROL_DEV", "0");      /*反控测量，设备*/
        configMap.put("DEV_UNIT", "0");         /*反控测量，单位*/
        configMap.put("DEV_RANGE", "0");        /*反控测量，量程*/
        configMap.put("DEV_DOOR_LIMIT", "0");   /*反控测量，门限*/
        configMap.put("SAMPLE_SYSTEM_ID", "0");   /*采样系统A(常规预处理) 采样系统B（均质预处理）*/

        configMap.put("SAMPLE_LEVEL_SWITCH", "1");   /*采样系统 液位报警开关 默认开*/
        configMap.put("CONTROL_RANGE", "0");         /*反控测量量程*/
        configMap.put("CONTROL_DOOR_LIMIT", "0");    /*反控测量门限*/
    }


    public SampleSystem(Context context) {
        configDBHelper = ConfigDBHelper.getInstance(context);
        InitCommPublic();
        syncDB();
    }
}
