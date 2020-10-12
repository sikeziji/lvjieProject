package com.yzlm.cyl.cfragment.Communication.Component.AppCalc;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by caoyiliang on 2017/3/14.
 */

public class CalcTable {
    //<量程,clac>
    private Map<String, Calc> CalcMap = new HashMap<>();

    public void Add(String Range, Calc calc) {
        CalcMap.put(Range, calc);
    }

    public Calc getCalc(String Range) {
        return CalcMap.get(Range);
    }
}
