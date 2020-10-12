package com.yzlm.cyl.cfragment.Config.NoBoard.ParCfg;

import android.content.Context;

import com.yzlm.cyl.cfragment.Config.MyDBHelper.CompoentNoBoadrdHelper.CompNoBoardConfigDBHelper;
import com.yzlm.cyl.cfragment.Config.NoBoard.CfgTool.NoBoardConfig;

/**
 * Created by caoyiliang on 2017/3/22.
 */

public class NoBoardOtherParameter extends NoBoardConfig {
    private void InitOtherParameter() {
        configMap.put("UNIT", "mg/L");
        configMap.put("YXWS", "2"); // 有效位数
        configMap.put("ALARM_L", "0");
        configMap.put("ALARM_H", "100");
        configMap.put("CAL_DATA_UPDATE", "false");
        configMap.put("ABSORB_UPDATE", "false");
        configMap.put("ALARM_TIME", "3");
        configMap.put("PRINTER","false");



        configMap.put("RTU_ID","1");
    }


    public NoBoardOtherParameter(Context context, String point) {
        configDBHelper = CompNoBoardConfigDBHelper.getInstance(context);
        this.point = point;
        InitOtherParameter();
        syncDB();
    }
}
