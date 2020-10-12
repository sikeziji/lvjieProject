package com.yzlm.cyl.cfragment.Communication.NoBoard.AppCalc;

import com.yzlm.cyl.cfragment.Communication.Component.AppCalc.Calc;
import com.yzlm.cyl.cfragment.Config.NoBoard.CfgTool.NoBoardKbf;

import java.util.List;
import java.util.Map;

import static com.yzlm.cyl.cfragment.Global.context;

/**
 * 项目名称    water
 * 类描述
 * 创建人      hp
 * 创建时间    2019/7/25
 */
public class NBCalc extends Calc {
    private NoBoardKbf mKbf;
    private List<Map> list;


    public NBCalc() {
        mKbf = new NoBoardKbf(context);
    }
    public double getF(String range,String comp){
        list = mKbf.select(comp, range, null, null, 0,1);
        double f = 1;
        if (list!=null){
            try {
                return Double.parseDouble(list.get(0).get("F").toString());
            } catch (Exception e) {
                return f;
            }
        }else {
            return f;
        }
    }
    public double getK(String range,String comp) {

        list = mKbf.select(comp, range, null, null, 0,1);
        double k = 1;
        if (list!=null){
            try {
                return Double.parseDouble(list.get(0).get("K").toString());
            } catch (Exception e) {
                return k;
            }
        }else {
            return k;
        }
    }

    public double getB(String range,String comp) {
        list = mKbf.select(comp, range, null, null, 0,1);
        double b = 0;
        if (list!=null){
            try {
                return Double.parseDouble(list.get(0).get("B").toString());
            } catch (Exception e) {
                return b;
            }
        }else {
            return b;
        }
    }

}
