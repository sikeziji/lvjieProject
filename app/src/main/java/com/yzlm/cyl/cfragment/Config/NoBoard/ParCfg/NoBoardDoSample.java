package com.yzlm.cyl.cfragment.Config.NoBoard.ParCfg;

import android.content.Context;

import com.yzlm.cyl.cfragment.Config.MyDBHelper.CompoentNoBoadrdHelper.CompNoBoardConfigDBHelper;
import com.yzlm.cyl.cfragment.Config.NoBoard.CfgTool.NoBoardConfig;

/**
 * Created by caoyiliang on 2016/11/15.
 */

public class NoBoardDoSample extends NoBoardConfig {
    private void InitDoSample() {

        configMap.put("C0", "0");
        configMap.put("C1", "0");
        configMap.put("C2", "0");
        configMap.put("C3", "0");
        configMap.put("C4", "0");
        configMap.put("C5", "0");

        configMap.put("ycjzFlag", "false");
        configMap.put("calibrationFlag","false");           //在校准中是否开启校准失败功能

        configMap.put("EnOrAIsShow", "1");     //  0:都不显示  1:显示光强能量  2:显示吸光度
    }


    public NoBoardDoSample(Context context, String point) {
        configDBHelper = CompNoBoardConfigDBHelper.getInstance(context);
        this.point = point;
        InitDoSample();
        syncDB();
    }
}
