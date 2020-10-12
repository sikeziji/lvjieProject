package com.yzlm.cyl.cfragment.Config.Component.ParCfg;

import android.content.Context;

import com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config;
import com.yzlm.cyl.cfragment.Config.MyDBHelper.CompoentHelper.ConfigDBHelper;

/**
 * Created by caoyiliang on 2016/12/30.
 */

public class DataCorrectParam extends Config {
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

        // 一键校准全部量程
        configMap.put("OneKeyCalSwitch", "close"); // 开关    // open   close，决定是否其它量程需要刷新KB
        configMap.put("OneKeyCalX", "");   //一键校准X
        configMap.put("OneKeyCalY", "1");   //一键校准Y
        configMap.put("OneKeyCalB", "");   //一键校准B
        configMap.put("OneKeyCalForceXFlag", "false");   //强制刷新X值
        configMap.put("OneKeyCalForceKBFlag", "false");   //强制刷新量程23KB (当量程校准是强制校准时候触发)

        configMap.put("OneKeyCalRange1RSA", "1"); //各量程算法   算法1:    X  算法2:    2*X      算法3：(1+X)*X
        configMap.put("OneKeyCalRange2RSA", "2"); //各量程算法
        configMap.put("OneKeyCalRange3RSA", "3"); //各量程算法
        configMap.put("OneKeyCalStartFlag", "false"); //校准X值：启动量程2 标样2测量
        configMap.put("OneKeyCalXRSA", "1"); //计算X的算法  算法1：X = B/(A*k1+b1) 算法2：X = B/(2(A*k1+b1))

        // 定量下限系数// 只关联做样
        configMap.put("QuantitativeLimitSwitch", "open"); // 开关    // open   close
        configMap.put("QuantitativeLimitValue", null);    // 门限
        configMap.put("QuantitativeLimitLow", null);    // 下限
        configMap.put("QuantitativeLimitHigh", null);   // 上限

        //只有比色有，所以无背板模式不需要
        configMap.put("XJYZ", 50);    // 消解阈值
        configMap.put("PJSJ", 1);     // 平均时间

        // 葡萄糖测试
        configMap.put("GlucoseSwitch1", "close");// 开关1    // open   close
        configMap.put("GlucoseSwitch2", "close");// 开关2    // open   close
        configMap.put("GlucoseA1", "");//第一次修正  电极常数下限
        configMap.put("GlucoseB1", "");//第一次修正  浓度下限
        configMap.put("GlucoseCorrect1", "");//第一次修正  修正值
        configMap.put("GlucoseA2", "");//第二次修正  浓度下限
        configMap.put("GlucoseB2", "");//第二次修正  浓度上限
        configMap.put("GlucoseCorrect2", "");//第二次修正  修正值

        //标核判定
        configMap.put("BHPD", "close");   // 标核判定开关    // open   close
        configMap.put("BHJZCon", "");   // 标核校准浓度
        configMap.put("BHJZCount", "");   // 标核校准判断次数
        configMap.put("BHJZFlag", "false");   // 标核校准判断标志位

        //W200 CODcr 跳值修正
        configMap.put("DoorCloseDataCounter", "0");//关门后出数计数
        configMap.put("MeaAValueEn", "");               //  缓存值 和 吸收能量 用";"分隔
        configMap.put("MasterCtrlDoorSignal", "open");   //主控板开关量信号(开门检测)
        configMap.put("MasterCtrlDoorStatus", "open");   //主控板门状态，open 开门状态，close:关门状态
        configMap.put("MasterCtrlDoorStatusOpenTime", ""); // 主控板记录开门时间

    }


    public DataCorrectParam(Context context, String point) {
        configDBHelper = ConfigDBHelper.getInstance(context);
        this.point = point;
        InitDataCorrectParam();
        syncDB();
    }
}
