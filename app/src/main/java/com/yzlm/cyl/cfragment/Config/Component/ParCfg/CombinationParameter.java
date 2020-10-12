package com.yzlm.cyl.cfragment.Config.Component.ParCfg;


import android.content.Context;

import com.yzlm.cyl.cfragment.Config.MyDBHelper.CompoentHelper.ConfigDBHelper;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config;

/**
 * Created by zwj on 2017/9/20.
 */

public class CombinationParameter extends Config {
    private void InitCombinationParameter() {

        configMap.put("zhcsAllFlowSum", "0");// 组合测试多少个流程
        configMap.put("zhcsFlow", "");      //  组合测试内容项 的具体有哪些流程 名称及次数 示例   做样,3;校准,1;
        configMap.put("zhcsSwitch", "false");    //  关闭做样设置中的自动测量
        configMap.put("zhcsStartFlag", "false");    // 启动标志位
    }


    public CombinationParameter(Context context, String point) {
        configDBHelper = ConfigDBHelper.getInstance(context);
        this.point = point;
        InitCombinationParameter();
        syncDB();
    }
}
