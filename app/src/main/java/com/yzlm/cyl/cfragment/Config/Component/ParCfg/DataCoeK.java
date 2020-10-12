package com.yzlm.cyl.cfragment.Config.Component.ParCfg;

import android.content.Context;

import com.yzlm.cyl.cfragment.Config.MyDBHelper.CompoentHelper.ConfigDBHelper;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config;

/*
 * Created by caoyiliang on 2016/12/30.
 */

public class DataCoeK extends Config {

    private void InitDataCorrectParam() {

        /*量程  浊度系数*/// TN 使用两个系数， LED使用三个系数
        configMap.put("KZ1Range1", "0");
        configMap.put("KZ2Range1", "0");
        configMap.put("KZ3Range1", "1");

        configMap.put("KZ1Range2", "0");
        configMap.put("KZ2Range2", "0");
        configMap.put("KZ3Range2", "1");

        configMap.put("KZ1Range3", "0");
        configMap.put("KZ2Range3", "0");
        configMap.put("KZ3Range3", "1");

        /*量程KBF**/
        configMap.put("Range1K", null);
        configMap.put("Range2K", null);
        configMap.put("Range3K", null);

        configMap.put("Range1B", null);
        configMap.put("Range2B", null);
        configMap.put("Range3B", null);

        configMap.put("Range1F", null);
        configMap.put("Range2F", null);
        configMap.put("Range3F", null);

        /*外部量程KB**/
        configMap.put("ExtRange1K", "1");
        configMap.put("ExtRange2K", "1");
        configMap.put("ExtRange3K", "1");

        configMap.put("ExtRange1B", "0");
        configMap.put("ExtRange2B", "0");
        configMap.put("ExtRange3B", "0");

        configMap.put("ExtC1", "1");
        configMap.put("ExtC2", "0");
        configMap.put("ExtC3", "1");
        configMap.put("ExtC4", "0");
        configMap.put("ExtC5", "1");
        configMap.put("ExtC6", "0");
        configMap.put("ExtC7", "1");
        configMap.put("ExtC8", "0");

        //量程C
        configMap.put("Range1C", "0");
        configMap.put("Range2C", "0");
        configMap.put("Range3C", "0");

        configMap.put("ExtRangeKDown", "");
        configMap.put("ExtRangeKUp", "");
        configMap.put("ExtRangeBDown", "");
        configMap.put("ExtRangeBUp", "");


        // 用户外部KB  密码2
        configMap.put("UserExtRange1K", "1");
        configMap.put("UserExtRange2K", "1");
        configMap.put("UserExtRange3K", "1");

        configMap.put("UserExtRange1B", "0");
        configMap.put("UserExtRange2B", "0");
        configMap.put("UserExtRange3B", "0");

        //TOC 转换系数
        configMap.put("ConversionFactorK", "1");
        configMap.put("ConversionFactorB", "0");

        configMap.put("NumberOfMea", "1");// 测量次数

    }

    public DataCoeK(Context context, String point) {
        configDBHelper = ConfigDBHelper.getInstance(context);
        this.point = point;
        InitDataCorrectParam();
        syncDB();
    }
}
