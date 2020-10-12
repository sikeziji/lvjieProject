package com.yzlm.cyl.cfragment.Config.NoBoard.ParCfg;

import android.content.Context;

import com.yzlm.cyl.cfragment.Config.MyDBHelper.CompoentNoBoadrdHelper.CompNoBoardConfigDBHelper;
import com.yzlm.cyl.cfragment.Config.NoBoard.CfgTool.NoBoardConfig;

/**
 * Created by caoyiliang on 2016/12/30.
 */

public class NoBoardDataCorrectParam extends NoBoardConfig {
    private void InitDataCorrectParam() {
        // 下限系数 检出限
        configMap.put("limitSwitch", "open"); // 开关    // open   close
        configMap.put("limitValue", null);    // 门限值
        configMap.put("limitLow", null);    // 下限
        configMap.put("limitHigh", null);   // 上限

        // 校准失败判定值  F
        configMap.put("FMin", null);   //
        configMap.put("FMax", null);   //

        // 超当前量程告警及限制出数
        configMap.put("alarmOutNowRangeSwitch", "open"); // 超量程告警开关    // open   close
        configMap.put("nowRangeUpMulti", "1");    // 上限倍数
        configMap.put("alarmOutNowRangeOutLimit", "close"); // 超量程限制出数开关    // open   close


        // 超仪表量程告警及限制出数
        configMap.put("alarmOutDevRangeSwitch", "open"); // 超量程告警开关    // open   close
        configMap.put("devRangeUpMulti", "1");    // 上限倍数
        configMap.put("alarmOutDevRangeOutLimit", "close"); // 超仪表量程限制出数开关    // open   close


        // 定量下限系数// 只关联做样
        configMap.put("QuantitativeLimitSwitch", "open"); // 开关    // open   close
        configMap.put("QuantitativeLimitValue", null);    // 门限
        configMap.put("QuantitativeLimitLow", null);    // 下限
        configMap.put("QuantitativeLimitHigh", null);   // 上限
    }


    public NoBoardDataCorrectParam(Context context, String point) {
        configDBHelper = CompNoBoardConfigDBHelper.getInstance(context);
        this.point = point;
        InitDataCorrectParam();
        syncDB();
    }
}
