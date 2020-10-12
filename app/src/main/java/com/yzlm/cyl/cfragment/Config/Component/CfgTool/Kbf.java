package com.yzlm.cyl.cfragment.Config.Component.CfgTool;

import android.content.Context;

import com.yzlm.cyl.cfragment.Communication.Component.AppCalc.Calc;
import com.yzlm.cyl.cfragment.Communication.Component.AppCalc.CalcTable;
import com.yzlm.cyl.cfragment.Communication.Component.AppCalc.KbfVal;
import com.yzlm.cyl.cfragment.Config.MyDBHelper.CompoentHelper.DataDBHelper;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.getCalc;
import static com.yzlm.cyl.clibrary.Util.DataUtil.copy;

/**
 * K、B、F值对象的数据库操作类
 * Created by WL on 2017/4/10.
 */

public class Kbf {
    private DataDBHelper kbfDBHelper;

    public Kbf(Context context) {
        kbfDBHelper = DataDBHelper.getInstance(context);
    }

    /**
     *向数据库中增加一条KBF值记录
     */
    public void Add(KbfVal kbfVal) {
        Map<String, Object> map = new HashMap<>();
        map.put("time", kbfVal.getTime());
        map.put("component", kbfVal.getComponent());
        map.put("range", kbfVal.getRange());
        map.put("K", kbfVal.getK());
        map.put("B", kbfVal.getB());
        map.put("F", kbfVal.getF());
        kbfDBHelper.insert("KBF", map);
    }

    public List<Map> select(String Component, String Range, String startTime, String endTime, long index,long len) {
        String sql = "select * from KBF where component=?";
        String[] params = new String[]{Component};
        if (!(startTime == null || endTime == null)) {
            sql += " and (datetime(time) between datetime(?) and datetime(?))";
            params = copy(params, new String[]{startTime, endTime}).toArray(new String[params.length + 2]);
        }
        if (Range != null) {
            sql += " and range=?";
            params = copy(params, new String[]{Range}).toArray(new String[params.length + 1]);
        }
        {
            sql += " order by id desc limit ?,?";
            params = copy(params, new String[]{Long.toString(index)}, new String[]{Long.toString(len)}).toArray(new String[params.length + 1]);
        }
        return kbfDBHelper.queryListMap(sql, params);
    }

    public List<Map> selectAllData(String Component) {
        String sql = "select * from KBF where component=?";
        String[] params = new String[]{Component};
        return kbfDBHelper.queryListMap(sql, params);
    }

    public void clearKBFData(){
        kbfDBHelper.execSQL("delete from KBF");
        kbfDBHelper.execSQL("delete from sqlite_sequence WHERE name ="+"\'KBF\'");
    }
    public void clearSelectKBFData(String Component){
        kbfDBHelper.execSQL("delete from KBF WHERE component =" + "\'" + Component + "\'");
    }


    /*保存KBF值至数据库*/
    public static void saveKBF(String component,short year,byte month,byte day,byte hour,byte minute,byte second,String range, double valK, double valB, double valF) {
        KbfVal kbfVal = new KbfVal();
        kbfVal.setYear(year);
        kbfVal.setMonth(month);
        kbfVal.setDay(day);
        kbfVal.setHour(hour);
        kbfVal.setMinute(minute);
        kbfVal.setSecond(second);
        kbfVal.setComponent(component);
        kbfVal.setRange(range);
        kbfVal.setK(valK);
        kbfVal.setB(valB);
        kbfVal.setF(valF);
        Kbf kbf = new Kbf(context);
        kbf.Add(kbfVal);

    }

    public static void addRangeKB(String compName,String strRange, double k, double b, double f) {
        Calendar cal = Calendar.getInstance();
        KbfVal kbfVal = new KbfVal();
        Kbf mKbf;
        mKbf = new Kbf(context);
        kbfVal = new KbfVal();
        kbfVal.setYear((short) cal.get(Calendar.YEAR));
        kbfVal.setMonth((byte) (cal.get(Calendar.MONTH) + 1));
        kbfVal.setDay((byte) cal.get(Calendar.DATE));
        kbfVal.setHour((byte) cal.get(Calendar.HOUR_OF_DAY));
        kbfVal.setMinute((byte) cal.get(Calendar.MINUTE));
        kbfVal.setSecond((byte) cal.get(Calendar.SECOND));
        kbfVal.setComponent(compName);
        kbfVal.setRange(String.valueOf(strRange));
        kbfVal.setK(k);
        kbfVal.setB(b);
        kbfVal.setF(f);
        mKbf.Add(kbfVal);
    }

    /*
     * compName:组份
     * range "1"
     * type "K" "B" "F"
     **/

    public static float getNewestKBF(String compName, String range, String type) {
        Kbf mKbf;
        float fData = 0;
        CalcTable ct = getCalc(compName);
        Calc calc = ct.getCalc(range);
        mKbf = new Kbf(context);
        List<Map> list = mKbf.select(compName, range, null, null, 0, 1);

        if (list.size() > 0) {
            fData = (float) Double.parseDouble(list.get(0).get(type).toString());
        } else {
            switch (type) {
                case "K":
                    fData = (float) calc.getK(range, compName);
                    break;
                case "B":
                    fData = (float) calc.getB(range, compName);
                    break;
                case "F":
                    fData = (float) calc.getF(range, compName);
                    break;
            }
        }
        return fData;
    }
}
