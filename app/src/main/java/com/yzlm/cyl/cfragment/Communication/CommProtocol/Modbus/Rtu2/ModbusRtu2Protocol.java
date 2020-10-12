package com.yzlm.cyl.cfragment.Communication.CommProtocol.Modbus.Rtu2;

import android.support.annotation.NonNull;

import com.yzlm.cyl.cfragment.Communication.Communication;
import com.yzlm.cyl.cfragment.Communication.Thread.SendManager;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.Util.DataUtil;

import java.util.Arrays;

import static com.yzlm.cyl.cfragment.Cal.Component.CalFunc.isB1CalEnable;
import static com.yzlm.cyl.cfragment.Cal.Component.CalFunc.isB2CalEnable;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.updateConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogDataModifyMsg;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogMsg;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.getPublicConfigData;
import static com.yzlm.cyl.cfragment.Content.Component.List2.List2_Content2.getModePermissions;
import static com.yzlm.cyl.cfragment.DBConvert.MDBConvert.toBytes;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.SetDateTime;
import static com.yzlm.cyl.cfragment.Frame.Content.Up_content3.stopWorking;
import static com.yzlm.cyl.cfragment.Global.IOBoardUsed;
import static com.yzlm.cyl.cfragment.Global.S0;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.doControlJob;
import static com.yzlm.cyl.cfragment.Global.doFlowing;
import static com.yzlm.cyl.cfragment.Global.getTimer_cycle;
import static com.yzlm.cyl.cfragment.Global.lRtu2CompRegMap;
import static com.yzlm.cyl.cfragment.Global.saveRunInfo2File;
import static com.yzlm.cyl.cfragment.Global.strComponent;
import static com.yzlm.cyl.cfragment.Global.syncAutoDoSample_topHour;
import static com.yzlm.cyl.clibrary.Util.DataUtil.bytesToHexString;
import static com.yzlm.cyl.clibrary.Util.DataUtil.copybyte;
import static com.yzlm.cyl.clibrary.Util.DataUtil.crc16;
import static com.yzlm.cyl.clibrary.Util.DataUtil.toByteArray;
import static java.lang.System.arraycopy;

public class ModbusRtu2Protocol {
    private static String nowCompName = "";

    public static void ParsingProtocolRtu2(Communication port, int sCom, byte[] rs, String sProtocol) {

        if (rs != null && rs.length > 6) {
            byte[] crc = new byte[2];
            //
            arraycopy(rs, rs.length - 2, crc, 0, 2);
            byte[] RtuRs = new byte[rs.length - 2];
            arraycopy(rs, 0, RtuRs, 0, RtuRs.length);

            //若校验模式是低位，则先进行反转
            if ("2".equals(getPublicConfigData("Modbus_Rtu2_CRC_MODE"))) {
                DataUtil.reverse(crc);
            }

            if (Arrays.equals(DataUtil.crc16(RtuRs, RtuRs.length), crc)) {
                nowCompName = getCompName(RtuRs[0]);

                if (!nowCompName.equals("")) {
                    switch (RtuRs[1]) {
                        case 3:
                            modbus_03_Solve(port, sCom, RtuRs);
                            break;
                        case 06:
                            modbus_06_Solve(port, sCom, RtuRs);
                            break;
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


        //非在线下不可读取参数,历史数据区域可读取    // 寄存器100 - 120 之间未VOC参数组分测量值
        if (!getModePermissions(nowCompName, "反控") && ((!(regAddr >= 100 && (regAddr + RegNum) < 122)) && !(regAddr == 0 && (regAddr + RegNum) < 3))) {
            sendModbusErrorCode(port, sCom, rs, 6);
            return;
        }
        if (RegNum < lRtu2CompRegMap.get(nowCompName).size() || RegNum < 128) {

            byte[] head = new byte[3];
            head[0] = rs[0];
            head[1] = rs[1];
            head[2] = (byte) ((RegNum * 2) & 0xff);
            if (lRtu2CompRegMap.get(nowCompName).get(regAddr) == null) {
                sendModbusErrorCode(port, sCom, rs, 2);
                return;
            }
            for (i = 0; i < RegNum; ) {
                if (lRtu2CompRegMap.get(nowCompName).get(regAddr) == null) {
                    sendModbusErrorCode(port, sCom, rs, 2);
                    return;
                }
                i += lRtu2CompRegMap.get(nowCompName).get(regAddr).getDataLen();
                if (i > RegNum) {
                    sendModbusErrorCode(port, sCom, rs, 1);
                    return;
                }

                //判断是否是float类型的值，若是则根据对应协议下的解析模式进行取值
                if (lRtu2CompRegMap.get(nowCompName).get(regAddr).getDataType().equals("FLOAT2")) {//MODBUS_RTU默认数字类型为FLOAT2
                    dataBytes = copybyte(toBytes(lRtu2CompRegMap.get(nowCompName).get(regAddr).getData(), getPublicConfigData("Modbus_Rtu2_ANALYSIS_MODE"), lRtu2CompRegMap.get(nowCompName).get(regAddr).getDataLen()));
                } else {
                    dataBytes = copybyte(toBytes(lRtu2CompRegMap.get(nowCompName).get(regAddr).getData(), lRtu2CompRegMap.get(nowCompName).get(regAddr).getDataType(), lRtu2CompRegMap.get(nowCompName).get(regAddr).getDataLen()));
                }

                arraycopy(dataBytes, 0, allDataBytes, allDataCh, dataBytes.length);
                allDataCh += dataBytes.length;

                regAddr += lRtu2CompRegMap.get(nowCompName).get(regAddr).getDataLen();

            }
            byte[] sendBytes = new byte[allDataCh + 3 + 2];
            arraycopy(head, 0, sendBytes, 0, 3);
            arraycopy(allDataBytes, 0, sendBytes, 3, allDataCh);


            if (getPublicConfigData("Modbus_Rtu2_CRC_MODE").equals("2")) {
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
     * modbus 写单个保持寄存器数据
     * */
    private static void modbus_06_Solve(Communication port, int sCom, byte[] rs) {
        int data = 0;
        int startRegAddr = (((short) rs[2] & 0xff) << 8) | (rs[3] & 0xff) + 255;//获取寄存器起始地址
        boolean dataErrFlag = false;
        if (lRtu2CompRegMap.get(nowCompName).get(startRegAddr).getRw().equals("W") ||
                lRtu2CompRegMap.get(nowCompName).get(startRegAddr).getRw().equals("WR")) {
            switch (lRtu2CompRegMap.get(nowCompName).get(startRegAddr).getDataType()) {
                case "WORD":
                    data = (((short) rs[4] & 0xff) << 8) | (rs[5] & 0xff);//获取寄存器起始地址
                    break;
                case "FLOAT":
                    break;
                case "DWORD":
                    break;
            }
            switch (data) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 10:
                case 11:
                case 12:
                case 13:
                case 14:
                case 15:
                case 16:
                case 17:
                case 18:
                    final String[] flowStr = new String[]{"停止测量", context.getString(R.string.SDZY), context.getString(R.string.SDJZ),
                            context.getString(R.string.SDB1), context.getString(R.string.SDB2), context.getString(R.string.SDYBQX),
                            context.getString(R.string.SDLYCL), context.getString(R.string.SDCSZY), context.getResources().getString(R.string.SDGLCX),
                            "", context.getResources().getString(R.string.SDBYCL), context.getString(R.string.SDBY2CL), context.getString(R.string.SDGYWDX),
                            context.getString(R.string.SDJYWDX), context.getString(R.string.SDLDHC), context.getString(R.string.SDKDHC),
                            context.getString(R.string.SDBYHC), context.getString(R.string.SDPXY), context.getString(R.string.SDJBHS)};

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
                                        saveRunInfo2File("组分[" + nowCompName + "] RTU2-停止测量");
                                    } else {
                                        if (doFlowing.get(nowCompName).equals(context.getString(R.string.waiting_for_instructions))) {
                                            doControlJob(nowCompName, flowStr[finalData]);
                                            saveRunInfo2File("组分[" + nowCompName + "] RTU2-启动 " + flowStr[finalData]);
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
                default:
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

        if (getPublicConfigData("Modbus_Rtu2_CRC_MODE").equals("2")) {
            byte[] crc = crc16(sendBytes, sendBytes.length);
            DataUtil.reverse(crc);
            sendBytes = copybyte(sendBytes, crc);
        } else {
            sendBytes = copybyte(sendBytes, crc16(sendBytes, sendBytes.length));
        }

        rS485_SendData(port, sCom, sendBytes);
    }

    /***
     * modbus 写多个保持寄存器数据
     * */
    private static void modbus_16_Solve(Communication port, int sCom, @NonNull byte[] rs) {

        int i;
        int startRegAddr = (((short) rs[2] & 0xff) << 8) | (rs[3] & 0xff) + 255;//获取寄存器起始地址
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
            if (lRtu2CompRegMap.get(nowCompName).get(startRegAddr).getRw().equals("W") ||
                    lRtu2CompRegMap.get(nowCompName).get(startRegAddr).getRw().equals("WR")) {
                switch (lRtu2CompRegMap.get(nowCompName).get(startRegAddr).getDataType()) {
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

                //对模式切换指令不限制
                if (!getModePermissions(nowCompName, "反控")) {
                    sendModbusErrorCode(port, sCom, rs, 6);
                    return;
                }
                switch (startRegAddr) {
                    case 277: {
                        switch (data) {
                            case 0:
                            case 1:
                            case 2:
                            case 3:
                            case 4:
                            case 5:
                            case 6:
                            case 7:
                            case 8:
                            case 10:
                            case 11:
                            case 12:
                            case 13:
                            case 14:
                            case 15:
                            case 16:
                            case 17:
                            case 18:
                                final String[] flowStr = new String[]{"停止测量", context.getString(R.string.SDZY), context.getString(R.string.SDJZ),
                                        context.getString(R.string.SDB1), context.getString(R.string.SDB2), context.getString(R.string.SDYBQX),
                                        context.getString(R.string.SDLYCL), context.getString(R.string.SDCSZY), context.getResources().getString(R.string.SDGLCX),
                                        "", context.getResources().getString(R.string.SDBYCL), context.getString(R.string.SDBY2CL), context.getString(R.string.SDGYWDX),
                                        context.getString(R.string.SDJYWDX), context.getString(R.string.SDLDHC), context.getString(R.string.SDKDHC),
                                        context.getString(R.string.SDBYHC), context.getString(R.string.SDPXY), context.getString(R.string.SDJBHS)};

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
                                                    saveRunInfo2File("组分[" + nowCompName + "] RTU2-停止测量");
                                                } else {
                                                    if (doFlowing.get(nowCompName).equals(context.getString(R.string.waiting_for_instructions))) {
                                                        doControlJob(nowCompName, flowStr[finalData]);
                                                        saveRunInfo2File("组分[" + nowCompName + "] RTU2-启动 " + flowStr[finalData]);
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
                            default:
                                sendModbusErrorCode(port, sCom, rs, 3);
                                return;
                        }
                    }
                    break;
                    case 279: {
                        boolean errFlag = false;
                        try {
                            int year = (rs[12] & 0xff) + 1970;
                            int month = (rs[11] & 0xff);
                            int day = (rs[10] & 0xff);
                            int hour = (rs[9] & 0xff);
                            int minute = (rs[8] & 0xff);
                            int second = (rs[7] & 0xff);
                            if (year > 2000 && year < 3000 && month > 0 && month < 13 && day > 0 && day < 32 && hour >= 0 && hour < 24 && minute >= 0 && minute < 60 && second >= 0 && second < 60) {
                                saveOperationLogMsg(nowCompName, "RTU2-设置时间" + year + "/" + month + "/" + day + " " + hour + ":" + minute + ":" + second, ErrorLog.msgType.操作_信息);
                                SetDateTime(nowCompName, year, month - 1, day, hour, minute, second);
                                byte[] timeByte = copybyte(toByteArray(year, 4), toByteArray((month), 4),
                                        toByteArray(day, 4), toByteArray(hour, 4),
                                        toByteArray(minute, 4), toByteArray((second), 4));
                                SendManager.SendCmd(nowCompName + "_时间管理_06_0", S0, 3, 200, timeByte);
                            } else {
                                errFlag = true;
                            }

                        } catch (Exception e) {

                        }

                        if (errFlag) {
                            sendModbusErrorCode(port, sCom, rs, 3);
                            return;
                        }
                    }
                    break;
                    case 282: {
                        int lxclh, lxclm;
                        int lxclt = (((short) rs[7] & 0xff) << 8) | (rs[8] & 0xff);
                        lxclh = lxclt / 60;
                        lxclm = lxclt - lxclh * 60;

                        if ((lxclm < 60 && lxclm > -1) && ((lxclh < 24 && lxclh >= 0))) {
                            if (lxclh == 0 && lxclm < 1) {
                                sendModbusErrorCode(port, sCom, rs, 3);
                                return;
                            }
                            saveOperationLogDataModifyMsg(nowCompName, "lxclm", String.valueOf(lxclm), "RTU2-连续测量分", ErrorLog.msgType.操作_信息);
                            saveOperationLogDataModifyMsg(nowCompName, "lxclh", String.valueOf(lxclh), "RTU2-连续测量时", ErrorLog.msgType.操作_信息);
                            updateConfigData(nowCompName, "lxclm", String.valueOf(lxclm));
                            updateConfigData(nowCompName, "lxclh", String.valueOf(lxclh));
                            int time = (lxclh * 60 + lxclm);
                            getTimer_cycle(nowCompName).setTime(time, 0);
                            getTimer_cycle(nowCompName).resume(0);
                        } else {
                            sendModbusErrorCode(port, sCom, rs, 3);
                            return;
                        }
                    }
                    break;
                    case 283: {
                        if (rs.length >= 31) {
                            String strH = "";
                            for (i = 7; i < 31; i++) {
                                if (rs[i] == 0x01) {
                                    strH += (i - 7 + "，");
                                }
                            }
                            if (strH.length() > 0) strH = strH.substring(0, strH.length() - 1);
                            saveOperationLogDataModifyMsg(nowCompName, "zqclh", strH, "RTU2018-周期测量时间", ErrorLog.msgType.操作_信息);
                            updateConfigData(nowCompName, "zqclh", strH);
                            syncAutoDoSample_topHour();
                        } else {
                            sendModbusErrorCode(port, sCom, rs, 3);
                            return;
                        }
                    }
                    break;
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

        if (getPublicConfigData("Modbus_Rtu2_CRC_MODE").equals("2")) {
            byte[] crc = crc16(sendBytes, sendBytes.length);
            DataUtil.reverse(crc);
            sendBytes = copybyte(sendBytes, crc);
        } else {
            sendBytes = copybyte(sendBytes, crc16(sendBytes, sendBytes.length));
        }

        rS485_SendData(port, sCom, sendBytes);
    }


    /*
     * 通过接收到地址找到对应组份
     */
    private static String getCompName(int reAddr) {
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
     * 发送异常码
     */
    private static void sendModbusErrorCode(Communication port, int sCom, byte[] rs, int err) {
        //寄存器地址+数量超出范围
        byte[] sendBytes = new byte[3];
        sendBytes[0] = rs[0];
        sendBytes[1] = (byte) ((rs[1] | 0x80) & 0xff);
        sendBytes[2] = (byte) err;//异常码


        if (getPublicConfigData("Modbus_Rtu2_CRC_MODE").equals("2")) {
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

}
