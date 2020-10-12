package com.yzlm.cyl.cfragment.Cal.Component;

import android.support.annotation.Nullable;

import static com.yzlm.cyl.cfragment.Communication.CommProtocol.GB2005.GB2005.iDataFormat;
import static com.yzlm.cyl.cfragment.Communication.Component.AppCalc.Energy.getEnergyCh;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.DBConvert.MDBConvert.bubbleSort;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;

/*
 * Created by zwj on 2018/7/5.
 * <p>
 * 吸光度修正后能量修正计算公式
 */

public class CalcEnRevise {

    /*TN :  EA =   "E03*pow(10,Am+2*log(E02/E04))"
    EB = "E02"
    EC = "E03"
    ED = "E04"*/

    /*CODcr NH3N
            EA = pow(10,Am)*E02
            EB = E02;
            */
        /*
    TP  EA = E01:
        EB = pow(10,Am)*E01
    */
    private String EA_Formula = "E03*pow(10,Am+2*log(E02/E04))";
    private String EB_Formula = "E02";
    private String EC_Formula = "E03";
    private String ED_Formula = "E04";
    private String EE_Formula = "E05";
    private String EF_Formula = "E06";

    public void setEA_Formula(String EA_Formula) {
        this.EA_Formula = EA_Formula;
    }

    public String getEA_Formula() {
        return EA_Formula;
    }

    public void setEB_Formula(String EB_Formula) {
        this.EB_Formula = EB_Formula;
    }

    public String getEB_Formula() {
        return EB_Formula;
    }

    public void setEC_Formula(String EC_Formula) {
        this.EC_Formula = EC_Formula;
    }

    public String getEC_Formula() {
        return EC_Formula;
    }

    public void setED_Formula(String ED_Formula) {
        this.ED_Formula = ED_Formula;
    }

    public String getED_Formula() {
        return ED_Formula;
    }

    public void setEE_Formula(String ED_Formula) {
        this.EE_Formula = ED_Formula;
    }

    public String getEE_Formula() {
        return EE_Formula;
    }

    public void setEF_Formula(String ED_Formula) {
        this.EF_Formula = ED_Formula;
    }

    public String getEF_Formula() {
        return EF_Formula;
    }


    /*
     * 量程 Y值
     **/
    private double yRange1 = 0, yRange2 = 0, yRange3 = 0;
    /*
     * 修正系数x：用于反应修正值接近真实值的速度
     */
    private double x = 0;

    public double getParX() {
        return x;
    }

    public void setParX(double x) {
        this.x = x;
    }

    private double getRange1Y() {
        return yRange1;
    }

    public void setRange1Y(double yRange1) {
        this.yRange1 = yRange1;
    }

    private double getRange2Y() {
        return yRange2;
    }

    public void setRange2Y(double yRange2) {
        this.yRange2 = yRange2;
    }

    private double getRange3Y() {
        return yRange3;
    }

    public void setRange3Y(double yRange3) {
        this.yRange3 = yRange3;
    }

    public double getRangeY(int range) {
        double y;
        switch (range) {
            case 1:
                y = getRange1Y();
                break;
            case 2:
                y = getRange2Y();
                break;
            case 3:
                y = getRange3Y();
                break;
            default:
                y = getRange1Y();
                break;
        }
        return y;
    }

    /*找最多能量数**/
    public static int findEnergySum(String strEA) {
        for (int i = 9; i > 1; i--) {
            if (strEA.contains("E" + iDataFormat(String.valueOf(i), 2))) {
                return i;
            }
        }
        return 0;
    }

    private static int findEnergyStr(String strEA) {
        int num = 0;
        if (strEA.length() == 3) {
            switch (strEA) {
                case "E01":
                    num = 0;
                    break;
                case "E02":
                    num = 1;
                    break;
                case "E03":
                    num = 2;
                    break;
                case "E04":
                    num = 3;
                    break;
                case "E05":
                    num = 4;
                    break;
                case "E06":
                    num = 5;
                    break;
            }
        }
        return num;
    }


    /*
     计算新的能量
    * **/
    public static int getNewEnergy(String Component, int range, String strE, int[] energy, double newA) {
        try {
            int valE = -1;
            /* 总氮增加浊度的时候计算能量的方式*/
            switch (Component) {
                case "TN":
                    switch (energy.length) {
                        case 6:
                            if (strE.contains("lg")) {
                                String strRange = String.valueOf(range);
                                double kz1 = Double.parseDouble(getConfigData(Component, "KZ1Range" + strRange));
                                double kz2 = Double.parseDouble(getConfigData(Component, "KZ2Range" + strRange));

                                int[] ch = getEnergyCh(strE);
                                if (ch.length < 7) {
                                    saveExceptInfo2File("组分[" + Component + "] 计算[A值]时发生表达式异常!");
                                    return valE;
                                }

                            /*valE = (int) (energy[3] * Math.pow(10, (newA + kz1 * (Math.log10((double) energy[2] / (double) energy[5]))
                                    + 2 * (Math.log10(((double) energy[1] / (double) energy[4])) - kz2 * Math.log10((double) energy[2] / (double) energy[5])))));*/
                                valE = (int) (energy[ch[0]] * Math.pow(10, (newA + kz1 * (Math.log10((double) energy[ch[1]] / (double) energy[ch[2]]))
                                        + 2 * (Math.log10(((double) energy[ch[3]] / (double) energy[ch[4]])) - kz2 * Math.log10((double) energy[ch[5]] / (double) energy[ch[6]])))));
                                return valE;

                            } else {
                                /*公式只有能量标识的情况下**/
                                if (strE.length() == 3) {
                                    int num = 0;
                                    num = findEnergyStr(strE);
                                    if (num < energy.length) {
                                        valE = energy[num];
                                        return valE;
                                    }
                                }
                            }
                            break;
                        case 4:
                            if (strE.contains("lg")) {
                                int[] ch = getEnergyCh(strE);
                                if (ch.length < 3) {
                                    saveExceptInfo2File("组分[" + Component + "] 计算[A值]时发生表达式异常!");
                                    return valE;
                                }
                                valE = (int) (energy[ch[0]] * Math.pow(10, (newA + 2 * (Math.log10((double) energy[ch[1]] / (double) energy[ch[2]])))));
                                return valE;
                            } else {
                                /*公式只有能量标识的情况下**/
                                if (strE.length() == 3) {
                                    int num = 0;
                                    num = findEnergyStr(strE);
                                    if (num < energy.length) {
                                        valE = energy[num];
                                        return valE;
                                    }
                                }
                            }
                            break;
                    }
                    break;
                case "TPb":
                    if (energy.length == 4) {
                        if (strE.contains("lg")) {
                            // 补偿K
                            double kc = Double.parseDouble(getConfigData(Component, "gpbck"));
                            // 补偿B
                            double bc = Double.parseDouble(getConfigData(Component, "gpbcb"));

                            //获取能量编号在公式中的位置
                            int[] ch = getEnergyCh(strE);
                            if (ch.length < 3) {
                                saveExceptInfo2File("组分[" + Component + "] 计算[A值]时发生表达式异常!");
                                return valE;
                            }

                            valE = (int) (energy[ch[0]] * Math.pow(10, ((Math.log10((double) energy[ch[1]] / (double) energy[ch[2]])) - newA) * kc + bc));
                            return valE;
                        } else {
                            /*公式只有能量标识的情况下**/
                            if (strE.length() == 3) {
                                int num = 0;
                                num = findEnergyStr(strE);
                                if (num < energy.length) {
                                    valE = energy[num];
                                    return valE;
                                }
                            }
                        }
                    } else {
                        saveExceptInfo2File("组分[" + Component + "] 能量计算能量值不等于4个");
                    }
                    break;
                case "TP":
                    switch (energy.length) {
                        case 4:
                            if (strE.contains("lg")) {
                                String strRange = String.valueOf(range);
                                double kz1 = Double.parseDouble(getConfigData(Component, "KZ1Range" + strRange));
                                double kz2 = Double.parseDouble(getConfigData(Component, "KZ2Range" + strRange));
                                double kz3 = Double.parseDouble(getConfigData(Component, "KZ3Range" + strRange));

                                //获取能量编号在公式中的位置
                                int[] ch = getEnergyCh(strE);
                                if (ch.length < 3) {
                                    saveExceptInfo2File("组分[" + Component + "] 计算[A值]时发生表达式异常!");
                                    return valE;
                                }

                                valE = (int) (energy[ch[0]] * Math.pow(10, (newA * kz3 - kz2 + kz1 * (Math.log10((double) energy[ch[1]] / (double) energy[ch[2]])))));
                                return valE;
                            } else {
                                /*公式只有能量标识的情况下**///EB=E02
                                if (strE.length() == 3) {
                                    int num = 0;
                                    num = findEnergyStr(strE);
                                    if (num < energy.length) {
                                        valE = energy[num];
                                        return valE;
                                    }
                                }
                            }
                        case 2:
                            return getEnergySum2(Component, strE, energy, newA, valE);
                    }
                    break;
                default:
                    return getEnergySum2(Component, strE, energy, newA, valE);
            }
        } catch (Exception e) {
            saveExceptInfo2File("组分[" + Component + "] 计算新的能量异常" + e.toString());
        }
        return 0;
    }

    @Nullable
    private static int getEnergySum2(String Component, String strE, int[] energy, double newA, int valE) {
        if (energy.length > 1) {
            int num = 0;
            /*公式只有能量标识的情况下**/
            if (strE.length() == 3) {
                num = findEnergyStr(strE);
                if (num < energy.length) {
                    valE = energy[num];
                }
            } else {
                num = strE.contains("E01") ? 0 : 1;
                valE = (int) (Math.pow(10, newA) * energy[num]);
            }
            return valE;
        } else {
            saveExceptInfo2File("组分[" + Component + "] 计算[E01值]时发生表达式计算能量值小于2个");
        }
        return -1;
    }

}
