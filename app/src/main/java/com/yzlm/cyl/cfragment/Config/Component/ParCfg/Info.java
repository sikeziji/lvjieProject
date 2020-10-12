package com.yzlm.cyl.cfragment.Config.Component.ParCfg;

import android.content.Context;

import com.yzlm.cyl.cfragment.Config.MyDBHelper.CompoentHelper.ConfigDBHelper;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config;

/**
 * Created by caoyiliang on 2016/11/15.
 */

public class Info extends Config {

    private void InitInfo() {

        configMap.put("HMIBH", 0);
        configMap.put("YQBH", 0);
        configMap.put("YBLCL", 0);
        configMap.put("YBLCH", 0);
        configMap.put("LC1L", 0);
        configMap.put("LC1H", 0);
        configMap.put("LC2L", 0);
        configMap.put("LC2H", 0);
        configMap.put("LC3L", 0);
        configMap.put("LC3H", 0);
        configMap.put("ZGSJND", 0);
        configMap.put("AUTORANGE", 0);
        configMap.put("AUTO_RANGE_JOB_SWITCH", "true");// 量程切换后立即启动一次 开关
        configMap.put("AUTO_RANGE_SWITCH_RECOVERY", "false");// 量程切换后切换回主量程 开关 2019年8月27日13:52:10
        configMap.put("AUTORANGE_MAIN_RANGE", "1");//开启量程切换后的主量程    2019年8月27日13:52:13

        configMap.put("RANGE", "1");
        configMap.put("RANGE_AUTO_CHANGE_COUNT", "0");
        configMap.put("RANGE_AUTO_CHANGE_START_FLAG", "false");
        configMap.put("RANGE_AUTO_CHANGE_FLOW", "");// 量程切换要启动流路名称

        /*量程切换过程中，缺失数据的时间点*/
        configMap.put("RANGE_AUTO_MISS_TIME", "");
        /*测量启动时间*/
        configMap.put("JOB_START_TIME", "");
        /*切换量程之后进行一次检查定点时间启动一次测量**/
        configMap.put("RANGE_AUTO_CHECK_SET_POINT_DO_JOB_FLAG", "false");

        /*出厂时间*/
        configMap.put("FACTORY_TIME", "");

        /*VOC 组份选择*/
        configMap.put("VOC_V1", "false");
        configMap.put("VOC_V2", "false");
        configMap.put("VOC_V3", "false");
        configMap.put("VOC_V4", "false");
        configMap.put("VOC_V5", "false");
        configMap.put("VOC_V6", "false");
        configMap.put("VOC_V7", "false");
        configMap.put("VOC_V8", "false");
        configMap.put("VOC_V9", "false");
        configMap.put("VOC_V10", "false");

        /*稀释基础量程*/
        configMap.put("XS_BASE_RANGE", "1");// 默认量程1  组分参数

        /*稀释模式*/
        configMap.put("XS_MODE", "1");// 稀释背板参数

        /*校准，标1启动标志*/ //为了记录过程中是否有报错
        configMap.put("CAL_Start_Flag", "false");

        /*校准，标2启动标志*/ //为了记录过程中是否有报错
        configMap.put("CAL2_Start_Flag", "false");

        /*校准，标1成功标志*/
        configMap.put("CAL_Suc_Flag", "");

        /*校准，标2成功标志*/
        configMap.put("CAL2_Suc_Flag", "");

        /*校准，标1时间校准启动时间*/
        configMap.put("CAL_TIME", "");

        /*校准成功后，标1时间校准启动时间*/
        configMap.put("CAL_SUC_TIME", "");

        /*校准成功后，标2时间校准启动时间*/
        configMap.put("CAL2_SUC_TIME", "");

        /*报错后出数标记*/
        configMap.put("ERROR_RECORD_MEA_HISTORY_FLAG", "false");


        // 量程1 标1 吸光度值
        configMap.put("Range1_CAL1_A_Value", "");
        // 量程2 标1 吸光度值
        configMap.put("Range2_CAL1_A_Value", "");
        // 量程3 标1 吸光度值
        configMap.put("Range3_CAL1_A_Value", "");

        // 量程1 标2 吸光度值
        configMap.put("Range1_CAL2_A_Value", "");
        // 量程2 标2 吸光度值
        configMap.put("Range2_CAL2_A_Value", "");
        // 量程3 标2 吸光度值
        configMap.put("Range3_CAL2_A_Value", "");

        /*校准，是否手动校准*/
        configMap.put("CAL_CONTROL", "false");

        /*体积校准标志*/
        configMap.put("Volume_Cal", "false");

        /*废液再生 K1*/
        configMap.put("fyzs_k1", "0.0111");
        /*废液再生 K2*/
        configMap.put("fyzs_k2", "0.0111");
        /*废液再生 B1*/
        configMap.put("fyzs_b1", "1");
        /*废液再生 原始B值*/
        configMap.put("fyzs_b", "0");
        /*废液再生 加入量M*/
        configMap.put("fyzs_M", "0");

        /*反控多点线性标志 当反控仪表标样核查时为true，参与修正。当仪表自己启动标样核查时为false，不参与修正*/
        configMap.put("Multi_Point_Linear", "false");

        // 量程1 标1 历史记录时间
        configMap.put("Range1_CAL1_His_Time", "");
        // 量程2 标1 历史记录时间
        configMap.put("Range2_CAL1_His_Time", "");
        // 量程3 标1 历史记录时间
        configMap.put("Range3_CAL1_His_Time", "");

        // 量程1 标2 历史记录时间
        configMap.put("Range1_CAL2_His_Time", "");
        // 量程2 标2 历史记录时间
        configMap.put("Range2_CAL2_His_Time", "");
        // 量程3 标2 历史记录时间
        configMap.put("Range3_CAL2_His_Time", "");
    }


    public Info(Context context, String point) {
        configDBHelper = ConfigDBHelper.getInstance(context);
        this.point = point;
        InitInfo();
        syncDB();
    }
}
