package com.yzlm.cyl.cfragment.Communication.CommProtocol.Modbus.RTU2018;

import android.os.Message;
import android.support.annotation.NonNull;

import com.yzlm.cyl.cfragment.Communication.Communication;
import com.yzlm.cyl.cfragment.Communication.Thread.SendManager;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.History;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.Util.DataUtil;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import static com.yzlm.cyl.cfragment.Cal.Component.CalFunc.isB1CalEnable;
import static com.yzlm.cyl.cfragment.Cal.Component.CalFunc.isB2CalEnable;
import static com.yzlm.cyl.cfragment.Communication.Component.Cmd.CommandParsing.AddError;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.updateConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogDataModifyMsg;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogMsg;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog.msgType.运维_信息;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.getPublicConfigData;
import static com.yzlm.cyl.cfragment.Content.Component.ContentTool.GetPlatRangSum;
import static com.yzlm.cyl.cfragment.Content.Component.List2.List2_Content2.LXCLStatusSetting;
import static com.yzlm.cyl.cfragment.Content.Component.List2.List2_Content2.MeaModeSetting;
import static com.yzlm.cyl.cfragment.Content.Component.List2.List2_Content2.ZDJZStatusSetting;
import static com.yzlm.cyl.cfragment.Content.Component.List2.List2_Content2.ZDQXStatusSetting;
import static com.yzlm.cyl.cfragment.Content.Component.List2.List2_Content2.ZDZYStatusSetting;
import static com.yzlm.cyl.cfragment.Content.Component.List2.List2_Content2.ZQCLStatusSetting;
import static com.yzlm.cyl.cfragment.Content.Component.List2.List2_Content2.getModePermissions;
import static com.yzlm.cyl.cfragment.Content.Component.List2.List2_Content2.setMeaModeSpinnerSelect;
import static com.yzlm.cyl.cfragment.Content.Component.List2.List2_Content3.setUseRange;
import static com.yzlm.cyl.cfragment.Content.Component.List2.List2_Content6.ResetWaste;
import static com.yzlm.cyl.cfragment.Content.Component.List2.List2_Content6.resetSurplusBottle;
import static com.yzlm.cyl.cfragment.DBConvert.BCDDeccode.bcd2Str;
import static com.yzlm.cyl.cfragment.DBConvert.MDBConvert.getHistoryTimeStringByDay;
import static com.yzlm.cyl.cfragment.DBConvert.MDBConvert.getHistoryTimeStringByHour;
import static com.yzlm.cyl.cfragment.DBConvert.MDBConvert.getHistoryTimeStringByMin;
import static com.yzlm.cyl.cfragment.DBConvert.MDBConvert.getHistoryTimeStringByMonth;
import static com.yzlm.cyl.cfragment.DBConvert.MDBConvert.getHistoryTimeStringBySec;
import static com.yzlm.cyl.cfragment.DBConvert.MDBConvert.getHistoryTimeStringByYear;
import static com.yzlm.cyl.cfragment.DBConvert.MDBConvert.toBytes;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.SetDateTime;
import static com.yzlm.cyl.cfragment.Frame.Content.Up_content3.stopWorking;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.WinWidgetHandler;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.sendSysTimeToDev;
import static com.yzlm.cyl.cfragment.Global.IOBoardUsed;
import static com.yzlm.cyl.cfragment.Global.S0;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.doControlJob;
import static com.yzlm.cyl.cfragment.Global.doFlowing;
import static com.yzlm.cyl.cfragment.Global.getTimer_cycle;
import static com.yzlm.cyl.cfragment.Global.lRtu2018CompRegMap;
import static com.yzlm.cyl.cfragment.Global.protocolName;
import static com.yzlm.cyl.cfragment.Global.saveRunInfo2File;
import static com.yzlm.cyl.cfragment.Global.strComponent;
import static com.yzlm.cyl.cfragment.Global.syncAutoDoSample_topHour;
import static com.yzlm.cyl.clibrary.Util.DataUtil.bytesToHexString;
import static com.yzlm.cyl.clibrary.Util.DataUtil.copybyte;
import static com.yzlm.cyl.clibrary.Util.DataUtil.crc16;
import static com.yzlm.cyl.clibrary.Util.DataUtil.toByteArray;
import static java.lang.System.arraycopy;

/*
 * Created by zwj on 2018/1/26.
 */

public class ModbusRtu2018Protocol {

    private static String nowCompName = "";

    public static void ParsingProtocolRtu2018(Communication port, int sCom, byte[] rs, String sProtocol) {

        /*String str = "接收到工控机命令:" + bytesToHexString(rs, rs.length);
        saveRunInfo2File(str);*/

        if (rs != null && rs.length > 6) {
            byte[] crc = new byte[2];
            //
            arraycopy(rs, rs.length - 2, crc, 0, 2);
            byte[] RtuRs = new byte[rs.length - 2];
            arraycopy(rs, 0, RtuRs, 0, RtuRs.length);


            //若校验模式是低位，则先进行反转
            if ("2".equals(getPublicConfigData("ModbusRtu2018_CRC_MODE"))) {
                DataUtil.reverse(crc);
            }

            if (Arrays.equals(crc16(RtuRs, RtuRs.length), crc)) {
                nowCompName = getCompName(RtuRs[0]);

                if (!nowCompName.equals("")) {
                    switch (RtuRs[1]) {
                        case 3:
                            modbus_03_Solve(port, sCom, RtuRs);
                            break;
                       /* case 06:
                            modbus_06_Solve(scom, RtuRs);
                            break;*/
                        case 16:
                            modbus_16_Solve(port, sCom, RtuRs);
                            break;
                        default:
                            // 异常码01
                            sendModbusErrorCode(port, sCom, RtuRs, 1);
                            break;
                    }
                }
            }
        }
    }

    /*
     * 通过接收到地址找到对应组份
     */
    public static String getCompName(int reAddr) {
        if (strComponent.get(1).length > 0) {
            for (String item : strComponent.get(1)) {
                if (getConfigData(item, "RTU_ID").equals("")) {
                    continue;
                }
                if (getConfigData(item, "RTU_ID").equals(String.valueOf(reAddr))) {
                    return item;
                }
            }
            if (getPublicConfigData("SYS_RTU_ID").equals(String.valueOf(reAddr))) {
                return "SYSTEM";
            }
        }
        return "";
    }

    /**
     * Modbus功能码03处理程序
     * 读保持寄存器
     */
    private static void modbus_03_Solve(Communication port, int sCom, byte[] rs) {
        int i;
        int startRegAddr = (((short) rs[2] & 0xff) << 8) | (rs[3] & 0xff);//获取寄存器起始地址
        int RegNum = ((((short) rs[4]) << 8) | rs[5]) & 0xff;//获取寄存器数量
        byte[] allDataBytes = new byte[256];
        int allDataCh = 0;
        int regAddr = startRegAddr;
        byte[] dataBytes;


        if (regAddr == 4248) {//运行模式 任何状态都可读取

        } else if (!getModePermissions(nowCompName, "反控") && (!((regAddr + RegNum) < 4224))) {//非在线下不可读取参数,历史数据区域可读取
            sendModbusErrorCode(port, sCom, rs, 6);
            return;
        }

        if (RegNum < lRtu2018CompRegMap.get(nowCompName).size() || RegNum < 128) {

            byte[] head = new byte[3];
            head[0] = rs[0];
            head[1] = rs[1];
            head[2] = (byte) ((RegNum * 2) & 0xff);
            if (lRtu2018CompRegMap.get(nowCompName).get(regAddr) == null) {
                sendModbusErrorCode(port, sCom, rs, 2);
                return;
            }
            for (i = 0; i < RegNum; ) {
                i += lRtu2018CompRegMap.get(nowCompName).get(regAddr).getDataLen();
                if (i > RegNum) {
                    sendModbusErrorCode(port, sCom, rs, 1);
                    return;
                }

                //判断是否是float类型的值，若是则根据对应协议下的解析模式进行取值
                if (lRtu2018CompRegMap.get(nowCompName).get(regAddr).getDataType().equals("FLOAT")) {//MODBUS_RTU_2018默认数字类型为FLOAT
                    dataBytes = copybyte(toBytes(lRtu2018CompRegMap.get(nowCompName).get(regAddr).getData(), getPublicConfigData("ModbusRtu2018_ANALYSIS_MODE"), lRtu2018CompRegMap.get(nowCompName).get(regAddr).getDataLen()));

                } else {
                    dataBytes = copybyte(toBytes(lRtu2018CompRegMap.get(nowCompName).get(regAddr).getData(), lRtu2018CompRegMap.get(nowCompName).get(regAddr).getDataType(), lRtu2018CompRegMap.get(nowCompName).get(regAddr).getDataLen()));
                }

                arraycopy(dataBytes, 0, allDataBytes, allDataCh, dataBytes.length);
                allDataCh += dataBytes.length;

                regAddr += lRtu2018CompRegMap.get(nowCompName).get(regAddr).getDataLen();
                if (lRtu2018CompRegMap.get(nowCompName).get(regAddr) == null) {
                    sendModbusErrorCode(port, sCom, rs, 2);
                    return;
                }
            }
            byte[] sendBytes = new byte[allDataCh + 3 + 2];
            arraycopy(head, 0, sendBytes, 0, 3);
            arraycopy(allDataBytes, 0, sendBytes, 3, allDataCh);
            if (getPublicConfigData("ModbusRtu2018_CRC_MODE").equals("2")) {
                byte[] crc = crc16(sendBytes, sendBytes.length - 2);
                DataUtil.reverse(crc);
                arraycopy(crc, 0, sendBytes, sendBytes.length - 2, 2);
            } else {
                arraycopy(crc16(sendBytes, sendBytes.length - 2), 0, sendBytes, sendBytes.length - 2, 2);
            }

            rS485_SendData(port, sCom, sendBytes);

        } else {
            //寄存器地址+数量超出范围
            sendModbusErrorCode(port, sCom, rs, 2);
        }
    }


    /***
     * modbus 写多个保持寄存器数据
     * */
    private static void modbus_16_Solve(Communication port, int sCom, @NonNull byte[] rs) {

        int i;
        int startRegAddr = (((short) rs[2] & 0xff) << 8) | (rs[3] & 0xff);//获取寄存器起始地址
        int RegNum = (((short) rs[4]) << 8) | rs[5];//获取寄存器数量
        int dataLen = rs[6];
        int dataBit = 7;
        //寄存器地址+数量在范围内
        int data = 0;
        boolean dataErrFlag = false;
        if (!(RegNum * 2 == dataLen)) {
            // 不符合modbus 协议格式 寄存器个数和字节数
            return;
        }
        if (rs.length > 7) {
            if (!(dataLen == (rs.length - 7))) {// 7 已经去除了CRC
                // 不符合modbus 协议格式  字节数和时间数据内容长度
                return;
            }
        }
        //      for (i = 0; i < RegNum; i += lRtu2018CompRegMap.get(nowCompName).get(regAddr).getDataLen(), regAddr += lRtu2018CompRegMap.get(nowCompName).get(regAddr).getDataLen())
        {
            if (lRtu2018CompRegMap.get(nowCompName).get(startRegAddr).getRw().equals("W") ||
                    lRtu2018CompRegMap.get(nowCompName).get(startRegAddr).getRw().equals("WR")) {
                switch (lRtu2018CompRegMap.get(nowCompName).get(startRegAddr).getDataType()) {
                    case "WORD":
                        data = (((short) rs[dataBit] & 0xff) << 8) | (rs[dataBit + 1] & 0xff);//获取寄存器起始地址
                        dataBit += 2;
                        break;
                    case "FLOAT":
                        dataBit += 4;
                        break;
                    case "DWORD":
                        dataBit += 4;
                        break;
                }
                if (nowCompName.equals("SYSTEM")) {
                    //对模式切换指令不限制
                    for (String item : strComponent.get(1)) {
                        if (!getModePermissions(item, "反控") && (data != 26 && data != 32 && data != 34)) {
                            sendModbusErrorCode(port, sCom, rs, 6);
                            return;
                        }
                    }
                    final String[] flowStr = new String[]{"", context.getString(R.string.SDZY), "停止测量"};
                    if (data < flowStr.length) {
                        if (!flowStr[data].equals("")) {
                            final int finalData = data;
                            if (!flowStr[finalData].equals("停止测量")) {
                                for (String item : strComponent.get(1)) {
                                    if ((!doFlowing.get(item).equals(context.getString(R.string.waiting_for_instructions)))) {
                                        sendModbusErrorCode(port, sCom, rs, 6);
                                        return;
                                    }
                                }
                            }
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    if (flowStr[finalData].equals("停止测量")) {
                                        for (String item : strComponent.get(1)) {
                                            stopWorking(item, false);
                                            saveRunInfo2File("组分[" + nowCompName + "] RTU2018-停止测量");
                                        }
                                    } else {
                                        for (String item : strComponent.get(1)) {
                                            if (doFlowing.get(item).equals(context.getString(R.string.waiting_for_instructions))) {
                                                doControlJob(item, flowStr[finalData]);
                                                saveRunInfo2File("组分[" + nowCompName + "] RTU2018-启动 " + flowStr[finalData]);
                                            }
                                        }
                                    }
                                }
                            }).start();
                        } else {
                            dataErrFlag = true;
                        }
                    } else {
                        dataErrFlag = true;
                    }
                    if (dataErrFlag) {
                        sendModbusErrorCode(port, sCom, rs, 3);
                        return;
                    }
                } else {
                    //对模式切换指令不限制
                    if (!getModePermissions(nowCompName, "反控") && data != 26 && data != 60) {
                        sendModbusErrorCode(port, sCom, rs, 6);
                        return;
                    }
                    switch (data) {
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                        case 5:
                        case 6:
                        case 7:
                        case 8:
                        case 9:
                        case 10:
                        case 11:
                        case 0x14:
                        case 0x15:
                        case 0x16:
                        case 0x1d:
                        case 32:
                        case 33:
                        case 35:
                        case 36:
                        case 38:
                        case 39:
                            final String[] flowStr = new String[]{"", context.getString(R.string.SDZY), context.getString(R.string.SDBYHC),
                                    context.getString(R.string.SDLDHC), context.getString(R.string.SDKDHC), context.getString(R.string.SDLYCL),
                                    context.getString(R.string.SDPXY), context.getString(R.string.SDJBHS), context.getResources().getString(R.string.SDB1),
                                    context.getResources().getString(R.string.SDB2), context.getString(R.string.SDYBQX), "停止测量",
                                    "", "", "",// 14
                                    "", "", "",// 17
                                    "", "", context.getString(R.string.SDJZ),
                                    context.getString(R.string.SDBYCL), context.getString(R.string.SDBY2CL), "",//23
                                    "", "", "",
                                    "", "", context.getString(R.string.SDCSZY),
                                    "", "", "",
                                    "", "", context.getString(R.string.SDGYWDX),
                                    context.getString(R.string.SDJYWDX), "", context.getResources().getString(R.string.SDXSJZ),
                                    context.getResources().getString(R.string.SDGLCX), "", ""};

                            if (!flowStr[data].equals("")) {
                                final int finalData = data;
                                if (!flowStr[finalData].equals("停止测量") && (!doFlowing.get(nowCompName).equals(context.getString(R.string.waiting_for_instructions)))) {
                                    sendModbusErrorCode(port, sCom, rs, 6);
                                    return;
                                } else {
                                    if (flowStr[finalData].equals(context.getResources().getString(R.string.SDB1))) {
                                        if (!isB1CalEnable(nowCompName, getConfigData(nowCompName, "RANGE"))) {
                                            sendModbusErrorCode(port, sCom, rs, 3);
                                            return;
                                        }
                                    } else if (flowStr[finalData].equals(context.getResources().getString(R.string.SDB2))) {
                                        if (!isB2CalEnable(nowCompName, getConfigData(nowCompName, "RANGE"))) {
                                            sendModbusErrorCode(port, sCom, rs, 3);
                                            return;
                                        }
                                    }
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (flowStr[finalData].equals("停止测量")) {
                                                stopWorking(nowCompName, false);
                                                saveRunInfo2File("组分[" + nowCompName + "] RTU2018-停止测量");
                                            } else {
                                                //反控启动标样核查时，将多点线性标志位修改为true，出数时参与修正
                                                if (flowStr[finalData].equals(context.getString(R.string.SDBYHC)) && getPublicConfigData("Multi_Point_Linear_Features").equals("true")) {
                                                    updateConfigData(nowCompName, "Multi_Point_Linear", "true");
                                                }
                                                if (doFlowing.get(nowCompName).equals(context.getString(R.string.waiting_for_instructions))) {
                                                    doControlJob(nowCompName, flowStr[finalData]);
                                                    saveRunInfo2File("组分[" + nowCompName + "] RTU2018-启动 " + flowStr[finalData]);
                                                }
                                            }
                                        }
                                    }).start();
                                }
                            } else {
                                dataErrFlag = true;
                            }
                            if (dataErrFlag) {
                                sendModbusErrorCode(port, sCom, rs, 3);
                                return;
                            }
                            break;
                        case 12:
                            Message msg = new Message();
                            msg.what = 110;
                            WinWidgetHandler.sendMessage(msg);
                            break;
                        case 13:// 设置时间
                            boolean errFlag = false;
                            try {
                                byte[] str = new byte[6];
                                for (i = 0; i < 6; i++) {
                                    str[i] = rs[9 + i];
                                }
                                String strs = bcd2Str(str);
                                if (strs.length() == 12) {
                                    int year = Integer.parseInt(strs.substring(0, 2)) + 2000;
                                    int month = Integer.parseInt(strs.substring(2, 4));
                                    int day = Integer.parseInt(strs.substring(4, 6));
                                    int hour = Integer.parseInt(strs.substring(6, 8));
                                    int minute = Integer.parseInt(strs.substring(8, 10));
                                    int second = Integer.parseInt(strs.substring(10, 12));
                                    if (year > 2000 && year < 3000 && month > 0 && month < 13 && day > 0 && day < 32 && hour >= 0 && hour < 24 && minute >= 0 && minute < 60 && second >= 0 && second < 60) {
                                        saveOperationLogMsg(nowCompName, "RTU2018-设置时间" + year + "/" + month + "/" + day + " " + hour + ":" + minute + ":" + second, ErrorLog.msgType.操作_信息);
                                        //时间矫正
                                        /*AddError(nowCompName, 614, 运维_信息);
                                        setDate(year, month - 1, day);
                                        setTime(hour, minute, second);*/
                                        SetDateTime(nowCompName, year, month - 1, day, hour, minute, second);
                                        byte[] timeByte = copybyte(toByteArray(year, 4), toByteArray((month), 4),
                                                toByteArray(day, 4), toByteArray(hour, 4),
                                                toByteArray(minute, 4), toByteArray((second), 4));
                                        SendManager.SendCmd(nowCompName + "_时间管理_06_0", S0, 3, 200, timeByte);
                                    } else {
                                        errFlag = true;
                                    }
                                } else {
                                    errFlag = true;
                                }
                            } catch (Exception e) {

                            }
                            /*catch (IOException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }*/
                            sendSysTimeToDev(null);
                            if (errFlag) {
                                sendModbusErrorCode(port, sCom, rs, 3);
                                return;
                            }
                            break;
                        case 14:// 测量模式
                            int meaMode = (((short) rs[9] & 0xff) << 8) | (rs[10] & 0xff);
                            if (meaMode > 0 && meaMode < 6) {
                                if (setMeaModeSpinnerSelect(nowCompName, meaMode)) {
                                    switch (meaMode) {
                                        case 1://连续测量模式
                                            AddError(nowCompName, 609, 运维_信息);
                                            break;
                                        case 2://周期测量模式
                                            AddError(nowCompName, 610, 运维_信息);
                                            break;
                                        case 3://定点模式
                                            AddError(nowCompName, 608, 运维_信息);
                                            break;
                                        case 4://受控模式  、手动模式
                                            if (getConfigData(nowCompName, "runningMode").equals("1")) {
                                                //在线   受控模式
                                                AddError(nowCompName, 612, 运维_信息);
                                            } else {
                                                //维护   手动模式
                                                AddError(nowCompName, 611, 运维_信息);
                                            }
                                            break;
                                    }
                                    if (meaMode == 5) {
                                        // 维护 手动 5 改 4显示
                                        meaMode = 4;
                                    }
                                    saveOperationLogDataModifyMsg(nowCompName, "meaMode", String.valueOf(meaMode), "RTU-2018测量模式", ErrorLog.msgType.操作_信息);
                                    updateConfigData(nowCompName, "meaMode", String.valueOf(meaMode));
                                    MeaModeSetting(nowCompName, String.valueOf(meaMode));
                                } else {
                                    sendModbusErrorCode(port, sCom, rs, 3);
                                    return;
                                }
                            } else {
                                sendModbusErrorCode(port, sCom, rs, 3);
                                return;
                            }
                            break;
                        case 15://测量间隔设置
                            int lxclh, lxclm;
                            int lxclt = (((short) rs[9] & 0xff) << 8) | (rs[10] & 0xff);
                            lxclh = lxclt / 60;
                            lxclm = lxclt - lxclh * 60;

                            if ((lxclm < 60 && lxclm > -1) && ((lxclh < 24 && lxclh >= 0))) {
                                if (lxclh == 0 && lxclm < 2) {
                                    sendModbusErrorCode(port, sCom, rs, 3);
                                    return;
                                }
                                saveOperationLogDataModifyMsg(nowCompName, "lxclm", String.valueOf(lxclm), "RTU2018-连续测量分", ErrorLog.msgType.操作_信息);
                                saveOperationLogDataModifyMsg(nowCompName, "lxclh", String.valueOf(lxclh), "RTU2018-连续测量时", ErrorLog.msgType.操作_信息);
                                updateConfigData(nowCompName, "lxclm", String.valueOf(lxclm));
                                updateConfigData(nowCompName, "lxclh", String.valueOf(lxclh));
                                int time = (lxclh * 60 + lxclm);
                                getTimer_cycle(nowCompName).setTime(time, 0);
                                getTimer_cycle(nowCompName).resume(0);
                            } else {
                                sendModbusErrorCode(port, sCom, rs, 3);
                                return;
                            }
                            break;
                        case 23:
                            int hour = (((short) rs[9] & 0xff) << 8) | (rs[10] & 0xff);
                            int day = (((short) rs[11] & 0xff) << 8) | (rs[12] & 0xff);
                            if (day >= 0 && day < 32 && (hour >= 0 && hour < 24)) {

                                saveOperationLogDataModifyMsg(nowCompName, "zdjzt", String.valueOf(day), "RTU2018-自动校准天", ErrorLog.msgType.操作_信息);
                                saveOperationLogDataModifyMsg(nowCompName, "zdjzh", String.valueOf(hour), "RTU2018-自动校准启动时间", ErrorLog.msgType.操作_信息);
                                updateConfigData(nowCompName, "zdjzt", String.valueOf(day));
                                updateConfigData(nowCompName, "zdjzh", String.valueOf(hour));
                                getTimer_cycle(nowCompName).setTime(day, 1);
                                getTimer_cycle(nowCompName).setTime(hour, 2);
                                getTimer_cycle(nowCompName).setCalibDate(Calendar.getInstance());
                                getTimer_cycle(nowCompName).resume(1);
                            } else {
                                sendModbusErrorCode(port, sCom, rs, 3);
                                return;
                            }
                            break;
                        case 24:
                            long tnum = ((long) (rs[9] & 0xff) << 32) | ((int) (rs[10] & 0xff) << 16) | ((short) (rs[11] & 0xff) << 8) | ((rs[12] & 0xff));
                            if (tnum <= 0xffffff && tnum > 0) {
                                StringBuilder sb = new StringBuilder();
                                for (i = 0; i < 24; i++) {
                                    if ((tnum & (0x01 << i)) != 0) {
                                        sb.append(i + "，");
                                    }
                                }
                                String s = sb.toString();
                                if (s.length() > 0) s = s.substring(0, s.length() - 1);
                                saveOperationLogDataModifyMsg(nowCompName, "zqclh", s, "RTU2018-周期测量时间", ErrorLog.msgType.操作_信息);
                                updateConfigData(nowCompName, "zqclh", s);
                                syncAutoDoSample_topHour();
                            } else {
                                sendModbusErrorCode(port, sCom, rs, 3);
                                return;
                            }
                            break;
                        case 25://清洗周期设置
                            int cleanTimes = (((short) rs[9] & 0xff) << 8) | (rs[10] & 0xff);
                            if (cleanTimes < 1 || cleanTimes > 1104) {
                                sendModbusErrorCode(port, sCom, rs, 3);
                                return;
                            } else {
                                updateConfigData(nowCompName, "zdqxTimes", "0");
                                saveOperationLogDataModifyMsg(nowCompName, "zdqxh", String.valueOf(cleanTimes), "RTU2018-自动清洗间隔", ErrorLog.msgType.操作_信息);
                                updateConfigData(nowCompName, "zdqxh", String.valueOf(cleanTimes));
                                getTimer_cycle(nowCompName).setTime(cleanTimes, 3);
                                getTimer_cycle(nowCompName).resume(2);
                            }
                            break;
                        case 26://运行模式 1在线 2 离线 3 维护
                            int runningStatus = (((short) rs[9] & 0xff) << 8) | (rs[10] & 0xff);
                            if (runningStatus > 0 && runningStatus < 4) {
                                switch (runningStatus) {
                                    case 1://在线
                                        AddError(nowCompName, 606, 运维_信息);
                                        break;
                                    case 2://离线
                                        AddError(nowCompName, 607, 运维_信息);
                                        break;
                                    case 3://维护
                                        AddError(nowCompName, 605, 运维_信息);
                                        break;
                                }
                                saveOperationLogDataModifyMsg(nowCompName, "runningMode", String.valueOf(runningStatus), "RUT2018-运行模式", ErrorLog.msgType.操作_信息);
                                updateConfigData(nowCompName, "runningMode", String.valueOf(runningStatus));
                                // 离线
                                if (runningStatus == 1) {
                                    ZDZYStatusSetting(nowCompName, false);
                                    LXCLStatusSetting(nowCompName, false, getConfigData(nowCompName, "lxclh"), getConfigData(nowCompName, "lxclm"));
                                    ZQCLStatusSetting(nowCompName, false);
                                    // 关闭自动校准，自动清洗
                                    ZDJZStatusSetting(nowCompName, false);
                                    ZDQXStatusSetting(nowCompName, false);
                                } else {
                                    MeaModeSetting(nowCompName, String.valueOf(getConfigData(nowCompName, "meaMode")));
                                }
                            } else {
                                sendModbusErrorCode(port, sCom, rs, 3);
                                return;
                            }
                            break;
                        case 27:// 自动校准开启关闭
                            int switchStatus = (((short) rs[9] & 0xff) << 8) | (rs[10] & 0xff);
                            if (switchStatus == 1 || switchStatus == 2) {
                                if (getConfigData(nowCompName, "ZDJZ").equals("false")) {
                                    //updateConfigData(nowCompName, "zdjzOnceFlag", "false");
                                }
                                saveOperationLogDataModifyMsg(nowCompName, "ZDJZ", (switchStatus == 1 ? "true" : "false"), "RTU2018修改-自动校准开关", ErrorLog.msgType.操作_信息);
                                updateConfigData(nowCompName, "ZDJZ", switchStatus == 1 ? "true" : "false");
                                if (switchStatus == 1) {
                                    String autoCalibDay = getConfigData(nowCompName, "zdjzt");
                                    String autoCalibHour = getConfigData(nowCompName, "zdjzh");

                                    getTimer_cycle(nowCompName).setTime((int) (Float.parseFloat(autoCalibDay.equals("") ? "0" : autoCalibDay)), 1);
                                    getTimer_cycle(nowCompName).setTime((int) (Float.parseFloat(autoCalibHour.equals("") ? "0" : autoCalibHour)), 2);
                                    getTimer_cycle(nowCompName).setCalibDate(Calendar.getInstance());
                                    getTimer_cycle(nowCompName).resume(1);
                                } else {
                                    getTimer_cycle(nowCompName).suspend(1);
                                }
                            } else {
                                sendModbusErrorCode(port, sCom, rs, 3);
                                return;
                            }
                            break;
                        case 28:// 自动清洗状态设置
                            int cleanSwitchStatus = (((short) rs[9] & 0xff) << 8) | (rs[10] & 0xff);
                            if (cleanSwitchStatus == 1 || cleanSwitchStatus == 2) {
                                saveOperationLogDataModifyMsg(nowCompName, "ZDQX", (cleanSwitchStatus == 1 ? "true" : "false"), "RTU-2018自动清洗开关", ErrorLog.msgType.操作_信息);
                                updateConfigData(nowCompName, "ZDQX", cleanSwitchStatus == 1 ? "true" : "false");

                                if (cleanSwitchStatus == 1) {
                                    String autoWashCount = getConfigData(nowCompName, "zdqxh");
                                    int time = Integer.parseInt(autoWashCount.equals("0") ? "0" : autoWashCount);
                                    if (time == 0)
                                        return;
                                    getTimer_cycle(nowCompName).setTime((int) (Float.parseFloat(autoWashCount.equals("") ? "0" : autoWashCount)), 3);
                                    getTimer_cycle(nowCompName).resume(2);
                                } else {
                                    updateConfigData(nowCompName, "zdqxTimes", "0");
                                    getTimer_cycle(nowCompName).suspend(2);
                                }
                            } else {
                                sendModbusErrorCode(port, sCom, rs, 3);
                                return;
                            }
                            break;
                        case 30:// 数据提取
                            boolean errFlag2 = false;
                            try {
                                String startTime = null;
                                String endTime = null;
                                byte[] str1 = new byte[6];
                                for (i = 0; i < 6; i++) {
                                    str1[i] = rs[9 + i];
                                }
                                String strs1 = bcd2Str(str1);
                                byte[] str2 = new byte[6];
                                for (i = 0; i < 6; i++) {
                                    str2[i] = rs[15 + i];
                                }
                                String strs2 = bcd2Str(str2);
                                if (strs1.length() == 12) {
                                    int year1 = Integer.parseInt(strs1.substring(0, 2)) + 2000;
                                    int month1 = Integer.parseInt(strs1.substring(2, 4));
                                    int day1 = Integer.parseInt(strs1.substring(4, 6));
                                    int hour1 = Integer.parseInt(strs1.substring(6, 8));
                                    int minute1 = Integer.parseInt(strs1.substring(8, 10));
                                    int second1 = Integer.parseInt(strs1.substring(10, 12));
                                    if (year1 > 2000 && year1 < 3000 && month1 > 0 && month1 < 13 && day1 > 0 && day1 < 32 && hour1 >= 0 && hour1 < 24 && minute1 >= 0 && minute1 < 60 && second1 >= 0 && second1 < 60) {
                                        startTime = year1 + "-" + strs1.substring(2, 4) + "-" + strs1.substring(4, 6) + " " + strs1.substring(6, 8) + ":" + strs1.substring(8, 10) + ":" + strs1.substring(10, 12);
                                    } else {
                                        errFlag2 = true;
                                    }
                                } else {
                                    errFlag2 = true;
                                }
                                if (strs2.length() == 12) {
                                    int year1 = Integer.parseInt(strs2.substring(0, 2)) + 2000;
                                    int month1 = Integer.parseInt(strs2.substring(2, 4));
                                    int day1 = Integer.parseInt(strs2.substring(4, 6));
                                    int hour1 = Integer.parseInt(strs2.substring(6, 8));
                                    int minute1 = Integer.parseInt(strs2.substring(8, 10));
                                    int second1 = Integer.parseInt(strs2.substring(10, 12));
                                    if (year1 > 2000 && year1 < 3000 && month1 > 0 && month1 < 13 && day1 > 0 && day1 < 32 && hour1 >= 0 && hour1 < 24 && minute1 >= 0 && minute1 < 60 && second1 >= 0 && second1 < 60) {
                                        endTime = year1 + "-" + strs2.substring(2, 4) + "-" + strs2.substring(4, 6) + " " + strs2.substring(6, 8) + ":" + strs2.substring(8, 10) + ":" + strs2.substring(10, 12);
                                    } else {
                                        errFlag2 = true;
                                    }
                                } else {
                                    errFlag2 = true;
                                }
                                if (errFlag2) {
                                    sendModbusErrorCode(port, sCom, rs, 3);
                                    return;
                                }
                                List<Map> list;
                                History mHistory;
                                mHistory = new History(context);
                                //数据提取时，维护数据不上传     sj:2020年2月20日 17:04:51
                                list = mHistory.select(nowCompName, context.getResources().getString(R.string.ZY), "M", startTime, endTime, 0, 1000);
                                if (list.size() > 0) {
                                    saveOperationLogMsg(nowCompName, "RTU2018-数据提取", ErrorLog.msgType.操作_信息);
                                    byte[] sendBytes = new byte[6];
                                    sendBytes[0] = rs[0];
                                    sendBytes[1] = rs[1];
                                    sendBytes[2] = rs[2];
                                    sendBytes[3] = rs[3];
                                    sendBytes[4] = rs[4];
                                    sendBytes[5] = rs[5];
                                    for (int c = list.size() - 1; c >= 0; c--) {
                                        byte[] timeBytes = new byte[6];
                                        String timeStr = list.get(c).get("time").toString();
                                        timeStr = timeStr.replace(" ", "").replace("-", "").replace(":", "");
                                        timeBytes = DataUtil.HexString2Bytes(timeStr.substring(2));
                                        sendBytes = copybyte(sendBytes, timeBytes);
                                        String strData = list.get(c).get("C").toString();
                                        sendBytes = copybyte(sendBytes, toBytes(strData, getPublicConfigData("ModbusRtu2018_ANALYSIS_MODE"), 2));
                                    }

                                    if (getPublicConfigData("ModbusRtu2018_CRC_MODE").equals("2")) {
                                        byte[] crc = crc16(sendBytes, sendBytes.length);
                                        DataUtil.reverse(crc);
                                        sendBytes = copybyte(sendBytes, crc);
                                    } else {
                                        sendBytes = copybyte(sendBytes, crc16(sendBytes, sendBytes.length));
                                    }

                                    rS485_SendData(port, sCom, sendBytes);
                                    return;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case 31: {
                            try {
                                resetSurplusBottle(nowCompName);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                        case 34: {
                            //选择仪表当前量程
                            int range = (((short) rs[9] & 0xff) << 8) | (rs[10] & 0xff);
                            if (range > 3 || range < 1 || (range > GetPlatRangSum(nowCompName))) {
                                //当量程值不为1-3时 就报错:非法数据值
                                //当只有2个量程时，设置量程3就报错:非法数据值
                                sendModbusErrorCode(port, sCom, rs, 3);
                                return;
                            }
                            if (!doFlowing.get(nowCompName).equals(context.getString(R.string.waiting_for_instructions))) {
                                sendModbusErrorCode(port, sCom, rs, 6);
                                return;
                            }
                            setUseRange(nowCompName, String.valueOf(range));
                        }
                        break;
                        case 37:
                            //重置废液
                            try {
                                ResetWaste(nowCompName);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        default:
                            sendModbusErrorCode(port, sCom, rs, 3);
                            return;
                    }
                }
            } else {
                /*不可写只读数据*/
                sendModbusErrorCode(port, sCom, rs, 3);
                return;
            }
        }
        byte[] sendBytes = new byte[6];
        sendBytes[0] = rs[0];
        sendBytes[1] = rs[1];
        sendBytes[2] = rs[2];
        sendBytes[3] = rs[3];
        sendBytes[4] = rs[4];
        sendBytes[5] = rs[5];

        if (getPublicConfigData("ModbusRtu2018_CRC_MODE").equals("2")) {
            byte[] crc = crc16(sendBytes, sendBytes.length);
            DataUtil.reverse(crc);
            sendBytes = copybyte(sendBytes, crc);
        } else {
            sendBytes = copybyte(sendBytes, crc16(sendBytes, sendBytes.length));
        }

        rS485_SendData(port, sCom, sendBytes);
    }


    /**
     * 发送数据
     */
    private static String rS485_SendData(Communication port, int sCom, byte[] dataBytes) {

        SendManager.SendCmd("IO" + "_" + ((!IOBoardUsed) ? "打印输出_0_0" : "ModbusRtu_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 500, dataBytes);
        return bytesToHexString(dataBytes, dataBytes.length);
    }


    /**
     * 发送异常码
     */
    private static void sendModbusErrorCode(Communication port, int sCom, byte[] rs, int err) {
        //寄存器地址+数量超出范围
        byte[] sendBytes = new byte[3];
        sendBytes[0] = rs[0];
        sendBytes[1] = (byte) ((rs[1] | 0x80) & 0xff);
        sendBytes[2] = (byte) err;//异常码
        byte[] crc = crc16(sendBytes, sendBytes.length - 2);
        if (getPublicConfigData("ModbusRtu2018_CRC_MODE").equals("2")) {
            DataUtil.reverse(crc);
        }
        sendBytes = copybyte(sendBytes, crc);
        rS485_SendData(port, sCom, sendBytes);
    }

    /**
     * TCP 传输的心跳包
     *
     * @param strProtocol
     * @return
     */
    public static byte[] getTcpHeartBeatMsg(String strProtocol) {
        byte[] head = {(byte) (0xA1 & 0xff), (byte) (0xA2 & 0xff), (byte) (0xA3 & 0xff)};
        byte[] end = {(byte) (0xB1 & 0xff), (byte) (0xB2 & 0xff), (byte) (0xB3 & 0xff)};
        byte[] sendBytes = null;
        byte[] isSystem = {0};
        byte[] sumId = {0};
        sumId[0] = (byte) strComponent.get(1).length;
        if (strProtocol.equals(protocolName[4])) {
            byte[] protocolCode = {0x01};
            if (sumId[0] > 0) {
                byte[] rtuId = new byte[sumId[0]];
                for (int i = 0; i < sumId[0]; i++) {
                    rtuId[i] = Byte.parseByte(getConfigData(strComponent.get(1)[i], "RTU_ID"));
                }
                sendBytes = copybyte(head, protocolCode, isSystem, sumId, rtuId);
                byte[] crcData = new byte[sendBytes.length - 3];
                arraycopy(sendBytes, 3, crcData, 0, crcData.length);

                if (getPublicConfigData("ModbusRtu2018_CRC_MODE").equals("2")) {
                    byte[] crc = crc16(crcData, crcData.length);
                    DataUtil.reverse(crc);
                    sendBytes = copybyte(sendBytes, crc, end);
                } else {
                    sendBytes = copybyte(sendBytes, crc16(crcData, crcData.length), end);
                }
            }
        }
        return sendBytes;
    }


    /**
     * 历史数据提取
     *
     * @param rs
     * @param startTime
     * @param endTime
     * @return
     */
    private static void getHistoryData(Communication port, int sCom, byte[] rs, String startTime, String endTime, String sFlow, int iStartIndex, int iEndIndex) {
        List<Map> list;
        History mHistory;
        mHistory = new History(context);
        list = mHistory.select(nowCompName, sFlow, startTime, endTime, iStartIndex, iEndIndex);
        try {
            if (list.size() > 0) {
                if (list.size() > 10) {
                    sendModbusErrorCode(port, sCom, rs, 12);
                } else {
                    rS485_SendData(port, sCom, getDataBytes(rs, list));
                }
            } else {
                sendModbusErrorCode(port, sCom, rs, 11);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 数据提取封装数据包
     *
     * @param rs
     * @param list
     * @return
     */
    private static byte[] getDataBytes(byte[] rs, List<Map> list) {
        saveOperationLogMsg(nowCompName, "RTU2018-文件提取", ErrorLog.msgType.操作_信息);
        byte[] sendBytes = new byte[7];
        sendBytes[0] = rs[0];
        sendBytes[1] = rs[1];
        sendBytes[2] = rs[2];
        sendBytes[3] = rs[3];
        sendBytes[4] = (byte) list.size();

        for (int c = list.size() - 1; c >= 0; c--) {
            byte[] timeBytes = new byte[6];
            String strId = list.get(c).get("id").toString();
            String timeStr = list.get(c).get("time").toString();
            timeStr = timeStr.replace(" ", "").replace("-", "").replace(":", "");
            timeBytes = DataUtil.HexString2Bytes(timeStr.substring(2));
            String strData = list.get(c).get("C").toString();
            String strDataTag = list.get(c).get("tag").toString();
            String strFlowName = list.get(c).get("flow").toString();
            String strAi = list.get(c).get("A").toString();
            /*能量解析*/
            String strEnSum = String.valueOf(0);    // 能量个数
            String sDataTypeCode = String.valueOf(1);// 数据类型编号
            String strEn = list.get(c).get("energy").toString();
            byte[] enBytes = new byte[0];
            if (!strEn.equals("")) {
                String[] strEns = strEn.substring(0, strEn.length() - 1).split(";");
                if (strEns.length > 0) {
                    //能量个数
                    strEnSum = String.valueOf(strEns.length);
                    // 能量类型
                    String sDataType = strEns[0].contains(".") ? getPublicConfigData("ModbusRtu2018_ANALYSIS_MODE") : "WORD";
                    sDataTypeCode = sDataType.equals("WORD") ? "1" : "2";
                    int enTypeLen = sDataTypeCode.equals("1") ? 2 : 4;
                    for (int i = 0; i < Integer.parseInt(strEnSum); i++) {
                        enBytes = copybyte(enBytes, toBytes(strEns[i], sDataType, enTypeLen));
                    }
                }
            }

            sendBytes = copybyte(sendBytes, toBytes(strId, "DWORD", 4), timeBytes, toBytes(strData, getPublicConfigData("ModbusRtu2018_ANALYSIS_MODE"), 2),
                    toBytes(String.valueOf(getDataFlowCode(strFlowName)), "WORD", 2), toBytes(strDataTag, "CHAR[]", 6),
                    toBytes(strAi, getPublicConfigData("ModbusRtu2018_ANALYSIS_MODE"), 2), toBytes(strEnSum, "WORD", 2), toBytes(sDataTypeCode, "WORD", 2), enBytes);
        }
        int iDataLen = (sendBytes.length - 7) / list.size();
        System.arraycopy(toBytes(String.valueOf(iDataLen), "WORD", 2), 0, sendBytes, 5, 2);

        if (getPublicConfigData("ModbusRtu2018_CRC_MODE").equals("2")) {
            byte[] crc = crc16(sendBytes, sendBytes.length);
            DataUtil.reverse(crc);
            sendBytes = copybyte(sendBytes, crc);
        } else {
            sendBytes = copybyte(sendBytes, crc16(sendBytes, sendBytes.length));
        }


        return sendBytes;
    }

    /**
     * @param rs   包含时间的字节数组  190704140000
     * @param sBit 数组起始地址
     * @return null 则解析失败
     */
    private static String getReDataTime(byte[] rs, int sBit) {
        String sTime = null;
        byte[] str1 = new byte[6];
        System.arraycopy(rs, sBit, str1, 0, 6);
        String sRs = bcd2Str(str1);
        if (sRs.length() == 12) {
            int year1 = Integer.parseInt(sRs.substring(0, 2)) + 2000;
            int month1 = Integer.parseInt(sRs.substring(2, 4));
            int day1 = Integer.parseInt(sRs.substring(4, 6));
            int hour1 = Integer.parseInt(sRs.substring(6, 8));
            int minute1 = Integer.parseInt(sRs.substring(8, 10));
            int second1 = Integer.parseInt(sRs.substring(10, 12));
            if (year1 > 2000 && year1 < 3000 && month1 > 0 && month1 < 13 && day1 > 0 && day1 < 32 && hour1 >= 0 && hour1 < 24 && minute1 >= 0 && minute1 < 60 && second1 >= 0 && second1 < 60) {
                sTime = year1 + "-" + sRs.substring(2, 4) + "-" + sRs.substring(4, 6) + " " + sRs.substring(6, 8) + ":" + sRs.substring(8, 10) + ":" + sRs.substring(10, 12);
            }
        }
        return sTime;

    }


    /**
     * 数据类型获取编号
     *
     * @param flowName 流程名称
     * @return 返回流程名称对应的编码
     */
    private static int getDataFlowCode(String flowName) {
        int flowCode = 0;

        if (flowName.contentEquals(context.getText(R.string.ZY))) {
            flowCode = 1;
        } else if (flowName.contentEquals(context.getText(R.string.BYHC))) {
            flowCode = 2;
        } else if (flowName.contentEquals(context.getText(R.string.LDHC))) {
            flowCode = 3;
        } else if (flowName.contentEquals(context.getText(R.string.KDHC))) {
            flowCode = 4;
        } else if (flowName.contentEquals(context.getText(R.string.LYCL))) {
            flowCode = 5;
        } else if (flowName.contentEquals(context.getText(R.string.PXY))) {
            flowCode = 6;
        } else if (flowName.contentEquals(context.getText(R.string.JBHS))) {
            flowCode = 7;
        } else if (flowName.contentEquals(context.getText(R.string.B1))) {
            flowCode = 8;
        } else if (flowName.contentEquals(context.getText(R.string.B2))) {
            flowCode = 9;
        } else if (flowName.contentEquals(context.getText(R.string.BYCL))) {
            flowCode = 21;
        } else if (flowName.contentEquals(context.getText(R.string.BY2CL))) {
            flowCode = 22;
        }
        return flowCode;
    }


    /**
     * 根据流程编码获取名称
     *
     * @param flowCode
     * @return
     */
    private static String getFlowName(int flowCode) {
        String flowName = "";
        switch (flowCode) {
            case 1:
                flowName = String.valueOf(context.getText(R.string.ZY));
                break;
            case 2:
                flowName = String.valueOf(context.getText(R.string.BYHC));
                break;
            case 3:
                flowName = String.valueOf(context.getText(R.string.LDHC));
                break;
            case 4:
                flowName = String.valueOf(context.getText(R.string.KDHC));
                break;
            case 5:
                flowName = String.valueOf(context.getText(R.string.LYCL));
                break;
            case 6:
                flowName = String.valueOf(context.getText(R.string.PXY));
                break;
            case 7:
                flowName = String.valueOf(context.getText(R.string.JBHS));
                break;
            case 8:
                flowName = String.valueOf(context.getText(R.string.B1));
                break;
            case 9:
                flowName = String.valueOf(context.getText(R.string.B2));
                break;
            case 21:
                flowName = String.valueOf(context.getText(R.string.BYCL));
                break;
            case 22:
                flowName = String.valueOf(context.getText(R.string.BY2CL));
                break;
        }

        return flowName;
    }


    /**
     * 检查数据包内容
     *
     * @param map
     * @return
     */
    private static boolean checkDataHistoryPackage(Map<String, Object> map) {
        boolean blDataOk = false;
        String sGetTime = (String) map.get("time");
        String sGetComponent = (String) map.get("component");
        String sGetC = (String) map.get("C");
        String sGetUnit = (String) map.get("unit");
        String sGetFlow = (String) map.get("flow");
        String sGetA = (String) map.get("A");
        String sGetEnergy = (String) map.get("energy");
        String sGettemperature = (String) map.get("temperature");
        String sGettag = (String) map.get("tag");

        if (!sGetTime.equals("")) {
            int year = getHistoryTimeStringByYear(sGetTime);
            int month = getHistoryTimeStringByMonth(sGetTime);
            int day = getHistoryTimeStringByDay(sGetTime);
            int hour = getHistoryTimeStringByHour(sGetTime);
            int min = getHistoryTimeStringByMin(sGetTime);
            int sec = getHistoryTimeStringBySec(sGetTime);
            if (year > 2000 && year < 3000 && month > 0 && month < 13 && day > 0 && day < 32 && hour >= 0 && hour < 24 && min >= 0 && min < 60 && sec >= 0 && sec < 60) {
                blDataOk = true;
            }
            // 有这个组分
            if (strComponent.get(1).length > 0) {
                for (String item : strComponent.get(1)) {
                    if (item.equals(sGetComponent)) {
                        blDataOk = true;
                        break;
                    }
                }
            }
            // 能量个数
            String[] strEns = sGetEnergy.substring(0, sGetEnergy.length() - 1).split(";");
            if (strEns.length < 31) {
                blDataOk = true;
            }
            //检查添加的流程名称
            String[] sFlow = context.getResources().getStringArray(R.array.flow);
            for (String item : sFlow) {
                if (item.contains(sGetFlow)) {
                    blDataOk = true;
                }
            }
        }
        return blDataOk;
    }
}
