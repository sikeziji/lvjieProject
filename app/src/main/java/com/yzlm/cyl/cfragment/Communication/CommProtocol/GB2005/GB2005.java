package com.yzlm.cyl.cfragment.Communication.CommProtocol.GB2005;

import android.util.Log;

import com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.CRC16;
import com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.DataStruct;
import com.yzlm.cyl.cfragment.Communication.Communication;
import com.yzlm.cyl.cfragment.Communication.Component.AppAction.ActionTable;
import com.yzlm.cyl.cfragment.Communication.Component.AppCalc.Calc;
import com.yzlm.cyl.cfragment.Communication.Component.AppCalc.CalcTable;
import com.yzlm.cyl.cfragment.Communication.Component.AppFlow.FlowTable;
import com.yzlm.cyl.cfragment.Communication.Thread.SendManager;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.History;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.Kbf;
import com.yzlm.cyl.cfragment.Global;
import com.yzlm.cyl.cfragment.R;

import java.io.File;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.yzlm.cyl.cfragment.Communication.CommProtocol.GB2017.GB2017.stringTimeCompare;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getColorTemp;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getHeatTime;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.ProtocolRead.getLastHistory;
import static com.yzlm.cyl.cfragment.Communication.CommunitcationTest.S1_DCIReadTimes;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.updateConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogMsg;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.getPublicConfigData;
import static com.yzlm.cyl.cfragment.Content.Component.List2.List2_Content2.getModePermissions;
import static com.yzlm.cyl.cfragment.Content.Component.List2.List2_Content3.setUseRange;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.SetDateTime;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.dataFormat;
import static com.yzlm.cyl.cfragment.Global.IOBoardUsed;
import static com.yzlm.cyl.cfragment.Global.S0;
import static com.yzlm.cyl.cfragment.Global.S1;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.doControlJob;
import static com.yzlm.cyl.cfragment.Global.doFlowing;
import static com.yzlm.cyl.cfragment.Global.getCalc;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;
import static com.yzlm.cyl.cfragment.Global.saveRunInfo2File;
import static com.yzlm.cyl.cfragment.Global.strComponent;
import static com.yzlm.cyl.cfragment.Global.syncAutoDoSample_topHour;
import static com.yzlm.cyl.cfragment.Global.workState;
import static com.yzlm.cyl.clibrary.Util.DataUtil.copybyte;
import static com.yzlm.cyl.clibrary.Util.DataUtil.toByteArray;
import static com.yzlm.cyl.clibrary.Util.FileUtil.readString;
import static java.lang.Integer.parseInt;

/**
 * Created by zwj on 2017/5/12.
 */

@SuppressWarnings("unchecked")
public class GB2005 {

    private static Map<String, String> Flows = new HashMap();
    private static String[] strCmd = new String[44];
    private static String[] strCode = new String[30];
    private static String MN = "123456789101112131415161";

    private static String[] StrNew = new String[20];
    private final static int ONE_COC_CH = 1;               /*测量值上传*/
    private final static int HEART = 2;                      /*心跳指令*/
    private final static int SPAN_COC_CH = 6;              /*标样二浓度           5011*/
    private final static int RANGE_COE_CH = 8;             /*量程KB             5012*/
    private final static int ADJUST_COE_CH = 10;           /*校正系数KB         5013*/
    private final static int PUMP_TIME = 12;                /*预开水泵时间      5014*/
    private final static int ALARM_MSG = 14;                /*最新一条报警      5015*/
    private final static int JOB_PERIOD = 16;               /*做样周期          5016*/
    private final static int RANGE_SELECT = 18;             /*量程选择          5019*/
    private final static int DIGESTION_TIME = 20;           /*消解时长          5020*/
    private final static int DIGESTION_TEMPERATURE = 22;    /*消解温度          5021*/
    private final static int COLORTIME = 24;                /*显色时长          5022*/
    private final static int ALARM_LIMIT = 26;              /*报警上下限        5023*/
    private final static int RANGE_INFO = 28;               /*量程读取          5024*/
    private final static int K2CR2O7_COC = 30;              /*K2CR2O7浓度       5025*/
    private final static int AUTO_CALIBRATION = 32;         /*自动校准          5018*/
    private final static int AUTO_CLAN = 34;                /*自动清洗          5026*/
    private final static int SYS_TIME = 36;                 /*系统时间          1011*/
    private final static int ABSORBANCY = 38;               /*吸光度能量        2013*/
    private final static int Correction_Factor = 40;           /*修正因子KB         5033*/
    private final static int SPAN1_COC_CH = 42;           /*标样一浓度         5031*/
    private final static int COLORTEMP = 44;           /*显色温度         5032*/

    public GB2005() {
        AnalysisCodeFile();
    }

    // 获取一个组份上传命令
    public String getMeaDataUpdateCmd(DataStruct DataInfo, boolean isHistory) {
        if (strCmd[ONE_COC_CH] == null) {
            if (!AnalysisCmdFile())
                return "";
        }
        return packageMeaDataUpdate(strCmd[ONE_COC_CH], DataInfo, isHistory);
    }

    private String getAbsorbancyCmd(DataStruct DataInfo) {
        if (strCmd[ABSORBANCY] == null) {
            if (!AnalysisCmdFile())
                return "";
        }
        return packageAbsorbancyDataUpdate(strCmd[ABSORBANCY], DataInfo);
    }

    // 获取标样2浓度命令获取
    private String getSpanCocCmd(DataStruct DataInfo) {
        if (strCmd[SPAN_COC_CH] == null) {
            if (!AnalysisCmdFile())
                return "";
        }
        return packageSpanCocUpdate(strCmd[SPAN_COC_CH], DataInfo);
    }

    // 获取标样1浓度命令获取
    private String getSpan1CocCmd(DataStruct DataInfo) {
        if (strCmd[SPAN1_COC_CH] == null) {
            if (!AnalysisCmdFile())
                return "";
        }

        return packageSpan1CocUpdate(strCmd[SPAN1_COC_CH], DataInfo);
    }

    /*获取量程KB命令*/
    private String getCoeKbCmd(String compName, float fk1, float fb1, float fk2, float fb2, float fk3, float fb3) {
        if (strCmd[RANGE_COE_CH] == null) {
            if (!AnalysisCmdFile())
                return "";
        }
        return packageCoeKbUpdate(strCmd[RANGE_COE_CH], compName, fk1, fb1, fk2, fb2, fk3, fb3);
    }

    /*获取量程KB命令*/
    private String getCoeKbCmd(String compName, float fk, float fb) {
        if (strCmd[RANGE_COE_CH] == null) {
            if (!AnalysisCmdFile())
                return "";
        }
        return packageCoeKbUpdate(strCmd[RANGE_COE_CH], compName, fk, fb);
    }

    /*工作曲线命令*/
    private String getAdjustKbCmd(String compName, float fk) {
        if (strCmd[ADJUST_COE_CH] == null) {
            if (!AnalysisCmdFile())
                return "";
        }
        return packageAdjustKbUpdate(strCmd[ADJUST_COE_CH], compName, fk);

    }

    /*获取量程KB命令*/
    private String getCorrectionFactorKbCmd(String compName, float fk, float fb) {
        if (strCmd[Correction_Factor] == null) {
            if (!AnalysisCmdFile())
                return "";
        }
        return packageCorrectionFactorKbUpdate(strCmd[Correction_Factor], compName, fk, fb);
    }

    /*预开水泵命令*/
    private String getPumpTimeCmd(String compName, int time) {
        if (strCmd[PUMP_TIME] == null) {
            if (!AnalysisCmdFile()) {
                return "";
            }
        }
        return packagePumpTimeUpdate(strCmd[PUMP_TIME], time);
    }

    /*报警记录命令*/
    private String getAlarmMsgCmd(int errNum) {
        if (strCmd[ALARM_MSG] == null) {
            if (!AnalysisCmdFile()) {
                return "";
            }
        }
        return packageAlarmMsgUpdate(strCmd[ALARM_MSG], errNum);
    }

    /*获取周期设置命令*/
    private String getJobPeriodCmd(String jobStr, int intervalTime) {
        if (strCmd[JOB_PERIOD] == null) {
            if (!AnalysisCmdFile()) {
                return "";
            }
        }
        return packageJobPeriodUpdate(strCmd[JOB_PERIOD], jobStr, intervalTime);
    }

    /*获取量程选择状态命令*/
    private String getRangeSelectCmd(String compName, int range, float LCL, float LCH) {
        if (strCmd[RANGE_SELECT] == null) {
            if (!AnalysisCmdFile()) {
                return "";
            }
        }
        return packageRangeSelectUpdate(strCmd[RANGE_SELECT], compName, range, LCL, LCH);
    }

    /*获取消解时长命令*/
    private String getDigestionTimeCmd(int time) {
        if (strCmd[DIGESTION_TIME] == null) {
            if (!AnalysisCmdFile()) {
                return "";
            }
        }
        return packageDigestionTimeUpdate(strCmd[DIGESTION_TIME], time);
    }

    /*获取消解温度命令*/
    private String getDigestionTemperatureCmd(int temperature) {
        if (strCmd[DIGESTION_TEMPERATURE] == null) {
            if (!AnalysisCmdFile()) {
                return "";
            }
        }
        return packageDigestionTemperatureUpdate(strCmd[DIGESTION_TEMPERATURE], temperature);
    }

    /*获取显色时长命令*/
    private String getColorTimeCmd(int time) {
        if (strCmd[COLORTIME] == null) {
            if (!AnalysisCmdFile()) {
                return "";
            }
        }
        return packageColorTimeUpdate(strCmd[COLORTIME], time);
    }

    /*获取显色温度命令*/
    private String getColorTempCmd(int time) {
        if (strCmd[COLORTEMP] == null) {
            if (!AnalysisCmdFile()) {
                return "";
            }
        }
        return packageColorTempUpdate(strCmd[COLORTEMP], time);
    }

    /*获取报警上下限命令*/
    private String getAlarmLimitCmd(String compName, float up, float down) {
        if (strCmd[ALARM_LIMIT] == null) {
            if (!AnalysisCmdFile()) {
                return "";
            }
        }
        return packageAlarmLimitUpdate(strCmd[ALARM_LIMIT], compName, up, down);
    }

    /*获取量程信息命令*/
    private String getRangeInfoCmd(String compName, float l, float h) {
        if (strCmd[RANGE_INFO] == null) {
            if (!AnalysisCmdFile()) {
                return "";
            }
        }
        return packageRangeInfoUpdate(strCmd[RANGE_INFO], compName, l, h);
    }

    /*获取K2CR2O7浓度命令*/
    private String getK2CR2O7CocCmd(String compName, float coc) {
        if (strCmd[K2CR2O7_COC] == null) {
            if (!AnalysisCmdFile()) {
                return "";
            }
        }
        return packageK2CR2O7CocUpdate(strCmd[K2CR2O7_COC], compName, coc);
    }

    /*获取自动校准命令*/
    private String getAutoCalibrationCmd(int day, int hour) {
        if (strCmd[AUTO_CALIBRATION] == null) {
            if (!AnalysisCmdFile()) {
                return "";
            }
        }
        return packageAutoCalibrationUpdate(strCmd[AUTO_CALIBRATION], day, hour);
    }

    /*获取自动清洗参数命令*/
    private String getAutoCleanCmd(String compName, int time) {
        if (strCmd[AUTO_CLAN] == null) {
            if (!AnalysisCmdFile()) {
                return "";
            }
        }
        return packageAutoCleanUpdate(strCmd[AUTO_CLAN], time);
    }

    /*获取系统时间命令*/
    private String getSysTimeCmd() {
        if (strCmd[SYS_TIME] == null) {
            if (!AnalysisCmdFile()) {
                return "";
            }
        }
        return packageSysTimeUpdate(strCmd[SYS_TIME]);
    }

    /***********************************************************************************************************************************/

    /*上传系统时间命令封装*/
    private String packageSysTimeUpdate(String cmd) {
        String[] StrOld = {"$MN$", "$T1$", "XXXX", "CRC"};
        String[] str = new String[1];
        DataStruct DataTime = getNowSysTime();
        str[0] = getDataTime(DataTime.getYear(), DataTime.getMonth(), DataTime.getDay(), DataTime.getHour(), DataTime.getMin(), DataTime.getSec());
        cmd = replaceTempData(cmd, StrOld, 0, getMN(), 0, 1);
        cmd = replaceTempData(cmd, StrOld, 1, str, 0, 1);
        if (cmd.contains("PolId=$PolId$") && strComponent.get(1).length > 0) {
            cmd = cmd.replace("$PolId$", getCode(strComponent.get(1)[0], strCode));
        }

        StrNew[0] = iDataFormat(String.valueOf(getDataCmd(cmd, "ST", "&&").length()), 4);
        StrNew[1] = getCrc16(getDataCmd(cmd, "ST", "&&"), findCrc(cmd));
        StrOld[3] = cmd.substring((cmd.lastIndexOf("&&") + 2), (cmd.lastIndexOf("&&") + 6));
        cmd = replaceTempData(cmd, StrOld, 2, StrNew, 0, 2) + "\r\n";
        return cmd;
    }

    /*上传自动清洗参数命令封装*/
    private String packageAutoCleanUpdate(String cmd, int time) {
        String[] StrOld = {"$MN$", "$AWP$", "$T1$", "XXXX", "CRC"};
        String[] str = packageAutoCleanData(time);
        cmd = replaceTempData(cmd, StrOld, 0, getMN(), 0, 1);
        cmd = replaceTempData(cmd, StrOld, 1, str, 0, 2);
        if (cmd.contains("PolId=$PolId$") && strComponent.get(1).length > 0) {
            cmd = cmd.replace("$PolId$", getCode(strComponent.get(1)[0], strCode));
        }

        StrNew[0] = iDataFormat(String.valueOf(getDataCmd(cmd, "ST", "&&").length()), 4);
        StrNew[1] = getCrc16(getDataCmd(cmd, "ST", "&&"), findCrc(cmd));
        StrOld[4] = cmd.substring((cmd.lastIndexOf("&&") + 2), (cmd.lastIndexOf("&&") + 6));
        cmd = replaceTempData(cmd, StrOld, 3, StrNew, 0, 2) + "\r\n";
        return cmd;
    }

    /*上传自动校准参数命令封装*/
    private String packageAutoCalibrationUpdate(String cmd, int day, int hour) {
        String[] StrOld = {"$MN$", "$ACP$", "$ACT$", "$T1$", "XXXX", "CRC"};
        String[] str = packageAutoCalibrationData(day, hour);
        cmd = replaceTempData(cmd, StrOld, 0, getMN(), 0, 1);
        cmd = replaceTempData(cmd, StrOld, 1, str, 0, 3);
        if (cmd.contains("PolId=$PolId$") && strComponent.get(1).length > 0) {
            cmd = cmd.replace("$PolId$", getCode(strComponent.get(1)[0], strCode));
        }

        StrNew[0] = iDataFormat(String.valueOf(getDataCmd(cmd, "ST", "&&").length()), 4);
        StrNew[1] = getCrc16(getDataCmd(cmd, "ST", "&&"), findCrc(cmd));
        StrOld[5] = cmd.substring((cmd.lastIndexOf("&&") + 2), (cmd.lastIndexOf("&&") + 6));
        cmd = replaceTempData(cmd, StrOld, 4, StrNew, 0, 2) + "\r\n";
        return cmd;
    }

    /*上传K2CR2O7浓度命令封装*/
    private String packageK2CR2O7CocUpdate(String cmd, String compName, float coc) {
        String[] StrOld = {"$MN$", "$K2Cr2o7$", "$T1$", "XXXX", "CRC"};
        String[] str = packageK2CR2O7CocData(compName, coc);
        cmd = replaceTempData(cmd, StrOld, 0, getMN(), 0, 1);
        cmd = replaceTempData(cmd, StrOld, 1, str, 0, 2);
        if (cmd.contains("PolId=$PolId$")) {
            cmd = cmd.replace("$PolId$", getCode(compName, strCode));
        }

        StrNew[0] = iDataFormat(String.valueOf(getDataCmd(cmd, "ST", "&&").length()), 4);
        StrNew[1] = getCrc16(getDataCmd(cmd, "ST", "&&"), findCrc(cmd));
        StrOld[4] = cmd.substring((cmd.lastIndexOf("&&") + 2), (cmd.lastIndexOf("&&") + 6));
        cmd = replaceTempData(cmd, StrOld, 3, StrNew, 0, 2) + "\r\n";
        return cmd;
    }

    /*上传量程信息命令封装*/
    private String packageRangeInfoUpdate(String cmd, String compName, float l, float h) {
        String[] StrOld = {"$MN$", "$RAL$", "$RAH$", "$T1$", "XXXX", "CRC"};
        String[] str = packageRangeInfoData(compName, l, h);
        cmd = replaceTempData(cmd, StrOld, 0, getMN(), 0, 1);
        cmd = replaceTempData(cmd, StrOld, 1, str, 0, 3);
        if (cmd.contains("PolId=$PolId$")) {
            cmd = cmd.replace("$PolId$", getCode(compName, strCode));
        }

        StrNew[0] = iDataFormat(String.valueOf(getDataCmd(cmd, "ST", "&&").length()), 4);
        StrNew[1] = getCrc16(getDataCmd(cmd, "ST", "&&"), findCrc(cmd));
        StrOld[5] = cmd.substring((cmd.lastIndexOf("&&") + 2), (cmd.lastIndexOf("&&") + 6));
        cmd = replaceTempData(cmd, StrOld, 4, StrNew, 0, 2) + "\r\n";
        return cmd;
    }

    /*上传报警上下限命令封装*/
    private String packageAlarmLimitUpdate(String cmd, String compName, float up, float down) {
        String[] StrOld = {"$MN$", "$ALL$", "$ALH$", "$T1$", "XXXX", "CRC"};
        String[] str = packageAlarmLimitData(compName, up, down);
        cmd = replaceTempData(cmd, StrOld, 0, getMN(), 0, 1);
        cmd = replaceTempData(cmd, StrOld, 1, str, 0, 3);
        if (cmd.contains("PolId=$PolId$")) {
            cmd = cmd.replace("$PolId$", getCode(compName, strCode));
        }

        StrNew[0] = iDataFormat(String.valueOf(getDataCmd(cmd, "ST", "&&").length()), 4);
        StrNew[1] = getCrc16(getDataCmd(cmd, "ST", "&&"), findCrc(cmd));
        StrOld[5] = cmd.substring((cmd.lastIndexOf("&&") + 2), (cmd.lastIndexOf("&&") + 6));
        cmd = replaceTempData(cmd, StrOld, 4, StrNew, 0, 2) + "\r\n";
        return cmd;
    }

    /*上传显色时长命令封装*/
    private String packageColorTimeUpdate(String cmd, int time) {
        String[] StrOld = {"$MN$", "$CTI$", "$T1$", "XXXX", "CRC"};
        String[] str = packageColorTimeData(time);
        cmd = replaceTempData(cmd, StrOld, 0, getMN(), 0, 1);
        cmd = replaceTempData(cmd, StrOld, 1, str, 0, 2);
        if (cmd.contains("PolId=$PolId$") && strComponent.get(1).length > 0) {
            cmd = cmd.replace("$PolId$", getCode(strComponent.get(1)[0], strCode));
        }

        StrNew[0] = iDataFormat(String.valueOf(getDataCmd(cmd, "ST", "&&").length()), 4);
        StrNew[1] = getCrc16(getDataCmd(cmd, "ST", "&&"), findCrc(cmd));
        StrOld[4] = cmd.substring((cmd.lastIndexOf("&&") + 2), (cmd.lastIndexOf("&&") + 6));
        cmd = replaceTempData(cmd, StrOld, 3, StrNew, 0, 2) + "\r\n";
        return cmd;
    }

    /*上传显色温度命令封装*/
    private String packageColorTempUpdate(String cmd, int time) {
        String[] StrOld = {"$MN$", "$CTE$", "$T1$", "XXXX", "CRC"};
        String[] str = packageColorTempData(time);
        cmd = replaceTempData(cmd, StrOld, 0, getMN(), 0, 1);
        cmd = replaceTempData(cmd, StrOld, 1, str, 0, 2);
        if (cmd.contains("PolId=$PolId$") && strComponent.get(1).length > 0) {
            cmd = cmd.replace("$PolId$", getCode(strComponent.get(1)[0], strCode));
        }

        StrNew[0] = iDataFormat(String.valueOf(getDataCmd(cmd, "ST", "&&").length()), 4);
        StrNew[1] = getCrc16(getDataCmd(cmd, "ST", "&&"), findCrc(cmd));
        StrOld[4] = cmd.substring((cmd.lastIndexOf("&&") + 2), (cmd.lastIndexOf("&&") + 6));
        cmd = replaceTempData(cmd, StrOld, 3, StrNew, 0, 2) + "\r\n";
        return cmd;
    }

    /*上传消解温度命令封装*/
    private String packageDigestionTemperatureUpdate(String cmd, int temperature) {
        String[] StrOld = {"$MN$", "$DTE$", "$T1$", "XXXX", "CRC"};
        String[] str = packageDigestionTemperatureData(temperature);
        cmd = replaceTempData(cmd, StrOld, 0, getMN(), 0, 1);
        cmd = replaceTempData(cmd, StrOld, 1, str, 0, 2);
        if (cmd.contains("PolId=$PolId$") && strComponent.get(1).length > 0) {
            cmd = cmd.replace("$PolId$", getCode(strComponent.get(1)[0], strCode));
        }

        StrNew[0] = iDataFormat(String.valueOf(getDataCmd(cmd, "ST", "&&").length()), 4);
        StrNew[1] = getCrc16(getDataCmd(cmd, "ST", "&&"), findCrc(cmd));
        StrOld[4] = cmd.substring((cmd.lastIndexOf("&&") + 2), (cmd.lastIndexOf("&&") + 6));
        cmd = replaceTempData(cmd, StrOld, 3, StrNew, 0, 2) + "\r\n";
        return cmd;
    }

    /*上传消解时长命令封装*/
    private String packageDigestionTimeUpdate(String cmd, int time) {
        String[] StrOld = {"$MN$", "$DTI$", "$T1$", "XXXX", "CRC"};
        String[] str = packageDigestionTimeData(time);
        cmd = replaceTempData(cmd, StrOld, 0, getMN(), 0, 1);
        cmd = replaceTempData(cmd, StrOld, 1, str, 0, 2);
        if (cmd.contains("PolId=$PolId$") && strComponent.get(1).length > 0) {
            cmd = cmd.replace("$PolId$", getCode(strComponent.get(1)[0], strCode));
        }

        StrNew[0] = iDataFormat(String.valueOf(getDataCmd(cmd, "ST", "&&").length()), 4);
        StrNew[1] = getCrc16(getDataCmd(cmd, "ST", "&&"), findCrc(cmd));
        StrOld[4] = cmd.substring((cmd.lastIndexOf("&&") + 2), (cmd.lastIndexOf("&&") + 6));
        cmd = replaceTempData(cmd, StrOld, 3, StrNew, 0, 2) + "\r\n";
        return cmd;
    }

    /*读取量程选择状态封装*/
    private String packageRangeSelectUpdate(String cmd, String compName, int range, float LCL, float LCH) {
        String[] StrOld = {"$MN$", "$RA$", "$MRL$", "$MRH$", "$T1$", "XXXX", "CRC"};
        String[] str = packageRangeSelectData(compName, range, LCL, LCH);
        cmd = replaceTempData(cmd, StrOld, 0, getMN(), 0, 1);
        cmd = replaceTempData(cmd, StrOld, 1, str, 0, 4);
        if (cmd.contains("PolId=$PolId$")) {
            cmd = cmd.replace("$PolId$", getCode(compName, strCode));
        }

        StrNew[0] = iDataFormat(String.valueOf(getDataCmd(cmd, "ST", "&&").length()), 4);
        StrNew[1] = getCrc16(getDataCmd(cmd, "ST", "&&"), findCrc(cmd));
        StrOld[6] = cmd.substring((cmd.lastIndexOf("&&") + 2), (cmd.lastIndexOf("&&") + 6));
        cmd = replaceTempData(cmd, StrOld, 5, StrNew, 0, 2) + "\r\n";
        return cmd;
    }

    /*做样周期命令封装*/
    private String packageJobPeriodUpdate(String cmd, String jobStr, int intervalTime) {
        String[] StrOld = {"$MN$", "$OTH$", "$IST$", "$T1$", "XXXX", "CRC"};
        /*周期直接替换*/
        cmd = cmd.replace(StrOld[1], jobStr);

        String[] str = packageJobPeriodData(intervalTime);
        cmd = replaceTempData(cmd, StrOld, 0, getMN(), 0, 1);
        cmd = replaceTempData(cmd, StrOld, 2, str, 0, 2);
        if (cmd.contains("PolId=$PolId$") && strComponent.get(1).length > 0) {
            cmd = cmd.replace("$PolId$", getCode(strComponent.get(1)[0], strCode));
        }

        StrNew[0] = iDataFormat(String.valueOf(getDataCmd(cmd, "ST", "&&").length()), 4);
        StrNew[1] = getCrc16(getDataCmd(cmd, "ST", "&&"), findCrc(cmd));
        StrOld[5] = cmd.substring((cmd.lastIndexOf("&&") + 2), (cmd.lastIndexOf("&&") + 6));
        cmd = replaceTempData(cmd, StrOld, 4, StrNew, 0, 2) + "\r\n";
        return cmd;
    }

    /*上传报警记录命令封装*/
    private String packageAlarmMsgUpdate(String cmd, int errNum) {
        String[] StrOld = {"$MN$", "$AL$", "$T1$", "XXXX", "CRC"};
        String[] str = packageAlarmMsgData(errNum);
        cmd = replaceTempData(cmd, StrOld, 0, getMN(), 0, 1);
        cmd = replaceTempData(cmd, StrOld, 1, str, 0, 2);
        if (cmd.contains("PolId=$PolId$") && strComponent.get(1).length > 0) {
            cmd = cmd.replace("$PolId$", getCode(strComponent.get(1)[0], strCode));
        }

        StrNew[0] = iDataFormat(String.valueOf(getDataCmd(cmd, "ST", "&&").length()), 4);
        StrNew[1] = getCrc16(getDataCmd(cmd, "ST", "&&"), findCrc(cmd));
        StrOld[4] = cmd.substring((cmd.lastIndexOf("&&") + 2), (cmd.lastIndexOf("&&") + 6));
        cmd = replaceTempData(cmd, StrOld, 3, StrNew, 0, 2) + "\r\n";
        return cmd;
    }

    /*上传预开水泵命令封装*/
    private String packagePumpTimeUpdate(String cmd, int time) {
        String[] StrOld = {"$MN$", "$PT$", "$T1$", "XXXX", "CRC"};
        String[] str = packagePumpTimeData(time);
        cmd = replaceTempData(cmd, StrOld, 0, getMN(), 0, 1);
        cmd = replaceTempData(cmd, StrOld, 1, str, 0, 2);
        if (cmd.contains("PolId=$PolId$") && strComponent.get(1).length > 0) {
            cmd = cmd.replace("$PolId$", getCode(strComponent.get(1)[0], strCode));
        }

        StrNew[0] = iDataFormat(String.valueOf(getDataCmd(cmd, "ST", "&&").length()), 4);
        StrNew[1] = getCrc16(getDataCmd(cmd, "ST", "&&"), findCrc(cmd));
        StrOld[4] = cmd.substring((cmd.lastIndexOf("&&") + 2), (cmd.lastIndexOf("&&") + 6));
        cmd = replaceTempData(cmd, StrOld, 3, StrNew, 0, 2) + "\r\n";
        return cmd;

    }

    /*上传工作曲线系数命令封装*/
    private String packageAdjustKbUpdate(String cmd, String compName, float k) {
        String[] StrOld = {"$MN$", "$AK$", "$T1$", "XXXX", "CRC"};
        String[] str = packageAdjustKbData(compName, k);
        cmd = replaceTempData(cmd, StrOld, 0, getMN(), 0, 1);
        cmd = replaceTempData(cmd, StrOld, 1, str, 0, 2);
        if (cmd.contains("PolId=$PolId$")) {
            cmd = cmd.replace("$PolId$", getCode(compName, strCode));
        }

        StrNew[0] = iDataFormat(String.valueOf(getDataCmd(cmd, "ST", "&&").length()), 4);
        StrNew[1] = getCrc16(getDataCmd(cmd, "ST", "&&"), findCrc(cmd));
        StrOld[4] = cmd.substring((cmd.lastIndexOf("&&") + 2), (cmd.lastIndexOf("&&") + 6));
        cmd = replaceTempData(cmd, StrOld, 3, StrNew, 0, 2) + "\r\n";
        return cmd;
    }

    /*上传量程系数命令封装*/
    private String packageCoeKbUpdate(String cmd, String compName, float fk1, float fb1, float fk2, float fb2, float fk3, float fb3) {
        String[] StrOld = {"$MN$", "$RCK1$", "$RCB1$", "$RCK2$", "$RCB2$", "$RCK3$", "$RCB3$", "$T1$", "XXXX", "CRC"};
        String[] str = packageCoeKbData(compName, fk1, fb1, fk2, fb2, fk3, fb3);
        cmd = replaceTempData(cmd, StrOld, 0, getMN(), 0, 1);
        cmd = replaceTempData(cmd, StrOld, 1, str, 0, 7);
        if (cmd.contains("PolId=$PolId$")) {
            cmd = cmd.replace("$PolId$", getCode(compName, strCode));
        }

        StrNew[0] = iDataFormat(String.valueOf(getDataCmd(cmd, "ST", "&&").length()), 4);
        StrNew[1] = getCrc16(getDataCmd(cmd, "ST", "&&"), findCrc(cmd));
        StrOld[9] = cmd.substring((cmd.lastIndexOf("&&") + 2), (cmd.lastIndexOf("&&") + 6));
        cmd = replaceTempData(cmd, StrOld, 8, StrNew, 0, 2) + "\r\n";
        return cmd;
    }

    /*上传量程系数命令封装*/
    private String packageCoeKbUpdate(String cmd, String compName, float fk, float fb) {
        String[] StrOld = {"$MN$", "$RCK$", "$RCB$", "$T1$", "XXXX", "CRC"};
        String[] str = packageCoeKbData(compName, fk, fb);
        cmd = replaceTempData(cmd, StrOld, 0, getMN(), 0, 1);
        cmd = replaceTempData(cmd, StrOld, 1, str, 0, 3);
        if (cmd.contains("PolId=$PolId$")) {
            cmd = cmd.replace("$PolId$", getCode(compName, strCode));
        }

        StrNew[0] = iDataFormat(String.valueOf(getDataCmd(cmd, "ST", "&&").length()), 4);
        StrNew[1] = getCrc16(getDataCmd(cmd, "ST", "&&"), findCrc(cmd));
        StrOld[5] = cmd.substring((cmd.lastIndexOf("&&") + 2), (cmd.lastIndexOf("&&") + 6));
        cmd = replaceTempData(cmd, StrOld, 4, StrNew, 0, 2) + "\r\n";
        return cmd;
    }

    /*上传修正因子命令封装*/
    private String packageCorrectionFactorKbUpdate(String cmd, String compName, float fk, float fb) {
        String[] StrOld = {"$MN$", "$CFk$", "$CFb$", "$T1$", "XXXX", "CRC"};
        String[] str = packageCoeKbData(compName, fk, fb);
        cmd = replaceTempData(cmd, StrOld, 0, getMN(), 0, 1);
        cmd = replaceTempData(cmd, StrOld, 1, str, 0, 3);
        if (cmd.contains("PolId=$PolId$")) {
            cmd = cmd.replace("$PolId$", getCode(compName, strCode));
        }

        StrNew[0] = iDataFormat(String.valueOf(getDataCmd(cmd, "ST", "&&").length()), 4);
        StrNew[1] = getCrc16(getDataCmd(cmd, "ST", "&&"), findCrc(cmd));
        StrOld[5] = cmd.substring((cmd.lastIndexOf("&&") + 2), (cmd.lastIndexOf("&&") + 6));
        cmd = replaceTempData(cmd, StrOld, 4, StrNew, 0, 2) + "\r\n";
        return cmd;
    }

    /*上传吸光度能量命令封装*/
    private String packageAbsorbancyDataUpdate(String cmd, DataStruct DataInfo) {
        String[] StrOld = {"$MN$", "$T1$", "$C1$-Rtd=", "$C1$-Flag=", "$C1$-DataType=", "XXXX", "CRC"};
        boolean bCode = cmd.contains("$C1$-DataType=");
        if (!bCode) {
            StrOld[4] = "DataType=";
        }
        String[] str = packageAbsorbData(DataInfo, strCode, bCode);
        cmd = replaceTempData(cmd, StrOld, 0, getMN(), 0, 1);
        cmd = replaceTempData(cmd, StrOld, 1, str, 0, 4);
        if (cmd.contains("PolId=$PolId$")) {
            cmd = cmd.replace("$PolId$", getCode(DataInfo.getType(), strCode));
        }

        StrNew[3] = iDataFormat(String.valueOf(getDataCmd(cmd, "ST", "&&").length()), 4);
        StrNew[4] = getCrc16(getDataCmd(cmd, "ST", "&&"), findCrc(cmd));
        StrOld[6] = cmd.substring((cmd.lastIndexOf("&&") + 2), (cmd.lastIndexOf("&&") + 6));
        cmd = replaceTempData(cmd, StrOld, 5, StrNew, 3, 2) + "\r\n";
        return cmd;
    }

    //标样2浓度命令封装
    private String packageSpanCocUpdate(String cmd, DataStruct DataInfo) {
        String[] StrOld = {"$MN$", "$SC$", "$A$", "$T1$", "XXXX", "CRC"};
        String[] str = packageSpanData(DataInfo);
        cmd = replaceTempData(cmd, StrOld, 0, getMN(), 0, 1);
        cmd = replaceTempData(cmd, StrOld, 1, str, 0, 3);
        if (cmd.contains("PolId=$PolId$")) {
            cmd = cmd.replace("$PolId$", getCode(DataInfo.getType(), strCode));
        }

        StrNew[0] = iDataFormat(String.valueOf(getDataCmd(cmd, "ST", "&&").length()), 4);
        StrNew[1] = getCrc16(getDataCmd(cmd, "ST", "&&"), findCrc(cmd));
        StrOld[5] = cmd.substring((cmd.lastIndexOf("&&") + 2), (cmd.lastIndexOf("&&") + 6));
        cmd = replaceTempData(cmd, StrOld, 4, StrNew, 0, 2) + "\r\n";
        return cmd;
    }

    //标样1浓度命令封装
    private String packageSpan1CocUpdate(String cmd, DataStruct DataInfo) {
        String[] StrOld = {"$MN$", "$GS$", "$A$", "$T1$", "XXXX", "CRC"};
        String[] str = packageSpanData(DataInfo);
        cmd = replaceTempData(cmd, StrOld, 0, getMN(), 0, 1);
        cmd = replaceTempData(cmd, StrOld, 1, str, 0, 3);
        if (cmd.contains("PolId=$PolId$")) {
            cmd = cmd.replace("$PolId$", getCode(DataInfo.getType(), strCode));
        }

        StrNew[0] = iDataFormat(String.valueOf(getDataCmd(cmd, "ST", "&&").length()), 4);
        StrNew[1] = getCrc16(getDataCmd(cmd, "ST", "&&"), findCrc(cmd));
        StrOld[5] = cmd.substring((cmd.lastIndexOf("&&") + 2), (cmd.lastIndexOf("&&") + 6));
        cmd = replaceTempData(cmd, StrOld, 4, StrNew, 0, 2) + "\r\n";
        return cmd;
    }

    // 浓度上传命令封装
    private static String packageMeaDataUpdate(String cmd, DataStruct DataInfo, boolean isHistory) {
        String dataTime = "";
        String[] StrHeadType = {"$MN$", "XXXX"};
        String[] StrDataType = {"$T1$", "$C1$-Rtd=", "$C1$-Flag=", "$C1$-DataType="};
        String[] StrEnd = {"CRC"};
        String[] strings = cmd.split("@");

        boolean bCode = cmd.contains("$C1$-DataType=");
        if (!bCode) {
            StrDataType[3] = "DataType=";
        }

        String[] dataStr = new String[strComponent.get(1).length];
        DataStruct[] dataStruct = new DataStruct[strComponent.get(1).length];

        if (isHistory) {
            String flowName;
            /*所有元素数据段内容组装*/
            for (int i = 0; i < strComponent.get(1).length; i++) {
                if (getPublicConfigData("CAL_DATA_UPDATE").equals("true")) {
                    flowName = null;
                } else {
                    flowName = context.getString(R.string.ZY);
                }
                dataStruct[i] = getLastHistory(strComponent.get(1)[i], flowName);
                dataStr[i] = "";
                if (dataStruct[i].getType() != null && (getModePermissions(strComponent.get(1)[i], "历史数据上传"))) {
                    String[] temp = packageMeaData(dataStruct[i], strCode, bCode);
                    dataStr[i] = replaceTempData(strings[1], StrDataType, 0, temp, 0, 4);
                    if (cmd.contains("PolId=$PolId$")) {
                        dataStr[i] = dataStr[i].replace("$PolId$", getCode(DataInfo.getType(), strCode));
                        //dataStr[i] += ",PolId=" + getCode(dataStruct[i].getType(), strCode);
                    }
                    if (!dataTime.equals("")) {
                        //判断时间是否是最新的
                        dataTime = stringTimeCompare(dataTime, temp[0]);
                    } else {
                        dataTime = temp[0];
                    }
                }
            }
        } else {
            for (int i = 0; i < strComponent.get(1).length; i++) {
                dataStr[i] = "";
                DataInfo.setType(strComponent.get(1)[i]);
                String[] temp = packageMeaData(DataInfo, strCode, bCode);
                dataStr[i] = replaceTempData(strings[1], StrDataType, 0, temp, 0, 4);
                if (cmd.contains("PolId=$PolId$")) {
                    dataStr[i] = dataStr[i].replace("$PolId$", getCode(DataInfo.getType(), strCode));
                    //dataStr[i] += ",PolId=" + getCode(dataStruct[i].getType(), strCode);
                }
                if (!dataTime.equals("")) {
                    //判断时间是否是最新的
                    dataTime = stringTimeCompare(dataTime, temp[0]);
                } else {
                    dataTime = temp[0];
                }
            }
        }

        /*组装常规仪表不同元素数据字符串*/
        String strData = "";
        for (String s : dataStr) {
            if (!s.equals("")) {
                strData += s;
                strData += ";";
            }
        }
        if (strData.equals("")) {
            return "";
        }
        /*帧头组装*/
        /*MN号*/
        String[] strMn = new String[1];
        if (!getPublicConfigData("ORG_ADDR").equals("")) {
            strMn[0] = getPublicConfigData("ORG_ADDR") + getPublicConfigData("DEV_ADDR");
        } else {
            strMn[0] = MN;
        }
        cmd = replaceTempData(strings[0], StrHeadType, 0, strMn, 0, 1);
        /*头+数据段+尾*/
        cmd += strData;
        /*兼容只有一个dataTime的情况*/
        if ((!dataTime.equals("")) && cmd.contains("$T1$")) {
            cmd = cmd.replace("$T1$", dataTime);
        }
        /*将数据内容区的最后一个符号";"去除*/
        cmd = cmd.substring(0, cmd.length() - 1);
        cmd += strings[2];
        /*命令长度*/
        String[] strLen = new String[1];
        strLen[0] = iDataFormat(String.valueOf(getDataCmd(cmd, "ST", "&&").length()), 4);
        cmd = replaceTempData(cmd, StrHeadType, 1, strLen, 0, 1);
        /*获取CRC*/
        String[] strCrc = new String[1];
        strCrc[0] = getCrc16(getDataCmd(cmd, "ST", "&&"), findCrc(cmd));
        StrEnd[0] = cmd.substring((cmd.lastIndexOf("&&") + 2), (cmd.lastIndexOf("&&") + 6));
        /*替换CRC*/
        cmd = replaceTempData(cmd, StrEnd, 0, strCrc, 0, 1) + "\r\n";
        String gbDataCmd = cmd;
        return cmd;
    }

    /***********************************************************************************************************************************/

    /*自动清洗参数封装*/
    private String[] packageAutoCleanData(int time) {
        /*都是封装一个int 的数据*/
        return packagePumpTimeData(time);
    }


    /*自动校准周期参数封装*/
    private String[] packageAutoCalibrationData(int day, int hour) {
        /*都是封装一个int 的数据*/
        String[] str = new String[3];
        str[0] = Integer.toString(day);
        str[1] = Integer.toString(hour);
        DataStruct DataTime = getNowSysTime();
        str[2] = getDataTime(DataTime.getYear(), DataTime.getMonth(), DataTime.getDay(), DataTime.getHour(), DataTime.getMin(), DataTime.getSec());
        return str;
    }

    /*K2CR2O7浓度数据封装*/
    private String[] packageK2CR2O7CocData(String compName, float coc) {
        String[] str = new String[2];
        str[0] = dataFormat(coc, parseInt(getConfigData(compName, "YXWS")));
        DataStruct DataTime = getNowSysTime();
        str[1] = getDataTime(DataTime.getYear(), DataTime.getMonth(), DataTime.getDay(), DataTime.getHour(), DataTime.getMin(), DataTime.getSec());
        return str;
    }

    /*量程信息数据封装*/
    private String[] packageRangeInfoData(String compName, float up, float down) {
        String[] str = new String[3];
        int point = parseInt(getConfigData(compName, "YXWS"));
        str[0] = dataFormat(up, point);
        str[1] = dataFormat(down, point);
        DataStruct DataTime = getNowSysTime();
        str[2] = getDataTime(DataTime.getYear(), DataTime.getMonth(), DataTime.getDay(), DataTime.getHour(), DataTime.getMin(), DataTime.getSec());
        return str;
    }

    /*报警上下限 数据封装*/
    private String[] packageAlarmLimitData(String compName, float up, float down) {
        String[] str = new String[3];
        int point = parseInt(getConfigData(compName, "YXWS"));
        str[0] = dataFormat(up, point);
        str[1] = dataFormat(down, point);
        DataStruct DataTime = getNowSysTime();
        str[2] = getDataTime(DataTime.getYear(), DataTime.getMonth(), DataTime.getDay(), DataTime.getHour(), DataTime.getMin(), DataTime.getSec());
        return str;
    }

    /*显色时长封装*/
    private String[] packageColorTimeData(int time) {
        /*都是封装一个int 的数据*/
        return packagePumpTimeData(time);
    }

    /*显色温度封装*/
    private String[] packageColorTempData(int temp) {
        /*都是封装一个int 的数据*/
        return packagePumpTimeData(temp);
    }

    /*消解温度封装*/
    private String[] packageDigestionTemperatureData(int temperature) {
        /*都是封装一个int 的数据*/
        return packagePumpTimeData(temperature);
    }

    /*消解时长封装*/
    private String[] packageDigestionTimeData(int time) {
        /*都是封装一个int 的数据*/
        return packagePumpTimeData(time);
    }

    /*量程选择封装*/
    private String[] packageRangeSelectData(int range) {
        /*都是封装一个int 的数据*/
        return packagePumpTimeData(range);
    }

    /*周期设置参数封装*/
    private String[] packageJobPeriodData(int intervalTime) {
        /*都是封装一个int 的数据*/
        return packagePumpTimeData(intervalTime);
    }

    /*报警命令数据封装*/
    private String[] packageAlarmMsgData(int errNum) {
        int iErrNum = 0;
        if ((errNum >= 1 && errNum <= 4) || (errNum >= 8 && errNum <= 10) || (errNum >= 12 && errNum <= 20)
                || (errNum >= 21 && errNum <= 24) || (errNum >= 28 && errNum <= 30) || (errNum >= 32 && errNum <= 40)
                || (errNum >= 61 && errNum <= 64) || (errNum >= 68 && errNum <= 70) || (errNum >= 72 && errNum <= 80)
                || (errNum >= 500 && errNum <= 505)) {
            //进试剂超时
            iErrNum = 1;
        } else if (errNum == 7 || errNum == 27 || errNum == 47 || errNum == 67 || errNum == 401 || errNum == 404 || errNum == 405
                || errNum == 407 || errNum == 408 || (errNum >= 410 && errNum <= 412)) {
            //缺水样
            iErrNum = 2;
        } else if (errNum == 5 || errNum == 25 || errNum == 45 || errNum == 65 || errNum == 402 || errNum == 403 || errNum == 406
                || errNum == 409) {
            //缺零样
            iErrNum = 3;
        } else if (errNum == 6 || errNum == 26 || errNum == 46 || errNum == 66) {
            //缺标样
            iErrNum = 4;
        } else if (errNum == 89 || errNum == 102 || errNum == 103) {
            //温度异常
            iErrNum = 5;
        } else if (errNum >= 96 && errNum <= 99) {
            //通讯异常
            iErrNum = 6;
        } else if (errNum == 94) {
            //漏液保护
            iErrNum = 7;
        } else if (errNum == 95 || errNum == 415) {
            //光谱异常
            iErrNum = 8;
        } else if (errNum == 81 || errNum == 84 || errNum == 85 || (errNum >= 90 && errNum <= 93) || errNum == 100 || errNum == 101 || (errNum >= 104 && errNum <= 110)
                || errNum == 155 || errNum == 156 || (errNum >= 160 && errNum <= 162) || (errNum >= 240 && errNum <= 244) || (errNum >= 250 && errNum <= 260)
                || (errNum >= 510 && errNum <= 512) || errNum == 529 || (errNum >= 531 && errNum <= 533) || (errNum >= 536 && errNum <= 537) || (errNum >= 541 && errNum <= 562)
                || (errNum >= 570 && errNum <= 572) || errNum == 576 || (errNum >= 578 && errNum <= 579) || (errNum >= 581 && errNum <= 583) || errNum == 585) {
            //其他异常
            iErrNum = 12;
        } else {

        }
        /*都是封装一个int 的数据*/
        return packagePumpTimeData(iErrNum);
    }

    /* 预开水泵时间数据封装*/
    private String[] packagePumpTimeData(int time) {
        String[] str = new String[2];
        str[0] = Integer.toString(time);
        DataStruct DataTime = getNowSysTime();
        str[1] = getDataTime(DataTime.getYear(), DataTime.getMonth(), DataTime.getDay(), DataTime.getHour(), DataTime.getMin(), DataTime.getSec());
        return str;
    }

    // 工作曲线KB 数据封装
    private String[] packageAdjustKbData(String compName, float fk) {
        String[] str = new String[2];
        int point = parseInt(getConfigData(compName, "YXWS"));
        str[0] = dataFormat(fk, point);
        DataStruct DataTime = getNowSysTime();
        str[1] = getDataTime(DataTime.getYear(), DataTime.getMonth(), DataTime.getDay(), DataTime.getHour(), DataTime.getMin(), DataTime.getSec());
        return str;
    }

    // 量程KB 数据封装
    private String[] packageCoeKbData(String compName, float fk1, float fb1, float fk2, float fb2, float fk3, float fb3) {
        String[] str = new String[7];
        int point = parseInt(getConfigData(compName, "YXWS"));
        str[0] = dataFormat(fk1, point);
        str[1] = dataFormat(fb1, point);
        str[2] = dataFormat(fk2, point);
        str[3] = dataFormat(fb2, point);
        str[4] = dataFormat(fk3, point);
        str[5] = dataFormat(fb3, point);
        DataStruct DataTime = getNowSysTime();
        str[6] = getDataTime(DataTime.getYear(), DataTime.getMonth(), DataTime.getDay(), DataTime.getHour(), DataTime.getMin(), DataTime.getSec());
        return str;
    }

    // 量程KB 数据封装
    private String[] packageCoeKbData(String compName, float fk, float fb) {
        String[] str = new String[3];
        int point = parseInt(getConfigData(compName, "YXWS"));
        str[0] = dataFormat(fk, point);
        str[1] = dataFormat(fb, point);
        DataStruct DataTime = getNowSysTime();
        str[2] = getDataTime(DataTime.getYear(), DataTime.getMonth(), DataTime.getDay(), DataTime.getHour(), DataTime.getMin(), DataTime.getSec());
        return str;
    }

    // 标样浓度 数据封装
    private static String[] packageSpanData(DataStruct DataInfo) {
        String[] str = new String[3];
        str[0] = dataFormat(DataInfo.getData(), parseInt(getConfigData(DataInfo.getType(), "YXWS")));
        str[1] = dataFormat(DataInfo.getAi(), parseInt(getConfigData(DataInfo.getType(), "YXWS")));
        str[2] = getDataTime(DataInfo.getYear(), DataInfo.getMonth(), DataInfo.getDay(), DataInfo.getHour(), DataInfo.getMin(), DataInfo.getSec());
        return str;
    }

    // 组份 数据封装
    private static String[] packageMeaData(DataStruct DataInfo, String[] StrGbCode, boolean bCode) {
        String[] str = new String[4];
        str[0] = getDataTime(DataInfo.getYear(), DataInfo.getMonth(), DataInfo.getDay(), DataInfo.getHour(), DataInfo.getMin(), DataInfo.getSec());
        str[1] = getCode(DataInfo.getType(), StrGbCode) + "-Rtd=" + dataFormat(DataInfo.getData(), parseInt(getConfigData(DataInfo.getType(), "YXWS")));
        str[2] = getCode(DataInfo.getType(), StrGbCode) + "-Flag=" + getDevStatus(DataInfo.getType(), workState.get(DataInfo.getType()), doFlowing.get(DataInfo.getType()), DataInfo);
        /*if (DataInfo.getType().equals("TP") || DataInfo.getType().equals("TN")) {
            str[2] = getCode(DataInfo.getType(), StrGbCode) + "-Flag=" + getDevStatus(DataInfo.getType(), workState.get(DataInfo.getType()), doFlowing.get(DataInfo.getType()), DataInfo);
        } else {
            str[2] = getCode(DataInfo.getType(), StrGbCode) + "-Flag=" + getDevStatus(DataInfo.getType(), workState.get(DataInfo.getType()), doFlowing.get(DataInfo.getType()));
        }*/
        str[3] = (bCode ? (getCode(DataInfo.getType(), StrGbCode) + "-") : "") + "DataType=" + getDataType(DataInfo.getType(), DataInfo.getFlow());
        return str;
    }

    // 组份 吸光度数据封装
    private String[] packageAbsorbData(DataStruct DataInfo, String[] StrGbCode, boolean bCode) {
        String[] str = new String[4];
        str[0] = getDataTime(DataInfo.getYear(), DataInfo.getMonth(), DataInfo.getDay(), DataInfo.getHour(), DataInfo.getMin(), DataInfo.getSec());
        str[1] = getCode(DataInfo.getType(), StrGbCode) + "-Rtd=" + dataFormat(DataInfo.getAi(), parseInt(getConfigData(DataInfo.getType(), "YXWS")));
        str[2] = getCode(DataInfo.getType(), StrGbCode) + "-Flag=" + getDevStatus(DataInfo.getType(), workState.get(DataInfo.getType()), doFlowing.get(DataInfo.getType()), DataInfo);
        /*if (DataInfo.getType().equals("TP") || DataInfo.getType().equals("TN")) {
            str[2] = getCode(DataInfo.getType(), StrGbCode) + "-Flag=" + getDevStatus(DataInfo.getType(), workState.get(DataInfo.getType()), doFlowing.get(DataInfo.getType()), DataInfo);
        } else {
            str[2] = getCode(DataInfo.getType(), StrGbCode) + "-Flag=" + getDevStatus(DataInfo.getType(), workState.get(DataInfo.getType()), doFlowing.get(DataInfo.getType()));
        }*/
        str[3] = (bCode ? (getCode(DataInfo.getType(), StrGbCode) + "-") : "") + "DataType=" + getDataType(DataInfo.getType(), DataInfo.getFlow());
        return str;
    }

    // 读取量程选择状态
    private String[] packageRangeSelectData(String compName, int range, float LCL, float LCH) {
        String[] str = new String[4];
        int point = parseInt(getConfigData(compName, "YXWS"));
        str[0] = Integer.toString(range);
        str[1] = dataFormat(LCL, point);
        str[2] = dataFormat(LCH, point);
        DataStruct DataTime = getNowSysTime();
        str[3] = getDataTime(DataTime.getYear(), DataTime.getMonth(), DataTime.getDay(), DataTime.getHour(), DataTime.getMin(), DataTime.getSec());
        return str;
    }

    /***********************************************************************************************************************************/

    // 时间封装
    public static String getDataTime(int year, int month, int day, int hour, int min, int sec) {

        return year + iDataFormat(String.valueOf(month), 2)
                + iDataFormat(String.valueOf(day), 2)
                + iDataFormat(String.valueOf(hour), 2)
                + iDataFormat(String.valueOf(min), 2)
                + iDataFormat(String.valueOf(sec), 2);
    }

    // 查找是什么校验算法
    public static int findCrc(String cmd) {
        //天津力合协议算法
        /*if (cmd.contains("CRC1")) {
            return 1;
            //212协议算法
        } else if (cmd.contains("CRC2")) {
            return 2;
        } else {
            return 0;
        }*/
        if (getPublicConfigData("GB2005_CRC").equals("0")) {
            return 1;
        } else if (getPublicConfigData("GB2005_CRC").equals("1")) {
            return 2;
        } else {
            return 0;
        }
    }

    //校验算法计算
    public static String getCrc16(String data, int whoCrc) {
        String crc = "";
        switch (whoCrc) {
            case 1:
                crc = CRC16.calCrc16(data.getBytes(), 0, data.length());
                break;
            case 2:
                crc = CRC16.calCrc162(data.getBytes(), 0, data.length());
                break;
        }
        return iDataFormat(crc.toUpperCase(), 4, null);
    }

    // 获取数据长度
    public static String getDataCmd(String cmd, String st, String end) {
        String str;
        int ch = cmd.indexOf(st);
        int ch2 = cmd.lastIndexOf("&&") + 2;
        str = cmd.substring(ch, ch2);
        return str;
    }

    //污染物代码
    public static String getCode(String type, String[] strGbCode) {
        String str = "";
        for (String items : strGbCode) {
            if (items.split(":")[0].equals(type)) {
                String[] strTemp = items.split(":");
                str = strTemp[1];
                break;
            }
        }
        return str;
    }


    //污染物代码得到组份名称
    public static String getCompNameByCode(String nameCode, String[] strGbCode) {
        String str = "";
        for (String items : strGbCode) {
            if (items.split(":")[1].equals(nameCode)) {
                String[] strTemp = items.split(":");
                str = strTemp[0];
                break;
            }
        }
        return str;
    }

    public static String[] getNowCfgCompNameCde(String[] strGbCode) {

        String[] list = new String[strComponent.get(1).length];

        for (int i = 0; i < strComponent.get(1).length; i++) {

            list[i] = getCode(strComponent.get(1)[i], strGbCode);
        }
        return list;
    }


    public static String getMeaData(float fData, int point) {

        return dataFormat(fData, point);
    }

    public static String getMeaDataVol(float fData, int point) {
        return getMeaData(fData, point);
    }


    //通过污染物代码获取污染物名称
    public static String getDetectionFactor(String strCode, String[] strGbCode) {
        String str = "";
        for (String items : strGbCode) {
            if (items.split(":")[1].equals(strCode)) {
                String[] strTemp = items.split(":");
                str = strTemp[0];
                break;
            }
        }
        return str;
    }

    // 组份，状态，流程号
    private static String getDevStatus(String Component, String info, String flowName) {
        // 做样   空闲  异常处理   报错后待机   超限报警    校准  清洗
        String[] status = {"N1", "N2", "D1", "D2", "T1", "M1", "M2",};
        String str = "";
        FlowTable ft = Global.getFlows(Component);
        if (ft == null) return str;
        ActionTable at = Global.getActions(Component);
        if (at == null) return str;
        String strNormal = context.getResources().getString(R.string.normal);
        if (info.equals(strNormal)) {
            if ((flowName.contains(context.getResources().getString(R.string.span_1))) || (flowName.contains(context.getResources().getString(R.string.span_2)))) {
                str = status[5];
            } else if ((flowName.contains(context.getResources().getString(R.string.YBQX)))) {
                str = status[6];
            } else if (flowName.contains(context.getResources().getString(R.string.ZY))) {
                str = status[0];
            } else {
                str = status[1];
            }
        } else {
            if (flowName.equals(context.getResources().getString(R.string.waiting_for_instructions))) {
                str = status[3];
            } else if (flowName.contains(context.getResources().getString(R.string.exceptionHandling))) {
                str = status[2];
            }
        }

        return str;
    }

    // 组份，状态，流程号,DataStruct
    private static String getDevStatus(String Component, String info, String flowName, DataStruct DataInfo) {
        //正常    维护  设定值 超上下限    排放源停运   故障  校准状态    做样状态    标测状态    零测状态    清洗状态    初始装液    冲洗状态    其他状态
        //零点核查状态  跨度核查状态  标样核查状态  平行样核查状态  加标回收状态  进样稳定性   光源稳定性
        String[] status = {"N", "M", "S", "T", "F", "D", "C", "A", "B", "Z", "Q", "I", "W", "E", "L", "K", "G", "P", "J", "X", "Y"};
        String str = "";
        FlowTable ft = Global.getFlows(Component);
        if (ft == null) return str;
        ActionTable at = Global.getActions(Component);
        if (at == null) return str;
        String strNormal = context.getResources().getString(R.string.normal);
        String strMode = getConfigData(Component, "runningMode");
        if (strMode.equals("2")) {// 离线
            str = status[4];
        } else {
            if (info.equals(strNormal)) {
                if ((flowName.contains(context.getResources().getString(R.string.span_1))) || (flowName.contains(context.getResources().getString(R.string.span_2)))) {
                    str = status[6];
                } else if ((flowName.contains(context.getResources().getString(R.string.YBQX)))) {
                    str = status[10];
                } else if (flowName.contains(context.getResources().getString(R.string.ZY))) {
                    str = status[7];
                } else if (flowName.contains(context.getResources().getString(R.string.LYCL))) {
                    str = status[9];
                } else if ((flowName.contains(context.getResources().getString(R.string.BYCL))) || (flowName.contains(context.getResources().getString(R.string.BY2CL)))) {
                    str = status[8];
                } else if (flowName.contains(context.getResources().getString(R.string.CSZY))) {
                    str = status[11];
                } else if (flowName.contains(context.getResources().getString(R.string.GLCX))) {
                    str = status[12];
                } else if (flowName.contains(context.getResources().getString(R.string.LDHC))) {
                    str = status[14];
                } else if (flowName.contains(context.getResources().getString(R.string.KDHC))) {
                    str = status[15];
                } else if (flowName.contains(context.getResources().getString(R.string.BYHC))) {
                    str = status[16];
                } else if (flowName.contains(context.getResources().getString(R.string.PXY))) {
                    str = status[17];
                } else if (flowName.contains(context.getResources().getString(R.string.JBHS))) {
                    str = status[18];
                } else if (flowName.contains(context.getResources().getString(R.string.JYWDX))) {
                    str = status[19];
                } else if (flowName.contains(context.getResources().getString(R.string.GYWDX))) {
                    str = status[20];
                } else if (flowName.contains(context.getResources().getString(R.string.waiting_for_instructions))) {
                    String flowTag = DataInfo.getDataTag();
                    String flow = DataInfo.getFlow();
                    if (flowTag.contains("M")) {
                        str = status[1];
                    } else if (flowTag.contains("T") || flowTag.contains("L")) {
                        str = status[3];
                    } else if (flow.equals(context.getResources().getString(R.string.B1)) || flow.equals(context.getResources().getString(R.string.B2))) {
                        str = status[2];
                    } else {
                        str = status[0];
                    }
                } else {
                    str = status[13];
                }
            } else {
                str = status[5];
            }
        }

        return str;
    }

    private static String getDataType(String Component, String flowName) {
        //1：做样数据 2：零点（标1）校准数据 3：量程（标2）校准数据 4：零样测量数据 5：标样1测量数据 6：标样2测量数据 7：零点核查数据
        //8：跨度核查数据 9：标样核查数据 10：平行样数据 11：加标回收数据 12：进样稳定性数据 13：光源稳定性数据
        String[] status = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13",};
        String str = "";
        FlowTable ft = Global.getFlows(Component);
        if (ft == null) return str;
        ActionTable at = Global.getActions(Component);
        if (at == null) return str;
        if ((flowName.contains(context.getResources().getString(R.string.span_1)))) {
            str = status[1];
        } else if ((flowName.contains(context.getResources().getString(R.string.span_2)))) {
            str = status[2];
        } else if (flowName.contains(context.getResources().getString(R.string.LYCL))) {
            str = status[3];
        } else if ((flowName.contains(context.getResources().getString(R.string.BYCL)))) {
            str = status[4];
        } else if ((flowName.contains(context.getResources().getString(R.string.BY2CL)))) {
            str = status[5];
        } else if (flowName.contains(context.getResources().getString(R.string.LDHC))) {
            str = status[6];
        } else if (flowName.contains(context.getResources().getString(R.string.KDHC))) {
            str = status[7];
        } else if (flowName.contains(context.getResources().getString(R.string.BYHC))) {
            str = status[8];
        } else if (flowName.contains(context.getResources().getString(R.string.PXY))) {
            str = status[9];
        } else if (flowName.contains(context.getResources().getString(R.string.JBHS))) {
            str = status[10];
        } else if (flowName.contains(context.getResources().getString(R.string.JYWDX))) {
            str = status[11];
        } else if (flowName.contains(context.getResources().getString(R.string.GYWDX))) {
            str = status[12];
        } else {
            str = status[0];
        }

        return str;
    }

    // 找Code代码
    private boolean AnalysisCodeFile() {

        if (strCode[0] == null) {
            String fileContent;
            fileContent = findFile("Csoft/Protocol/GB/", "Code2005");
            if (fileContent != null) {
                strCode = fileContent.replace("\t", "").replace(" ", "").replace("\r\n", "").replace("\n", "").split("#");
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    // 找国标协议文件
    private static boolean AnalysisCmdFile() {
        String fileContent;
        fileContent = findFile("Csoft/Protocol/GB/", "GB2005");
        if (fileContent != null) {
            strCmd = fileContent.split("\r\n");
            return true;
        } else {
            saveExceptInfo2File("国标文件异常");
            return false;
        }
    }

    // 找路径文件
    public static String findFile(String path, String name) {

        if (Global.SdcardPath == null && Global.extSdcardPath == null) {
            return null;
        }
        String dirPath;
        if (Global.SdcardPath != null) {
            dirPath = Global.SdcardPath;
        } else {
            dirPath = Global.extSdcardPath;
        }
        String pathFileName;
        pathFileName = dirPath + path + name + ".txt";
        File dbFile = new File(pathFileName);
        if (dbFile.exists()) {
            return readString(pathFileName, "GBK");
        } else {
            return null;
        }
    }


    // int数字不足多少位补'0'
    public static String iDataFormat(String iCode, int num) {
        String result;
        if (num == 0) {
            result = String.valueOf(iCode);
        } else {
            result = String.format("%0" + num + "d", parseInt(iCode));
        }
        return result;
    }

    // 字符串补全"0"
    public static String iDataFormat(String sCode, int num, Objects ob) {
        String str;
        switch (sCode.length()) {
            case 1:
                str = "000" + sCode;
                break;
            case 2:
                str = "00" + sCode;
                break;
            case 3:
                str = "0" + sCode;
                break;
            default:
                str = sCode;
                break;
        }
        return str;
    }


    // 对字符串进行替换处理
    public static String replaceTempData(String str, String[] strOld, int oldStart, String[] strNew, int newStart, int len) {

        if (len > 0) {
            while (true) {
                if (oldStart < strOld.length && newStart < StrNew.length) {
                    str = str.replace(strOld[oldStart], strNew[newStart]);
                    len--;
                    oldStart++;
                    newStart++;
                    if (len <= 0) {
                        break;
                    }
                } else {
                    break;
                }
            }
        }
        return str;
    }

    /*当前系统时间*/
    private static DataStruct getNowSysTime() {

        Calendar calendar;
        calendar = Calendar.getInstance();
        //  calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        DataStruct dataStruct = new DataStruct();
        dataStruct.setDataTime(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1
                , calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));

        return dataStruct;
    }

    private static BigInteger trimToNumber(String s) {
        int n = s.length();
        char[] a = new char[n];
        int len = 0;
        for (int i = 0; i < n; i++) {
            char ch = s.charAt(i);
            if (ch >= '0' && ch <= '9') {
                a[len++] = ch;
            }
        }
        return new BigInteger(new String(a, 0, len));
    }

    /**************************************************************************************************/
    /*校验国标协议 接收到数据CRC*/
    public static boolean CheckCrc(byte[] rs) {

        if (rs != null && rs.length > 18) {
            int delNum = 0;
            int len = rs.length;
            int gbProtocolNum;
            int gbProtocolHead = 6;
            int head;
            int delAllNum;
            String str = new String(rs);

            String reCrc = str.substring(str.lastIndexOf("&&") + 2, str.lastIndexOf("&&") + 2 + 4);
            if (str.contains("\r\n")) {
                delNum = 2;
            }
            gbProtocolNum = 10;
            head = gbProtocolHead;
            delAllNum = gbProtocolNum + delNum;

            byte[] bt2 = new byte[len - delAllNum];

            System.arraycopy(rs, head, bt2, 0, bt2.length);
            String crc = getCrc16(getDataCmd(str, "ST", "&&"), findCrc(str));
            //String crc = CRC16.calCrc162(bt2, 0, bt2.length);

            /*byte[] bt3 = new byte[len - delAllNum];
            System.arraycopy(rs, head, bt3, 0, bt3.length);
            String crcpt = CRC16.calCrc16(bt3, 0, bt3.length);*/

            //return (reCrc.toUpperCase().equals(iDataFormat(crc.toUpperCase(), 4, null))) || ((reCrc.toUpperCase().equals(iDataFormat(crcpt.toUpperCase(), 4, null))));
            return (reCrc.toUpperCase().equals(iDataFormat(crc.toUpperCase(), 4, null)));
        }
        return false;
    }

    private static String IntenrnationalReply(String mn, String cn, String temp, boolean bPolId, String compName) {
        String cmd0 = "##XXXXST=32;";
        String cmd1 = ";PW=;MN=";
        cmd1 = cmd0 + cn + cmd1;
        if (!mn.equals("")) {
            cmd1 = cmd1 + mn + ";";
        } else {
            cmd1 += ";";
        }
        if (!temp.equals("")) {
            cmd1 = cmd1 + temp + ";";
        }
        String cmd2 = "CP=&&DataTime=$T1$&&CRC2";
        if (bPolId) {
            cmd2 = "CP=&&DataTime=$T1$" + ",PolId=" + getCode(compName, strCode) + "&&CRC2";
        }
        String cmd = cmd1 + cmd2;

        String[] StrOld = {"$T1$", "XXXX", "CRC"};
        String[] StrNew = new String[3];
        String[] str = new String[3];

        /*时间*/
        DataStruct DataTime = getNowSysTime();
        str[0] = getDataTime(DataTime.getYear(), DataTime.getMonth(), DataTime.getDay(), DataTime.getHour(), DataTime.getMin(), DataTime.getSec());
        str[0] = str[0].substring(0, str[0].length() - 2);
        cmd = replaceTempData(cmd, StrOld, 0, str, 0, 1);
        /*长度*/
        StrNew[0] = iDataFormat(String.valueOf(getDataCmd(cmd, "ST", "&&").length()), 4);
        /*CRC*/
        StrNew[1] = getCrc16(getDataCmd(cmd, "ST", "&&"), findCrc(cmd));
        /*组合CRC 1/2*/
        StrOld[2] = cmd.substring((cmd.lastIndexOf("&&") + 2), (cmd.lastIndexOf("&&") + 6));

        cmd = replaceTempData(cmd, StrOld, 1, StrNew, 0, 2);
        return cmd;
    }

    private static void Cn5011ReadSpanCoc(Communication port, int sCom, String compName) {
        List<Map> list;
        History mHistory;
        mHistory = new History(context);
        DataStruct DataInfo = new DataStruct();
        DataInfo.setType(compName);
        list = mHistory.selectFlowAndTimeData(compName, context.getResources().getString(R.string.B2), getConfigData(compName, "Range" + getConfigData(compName, "RANGE") + "_CAL2_His_Time"));
        if (list.size() > 0) {
            String timeStr = list.get(0).get("time").toString();
            timeStr = timeStr.replace(" ", "-");
            String[] time = timeStr.split("[:-]");
            int year = Integer.valueOf(time[0]);
            int month = Integer.valueOf(time[1]);
            int day = Integer.valueOf(time[2]);
            int hour = Integer.valueOf(time[3]);
            int min = Integer.valueOf(time[4]);
            int sec = Integer.valueOf(time[5]);
            DataInfo.setDataTime(year, month, day, hour, min, sec);
            DataInfo.setData(Float.parseFloat(list.get(0).get("C").toString()));
            //DataInfo.setType(list.get(0).get("component").toString());
            DataInfo.setAi(Float.parseFloat(list.get(0).get("A").toString()));
            DataInfo.setDataTag(list.get(0).get("tag").toString());
            DataInfo.setFlow(list.get(0).get("flow").toString());
        }
        String cmd = new GB2005().getSpanCocCmd(DataInfo);
        SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : "读取标样浓度_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 500, cmd.getBytes());
    }

    private static void Cn5031ReadSpanCoc(Communication port, int sCom, String compName) {
        List<Map> list;
        History mHistory;
        mHistory = new History(context);
        DataStruct DataInfo = new DataStruct();
        DataInfo.setType(compName);
        list = mHistory.selectFlowAndTimeData(compName, context.getResources().getString(R.string.B1), getConfigData(compName, "Range" + getConfigData(compName, "RANGE") + "_CAL1_His_Time"));
        if (list.size() > 0) {
            String timeStr = list.get(0).get("time").toString();
            timeStr = timeStr.replace(" ", "-");
            String[] time = timeStr.split("[:-]");
            int year = Integer.valueOf(time[0]);
            int month = Integer.valueOf(time[1]);
            int day = Integer.valueOf(time[2]);
            int hour = Integer.valueOf(time[3]);
            int min = Integer.valueOf(time[4]);
            int sec = Integer.valueOf(time[5]);
            DataInfo.setDataTime(year, month, day, hour, min, sec);
            DataInfo.setData(Float.parseFloat(list.get(0).get("C").toString()));
            //DataInfo.setType(list.get(0).get("component").toString());
            DataInfo.setAi(Float.parseFloat(list.get(0).get("A").toString()));
            DataInfo.setDataTag(list.get(0).get("tag").toString());
            DataInfo.setFlow(list.get(0).get("flow").toString());
        }

        String cmd = new GB2005().getSpan1CocCmd(DataInfo);
        SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : "读取标样浓度_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 500, cmd.getBytes());
    }

    private static void Cn5012ReadRangCec(Communication port, int sCom, String compName) {
        Kbf mKbf;
        /*float fk1, fk2, fk3, fb1, fb2, fb3;
        CalcTable ct = getCalc(compName);
        Calc calc = ct.getCalc("1");
        mKbf = new Kbf(context);
        List<Map> list = mKbf.select(compName, "1", null, null, 0, 1);

        if (list.size() > 0) {
            fk1 = (float) Double.parseDouble(list.get(0).get("K").toString());
            fb1 = (float) Double.parseDouble(list.get(0).get("B").toString());
        } else {
            fk1 = (float) calc.getK("1", compName);
            fb1 = (float) calc.getB("1", compName);
        }
        list = mKbf.select(compName, "2", null, null, 0, 1);
        calc = ct.getCalc("2");
        if (list.size() > 0) {
            fk2 = (float) Double.parseDouble(list.get(0).get("K").toString());
            fb2 = (float) Double.parseDouble(list.get(0).get("B").toString());
        } else {
            fk2 = (float) calc.getK("2", compName);
            fb2 = (float) calc.getB("2", compName);
        }
        list = mKbf.select(compName, "3", null, null, 0, 1);
        calc = ct.getCalc("3");
        if (list.size() > 0) {
            fk3 = (float) Double.parseDouble(list.get(0).get("K").toString());
            fb3 = (float) Double.parseDouble(list.get(0).get("B").toString());
        } else {
            fk3 = (float) calc.getK("3", compName);
            fb3 = (float) calc.getB("3", compName);
        }
        String cmd = new GB2005().getCoeKbCmd(compName, fk1, fb1, fk2, fb2, fk3, fb3);*/

        float fk, fb;
        String strRange = getConfigData(compName, "RANGE");
        CalcTable ct = getCalc(compName);
        Calc calc = ct.getCalc(strRange);
        mKbf = new Kbf(context);
        List<Map> list = mKbf.select(compName, strRange, null, null, 0, 1);
        if (list.size() > 0) {
            fk = (float) Double.parseDouble(list.get(0).get("K").toString());
            fb = (float) Double.parseDouble(list.get(0).get("B").toString());
        } else {
            fk = (float) calc.getK(strRange, compName);
            fb = (float) calc.getB(strRange, compName);
        }
        String cmd = new GB2005().getCoeKbCmd(compName, fk, fb);
        SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : "读取量程KB_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 500, cmd.getBytes());
    }

    private static void Cn5033ReadRangCec(Communication port, int sCom, String compName) {
        Kbf mKbf;
        float fk, fb;
        String strRange = getConfigData(compName, "RANGE");
        fk = (float) Double.parseDouble(getConfigData(compName, "ExtRange" + strRange + "K"));
        fb = (float) Double.parseDouble(getConfigData(compName, "ExtRange" + strRange + "B"));
        String cmd = new GB2005().getCorrectionFactorKbCmd(compName, fk, fb);
        SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : "读取量程KB_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 500, cmd.getBytes());
    }

    private static void Cn5013ReadWorkCec(Communication port, int sCom, String compName) {

        float fk = 1;
        //fk = Float.parseFloat(getNowRangeF(compName));
        String cmd = new GB2005().getAdjustKbCmd(compName, fk);
        SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : "读取工作曲线_09_" + (sCom == 1 ? "07" : "09")), port, 1, 500, cmd.getBytes());
    }

    private static void Cn5014ReadPumpTime(Communication port, int sCom, String compName) {

        String cmd = new GB2005().getPumpTimeCmd(compName, Integer.parseInt(getPublicConfigData("CK_PUMP_TIME")));
        SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : "读取预开水泵_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 500, cmd.getBytes());
    }

    private static void Cn5015ReadAlarmData(Communication port, int sCom, String compName) {

        ErrorLog error = new ErrorLog(context);
        int errNum = 0;
        List<Map> list = error.select(compName, null, null, new ErrorLog.msgType[]{ErrorLog.msgType.values()[3], ErrorLog.msgType.values()[4]}, 0);
        if (list.size() == 0) {
        } else {
            errNum = Integer.valueOf(list.get(0).get("errNum").toString());
        }
        String cmd = new GB2005().getAlarmMsgCmd(errNum);
        SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : "读取报警错误_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 500, cmd.getBytes());
    }

    private static void Cn5016ReadPeriodSetData(Communication port, int sCom, String compName) {

        String[] Hs = getConfigData(compName, "zqclh").split("[,，]");
        String str = "";
        boolean find = false;
        for (int i = 0; i < 24; i++) {
            for (String h : Hs) {
                // 从0点开始
                if (i == Integer.valueOf(h)) {
                    find = true;
                    break;
                }
            }
            if (find) {
                str += "1";
            } else {
                str += "0";
            }


            find = false;
        }

        int hour = (getConfigData(compName, "lxclh")).equals("") ? 0 : Integer.valueOf(getConfigData(compName, "lxclh"));
        int min = getConfigData(compName, "lxclm").equals("") ? 0 : Integer.valueOf(getConfigData(compName, "lxclm"));
        String cmd = new GB2005().getJobPeriodCmd(str, hour * 60 + min);
        SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : "读取周期做样设置_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 500, cmd.getBytes());
    }

    private static void Cn5019ReadNowSelectRange(Communication port, int sCom, String compName) {
        String strRange = getConfigData(compName, "RANGE");
        String cmd = new GB2005().getRangeSelectCmd(compName, Integer.valueOf(strRange), Float.valueOf(getConfigData(compName, "LC" + strRange + "L")), Float.valueOf(getConfigData(compName, "LC" + strRange + "H")));
        SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : "读取当前量程_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 500, cmd.getBytes());
    }

    private static void Cn5020ReadDigestionTime(Communication port, int sCom, String compName) {
        int time = getHeatTime(compName).equals("") ? 0 : Integer.valueOf(getHeatTime(compName));
        String cmd = new GB2005().getDigestionTimeCmd(time);
        SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : "读取消解时间_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 500, cmd.getBytes());
    }

    private static void Cn5021ReadDigestionTemp(Communication port, int sCom, String compName) {
        int temp = getConfigData(compName, "xjwd").equals("") ? 0 : Integer.valueOf(getConfigData(compName, "xjwd"));
        String cmd = new GB2005().getDigestionTemperatureCmd(temp);
        SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : "读取消解温度_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 500, cmd.getBytes());
    }

    private static void Cn5022ReadColorTime(Communication port, int sCom, String compName) {
        int time = getConfigData(compName, "xssc").equals("") ? 0 : Integer.valueOf(getConfigData(compName, "xssc"));
        String cmd = new GB2005().getColorTimeCmd(time);
        SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : "读取显色时长_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 500, cmd.getBytes());
    }

    private static void Cn5023ReadAlarmLimit(Communication port, int sCom, String compName) {

        float alarmHigh = getConfigData(compName, "ALARM_H").equals("") ? 0 : Float.valueOf(getConfigData(compName, "ALARM_H"));
        float alarmLow = getConfigData(compName, "ALARM_L").equals("") ? 0 : Float.valueOf(getConfigData(compName, "ALARM_L"));
        String cmd = new GB2005().getAlarmLimitCmd(compName, alarmLow, alarmHigh);
        SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : "读取报警上下限_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 500, cmd.getBytes());

    }

    private static void Cn5024ReadRangeInfo(Communication port, int sCom, String compName) {
        float rangeLow = getConfigData(compName, "YBLCL").equals("") ? 0 : Float.valueOf(getConfigData(compName, "YBLCL"));
        float rangeHigh = getConfigData(compName, "YBLCH").equals("") ? 0 : Float.valueOf(getConfigData(compName, "YBLCH"));

        String cmd = new GB2005().getRangeInfoCmd(compName, rangeLow, rangeHigh);
        SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : "读取设备量程_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 500, cmd.getBytes());
    }

    private static void CN5025ReadK2CR2O7Coc(Communication port, int sCom, String compName) {
        String cmd = new GB2005().getK2CR2O7CocCmd(compName, Float.parseFloat(getConfigData(compName, "ZGSJND")));
        SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : "读取K2Cr207浓度_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 500, cmd.getBytes());
    }

    private static void Cn5018ReadAutoCalibrationSetting(Communication port, int sCom, String compName) {

        int day = getConfigData(compName, "zdjzt").equals("") ? 0 : Integer.valueOf(getConfigData(compName, "zdjzt"));
        int hour = getConfigData(compName, "zdjzh").equals("") ? 0 : Integer.valueOf(getConfigData(compName, "zdjzh"));
        String cmd = new GB2005().getAutoCalibrationCmd(day, hour);
        SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : "读取自动校准设置_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 500, cmd.getBytes());
    }

    private static void Cn5026ReadAutoCleanSetting(Communication port, int sCom, String compName) {

        int times = getConfigData(compName, "zdqxh").equals("") ? 0 : Integer.valueOf(getConfigData(compName, "zdqxh"));

        String cmd = new GB2005().getAutoCleanCmd(compName, times);
        SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : "读取自动清洗设置_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 500, cmd.getBytes());
    }

    private static void Cn1011ReadSystemTime(Communication port, int sCom) {
        String cmd = new GB2005().getSysTimeCmd();
        SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : "读取设备时间_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 500, cmd.getBytes());
    }

    private static void Cn5032ReadColorTemp(Communication port, int sCom, String compName) {
        int time = getColorTemp(compName).equals("") ? 0 : Integer.valueOf(getColorTemp(compName));
        String cmd = new GB2005().getColorTempCmd(time);
        SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : "读取消解时间_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 500, cmd.getBytes());
    }

    private static void Cn3014SetPeriodDoJob(Communication port, int sCom, String str, String compName, boolean bPolId) {
        int othStart = str.indexOf("OTH=");

        String othStr = (str.substring((othStart + 4), (othStart + 4 + 24)));
        int istStart = str.indexOf("IST=");
        String istStr = String.valueOf(trimToNumber(str.substring((istStart + 3), (istStart + 3 + 6))));

        Log.i("OTH", othStr);
        Log.i("IST", istStr);

        byte[] othBt = othStr.getBytes();
        String othSetStr = "";

        if (othBt.length == 24) {
            for (int i = 0; i < 24; i++) {
                if (1 == (Integer.valueOf(othBt[i]) - 0x30)) {
                    if (!othSetStr.equals("")) {
                        othSetStr += "，";
                    }
                    othSetStr += String.valueOf(i);
                }
            }

            updateConfigData(compName, "zqclh", othSetStr);

            Log.i("othSetStr", othSetStr);
            int hour = Integer.valueOf(istStr) / 60;
            int min = Integer.valueOf(istStr) % 60;
            if (hour > 23) {
                hour = 23;
            } else if (hour < 0) {
                hour = 0;
            }
            if (min > 59) {
                min = 59;
            } else if (min < 0) {
                min = 0;
            }
            Log.i("连续测量小时", String.valueOf(hour));
            Log.i("连续测量分", String.valueOf(min));
            updateConfigData(compName, "lxclh", String.valueOf(hour));
            updateConfigData(compName, "lxclm", String.valueOf(min));

            String cmd = IntenrnationalReply("", "CN=3014", "OTH=;IST=", bPolId,compName);
            Log.i("设置连续测量时间_09_07", cmd);
            SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : "设置连续测量时间_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 500, cmd.getBytes());
            syncAutoDoSample_topHour();
        }
    }

    private static void Cn3017SetRangeSelected(Communication port, int sCom, String str, String compName, boolean bPolId) {
        if (!doFlowing.get(compName).equals(context.getString(R.string.waiting_for_instructions))) {
            return;
        }
        int othStart = str.indexOf("RA=");
        String raStr = (str.substring((othStart + 3), (othStart + 3 + 6)));
        raStr = raStr.substring(0, raStr.lastIndexOf(";"));
        Log.i("设置选择量程", String.valueOf(trimToNumber(raStr)));
        String strRange = String.valueOf(trimToNumber(raStr));
        int iRange = Integer.parseInt(strRange);
        if (iRange > 3) {
            iRange = 3;
        } else if (iRange < 1) {
            iRange = 1;
        }
        setUseRange(compName, String.valueOf(iRange));
        String cmd = IntenrnationalReply("", "CN=3017", "RA=", bPolId,compName);
        SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : "设置选择量程_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 500, cmd.getBytes());
    }

    private static void Cn3019SetAutoCleanSetting(Communication port, int sCom, String str, String compName, boolean bPolId) {
        if (!doFlowing.get(compName).equals(context.getString(R.string.waiting_for_instructions))) {
            return;
        }
        int start = str.indexOf("AWP=");
        String raStr = (str.substring((start + 4), (start + 4 + 6)));
        raStr = raStr.substring(0, raStr.lastIndexOf(";"));
        updateConfigData(compName, "zdqxh", String.valueOf(trimToNumber(raStr)));
        String cmd = IntenrnationalReply("", "CN=3019", "AWP=", bPolId,compName);
        SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : "设置自动清洗周期_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 500, cmd.getBytes());

    }

    private static void Cn3018SetAutoCalibrationSetting(Communication port, int sCom, String str, String compName, boolean bPolId) {
        if (!doFlowing.get(compName).equals(context.getString(R.string.waiting_for_instructions))) {
            return;
        }
        int start = str.indexOf("ACP=");
        String raStr = (str.substring((start + 4), (start + 4 + 6)));
        raStr = raStr.substring(0, raStr.lastIndexOf(";"));
        int iDay = Integer.parseInt(String.valueOf(trimToNumber(raStr)));
        if (iDay > 31) {
            iDay = 31;
        } else if (iDay < 0) {
            iDay = 0;
        }
        updateConfigData(compName, "zdjzt", String.valueOf(iDay));
        Log.i("设置自动校准周期—天", String.valueOf(trimToNumber(raStr)));
        start = str.indexOf("ACT=");
        raStr = (str.substring((start + 4), (start + 4 + 6)));
        raStr = raStr.substring(0, raStr.lastIndexOf(";"));
        int iHour = Integer.parseInt(String.valueOf(trimToNumber(raStr)));
        if (iHour > 23) {
            iHour = 23;
        } else if (iHour < 0) {
            iHour = 0;
        }
        updateConfigData(compName, "zdjzh", String.valueOf(iHour));

        Log.i("设置自动校准周期-时", String.valueOf(trimToNumber(raStr)));
        String cmd = IntenrnationalReply("", "CN=3018", "ACP=;ACT=", bPolId,compName);
        SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : "设置自动校准周期_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 500, cmd.getBytes());
    }

    private static void Cn3011calibration(Communication port, int sCom, String compName, boolean bPolId) {
        if (!doFlowing.get(compName).equals(context.getString(R.string.waiting_for_instructions))) {
            return;
        }
        doControlJob(compName, context.getResources().getString(R.string.SDJZ));

        String cmd = IntenrnationalReply("", "CN=3011", "", bPolId,compName);
        SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : "远程手动校准_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 500, cmd.getBytes());
    }

    private static void CnDoJob(Communication port, int sCom, String compName, String strCN, String flowName, boolean bPolId) {
        if (!doFlowing.get(compName).equals(context.getString(R.string.waiting_for_instructions))) {
            return;
        }
        doControlJob(compName, flowName);

        String cmd = IntenrnationalReply("", "CN=" + strCN, "", bPolId,compName);
        SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : "远程" + flowName + "_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 500, cmd.getBytes());
    }

    private static void Cn3015DoClean(Communication port, int sCom, String compName, boolean bPolId) {
        if (!doFlowing.get(compName).equals(context.getString(R.string.waiting_for_instructions))) {
            return;
        }
        doControlJob(compName, context.getResources().getString(R.string.SDYBQX));

        String cmd = IntenrnationalReply("", "CN=3015", "", bPolId,compName);
        SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : "远程仪表清洗_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 500, cmd.getBytes());
    }

    private static void Cn3016emergencyStop(Communication port, int sCom, String compName, boolean bPolId) {

        SendManager.SendCmd(compName + "_紧急停止" + "_8_07", S0, 3, 500, null);
        String cmd = IntenrnationalReply("", "CN=3016", "", bPolId,compName);
        SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : "远程停止_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 500, cmd.getBytes());
    }

    private static void Cn1012emergencyStop(Communication port, int sCom, String str, String compName) {
        SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : "江苏QR1_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 500, str.getBytes());

        int start = str.indexOf("SystemTime=");
        String strSysTime = (str.substring((start + 11), (start + 11 + 14)));
        if (!strSysTime.equals("")) {
            int year = Integer.parseInt(strSysTime.substring(0, 4));
            int month = Integer.parseInt(strSysTime.substring(4, 6));
            int day = Integer.parseInt(strSysTime.substring(6, 8));
            int hour = Integer.parseInt(strSysTime.substring(8, 10));
            int minute = Integer.parseInt(strSysTime.substring(10, 12));
            int second = Integer.parseInt(strSysTime.substring(12, 14));

            saveOperationLogMsg(compName, "GB2005-设置时间" + year + "/" + month + "/" + day + " " + hour + ":" + minute + ":" + second, ErrorLog.msgType.操作_信息);
            SetDateTime(compName, year, month - 1, day, hour, minute, second);
            //时间矫正
            /*AddError(compName, 614, 运维_信息);
            try {
                setDate(year, month - 1, day);
                setTime(hour, minute, second);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
            byte[] timeByte = copybyte(toByteArray(year, 4), toByteArray((month), 4),
                    toByteArray(day, 4), toByteArray(hour, 4),
                    toByteArray(minute, 4), toByteArray((second), 4));
            SendManager.SendCmd(compName + "_时间管理_06_0", S0, 3, 200, timeByte);
        }
    }

    public static DataStruct[] getDataStructs(int sCom, boolean updateAbs) {
        List<Map> list;
        GB2005 gb2005 = new GB2005();
        History mHistory;
        mHistory = new History(context);
        String[] compNames;

        compNames = strComponent.get(1);
        DataStruct[] data = new DataStruct[compNames.length];
        for (int i = 0; i < compNames.length; i++) {
            data[i] = new DataStruct();
        }
        for (int i = 0; i < compNames.length; i++) {
            /* 校准数据上传*/
            if (getPublicConfigData("CAL_DATA_UPDATE").equals("true")) {
                list = mHistory.select(compNames[i], null, null, null, 0, 1);
            } else {
                list = mHistory.select(compNames[i], context.getResources().getString(R.string.ZY), null, null, 0, 1);
            }
            if (list.size() > 0) {
                String timeStr = list.get(0).get("time").toString();
                timeStr = timeStr.replace(" ", "-");
                String[] time = timeStr.split("[:-]");
                int year = Integer.valueOf(time[0]);
                int month = Integer.valueOf(time[1]);
                int day = Integer.valueOf(time[2]);
                int hour = Integer.valueOf(time[3]);
                int min = Integer.valueOf(time[4]);
                int sec = Integer.valueOf(time[5]);
                data[i].setDataTime(year, month, day, hour, min, sec);
                data[i].setData(Float.parseFloat(list.get(0).get("C").toString()));
                data[i].setType(list.get(0).get("component").toString());
                data[i].setAi(Float.parseFloat(list.get(0).get("A").toString()));
                data[i].setDataTag(list.get(0).get("tag").toString());
                data[i].setFlow(list.get(0).get("flow").toString());
                if (getPublicConfigData("ABSORB_UPDATE").equals("true") && updateAbs) {
                    SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : "国标吸光度上传_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), S1, 1, 500, gb2005.getAbsorbancyCmd(data[i]).getBytes());
                }
            }
        }
        return data;
    }

    // 上传通知命令
    public static void sendHeartbeatCmd(Communication port, int sCom) {

        String strCmd2 = "##XXXXQN=$T$;ST=9130;CN=9021;PW=123456;MN=$MN$;CP=&&&&CRC1";
        String[] StrHeadType = {"$T$", "$MN$", "XXXX"};
        String[] StrEnd = {"CRC"};

        if (strCmd[HEART] == null) {
            if (!AnalysisCmdFile()) {
                return;
            }
        }

        if (strCmd[HEART].contains("##") && (!strCmd[HEART].substring(0, 2).equals("//"))) {
            strCmd2 = strCmd[HEART];
        }
        // 兼容心跳包内包含DataTime的指令，该指令和测量数据上传指令相同
        if (strCmd2.contains("DataTime")) {
            try {
                String cmd = packageMeaDataUpdate(strCmd[HEART], getDataStructs(sCom, false)[0], true);
                SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : "心跳包_09_" + (sCom == 3 ? "08" : (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09")))), port, 1, 500, cmd.getBytes());
            } catch (Exception e) {
                Log.e("exception", e.toString());
            }
            return;
        }

        /*帧头组装*/
        DataStruct DataTime = getNowSysTime();
        String[] headTemp = new String[3];
        headTemp[0] = getDataTime(DataTime.getYear(), DataTime.getMonth(), DataTime.getDay(), DataTime.getHour(), DataTime.getMin(), DataTime.getSec()) + "000";

        strCmd2 = replaceTempData(strCmd2, StrHeadType, 0, headTemp, 0, 1);

        /*MN号*/
        String[] strMn = new String[1];
        if (!getPublicConfigData("ORG_ADDR").equals(""))
            strMn[0] = getPublicConfigData("ORG_ADDR") + getPublicConfigData("DEV_ADDR");
        else
            strMn[0] = MN;
        strCmd2 = replaceTempData(strCmd2, StrHeadType, 1, strMn, 0, 1);

        /*命令长度*/
        String[] strLen = new String[1];
        strLen[0] = iDataFormat(String.valueOf(getDataCmd(strCmd2, "QN", "&&").length()), 4);
        strCmd2 = replaceTempData(strCmd2, StrHeadType, 2, strLen, 0, 1);

        /*获取CRC*/
        String[] strCrc = new String[1];
        strCrc[0] = getCrc16(getDataCmd(strCmd2, "QN", "&&"), findCrc(strCmd2));
        StrEnd[0] = strCmd2.substring((strCmd2.lastIndexOf("&&") + 2), (strCmd2.lastIndexOf("&&") + 6));
        /*替换CRC*/
        strCmd2 = replaceTempData(strCmd2, StrEnd, 0, strCrc, 0, 1) + "\r\n";

        SendManager.SendCmd("IO" + "_" + "心跳包_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09")), port, 1, 500, strCmd2.getBytes());

    }

    public static void ParsingProtocolInternational(Communication port, int sCom, byte[] rs) {
        if (rs != null && rs.length > 18) {

            if (CheckCrc(rs)) {
                try {
                    String str = new String(rs);
                    int cnStart = str.indexOf("CN");
                    boolean bPolId = str.contains("PolId=");
                    if (cnStart != -1) {
                        String[] compNames;
                        String compName;

                        compNames = strComponent.get(1);
                        if (compNames.length == 0) {
                            return;
                        }
                        S1_DCIReadTimes++;

                        compName = compNames[0];
                        String cnCmd = str.substring(cnStart, cnStart + 7);
                        if (getModePermissions(compName, "反控")) {
                            int mnStart = str.indexOf("MN");
                            String strMN = str.substring(mnStart + 3, mnStart + 17);
                            if (!strMN.equals(getMN()[0])) {
                                saveRunInfo2File("MN号错误");
                                return;
                            }
                            if ("CN=1011".equals(cnCmd)) {
                                Cn1011ReadSystemTime(port, sCom);
                            } else if ("CN=5011".equals(cnCmd)) {
                                Cn5011ReadSpanCoc(port, sCom, compName);
                            } else if ("CN=5012".equals(cnCmd)) {
                                Cn5012ReadRangCec(port, sCom, compName);
                            } else if ("CN=5013".equals(cnCmd)) {
                                Cn5013ReadWorkCec(port, sCom, compName);
                            } else if ("CN=5014".equals(cnCmd)) {
                                Cn5014ReadPumpTime(port, sCom, compName);
                            } else if ("CN=5015".equals(cnCmd)) {
                                Cn5015ReadAlarmData(port, sCom, compName);
                            } else if ("CN=5016".equals(cnCmd)) {
                                Cn5016ReadPeriodSetData(port, sCom, compName);
                            } else if ("CN=5018".equals(cnCmd)) {
                                Cn5018ReadAutoCalibrationSetting(port, sCom, compName);
                            } else if ("CN=5019".equals(cnCmd)) {
                                Cn5019ReadNowSelectRange(port, sCom, compName);
                            } else if ("CN=5020".equals(cnCmd)) {
                                Cn5020ReadDigestionTime(port, sCom, compName);
                            } else if ("CN=5021".equals(cnCmd)) {
                                Cn5021ReadDigestionTemp(port, sCom, compName);
                            } else if ("CN=5022".equals(cnCmd)) {
                                Cn5022ReadColorTime(port, sCom, compName);
                            } else if ("CN=5023".equals(cnCmd)) {
                                Cn5023ReadAlarmLimit(port, sCom, compName);
                            } else if ("CN=5024".equals(cnCmd)) {
                                Cn5024ReadRangeInfo(port, sCom, compName);
                            } else if ("CN=5025".equals(cnCmd)) {
                                CN5025ReadK2CR2O7Coc(port, sCom, compName);
                            } else if ("CN=5026".equals(cnCmd)) {
                                Cn5026ReadAutoCleanSetting(port, sCom, compName);
                            } else if ("CN=5031".equals(cnCmd)) {
                                Cn5031ReadSpanCoc(port, sCom, compName);
                            } else if ("CN=5032".equals(cnCmd)) {
                                Cn5032ReadColorTemp(port, sCom, compName);
                            } else if ("CN=5033".equals(cnCmd)) {
                                Cn5033ReadRangCec(port, sCom, compName);
                            } else if ("CN=3014".equals(cnCmd)) {
                                Cn3014SetPeriodDoJob(port, sCom, str, compName, bPolId);
                            } else if ("CN=3017".equals(cnCmd)) {
                                Cn3017SetRangeSelected(port, sCom, str, compName, bPolId);
                            } else if ("CN=3012".equals(cnCmd)) {
                                CnDoJob(port, sCom, compName, "3012", context.getResources().getString(R.string.SDZY), bPolId);
                            } else if ("CN=3015".equals(cnCmd)) {
                                Cn3015DoClean(port, sCom, compName, bPolId);
                            } else if ("CN=3011".equals(cnCmd)) {
                                Cn3011calibration(port, sCom, compName, bPolId);
                            } else if ("CN=3016".equals(cnCmd)) {
                                Cn3016emergencyStop(port, sCom, compName, bPolId);
                            } else if ("CN=3018".equals(cnCmd)) {
                                Cn3018SetAutoCalibrationSetting(port, sCom, str, compName, bPolId);
                            } else if ("CN=3019".equals(cnCmd)) {
                                Cn3019SetAutoCleanSetting(port, sCom, str, compName, bPolId);
                            } else if ("CN=1012".equals(cnCmd) || "CN=3024".equals(cnCmd)) {//2020年6月12日15:57:27 新增3024时间校准
                                Cn1012emergencyStop(port, sCom, str, compName);
                            } else if ("CN=3025".equals(cnCmd)) {
                                CnDoJob(port, sCom, compName, "3025", context.getResources().getString(R.string.SDBYHC), bPolId);
                            } else if ("CN=3026".equals(cnCmd)) {
                                CnDoJob(port, sCom, compName, "3026", context.getResources().getString(R.string.SDBYCL), bPolId);
                            } else if ("CN=3027".equals(cnCmd)) {
                                CnDoJob(port, sCom, compName, "3027", context.getResources().getString(R.string.SDBY2CL), bPolId);
                            } else if ("CN=3028".equals(cnCmd)) {
                                CnDoJob(port, sCom, compName, "3028", context.getResources().getString(R.string.SDLYCL), bPolId);
                            }
                        }
                    }
                } catch (Exception e) {
                    Log.e("GB2005解析异常", e.toString());
                }
            }
        }
    }

    private static String[] getMN() {
        /*MN号*/
        String[] strMn = new String[1];
        if (!getPublicConfigData("ORG_ADDR").equals(""))
            strMn[0] = getPublicConfigData("ORG_ADDR") + getPublicConfigData("DEV_ADDR");
        else
            strMn[0] = MN;
        return strMn;
    }

}
