package com.yzlm.cyl.cfragment.Config.Component.ParCfg;

import android.content.Context;

import com.yzlm.cyl.cfragment.Config.MyDBHelper.CompoentHelper.ConfigDBHelper;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config;

/*
 * Created by zwj on 2019/2/21.
 */

public class AuthFuncParam extends Config {

    private void InitAuthFuncParam() {

        /*认证开门检测功能*/
        //configMap.put("MeaA", "");
       // configMap.put("MeaB", "0");// 认证测量值B
       // configMap.put("MeaC", "0");// 认证测量值C
        //configMap.put("CheckTimePoint", "59");//认证检查出数时间
        //configMap.put("AuthOpenDoorFuncSwitch", "close");//认证开门检测功能开关
        configMap.put("DoorStatus", "open");//门状态，open 开门状态，close:关门状态
        configMap.put("DoorStatusOpenTime",""); // 记录开门时间
       // configMap.put("OpenDoorAfterMeaDataCounter", "0");//开门状态后出数个数计数器
        configMap.put("START_ZY_JOB_TIME", "");//记录做样的时间
        configMap.put("StartFlowName", "");//记录启动流程
        configMap.put("StopFlowTime", "");//记录停止测量时间

        configMap.put("DoorSignal1", "open");
        configMap.put("DoorSignal2", "open");

        //configMap.put("MeaDFuncSwitch", "close");// 比武测量值D 通过协议设置该值（可根据该值出数）功能开关
        //configMap.put("MeaD", "");// 比武测量值D 通过协议设置该值（可根据该值出数）
        //configMap.put("MeaDWaveValue", "2.5");// 比武测量值D 波动值 %
        //configMap.put("PowerCommSwitch", "close");  // 通信开关 // open  close
        //configMap.put("PowerCommProductId", "1");     // 通信产品ID
        //configMap.put("PowerCommIsMaster", "false");     //true/false    //主从关系
        //configMap.put("PowerCommCheckTime", "57");  //电力通信检查时间
        //configMap.put("PowerCommSlaveAddress","");
        //configMap.put("PowerCommCalData", "");  // 计算后数据

    }

    public AuthFuncParam(Context context, String point) {
        configDBHelper = ConfigDBHelper.getInstance(context);
        this.point = point;
        InitAuthFuncParam();
        syncDB();
    }
}
