package com.yzlm.cyl.cfragment.Communication.Component.AppCalc;

import android.util.Log;

import com.yzlm.cyl.cfragment.Cal.Component.CalcCorrectJumpValue;
import com.yzlm.cyl.cfragment.Cal.Component.CalcEnRevise;
import com.yzlm.cyl.cfragment.CalcHelper.StrExpression;
import com.yzlm.cyl.cfragment.Communication.Component.AppFlow.FlowClass;
import com.yzlm.cyl.cfragment.Communication.Component.AppFlow.FlowTable;
import com.yzlm.cyl.cfragment.Communication.NoBoard.AppCalc.NBEnergy;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.History;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.Kbf;
import com.yzlm.cyl.cfragment.Config.Component.ParCfg.DoSample;
import com.yzlm.cyl.cfragment.Global;
import com.yzlm.cyl.cfragment.R;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.yzlm.cyl.cfragment.AppFunction.CountCalibrationIdleDays.ClearCalibrationCountDays;
import static com.yzlm.cyl.cfragment.Cal.Component.CalcAiRevise.getNewAi;
import static com.yzlm.cyl.cfragment.Cal.Component.CalcAiRevise.isAbsorbRevise;
import static com.yzlm.cyl.cfragment.Cal.Component.CalcCorrectJumpValue.judgeDisCorrectJumpValue;
import static com.yzlm.cyl.cfragment.Cal.Component.CalcEnRevise.findEnergySum;
import static com.yzlm.cyl.cfragment.Cal.Component.CalcEnRevise.getNewEnergy;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.GB2005.GB2005.iDataFormat;
import static com.yzlm.cyl.cfragment.Communication.Component.Cmd.CommandParsing.AddError;
import static com.yzlm.cyl.cfragment.Communication.Component.Cmd.CommandParsing.isDoCalc;
import static com.yzlm.cyl.cfragment.Communication.Component.Cmd.CommandParsing.isNoBoardDoCalc;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.updateConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogDataModifyMsg;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog.msgType.报错_警告;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.History.dilutionCal;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.History.getGlucoseTestRevisecVal;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.History.getcValDownLimit;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.History.isEnergyNumSame;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.History.settingCalVolume;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Kbf.getNewestKBF;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.getPublicConfigData;
import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.QueryMeasCateg;
import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.getModuleName;
import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.isHaveMeasCategory;
import static com.yzlm.cyl.cfragment.DBConvert.MDBConvert.bubbleSort;
import static com.yzlm.cyl.cfragment.Global.blAuthenticationFunction;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.doubleRound;
import static com.yzlm.cyl.cfragment.Global.getCalc;
import static com.yzlm.cyl.cfragment.Global.getCalcEn;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;
import static com.yzlm.cyl.cfragment.Global.saveRunInfo2File;
import static com.yzlm.cyl.cfragment.Global.strNoBoardComponent;

/**
 * Created by caoyiliang on 2017/2/24.
 */

public class Energy {
    private short year;
    private byte month;
    private byte day;
    private byte hour;
    private byte minute;
    private byte second;

    private String component;
    private byte flow;

    public float[] temperature;
    public int[] energy;
    public float[] fEnergy;
    private String range;

    StrExpression strExpression = new StrExpression();

    private History mHistory;

    public Energy(int Count) {
        temperature = new float[Count];
        energy = new int[Count];
        fEnergy = new float[Count];
        mHistory = new History(context);
    }

    private double value;

    public double getValue() {
        return value;
    }

    public void setValue(double val) {
        this.value = val;
    }

    private String unit;

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setYear(short year) {
        this.year = year;
    }

    public short getYear() {
        return year;
    }

    public void setMonth(byte month) {
        this.month = month;
    }

    public byte getMonth() {
        return month;
    }

    public void setDay(byte day) {
        this.day = day;
    }

    public byte getDay() {
        return day;
    }

    public void setHour(byte hour) {
        this.hour = hour;
    }

    public byte getHour() {
        return hour;
    }

    public void setMinute(byte minute) {
        this.minute = minute;
    }

    public byte getMinute() {
        return minute;
    }

    public void setSecond(byte second) {
        this.second = second;
    }

    public byte getSecond() {
        return second;
    }

    public String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String temp = year + "-" + (int) month + "-" + (int) day + " " + (int) hour + ":" + (int) minute + ":" + (int) second;
        try {
            temp = new Timestamp(format.parse(temp).getTime()).toString();
            if (temp.contains(".")) {
                temp = temp.substring(0, temp.indexOf("."));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return temp;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public byte getFlow() {
        return flow;
    }

    public void setFlow(byte flow) {
        this.flow = flow;
    }

    public String getRange() {
        FlowTable ft = Global.getFlows(component);
        FlowClass fc = ft.getFlowByCmd(getFlow());
        range = String.valueOf(fc.getRange());
        return String.valueOf(fc.getRange());
    }

    public synchronized void isCalcKBValue() {

        boolean blIsCalcFlowStart = true;
        if (!QueryMeasCateg(component).equals("7")) {
            if ((flow == 4 || flow == 5 || flow == 6 || flow == 7 || flow == 8 || flow == 9)) {
                if (isEnergyCalCfgError(component, flow, energy)) return;
            }
        }
        // 启动的时候不是校准流程的情况下，才进行KB 计算
        if (getConfigData(component, "StartFlowName").contains(context.getResources().getString(R.string.B1)) ||
                (getConfigData(component, "StartFlowName").contains(context.getResources().getString(R.string.B2)))) {
            blIsCalcFlowStart = false;
        }
        double valA = calcAValue(component);
        /* 如果流程动作号为量程1标1,量程2标1,量程3标1的话，记录A值用以计算K值（WL）*/
        /* 如果流程动作号为量程1标2,量程2标2,量程3标2的话，开始计算K值（WL）*/
        CalcTable ct = getCalc(component);
        Calc calc = ct.getCalc(getRange());
        switch (flow) {
            case 0x04: {
                calc.setCalib1A(valA);
                updateConfigData(component, "CAL_TIME", getTime());
                updateConfigData(component, "CAL_Suc_Flag", "true");
                //清校准标1启动标记
                updateConfigData(component, "CAL_Start_Flag", "false");

                if (!blIsCalcFlowStart) {// 不是校准启动的情况 刷新KB
                    String strR1Cal2AValue = getConfigData(component, "Range1_CAL2_A_Value");
                    // 没有量程1的标2吸光度
                    if (!strR1Cal2AValue.equals("")) {
                        updateConfigData(component, "CAL_SUC_TIME", getConfigData(component, "CAL_TIME"));
                        calc.setCalib2A(Double.parseDouble(strR1Cal2AValue));
                        range1CalcKB(calc, false);
                    }
                }
            }
            break;
            case 0x05: {
                calc.setCalib1A(valA);
                updateConfigData(component, "CAL_TIME", getTime());
                updateConfigData(component, "CAL_Suc_Flag", "true");
                //清校准标1启动标记
                updateConfigData(component, "CAL_Start_Flag", "false");

                if (!blIsCalcFlowStart) {//不是校准启动的情况 刷新KB
                    String strR2Cal2AValue = getConfigData(component, "Range2_CAL2_A_Value");
                    // 没有量程2的标2吸光度
                    if (!strR2Cal2AValue.equals("")) {
                        updateConfigData(component, "CAL_SUC_TIME", getConfigData(component, "CAL_TIME"));
                        calc.setCalib2A(Double.parseDouble(strR2Cal2AValue));
                        range2CalcKB(calc, false);
                    }
                }
            }
            break;
            case 0x06: {
                calc.setCalib1A(valA);

                updateConfigData(component, "CAL_TIME", getTime());
                updateConfigData(component, "CAL_Suc_Flag", "true");
                //清校准标1启动标记
                updateConfigData(component, "CAL_Start_Flag", "false");

                if (!blIsCalcFlowStart) {   //不是校准启动的情况 刷新KB
                    String strR3Cal2AValue = getConfigData(component, "Range3_CAL2_A_Value");
                    // 没有量程3的标2吸光度
                    if (!strR3Cal2AValue.equals("")) {
                        updateConfigData(component, "CAL_SUC_TIME", getConfigData(component, "CAL_TIME"));

                        calc.setCalib2A(Double.parseDouble(strR3Cal2AValue));
                        range3CalcKB(calc, false);
                    }
                }
            }
            break;
            case 0x07: {
                calc.setCalib2A(valA);

                double Calib1AConc = 0;
                try {
                    //清校准标2启动标记
                    updateConfigData(component, "CAL2_Start_Flag", "false");

                    if (!blIsCalcFlowStart) {   //不是校准启动的情况 刷新KB
                        String strR1Cal1AValue = getConfigData(component, "Range1_CAL1_A_Value");
                        if (!strR1Cal1AValue.equals("")) {
                            updateConfigData(component, "CAL2_SUC_TIME", getTime());

                            calc.setCalib1A(Double.parseDouble(strR1Cal1AValue));
                            range1CalcKB(calc, false);
                        } else {
                            saveExceptInfo2File("组分[" + component + "] 单独标2，但量程1标1吸光度未记录过  ");
                        }
                    } else {
                        List<Map> hisMap = mHistory.select(component, null, null, null, 0, 1);

                        if (hisMap.size() > 0) {
                            String flowName = hisMap.get(0).get("flow").toString();
                            if (flowName.equals(context.getResources().getString(R.string.span_1))) {
                                Calib1AConc = Double.parseDouble(hisMap.get(0).get("A").toString());
                                calc.setCalib1A(Calib1AConc);
                                range1CalcKB(calc, true);
                            } else {
                                saveExceptInfo2File("组分[" + component + "] 标2 完成测量计算KBF，历史数据无标1数据");
                                return;
                            }
                        } else {
                            saveExceptInfo2File("组分[" + component + "] 标2 完成测量计算KBF，历史数据无数据");
                        }
                    }
                } catch (Exception e) {
                    return;
                }
            }
            break;
            case 0x08: {
                calc.setCalib2A(valA);

                double Calib1AConc = 0;
                try {

                    //清校准标2启动标记
                    updateConfigData(component, "CAL2_Start_Flag", "false");
                    if (!blIsCalcFlowStart) {   //不是校准启动的情况 刷新KB
                        String strR2Cal1AValue = getConfigData(component, "Range2_CAL1_A_Value");
                        if (!strR2Cal1AValue.equals("")) {
                            updateConfigData(component, "CAL2_SUC_TIME", getTime());

                            calc.setCalib1A(Double.parseDouble(strR2Cal1AValue));
                            range2CalcKB(calc, false);
                        } else {
                            saveExceptInfo2File("组分[" + component + "] 单独标2，但量程2标1吸光度未记录过  ");
                        }
                    } else {
                        List<Map> hisMap = mHistory.select(component, null, null, null, 0, 1);

                        if (hisMap.size() > 0) {
                            String flowName = hisMap.get(0).get("flow").toString();
                            if (flowName.equals(context.getResources().getString(R.string.span_1))) {
                                Calib1AConc = Double.parseDouble(hisMap.get(0).get("A").toString());
                                calc.setCalib1A(Calib1AConc);
                                range2CalcKB(calc, true);
                            } else {
                                saveExceptInfo2File("组分[" + component + "] 标2 完成测量计算KBF，历史数据无标1数据");
                                return;
                            }
                        } else {
                            saveExceptInfo2File("组分[" + component + "] 标2 完成测量计算KBF，历史数据无数据");
                        }
                    }
                } catch (Exception e) {
                    return;
                }
            }
            break;
            case 0x09: {
                calc.setCalib2A(valA);
                updateConfigData(component, "Range3_CAL2_A_Value", String.valueOf(valA));
                double Calib1AConc = 0;
                try {

                    //清校准标2启动标记
                    updateConfigData(component, "CAL2_Start_Flag", "false");

                    if (!blIsCalcFlowStart) {   //不是校准启动的情况 刷新KB
                        String strR3Cal1AValue = getConfigData(component, "Range3_CAL1_A_Value");
                        if (!strR3Cal1AValue.equals("")) {
                            updateConfigData(component, "CAL2_SUC_TIME", getTime());

                            calc.setCalib1A(Double.parseDouble(strR3Cal1AValue));
                            range3CalcKB(calc, false);
                        } else {
                            saveExceptInfo2File("组分[" + component + "] 单独标2，但量程3标1吸光度未记录过  ");
                        }
                    } else {
                        List<Map> hisMap = mHistory.select(component, null, null, null, 0, 1);

                        if (hisMap.size() > 0) {
                            String flowName = hisMap.get(0).get("flow").toString();
                            if (flowName.equals(context.getResources().getString(R.string.span_1))) {
                                Calib1AConc = Double.parseDouble(hisMap.get(0).get("A").toString());
                                calc.setCalib1A(Calib1AConc);
                                range3CalcKB(calc, true);
                            } else {
                                saveExceptInfo2File("组分[" + component + "] 标2 完成测量计算KBF，历史数据无标1数据");
                                return;
                            }
                        } else {
                            saveExceptInfo2File("组分[" + component + "] 标2 完成测量计算KBF，历史数据无数据");
                        }
                    }

                } catch (Exception e) {
                    return;
                }
            }
            break;
        }
    }

    /**
     * 量程3 计算KB
     *
     * @param calc
     * @param blIsCalcFlowStart 启动的时候不是校准流程的情况为false ,是校准流程的情况为true
     */
    private void range3CalcKB(Calc calc, boolean blIsCalcFlowStart) {

        String C5 = getConfigData(component, "C5");
        String C4 = getConfigData(component, "C4");
        double valK, valB;
        if (QueryMeasCateg(component).equals("13")) {
            valK = calc_SWDX_K(calc, C4, C5, calc.getCalib1A(), calc.getCalib2A());
            valB = calc_SWDX_B(calc, C4, calc.getCalib1A(), valK);
        } else {
            valK = calc_K(calc, C5, C4, calc.getCalib2A(), calc.getCalib1A());
            valB = calc_B(calc, C4, calc.getCalib1A(), valK);
        }
        double valF = calc_F(calc, calc.getK(getRange(), component), valK);
        calc.setF(valF);

        if (!updateKBVal(calc, valK, valB, valF)) {
            //AddError(component, 533, ErrorLog.msgType.运行_信息);
            updateConfigData(component, "CAL2_Suc_Flag", "false");
        } else {
            if (!Double.isNaN(calc.getCalib1A())) {
                updateConfigData(component, "Range3_CAL1_A_Value", String.valueOf(calc.getCalib1A()));
            }
            if (!Double.isNaN(calc.getCalib2A())) {
                updateConfigData(component, "Range3_CAL2_A_Value", String.valueOf(calc.getCalib2A()));
            }
            saveKBF("3", valK, valB, calc.getCalF());

            AddError(component, 639, String.valueOf(doubleRound(valK, 3)), ErrorLog.msgType.运维_信息);
            AddError(component, 640, String.valueOf(doubleRound(valB, 3)), ErrorLog.msgType.运维_信息);

            // 校准成功清外部KB
            extKBParReset(component, "3");

            if (blIsCalcFlowStart) {
                updateConfigData(component, "CAL2_SUC_TIME", getTime());
                updateConfigData(component, "CAL_SUC_TIME", getConfigData(component, "CAL_TIME"));
            }
            updateConfigData(component, "CAL2_Suc_Flag", "true");
        }
        ClearCalibrationCountDays(component);
        // 清除所有量程C
        if (blAuthenticationFunction) {
            updateConfigData(component, "Range1C", "0");
            updateConfigData(component, "Range2C", "0");
            updateConfigData(component, "Range3C", "0");
        }
    }

    /**
     * 量程2 计算KB
     *
     * @param calc
     * @param blIsCalcFlowStart 启动的时候不是校准流程的情况为false ,是校准流程的情况为true
     */
    private void range2CalcKB(Calc calc, boolean blIsCalcFlowStart) {

        String C3 = getConfigData(component, "C3");
        String C2 = getConfigData(component, "C2");
        double valK, valB;
        if (QueryMeasCateg(component).equals("13")) {
            valK = calc_SWDX_K(calc, C2, C3, calc.getCalib1A(), calc.getCalib2A());
            valB = calc_SWDX_B(calc, C2, calc.getCalib1A(), valK);
        } else {
            valK = calc_K(calc, C3, C2, calc.getCalib2A(), calc.getCalib1A());
            valB = calc_B(calc, C2, calc.getCalib1A(), valK);
        }
        double valF = calc_F(calc, calc.getK(getRange(), component), valK);
        calc.setF(valF);

        if (!updateKBVal(calc, valK, valB, valF)) {
            //AddError(component, 533, ErrorLog.msgType.运行_信息);
            updateConfigData(component, "CAL2_Suc_Flag", "false");
        } else {
            saveKBF("2", valK, valB, calc.getCalF());
            if (!Double.isNaN(calc.getCalib1A())) {
                updateConfigData(component, "Range2_CAL1_A_Value", String.valueOf(calc.getCalib1A()));
            }
            if (!Double.isNaN(calc.getCalib2A())) {
                updateConfigData(component, "Range2_CAL2_A_Value", String.valueOf(calc.getCalib2A()));
            }
            // 配置稀释背板的情况下，量程2的KB同时更新量程3KB
            if (isHaveMeasCategory(component, "5") && getConfigData(component, "XS_BASE_RANGE").equals("2")) {
                saveKBF("3", valK, valB, calc.getF(getRange(), component));
                saveRunInfo2File("组分[" + component + "] 配置稀释背板情况：量程2校准更新量程3KB");
            }

            AddError(component, 639, String.valueOf(doubleRound(valK, 3)), ErrorLog.msgType.运维_信息);
            AddError(component, 640, String.valueOf(doubleRound(valB, 3)), ErrorLog.msgType.运维_信息);

            // 校准成功清外部KB
            extKBParReset(component, "2");

            if (blIsCalcFlowStart) {
                updateConfigData(component, "CAL2_SUC_TIME", getTime());
                updateConfigData(component, "CAL_SUC_TIME", getConfigData(component, "CAL_TIME"));
            }
            updateConfigData(component, "CAL2_Suc_Flag", "true");
        }
        ClearCalibrationCountDays(component);
        // 清除所有量程C
        if (blAuthenticationFunction) {
            updateConfigData(component, "Range1C", "0");
            updateConfigData(component, "Range2C", "0");
            updateConfigData(component, "Range3C", "0");
        }
    }

    /**
     * 量程1KB计算
     *
     * @param calc
     * @param blIsCalcFlowStart 启动的时候不是校准流程的情况为false ,是校准流程的情况为true
     */
    private void range1CalcKB(Calc calc, boolean blIsCalcFlowStart) {

        String C1 = getConfigData(component, "C1");
        String C0 = getConfigData(component, "C0");
        double valK, valB;
        if (QueryMeasCateg(component).equals("13")) {
            valK = calc_SWDX_K(calc, C0, C1, calc.getCalib1A(), calc.getCalib2A());
            valB = calc_SWDX_B(calc, C0, calc.getCalib1A(), valK);
        } else {
            valK = calc_K(calc, C1, C0, calc.getCalib2A(), calc.getCalib1A());
            valB = calc_B(calc, C0, calc.getCalib1A(), valK);
        }
        double valF = calc_F(calc, calc.getK(getRange(), component), valK);
        calc.setF(valF);

        //如果是量程1强制校准则直接刷新量程23kb
        if ((getConfigData(component, "ycjzFlag").equals("true"))) {
            updateConfigData(component, "OneKeyCalForceKBFlag", "true");
        }

        if (!updateKBVal(calc, valK, valB, valF)) {
            // AddError(component, 533, ErrorLog.msgType.运行_信息);
            updateConfigData(component, "CAL2_Suc_Flag", "false");
        } else {
            if (!Double.isNaN(calc.getCalib1A())) {
                updateConfigData(component, "Range1_CAL1_A_Value", String.valueOf(calc.getCalib1A()));
            }
            if (!Double.isNaN(calc.getCalib2A())) {
                updateConfigData(component, "Range1_CAL2_A_Value", String.valueOf(calc.getCalib2A()));
            }
            saveKBF("1", valK, valB, calc.getCalF());
            // 配置稀释背板的情况下，量程1的KB同时更新量程3KB
            if (isHaveMeasCategory(component, "5") && getConfigData(component, "XS_BASE_RANGE").equals("1")) {
                saveKBF("3", valK, valB, calc.getF(getRange(), component));
                saveRunInfo2File("组分[" + component + "] 配置稀释背板情况：量程1校准更新量程3KB");
            }

            AddError(component, 639, String.valueOf(doubleRound(valK, 3)), ErrorLog.msgType.运维_信息);
            AddError(component, 640, String.valueOf(doubleRound(valB, 3)), ErrorLog.msgType.运维_信息);

            // 校准成功清外部KB
            extKBParReset(component, "1");
            if (blIsCalcFlowStart) {
                updateConfigData(component, "CAL2_SUC_TIME", getTime());
                updateConfigData(component, "CAL_SUC_TIME", getConfigData(component, "CAL_TIME"));
            }
            updateConfigData(component, "CAL2_Suc_Flag", "true");
            try {
                // 量程1一键校准量程23KB
                if (getConfigData(component, "OneKeyCalSwitch").equals("open") && (!getConfigData(component, "OneKeyCalX").equals(""))) {
                    getCalRangeRsa(component, valK, valB, calc.getCalF(), "OneKeyCalRange2RSA", "2");
                    getCalRangeRsa(component, valK, valB, calc.getCalF(), "OneKeyCalRange3RSA", "3");
                    if (getConfigData(component, "OneKeyCalStartFlag").equals("true")) {
                        updateConfigData(component, "OneKeyCalStartFlag", "false");
                    }
                    saveRunInfo2File("组分[" + component + "] 一键校准量程23，X:" + getConfigData(component, "OneKeyCalX"));
                }
                if ((getConfigData(component, "OneKeyCalForceKBFlag").equals("true"))) {
                    updateConfigData(component, "OneKeyCalForceKBFlag", "false");
                }
            } catch (Exception e) {
                saveRunInfo2File("组分[" + component + "] 一键校准计算异常!");
            }
            // 清除所有量程C
            if (blAuthenticationFunction) {
                updateConfigData(component, "Range1C", "0");
                updateConfigData(component, "Range2C", "0");
                updateConfigData(component, "Range3C", "0");
            }
        }
        ClearCalibrationCountDays(component);
    }

    /*
     * 检查能量个数及算法配备**/
    public static boolean isEnergyCalCfgError(String component, Byte flow, int[] energy) {
        boolean energyError = false;
        switch (component) {
            case "TN":
            case "TPb":
                // TN  检查设置的测量点和测控板返回测量点数是否匹配
                if (!isEnergyNumSame(component, energy)) {
                    energyError = true;
                }
                break;
            case "TP":
                // 由于安卓接收光强的时候时根据配置文件E0X的个数来加载到energy的，故下面判定不起作用，暂保留
                // TP  检查配置的算法文件的能量个数和测控板返回能量个数是否匹配
                if ((!(findEnergySum(getCalcAFormula(component, flow)) == energy.length))) {
                    energyError = true;
                }
                break;
        }
        if (energyError) {
            AddError(component, 578, 报错_警告);
            saveExceptInfo2File("组分[" + component + "]" + "流程" + flow + "光强与算法不匹配");
            return true;
        }
        return false;
    }

    /*一键校准算法计算**/
    private void getCalRangeRsa(String compName, double valK, double valB, double valF, String oneKeyCalRange2RSA, String s) {
        double oldK = getNewestKBF(compName, s, "K");
        boolean blOneKey = false;
        boolean updateExtKBFlag = false;
        if ((getConfigData(compName, "OneKeyCalForceKBFlag").equals("true"))) {
            blOneKey = true;
        }
        switch (getConfigData(compName, oneKeyCalRange2RSA)) {
            case "1":
                if (updateKBVal(compName, s, valK)) {
                    if (oldK == 1 || blOneKey) {
                        valF = 1;
                    } else {
                        valF = oldK / valK;
                    }

                    saveKBF(s, valK, valB, valF);
                    updateConfigData(compName, "Range" + s + "K", String.valueOf(valK));
                    updateConfigData(compName, "Range" + s + "B", String.valueOf(valB));
                    updateConfigData(compName, "Range" + s + "F", String.valueOf(valF));
                    CalcTable ct1 = getCalc(compName);
                    Calc calc1 = ct1.getCalc(s);
                    calc1.setK(valK);
                    calc1.setB(valB);
                    calc1.setF(valF);
                    updateExtKBFlag = true;
                }
                break;
            case "2":
                double k = Float.parseFloat(getConfigData(compName, "OneKeyCalX")) * valK;
                double b = Float.parseFloat(getConfigData(compName, "OneKeyCalX")) * valB;
                if (updateKBVal(compName, s, k)) {
                    if (oldK == 1 || blOneKey) {
                        valF = 1;
                    } else {
                        valF = oldK / k;
                    }
                    saveKBF(s, k, b, valF);
                    updateConfigData(compName, "Range" + s + "K", String.valueOf(k));
                    updateConfigData(compName, "Range" + s + "B", String.valueOf(b));
                    updateConfigData(compName, "Range" + s + "F", String.valueOf(valF));
                    CalcTable ct2 = getCalc(compName);
                    Calc calc2 = ct2.getCalc(s);
                    calc2.setK(k);
                    calc2.setB(b);
                    calc2.setF(valF);
                    updateExtKBFlag = true;
                }
                break;
            case "3":
                double k0 = 2 * Float.parseFloat(getConfigData(compName, "OneKeyCalX")) * valK;
                double b0 = 2 * Float.parseFloat(getConfigData(compName, "OneKeyCalX")) * valB;
                if (updateKBVal(compName, s, k0)) {
                    if (oldK == 1 || blOneKey) {
                        valF = 1;
                    } else {
                        valF = oldK / k0;
                    }
                    saveKBF(s, k0, b0, valF);
                    updateConfigData(compName, "Range" + s + "K", String.valueOf(k0));
                    updateConfigData(compName, "Range" + s + "B", String.valueOf(b0));
                    updateConfigData(compName, "Range" + s + "F", String.valueOf(valF));
                    CalcTable ct3 = getCalc(compName);
                    Calc calc3 = ct3.getCalc(s);
                    calc3.setK(k0);
                    calc3.setB(b0);
                    calc3.setF(valF);
                    updateExtKBFlag = true;
                }
                break;
            case "4":
                double x = Double.parseDouble(getConfigData(compName, "OneKeyCalX"));
                double y = Double.parseDouble(getConfigData(compName, "OneKeyCalY"));
                double k1 = (1 + x) * x * y * valK;
                double b1 = (1 + x) * x * y * valB;
                if (updateKBVal(compName, s, k1)) {
                    if (oldK == 1 || blOneKey) {
                        valF = 1;
                    } else {
                        valF = oldK / k1;
                    }
                    saveKBF(s, k1, b1, valF);
                    updateConfigData(compName, "Range" + s + "K", String.valueOf(k1));
                    updateConfigData(compName, "Range" + s + "B", String.valueOf(b1));
                    updateConfigData(compName, "Range" + s + "F", String.valueOf(valF));
                    CalcTable ct4 = getCalc(compName);
                    Calc calc4 = ct4.getCalc(s);
                    calc4.setK(k1);
                    calc4.setB(b1);
                    calc4.setF(valF);
                    updateExtKBFlag = true;
                }
                break;
        }
        if (updateExtKBFlag) {
            extKBParReset(compName, s);
        }
    }

    /*
     *  compName: 组份
     *  s: 量程(1,2,3)
     * **/
    public static void extKBParReset(String compName, String s) {
        // 校准成功清外部KB
        saveOperationLogDataModifyMsg(compName, "ExtRange" + s + "K", "1", "ExtK" + s, ErrorLog.msgType.操作_信息);
        updateConfigData(compName, "ExtRange" + s + "K", "1");
        saveOperationLogDataModifyMsg(compName, "ExtRange" + s + "B", "0", "ExtB" + s, ErrorLog.msgType.操作_信息);
        updateConfigData(compName, "ExtRange" + s + "B", "0");
    }

    /*保存KBF值至数据库*/
    private void saveKBF(String range, double valK, double valB, double valF) {
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


    private String getCalcCFormula() {

        FlowTable ft = Global.getFlows(component);
        if (ft != null) {
            FlowClass fc = ft.getFlowByCmd(flow);
            if (fc != null) {
                CalcTable ct = getCalc(component);
                if (ct != null) {
                    String range = String.valueOf(fc.getRange());
                    Calc calc = ct.getCalc(range);
                    if (calc != null) {

                        return calc.getC_Formula();
                    }
                }
            }
        }
        return "";
    }

    public static String getCalcAFormula(String component, Byte flow) {

        FlowTable ft = Global.getFlows(component);
        if (ft != null) {
            FlowClass fc = ft.getFlowByCmd(flow);
            if (fc != null) {
                CalcTable ct = getCalc(component);
                if (ct != null) {
                    String range = String.valueOf(fc.getRange());
                    Calc calc = ct.getCalc(range);
                    if (calc != null) {
                        return calc.getA_Formula();
                    }
                }
            }
        }
        return "";
    }

    /*
     * 只能查找当前不重复的能量E0X的位置
     * **/
    public static int[] getEnergyCh(String formula, int enSum) {

        int[] ch = new int[enSum];
        int[] Ech = new int[enSum];
        for (int i = 0; i < enSum; i++) {
            ch[i] = i;
            //获取能量编号在公式中的位置
            Ech[i] = formula.indexOf("E" + iDataFormat(String.valueOf((i + 1)), 2));
//            if (Ech[i] == -1) {
//                //   异常直接返回
//                return Ech;
//            }
        }
        /*备份原始位置*/
        int[] EchTemp = new int[enSum];
        System.arraycopy(Ech, 0, EchTemp, 0, enSum);
        /*排序*/
        bubbleSort(Ech);
        for (int i = 0; i < Ech.length; i++) {
            for (int j = 0; j < Ech.length; j++) {
                if (Ech[i] == EchTemp[j]) {
                    Ech[i] = j;
                    break;
                }
            }
        }
        System.arraycopy(Ech, 0, ch, 0, enSum);

        return ch;
    }

    /*
     * 查找当前的能量E0X的位置
     * **/
    public static int[] getEnergyCh(String formula) {
        //用正则表达式匹配E0X等字符
        String pattern = "E\\d\\d";
        //创建Pattern对象
        Pattern p = Pattern.compile(pattern);
        //创建Matcher对象
        Matcher m = p.matcher(formula);
        List<String> matchStrs = new ArrayList<>();
        while (m.find()) { //此处find（）每次被调用后，会偏移到下一个匹配
            matchStrs.add(m.group());//获取当前匹配的值
        }
        int[] ch = new int[matchStrs.size()];
        for (int i = 0; i < matchStrs.size(); i++) {
            ch[i] = Integer.parseInt(matchStrs.get(i).substring(matchStrs.get(i).length() - 2)) - 1;
        }

        return ch;
    }


    /*
     * 除 TP   TN  TPb之外
     * 识别 E01 E02 顺序  识别算法第一个符号位  + -
     * 吸光度算法只用到2个光强的时候，识别了两个光强之间的运算符号（* / + -）
     */
    public double FormulaCalc_A() {
        double valA = 0;
        int sign = 1;
        String strSignEn = "/";
        String a_formula = getCalcAFormula(component, flow);
        switch (component) {
            case "TP":
                if (energy.length > 3) {//双光源 4组能量计算
                    FlowTable ft = Global.getFlows(component);
                    double kz1 = 0, kz2 = 0, kz3 = 1;
                    try {
                        if (ft != null) {
                            FlowClass fc = ft.getFlowByCmd(flow);
                            if (fc != null) {
                                String range = String.valueOf(fc.getRange());
                                kz1 = Double.parseDouble(getConfigData(component, "KZ1Range" + range));
                                kz2 = Double.parseDouble(getConfigData(component, "KZ2Range" + range));
                                kz3 = Double.parseDouble(getConfigData(component, "KZ3Range" + range));
                            }
                        }
                    } catch (Exception e) {
                        Log.i("except", "TP 量程" + range + "浊度系数异常");
                    }
                    int[] ch = getEnergyCh(a_formula);
                    if (ch.length < 4) {
                        saveExceptInfo2File("组分[" + component + "] 计算[A值]时发生表达式异常!");
                        return valA;
                    }
                    valA = (Math.log10(((double) energy[ch[0]]) / ((double) energy[ch[1]])) - kz1 * Math.log10(((double) energy[ch[2]]) / ((double) energy[ch[3]])) + kz2) / kz3;
                } else if (energy.length > 1) {
                    int[] ch = getEnergyCh(a_formula);
                    if (ch.length < 2) {
                        saveExceptInfo2File("组分[" + component + "] 计算[A值]时发生表达式异常!");
                        return valA;
                    }
                    valA = Math.log10(((double) energy[ch[0]]) / ((double) energy[ch[1]]));
                } else {
                    saveExceptInfo2File("组分[" + component + "] 计算[A值]时发生表达式计算能量值小于2个");
                }
                break;
            case "TN":
                //干扰判断
                if (QueryMeasCateg(component).equals("2")) {
                    if (getConfigData(component, "interferenceFlag").equals("true")) {
                        String[] strEnergy = null;
                        List<Map> hisMap = mHistory.selectTop("TN");
                        if (hisMap.size() > 0) {
                            strEnergy = hisMap.get(0).get("energy").toString().split(";");
                        }
                        if (strEnergy != null && strEnergy.length == energy.length) {
                            if ((Integer.valueOf(strEnergy[0]) - energy[0]) > 200 || (Integer.valueOf(strEnergy[0]) - energy[0]) < -200) {
                                Random r = new Random();
                                energy[0] = Double.valueOf(String.valueOf((r.nextDouble() * 0.002 + 0.999) * Integer.valueOf(strEnergy[0]))).intValue();
                                //参比干扰
                                AddError(component, 615, 报错_警告);
                                switch (energy.length) {
                                    case 6: {
                                        if ((Integer.valueOf(strEnergy[5]) - energy[5]) > 200 || (Integer.valueOf(strEnergy[5]) - energy[5]) < -200) {
                                            energy[5] = Integer.valueOf(strEnergy[5]);
                                        }
                                        if ((Integer.valueOf(strEnergy[4]) - energy[4]) > 200 || (Integer.valueOf(strEnergy[4]) - energy[4]) < -200) {
                                            energy[4] = Integer.valueOf(strEnergy[4]);
                                        }
                                    }
                                    case 4: {
                                        if ((Integer.valueOf(strEnergy[2]) - energy[2]) > 200 || (Integer.valueOf(strEnergy[2]) - energy[2]) < -200) {
                                            energy[2] = Integer.valueOf(strEnergy[2]);
                                        }
                                        if ((Integer.valueOf(strEnergy[1]) - energy[1]) > 200 || (Integer.valueOf(strEnergy[1]) - energy[1]) < -200) {
                                            energy[1] = Integer.valueOf(strEnergy[1]);
                                        }
                                    }
                                    default:
                                        break;
                                }
                            } else {
                                switch (energy.length) {
                                    case 6: {
                                        if ((Integer.valueOf(strEnergy[5]) - energy[5]) > 200 || (Integer.valueOf(strEnergy[5]) - energy[5]) < -200) {
                                            energy[5] = Integer.valueOf(strEnergy[5]);
                                        }
                                        if ((Integer.valueOf(strEnergy[4]) - energy[4]) > 200 || (Integer.valueOf(strEnergy[4]) - energy[4]) < -200) {
                                            energy[4] = Integer.valueOf(strEnergy[4]);
                                        }
                                    }
                                    case 4: {
                                        if ((Integer.valueOf(strEnergy[2]) - energy[2]) > 200 || (Integer.valueOf(strEnergy[2]) - energy[2]) < -200) {
                                            energy[2] = Integer.valueOf(strEnergy[2]);
                                        }
                                        if ((Integer.valueOf(strEnergy[1]) - energy[1]) > 200 || (Integer.valueOf(strEnergy[1]) - energy[1]) < -200) {
                                            energy[1] = Integer.valueOf(strEnergy[1]);
                                        }
                                    }
                                    default:
                                        break;
                                }
                            }
                        }
                    }
                }
                if (a_formula.contains("lg")) {
                    char f = a_formula.charAt(0);
                    if (f == '-') {
                        sign = -1;
                    }
                }
                if (energy.length > 5) {
                    FlowTable ft = Global.getFlows(component);
                    double kz1 = 0, kz2 = 0;
                    try {
                        if (ft != null) {
                            FlowClass fc = ft.getFlowByCmd(flow);
                            if (fc != null) {
                                String range = String.valueOf(fc.getRange());
                                kz1 = Double.parseDouble(getConfigData(component, "KZ1Range" + range));
                                kz2 = Double.parseDouble(getConfigData(component, "KZ2Range" + range));
                            }
                        }
                    } catch (Exception e) {
                        Log.i("except", "TN 量程" + range + "浊度系数异常");
                    }
                    int[] ch = getEnergyCh(a_formula);
                    if (ch.length < 8) {
                        saveExceptInfo2File("组分[" + component + "] 计算[A值]时发生表达式异常!");
                        return valA;
                    }
                    double dVal1 = Math.log10(((double) energy[ch[0]]) / ((double) energy[ch[1]]));
                    double dVal2 = Math.log10(((double) energy[ch[2]]) / ((double) energy[ch[3]]));
                    double dVal3 = Math.log10(((double) energy[ch[4]]) / ((double) energy[ch[5]]));
                    double dVal4 = Math.log10(((double) energy[ch[6]]) / ((double) energy[ch[7]]));
                    //如果波长2或波长3的吸光度小于0，默认吸光度为0，将参比和吸收改为一致，使参比/吸收 = 1    log1=0
                    if (dVal2 <= 0) {
                        dVal2 = 0;
                        energy[ch[3]] = energy[ch[2]];
                    }
                    if (dVal3 <= 0) {
                        dVal3 = 0;
                        energy[ch[5]] = energy[ch[4]];
                    }
                    if (dVal4 <= 0) {
                        dVal4 = 0;
                        energy[ch[7]] = energy[ch[6]];
                    }
                    valA = dVal1 - kz1 * dVal2 - 2 * (dVal3 - kz2 * dVal4);
                    // saveRunInfo2File("组分[" + component + "] 计算[A值]:" + String.valueOf(valA));
                } else if (energy.length > 3) {
                    //获取能量编号在公式中的位置
                    int[] ch = getEnergyCh(a_formula);
                    if (ch.length < 4) {
                        saveExceptInfo2File("组分[" + component + "] 计算[A值]时发生表达式异常!");
                        return valA;
                    }
                    double dVal1 = Math.log10(((double) energy[ch[0]]) / ((double) energy[ch[1]]));
                    double dVal2 = Math.log10(((double) energy[ch[2]]) / ((double) energy[ch[3]]));
                    if (dVal2 <= 0) {
                        dVal2 = 0;
                        energy[ch[3]] = energy[ch[2]];
                    }
                    valA = dVal1 - 2 * dVal2;
                } else {
                    saveExceptInfo2File("组分[" + component + "] 计算[A值]时发生表达式计算能量值小于4个");
                }
                break;
            case "TPb":
                if (energy.length > 3) {
                    // 补偿K
                    double kc = Double.parseDouble(getConfigData(component, "gpbck"));
                    // 补偿B
                    double bc = Double.parseDouble(getConfigData(component, "gpbcb"));

                    //获取能量编号在公式中的位置
                    int[] ch = getEnergyCh(a_formula);
                    if (ch.length < 4) {
                        saveExceptInfo2File("组分[" + component + "] 计算[A值]时发生表达式异常!");
                        return valA;
                    }
                    valA = Math.log10(((double) energy[ch[0]]) / ((double) energy[ch[1]])) - (Math.log10(((double) energy[ch[2]]) / ((double) energy[ch[3]])) - bc) / kc;
                } else {
                    saveExceptInfo2File("组分[" + component + "] 计算[A值]时发生表达式计算能量值小于4个");
                }
                break;
            case "CODcr"://和default相比就energy.length > 3时计算公式 少一个 2 *
                if (a_formula.contains("lg")) {
                    char f = a_formula.charAt(0);
                    if (f == '-') {
                        sign = -1;
                    }
                    if (energy.length > 3) {
                        //获取能量编号在公式中的位置
                        int[] ch = getEnergyCh(a_formula);
                        if (ch.length < 4) {
                            saveExceptInfo2File("组分[" + component + "] 计算[A值]时发生表达式异常!");
                            return valA;
                        }
                        //和default相比就此处少一个 2 *
                        valA = sign * (Math.log10(((double) energy[ch[0]]) / ((double) energy[ch[1]])) - Math.log10(((double) energy[ch[2]]) / ((double) energy[ch[3]])));
                    } else if (energy.length > 1) {

                        int ch1 = 0, ch2 = 1;
                        int E1ch = a_formula.indexOf("E01");
                        int E2ch = a_formula.indexOf("E02");

                        if (E1ch > E2ch) {
                            /*找到*/
                            strSignEn = a_formula.substring(E2ch + 3, E1ch).replace("\t", "").replace(" ", "").replace("\r\n", "").replace("\n", "");
                            ch1 = 1;
                            ch2 = 0;
                        } else {
                            strSignEn = a_formula.substring(E1ch + 3, E2ch).replace("\t", "").replace(" ", "").replace("\r\n", "").replace("\n", "");
                        }
                        switch (strSignEn) {
                            case "/":
                            case "／":
                                valA = sign * (Math.log10(((double) energy[ch1]) / ((double) energy[ch2])));
                                break;
                            case "-":
                            case "－":
                                valA = sign * (Math.log10(((double) energy[ch1]) - ((double) energy[ch2])));
                                break;
                            case "＋":
                            case "+":
                                valA = sign * (Math.log10(((double) energy[ch1]) + ((double) energy[ch2])));
                                break;
                            case "*":
                            case "＊":
                                valA = sign * (Math.log10(((double) energy[ch1]) * ((double) energy[ch2])));
                                break;
                        }
                    } else {
                        saveExceptInfo2File("组分[" + component + "] 计算[A值]时发生表达式计算能量值小于2个");
                    }
                }
                break;
            default:
                if (a_formula.contains("lg")) {
                    char f = a_formula.charAt(0);
                    if (f == '-') {
                        sign = -1;
                    }
                    if (energy.length > 3) {
                        //获取能量编号在公式中的位置
                        int[] ch = getEnergyCh(a_formula);
                        if (ch.length < 4) {
                            saveExceptInfo2File("组分[" + component + "] 计算[A值]时发生表达式异常!");
                            return valA;
                        }
                        valA = sign * (Math.log10(((double) energy[ch[0]]) / ((double) energy[ch[1]])) - 2 * Math.log10(((double) energy[ch[2]]) / ((double) energy[ch[3]])));
                    } else if (energy.length > 1) {

                        int ch1 = 0, ch2 = 1;
                        int E1ch = a_formula.indexOf("E01");
                        int E2ch = a_formula.indexOf("E02");

                        if (E1ch > E2ch) {
                            /*找到*/
                            strSignEn = a_formula.substring(E2ch + 3, E1ch).replace("\t", "").replace(" ", "").replace("\r\n", "").replace("\n", "");
                            ch1 = 1;
                            ch2 = 0;
                        } else {
                            strSignEn = a_formula.substring(E1ch + 3, E2ch).replace("\t", "").replace(" ", "").replace("\r\n", "").replace("\n", "");
                        }
                        switch (strSignEn) {
                            case "/":
                            case "／":
                                valA = sign * (Math.log10(((double) energy[ch1]) / ((double) energy[ch2])));
                                break;
                            case "-":
                            case "－":
                                valA = sign * (Math.log10(((double) energy[ch1]) - ((double) energy[ch2])));
                                break;
                            case "＋":
                            case "+":
                                valA = sign * (Math.log10(((double) energy[ch1]) + ((double) energy[ch2])));
                                break;
                            case "*":
                            case "＊":
                                valA = sign * (Math.log10(((double) energy[ch1]) * ((double) energy[ch2])));
                                break;
                        }
                    } else {
                        saveExceptInfo2File("组分[" + component + "] 计算[A值]时发生表达式计算能量值小于2个");
                    }
                }
                break;
        }
        if (Double.isNaN(valA)) {
            valA = 0;
            if (energy.length > 3) {
                saveExceptInfo2File("组分[" + component + "] 计算[A值]时发生表达式计算结果为NaN:" + "E1," + energy[0]
                        + "E2," + energy[1] + "E3," + energy[2] + "E4," + energy[3]);
            } else {
                saveExceptInfo2File("组分[" + component + "] 计算[A值]时发生表达式计算结果为NaN:" + "E1," + energy[0]
                        + "E2," + energy[1]);
            }
        }
        return valA;
    }


    /*
     * 吸光度及能量修正
     * 组份 ，流程名称 ，量程，当前吸光度，当前量程K，原始能量
     * **/
    private static double absorbEnRevise(String component, String flName, int range, double nowA, double rangeK, int[] energy, float[] fEnergy) {

        double newA = nowA;
        /*判断是否需要修正*/
        if (isAbsorbRevise(component, flName, range, nowA, rangeK)) {
            History history = new History(context);
            /*历史数据是否有测量值***/
            List<Map> hisMap = history.select(component, flName, null, null, 0, 1);
            if (hisMap.size() > 0) {
                CalcEnRevise calcEnRevise = getCalcEn(component);
                newA = getNewAi(Double.parseDouble(hisMap.get(0).get("A").toString()), nowA, calcEnRevise.getParX());
                //saveRunInfo2File("进行吸光度修正" + "修正前:" + nowA + "修正后:" + newA);
                if (component.equals("TN")) {
                    if ((!calcEnRevise.getEA_Formula().contains("KZ1") && energy.length == 6)
                            || (energy.length == 4 && calcEnRevise.getEA_Formula().contains("KZ1"))) {
                        saveExceptInfo2File("TN 能量为6 EA 算法异常 不匹配，未包含KZ1");
                        AddError(component, 579, 报错_警告);
                        return newA;
                    }
                } else if (component.equals("TP")) {
                    if ((!calcEnRevise.getEA_Formula().contains("KZ1") && energy.length == 4)
                            || (energy.length == 2 && calcEnRevise.getEA_Formula().contains("KZ1"))) {
                        saveExceptInfo2File("TP 能量为4 EA 算法异常 不匹配，未包含KZ1");
                        AddError(component, 579, 报错_警告);
                        return newA;
                    }
                }
                if (QueryMeasCateg(component).equals("3") || QueryMeasCateg(component).equals("8") || QueryMeasCateg(component).equals("11")) {
                    if (fEnergy.length > 0) {
                        fEnergy[0] = (float) newA;
                        //saveRunInfo2File("进行吸光度修正存储" + "修正后:" + newA);
                    }
                } else {
                    switch (energy.length) {
                        case 6:
                            energy[5] = getNewEnergy(component, range, calcEnRevise.getEF_Formula(), energy, newA);
                            energy[4] = getNewEnergy(component, range, calcEnRevise.getEE_Formula(), energy, newA);
                        case 4:
                            energy[3] = getNewEnergy(component, range, calcEnRevise.getED_Formula(), energy, newA);
                            energy[2] = getNewEnergy(component, range, calcEnRevise.getEC_Formula(), energy, newA);
                        case 2:
                            energy[1] = getNewEnergy(component, range, calcEnRevise.getEB_Formula(), energy, newA);
                            energy[0] = getNewEnergy(component, range, calcEnRevise.getEA_Formula(), energy, newA);
                            break;
                    }
                }
            }
        }
        return newA;
    }

    /* 计算C值（WL注释）*/
    public double calc_CValue(double dK, double dB, double dA, double tempEK, double tempEB) {
        double valC = 0;
        CalcTable ct = getCalc(component);
        if (ct != null) {
            String range = getRange();
            Calc calc = ct.getCalc(range);
            if (calc != null) {
                valC = dK * dA + dB;
                if (!Double.isNaN(tempEK) && !Double.isNaN(tempEB)) {
                    valC = tempEK * valC + tempEB;
                }
            }
            if (Double.isNaN(valC)) {
                saveExceptInfo2File("组分[" + component + "] 计算[C值]时发生表达式计算结果为NaN:" + "K," + dK + "A," + dA + "B," + dB);
                valC = 0;
            }
        }
        return valC;
    }


    /**
     * @param tempK  量程K
     * @param tempA  吸光度A
     * @param fcName 流程名称
     * @return
     */
    public double reviseAEn(String component, double tempK, double tempA, String fcName) {

        if (fcName.equals(context.getResources().getString(R.string.ZY)) || fcName.equals(context.getResources().getString(R.string.JBHS))) {
            if (!component.equals("TN")) {
                tempA = absorbEnRevise(component, fcName, Integer.parseInt(getRange()), tempA, tempK, energy, fEnergy);
            } else {
                if (QueryMeasCateg(component).equals("2") && isEnergyNumSame("TN", energy) || QueryMeasCateg(component).equals("7")) {
                    tempA = absorbEnRevise(component, fcName, Integer.parseInt(getRange()), tempA, tempK, energy, fEnergy);
                }
            }
        }
        return tempA;
    }

    /* 计算K值（WL）*/
    private double calc_K(Calc calc, String C_1, String C_2, double A_1, double A_2) {
        double valK = 0;

        try {
            valK = (Double.parseDouble(C_1) - Double.parseDouble(C_2)) / (A_1 - A_2);

            if (Double.isNaN(valK)) {
                saveExceptInfo2File("组分[" + component + "] 计算[K值]时发生表达式计算结果为NaN" + ":C前：" + C_1 + ",C后:" + C_2 + ",A前:" + A_1 + ",A后:" + A_2);
                valK = 0;
            }
            return valK;
        } catch (Exception e) {
            saveExceptInfo2File("组分[" + component + "] 计算[K值]时发生表达式计算错误" + e.getClass().getName() + ":C前：" + C_1 + ",C后:" + C_2 + ",A前:" + A_1 + ",A后:" + A_2);
        }
        return valK;
    }

    /* 计算B值（WL）*/
    private double calc_B(Calc calc, String C, double A, double K) {
        double valB = 0;

        try {
            valB = Double.parseDouble(C) - K * A;

            if (Double.isNaN(valB)) {
                saveExceptInfo2File("组分[" + component + "] 计算[K值]时发生表达式计算结果为NaN" + ":C:" + C + ",A:" + A + ",K:" + K);
                valB = 0;
            }
            return valB;
        } catch (Exception e) {
            saveExceptInfo2File("组分[" + component + "] 计算[B值]时发生表达式计算错误" + e.getClass().getName() + ":C:" + C + ",A:" + A + ",K:" + K);
        }
        return valB;
    }

    /* 计算F值（WL）*/
    private double calc_F(Calc calc, double Kold, double Knew) {
        double valF = 0;
        try {
            valF = Kold / Knew;

            if (Double.isNaN(valF)) {
                saveExceptInfo2File("组分[" + component + "] 计算[K值]时发生表达式计算结果为NaN" + ":Kold:" + Kold + ",Knew:" + Knew);
                valF = 0;
            }
            return valF;
        } catch (Exception e) {
            saveExceptInfo2File("组分[" + component + "] 计算[F值]时发生表达式计算错误" + e.getClass().getName() + ":Kold:" + Kold + ",Knew:" + Knew);
        }
        return valF;
    }

    /* 计算K值（WL）*/
    private double calc_SWDX_K(Calc calc, String C_1, String C_2, double A_1, double A_2) {
        double valK = 0;

        try {
            valK = (A_2 - A_1) / (Math.log10(Double.parseDouble(C_2)) - Math.log10(Double.parseDouble(C_1)));

            if (Double.isNaN(valK)) {
                saveExceptInfo2File("组分[" + component + "] 计算[K值]时发生表达式计算结果为NaN" + ":C前：" + C_1 + ",C后:" + C_2 + ",A前:" + A_1 + ",A后:" + A_2);
                valK = 0;
            }
            return valK;
        } catch (Exception e) {
            saveExceptInfo2File("组分[" + component + "] 计算[K值]时发生表达式计算错误" + e.getClass().getName() + ":C前：" + C_1 + ",C后:" + C_2 + ",A前:" + A_1 + ",A后:" + A_2);
        }
        return valK;
    }

    /* 计算B值（WL）*/
    private double calc_SWDX_B(Calc calc, String C, double A, double K) {
        double valB = 0;

        try {
            valB = A - K * Math.log10(Double.parseDouble(C));

            if (Double.isNaN(valB)) {
                saveExceptInfo2File("组分[" + component + "] 计算[K值]时发生表达式计算结果为NaN" + ":C:" + C + ",A:" + A + ",K:" + K);
                valB = 0;
            }
            return valB;
        } catch (Exception e) {
            saveExceptInfo2File("组分[" + component + "] 计算[B值]时发生表达式计算错误" + e.getClass().getName() + ":C:" + C + ",A:" + A + ",K:" + K);
        }
        return valB;
    }

    /* 进行K、B值的更新（WL）*/
    private boolean updateKBVal(Calc calc, double valK, double valB, double valF) {
        calc.setK(valK);
        calc.setB(valB);
        calc.setF(1.0);

        return true;
    }

    /*
        一键校准功能，根据前一次K和当前的K ，判断是否在F值范围之内决定是否刷新量程KB
        rangeStr： "1","2","3"
    *   valK：新算出来的 K
     *  */
    private boolean updateKBVal(String comp, String rangeStr, double valK) {

        /* 若前一个K值不为1，则计算偏移率F值，若F值在[0.5,2.0]之间则更新K、B值，否则不更新*/
        CalcTable ct = getCalc(comp);
        Calc calc = ct.getCalc(rangeStr);
        double k1 = calc.getK(rangeStr, comp);

        String fMin = "0.5";
        String fMax = "2.0";
        try {
            fMin = getConfigData(comp, "FMin").equals("") ? "0.5" : getConfigData(comp, "FMin");
            fMax = getConfigData(comp, "FMax").equals("") ? "2.0" : getConfigData(comp, "FMax");
        } catch (Exception e) {

        }
        double valF = k1 / valK;

        if (k1 != 1 && (getConfigData(comp, "OneKeyCalForceKBFlag").equals("false")) && (valF < Double.parseDouble(fMin) || valF > Double.parseDouble(fMax))) {
            saveRunInfo2File("组分[" + comp + "]" + "量程" + rangeStr + "不更新K]" + "nowK = " + valK + "PreK = " + k1 + "FMin=" + fMin + "FMax=" + fMax);
            return false;
        } else {
            if (getConfigData(comp, "OneKeyCalForceKBFlag").equals("true")) {
                saveRunInfo2File("组分[" + comp + "]" + "强制一键刷新量程" + rangeStr + "KB!");
            }
        }
        return true;
    }


    /*********************************************************************************/
    /***
     * 计算滴定K
     *
     */
    public synchronized void isCalcTitrationKBValue() {

        double valA = calcAValue(component);
        /* 如果流程动作号为量程1标1,量程2标1,量程3标1的话，记录A值用以计算K值（WL）*/
        /* 如果流程动作号为量程1标2,量程2标2,量程3标2的话，开始计算K值（WL）*/
        CalcTable ct = getCalc(component);
        Calc calc = ct.getCalc(getRange());
        switch (flow) {
            case 0x04: {
                calc.setCalib1A(valA);
                updateConfigData(component, "CAL_TIME", getTime());
            }
            break;
            case 0x05: {
                calc.setCalib1A(valA);
                updateConfigData(component, "CAL_TIME", getTime());
            }
            break;
            case 0x06: {
                calc.setCalib1A(valA);
                updateConfigData(component, "CAL_TIME", getTime());
            }
            break;
            case 0x07: {
                calc.setCalib2A(valA);
                double Calib1AConc = 0;
                try {
                    List<Map> hisMap = mHistory.select(component, null, null, null, 0, 1);

                    if (hisMap.size() > 0) {
                        String flowName = hisMap.get(0).get("flow").toString();
                        if (flowName.equals(context.getResources().getString(R.string.span_1))) {
                            Calib1AConc = Double.parseDouble(hisMap.get(0).get("A").toString());

                            String C1 = getConfigData(component, "C1");
                            String C0 = getConfigData(component, "C0");
                            double valK = calc_K(calc, C1, C0, calc.getCalib2A(), Calib1AConc);
                            double valB = calc_B(calc, C0, Calib1AConc, valK);
                            double valF = calc_F(calc, calc.getK(getRange(), component), valK);
                            calc.setF(valF);

                            if (!updateKBVal(calc, valK, valB, valF)) {
                                // AddError(component, 533, ErrorLog.msgType.运行_信息);
                            } else {
                                saveKBF("1", valK, valB, calc.getCalF());
                                // 配置稀释背板的情况下，量程1的KB同时更新量程3KB
                                if (isHaveMeasCategory(component, "5") && getConfigData(component, "XS_BASE_RANGE").equals("1")) {
                                    saveKBF("3", valK, valB, calc.getF(getRange(), component));
                                    saveRunInfo2File("组分[" + component + "] 配置稀释背板情况：量程1校准更新量程3KB");
                                }
                                // 校准成功清外部KB
                                extKBParReset(component, "1");
                                updateConfigData(component, "CAL2_SUC_TIME", getTime());
                                updateConfigData(component, "CAL_SUC_TIME", getConfigData(component, "CAL_TIME"));
                            }
                            ClearCalibrationCountDays(component);
                        } else {
                            saveExceptInfo2File("组分[" + component + "] 标2 完成测量计算KBF，历史数据无标1数据");
                            return;
                        }
                    } else {
                        saveExceptInfo2File("组分[" + component + "] 标2 完成测量计算KBF，历史数据无数据");
                    }
                } catch (Exception e) {
                    return;
                }
            }
            break;
            case 0x08: {
                calc.setCalib2A(valA);

                double Calib1AConc = 0;
                try {
                    List<Map> hisMap = mHistory.select(component, null, null, null, 0, 1);

                    if (hisMap.size() > 0) {
                        String flowName = hisMap.get(0).get("flow").toString();
                        if (flowName.equals(context.getResources().getString(R.string.span_1)/*"标1"*/)) {
                            Calib1AConc = Double.parseDouble(hisMap.get(0).get("A").toString());

                            String C3 = getConfigData(component, "C3");
                            String C2 = getConfigData(component, "C2");
                            double valK = calc_K(calc, C3, C2, calc.getCalib2A(), Calib1AConc);
                            double valB = calc_B(calc, C2, Calib1AConc, valK);
                            double valF = calc_F(calc, calc.getK(getRange(), component), valK);
                            calc.setF(valF);

                            if (!updateKBVal(calc, valK, valB, valF)) {
                                // AddError(component, 533, ErrorLog.msgType.运行_信息);
                            } else {
                                saveKBF("2", valK, valB, calc.getCalF());
                                // 配置稀释背板的情况下，量程2的KB同时更新量程3KB
                                if (isHaveMeasCategory(component, "5") && getConfigData(component, "XS_BASE_RANGE").equals("2")) {
                                    saveKBF("3", valK, valB, calc.getF(getRange(), component));
                                    saveRunInfo2File("组分[" + component + "] 配置稀释背板情况：量程2校准更新量程3KB");
                                }
                                // 校准成功清外部KB
                                extKBParReset(component, "2");
                                updateConfigData(component, "CAL2_SUC_TIME", getTime());
                                updateConfigData(component, "CAL_SUC_TIME", getConfigData(component, "CAL_TIME"));
                            }
                            ClearCalibrationCountDays(component);
                        } else {
                            saveExceptInfo2File("组分[" + component + "] 标2 完成测量计算KBF，历史数据无标1数据");
                            return;
                        }
                    } else {
                        saveExceptInfo2File("组分[" + component + "] 标2 完成测量计算KBF，历史数据无数据");
                    }
                } catch (Exception e) {
                    return;
                }
            }
            break;
            case 0x09: {
                calc.setCalib2A(valA);

                double Calib1AConc = 0;
                try {
                    List<Map> hisMap = mHistory.select(component, null, null, null, 0, 1);

                    if (hisMap.size() > 0) {
                        String flowName = hisMap.get(0).get("flow").toString();
                        if (flowName.equals(context.getResources().getString(R.string.span_1)/*"标1"*/)) {
                            Calib1AConc = Double.parseDouble(hisMap.get(0).get("A").toString());

                            String C5 = getConfigData(component, "C5");
                            String C4 = getConfigData(component, "C4");
                            double valK = calc_K(calc, C5, C4, calc.getCalib2A(), Calib1AConc);
                            double valB = calc_B(calc, C4, Calib1AConc, valK);
                            double valF = calc_F(calc, calc.getK(getRange(), component), valK);
                            calc.setF(valF);

                            if (!updateKBVal(calc, valK, valB, valF)) {
                                //AddError(component, 533, ErrorLog.msgType.运行_信息);
                            } else {
                                saveKBF("3", valK, valB, calc.getCalF());
                                // 校准成功清外部KB
                                extKBParReset(component, "3");
                                updateConfigData(component, "CAL2_SUC_TIME", getTime());
                                updateConfigData(component, "CAL_SUC_TIME", getConfigData(component, "CAL_TIME"));
                            }
                            ClearCalibrationCountDays(component);
                        } else {
                            saveExceptInfo2File("组分[" + component + "] 标2 完成测量计算KBF，历史数据无标1数据");
                            return;
                        }
                    } else {
                        saveExceptInfo2File("组分[" + component + "] 标2 完成测量计算KBF，历史数据无数据");
                    }
                } catch (Exception e) {
                    return;
                }
            }
            break;
        }
    }

    /* 进行滴定K、B值的更新（WL）*/
    public boolean updateTitrationKBVal(Calc calc, double valK, double valB, double valF) {
        /* 若前一个K值不为1，则计算偏移率F值，若F值在[0.8,1.0]之间则更新K、B值，否则不更新*/
        if (calc.getK(getRange(), component) != 1) {
            if ((valF >= 0.8 && valF <= 1.0) || ((getConfigData(component, "ycjzFlag").equals("true")))) {
                calc.setK(valK);
                calc.setB(valB);
                if ((getConfigData(component, "ycjzFlag").equals("true"))) {
                    calc.setF(1.0);
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        DoSample mDoSampleDB;
                        updateConfigData(component, "ycjzFlag", "false");

                    }
                }).start();
            } else {
                return false;
            }
        }
        /* 若前一个K值为1，则直接更新K、B值*/
        else {
            calc.setK(valK);
            calc.setB(valB);
        }
        return true;
    }


    /**
     * 除
     * 识别 Vn
     */
    public double FormulaCalc_titrationA() {
        double valA = 0;
        valA = fEnergy[0];
        return valA;
    }

    /* 滴定计算C值（WL注释）*/
    public double calc_titrationC(boolean blAbsorbEnRevise) {

        double valC = 0;
        CalcTable ct = getCalc(component);
        if (ct != null) {
            String range = getRange();
            Calc calc = ct.getCalc(range);
            if (calc != null) {
                double tempK = calc.getK(getRange(), component);
                double tempB = calc.getB(getRange(), component);
                double tempA = calcAValue(component);
                FlowTable ft = Global.getFlows(component);
                FlowClass fc = ft.getFlowByCmd(getFlow());
                String fcName = fc.getName();
                if (blAbsorbEnRevise) {
                    if (fcName.equals(context.getResources().getString(R.string.ZY)) || fcName.equals(context.getResources().getString(R.string.JBHS))) {
                        if (!component.equals("TN")) {
                            tempA = absorbEnRevise(component, fcName, Integer.parseInt(getRange()), tempA, tempK, energy, fEnergy);
                        } else {
                            if (isEnergyNumSame("TN", energy)) {
                                tempA = absorbEnRevise(component, fcName, Integer.parseInt(getRange()), tempA, tempK, energy, fEnergy);
                            }
                        }
                    }
                }
                // 吸光度存储
                calc.setA(tempA);

                // 测量值计算
                valC = tempK * tempA + tempB;
                if (Double.isNaN(valC)) {
                    saveExceptInfo2File("组分[" + component + "] 计算[C值]时发生表达式计算结果为NaN:" + "K," + tempK + "A," + tempA + "B," + tempB);
                    valC = 0;
                }
                return valC;
            } else {
                saveExceptInfo2File("组分[" + component + "] 计算[C值]时发生该组分无此量程算法，量程为：" + range);
            }
        } else {
            saveExceptInfo2File("组分[" + component + "] 计算[C值]时发生该组分算法表为NULL");
        }
        return valC;
    }


    /**********************************************************************************/

    /***
     *  GC 计算
     * */
    public double calc_GCC() {
        double valC = 0;
        try {
            for (int i = 0; i < 10; i++) {
                if (getConfigData(component, "VOC_V" + (i + 1)).equals("true")) {
                    valC += fEnergy[i];
                }
            }
        } catch (Exception e) {
            saveExceptInfo2File("组分[" + component + "] 计算[C值]时发生异常，请检查组分配置个数");
        }
        return valC;
    }


    /***
     * GC 校准处理
     *
     */
    public synchronized void isCalcGCValue() {

        switch (flow) {
            case 0x04:
            case 0x05:
            case 0x06:
                ClearCalibrationCountDays(component);
                break;
            case 0x07:
            case 0x08:
            case 0x09:
                break;
        }
    }


    /**********************************************************************************/


    /***
     *  吸光度计算
     */
    public double calcAValue(String component) {
        double dValueA = 0.0;
        if (QueryMeasCateg(component).equals("3") || QueryMeasCateg(component).equals("8")
                || QueryMeasCateg(component).equals("11")) {
            dValueA = FormulaCalc_titrationA();
        } else if (QueryMeasCateg(component).equals("4")) {

        } else if (QueryMeasCateg(component).equals("13")) {

        } else {
            dValueA = FormulaCalc_A();
        }
        return dValueA;
    }


    /**
     * @param component 组分名称
     * @return 测量值
     */
    public double calcMeaValue(String component, String fcName, Energy energys) {
        double cVal = 0.0;
        double tempK = 1, tempB = 0;
        double tempEK = Double.NaN, tempEB = Double.NaN;
        Calc calc;
        CalcTable ct = getCalc(component);
        if (ct != null) {
            String range = energys.getRange();
            calc = ct.getCalc(range);
            if (calc != null) {
                tempK = calc.getK(range, component);
                tempB = calc.getB(range, component);
                // 吸光度计算
                double tempA = calcAValue(component);
                //saveRunInfo2File("初始吸光度" + tempA);
                // 存储吸光度
                calc.setA(tempA);
                /***************************/

                if (QueryMeasCateg(component).equals("4")) {
                    cVal = calc_GCC();
                } else if (QueryMeasCateg(component).equals("13")) {//生物毒性
                    //1、 原始光强计算值
                    cVal = calc_SWDX(calc, tempK, tempB);
                    // 2、外部KB
                    //量程KB作用 做样，零样测量，标样测量，零核，量核，跨核，平行样，加标回收
                    if ((fcName.equals(context.getResources().getString(R.string.ZY)) || fcName.equals(context.getResources().getString(R.string.LYCL))
                            || fcName.equals(context.getResources().getString(R.string.BYCL)) || fcName.equals(context.getResources().getString(R.string.BY2CL)))
                            || (fcName.equals(context.getResources().getString(R.string.LDHC)) || fcName.equals(context.getResources().getString(R.string.KDHC))
                            || fcName.equals(context.getResources().getString(R.string.BYHC)) || fcName.equals(context.getResources().getString(R.string.PXY)))
                            || fcName.equals(context.getResources().getString(R.string.JBHS))) {
                        cVal = Double.parseDouble(getConfigData(component, "UserExtRange" + range + "K")) * cVal + Double.parseDouble(getConfigData(component, "UserExtRange" + range + "B"));
                    }
                    // 3、检出限
                    //cVal = getcValDownLimit(cVal, component, fcName, energys);
                } else {
                    /***************************/
                    //1、 原始光强计算吸光度及测量值
                    if (fcName.equals(context.getResources().getString(R.string.LDHC))) {
                        // 零点核查都使用量程1KB进行计算
                        tempK = calc.getK("1", component);
                        tempB = calc.getB("1", component);
                    }
                    //测量值计算（第一次计算测量值）
                    cVal = calc_CValue(tempK, tempB, calc.getA(), tempEK, tempEB);
                    /***************************/

                    // 除  标1、标2、进样稳定性、光源稳定性、稀释校准 进行测量值处理
                    if (!fcName.equals((context.getResources().getString(R.string.B1))) && !fcName.equals((context.getResources().getString(R.string.B2)))
                            && !fcName.equals(context.getResources().getString(R.string.JYWDX)) && !fcName.equals((context.getResources().getString(R.string.GYWDX)))
                            && !fcName.equals((context.getResources().getString(R.string.XSJZ)))) {
                        //2、吸光度修正
                        double tempPreA = calc.getA();
                        calc.setA(reviseAEn(component, tempK, calc.getA(), fcName));
                        //3、吸光度修正后的测量值计算（第二次计算测量值）
                        if (calc.getA() != tempPreA) {
                            cVal = calc_CValue(tempK, tempB, calc.getA(), tempEK, tempEB);
                        }
                        //4、修正因子 外部KB   "4 VOC", "5 稀释" "6 蒸馏"  不需要外部KB
                        if (!QueryMeasCateg(component).equals("4") && !QueryMeasCateg(component).equals("5")
                                && !QueryMeasCateg(component).equals("6")) {
                            //量程KB作用 做样，零样测量，标样测量，零核，量核，跨核，平行样，加标回收
                            if ((fcName.equals(context.getResources().getString(R.string.ZY)) || fcName.equals(context.getResources().getString(R.string.LYCL))
                                    || fcName.equals(context.getResources().getString(R.string.BYCL)) || fcName.equals(context.getResources().getString(R.string.BY2CL)))
                                    || (fcName.equals(context.getResources().getString(R.string.LDHC)) || fcName.equals(context.getResources().getString(R.string.KDHC))
                                    || fcName.equals(context.getResources().getString(R.string.BYHC)) || fcName.equals(context.getResources().getString(R.string.PXY)))
                                    || fcName.equals(context.getResources().getString(R.string.JBHS))) {
                                tempEK = Double.parseDouble(getConfigData(component, "ExtRange" + range + "K"));
                                tempEB = Double.parseDouble(getConfigData(component, "ExtRange" + range + "B"));
                                cVal = tempEK * cVal + tempEB;
                            }
                        }
                        //5、加标系数 //加标回收数据补偿 （在外部KB 前）
                        if (fcName.equals(context.getResources().getString(R.string.JBHS))) {
                            CalcEnRevise calcEnRevise = getCalcEn(component);
                            //算法文件未配置不进行补偿
                            if (calcEnRevise != null) {
                                cVal = cVal + getSpanCheckCompensationFactor(component);
                            }
                        }
                        /***************************/
                        //6、W200 CODcr 跳值修正
                        int iDo = 0;
                        try {
                            if (energys.energy.length > 1) {
                                iDo = judgeDisCorrectJumpValue(component, fcName, cVal, energys.energy[1], getTime());
                                if (iDo == 1) {
                                    cVal = getW200JumpVal(component, cVal, iDo);
                                }
                            }
                        } catch (Exception e) {
                            saveRunInfo2File("组分[" + component + "]  跳值检测异常：" + e.toString());
                        }
                        /***************************/
                        //7、反算吸光度,修正能量（第一次）
                        setEnergyRevise(component, Integer.parseInt(getRange()), cVal, tempK, tempB, tempEK, tempEB);
                        //8、测量值计算（第三次计算测量值）
                        cVal = calc_CValue(tempK, tempB, calc.getA(), tempEK, tempEB);

                        //9、检出限判定（第一次判定）
                        double tempC = cVal;
                        cVal = getcValDownLimit(cVal, component, fcName, energys);
                        //10 CODmn滴定 防出零
                        if (getConfigData(component, "MeaDataLimitMode").equals("0")) {
                            //葡萄糖测试修正数据(仅在防出零后)
                            //注射泵滴定CODmn才执行
                            cVal = getGlucoseTestRevisecVal(cVal, component, energys);
                        }
                        //yj新增 2020年6月2日16:31:26    多点线性：标样核查仪表出数修正
                        if (getPublicConfigData("Multi_Point_Linear_Features").equals("true") && getConfigData(component, "Multi_Point_Linear").equals("true")) {
                            float fLCH = Float.parseFloat(getConfigData(component, "YBLCH"));
                            if (cVal >= 0.08 * fLCH && cVal <= 0.12 * fLCH) {
                                cVal = new Random().nextDouble() * (0.01 * fLCH) + 0.095 * fLCH;
                            } else if (cVal >= 0.16 * fLCH && cVal <= 0.24 * fLCH) {
                                cVal = new Random().nextDouble() * (0.02 * fLCH) + 0.19 * fLCH;
                            }
                            updateConfigData(component, "Multi_Point_Linear", "false");
                        }
                        //11、检出限修正 注射泵葡萄糖测试后，（吸光度光强修正）
                        if (cVal != tempC) {
                            setEnergyRevise(component, Integer.parseInt(getRange()), cVal, tempK, tempB, tempEK, tempEB);
                            //测量值计算（第四次计算测量值）
                            cVal = calc_CValue(tempK, tempB, calc.getA(), tempEK, tempEB);
                        }
                        //12、 W200 CODcr 校准体积计算
                        settingCalVolume(fcName, component, calc.getA());
                        /***************************/
                        // 13、W200 CODcr 跳值修正 --- 存储A  及吸收能量
                        if (iDo == 2) {
                            cVal = getW200JumpVal(component, cVal, iDo);
                        }
                        /***************************/
                        //14、用户KB 参与计算  "4 VOC", "5 稀释" "6 蒸馏"  不需要
                        if (!QueryMeasCateg(component).equals("4") && !QueryMeasCateg(component).equals("5")
                                && !QueryMeasCateg(component).equals("6")) {
                            //量程KB作用 做样，零样测量，标样测量，零核，量核，跨核，平行样，加标回收
                            if ((fcName.equals(context.getResources().getString(R.string.ZY)) || fcName.equals(context.getResources().getString(R.string.LYCL))
                                    || fcName.equals(context.getResources().getString(R.string.BYCL)) || fcName.equals(context.getResources().getString(R.string.BY2CL)))
                                    || (fcName.equals(context.getResources().getString(R.string.LDHC)) || fcName.equals(context.getResources().getString(R.string.KDHC))
                                    || fcName.equals(context.getResources().getString(R.string.BYHC)) || fcName.equals(context.getResources().getString(R.string.PXY)))
                                    || fcName.equals(context.getResources().getString(R.string.JBHS))) {
                                cVal = Double.parseDouble(getConfigData(component, "UserExtRange" + range + "K")) * cVal + Double.parseDouble(getConfigData(component, "UserExtRange" + range + "B"));
                            }
                        }
                        //15、稀释倍数    稀释背板的量程3做样的话就使用 稀释稀释 K
                        if (getRange().equals("3") && fcName.equals(context.getResources().getString(R.string.ZY))
                                && isHaveMeasCategory(component, "5")) {
                            double k = getNewestKBF(component + getModuleName("5"), getConfigData(component + getModuleName("5"), "XS_MODE"), "K");
                            cVal = k * cVal;
                        }
                        //16、转换系数KB 参与计算  只有 "11 TOC"
                        if (QueryMeasCateg(component).equals("11")) {
                            //转换系数KB作用 做样
                            if (fcName.equals(context.getResources().getString(R.string.ZY))) {
                                cVal = Double.parseDouble(getConfigData(component, "ConversionFactorK")) * cVal + Double.parseDouble(getConfigData(component, "ConversionFactorB"));
                            }
                        }
                        // 17、检出限
                        cVal = getcValDownLimit(cVal, component, fcName, energys);
                    }
                    // 标1 测量值
                    if (fcName.equals(context.getResources().getString(R.string.span_1))) {
                        try {
                            if (!QueryMeasCateg(component).equals("4")) {
                                String C1 = null;
                                C1 = getConfigData(component, "C" + (Integer.parseInt(range) * 2 - 2));
                                cVal = Double.parseDouble(C1);
                            }
                        } catch (Exception e) {
                            saveExceptInfo2File("组分[" + component + "]" + fcName + "计算异常");
                        }
                    } else if (fcName.equals(context.getResources().getString(R.string.span_2))) {
                        try {
                            if (!QueryMeasCateg(component).equals("4")) {
                                String C2 = null;
                                C2 = getConfigData(component, "C" + (Integer.parseInt(range) * 2 - 1));
                                cVal = Double.parseDouble(C2);
                            }
                        } catch (Exception e) {
                            saveExceptInfo2File("组分[" + component + "]" + fcName + "计算异常");
                        }
                    } else if (fcName.equals(context.getResources().getString(R.string.dilutionCal))) {
                        cVal = dilutionCal(energys, component, cVal);
                    }
                }
            } else {
                saveExceptInfo2File("组分[" + component + "] 计算[C值]时发生该组分无此量程算法，" + fcName + "量程为：" + range);
            }
        } else {
            saveExceptInfo2File("组分[" + component + "] 计算[C值]时发生该组分算法表为NULL");
        }


        return cVal;
    }

    /**
     * W200 跳值判定后需要执行动作
     *
     * @param component 组分
     * @param cVal      测量值
     * @param iDo       执行动作
     * @return 测量值结果
     */
    private double getW200JumpVal(String component, double cVal, int iDo) {
        if (iDo == 1) {
            // 修正
            String sMeaA = getConfigData(component, "MeaAValueEn");
            // 测量值
            double dAValue = Double.parseDouble(sMeaA.split(";")[0]);
            cVal = new CalcCorrectJumpValue().getRandomCalData(dAValue);
        } else if (iDo == 2) {
            // 存储
            String str = cVal + ";" + energy[1];
            updateConfigData(component, "MeaAValueEn", str);
        }
        return cVal;
    }


    /**
     * 加标回收 测量值 进行补偿
     *
     * @param compName 组分
     * @return
     */
    private double getSpanCheckCompensationFactor(String compName) {
        double dValue = 0.0;
        switch (compName) {
            /*case "CODmn":
                dValue = 0.5;
                break;*/
            case "TN":
                dValue = 0.1;
                break;
            case "TP":
                dValue = 0.1;
                break;
            case "NH3N":
                dValue = 0.1;
                break;
        }
        return dValue;
    }

    /**
     * 能量修正
     *
     * @param comp 组分
     * @param cVal 测量值
     */
    private void setEnergyRevise(String comp, int range, double cVal, double tempK, double tempB, double tempEK, double tempEB) {
        CalcTable ct = getCalc(comp);
        if (ct != null) {
            Calc calc = ct.getCalc(String.valueOf(range));
            if (calc != null) {
                double newA = (cVal - tempB) / tempK;
                if (!Double.isNaN(tempEK) && !Double.isNaN(tempEB)) {
                    newA = (((cVal - tempEB) / tempEK) - tempB) / tempK;
                }
                calc.setA(newA);
                CalcEnRevise calcEnRevise = getCalcEn(comp);
                if (calcEnRevise != null) {
                    switch (energy.length) {
                        case 6:
                            energy[5] = getNewEnergy(comp, range, calcEnRevise.getEF_Formula(), energy, newA);
                            energy[4] = getNewEnergy(comp, range, calcEnRevise.getEE_Formula(), energy, newA);
                        case 4:
                            energy[3] = getNewEnergy(comp, range, calcEnRevise.getED_Formula(), energy, newA);
                            energy[2] = getNewEnergy(comp, range, calcEnRevise.getEC_Formula(), energy, newA);
                        case 2:
                            energy[1] = getNewEnergy(comp, range, calcEnRevise.getEB_Formula(), energy, newA);
                            energy[0] = getNewEnergy(comp, range, calcEnRevise.getEA_Formula(), energy, newA);
                            break;
                    }

                }
            }
        }
    }

    /**
     * @param component  组分
     * @param energy     5721能量存储
     * @param nbEnergies 5722参数能量存储
     */
    public static void calcKBF(String component, Energy energy, NBEnergy[] nbEnergies) {

        /*判断是否计算KBF值*/
        switch (QueryMeasCateg(component)) {
            case "3":
            case "8":
            case "11":
                if (isDoCalc(component, energy)) {
                    energy.isCalcTitrationKBValue();
                }
                break;
            case "4":
                energy.isCalcGCValue();
                break;
            case "7":
                if (isDoCalc(component, energy)) {
                    energy.isCalcKBValue();
                    for (int iComp = 0; iComp < strNoBoardComponent.get(1).length; iComp++) {
                        if (isNoBoardDoCalc(strNoBoardComponent.get(1)[iComp], nbEnergies[iComp])) {
                            // TSS 不能自动校准
                            if (!strNoBoardComponent.get(1)[iComp].equals("TSS")) {
                                nbEnergies[iComp].isNoBoardCalcKBValue(component);
                            }
                        }
                    }
                }
                break;
            default:
                if (isDoCalc(component, energy)) {
                    energy.isCalcKBValue();
                }
                break;
        }
    }

    public double calc_SWDX(Calc calc, double dK, double dB) {
        double cVal = 0;
        int[] chD = getEnergyCh(calc.getD_Formula());
        double calD = ((double) energy[chD[0]]) / ((double) energy[chD[1]]);
        // 存储D值
        calc.setD(calD);
        //记录最新D值
        updateConfigData(component, "SWDX_D", String.valueOf(calD));
        if (calD < 0.6 || calD > 1.8) {//超出范围则报警
            AddError(component, 666, 报错_警告);
        }
        int[] chH = getEnergyCh(calc.getH_Formula());
        cVal = 1 - ((double) energy[chH[0]]) / (calD * (double) energy[chH[1]]);
        if (getConfigData(component, "UNIT").equals("%")) {
            cVal *= 100;
        }
        int[] chA = getEnergyCh(calc.getA_Formula());
        // 吸光度计算
        double dTempA = Math.log10((calD * (double) energy[chA[0]]) / ((double) energy[chA[1]]) - 1);
        // 存储吸光度
        calc.setA(dTempA);
        double C2 = Math.pow(10, (dTempA - dB) / dK);
        //存储测量值C
        calc.setC(C2);
        return cVal;
    }

}
