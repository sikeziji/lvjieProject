package com.yzlm.cyl.cfragment.Communication.NoBoard.AppCalc;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ZWJ on 2019/7/25
 */

public class NBCalcTable {
    //<量程,clac>
    private Map<String, NBCalc> CalcMap = new HashMap<>();

    public void Add(String Range, NBCalc calc) {
        CalcMap.put(Range, calc);
    }

    public NBCalc getCalc(String Range) {
        return CalcMap.get(Range);
    }
}
