package com.yzlm.cyl.cfragment.Communication.NoBoard.AppCalc;

import android.util.Log;

import com.yzlm.cyl.cfragment.Cal.Component.CalcEnRevise;
import com.yzlm.cyl.cfragment.CalcHelper.StrExpression;
import com.yzlm.cyl.cfragment.Communication.Component.AppCalc.Calc;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.Config.NoBoard.CfgTool.NoBoardHistory;
import com.yzlm.cyl.cfragment.Config.NoBoard.CfgTool.NoBoardKbf;
import com.yzlm.cyl.cfragment.R;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import static com.yzlm.cyl.cfragment.Cal.Component.CalcAiRevise.getNewAi;
import static com.yzlm.cyl.cfragment.Cal.Component.CalcAiRevise.isAbsorbScopeSuitable;
import static com.yzlm.cyl.cfragment.Cal.Component.CalcEnRevise.getNewEnergy;
import static com.yzlm.cyl.cfragment.Communication.Component.AppCalc.Energy.getEnergyCh;
import static com.yzlm.cyl.cfragment.Communication.Component.Cmd.CommandParsing.AddNoBoardError;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.NoBoard.CfgTool.NoBoardConfig.getNoBoardConfigData;
import static com.yzlm.cyl.cfragment.Config.NoBoard.CfgTool.NoBoardConfig.updateNoBoardConfigData;
import static com.yzlm.cyl.cfragment.Config.NoBoard.CfgTool.NoBoardDevLog.saveNoBoardOperationLogDataModifyMsg;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.getCalcEn;
import static com.yzlm.cyl.cfragment.Global.getNoBoardCalc;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;
import static com.yzlm.cyl.cfragment.Global.saveRunInfo2File;

/**
 * Created by caoyiliang on 2017/2/24.
 */

public class NBEnergy {
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
    private String range;

    StrExpression strExpression = new StrExpression();

    private NoBoardHistory mHistory;

    public NBEnergy(int Count) {
        temperature = new float[Count];
        energy = new int[Count];
        mHistory = new NoBoardHistory(context);
    }

    private double value;

    public void setFlow(byte flow) {
        this.flow = flow;
    }

    public byte getFlow() {
        return flow;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String getRange() {
        return range;
    }

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

    public void setMonth(byte month) {
        this.month = month;
    }

    public void setDay(byte day) {
        this.day = day;
    }

    public void setHour(byte hour) {
        this.hour = hour;
    }

    public void setMinute(byte minute) {
        this.minute = minute;
    }

    public void setSecond(byte second) {
        this.second = second;
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


    public synchronized void isNoBoardCalcKBValue(String fComponent) {

        boolean blIsCalcFlowStart = false;

        // 启动的时候不是校准流程的情况下，才进行KB 计算
        if (getConfigData(fComponent, "StartFlowName").contains(context.getResources().getString(R.string.JZ))) {
            blIsCalcFlowStart = true;
        }
        double valA = noBoardFormulaCalc_A();
        /* 如果流程动作号为量程1标1,量程2标1,量程3标1的话，记录A值用以计算K值（WL）*/
        /* 如果流程动作号为量程1标2,量程2标2,量程3标2的话，开始计算K值（WL）*/
        NBCalcTable ct = getNoBoardCalc(component);
        NBCalc calc = ct.getCalc(String.valueOf(range));
        switch (flow) {
            case 0x04: {
                calc.setCalib1A(valA);
                updateNoBoardConfigData(component, "CAL_TIME", getTime());
                updateNoBoardConfigData(component, "CAL_Suc_Flag", "true");
                //清校准标1启动标记
                updateNoBoardConfigData(component, "CAL_Start_Flag", "false");
                //清校准标1启动标记
                AddNoBoardError(component, 527, ErrorLog.msgType.运维_信息);
                if (!blIsCalcFlowStart) {// 不是校准启动的情况 刷新KB
                    String strR1Cal2AValue = getNoBoardConfigData(component, "Range1_CAL2_A_Value");
                    // 没有量程1的标2吸光度
                    if (!strR1Cal2AValue.equals("")) {
                        calc.setCalib2A(Double.parseDouble(strR1Cal2AValue));
                        range1CalcKB(calc);
                    }
                }
            }
            break;
            case 0x05: {
                calc.setCalib1A(valA);
                updateNoBoardConfigData(component, "CAL_TIME", getTime());
                updateNoBoardConfigData(component, "CAL_Suc_Flag", "true");
                //清校准标1启动标记
                updateNoBoardConfigData(component, "CAL_Start_Flag", "false");
                AddNoBoardError(component, 527, ErrorLog.msgType.运维_信息);
                if (!blIsCalcFlowStart) {//不是校准启动的情况 刷新KB
                    String strR2Cal2AValue = getNoBoardConfigData(component, "Range2_CAL2_A_Value");
                    // 没有量程2的标2吸光度
                    if (!strR2Cal2AValue.equals("")) {
                        calc.setCalib2A(Double.parseDouble(strR2Cal2AValue));
                        range2CalcKB(calc);
                    }
                }
            }
            break;
            case 0x06: {
                calc.setCalib1A(valA);

                updateNoBoardConfigData(component, "CAL_TIME", getTime());
                updateNoBoardConfigData(component, "CAL_Suc_Flag", "true");
                //清校准标1启动标记
                updateNoBoardConfigData(component, "CAL_Start_Flag", "false");
                AddNoBoardError(component, 527, ErrorLog.msgType.运维_信息);

                if (!blIsCalcFlowStart) {   //不是校准启动的情况 刷新KB
                    String strR3Cal2AValue = getNoBoardConfigData(component, "Range3_CAL2_A_Value");
                    // 没有量程3的标2吸光度
                    if (!strR3Cal2AValue.equals("")) {
                        calc.setCalib2A(Double.parseDouble(strR3Cal2AValue));
                        range2CalcKB(calc);
                    }
                }
            }
            break;
            case 0x07: {
                calc.setCalib2A(valA);
                double Calib1AConc = 0;
                try {
                    AddNoBoardError(component, 528, ErrorLog.msgType.运维_信息);
                    //清校准标2启动标记
                    updateNoBoardConfigData(component, "CAL2_Start_Flag", "false");

                    if (!blIsCalcFlowStart) {   //不是校准启动的情况 刷新KB
                        String strR1Cal1AValue = getNoBoardConfigData(component, "Range1_CAL1_A_Value");
                        if (!strR1Cal1AValue.equals("")) {
                            calc.setCalib1A(Double.parseDouble(strR1Cal1AValue));
                            range1CalcKB(calc);
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
                                range1CalcKB(calc);
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
                    AddNoBoardError(component, 528, ErrorLog.msgType.运维_信息);
                    //清校准标2启动标记
                    updateNoBoardConfigData(component, "CAL2_Start_Flag", "false");
                    if (!blIsCalcFlowStart) {   //不是校准启动的情况 刷新KB
                        String strR2Cal1AValue = getNoBoardConfigData(component, "Range2_CAL1_A_Value");
                        if (!strR2Cal1AValue.equals("")) {
                            calc.setCalib1A(Double.parseDouble(strR2Cal1AValue));
                            range1CalcKB(calc);
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
                                range2CalcKB(calc);
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
                updateNoBoardConfigData(component, "Range3_CAL2_A_Value", String.valueOf(valA));
                double Calib1AConc = 0;
                try {
                    AddNoBoardError(component, 528, ErrorLog.msgType.运维_信息);
                    //清校准标2启动标记
                    updateNoBoardConfigData(component, "CAL2_Start_Flag", "false");

                    if (!blIsCalcFlowStart) {   //不是校准启动的情况 刷新KB
                        String strR3Cal1AValue = getNoBoardConfigData(component, "Range3_CAL1_A_Value");
                        if (!strR3Cal1AValue.equals("")) {
                            calc.setCalib1A(Double.parseDouble(strR3Cal1AValue));
                            range1CalcKB(calc);
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
                                range3CalcKB(calc);
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

    /*量程3 计算 KB**/
    public boolean range3CalcKB(NBCalc calc) {

        String C5 = getNoBoardConfigData(component, "C5");
        String C4 = getNoBoardConfigData(component, "C4");
        double valK = calc_K(calc, C5, C4, calc.getCalib2A(), calc.getCalib1A());
        double valB = calc_B(calc, C4, calc.getCalib1A(), valK);
        double valF = calc_F(calc, calc.getK(String.valueOf(range), component), valK);
        calc.setF(valF);

        if (!updateNoBoardKBVal(calc, Integer.parseInt(range), valK, valB, valF)) {
            //AddNoBoardError(component, 533, ErrorLog.msgType.运行_信息);
            updateNoBoardConfigData(component, "CAL2_Suc_Flag", "false");
            return false;
        } else {
            if (!Double.isNaN(calc.getCalib1A())) {
                updateNoBoardConfigData(component, "Range3_CAL1_A_Value", String.valueOf(calc.getCalib1A()));
            }
            if (!Double.isNaN(calc.getCalib2A())) {
                updateNoBoardConfigData(component, "Range3_CAL2_A_Value", String.valueOf(calc.getCalib2A()));
            }
            saveNoBoardKBF("3", valK, valB, calc.getCalF());
            // 校准成功清外部KB
            extNoBoardKBParReset(component, "3");

            updateNoBoardConfigData(component, "CAL2_SUC_TIME", getTime());
            updateNoBoardConfigData(component, "CAL_SUC_TIME", getNoBoardConfigData(component, "CAL_TIME"));
            updateNoBoardConfigData(component, "CAL2_Suc_Flag", "true");
            return true;
        }
    }

    /*量程2 计算KB*/
    public boolean range2CalcKB(NBCalc calc) {

        String C3 = getNoBoardConfigData(component, "C3");
        String C2 = getNoBoardConfigData(component, "C2");
        double valK = calc_K(calc, C3, C2, calc.getCalib2A(), calc.getCalib1A());
        double valB = calc_B(calc, C2, calc.getCalib1A(), valK);
        double valF = calc_F(calc, calc.getK(String.valueOf(range), component), valK);
        calc.setF(valF);

        if (!updateNoBoardKBVal(calc, Integer.parseInt(range), valK, valB, valF)) {
            //AddNoBoardError(component, 533, ErrorLog.msgType.运行_信息);
            updateNoBoardConfigData(component, "CAL2_Suc_Flag", "false");
            return false;
        } else {
            saveNoBoardKBF("2", valK, valB, calc.getCalF());
            if (!Double.isNaN(calc.getCalib1A())) {
                updateNoBoardConfigData(component, "Range2_CAL1_A_Value", String.valueOf(calc.getCalib1A()));
            }
            if (!Double.isNaN(calc.getCalib2A())) {
                updateNoBoardConfigData(component, "Range2_CAL2_A_Value", String.valueOf(calc.getCalib2A()));
            }
            // 校准成功清外部KB
            extNoBoardKBParReset(component, "2");
            updateNoBoardConfigData(component, "CAL2_SUC_TIME", getTime());
            updateNoBoardConfigData(component, "CAL_SUC_TIME", getNoBoardConfigData(component, "CAL_TIME"));
            updateNoBoardConfigData(component, "CAL2_Suc_Flag", "true");
            return true;
        }
    }

    /*量程1KB计算*/
    public boolean range1CalcKB(NBCalc calc) {

        String C1 = getNoBoardConfigData(component, "C1");
        String C0 = getNoBoardConfigData(component, "C0");
        double valK = calc_K(calc, C1, C0, calc.getCalib2A(), calc.getCalib1A());
        double valB = calc_B(calc, C0, calc.getCalib1A(), valK);
        double valF = calc_F(calc, calc.getK(String.valueOf(range), component), valK);

        calc.setF(valF);
        //如果是量程1强制校准则直接刷新量程23kb
        if (!updateNoBoardKBVal(calc, Integer.parseInt(range), valK, valB, valF)) {
            // AddNoBoardError(component, 533, ErrorLog.msgType.运行_信息);
            updateNoBoardConfigData(component, "CAL2_Suc_Flag", "false");
            return false;
        } else {
            if (!Double.isNaN(calc.getCalib1A())) {
                updateNoBoardConfigData(component, "Range1_CAL1_A_Value", String.valueOf(calc.getCalib1A()));
            }
            if (!Double.isNaN(calc.getCalib2A())) {
                updateNoBoardConfigData(component, "Range1_CAL2_A_Value", String.valueOf(calc.getCalib2A()));
            }
            saveNoBoardKBF("1", valK, valB, calc.getCalF());
            // 校准成功清外部KB
            extNoBoardKBParReset(component, "1");
            updateNoBoardConfigData(component, "CAL2_SUC_TIME", getTime());
            updateNoBoardConfigData(component, "CAL_SUC_TIME", getNoBoardConfigData(component, "CAL_TIME"));
            updateNoBoardConfigData(component, "CAL2_Suc_Flag", "true");
            return true;
        }

    }


    /*
     *  compName: 组份
     *  s: 量程(1,2,3)
     * **/
    private static void extNoBoardKBParReset(String compName, String s) {
        // 校准成功清外部KB
        saveNoBoardOperationLogDataModifyMsg(compName, "ExtRange" + s + "K", "1", "ExtK" + s, ErrorLog.msgType.操作_信息);
        updateNoBoardConfigData(compName, "ExtRange" + s + "K", "1");
        saveNoBoardOperationLogDataModifyMsg(compName, "ExtRange" + s + "B", "0", "ExtB" + s, ErrorLog.msgType.操作_信息);
        updateNoBoardConfigData(compName, "ExtRange" + s + "B", "0");
    }

    /*保存KBF值至数据库*/
    private void saveNoBoardKBF(String range, double valK, double valB, double valF) {
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
     * 获取无背板组分 吸光度计算公式
     *
     * @param component
     * @param iRange
     * @return
     */
    public static String getNoBoardCalcAFormula(String component, int iRange) {

        NBCalcTable ct = getNoBoardCalc(component);
        if (ct != null) {
            String range = String.valueOf(iRange);
            Calc calc = ct.getCalc(range);
            if (calc != null) {
                return calc.getA_Formula();
            }
        }
        return "";
    }


    /*
     * 除 TP   TN  TPb之外
     * 识别 E01 E02 顺序  识别算法第一个符号位  + -
     * 吸光度算法只用到2个光强的时候，识别了两个光强之间的运算符号（* / + -）
     */
    public double noBoardFormulaCalc_A() {
        double valA = 0;
        int sign = 1;
        String strSignEn = "/";
        String a_formula = getNoBoardCalcAFormula(component, Integer.parseInt(range));
        switch (component) {
            case "TP":
            case "CODcr":
            case "CODuv":
                if (energy.length > 3) {// 4组能量计算 扣浊度

                    double kz1 = 0, kz2 = 0, kz3 = 1;
                    try {
                        kz1 = Double.parseDouble(getNoBoardConfigData(component, "KZ1Range" + range));
                        kz2 = Double.parseDouble(getNoBoardConfigData(component, "KZ2Range" + range));
                        kz3 = Double.parseDouble(getNoBoardConfigData(component, "KZ3Range" + range));
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
            case "TPb":
                if (energy.length > 3) {
                    // 补偿K
                    double kc = Double.parseDouble(getNoBoardConfigData(component, "gpbck"));
                    // 补偿B
                    double bc = Double.parseDouble(getNoBoardConfigData(component, "gpbcb"));

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
            default:
                if (a_formula.contains("lg")) {
                    char f = a_formula.charAt(0);
                    if (f == '-') {
                        sign = -1;
                    }
                    if (energy.length > 3) {
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
     判断是否需要修正
    * **/
    private static boolean isNoBoardAbsorbRevise(String Component, String flName, int range, double nowAi, double rangeK) {
        try {
            CalcEnRevise calcEnRevise = getCalcEn(Component);
            if (calcEnRevise.getParX() == 0 && calcEnRevise.getRangeY(range) == 0) {
                saveRunInfo2File("组分[" + Component + "] X Y 为默认值" + calcEnRevise.getParX() + calcEnRevise.getRangeY(range));
                return false;
            }
            NoBoardHistory history = new NoBoardHistory(context);
            /*历史数据是否有测量值***/
            List<Map> hisMap = history.select(Component, flName, null, null, 0, 1);
            if (hisMap.size() > 0) {
                return isAbsorbScopeSuitable(Double.parseDouble(hisMap.get(0).get("A").toString()), nowAi, calcEnRevise.getRangeY(range), rangeK);
            } else {
                saveRunInfo2File("组分[" + Component + "] 没有数据,吸光度2算法不执行");
                return false;
            }
        } catch (Exception e) {

        }
        return false;
    }

    /*
     * 吸光度及能量修正
     * 组份 ，流程名称 ，量程，当前吸光度，当前量程K，原始能量
     * **/
    private static double absorbEnRevise(String component, String flName, int range, double nowA, double rangeK, int[] energy) {

        double newA = nowA;
        /*判断是否需要修正*/
        if (isNoBoardAbsorbRevise(component, flName, range, nowA, rangeK)) {
            NoBoardHistory history = new NoBoardHistory(context);
            /*历史数据是否有测量值***/
            List<Map> hisMap = history.select(component, flName, null, null, 0, 1);
            if (hisMap.size() > 0) {

                CalcEnRevise calcEnRevise = getCalcEn(component);
                newA = getNewAi(Double.parseDouble(hisMap.get(0).get("A").toString()), nowA, calcEnRevise.getParX());
                if (component.equals("TP")) {
                    if ((!calcEnRevise.getEA_Formula().contains("KZ1") && energy.length == 4)
                            || (energy.length == 2 && calcEnRevise.getEA_Formula().contains("KZ1"))) {
                        saveExceptInfo2File("TP 能量为4 EA 算法异常 不匹配，未包含KZ1");
                        AddNoBoardError(component, 579, ErrorLog.msgType.报错_警告);
                        return newA;
                    }
                }
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
        return newA;
    }

    /* 计算C值（WL注释）*/
    public double noBoardCalc_C(String fcName) {
        double valC = 0;
        NBCalcTable ct = getNoBoardCalc(component);
        if (ct != null) {

            NBCalc calc = ct.getCalc(range);
            if (calc != null) {
                double tempK = calc.getK(range, component);
                double tempB = calc.getB(range, component);
                double tempA = noBoardFormulaCalc_A();

                if (fcName.equals(context.getResources().getString(R.string.ZY)) || fcName.equals(context.getResources().getString(R.string.JBHS))) {
                    //      tempA = absorbEnRevise(component, fcName, iRange, tempA, tempK, energy);
                }
                if (fcName.equals(context.getResources().getString(R.string.LDHC))) {
                    // 零点核查都使用量程1KB进行计算
                    tempK = calc.getK("1", component);
                    tempB = calc.getB("1", component);
                }
                valC = tempK * tempA + tempB;

                calc.setA(tempA);
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

    /* 计算K值（WL）*/
    private double calc_K(NBCalc calc, String C_1, String C_2, double A_1, double A_2) {
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
    private double calc_B(NBCalc calc, String C, double A, double K) {
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
    private double calc_F(NBCalc calc, double Kold, double Knew) {
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

    /* 进行K、B值的更新（WL）*/
    private boolean updateNoBoardKBVal(NBCalc calc, int iRange, double valK, double valB, double valF) {
        /* *//* 若前一个K值不为1，则计算偏移率F值，若F值在[0.5,2.0]之间则更新K、B值，否则不更新*//*
        if (calc.getK(String.valueOf(iRange), component) != 1) {
            String fMin = "0.5";
            String fMax = "2.0";
            try {
                fMin = getNoBoardConfigData(component, "FMin").equals("") ? "0.5" : getNoBoardConfigData(component, "FMin");
                fMax = getNoBoardConfigData(component, "FMax").equals("") ? "2.0" : getNoBoardConfigData(component, "FMax");
            } catch (Exception e) {

            }
            if ((valF < Double.parseDouble(fMin) || valF > Double.parseDouble(fMax)) && (getNoBoardConfigData(component, "ycjzFlag").equals("false"))) {
                saveRunInfo2File("组分[" + component + "]" + "校准失败]" + "PF = " + valF + "FMin=" + fMin + "FMax=" + fMax);
                return false;
            } else {
                calc.setK(valK);
                calc.setB(valB);
                if ((getNoBoardConfigData(component, "ycjzFlag").equals("true"))) {
                    calc.setF(1.0);
                    saveRunInfo2File("组分[" + component + "]" + "强制刷新KB!");
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        updateNoBoardConfigData(component, "ycjzFlag", "false");

                    }
                }).start();
            }
        } else */
        {
            /* 若前一个K值为1，则直接更新K、B值*/
            calc.setK(valK);
            calc.setB(valB);
            calc.setF(1.0);
        }
        return true;
    }


}
