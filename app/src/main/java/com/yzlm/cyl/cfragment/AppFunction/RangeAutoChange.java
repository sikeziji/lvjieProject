package com.yzlm.cyl.cfragment.AppFunction;

import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Content.Component.List2.List2_Content3.setUseRange;
import static com.yzlm.cyl.cfragment.Content.Component.ContentTool.GetPlatRangSum;

/**
 * Created by zwj on 2017/6/13.
 */

public class RangeAutoChange {

    public RangeAutoChange(String compName, double data) {

        float rang1High = Float.parseFloat(getConfigData(compName, "LC1H").equals("") ? "0" : getConfigData(compName, "LC1H"));
        float rang2High = Float.parseFloat(getConfigData(compName, "LC2H").equals("") ? "0" : getConfigData(compName, "LC2H"));


        String range;
        if (GetPlatRangSum(compName) == 3) {
            range = ((rang2High + rang2High * 0.1) < data) ? "3" : (((rang1High + rang1High * 0.1) >= data) ? "1" : "2");
        } else {
            range = (((rang1High + rang1High * 0.1) >= data) ? "1" : "2");
        }
        setUseRange(compName, range);
    }
}
