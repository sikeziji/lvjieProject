package com.yzlm.cyl.cfragment.Cal.Component;

import com.yzlm.cyl.cfragment.R;

import static com.yzlm.cyl.cfragment.AppFunction.OpenDoorCheck.getMasterCtrlDoorStatusToNowDate;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.updateConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.History.randomData;
import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.QueryMeasBoardType;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.saveRunInfo2File;

public class CalcCorrectJumpValue {

    /**
     * @param sCompName  组分名称
     * @param sFlowName  流程名称
     * @param dNowValue  测量值
     * @param iAbsorbEn  测量值吸收能量
     * @param sValueTime 测量值时间
     * @return 0：正常执行流程 2: 出数后存A 1:数据进行修正
     */
    public static short judgeDisCorrectJumpValue(String sCompName, String sFlowName, double dNowValue, int iAbsorbEn, String sValueTime) {
        try {
            if (QueryMeasBoardType(sCompName).equals("2") && sCompName.equals("CODcr")) {
                if ((getConfigData(sCompName, "meaMode").equals("1")) || (getConfigData(sCompName, "meaMode").equals("3"))) {
                    if (sFlowName.equals(context.getResources().getString(R.string.ZY))) {
                        //数据时间到，当前时间内，未开过门
                        if (!getMasterCtrlDoorStatusToNowDate(sCompName, sValueTime)) {
                            if (getConfigData(sCompName, "DoorCloseDataCounter").equals("1")) {
                                String sMeaA = getConfigData(sCompName, "MeaAValueEn");
                                if (sMeaA.equals("")) {
                                    // 执行防出零后存A 及吸收光强
                                    saveRunInfo2File("组分[" + sCompName + "]  没缓存A");
                                    return 2;
                                } else {
                                    // 前一个测量值
                                    double dAValue = Double.parseDouble(sMeaA.split(";")[0]);
                                    // 前一个值的吸收能量
                                    int dAValueAbsorb = Integer.parseInt(sMeaA.split(";")[1]);

                                    if (dNowValue > (dAValue * 1.1) || dAValue < (dAValue * 0.9)) {
                                        if (Math.abs(iAbsorbEn - dAValueAbsorb) < 50) {
                                            return 1;
                                        } else {
                                            saveRunInfo2File("组分[" + sCompName + "]  超范围 cVEn：" + iAbsorbEn + "AEn:" + dAValueAbsorb);
                                        }
                                    } else {
                                        saveRunInfo2File("组分[" + sCompName + "]  超范围 cV：" + dNowValue + "A:" + dAValue);
                                    }
                                }
                            } else {
                                updateConfigData(sCompName, "DoorCloseDataCounter", "1");
                            }
                        } else {
                            saveRunInfo2File("组分[" + sCompName + "]  最近一小时开门了");
                        }
                    } else {
                        if (!getConfigData(sCompName, "MeaAValueEn").equals("")) {
                            updateConfigData(sCompName, "MeaAValueEn", "");
                        }
                        saveRunInfo2File("组分[" + sCompName + "]  非做样数据：" + sFlowName);
                    }
                } else {
                    saveRunInfo2File("组分[" + sCompName + "]  模式不正确");
                }
            } else {
                if (getConfigData(sCompName, "DoorCloseDataCounter").equals("1")) {
                    updateConfigData(sCompName, "DoorCloseDataCounter", "0");
                }
                if (!getConfigData(sCompName, "MeaAValueEn").equals("")) {
                    updateConfigData(sCompName, "MeaAValueEn", "");
                }
            }
        } catch (Exception e) {
            saveRunInfo2File("组分[" + sCompName + "]  jumpValue：" + e.toString());
        }
        return 0;
    }


    /**
     * 正负1修正
     *
     * @param cValA 测量值
     * @return 修正后测量值
     */
    public double getRandomCalData(double cValA) {

        return randomData(cValA, cValA + 1, (cValA + cValA * 0.01), (cValA - cValA * 0.01));
    }

}
