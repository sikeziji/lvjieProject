package com.yzlm.cyl.cfragment.Config.Component.CfgTool;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.yzlm.cyl.cfragment.AppFunction.AlarmLimit;
import com.yzlm.cyl.cfragment.AppFunction.RangeAutoChange;
import com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.DataStruct;
import com.yzlm.cyl.cfragment.Communication.Component.AppCalc.Calc;
import com.yzlm.cyl.cfragment.Communication.Component.AppCalc.CalcTable;
import com.yzlm.cyl.cfragment.Communication.Component.AppCalc.Energy;
import com.yzlm.cyl.cfragment.Communication.Component.AppFlow.FlowClass;
import com.yzlm.cyl.cfragment.Communication.Component.AppFlow.FlowTable;
import com.yzlm.cyl.cfragment.Communication.NoBoard.AppCalc.NBEnergy;
import com.yzlm.cyl.cfragment.Communication.Thread.SendManager;
import com.yzlm.cyl.cfragment.Config.MyDBHelper.CompoentHelper.DataDBHelper;
import com.yzlm.cyl.cfragment.Global;
import com.yzlm.cyl.cfragment.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import static com.yzlm.cyl.cfragment.Cal.Component.CalcReagentVolume.CalReagentVolume;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.Modbus.RTU2018.ModbusRtu2018.getTimeHaveSec;
import static com.yzlm.cyl.cfragment.Communication.Component.AppCalc.Energy.calcKBF;
import static com.yzlm.cyl.cfragment.Communication.Component.AppCalc.Energy.isEnergyCalCfgError;
import static com.yzlm.cyl.cfragment.Communication.Component.Cmd.CommandParsing.AddError;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.updateConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogMsg;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog.msgType.报错_警告;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Kbf.getNewestKBF;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Kbf.saveKBF;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.getPublicConfigData;
import static com.yzlm.cyl.cfragment.Content.Component.ContentTool.ConvertUnit;
import static com.yzlm.cyl.cfragment.Content.Component.List2.List2_Content3.setUseRange;
import static com.yzlm.cyl.cfragment.Content.Component.List2.List2_Content6.reagentResidueCalculation;
import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.QueryMeasBoardType;
import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.QueryMeasCateg;
import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.getModuleName;
import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.isHaveMeasCategory;
import static com.yzlm.cyl.cfragment.Global.S0;
import static com.yzlm.cyl.cfragment.Global.S1;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.doFlowing;
import static com.yzlm.cyl.cfragment.Global.doubleRound;
import static com.yzlm.cyl.cfragment.Global.getCalc;
import static com.yzlm.cyl.cfragment.Global.getCmds;
import static com.yzlm.cyl.cfragment.Global.historyCalcFlag;
import static com.yzlm.cyl.cfragment.Global.lComponentVoc;
import static com.yzlm.cyl.cfragment.Global.manualStopFlag;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;
import static com.yzlm.cyl.cfragment.Global.saveRunInfo2File;
import static com.yzlm.cyl.clibrary.Util.DataUtil.copy;
import static com.yzlm.cyl.clibrary.Util.DataUtil.toByteArray;
import static java.lang.Thread.sleep;

/**
 * Created by caoyiliang on 2016/11/28.
 */

@SuppressWarnings("unchecked")
public class History {
    private DataDBHelper historyDBHelper;
    final int pointNum = 6;

    public History(Context context) {
        historyDBHelper = DataDBHelper.getInstance(context);
    }

    public synchronized void Add(Energy energy, NBEnergy[] nbEnergies) {

        DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
        df.setMaximumFractionDigits(pointNum);
        String Energy = "";
        String comp = energy.getComponent();
        FlowTable ft = Global.getFlows(comp);
        FlowClass fc = ft.getFlowByCmd(energy.getFlow());
        String fcName = fc.getName();
        double cVal = 0;

        try {
            //检查能量个数及算法匹配
            if (!QueryMeasCateg(comp).equals("7")) {
                if (isEnergyCalCfgError(comp, energy.getFlow(), energy.energy)) {
                    return;
                }
            }
            // 原始光强计算吸光度 及测量值
            cVal = energy.calcMeaValue(comp, fcName, energy);

            /*做样 加标回收 量程判断*/
            if (AutoRangeChangeSelect(fcName, comp, cVal) == 1) {
                saveRunInfo2File("切换量程不存储数据！");
                return;
            }
            // 界面显示吸光度；
            if (getPublicConfigData("AIsShow").equals("true")) {
                manualStopFlag.put(comp, "false");
                Calc calc = getCalcA(fc, comp);
                //停止再实时获取数据
                updateConfigData(comp, "readHistoryDataFlag", "false");
                // 刷新显示A 值
                updateConfigData(comp, "nowHistoryDataAEn", String.valueOf(calc.getA()));

                // 等待3分钟进行界面显示吸光度，并且接收是否有手动紧急停止信号
                // 由于又同步锁，此延时回导致其它组分出数时间被延迟；故此功能只有单表可使用；
                for (int i = 0; i < 3 * 60; i++) {
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (!doFlowing.get(comp).equals(fcName)) {
                        saveRunInfo2File("组分[" + comp + "] 流程：" + doFlowing.get(comp) + "\r\n");
                        break;
                    }
                    if (manualStopFlag.get(comp).equals("true")) {
                        updateConfigData(comp, "readHistoryDataFlag", "true");
                        updateConfigData(comp, "nowHistoryDataAEn", "");
                        saveRunInfo2File("组分[" + comp + "] 紧急停止-清除数据!" + "\r\n");
                        return;
                    }
                }
                updateConfigData(comp, "nowHistoryDataAEn", "");
            }

            // 防止重复存储KBF
            {
                if (historyCalcFlag.get(comp).equals(energy.getTime())) {
                    return;
                }
                historyCalcFlag.put(comp, energy.getTime());
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                boolean timeFlag = false;
                // 数据时间判断
                String lastDataTime = getLastDataTime(comp);
                if (lastDataTime != null) {
                    timeFlag = lastDataTime.equals(energy.getTime());
                }
                if (timeFlag) {
                    return;
                }
            }

            // 运行日志存储
            if (fcName.equals(context.getString(R.string.B1))) {
                AddError(comp, 647, String.valueOf(doubleRound(getCalcA(fc, comp).getA(), 3)), ErrorLog.msgType.运维_信息);
            }
            if (fcName.equals(context.getString(R.string.B2))) {
                AddError(comp, 648, String.valueOf(doubleRound(getCalcA(fc, comp).getA(), 3)), ErrorLog.msgType.运维_信息);
            }

            // 计算KBF
            calcKBF(comp, energy, nbEnergies);

            //计算后再将能量存储
            if (QueryMeasCateg(comp).equals("4")) {
                int index = 0;
                for (float item : energy.fEnergy) {
                    if (Objects.requireNonNull(lComponentVoc.get(comp)).length > 0) {
                        Energy += (Objects.requireNonNull(lComponentVoc.get(comp))[index++] + "=" + item + " mg/L" + ";");
                    }
                }
            } else if (QueryMeasCateg(comp).equals("3") || QueryMeasCateg(comp).equals("8")
                    || QueryMeasCateg(comp).equals("11")) {
                for (float item : energy.fEnergy) {
                    Energy += item + ";";
                }
            } else {
                for (int i = 0; i < energy.energy.length; i++) {
                    //光强有0  报错 ：能量异常，修正能量
                    if (energy.energy[i] == 0) {
                        //修正能量
                        energy.energy[i] = 1;
                        int error = 536;
                        if (QueryMeasCateg(comp).equals("2") || QueryMeasCateg(comp).equals("7")
                                || QueryMeasCateg(comp).equals("12")) {
                            error = 537;
                        }
                        AddError(comp, error, 报错_警告);
                        saveExceptInfo2File("组分[" + comp + "]" + fcName + "光强能量为0,修正为1");
                    }
                    Energy += energy.energy[i] + ";";
                }
            }
            /* 17、超量程告警,限制出值*/
            cVal = oveRangeSetting(fcName, comp, Integer.parseInt(energy.getRange()), cVal);

            /*将历史数据进行解析处理*/
            DataProcess(energy, fc, fcName, Energy, cVal);
            if (getPublicConfigData("AIsShow").equals("true")) {
                //下次出数后可以读取一次数值
                updateConfigData(comp, "readHistoryDataFlag", "true");
                updateConfigData(comp, "nowHistoryDataAEn", "");
            }
        } catch (Exception e) {
            saveExceptInfo2File("组分[" + comp + "] 计算异常" + e.toString());
        }
    }


    //稀释校准
    public static double dilutionCal(Energy energy, String comp, double cVal) {
        try {
            AddError(comp, 575, ErrorLog.msgType.运维_信息);
            if (isHaveMeasCategory(comp, "5")) {
                String C2;
                C2 = getConfigData(comp, "C" + (Integer.parseInt("3") * 2 - 1));

                double a = energy.calcAValue(comp);
                double k = getNewestKBF(comp, "3", "K");
                double b = getNewestKBF(comp, "3", "B");
                // 稀释稀释 D
                double d = Double.parseDouble(C2) / (k * a + b);
                cVal = Double.parseDouble(C2);
                // 超范围
                if (d < 0 || d > 1000) {
                    saveRunInfo2File(comp + "稀释稀释超范围( 0 - 1000 ) " + d);
                    AddError(comp, 576, ErrorLog.msgType.运行_信息);
                } else {
                    String timeStr = energy.getTime();
                    timeStr = timeStr.replace(" ", "-");
                    String[] time = timeStr.split("[:-]");
                    saveKBF(comp + getModuleName("5"), Short.parseShort(time[0]), Byte.valueOf(time[1]), Byte.valueOf(time[2]), Byte.valueOf(time[3]), Byte.valueOf(time[4]), Byte.valueOf(time[5]), getConfigData(energy.getComponent() + "xs", "XS_MODE"), d, 0, 1);
                }
            }
        } catch (Exception e) {
            saveRunInfo2File("组分[" + comp + getModuleName("5") + "] 计算稀释D异常");
        }
        return cVal;
    }

    /*将历史数据进行解析处理*/
    private void DataProcess(Energy energy, FlowClass fc, String fcName, String Energy, double cVal) {
        boolean timeFlag = false;
        String comp = energy.getComponent();
        // 更新测量值
        energy.setValue(cVal);
        // 获取 计算值
        Calc calc = getCalcA(fc, comp);

        // 数据时间判断
        String lastDataTime = getLastDataTime(comp);
        if (lastDataTime != null) {
            timeFlag = lastDataTime.equals(energy.getTime());
        }
        {
            if (!timeFlag) {
                /*出数存储数据库**/
                insertData(energy, Energy, cVal, calc.getA(), calc.getC(), null);
                /*余量刷新**/
                reagentResidueCalculation(comp, fcName, Integer.parseInt(energy.getRange()));

                /*计算并保存高锰酸钾加入量**/
                if (QueryMeasCateg(comp).equals("1") && comp.equals("CODcr")) {
                    updateConfigData(comp, "fyzs_M", String.format("%.2f", CalReagentVolume(comp)));
                }

                //核查判定
                CheckDetermination(comp, fcName, cVal);

                // 刷新一键校准X
                oneKeyCalRangeGetX(energy, fc.getName(), comp);

                //测量值报警
                alarmLimitDeal(cVal, comp);

                // 打印数据
                measurementOfPrinting(cVal, comp, energy);

                // 状态刷新
                afterMeaValueDeal(comp, fcName);

                //刷新标1/标2时间
                RefreshCALHisTime(comp, fcName, energy.getRange(), energy.getTime());

                // 高指 做样数据进行判断是否启动一次冲洗
                if (QueryMeasCateg(comp).equals("3") || QueryMeasCateg(comp).equals("8")) {
                    if (getConfigData(comp, "ZDCX").equals("true") && getConfigData(comp, "autoWashDoOnceFlag").equals("false")) {
                        if (fc.getName().equals(context.getResources().getString(R.string.ZY))) {
                            try {
                                double dLimit = Double.parseDouble(getConfigData(comp, "autoWashLimitValue"));
                                if (cVal > dLimit) {
                                    updateConfigData(comp, "autoWashDoOnceFlag", "true");
                                }
                            } catch (Exception e) {
                                saveExceptInfo2File("组分[" + comp + "]" + fcName + "超测量指标值启动冲洗判断失败!");
                            }
                        }
                    }
                }
            }
        }

    }

    private void afterMeaValueDeal(String comp, String fcName) {
        if (fcName.equals(context.getResources().getString(R.string.ZY))) {
            // 清除强制强制校准状态
            if (getConfigData(comp, "ycjzFlag").equals("true")) {
                updateConfigData(comp, "ycjzFlag", "false");
            }
            // 清除强制刷新KB状态
            if ((getConfigData(comp, "OneKeyCalForceKBFlag").equals("true"))) {
                updateConfigData(comp, "OneKeyCalForceKBFlag", "false");
            }
        }
    }

    private Calc getCalcA(FlowClass fc, String comp) {
        CalcTable ct = getCalc(comp);
        return ct.getCalc(String.valueOf(fc.getRange()));
    }

    /**
     * 校准体积V1 W200 CODcr
     *
     * @param fcName 流程名称
     * @param comp   组分
     * @param aVal   吸光度
     */
    public static void settingCalVolume(String fcName, String comp, double aVal) {
        int sendV1 = 0;
        if (fcName.equals(context.getResources().getString(R.string.ZY))) {
            /*校准体积V1重新计算**/
            if (QueryMeasBoardType(comp).equals("2") && comp.equals("CODcr")) {
                if (getConfigData(comp, "Volume_Cal").equals("true")) {
                    List<Map> list = getLastHistoryFlowData(comp, context.getString(R.string.B1));
                    List<Map> list2 = getLastHistoryFlowData(comp, context.getString(R.string.B2));
                    Double dA1 = Double.parseDouble(list.get(0).get("A").toString());
                    Double dA2 = Double.parseDouble(list2.get(0).get("A").toString());
                    Double dAZY = aVal;
                    //校准体积V1在进入界面时就已经读取，所以点击体积校准后到此处重新计算，会有该值
                    int V1 = Integer.parseInt(getCmds(comp).getCmd(6200).getValue().toString());
                    if ((dA2 > dA1) && (dAZY > dA1)) {
                        //如果V1为负数，sendV1将等于0
                        sendV1 = (int) (V1 * (dA2 - dA1) / (dAZY - dA1));
                    }
                    /*校准体积V1重新计算**/
                    if (sendV1 != 0) {
                        /*默认只重新计算并下发 2_ 的计量板校准体积V1**/
                        int index = 2;
                        byte[] dataByte = toByteArray(sendV1, 4);
                        saveOperationLogMsg(comp, "设置计量板" + index + "计量参数C校准体积V1", ErrorLog.msgType.操作_信息);
                        SendManager.SendCmd(comp + "_计量板" + index + "计量参数C设置_06_" + (108 + (index - 1)), S0, 3, 200, dataByte);
                    }
                }
            }
            //清除状态
            updateConfigData(comp, "Volume_Cal", "false");
        }
    }

    /**
     * 将历史数据进行存储
     *
     * @param energy
     * @param Energy
     * @param cVal
     */
    public void insertData(Energy energy, String Energy, double cVal, double aVal, double c2Val, String time) {
        final Map<String, Object> map = new HashMap<>();
        String comp = energy.getComponent();
        FlowTable ft = Global.getFlows(comp);
        FlowClass fc = ft.getFlowByCmd(energy.getFlow());
        String fcName = fc.getName();
        String Temperature = "";
        for (float item : energy.temperature) {
            Temperature += item + ";";
        }
        if (time == null) {
            map.put("time", energy.getTime());
        } else {
            map.put("time", time);
        }
        map.put("component", comp);
        map.put("flow", fcName);
        map.put("temperature", Temperature);
        map.put("energy", Energy);
        map.put("C", String.format("%." + pointNum + "f ", cVal));
        map.put("unit", energy.getUnit());
        map.put("A", aVal);
        String strMeaTag = getMeaTagStatus(cVal, comp);
        map.put("tag", strMeaTag);
        map.put("C2", String.format("%." + pointNum + "f ", c2Val));
        historyInsertData(comp, map);
    }


    /**
     * @param comp 组分
     * @param map  数据map
     * @return
     */
    public synchronized static boolean historyInsertData(String comp, Map<String, Object> map) {
        boolean timeFlag = false;
        DataDBHelper historyDBHelper;
        historyDBHelper = DataDBHelper.getInstance(context);
        String strSetTime = (String) map.get("time");
        String lastDataTime = getLastDataTime(comp);
        if (lastDataTime != null) {
            timeFlag = lastDataTime.equals(strSetTime);
        }
        if (!timeFlag) {
            // 数据时间是否小于系统当前时间
            String strNowTime = getTimeHaveSec();
            if (strSetTime.compareTo(strNowTime) < 0) {
                historyDBHelper.insert("History", map);
                return true;
            } else {
                saveRunInfo2File("组分[" + comp + "] 数据时间:" + strSetTime + "系统时间:" + strNowTime);
            }
        }
        return false;
    }

    /**
     * 根据id  修改数据
     *
     * @param id           id 号
     * @param columns      数据库 columns 名称
     * @param columnsValue 数值
     * @return
     */
    public static boolean updateMeaHistoryData(long id, String[] columns, String[] columnsValue) {

        History history = new History(context);
        List<Map> list = history.getDataById(String.valueOf(id));
        if (list.size() > 0) {
            history.updateDatById(columns, columnsValue, id);
            return true;
        } else {
            return false;
        }
    }


    /*
     *
     * 记录数据标识
     * ***/
    @NonNull
    private static String getMeaTagStatus(double cVal, String comp) {
        String strMeaTag = "";
        try {
            if (getConfigData(comp, "doJobRunningMode").equals("3")) {
                // 维护
                strMeaTag = strMeaTag.equals("") ? "M" : strMeaTag + "-M";
            }
            if ((cVal > Float.parseFloat(getConfigData(comp, "YBLCH")))) {
                //超仪表上限
                strMeaTag = strMeaTag.equals("") ? "T" : strMeaTag + "-T";
            }
            if ((cVal < Float.parseFloat(getConfigData(comp, "YBLCL")))) {
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

    /**
     * @param cVal 测量值
     * @param comp 组分
     */
    private void alarmLimitDeal(double cVal, String comp) {
        try {
            try {
                /*报警上下限刷新*/
                double al = Double.parseDouble(getConfigData(comp, "ALARM_L"));
                double ah = Double.parseDouble(getConfigData(comp, "ALARM_H"));
                new AlarmLimit(comp, cVal, al, ah);
            } catch (Exception e) {
                saveExceptInfo2File("组分[" + comp + "] 报警上下限参数异常");
            }
        } catch (Exception e) {
            saveExceptInfo2File("组分[" + comp + "]" + "计算后处理异常!");
        }
    }

    /*
     * 数据值进行检出限判定*/
    public static double getcValDownLimit(double cVal, String comp, String flowName, Energy eEnergy) {
        if (flowName.equals(context.getResources().getString(R.string.GYWDX)) || flowName.equals(context.getResources().getString(R.string.JYWDX))) {
            //光源稳定性和进样稳定性不做修正
            return cVal;
        }
        /*VOC 没有下限修正*/
        if (!QueryMeasCateg(comp).equals("4")) {
            switch (getConfigData(comp, "MeaDataLimitMode")) {
                case "0":
                    // 走防出零
                    cVal = CValDownLimit(comp, cVal);
                    break;
                case "1":// 负值显示
                    break;
                case "2":
                    /*定量下限*/
                    if (flowName.equals(context.getResources().getString(R.string.ZY)) || flowName.equals(context.getResources().getString(R.string.JBHS))) {
                        cVal = quantitativeLimit(comp, cVal);
                    } else {
                        /*下限*/
                        cVal = CValDownLimit(comp, cVal);
                    }
                    break;
            }
        }
        return cVal;
    }

    /**
     * 葡萄糖测试数据修正
     *
     * @param cVal    测量值
     * @param comp    组分
     * @param eEnergy 数据能量
     * @return 测量值
     */
    public static double getGlucoseTestRevisecVal(double cVal, String comp, Energy eEnergy) {
        if (QueryMeasCateg(comp).equals("8") && comp.equals("CODmn")) {
            if (getConfigData(comp, "GlucoseSwitch1").equals("open")) {
                if (!getConfigData(comp, "GlucoseA1").equals("") && !getConfigData(comp, "GlucoseB1").equals("")) {
                    short year = Short.parseShort(((getCmds(comp).getCmd(621).getValue() == null ? "0" : getCmds(comp).getCmd(621).getValue()).toString()));
                    byte month = Byte.parseByte(((getCmds(comp).getCmd(622).getValue() == null ? "0" : getCmds(comp).getCmd(622).getValue()).toString()));
                    byte day = Byte.parseByte(((getCmds(comp).getCmd(623).getValue() == null ? "0" : getCmds(comp).getCmd(623).getValue()).toString()));
                    byte hour = Byte.parseByte(((getCmds(comp).getCmd(624).getValue() == null ? "0" : getCmds(comp).getCmd(624).getValue()).toString()));
                    byte minute = Byte.parseByte(((getCmds(comp).getCmd(625).getValue() == null ? "0" : getCmds(comp).getCmd(625).getValue()).toString()));
                    if (eEnergy.getYear() == year && eEnergy.getMonth() == month
                            && eEnergy.getDay() == day && eEnergy.getHour() == hour
                            && ((eEnergy.getMinute() - minute) >= -1 && (eEnergy.getMinute() - minute) <= 1)) {
                        int dEleEnergy = Integer.parseInt(((getCmds(comp).getCmd(620).getValue() == null ? "0" : getCmds(comp).getCmd(620).getValue()).toString()));
                        if (dEleEnergy > Float.parseFloat(getConfigData(comp, "GlucoseA1")) && cVal > Double.parseDouble(getConfigData(comp, "GlucoseB1"))) {
                            cVal = cVal + Double.parseDouble(getConfigData(comp, "GlucoseCorrect1"));
                        }
                    }
                }
            }
            if (getConfigData(comp, "GlucoseSwitch2").equals("open")) {
                if (!getConfigData(comp, "GlucoseA2").equals("") && !getConfigData(comp, "GlucoseB2").equals("")) {
                    if (cVal > Double.parseDouble(getConfigData(comp, "GlucoseA2")) && cVal < Double.parseDouble(getConfigData(comp, "GlucoseB2"))) {
                        cVal = cVal + Double.parseDouble(getConfigData(comp, "GlucoseCorrect2"));
                    }
                }
            }
        }
        return cVal;
    }


    /*测量完成之后出数打印*/
    private void measurementOfPrinting(double cVal, String comp, Energy energy) {
        try {
            if (getConfigData(comp, "PRINTER").equals("true")) {
                String strData;
                /*
                 * 由于打印机不支持打印 μg/L ，故默认为mg/L
                 * */
                if (getConfigData(comp, "UNIT").equals("μg/L")) {
                    strData = "\r\n" + comp + "=" + String.format("%." + Integer.parseInt(getConfigData(comp, "YXWS")) + "f ", cVal) + "" + "mg/L" + "\r\n" + energy.getTime() + "\r\n";
                } else {
                    String afterValue = ConvertUnit("mg/L", getConfigData(comp, "UNIT"), cVal, Integer.parseInt(getConfigData(comp, "YXWS")));
                    strData = "\r\n" + comp + "=" + afterValue + "" + getConfigData(comp, "UNIT") + "\r\n" + energy.getTime() + "\r\n";
                }
                SendManager.SendCmd("IO" + "_" + "打印输出_09_10", S1, 1, 200, strData.getBytes());
            }
        } catch (Exception e) {
            saveExceptInfo2File("组分[" + comp + "] 测量完成后出数打印异常");
        }
    }

    /**
     * 是否需要计算
     *
     * @param fcName 流程名称
     * @return 是否是测量计算流程
     */
    private boolean isMeaFlowName(String fcName) {
        return (fcName.equals(context.getResources().getString(R.string.ZY)) || fcName.equals(context.getResources().getString(R.string.LYCL))
                || fcName.equals(context.getResources().getString(R.string.BYCL)) || fcName.equals(context.getResources().getString(R.string.BY2CL))
                || (fcName.equals(context.getResources().getString(R.string.LDHC)) || fcName.equals(context.getResources().getString(R.string.KDHC))
                || fcName.equals(context.getResources().getString(R.string.BYHC)) || fcName.equals(context.getResources().getString(R.string.PXY)))
                || fcName.equals(context.getResources().getString(R.string.JBHS)));
    }

    /*一键校准获取X**/
    private void oneKeyCalRangeGetX(Energy energy, String fcName, String comp) {
        try {
            // 一键校准标2测量刷新X
            if (fcName.equals(context.getResources().getString(R.string.BY2CL)) && energy.getRange().equals("2")) {
                if (getConfigData(comp, "OneKeyCalStartFlag").equals("true")) {
                    updateConfigData(comp, "OneKeyCalStartFlag", "false");
                    if (!getConfigData(comp, "OneKeyCalB").equals("")) {
                        // 量程1kb
                        CalcTable ct = getCalc(comp);
                        Calc calc = ct.getCalc("1");
                        double k1 = calc.getK("1", comp);
                        double b1 = calc.getB("1", comp);
                        // 测试液浓度
                        double calB = Double.parseDouble(getConfigData(comp, "OneKeyCalB"));
                        double x = 0;
                        // x 算法
                        switch (getConfigData(comp, "OneKeyCalXRSA")) {
                            case "1":
                                x = (calB / (energy.FormulaCalc_A() * k1 + b1));
                                break;
                            case "2":
                                x = (calB / (energy.FormulaCalc_A() * k1 + b1)) / 2;
                                break;
                        }
                        saveRunInfo2File("组分[" + comp + "] Ab:" + energy.FormulaCalc_A() + "k1:" + k1 + "b1:" + b1 + "x:" + x);
                        String strForceXFlag = getConfigData(comp, "OneKeyCalForceXFlag");
                        String strX = getConfigData(comp, "OneKeyCalX");
                        if (strX.equals("") || strForceXFlag.equals("") || strForceXFlag.equals("true")) {
                            updateConfigData(comp, "OneKeyCalX", String.valueOf(x));
                            updateConfigData(comp, "OneKeyCalForceXFlag", "false");
                        } else {
                            if (!strX.equals("")) {
                                double X = Double.parseDouble(strX);
                                String fMin = "0.5";
                                String fMax = "2.0";
                                try {
                                    fMin = getConfigData(comp, "FMin").equals("") ? "0.5" : getConfigData(comp, "FMin");
                                    fMax = getConfigData(comp, "FMax").equals("") ? "2.0" : getConfigData(comp, "FMax");
                                } catch (Exception e) {
                                    saveExceptInfo2File("组分[" + energy.getComponent() + "]" + "获取F限制异常!");
                                }
                                double valF = 0;
                                try {
                                    valF = X / x;
                                    if (Double.isNaN(valF)) {
                                        saveExceptInfo2File("组分[" + comp + "] 计算[K值]时发生表达式计算结果为NaN" + ":preX:" + X + ",Xnew:" + x);
                                        valF = 0;
                                    }
                                } catch (Exception e) {
                                    saveExceptInfo2File("组分[" + comp + "] 计算[F值]时发生表达式计算错误" + e.getClass().getName() + ":preX:" + X + ",Xnew:" + x);
                                }
                                if ((valF < Double.parseDouble(fMin) || valF > Double.parseDouble(fMax))) {
                                    AddError(energy.getComponent(), 585, ErrorLog.msgType.运行_信息);
                                } else {
                                    updateConfigData(comp, "OneKeyCalX", String.valueOf(x));
                                }
                                saveRunInfo2File("组分[" + comp + "] valF:" + valF + "preX:" + X + "x:" + x + "fMin:" + fMin + "fMax:" + fMax);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            saveExceptInfo2File("组分[" + energy.getComponent() + "]" + "一键校准X计算异常!");
        }
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
            sql += " order by time desc , id desc limit ?,?";
            params = copy(params, new String[]{Long.toString(index)}, new String[]{Long.toString(len)}).toArray(new String[params.length + 1]);
        }
        return historyDBHelper.queryListMap(sql, params);
    }

    /*  *//*
     * Component TP
     * Flow  做样
     *
     * */
    public List<Map> select(String Component, String Flow, String Flag, String startTime, String endTime, long index, long len) {
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
        if (Flag != null) {
            sql += " and tag!=?";
            params = copy(params, new String[]{Flag}).toArray(new String[params.length + 1]);
        }
        {
            sql += " order by time desc , id desc limit ?,?";
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
            sql += " order by time desc , id desc limit ?,?";
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
        sql += " order by time desc , id desc limit 0,1";

        return historyDBHelper.queryListMap(sql, params);
    }

    public List<Map> selectAvg(String Component, String[] Flows, long index, long len) {
        String sql = "select avg(C) as cAvg from(select * from History where component=?";
        String[] params = new String[]{Component};
        if (Flows != null) {
            sql += " and(";
            for (String item : Flows) {
                sql += " flow=? or";
                params = copy(params, new String[]{item}).toArray(new String[params.length + 1]);
            }
            sql = sql.substring(0, sql.length() - 3);
            sql += ")";
        }
        sql += " order by time desc , id desc limit ?,?)";
        params = copy(params, new String[]{Long.toString(index)}, new String[]{Long.toString(len)}).toArray(new String[params.length + 1]);

        return historyDBHelper.queryListMap(sql, params);
    }

    /**
     * 新增     根据时间查询指定分组历史记录
     */
    public List<Map> selectTimeDate(String Component, String startTime, String endTime) {
        String sql = "select * from History where component= " + "'" + Component + "'" + " and datetime(time) between " + "'" + startTime + "'" + " and " + "'" + endTime + "'";
        return historyDBHelper.queryListMap(sql, null);
    }

    /**
     * 新增     查询表的列数
     */
    public List<Map> selectcolumns() {
        String sql = "pragma table_info(History)";
        return historyDBHelper.queryListMap(sql, null);
    }

    /**
     * 查询最新一条数据
     *
     * @return
     */
    public List<Map> selectTop() {
        String sql = "select * from History";
        String[] params = new String[]{};
        sql += " order by time desc , id desc limit 0,1";

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

    public List<Map> selectFlowAndTimeData(String Component, String Flow, String Time) {
        String sql = "select * from History where component=?";
        String[] params = new String[]{Component};
        if (Flow != null) {
            sql += " and flow=?";
            params = copy(params, new String[]{Flow}).toArray(new String[params.length + 1]);
        }
        if (Time != null) {
            sql += " and time=?";
            params = copy(params, new String[]{Time}).toArray(new String[params.length + 1]);
        }
        return historyDBHelper.queryListMap(sql, params);
    }

    /*
     * 小于 limit  出数出 down  -  up 直接的数
     */
    public static double randomData(double cVal, double limit, double up, double down) {

        if (cVal <= limit) {
            Random random = new Random();
            //修正值在[up，down)之间
            cVal = random.nextDouble() * (up - down) + down;
        }
        return cVal;
    }

    /*
     * 小于 limit  出数出 down  -  up 直接的数
     */
    public static double randomData(String compName, double cVal, double limit, double up, double down) {
        switch (compName) {
            case "VPC":
                if (cVal <= limit) {
                    Random random = new Random();
                    //修正值在[up，down)之间
                    cVal = random.nextDouble() * (up - down) + down;
                }
                break;
            default:
                if (cVal < limit) {
                    Random random = new Random();
                    //修正值在[up，down)之间
                    cVal = random.nextDouble() * (up - down) + down;
                }
                break;
        }
        return cVal;
    }

    /*
       各个产品默认修正下限       防出零
       * **/
    public static double getLimitLow(String compName) {
        double limitLow;
        switch (compName) {
            case "CODmn":
            case "Pb":
                limitLow = 0.05;
                break;
            case "CODcr":
                limitLow = 1;
                break;
            case "TCu":
            case "Cu":
            case "Mn":
            case "Ni":
            case "TN":
            case "TPb":
            case "TNi":
                limitLow = 0.01;
                break;
            case "CN":
            case "VPC":
                limitLow = 0.001;
                break;
            case "TP":
            case "NH3N":
            case "TCr":
            case "TMn":
            case "TFe":
            case "CrVI":
                limitLow = 0.005;
                break;
            default:
                limitLow = 0.02;
                break;
        }
        return limitLow;
    }

    /*
    各个产品默认修正上限  防出零
    * **/
    public static double getLimitHigh(String compName) {
        double limitHigh;
        switch (compName) {
            case "CODmn":
                limitHigh = 0.2;
                break;
            case "CODcr":
                limitHigh = 3;
                break;
            case "TCu":
            case "Cu":
            case "Mn":
            case "Ni":
            case "TN":
            case "TPb":
            case "TNi":
                limitHigh = 0.05;
                break;
            case "Pb":
                limitHigh = 0.1;
                break;
            case "CN":
                limitHigh = 0.005;
                break;
            case "VPC":
                limitHigh = 0.002;
                break;
            case "TP":
            case "NH3N":
            case "TCr":
            case "TMn":
            case "TFe":
                limitHigh = 0.01;
                break;
            case "CrVI":
                limitHigh = 0.03;
                break;
            default:
                limitHigh = 0.05;
                break;
        }
        return limitHigh;
    }

    /*
 各个产品默认界限   防出零
 * **/
    public static double getLimitDoorValue(String compName) {
        double limitValue;
        switch (compName) {
            case "CODmn":
                limitValue = 0.2;
                break;
            case "CODcr":
                limitValue = 1;
                break;
            case "TCu":
            case "Cu":
            case "Mn":
            case "Ni":
            case "TN":
            case "TNi":
            case "TPb":
                limitValue = 0.05;
                break;
            case "Pb":
                limitValue = 0.1;
                break;
            case "CN":
                limitValue = 0.005;
                break;
            case "TP":
            case "NH3N":
            case "TCr":
            case "TMn":
            case "TFe":
            case "CrVI":
                limitValue = 0.01;
                break;
            case "VPC":
                limitValue = 0.002;
                break;
            default:
                limitValue = 0.05;
                break;
        }
        return limitValue;
    }

    /*
     * 下限值修正     检出限    防出零
     * */
    private static double CValDownLimit(String compName, Double cVal) {

        try {
            //不再使用，并且默认"limitSwitch"为open，所以注释掉
            /*if (getConfigData(compName, "limitSwitch").equals("close")) {
                return cVal;
            } else {*/
            String strLimitValue = getConfigData(compName, "limitValue");
            String strLimitLow = getConfigData(compName, "limitLow");
            String strLimitHigh = getConfigData(compName, "limitHigh");

            if (strLimitValue == null || strLimitValue.equals("") || strLimitLow == null || strLimitLow.equals("") || strLimitHigh == null || strLimitHigh.equals("")) {
                cVal = randomData(cVal, getLimitDoorValue(compName), getLimitHigh(compName), getLimitLow(compName));
                if (cVal == 0) {
                    cVal = randomData(cVal, 0.003, 0.003, 0.001);
                }
            } else {
                cVal = randomData(cVal, Double.parseDouble(strLimitValue), Double.parseDouble(strLimitHigh), Double.parseDouble(strLimitLow));
            }
            //}
        } catch (Exception e) {
            saveExceptInfo2File("组分[" + compName + "]" + "检出限异常!");
        }

        return cVal;
    }

    /*
  各个产品默认修正下限 定量下限
  * **/
    private static double getQuantitativeLimitLow(String compName) {
        double limitLow;
        switch (compName) {
            case "CODcr":
                limitLow = 10;
                break;
            case "NH3N":
                limitLow = 0.05;
                break;
            default:
                limitLow = getLimitLow(compName);
                break;
        }
        return limitLow;
    }

    /*
    各个产品默认修正上限  定量下限
    * **/
    private static double getQuantitativeLimitHigh(String compName) {
        double limitHigh;
        switch (compName) {
            case "CODcr":
                limitHigh = 13;
                break;
            case "NH3N":
                limitHigh = 0.15;
                break;
            default:
                limitHigh = getLimitHigh(compName);
                break;
        }
        return limitHigh;
    }

    /*
    各个产品默认界限    定量下限
 * **/
    private static double getQuantitativeLimitDoorValue(String compName) {
        double limitValue;
        switch (compName) {
            case "CODcr":
                limitValue = 10;
                break;
            case "NH3N":
                limitValue = 0.15;
                break;
            default:
                limitValue = getLimitDoorValue(compName);
                break;
        }
        return limitValue;
    }

    /*
     *  下限值修正     定量下限
     * */
    private static double quantitativeLimit(String compName, Double cVal) {
        try {
            //不再使用，并且默认"QuantitativeLimitSwitch"为open，所以注释掉
            /*if (getConfigData(compName, "QuantitativeLimitSwitch").equals("close")) {
                return cVal;
            } else {*/
            String strLimitValue = getConfigData(compName, "QuantitativeLimitValue");
            String strLimitLow = getConfigData(compName, "QuantitativeLimitLow");
            String strLimitHigh = getConfigData(compName, "QuantitativeLimitHigh");
            if (strLimitValue == null || strLimitValue.equals("") || strLimitLow == null || strLimitLow.equals("") || strLimitHigh == null || strLimitHigh.equals("")) {
                cVal = randomData(cVal, getQuantitativeLimitDoorValue(compName), getQuantitativeLimitHigh(compName), getQuantitativeLimitLow(compName));
                if (cVal == 0) {
                    cVal = randomData(cVal, 0.003, 0.003, 0.001);
                }
            } else {
                cVal = randomData(cVal, Double.parseDouble(strLimitValue), Double.parseDouble(strLimitHigh), Double.parseDouble(strLimitLow));
            }
            //}
        } catch (Exception e) {
            saveExceptInfo2File("组分[" + compName + "]" + "定量下限异常!");
        }

        return cVal;
    }

    /*
     *   量程自动切换判断
     * */
    private int AutoRangeChangeSelect(String flowName, String compName, double cVal) {
        int iChangeTimes = 2;

        if ((flowName.equals(context.getResources().getString(R.string.ZY)) || flowName.equals(context.getResources().getString(R.string.JBHS)))
                && (getConfigData(compName, "AUTORANGE").equals("true"))) {
            /*量程自动切换判断*/
            String ucAutoRangeTemp = getConfigData(compName, "RANGE");

            /*量程切换判断*/
            new RangeAutoChange(compName, cVal);

            if (!ucAutoRangeTemp.equals(getConfigData(compName, "RANGE"))) {
                /*量程进行一次切换*/
                AddError(compName, 529, ErrorLog.msgType.运维_信息);
                //是否开启切换量程后再启动
                if (getConfigData(compName, "AUTO_RANGE_JOB_SWITCH").equals("true")) {
                    /*经过切换之后*/
                    int times = Integer.parseInt(getConfigData(compName, "RANGE_AUTO_CHANGE_COUNT"));

                    if (times >= iChangeTimes) {
                        updateConfigData(compName, "RANGE_AUTO_CHANGE_COUNT", "0");
                        if (getConfigData(compName, "AUTO_RANGE_SWITCH_RECOVERY").equals("true")) {
                            //切回主量程
                            setUseRange(compName, getConfigData(compName, "AUTORANGE_MAIN_RANGE"));
                            /*量程进行一次切换*/
                            AddError(compName, 529, ErrorLog.msgType.运维_信息);
                        }
                    } else {
                        updateConfigData(compName, "RANGE_AUTO_CHANGE_COUNT", String.valueOf(++times));
                        updateConfigData(compName, "RANGE_AUTO_CHANGE_START_FLAG", "true");
                        updateConfigData(compName, "RANGE_AUTO_CHANGE_FLOW", flowName);// 再启动流程
                        // 需要补测则当前值不需要
                        return 1;
                    }
                } else {
                    if (getConfigData(compName, "AUTO_RANGE_SWITCH_RECOVERY").equals("true")) {
                        //切回主量程
                        setUseRange(compName, getConfigData(compName, "AUTORANGE_MAIN_RANGE"));
                        /*量程进行一次切换*/
                        AddError(compName, 529, ErrorLog.msgType.运维_信息);
                    }
                    updateConfigData(compName, "RANGE_AUTO_CHANGE_COUNT", "0");
                }
            } else {
                if (getConfigData(compName, "AUTO_RANGE_SWITCH_RECOVERY").equals("true")) {
                    //切回主量程
                    setUseRange(compName, getConfigData(compName, "AUTORANGE_MAIN_RANGE"));
                    /*量程进行一次切换*/
                    AddError(compName, 529, ErrorLog.msgType.运维_信息);
                }
                updateConfigData(compName, "RANGE_AUTO_CHANGE_COUNT", "0");
            }
        } else {
            updateConfigData(compName, "RANGE_AUTO_CHANGE_COUNT", "0");
        }
        return 0;
    }

    /*
     * 最后一条数据的数据时间
     */
    public static String getLastDataTime(String cmpName) {
        String time = null;

        History history = new History(context);
        List<Map> hisMap = history.selectTop(cmpName);
        if (hisMap.size() > 0) {
            time = hisMap.get(0).get("time").toString();
        }
        return time;
    }

    /*
     * 获取历史数据最后一条用于上传
     **/
    public static List<Map> getLastUpdateData(String cmpName) {

        History history = new History(context);

        List<Map> list;
        /*获取历史数据最新一条*/

        /*校准数据上传*/
        if (getPublicConfigData("CAL_DATA_UPDATE").equals("true")) {
            list = history.select(cmpName, null, null, null, 0, 1);
        } else {

            list = history.select(cmpName, context.getString(R.string.ZY), null, null, 0, 1);
        }
        return list;
    }

    /*
     * 获取历史数据中最新的数据
     * */
    public static List<Map> getHistoryData(String cmpName, int startNum, int sum) {

        History history = new History(context);

        List<Map> list;
        /*获取历史数据最新一条*/

        list = history.select(cmpName, null, null, null, startNum, sum);

        return list;
    }

    /*
     * 获取最新一条指定组份 指定类型的数据
     */
    public static List<Map> getLastHistoryFlowData(String cmpName, String flowName) {

        History history = new History(context);

        List<Map> list;
        /*获取历史数据最新一条*/
        list = history.select(cmpName, flowName, null, null, 0, 1);

        return list;
    }


    /*
     * 时间加秒处理
     */
    public static Date addSecond(Date date, int seconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, seconds);
        return calendar.getTime();
    }

    /*
     * 修正历史数据时间
     */
    public static String CorrRecHistoryDataTime(int year, int month, int date, int hourOfDay, int minute, int second) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, date, hourOfDay, minute, second);
        Date hisDate = calendar.getTime();

        if ((minute == 59) && (second > 54 && second <= 59)) {
            Date corrDate = addSecond(hisDate, 60 - second);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
            return simpleDateFormat.format(corrDate.getTime());
        }
        return "";
    }

    /*判断光谱测量界面的测量点数是否符合算法需要的点数**/
    public static boolean isEnergyNumSame(String compName, int[] energy) {
        boolean flag = false;
        try {
            String[] measPointGroup = getConfigData(compName, "gpcld").split(";");
            flag = ((measPointGroup.length - 1) * 2 == energy.length);
        } catch (Exception e) {
            saveExceptInfo2File("判断设置测量点和算法匹配时异常");
        }
        return flag;
    }

    /*
     * 背板数据 结构获取
     */
    public static DataStruct getDataStruct(List<Map> list, int i) {

        DataStruct data = new DataStruct();
        try {
            if (list.size() > 0 && list.size() >= i) {
                String timeStr = list.get(i).get("time").toString();
                timeStr = timeStr.replace(" ", "-");
                String[] time = timeStr.split("[:-]");
                int year = Integer.parseInt(time[0]);
                int month = Integer.parseInt(time[1]);
                int day = Integer.parseInt(time[2]);
                int hour = Integer.parseInt(time[3]);
                int min = Integer.parseInt(time[4]);
                int sec = Integer.parseInt(time[5]);
                data.setDataTime(year, month, day, hour, min, sec);
                data.setData(Float.parseFloat(list.get(i).get("C").toString()));
                data.setType(list.get(i).get("component").toString());
                data.setFlow(list.get(i).get("flow").toString());
                data.setStrEngs(list.get(i).get("energy").toString());
            }
        } catch (Exception e) {
            Log.i("except", e.toString());
        }
        return data;
    }


    /*超仪器量程上限值**/
    private static Boolean overDevUpRange(String compName, double cVal) {
        if (cVal > Float.parseFloat(getConfigData(compName, "YBLCH")) * Double.parseDouble(getConfigData(compName, "devRangeUpMulti"))) {
            AddError(compName, 531, ErrorLog.msgType.运行_信息);
            saveRunInfo2File("组分[" + compName + "]超仪表量程上限: 实测值 = " + cVal);
            return true;
        }
        return false;
    }

    /*超当前量程上限值**/
    private static Boolean overNowUpRange(String compName, int range, double cVal) {
        return (cVal > Double.parseDouble(getConfigData(compName, "LC" + range + "H")) * Double.parseDouble(getConfigData(compName, "nowRangeUpMulti")));
    }

    /*超仪表上下限报警
     *
     * **/
    public static double oveRangeSetting(String fcName, String compName, int range, double cVal) {
        try {
            if (fcName.equals(context.getResources().getString(R.string.ZY))) {
                // 超当前量程上下限告警
                if (getConfigData(compName, "alarmOutNowRangeSwitch").equals("open")) {
                    if ((cVal > Double.parseDouble(getConfigData(compName, "LC" + range + "H")) * Double.parseDouble(getConfigData(compName, "nowRangeUpMulti")))
                            || (cVal < Double.parseDouble(getConfigData(compName, "LC" + range + "L")))) {
                        saveRunInfo2File("组分[" + compName + "]超当前量程" + range + "测量范围:实测值 = " + cVal);
                        AddError(compName, (570 + range - 1), ErrorLog.msgType.运行_信息);

                        if (overNowUpRange(compName, range, cVal)) {
                            // 超当前量程上限限制出数
                            if (getConfigData(compName, "alarmOutNowRangeOutLimit").equals("open")) {
                                cVal = Double.parseDouble(getConfigData(compName, "LC" + range + "H")) * Double.parseDouble(getConfigData(compName, "nowRangeUpMulti"));
                            }
                        }
                    }
                }
                // 超仪器量程上下限告警
                if (getConfigData(compName, "alarmOutDevRangeSwitch").equals("open")) {
                    if (cVal < Float.parseFloat(getConfigData(compName, "YBLCL"))) {
                        saveRunInfo2File("组分[" + compName + "]超仪表量程下限: 实测值 = " + cVal);
                        AddError(compName, 532, ErrorLog.msgType.运行_信息);
                    } else if (overDevUpRange(compName, cVal)) {
                        // 海外非标氰化物需求超标限制出数
                        if (getConfigData(compName, "alarmOutDevRangeOutLimit").equals("open")) {
                            cVal = Double.parseDouble(getConfigData(compName, "YBLCH")) * Double.parseDouble(getConfigData(compName, "devRangeUpMulti"));
                        }
                    }
                }
                if (getPublicConfigData("SYS_RELAYCONF").equals("2")) {
                    if (cVal < Float.parseFloat(getConfigData(compName, "ALARM_L"))) {
                        AddError(compName, 582, ErrorLog.msgType.运行_信息);
                    } else if (cVal > Float.parseFloat(getConfigData(compName, "ALARM_H"))) {
                        AddError(compName, 583, ErrorLog.msgType.运行_信息);
                    }
                }
            }
        } catch (Exception e) {
            saveRunInfo2File("组分[" + compName + "] 超量程报警异常");
        }
        return cVal;
    }

    /**
     * 核查判定
     *
     * @param compName 参数
     * @param fcName   流程名称
     * @param cVal     值
     */
    public void CheckDetermination(String compName, String fcName, double cVal) {
        if (!fcName.equals(context.getResources().getString(R.string.BYHC))) {
            return;
        }
        //当是自动标样核查并且开启标核校准
        if (getConfigData(compName, "BHPD").equals("open") && !getConfigData(compName, "BHJZCon").equals("")) {

            /*if (fcName.equals(context.getResources().getString(R.string.span_2))) {
                doControlJob(compName, context.getResources().getString(R.string.BYHC));
            } else {*/
            double bValCon = Double.parseDouble(getConfigData(compName, "BHJZCon"));
            double dA = ((cVal - bValCon) / bValCon) * 100;
            if (dA <= -10 || dA >= 10) {
                //标样核查不合格,dA
                AddError(compName, 617, 报错_警告);
                saveRunInfo2File("组分[" + compName + "] 标样核查不合格" + String.format("%." + pointNum + "f ", dA) + "%");
                updateConfigData(compName, "BHJZFlag", "true");


                if (getConfigData(compName, "BHJZCount").equals("")) {
                    updateConfigData(compName, "BHJZCount", "2");
                }
                saveRunInfo2File("BHJZCount:" + getConfigData(compName, "BHJZCount"));
                /*//判断自动标样核查次数
                saveRunInfo2File("BHJZCount_1" + getConfigData(compName, "BHJZCount"));

                saveRunInfo2File("BHJZCount_2" + getConfigData(compName, "BHJZCount"));
                int iBHJZCount = Integer.parseInt(getConfigData(compName, "BHJZCount"));
                if (iBHJZCount > 0) {
                    updateConfigData(compName, "BHJZCount", String.valueOf(--iBHJZCount));
                    doControlJob(compName, context.getResources().getString(R.string.JZ) + "_" + context.getResources().getString(R.string.BYHC));
                    saveRunInfo2File("组分[" + compName + "] " + String.valueOf(iBHJZCount) + "启动校准标核");
                } else {
                    updateConfigData(compName, "BHJZCount", "");
                }*/
            } else {
                //标样核查合格,dA
                if (getConfigData(compName, "BHJZFlag").equals("true")) {
                    updateConfigData(compName, "BHJZFlag", "false");
                }
                if (!getConfigData(compName, "BHJZCount").equals("")) {
                    updateConfigData(compName, "BHJZCount", "");
                }
                saveRunInfo2File("组分[" + compName + "] 标样核查合格");
            }
            //}
        }
    }

    public void RefreshCALHisTime(String compName, String fcName, String range, String time) {
        if (fcName.equals(context.getResources().getString(R.string.span_1))) {
            updateConfigData(compName, "Range" + range + "_CAL1_His_Time", time);
        } else if (fcName.equals(context.getResources().getString(R.string.span_2))) {
            updateConfigData(compName, "Range" + range + "_CAL2_His_Time", time);
        }
    }

}
