package com.yzlm.cyl.cfragment.Config.Component.ParCfg;

import android.content.Context;

import com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config;
import com.yzlm.cyl.cfragment.Config.MyDBHelper.CompoentHelper.ConfigDBHelper;

/**
 * Created by WL on 2017/4/9.
 * 机械手界面的参数封装类
 */
public class RobotPar extends Config {
    private void InitRobotPar() {
        //光耦设置X
        configMap.put("GOSZ_X", "");
        //光耦设置Y
        configMap.put("GOSZ_Y", "");
        //光耦设置Z
        configMap.put("GOSZ_Z", "");
        //坐标点个数
        configMap.put("PpointsNum", "0");
        //坐标点位置
        configMap.put("PpointsAddr", "");   //例: 3-4-5;7-8-9    这样是两个坐标点，分别为(3,4,5),(7,8,9)
    }


    public RobotPar(Context context, String point) {
        configDBHelper = ConfigDBHelper.getInstance(context);
        this.point = point;
        InitRobotPar();
        syncDB();
    }
}
