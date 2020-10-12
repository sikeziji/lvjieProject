package com.yzlm.cyl.cfragment.Cal.Component;


import com.yzlm.cyl.cfragment.Config.Component.CfgTool.History;

import java.util.List;
import java.util.Map;

import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.getCalcEn;
import static com.yzlm.cyl.cfragment.Global.saveRunInfo2File;

/*
 * Created by zwj on 2018/7/6.
 */

public class CalcAiRevise {

    /*
     判断是否需要修正
    * **/
    public static boolean isAbsorbRevise(String Component, String flName, int range, double nowAi, double rangeK) {
        try {
            CalcEnRevise calcEnRevise = getCalcEn(Component);
            if (calcEnRevise != null) {
                if (calcEnRevise.getParX() == 0 && calcEnRevise.getRangeY(range) == 0) {
                    saveRunInfo2File("组分[" + Component + "] X Y 为默认值" + calcEnRevise.getParX() + calcEnRevise.getRangeY(range));
                    return false;
                }
                History history = new History(context);
                /*历史数据是否有测量值***/
                List<Map> hisMap = history.select(Component, flName, null, null, 0, 1);
                if (hisMap.size() > 0) {
                    return isAbsorbScopeSuitable(Double.parseDouble(hisMap.get(0).get("A").toString()), nowAi, calcEnRevise.getRangeY(range), rangeK);
                } else {
                    saveRunInfo2File("组分[" + Component + "] 没有数据,吸光度2算法不执行");
                    return false;
                }
            }
        } catch (Exception e) {
        }
        return false;
    }


    /*
     * 判断当前吸光度和上一个吸光度差 范围适合修正
     * **/
    public static boolean isAbsorbScopeSuitable(double priorA, double nowA, double rangeY, double rangeK) {

        //saveRunInfo2File("吸光度范围判断符合" + "nowA:" + nowA + "priorA:" + priorA + "rangeY:" + rangeY + "rangeK:" + rangeK + "判断值1:" + (nowA - priorA) + "判断值2:" + (rangeY / rangeK));
        //saveRunInfo2File("吸光度范围判断不符合" + "nowA:" + nowA + "priorA:" + priorA + "rangeY:" + rangeY + "rangeK:" + rangeK + "判断值1:" + (nowA - priorA) + "判断值2:" + (rangeY / rangeK));
        return Math.abs(nowA - priorA) <= ( rangeY/ rangeK);
    }

    /*
     获取修正后的吸光度
      前一个做样数的吸光度
      当前数据的吸光度
      参数xa
    * **/
    public static double getNewAi(double priorA, double nowA, double xa) {

        //saveRunInfo2File("新吸光度计算" + "nowA:" + nowA + "priorA:" + priorA + "xa:" + xa);
        return (nowA - priorA) * xa + priorA;
    }


}
