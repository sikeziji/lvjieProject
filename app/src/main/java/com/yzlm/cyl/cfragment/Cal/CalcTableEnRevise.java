package com.yzlm.cyl.cfragment.Cal;

import com.yzlm.cyl.cfragment.Cal.Component.CalcEnRevise;

import java.util.HashMap;
import java.util.Map;

/*
 * Created by zwj on 2018/7/5.
 */

class CalcTableEnRevise {

    //<量程,calcEn>
    public Map<String, CalcEnRevise> CalcEnMap = new HashMap<>();

    public void Add(String compName, CalcEnRevise calcEn) {
        CalcEnMap.put(compName, calcEn);
    }

    public CalcEnRevise getCalcEn(String compName) {
        return CalcEnMap.get(compName);
    }
}
