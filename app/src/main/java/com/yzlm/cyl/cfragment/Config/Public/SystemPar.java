package com.yzlm.cyl.cfragment.Config.Public;

import android.content.Context;

import com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig;
import com.yzlm.cyl.cfragment.Config.MyDBHelper.CompoentHelper.ConfigDBHelper;

public class SystemPar extends PublicConfig {

    private void init() {

        configMap.put("EnIsShow", "true");  /*历史记录光强能量显示   2020年7月28日09:30:55弃用*/

        configMap.put("ClearNotesBtnIsShow", "true");  /*清除报警、清除历史记录；仪器信息界面是否显示*/

        configMap.put("AIsShow", "false");  /*吸光度主界面显示，出数更改为结束后*/

        /*清除一切用户设置功能，（主要跟测量值有关系）*/
        configMap.put("userSettingFlag","true");

        //用户密码  (外部K2B2界面)
        configMap.put("userPassword2", "");

        //用户密码
        configMap.put("userPassword1", "");
    }

    public SystemPar(Context context) {
        configDBHelper = ConfigDBHelper.getInstance(context);
        init();
        syncDB();
    }
}
