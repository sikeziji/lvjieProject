package com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool;

import com.yzlm.cyl.cfragment.Communication.Thread.SendManager;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.Content.Component.Config.MeaParTable;
import com.yzlm.cyl.cfragment.R;

import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.updateConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogMsg;
import static com.yzlm.cyl.cfragment.Content.Component.ContentTool.getParDownLimit;
import static com.yzlm.cyl.cfragment.Content.Component.ContentTool.getParUpLimit;
import static com.yzlm.cyl.cfragment.Global.S0;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.lMeaPar;
import static com.yzlm.cyl.clibrary.Util.DataUtil.copybyte;
import static com.yzlm.cyl.clibrary.Util.DataUtil.toByteArray;

public class ProtocolWrite {


    /*
   设置仪器消解温度
   * **/
    public static boolean setHeatTemp(String compName, String value) {
        MeaParTable mt;
        int iUp = 0;
        int iDown = 0;
        int iValue;
        float fValue;
        try {
            if (lMeaPar.size() > 0) {
                mt = lMeaPar.get(compName);
                String strName = context.getResources().getString(R.string.digestionTemper);
                if (getParUpLimit(mt, lMeaPar, compName, strName) != null) {
                    iUp = Integer.parseInt(getParUpLimit(mt, lMeaPar, compName, strName));
                }
                if (getParDownLimit(mt, lMeaPar, compName, strName) != null) {
                    iDown = Integer.parseInt(getParDownLimit(mt, lMeaPar, compName, strName));
                }
                if (!value.equals("")) {
                    if (value.contains(".")) {
                        fValue = (Float.parseFloat(value));
                        iValue = (int) fValue;
                    } else {
                        iValue = Integer.parseInt(value);
                    }
                    if (!(iValue < iDown || iValue > iUp)) {
                        saveOperationLogMsg(compName, "协议设置的消解温度_" + iValue, ErrorLog.msgType.操作_信息);
                        updateConfigData(compName, "xjwd", String.valueOf(iValue));
                        byte[] xjwdByte = copybyte(toByteArray(iValue, 4));
                        SendManager.SendCmd(compName + "_" + "设置消解温度_06_4", S0, 3, 100, xjwdByte);
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    /*
      设置仪器消解时长
      * **/
    public static boolean setHeatTime(String compName, String value) {

        MeaParTable mt;
        int iUp = 0, iDown = 0, iValue;
        float fValue;

        if (lMeaPar.size() > 0) {
            mt = lMeaPar.get(compName);
            String strName = context.getResources().getString(R.string.digestionTimer);
            if (getParUpLimit(mt, lMeaPar, compName, strName) != null) {
                iUp = Integer.parseInt(getParUpLimit(mt, lMeaPar, compName, strName));
            }
            if (getParDownLimit(mt, lMeaPar, compName, strName) != null) {
                iDown = Integer.parseInt(getParDownLimit(mt, lMeaPar, compName, strName));
            }
            if (!value.equals("")) {
                if (value.contains(".")) {
                    fValue = (Float.parseFloat(value));
                    iValue = (int) fValue;
                } else {
                    iValue = Integer.parseInt(value);
                }
                if (!(iValue < iDown || iValue > iUp)) {
                    saveOperationLogMsg(compName, "协议设置消解时长" + iValue, ErrorLog.msgType.操作_信息);
                    updateConfigData(compName, "xjsc", String.valueOf(iValue));
                    byte[] xjscByte = copybyte(toByteArray(iValue, 4));
                    SendManager.SendCmd(compName + "_" + "设置消解时长_06_5", S0, 3, 100, xjscByte);
                    return true;
                }
            }
        }
        return false;
    }

    /*
    设置仪器显色温度
    * **/
    public static boolean setColorTemp(String compName, String value) {
        MeaParTable mt;
        int iUp = 0, iDown = 0, iValue;
        float fValue;

        if (lMeaPar.size() > 0) {
            mt = lMeaPar.get(compName);
            if (getParUpLimit(mt, lMeaPar, compName, context.getResources().getString(R.string.colorTemper)) != null) {
                iUp = Integer.parseInt(getParUpLimit(mt, lMeaPar, compName, context.getResources().getString(R.string.colorTemper)));
            }
            if (getParDownLimit(mt, lMeaPar, compName, context.getResources().getString(R.string.colorTemper)) != null) {
                iDown = Integer.parseInt(getParDownLimit(mt, lMeaPar, compName, context.getResources().getString(R.string.colorTemper)));
            }
            if (!value.equals("")) {
                if (value.contains(".")) {
                    fValue = (Float.parseFloat(value));
                    iValue = (int) fValue;
                } else {
                    iValue = Integer.parseInt(value);
                }
                if (!(iValue < iDown || iValue > iUp)) {
                    saveOperationLogMsg(compName, "协议设置的显色温度_" + iValue, ErrorLog.msgType.操作_信息);
                    updateConfigData(compName, "xswd", value);
                    return true;
                }
            }
        }
        return false;

    }

    /*
      设置仪器显色时长
      * **/
    public static boolean setColorTime(String compName, String value) {
        MeaParTable mt;
        int iUp = 0, iDown = 0, iValue;
        float fValue;
        if (lMeaPar.size() > 0) {
            mt = lMeaPar.get(compName);
            if (getParUpLimit(mt, lMeaPar, compName, context.getResources().getString(R.string.colorTimer)) != null) {
                iUp = Integer.parseInt(getParUpLimit(mt, lMeaPar, compName, context.getResources().getString(R.string.colorTimer)));
            }
            if (getParDownLimit(mt, lMeaPar, compName, context.getResources().getString(R.string.colorTimer)) != null) {
                iDown = Integer.parseInt(getParDownLimit(mt, lMeaPar, compName, context.getResources().getString(R.string.colorTimer)));
            }
            if (!value.equals("")) {
                if (value.contains(".")) {
                    fValue = (Float.parseFloat(value));
                    iValue = (int) fValue;
                } else {
                    iValue = Integer.parseInt(value);
                }
                if (!(iValue < iDown || iValue > iUp)) {
                    saveOperationLogMsg(compName, "协议设置的显色时长_" + iValue, ErrorLog.msgType.操作_信息);
                    updateConfigData(compName, "xssc", value);
                    return true;
                }
            }
        }
        return false;
    }

    /*
         设置仪器消解降温
         * **/
    public static boolean setHeatCoolTemp(String compName, String value) {
        MeaParTable mt;
        int iUp = 0, iDown = 0, iValue;
        float fValue;
        if (lMeaPar.size() > 0) {
            mt = lMeaPar.get(compName);
            if (getParUpLimit(mt, lMeaPar, compName, context.getResources().getString(R.string.dispellingCooling)) != null) {
                iUp = Integer.parseInt(getParUpLimit(mt, lMeaPar, compName, context.getResources().getString(R.string.dispellingCooling)));
            }
            if (getParDownLimit(mt, lMeaPar, compName, context.getResources().getString(R.string.dispellingCooling)) != null) {
                iDown = Integer.parseInt(getParDownLimit(mt, lMeaPar, compName, context.getResources().getString(R.string.dispellingCooling)));
            }
            if (!value.equals("")) {
                if (value.contains(".")) {
                    fValue = (Float.parseFloat(value));
                    iValue = (int) fValue;
                } else {
                    iValue = Integer.parseInt(value);
                }
                if (!(iValue < iDown || iValue > iUp)) {
                    saveOperationLogMsg(compName, "协议设置的消解降温_" + iValue, ErrorLog.msgType.操作_信息);
                    updateConfigData(compName, "xjjw", value);
                    return true;
                }
            }
        }
        return false;
    }

    /*
            设置仪器显色降温
            * **/
    public static boolean setColorCoolTemp(String compName, String value) {
        MeaParTable mt;
        int iUp = 0, iDown = 0, iValue;
        float fValue;
        if (lMeaPar.size() > 0) {
            mt = lMeaPar.get(compName);
            if (getParUpLimit(mt, lMeaPar, compName, context.getResources().getString(R.string.colorCooling)) != null) {
                iUp = Integer.parseInt(getParUpLimit(mt, lMeaPar, compName, context.getResources().getString(R.string.colorCooling)));
            }
            if (getParDownLimit(mt, lMeaPar, compName, context.getResources().getString(R.string.colorCooling)) != null) {
                iDown = Integer.parseInt(getParDownLimit(mt, lMeaPar, compName, context.getResources().getString(R.string.colorCooling)));
            }
            if (!value.equals("")) {
                if (value.contains(".")) {
                    fValue = (Float.parseFloat(value));
                    iValue = (int) fValue;
                } else {
                    iValue = Integer.parseInt(value);
                }
                if (!(iValue < iDown || iValue > iUp)) {
                    saveOperationLogMsg(compName, "协议设置的显色降温_" + iValue, ErrorLog.msgType.操作_信息);
                    updateConfigData(compName, "xsjw", value);
                    return true;
                }
            }
        }
        return false;
    }

    /*
            设置仪器显色静置
            * **/
    public static boolean setColorStandTime(String compName, String value) {
        MeaParTable mt;
        int iUp = 0, iDown = 0, iValue;
        float fValue;
        if (lMeaPar.size() > 0) {
            mt = lMeaPar.get(compName);
            if (getParUpLimit(mt, lMeaPar, compName, context.getResources().getString(R.string.colorStand)) != null) {
                iUp = Integer.parseInt(getParUpLimit(mt, lMeaPar, compName, context.getResources().getString(R.string.colorStand)));
            }
            if (getParDownLimit(mt, lMeaPar, compName, context.getResources().getString(R.string.colorStand)) != null) {
                iDown = Integer.parseInt(getParDownLimit(mt, lMeaPar, compName, context.getResources().getString(R.string.colorStand)));
            }
            if (!value.equals("")) {
                if (value.contains(".")) {
                    fValue = (Float.parseFloat(value));
                    iValue = (int) fValue;
                } else {
                    iValue = Integer.parseInt(value);
                }
                if (!(iValue < iDown || iValue > iUp)) {
                    saveOperationLogMsg(compName, "协议设置的显色静置_" + iValue, ErrorLog.msgType.操作_信息);
                    updateConfigData(compName, "xsjz", value);
                    return true;
                }
            }
        }
        return false;
    }


    /*
            设置仪器  等待静置
            * **/
    public static boolean setWaitStand(String compName, String value) {
        MeaParTable mt;
        int iUp = 0, iDown = 0, iValue;
        float fValue;

        if (lMeaPar.size() > 0) {
            mt = lMeaPar.get(compName);
            if (getParUpLimit(mt, lMeaPar, compName, context.getResources().getString(R.string.waitingStand)) != null) {
                iUp = Integer.parseInt(getParUpLimit(mt, lMeaPar, compName, context.getResources().getString(R.string.waitingStand)));
            }
            if (getParDownLimit(mt, lMeaPar, compName, context.getResources().getString(R.string.waitingStand)) != null) {
                iDown = Integer.parseInt(getParDownLimit(mt, lMeaPar, compName, context.getResources().getString(R.string.waitingStand)));
            }
            if (!value.equals("")) {
                if (value.contains(".")) {
                    fValue = (Float.parseFloat(value));
                    iValue = (int) fValue;
                } else {
                    iValue = Integer.parseInt(value);
                }
                if (!(iValue < iDown || iValue > iUp)) {
                    saveOperationLogMsg(compName, "协议设置的等待静置_" + iValue, ErrorLog.msgType.操作_信息);
                    updateConfigData(compName, "waitStanding", value);
                    return true;
                }
            }
        }
        return false;
    }


    /*
                设置仪器  水样体积
                * **/
    public static boolean setWaterVolume(String compName, String value) {
        MeaParTable mt;
        float fUp = 0, fDown = 0, fValue;

        if (lMeaPar.size() > 0) {
            mt = lMeaPar.get(compName);
            if (getParUpLimit(mt, lMeaPar, compName, context.getResources().getString(R.string.waterVolume)) != null) {
                fUp = Float.parseFloat(getParUpLimit(mt, lMeaPar, compName, context.getResources().getString(R.string.waterVolume)));
            }
            if (getParDownLimit(mt, lMeaPar, compName, context.getResources().getString(R.string.waterVolume)) != null) {
                fDown = Float.parseFloat(getParDownLimit(mt, lMeaPar, compName, context.getResources().getString(R.string.waterVolume)));
            }
            if (!value.equals("")) {
                fValue = Float.parseFloat(value);
                if (!(fValue < fDown || fValue > fUp)) {
                    saveOperationLogMsg(compName, "协议设置的水样体积_" + fValue, ErrorLog.msgType.操作_信息);
                    updateConfigData(compName, "WATER_VOLUME", value);
                    return true;
                }
            }
        }
        return false;
    }


    /*
                设置仪器  氧化剂体积
                * **/
    public static boolean setOxidantVolume(String compName, String value) {
        MeaParTable mt;
        float fUp = 0, fDown = 0, fValue;

        if (lMeaPar.size() > 0) {
            mt = lMeaPar.get(compName);
            if (getParUpLimit(mt, lMeaPar, compName, context.getResources().getString(R.string.oxidantVolume)) != null) {
                fUp = Float.parseFloat(getParUpLimit(mt, lMeaPar, compName, context.getResources().getString(R.string.oxidantVolume)));
            }
            if (getParDownLimit(mt, lMeaPar, compName, context.getResources().getString(R.string.oxidantVolume)) != null) {
                fDown = Float.parseFloat(getParDownLimit(mt, lMeaPar, compName, context.getResources().getString(R.string.oxidantVolume)));
            }
            if (!value.equals("")) {
                fValue = Float.parseFloat(value);
                if (!(fValue < fDown || fValue > fUp)) {
                    saveOperationLogMsg(compName, "协议设置的氧化剂体积_" + fValue, ErrorLog.msgType.操作_信息);
                    updateConfigData(compName, "OXIDANT_VOLUME", value);
                    return true;
                }
            }
        }
        return false;
    }


    /*
                设置仪器  还原剂体积
                * **/
    public static boolean setReductantVolume(String compName, String value) {
        MeaParTable mt;
        float fUp = 0, fDown = 0, fValue;

        if (lMeaPar.size() > 0) {
            mt = lMeaPar.get(compName);
            if (getParUpLimit(mt, lMeaPar, compName, context.getResources().getString(R.string.reductantVolume)) != null) {
                fUp = Float.parseFloat(getParUpLimit(mt, lMeaPar, compName, context.getResources().getString(R.string.reductantVolume)));
            }
            if (getParDownLimit(mt, lMeaPar, compName, context.getResources().getString(R.string.reductantVolume)) != null) {
                fDown = Float.parseFloat(getParDownLimit(mt, lMeaPar, compName, context.getResources().getString(R.string.reductantVolume)));
            }
            if (!value.equals("")) {
                fValue = Float.parseFloat(value);
                if (!(fValue < fDown || fValue > fUp)) {
                    saveOperationLogMsg(compName, "协议设置的还原剂体积_" + fValue, ErrorLog.msgType.操作_信息);
                    updateConfigData(compName, "REDUCTANT_VOLUME", value);
                    return true;
                }
            }
        }
        return false;
    }


    /*
                设置仪器  还原剂浓度
                * **/
    public static boolean setReductantCoc(String compName, String value) {
        MeaParTable mt;
        float fUp = 0, fDown = 0, fValue;

        if (lMeaPar.size() > 0) {
            mt = lMeaPar.get(compName);
            if (getParUpLimit(mt, lMeaPar, compName, context.getResources().getString(R.string.reductantCoc)) != null) {
                fUp = Float.parseFloat(getParUpLimit(mt, lMeaPar, compName, context.getResources().getString(R.string.reductantCoc)));
            }
            if (getParDownLimit(mt, lMeaPar, compName, context.getResources().getString(R.string.reductantCoc)) != null) {
                fDown = Float.parseFloat(getParDownLimit(mt, lMeaPar, compName, context.getResources().getString(R.string.reductantCoc)));
            }
            if (!value.equals("")) {
                fValue = Float.parseFloat(value);
                if (!(fValue < fDown || fValue > fUp)) {
                    saveOperationLogMsg(compName, "协议设置的还原剂浓度_" + fValue, ErrorLog.msgType.操作_信息);
                    updateConfigData(compName, "REDUCTANT_COC", value);
                    return true;
                }
            }
        }
        return false;
    }


    /*
                设置仪器  滴定液体积
                * **/
    public static boolean setTitrationVolume(String compName, String value) {
        MeaParTable mt;
        float fUp = 0, fDown = 0, fValue;

        if (lMeaPar.size() > 0) {
            mt = lMeaPar.get(compName);
            if (getParUpLimit(mt, lMeaPar, compName, context.getResources().getString(R.string.titrationVolume)) != null) {
                fUp = Float.parseFloat(getParUpLimit(mt, lMeaPar, compName, context.getResources().getString(R.string.titrationVolume)));
            }
            if (getParDownLimit(mt, lMeaPar, compName, context.getResources().getString(R.string.titrationVolume)) != null) {
                fDown = Float.parseFloat(getParDownLimit(mt, lMeaPar, compName, context.getResources().getString(R.string.titrationVolume)));
            }
            if (!value.equals("")) {
                fValue = Float.parseFloat(value);
                if (!(fValue < fDown || fValue > fUp)) {
                    saveOperationLogMsg(compName, "协议设置的滴定液体积_" + fValue, ErrorLog.msgType.操作_信息);
                    updateConfigData(compName, "TITRATION_VOLUME", value);
                    return true;
                }
            }
        }
        return false;
    }


}
