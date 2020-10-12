package com.yzlm.cyl.cfragment.Config.NoBoard.CfgTool;

import android.content.Context;
import android.support.annotation.NonNull;

import com.yzlm.cyl.cfragment.AppFunction.AlarmLimit;
import com.yzlm.cyl.cfragment.Communication.Component.AppCalc.Calc;
import com.yzlm.cyl.cfragment.Communication.Component.AppFlow.FlowClass;
import com.yzlm.cyl.cfragment.Communication.Component.AppFlow.FlowTable;
import com.yzlm.cyl.cfragment.Communication.NoBoard.AppCalc.NBCalcTable;
import com.yzlm.cyl.cfragment.Communication.NoBoard.AppCalc.NBEnergy;
import com.yzlm.cyl.cfragment.Communication.Thread.SendManager;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.Config.MyDBHelper.CompoentNoBoadrdHelper.CompNoBoardDataDBHelper;
import com.yzlm.cyl.cfragment.Global;
import com.yzlm.cyl.cfragment.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yzlm.cyl.cfragment.Communication.CommProtocol.Modbus.RTU2018.ModbusRtu2018.getTimeHaveSec;
import static com.yzlm.cyl.cfragment.Communication.Component.Cmd.CommandParsing.AddNoBoardError;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.History.getLimitDoorValue;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.History.getLimitHigh;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.History.getLimitLow;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.History.randomData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.getPublicConfigData;
import static com.yzlm.cyl.cfragment.Config.NoBoard.CfgTool.NoBoardConfig.getNoBoardConfigData;
import static com.yzlm.cyl.cfragment.Config.NoBoard.CfgTool.NoBoardConfig.updateNoBoardConfigData;
import static com.yzlm.cyl.cfragment.Content.Component.ContentTool.ConvertUnit;
import static com.yzlm.cyl.cfragment.Global.S1;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.getNoBoardCalc;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;
import static com.yzlm.cyl.cfragment.Global.saveRunInfo2File;
import static com.yzlm.cyl.cfragment.Global.strComponent;
import static com.yzlm.cyl.clibrary.Util.DataUtil.copy;

/**
 * 项目名称    water
 * 类描述
 * 创建人      hp
 * 创建时间    2019/7/24
 */
public class NoBoardHistory {

    private CompNoBoardDataDBHelper historyDBHelper;
    private final int pointNum = 6;


    public NoBoardHistory(Context context) {
        historyDBHelper = CompNoBoardDataDBHelper.getInstance(context);
    }

    public void Add(String fComponent, NBEnergy energy) {

        DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
        df.setMaximumFractionDigits(pointNum);
        String Temperature = "";
        String Energy = "";
        for (float item : energy.temperature) {
            Temperature += item + ";";
        }

        String comp = energy.getComponent();
        FlowTable ft = Global.getFlows(strComponent.get(1)[0]);// 第一个多光谱组分
        FlowClass fc = ft.getFlowByCmd(energy.getFlow());
        final Map<String, Object> map = new HashMap<>();

        map.put("time", energy.getTime());
        map.put("component", comp);
        String fcName = fc.getName();
        map.put("flow", fcName);
        map.put("temperature", Temperature);

        double cVal = 0;
        cVal = energy.noBoardCalc_C(fcName);

        //计算后再将能量存储
        {
            for (int item : energy.energy) {
                Energy += item + ";";
                //光强有0  报错 ：能量异常，不出数据
                if (item == 0) {
                    int error = 537;
                    AddNoBoardError(comp, error, ErrorLog.msgType.报错_警告);
                    saveExceptInfo2File("组分[" + comp + "]" + fcName + "光强能量为0");
                    return;
                }
            }
        }
        map.put("energy", Energy);

        if ((fcName.equals(context.getResources().getString(R.string.ZY)) || fcName.equals(context.getResources().getString(R.string.LYCL)) || fcName.equals(context.getResources().getString(R.string.BYCL))
                || fcName.equals(context.getResources().getString(R.string.BY2CL)))
                || (fcName.equals(context.getResources().getString(R.string.LDHC)) || fcName.equals(context.getResources().getString(R.string.KDHC))
                || fcName.equals(context.getResources().getString(R.string.BYHC)) || fcName.equals(context.getResources().getString(R.string.PXY)))
                || fcName.equals(context.getResources().getString(R.string.JBHS))) {
            try {
                //量程KB作用
                cVal = Double.parseDouble(getNoBoardConfigData(comp, "ExtRange" + energy.getRange() + "K")) * cVal + Double.parseDouble(getNoBoardConfigData(comp, "ExtRange" + energy.getRange() + "B"));

                if (fcName.equals(context.getResources().getString(R.string.ZY))) {
                    // 清除强制校准状态
                    if (getNoBoardConfigData(energy.getComponent(), "ycjzFlag").equals("true")) {
                        updateNoBoardConfigData(energy.getComponent(), "ycjzFlag", "false");
                    }
                }
                cVal = getcValAfterDeal(cVal, comp, fc, energy, map);

            } catch (Exception e) {
                saveExceptInfo2File("组分[" + comp + "] 解析历史数据能量" + "测量类计算异常");
            }
        } else if (fcName.equals(context.getResources().getString(R.string.span_1))) {
            try {
                String C1 = null;
                C1 = getNoBoardConfigData(comp, "C" + (Integer.parseInt(energy.getRange()) * 2 - 2));
                cVal = Double.parseDouble(C1);
            } catch (Exception e) {
                saveExceptInfo2File("组分[" + comp + "]" + fcName + "计算异常");
            }
        } else if (fcName.equals(context.getResources().getString(R.string.span_2))) {
            try {
                String C2 = null;
                C2 = getNoBoardConfigData(comp, "C" + (Integer.parseInt(energy.getRange()) * 2 - 1));
                cVal = Double.parseDouble(C2);
            } catch (Exception e) {
                saveExceptInfo2File("组分[" + comp + "]" + fcName + "计算异常");
            }
        }
        /*出数存储数据库**/
        saveHistoryData(fComponent, energy, fc, map, fcName, cVal);
    }

    /*超仪器量程上限值**/
    private static Boolean overDevUpRange(String compName, double cVal) {
        if (cVal > Float.parseFloat(getNoBoardConfigData(compName, "YBLCH")) * Double.parseDouble(getNoBoardConfigData(compName, "devRangeUpMulti"))) {
            AddNoBoardError(compName, 531, ErrorLog.msgType.运行_信息);
            saveRunInfo2File("组分[" + compName + "]超仪表量程上限: 实测值 = " + cVal);
            return true;
        }
        return false;
    }

    /*超当前量程上限值**/
    private static Boolean overNowUpRange(String compName, int range, double cVal) {
        return (cVal > Double.parseDouble(getNoBoardConfigData(compName, "LC" + range + "H")) * Double.parseDouble(getNoBoardConfigData(compName, "nowRangeUpMulti")));
    }

    /*超仪表上下限报警
     *
     * **/
    private static double oveRangeSetting(String fcName, String compName, int range, double cVal) {
        try {
            // 超当前量程上下限告警
            if (getNoBoardConfigData(compName, "alarmOutNowRangeSwitch").equals("open")) {
                if ((cVal > Double.parseDouble(getNoBoardConfigData(compName, "LC" + range + "H")) * Double.parseDouble(getNoBoardConfigData(compName, "nowRangeUpMulti")))
                        || (cVal < Double.parseDouble(getNoBoardConfigData(compName, "LC" + range + "L")))) {
                    saveRunInfo2File("组分[" + compName + "]超当前量程" + range + "测量范围:实测值 = " + cVal);
                    AddNoBoardError(compName, (570 + range - 1), ErrorLog.msgType.运行_信息);

                    if (overNowUpRange(compName, range, cVal)) {
                        // 超当前量程上限限制出数
                        if (getNoBoardConfigData(compName, "alarmOutNowRangeOutLimit").equals("open")) {
                            cVal = Double.parseDouble(getNoBoardConfigData(compName, "LC" + range + "H")) * Double.parseDouble(getNoBoardConfigData(compName, "nowRangeUpMulti"));
                        }
                    }
                }
            }
            // 超仪器量程上下限告警
            if (getNoBoardConfigData(compName, "alarmOutDevRangeSwitch").equals("open")) {
                if (cVal < Float.parseFloat(getNoBoardConfigData(compName, "YBLCL"))) {
                    saveRunInfo2File("组分[" + compName + "]超仪表量程下限: 实测值 = " + cVal);
                    AddNoBoardError(compName, 532, ErrorLog.msgType.运行_信息);
                } else if (overDevUpRange(compName, cVal)) {
                    if (getNoBoardConfigData(compName, "alarmOutDevRangeOutLimit").equals("open")) {
                        cVal = Double.parseDouble(getNoBoardConfigData(compName, "YBLCH")) * Double.parseDouble(getNoBoardConfigData(compName, "devRangeUpMulti"));
                    }
                }
            }
            if (getPublicConfigData("SYS_RELAYCONF").equals("2")) {
                if (cVal < Float.parseFloat(getNoBoardConfigData(compName, "ALARM_L"))) {
                    AddNoBoardError(compName, 582, ErrorLog.msgType.运行_信息);
                } else if (cVal > Float.parseFloat(getNoBoardConfigData(compName, "ALARM_H"))) {
                    AddNoBoardError(compName, 583, ErrorLog.msgType.运行_信息);
                }
            }
        } catch (Exception e) {
            saveRunInfo2File("组分[" + compName + "] 超量程报警异常");
        }
        return cVal;
    }

    /*
     *
     * 记录数据标识
     * ***/
    @NonNull
    private static String getMeaTagStatus(double cVal, String fComponent, String comp) {
        String strMeaTag = "";
        try {
            if (getConfigData(fComponent, "doJobRunningMode").equals("3")) {
                // 维护
                strMeaTag = strMeaTag.equals("") ? "M" : strMeaTag + "-M";
            }
            if ((cVal > Float.parseFloat(getNoBoardConfigData(comp, "YBLCH")))) {
                //超仪表上限
                strMeaTag = strMeaTag.equals("") ? "T" : strMeaTag + "-T";
            }
            if ((cVal < Float.parseFloat(getNoBoardConfigData(comp, "YBLCL")))) {
                // 超仪表下限
                strMeaTag = strMeaTag.equals("") ? "L" : strMeaTag + "-L";
            }
            if (strMeaTag.equals("")) {
                strMeaTag = "N";
            }
        } catch (Exception e) {
            saveExceptInfo2File("组分[" + comp + "] 测量完成数据标识记录异常");
        }
        return strMeaTag;
    }

    /*
     * 最后一条数据的数据时间
     */
    private static String getLastDataTime(String cmpName) {
        String time = null;

        NoBoardHistory history = new NoBoardHistory(context);
        List<Map> hisMap = history.selectTop(cmpName);
        if (hisMap.size() > 0) {
            time = hisMap.get(0).get("time").toString();
        }
        return time;
    }

    /*将历史数据进行存储*/
    private void saveHistoryData(String fComponent, NBEnergy energy, FlowClass fc, Map<String, Object> map, String fcName, double cVal) {
        boolean timeFlag = false;
        String comp = energy.getComponent();

        /*超量程告警*/
        cVal = oveRangeSetting(fcName, comp, fc.getRange(), cVal);

        energy.setValue(cVal);
        map.put("C", String.format("%." + pointNum + "f ", cVal));
        map.put("unit", energy.getUnit());
        if (fcName.equals(context.getResources().getString(R.string.ZY))) {
            NBCalcTable ct = getNoBoardCalc(comp);
            Calc calc = ct.getCalc(String.valueOf(fc.getRange()));
            map.put("A", calc.getA());
        } else {
            map.put("A", energy.noBoardFormulaCalc_A());
        }
        map.put("tag", "");
        String strMeaTag = getMeaTagStatus(cVal, fComponent, comp);
        map.put("tag", strMeaTag);
        String lastDataTime = getLastDataTime(comp);
        if (lastDataTime != null) {
            timeFlag = lastDataTime.equals(energy.getTime());
        }
        if (!timeFlag) {
            // 数据时间是否小于系统当前时间
            String strNowTime = getTimeHaveSec();
            if (energy.getTime().compareTo(strNowTime) < 0) {
                historyDBHelper.insert("History", map);// 注意线程，需要锁
            } else {
                saveRunInfo2File("组分[" + comp + "] 数据时间:" + energy.getTime() + "系统时间:" + strNowTime);
            }
        }
    }

    /*
     * 下限值修正
     * */
    private static double CValDownLimit(String compName, double cVal) {

        try {
            if (getNoBoardConfigData(compName, "limitSwitch").equals("close")) {
                return cVal;
            } else {
                String strLimitValue = getNoBoardConfigData(compName, "limitValue");
                String strLimitLow = getNoBoardConfigData(compName, "limitLow");
                String strLimitHigh = getNoBoardConfigData(compName, "limitHigh");

                if (strLimitValue == null || strLimitValue.equals("") || strLimitLow == null || strLimitLow.equals("") || strLimitHigh == null || strLimitHigh.equals("")) {
                    cVal = randomData(cVal, getLimitDoorValue(compName), getLimitHigh(compName), getLimitLow(compName));
                    if (cVal == 0) {
                        cVal = randomData(cVal, 0.003, 0.003, 0.001);
                    }
                } else {
                    cVal = randomData(cVal, Double.parseDouble(strLimitValue), Double.parseDouble(strLimitHigh), Double.parseDouble(strLimitLow));
                }
            }
        } catch (Exception e) {
            saveExceptInfo2File("组分[" + compName + "]" + "检出限异常!");
        }

        return cVal;
    }

    /*
     * 下限值修正
     * */
    private static double quantitativeLimit(String compName, double cVal) {
        try {
            if (getNoBoardConfigData(compName, "QuantitativeLimitSwitch").equals("close")) {
                return cVal;
            } else {
                String strLimitValue = getNoBoardConfigData(compName, "QuantitativeLimitValue");
                String strLimitLow = getNoBoardConfigData(compName, "QuantitativeLimitLow");
                String strLimitHigh = getNoBoardConfigData(compName, "QuantitativeLimitHigh");
                if (strLimitValue == null || strLimitValue.equals("") || strLimitLow == null || strLimitLow.equals("") || strLimitHigh == null || strLimitHigh.equals("")) {
                    cVal = randomData(cVal, getLimitDoorValue(compName), getLimitHigh(compName), getLimitLow(compName));
                    if (cVal == 0) {
                        cVal = randomData(cVal, 0.003, 0.003, 0.001);
                    }
                } else {
                    cVal = randomData(cVal, Double.parseDouble(strLimitValue), Double.parseDouble(strLimitHigh), Double.parseDouble(strLimitLow));
                }
            }
        } catch (Exception e) {
            saveExceptInfo2File("组分[" + compName + "]" + "定量下限异常!");
        }

        return cVal;
    }

    /*
     * 数据值进行检出限判定*/
    private static double getNoBoardcValDownLimit(double cVal, String comp, String flowName) {

        /*定量下限*/
        if (flowName.equals(context.getResources().getString(R.string.ZY)) || flowName.equals(context.getResources().getString(R.string.JBHS))) {
            cVal = quantitativeLimit(comp, cVal);
        } else {
            /*下限*/
            cVal = CValDownLimit(comp, cVal);
        }

        return cVal;
    }

    /*测量完成之后出数打印*/
    private void measurementOfPrinting(double cVal, String comp, NBEnergy energy) {
        try {
            if (getNoBoardConfigData(comp, "PRINTER").equals("true")) {
                String strData;
                /*
                 * 由于打印机不支持打印 μg/L ，故默认为mg/L
                 * */
                if (getNoBoardConfigData(comp, "UNIT").equals("μg/L")) {
                    strData = "\r\n" + comp + "=" + String.format("%." + Integer.parseInt(getNoBoardConfigData(comp, "YXWS")) + "f ", cVal) + "" + "mg/L" + "\r\n" + energy.getTime() + "\r\n";
                } else {
                    String afterValue = ConvertUnit("mg/L", getNoBoardConfigData(comp, "UNIT"), cVal, Integer.parseInt(getNoBoardConfigData(comp, "YXWS")));
                    strData = "\r\n" + comp + "=" + afterValue + "" + getNoBoardConfigData(comp, "UNIT") + "\r\n" + energy.getTime() + "\r\n";
                }
                SendManager.SendCmd("IO" + "_" + "打印输出_09_10", S1, 1, 200, strData.getBytes());
            }
        } catch (Exception e) {
            saveExceptInfo2File("组分[" + comp + "] 测量完成后出数打印异常");
        }
    }

    private double getcValAfterDeal(double cVal, String comp, FlowClass fc, NBEnergy energy, Map<String, Object> map) {

        cVal = getNoBoardcValDownLimit(cVal, comp, fc.getName());
        try {
            /*报警上下限刷新*/
            try {
                double al = Double.parseDouble(getNoBoardConfigData(comp, "ALARM_L"));
                double ah = Double.parseDouble(getNoBoardConfigData(comp, "ALARM_H"));
                new AlarmLimit(comp, cVal, al, ah);
            } catch (Exception e) {
                saveExceptInfo2File("组分[" + comp + "] 报警上下限参数异常");
            }
            measurementOfPrinting(cVal, comp, energy);

        } catch (Exception e) {
            saveExceptInfo2File("组分[" + energy.getComponent() + "]" + "计算后处理异常!");
        }
        return cVal;
    }


    /*  *//*
     * Component TP
     * Flow  做样
     *
     * */
    public List<Map> select(String Component, String Flow, String startTime, String endTime, long index, long len) {
        String sql = "select * from History where component=?";
        String[] params = new String[]{Component};
        if (!(startTime == null || endTime == null)) {
            sql += " and (datetime(time) between datetime(?) and datetime(?))";
            params = copy(params, new String[]{startTime, endTime}).toArray(new String[params.length + 2]);
        }

        if (Flow != null) {
            sql += " and flow=?";
            params = copy(params, new String[]{Flow}).toArray(new String[params.length + 1]);
        }
        {
            sql += " order by id desc limit ?,?";
            params = copy(params, new String[]{Long.toString(index)}, new String[]{Long.toString(len)}).toArray(new String[params.length + 1]);
        }
        return historyDBHelper.queryListMap(sql, params);
    }

    /*
     * 可以查找多个流程的数据
     * */
    public List<Map> select(String Component, String[] Flows, String startTime, String endTime, long index, long len, Object obj) {
        String sql = "select * from History where component=?";
        String[] params = new String[]{Component};
        if (!(startTime == null || endTime == null)) {
            sql += " and (datetime(time) between datetime(?) and datetime(?))";
            params = copy(params, new String[]{startTime, endTime}).toArray(new String[params.length + 2]);
        }
        if (Flows != null) {
            sql += " and(";
            for (String item : Flows) {
                sql += " flow=? or";
                params = copy(params, new String[]{item}).toArray(new String[params.length + 1]);
            }
            sql = sql.substring(0, sql.length() - 3);
            sql += ")";
        }
        {
            sql += " order by id desc limit ?,?";
            params = copy(params, new String[]{Long.toString(index)}, new String[]{Long.toString(len)}).toArray(new String[params.length + 1]);
        }
        return historyDBHelper.queryListMap(sql, params);
    }


    /**
     * 查询指定组分的最新历史记录
     *
     * @param Component 组分名
     * @return
     */
    public List<Map> selectTop(String Component) {
        String sql = "select * from History where component=?";
        String[] params = new String[]{Component};
        sql += " order by id desc limit 0,1";

        return historyDBHelper.queryListMap(sql, params);
    }

    /**
     * 查询最新一条数据
     *
     * @return
     */
    private List<Map> selectTop() {
        String sql = "select * from History";
        String[] params = new String[]{};
        sql += " order by id desc limit 0,1";

        return historyDBHelper.queryListMap(sql, params);
    }

    /**
     * 查询 指定组分的历史记录
     *
     * @param Component 组分名
     * @return
     */
    public List<Map> selectAllData(String Component) {
        String sql = "select * from History where component=?";
        String[] params = new String[]{Component};

        return historyDBHelper.queryListMap(sql, params);
    }

    /**
     * 清除所有组分的历史记录
     */
    public void clearHistoryData() {
        historyDBHelper.execSQL("delete from History");
        historyDBHelper.execSQL("delete from sqlite_sequence WHERE name =" + "\'History\'");
    }

    /**
     * 清除指定组分历史记录
     *
     * @param Component 组分名
     */
    public void clearSelectData(String Component) {
        historyDBHelper.execSQL("delete from History WHERE component =" + "\'" + Component + "\'");
    }

    /**
     * 根据数据库id删除数据
     *
     * @param dataId 数据库id
     */
    public List<Map> getDataById(String dataId) {
        String sql = "select * from History where id=?";
        String[] params = new String[]{dataId};

        return historyDBHelper.queryListMap(sql, params);
    }

    /**
     * 根据数据库id删除数据
     *
     * @param dataId 数据库id
     */
    public void deleteDataById(String dataId) {
        historyDBHelper.execSQL("delete from History WHERE id =" + "\'" + dataId + "\'");
    }

    /**
     * 通过ID 更新数据
     *
     * @param columns 列名
     * @param values  值
     * @param dataId  id
     */
    public void updateDatById(String[] columns, Object[] values, long dataId) {
        String sql = "update History set ";

        for (int i = 0; i < columns.length; i++) {
            sql += columns[i] + "=" + "\'" + values[i] + "\'" + ",";
        }
        sql = sql.substring(0, sql.length() - 1);
        {
            sql += " where id=" + dataId;
        }
        historyDBHelper.execSQL(sql);
    }

    /*
     * 数据库中获取指定时间数据
     */
    public List<Map> selectTimeData(String time) {
        String[] params = new String[]{time};
        String sql = "select * from History where time=?";// + "\'" + time + "\'";
        return historyDBHelper.queryListMap(sql, params);
    }

}
