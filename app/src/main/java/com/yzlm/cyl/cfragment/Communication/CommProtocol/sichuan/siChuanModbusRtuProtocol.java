package com.yzlm.cyl.cfragment.Communication.CommProtocol.sichuan;

import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;

import com.yzlm.cyl.cfragment.Communication.Communication;
import com.yzlm.cyl.cfragment.Communication.Thread.SendManager;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.Util.DataUtil;

import java.util.Arrays;

import static com.yzlm.cyl.cfragment.Cal.Component.CalFunc.isB1CalEnable;
import static com.yzlm.cyl.cfragment.Cal.Component.CalFunc.isB2CalEnable;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.sichuan.siChuanModbusRtu.addSiChuanRtuRegisterData;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.sichuan.siChuanModbusRtu.clearStatusParChange;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog.saveOperationLogMsg;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.getPublicConfigData;
import static com.yzlm.cyl.cfragment.Content.Component.List2.List2_Content2.getModePermissions;
import static com.yzlm.cyl.cfragment.DBConvert.MDBConvert.getSystemDateDay;
import static com.yzlm.cyl.cfragment.DBConvert.MDBConvert.getSystemDateHour;
import static com.yzlm.cyl.cfragment.DBConvert.MDBConvert.getSystemDateMin;
import static com.yzlm.cyl.cfragment.DBConvert.MDBConvert.getSystemDateMonth;
import static com.yzlm.cyl.cfragment.DBConvert.MDBConvert.getSystemDateSec;
import static com.yzlm.cyl.cfragment.DBConvert.MDBConvert.getSystemDateYear;
import static com.yzlm.cyl.cfragment.DBConvert.MDBConvert.toBytes;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.SetDateTime;
import static com.yzlm.cyl.cfragment.Frame.Content.Up_content3.stopWorking;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.WinWidgetHandler;
import static com.yzlm.cyl.cfragment.Global.IOBoardUsed;
import static com.yzlm.cyl.cfragment.Global.S0;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.doControlJob;
import static com.yzlm.cyl.cfragment.Global.doFlowing;
import static com.yzlm.cyl.cfragment.Global.lSiChuanRtuCompRegMap;
import static com.yzlm.cyl.cfragment.Global.saveRunInfo2File;
import static com.yzlm.cyl.cfragment.Global.siChuanProtocolStatusParIsChange;
import static com.yzlm.cyl.cfragment.Global.strComponent;
import static com.yzlm.cyl.clibrary.Util.DataUtil.bytesToHexString;
import static com.yzlm.cyl.clibrary.Util.DataUtil.copybyte;
import static com.yzlm.cyl.clibrary.Util.DataUtil.crc16;
import static com.yzlm.cyl.clibrary.Util.DataUtil.toByteArray;
import static java.lang.System.arraycopy;

public class siChuanModbusRtuProtocol {
    private static String nowCompName = "";

    public static void ParsingProtocolSiChuanRtu(Communication port, int sCom, byte[] rs, String sProtocol) {

        Log.i("协议", "解析模式：" + getPublicConfigData(sProtocol +
                "_ANALYSIS_MODE") + ",校验模式：" + getPublicConfigData(sProtocol + "_CRC_MODE"));


        if (rs != null && rs.length > 6) {
            byte[] crc = new byte[2];
            arraycopy(rs, rs.length - 2, crc, 0, 2);//CRC码

            byte[] RtuRs = new byte[rs.length - 2];
            arraycopy(rs, 0, RtuRs, 0, RtuRs.length);//数据


            if (Arrays.equals(DataUtil.crc16(RtuRs, RtuRs.length), crc)) {
                nowCompName = getCompName(RtuRs[0]);

                if (!nowCompName.equals("")) {
                    switch (RtuRs[1]) {
                        case 3:
                            modbus_03_Solve(port, sCom, RtuRs);
                            break;
                        case 6:
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


        //非在线下不可读取参数,历史数据区域可读取
        if (!getModePermissions(nowCompName, "反控") && !((regAddr + RegNum) < 200)) {
            sendModbusErrorCode(port, sCom, rs, 6);
            return;
        }
        if (RegNum < lSiChuanRtuCompRegMap.get(nowCompName).size() || RegNum < 128) {

            byte[] head = new byte[3];
            head[0] = rs[0];
            head[1] = rs[1];
            head[2] = (byte) ((RegNum * 2) & 0xff);
            //起始地址判定
            if (lSiChuanRtuCompRegMap.get(nowCompName).get(regAddr) == null) {
                sendModbusErrorCode(port, sCom, rs, 2);
                return;
            }
            for (i = 0; i < RegNum; ) {
                //
                if (lSiChuanRtuCompRegMap.get(nowCompName).get(regAddr) == null) {
                    dataBytes = copybyte(toBytes("0", "WORD", 2));
                    arraycopy(dataBytes, 0, allDataBytes, allDataCh, dataBytes.length);
                    allDataCh += dataBytes.length;
                    regAddr += 2;
                    i++;
                    continue;
                }
                i += lSiChuanRtuCompRegMap.get(nowCompName).get(regAddr).getDataLen();
                if (i > RegNum) {
                    sendModbusErrorCode(port, sCom, rs, 1);
                    return;
                }

                dataBytes = copybyte(toBytes(lSiChuanRtuCompRegMap.get(nowCompName).get(regAddr).getData(), lSiChuanRtuCompRegMap.get(nowCompName).get(regAddr).getDataType(), lSiChuanRtuCompRegMap.get(nowCompName).get(regAddr).getDataLen()));

                arraycopy(dataBytes, 0, allDataBytes, allDataCh, dataBytes.length);
                allDataCh += dataBytes.length;

                regAddr += lSiChuanRtuCompRegMap.get(nowCompName).get(regAddr).getDataLen();
            }
            byte[] sendBytes = new byte[allDataCh + 3 + 2];
            arraycopy(head, 0, sendBytes, 0, 3);
            arraycopy(allDataBytes, 0, sendBytes, 3, allDataCh);
            arraycopy(crc16(sendBytes, sendBytes.length - 2), 0, sendBytes, sendBytes.length - 2, 2);
            rS485_SendData(port, sCom, sendBytes);
            // 如果读取状态或者参数命令后

            if (((regAddr + RegNum) >= 100)) {
                String sType = (regAddr + RegNum) >= 200 ? "2" : "1";
                if (!siChuanProtocolStatusParIsChange.get(nowCompName).equals("0")) {
                    clearStatusParChange(nowCompName, sType);
                    //更新一次寄存器
                    addSiChuanRtuRegisterData(nowCompName,false);
                }
            }

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
        int startRegAddr = (((short) rs[2] & 0xff) << 8) | (rs[3] & 0xff);//获取寄存器起始地址
        boolean dataErrFlag = false;
        try {
            // 时间设置
            String year = null, month = null, day = null, hour = null, minute = null, sec = null;
            boolean blSetTime = false;
            if (lSiChuanRtuCompRegMap.get(nowCompName).get(startRegAddr).getRw().equals("W") ||
                    lSiChuanRtuCompRegMap.get(nowCompName).get(startRegAddr).getRw().equals("WR")) {
                switch (lSiChuanRtuCompRegMap.get(nowCompName).get(startRegAddr).getDataType()) {
                    case "WORD":
                        data = (((short) rs[4] & 0xff) << 8) | (rs[5] & 0xff);//获取寄存器数值
                        break;
                    case "FLOAT":
                        break;
                    case "DWORD":
                        break;
                }
                // 同10设置
                switch (lSiChuanRtuCompRegMap.get(nowCompName).get(startRegAddr).getDescribe()) {
                    case "启动测试":
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
                            case 12:
                            case 13:
                            case 14:
                            case 16:
                            case 17:
                            case 18:
                            case 19:
                            case 20:
                                data = data - 1;
                                final String[] flowStr = new String[]{"停止测量", context.getString(R.string.SDZY), context.getString(R.string.SDBY2CL), context.getString(R.string.SDJZ),
                                        context.getString(R.string.SDYBQX), context.getString(R.string.SDB1), context.getString(R.string.SDB2), context.getString(R.string.SDBYHC), context.getString(R.string.SDGYWDX),
                                        context.getString(R.string.SDJYWDX), context.getResources().getString(R.string.SDBYCL), context.getString(R.string.SDLYCL),
                                        context.getString(R.string.SDPXY), context.getString(R.string.SDJBHS), "",
                                        context.getString(R.string.SDLDHC), context.getString(R.string.SDKDHC), context.getResources().getString(R.string.SDGLCX),
                                        context.getString(R.string.SDCSZY), context.getString(R.string.SDXSJZ)
                                };

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
                                                    saveRunInfo2File("组分[" + nowCompName + "] 四川协议-停止测量");
                                                } else {
                                                    if (doFlowing.get(nowCompName).equals(context.getString(R.string.waiting_for_instructions))) {
                                                        doControlJob(nowCompName, flowStr[finalData]);
                                                        saveRunInfo2File("组分[" + nowCompName + "] 四川协议-启动 " + flowStr[finalData]);
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
                            case 15:
                                Message msg = new Message();
                                msg.what = 110;
                                WinWidgetHandler.sendMessage(msg);
                                break;
                            default:
                                sendModbusErrorCode(port, sCom, rs, 3);
                                return;
                        }
                        break;
                    case "系统时间年":
                        year = String.valueOf(data);
                        blSetTime = true;
                        break;
                    case "系统时间月":
                        month = String.valueOf(data);
                        blSetTime = true;
                        break;
                    case "系统时间日":
                        day = String.valueOf(data);
                        blSetTime = true;
                        break;
                    case "系统时间时":
                        hour = String.valueOf(data);
                        blSetTime = true;
                        break;
                    case "系统时间分":
                        minute = String.valueOf(data);
                        blSetTime = true;
                        break;
                    case "系统时间秒":
                        sec = String.valueOf(data);
                        blSetTime = true;
                        break;
                    default:
                        dataErrFlag = true;
                        break;
                }
            }
            if (blSetTime) {
                dataErrFlag = !setDevTime(year, month, day, hour, minute, sec);
            }
        } catch (Exception e) {
            dataErrFlag = true;
            e.printStackTrace();
        }
        if (dataErrFlag) {
            sendModbusErrorCode(port, sCom, rs, 3);
            return;
        }
        byte[] sendBytes = new byte[6];
        sendBytes[0] = rs[0];
        sendBytes[1] = rs[1];
        sendBytes[2] = rs[2];
        sendBytes[3] = rs[3];
        sendBytes[4] = rs[4];
        sendBytes[5] = rs[5];
        sendBytes = copybyte(sendBytes, crc16(sendBytes, sendBytes.length));
        rS485_SendData(port, sCom, sendBytes);
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
        try {
// 时间设置
            String year = null, month = null, day = null, hour = null, minute = null, sec = null;
            boolean blSetTime = false;
            for (i = 0; i < RegNum; i += lSiChuanRtuCompRegMap.get(nowCompName).get(startRegAddr).getDataLen(), startRegAddr += lSiChuanRtuCompRegMap.get(nowCompName).get(startRegAddr).getDataLen()) {
                if (lSiChuanRtuCompRegMap.get(nowCompName).get(startRegAddr).getRw().equals("W") ||
                        lSiChuanRtuCompRegMap.get(nowCompName).get(startRegAddr).getRw().equals("WR")) {
                    switch (lSiChuanRtuCompRegMap.get(nowCompName).get(startRegAddr).getDataType()) {
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
                    // 同06设置
                    switch (lSiChuanRtuCompRegMap.get(nowCompName).get(startRegAddr).getDescribe()) {
                        case "启动测试":
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
                                case 12:
                                case 13:
                                case 14:
                                case 16:
                                case 17:
                                case 18:
                                case 19:
                                case 20:
                                    data = data - 1;
                                    final String[] flowStr = new String[]{"停止测量", context.getString(R.string.SDZY), context.getString(R.string.SDBY2CL), context.getString(R.string.SDJZ),
                                            context.getString(R.string.SDYBQX), context.getString(R.string.SDB1), context.getString(R.string.SDB2), context.getString(R.string.SDBYHC), context.getString(R.string.SDGYWDX),
                                            context.getString(R.string.SDJYWDX), context.getResources().getString(R.string.SDBYCL), context.getString(R.string.SDLYCL),
                                            context.getString(R.string.SDPXY), context.getString(R.string.SDJBHS), "",
                                            context.getString(R.string.SDLDHC), context.getString(R.string.SDKDHC), context.getResources().getString(R.string.SDGLCX),
                                            context.getString(R.string.SDCSZY), context.getString(R.string.SDXSJZ)
                                    };

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
                                                        saveRunInfo2File("组分[" + nowCompName + "] 四川协议-停止测量");
                                                    } else {
                                                        if (doFlowing.get(nowCompName).equals(context.getString(R.string.waiting_for_instructions))) {
                                                            doControlJob(nowCompName, flowStr[finalData]);
                                                            saveRunInfo2File("组分[" + nowCompName + "] 四川协议-启动 " + flowStr[finalData]);
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
                                case 15:
                                    Message msg = new Message();
                                    msg.what = 110;
                                    WinWidgetHandler.sendMessage(msg);
                                    break;
                                default:
                                    sendModbusErrorCode(port, sCom, rs, 3);
                                    return;
                            }
                            break;
                        case "系统时间年":
                            year = String.valueOf(data);
                            blSetTime = true;
                            break;
                        case "系统时间月":
                            month = String.valueOf(data);
                            blSetTime = true;
                            break;
                        case "系统时间日":
                            day = String.valueOf(data);
                            blSetTime = true;
                            break;
                        case "系统时间时":
                            hour = String.valueOf(data);
                            blSetTime = true;
                            break;
                        case "系统时间分":
                            minute = String.valueOf(data);
                            blSetTime = true;
                            break;
                        case "系统时间秒":
                            sec = String.valueOf(data);
                            blSetTime = true;
                            break;
                        default:
                            dataErrFlag = true;
                            break;
                    }
                    if (dataErrFlag) {
                        sendModbusErrorCode(port, sCom, rs, 3);
                        return;
                    }
                } else {
                    /*不可写只读数据*/
                    sendModbusErrorCode(port, sCom, rs, 3);
                    return;
                }
            }
            if (blSetTime) {
                dataErrFlag = !setDevTime(year, month, day, hour, minute, sec);
            }
        } catch (Exception e) {
            dataErrFlag = true;
            e.printStackTrace();
        }
        if (dataErrFlag) {
            sendModbusErrorCode(port, sCom, rs, 3);
            return;
        }
        byte[] sendBytes = new byte[6];
        sendBytes[0] = rs[0];
        sendBytes[1] = rs[1];
        sendBytes[2] = rs[2];
        sendBytes[3] = rs[3];
        sendBytes[4] = rs[4];
        sendBytes[5] = rs[5];
        sendBytes = copybyte(sendBytes, crc16(sendBytes, sendBytes.length));
        rS485_SendData(port, sCom, sendBytes);
    }

    /**
     * @param year   年
     * @param month  月
     * @param day    日
     * @param hour   时
     * @param minute 分
     * @param sec    秒
     * @return 是否成功
     */
    private static boolean setDevTime(String year, String month, String day, String hour, String minute, String sec) {
        if (null == year) {
            year = String.valueOf(getSystemDateYear());
        }
        if (null == month) {
            month = String.valueOf(getSystemDateMonth());
        }
        if (null == day) {
            day = String.valueOf(getSystemDateDay());
        }
        if (null == hour) {
            hour = String.valueOf(getSystemDateHour());
        }
        if (null == minute) {
            minute = String.valueOf(getSystemDateMin());
        }
        if (null == sec) {
            sec = String.valueOf(getSystemDateSec());
        }
        int iYear, iMonth, iDay, iHour, iMinute, iSec;
        iYear = Integer.parseInt(year);
        iMonth = Integer.parseInt(month);
        iDay = Integer.parseInt(day);
        iHour = Integer.parseInt(hour);
        iMinute = Integer.parseInt(minute);
        iSec = Integer.parseInt(sec);

        if (iYear > 2000 && iYear < 3000 && iMonth > 0 && iMonth < 13
                && iDay > 0 && iDay < 32 && iHour >= 0 && iHour < 24
                && iMinute >= 0 && iMinute < 60 && iSec >= 0 && iSec < 60) {
            saveOperationLogMsg(nowCompName, "四川协议-设置时间" + year + "/" + month + "/" + day + " " + hour + ":" + minute + ":" + sec, ErrorLog.msgType.操作_信息);
            //时间矫正
                                /*AddError(nowCompName, 614, 运维_信息);
                                setDate(year, month - 1, day);
                                setTime(hour, minute, second);*/
            SetDateTime(nowCompName, iYear, iMonth - 1, iDay, iHour, iMinute, iSec);
            byte[] timeByte = copybyte(toByteArray(iYear, 4), toByteArray((iMonth), 4),
                    toByteArray(iDay, 4), toByteArray(iHour, 4),
                    toByteArray(iMinute, 4), toByteArray((iSec), 4));
            SendManager.SendCmd(nowCompName + "_时间管理_06_0", S0, 3, 200, timeByte);
            return true;
        }
        return false;
    }


    /*
     * 通过接收到地址找到对应组份
     */
    private static String getCompName(int reAddr) {
        if (strComponent.get(1).length > 0) {
            for (String item : strComponent.get(1)) {
                if (getConfigData(item, "SiChuan_RTU_ID").equals("")) {
                    continue;
                }
                if (getConfigData(item, "SiChuan_RTU_ID").equals(String.valueOf(reAddr))) {
                    return item;
                }
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
        sendBytes = copybyte(sendBytes, crc16(sendBytes, sendBytes.length));
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
