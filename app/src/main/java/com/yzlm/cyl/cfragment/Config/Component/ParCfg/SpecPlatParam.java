package com.yzlm.cyl.cfragment.Config.Component.ParCfg;

import android.content.Context;
import com.yzlm.cyl.cfragment.Config.MyDBHelper.CompoentHelper.ConfigDBHelper;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config;

/**
 * Created by WL on 2017/4/9.
 * 0505界面光谱界面的参数封装类
 */
public class SpecPlatParam extends Config {
    private void InitSpecPlatParam() {
        //光谱地址
        configMap.put("gpdz", null);
        //积分时间
        configMap.put("jfsj", null);
        //平均次数
        configMap.put("pjcs", null);
        //暗电流处理
        configMap.put("adlcl", null);
        //打灯电压
        configMap.put("dddy", null);
        //打灯次数
        configMap.put("ddcs",null);
        //暗电流补偿
        configMap.put("adlbc", null);
        //自动调节
        configMap.put("zdtj",null);
        //参考波长
        configMap.put("ckbc", null);
        //最低能量
        configMap.put("zdnl", null);
        //最高能量
        configMap.put("zgnl",null);
        //光谱系数K
        configMap.put("gpxsk", null);
        //光谱系数B
        configMap.put("gpxsb",null);
        //光谱测量点
        configMap.put("gpcld",null);
        //光谱测量波长数
        configMap.put("gpcldSum",null);

        //光谱寻峰校准成功标志，读取到true，更新光谱测量点1
        configMap.put("gppeakFinshflag","false");

        //补偿系数K
        configMap.put("gpbck", "1");
        //补偿系数B
        configMap.put("gpbcb","0");
    }


    public SpecPlatParam(Context context, String point) {
        configDBHelper = ConfigDBHelper.getInstance(context);
        this.point = point;
        InitSpecPlatParam();
        syncDB();
    }
}
