package com.yzlm.cyl.cfragment.Config.Component.ParCfg;

import android.content.Context;

import com.yzlm.cyl.cfragment.Config.MyDBHelper.CompoentHelper.ConfigDBHelper;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config;

/**
 * Created by zwj on 2017/10/31.
 */

public class FactorySetPar extends Config {

    private void InitFactoryPar() {
        configMap.put("FAC_HMIBH", 0);
        configMap.put("FAC_YQBH", 0);
        configMap.put("FAC_YBLCL", 0);
        configMap.put("FAC_YBLCH", 0);
        configMap.put("FAC_LC1L", 0);
        configMap.put("FAC_LC1H", 0);
        configMap.put("FAC_LC2L", 0);
        configMap.put("FAC_LC2H", 0);
        configMap.put("FAC_LC3L", 0);
        configMap.put("FAC_LC3H", 0);
        configMap.put("FAC_LC1K", 1);
        configMap.put("FAC_LC1B", 0);
        configMap.put("FAC_LC1F", 1);
        configMap.put("FAC_LC2K", 1);
        configMap.put("FAC_LC2B", 0);
        configMap.put("FAC_LC2F", 1);
        configMap.put("FAC_LC3K", 1);
        configMap.put("FAC_LC3B", 0);
        configMap.put("FAC_LC3F", 1);
    }


    public FactorySetPar(Context context, String point) {
        configDBHelper = ConfigDBHelper.getInstance(context);
        this.point = point;
        InitFactoryPar();
        syncDB();
    }
}
