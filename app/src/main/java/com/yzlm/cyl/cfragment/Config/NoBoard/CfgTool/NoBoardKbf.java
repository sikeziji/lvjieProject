package com.yzlm.cyl.cfragment.Config.NoBoard.CfgTool;

import android.content.Context;

import com.yzlm.cyl.cfragment.Communication.NoBoard.AppCalc.NBCalc;
import com.yzlm.cyl.cfragment.Communication.NoBoard.AppCalc.NBCalcTable;
import com.yzlm.cyl.cfragment.Communication.NoBoard.AppCalc.NoBoardKbfVal;
import com.yzlm.cyl.cfragment.Config.MyDBHelper.CompoentNoBoadrdHelper.CompNoBoardDataDBHelper;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.getNoBoardCalc;
import static com.yzlm.cyl.clibrary.Util.DataUtil.copy;

/**
 * K、B、F值对象的数据库操作类
 * Created by WL on 2017/4/10.
 */

public class NoBoardKbf {
    private CompNoBoardDataDBHelper kbfDBHelper;

    public NoBoardKbf(Context context) {
        kbfDBHelper = CompNoBoardDataDBHelper.getInstance(context);
    }

    /**
     * 向数据库中增加一条KBF值记录
     */
    public void Add(NoBoardKbfVal kbfVal) {
        Map<String, Object> map = new HashMap<>();
        map.put("time", kbfVal.getTime());
        map.put("component", kbfVal.getComponent());
        map.put("range", kbfVal.getRange());
        map.put("K", kbfVal.getK());
        map.put("B", kbfVal.getB());
        map.put("F", kbfVal.getF());
        kbfDBHelper.insert("KBF", map);
    }

    public List<Map> select(String Component, String Range, String startTime, String endTime, long index, long len) {
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

    public void clearKBFData() {
        kbfDBHelper.execSQL("delete from KBF");
        kbfDBHelper.execSQL("delete from sqlite_sequence WHERE name =" + "\'KBF\'");
    }

    public void clearSelectKBFData(String Component) {
        kbfDBHelper.execSQL("delete from KBF WHERE component =" + "\'" + Component + "\'");
    }


    /* 无背板 组分 保存KBF值至数据库*/
    public static void saveNoBoardKBF(String component, short year, byte month, byte day, byte hour, byte minute, byte second, String range, double valK, double valB, double valF) {
        NoBoardKbfVal kbfVal = new NoBoardKbfVal();
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
        NoBoardKbf kbf = new NoBoardKbf(context);
        kbf.Add(kbfVal);
    }

    /**
     * 无背板 组分  增加量程KB
     *
     * @param compName 组分名称
     * @param strRange 量程 “1 ，2,3”
     * @param k
     * @param b
     * @param f
     */
    public static void addNoBoardRangeKB(String compName, String strRange, double k, double b, double f) {
        Calendar cal = Calendar.getInstance();
        NoBoardKbfVal kbfVal = new NoBoardKbfVal();
        NoBoardKbf mKbf;
        mKbf = new NoBoardKbf(context);
        kbfVal = new NoBoardKbfVal();
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

    public static float getNoBoardNewestKBF(String compName, String range, String type) {
        NoBoardKbf mKbf;
        float fData = 0;
        NBCalcTable ct = getNoBoardCalc(compName);
        NBCalc calc = ct.getCalc(range);
        mKbf = new NoBoardKbf(context);
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
