package com.yzlm.cyl.cfragment.Config.NoBoard.ParCfg;

import android.content.Context;

import com.yzlm.cyl.cfragment.Config.MyDBHelper.CompoentNoBoadrdHelper.CompNoBoardConfigDBHelper;
import com.yzlm.cyl.cfragment.Config.NoBoard.CfgTool.NoBoardConfig;

/**
 * Created by caoyiliang on 2016/11/15.
 */

public class NoBoardInfo extends NoBoardConfig {

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
        configMap.put("AUTO_RANGE_JOB_SWITCH","true");// 量程切换后立即启动一次 开关

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
    }


    public NoBoardInfo(Context context, String point) {
        configDBHelper = CompNoBoardConfigDBHelper.getInstance(context);
        this.point = point;
        InitInfo();
        syncDB();
    }
}
