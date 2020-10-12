package com.yzlm.cyl.cfragment.Cal.Component;

import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;

public class CalFunc {


    /*判定是否满足单独标1的条件**/
    public static boolean isB1CalEnable(String compName, String strRange) {
        return !getConfigData(compName, ("Range" + strRange + "_CAL2_A_Value")).equals("");
    }

    /*判定是否满足单独标2的条件**/
    public static boolean isB2CalEnable(String compName, String strRange) {
        return !getConfigData(compName, ("Range" + strRange + "_CAL1_A_Value")).equals("");
    }

}
