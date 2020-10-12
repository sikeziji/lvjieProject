package com.yzlm.cyl.cfragment.Communication.Component.Cmd;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.yzlm.cyl.cfragment.Communication.Communication;
import com.yzlm.cyl.cfragment.Communication.Component.AppCalc.Calc;
import com.yzlm.cyl.cfragment.Communication.Component.AppCalc.CalcTable;
import com.yzlm.cyl.cfragment.Communication.Component.AppCalc.Energy;
import com.yzlm.cyl.cfragment.Communication.Component.AppCmd.Cmd;
import com.yzlm.cyl.cfragment.Communication.Component.AppCmd.CmdTable;
import com.yzlm.cyl.cfragment.Communication.Component.AppCmd.RecMessage;
import com.yzlm.cyl.cfragment.Communication.Component.AppError.AppError;
import com.yzlm.cyl.cfragment.Communication.Component.AppFlow.FlowClass;
import com.yzlm.cyl.cfragment.Communication.Component.AppFlow.FlowStep;
import com.yzlm.cyl.cfragment.Communication.Component.AppFlow.FlowTable;
import com.yzlm.cyl.cfragment.Communication.NoBoard.AppCalc.NBEnergy;
import com.yzlm.cyl.cfragment.Communication.Thread.SendManager;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.History;
import com.yzlm.cyl.cfragment.Config.NoBoard.CfgTool.NoBoardHistory;
import com.yzlm.cyl.cfragment.Global;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.Util.DataUtil;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yzlm.cyl.cfragment.Communication.Component.Cmd.CommandPackaging.findAddr;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.updateConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.History.CorrRecHistoryDataTime;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.History.getHistoryData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.getPublicConfigData;
import static com.yzlm.cyl.cfragment.Content.Component.ContentTool.setLvDeviceFlowInfo;
import static com.yzlm.cyl.cfragment.Content.Component.List1.List1_Content3.cfgFlowHandler;
import static com.yzlm.cyl.cfragment.Content.Component.List2.List2_Content1.mclcsHandler;
import static com.yzlm.cyl.cfragment.Content.Component.List2.List2_Content3_2.getNoZeroCKAEnergy;
import static com.yzlm.cyl.cfragment.Content.Component.List2.List2_Content3_2.getVocMeaComponentNum;
import static com.yzlm.cyl.cfragment.Content.Component.List2.List2_Content5.mjCompSysTimeHandler;
import static com.yzlm.cyl.cfragment.Content.Component.List4.DealList4_Content7.editSerialListHandler;
import static com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_DevProtect.devProtectHandler;
import static com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_FlowConfig.flowConfigHandler;
import static com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_directory.receiveDirectoryHandler;
import static com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_pTOCbfzj.mTOCbfzjHandler;
import static com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_pTOCcldy.mTOCcldyHandler;
import static com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_pTOCjldy.mTOCjldyHandler;
import static com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_pVOCcldy.mVoccldyHandler;
import static com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_pbcpb.pbcpbHandler;
import static com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_pbfzj.mddbfzjHandler;
import static com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_pcldy.mcldyHandler;
import static com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_pddcldy.mddcldyHandler;
import static com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_pddcsdy.mddcsdyHandler;
import static com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_pddcsdy2.mddcsdy2Handler;
import static com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_pddwkcsdy.mddwkdyHandler;
import static com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_pgpcldy.arrayListEnergy;
import static com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_pgpcldy.mEtSpecCoefB;
import static com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_pgpcldy.mEtSpecCoefK;
import static com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_pgpcldy.mgpcldyHandler;
import static com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_pgpcldy.specData;
import static com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_pgpcldy.specSum;
import static com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_pgpcldy.updateLV;
import static com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_pjlbcs.mJLBjlcsHandler;
import static com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_pjlbcscdy.mJLCS_CHandler;
import static com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_pjlbsz.jlbszhandler;
import static com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_pjlcs.mjlcsHandler;
import static com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_pjlcs2.mjlcs2Handler;
import static com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_pjldy.mjldyHandler;
import static com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_pswdxcldy.mswdxcldyHandler;
import static com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_pzsbcs.mZsbcsHandler;
import static com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_pzsbsz.mddzsbszHandler;
import static com.yzlm.cyl.cfragment.Content.Component.List5.List5_Content1.mSoftwareVerHandler;
import static com.yzlm.cyl.cfragment.Content.Component.ListSetting.List_Content_Setting2.mExtendConnHandler;
import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.QueryMeasBoardType;
import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.QueryMeasCateg;
import static com.yzlm.cyl.cfragment.Dialog.Component.Dialog_SpecPeak.mSpecPeakHandler;
import static com.yzlm.cyl.cfragment.Frame.Content.Up_content3_6.refreshCurve_Handler;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.WinWidgetHandler;
import static com.yzlm.cyl.cfragment.Global.AutoSampleEnable;
import static com.yzlm.cyl.cfragment.Global.LogSize;
import static com.yzlm.cyl.cfragment.Global.RealTimeStatusThread;
import static com.yzlm.cyl.cfragment.Global.S0;
import static com.yzlm.cyl.cfragment.Global.cmdStatus;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.doFlowWorking;
import static com.yzlm.cyl.cfragment.Global.doFlowing;
import static com.yzlm.cyl.cfragment.Global.flowReceiveFlag;
import static com.yzlm.cyl.cfragment.Global.getCalc;
import static com.yzlm.cyl.cfragment.Global.getCmds;
import static com.yzlm.cyl.cfragment.Global.getError;
import static com.yzlm.cyl.cfragment.Global.getNoBoardCalcSelectEn;
import static com.yzlm.cyl.cfragment.Global.getNoBoardError;
import static com.yzlm.cyl.cfragment.Global.mHandler;
import static com.yzlm.cyl.cfragment.Global.mListDevice;
import static com.yzlm.cyl.cfragment.Global.mNoBoardHandler;
import static com.yzlm.cyl.cfragment.Global.noBoardWorkState;
import static com.yzlm.cyl.cfragment.Global.saveCurveInfo2File;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;
import static com.yzlm.cyl.cfragment.Global.saveRunInfo2File;
import static com.yzlm.cyl.cfragment.Global.str557View;
import static com.yzlm.cyl.cfragment.Global.strNoBoardComponent;
import static com.yzlm.cyl.cfragment.Global.versionID;
import static com.yzlm.cyl.cfragment.Global.workState;
import static com.yzlm.cyl.clibrary.Util.DataUtil.byte2float;
import static com.yzlm.cyl.clibrary.Util.DataUtil.byteToShort;
import static com.yzlm.cyl.clibrary.Util.DataUtil.bytesToHexString;
import static com.yzlm.cyl.clibrary.Util.DataUtil.crc16;
import static com.yzlm.cyl.clibrary.Util.DataUtil.padRight;
import static com.yzlm.cyl.clibrary.Util.DataUtil.reverse;
import static com.yzlm.cyl.clibrary.Util.DataUtil.shortToByte;
import static com.yzlm.cyl.clibrary.Util.DataUtil.toInt;
import static java.lang.System.arraycopy;
import static java.lang.Thread.sleep;

/**
 * Created by caoyiliang on 2017/2/9.
 */

public class CommandParsing {
    public static boolean communicationTest(String Component, byte Number, String Code, byte[] Command, Communication SP) {
        try {
            byte[] Addr = new byte[4];
            arraycopy(Command, 0, Addr, 0, 4);
            byte[] crc = new byte[2];
            arraycopy(Command, Command.length - 2, crc, 0, 2);
            byte[] Codebyte = new byte[0];
            switch (Command[4]) {
                case 0x08: {
                    Codebyte = new byte[]{Command[6]};
                }
                break;
                case 0x03:
                case 0x06: {
                    Codebyte = new byte[]{Command[5], Command[6]};
                }
                break;
                case 0x23: {
                    Codebyte = new byte[]{Command[7]};
                }
                break;
                case 0x26: {

                }
                break;
            }

            return Arrays.equals(findAddr(Component), Addr) && Arrays.equals(crc16(Command, Command.length - 2), crc) && (Command[4] == Number);

        } catch (Exception ex) {
            return false;
        }
    }

    public static RecMessage dataParsing(String str, byte[] Command, Object object) {
        String Component = str.split("_")[0];
        String StrCommand = str.split("_")[1];
        RecMessage RsMessage = new RecMessage();



        /* 发布软件时需将此处注释取消,用于记录软件的通讯异常错误日志(WL)*/
        try {

            //0x32 处理转发协议的回复，去掉头尾
            if (Command[4] == 0x32) {
                byte[] Cmd = new byte[Command.length - 7];
                System.arraycopy(Command, 5, Cmd, 0, Command.length - 7);
                Command = Cmd;
                if (Command[4] == 0x03) {
                    String StrObject = str.split("_")[4];
                    object = Integer.parseInt(StrObject);
                }
                //判断转发串口
                switch (Command[2]) {
                    case 0x00: {//光电检测板
                        switch (Command[4]) {
                            case 0x08: {
                                if (Command[5] == 0x04) {//生物毒性读取能量值
                                    Message msg = new Message();
                                    msg.obj = toInt(new byte[]{Command[7], Command[6]}) + "_" + toInt(new byte[]{Command[9], Command[8]});
                                    msg.what = 0 * 100 + 32;
                                    mswdxcldyHandler.sendMessage(msg);
                                }
                            }
                            break;
                            case 0x03: {
                                byte[] Codebyte = new byte[]{Command[5], Command[6]};
                                reverse(Codebyte);
                                switch (toInt(Codebyte)) {
                                    case 1: {
                                        byte[] Temp = new byte[32];
                                        arraycopy(Command, 9, Temp, 0, 32);
                                        String dd = new String(Temp, 0, Temp.length, "GB2312");
                                        Message msg = new Message();
                                        msg.what = toInt(Codebyte) + 0 * 100 + 32;//寄存器地址 + 转发串口 * 100 + 转发功能码   这里转发串口00，转发功能码32，所以0*100 + 32 = 32
                                        msg.obj = dd;
                                        mSoftwareVerHandler.sendMessage(msg);
                                    }
                                    break;
                                }
                            }
                            break;
                        }
                    }
                    break;
                    case 0x01: {

                    }
                    break;
                    case 0x02: {

                    }
                    break;
                    case 0x03: {//计量板/机械驱动板
                        switch (Command[4]) {
                            case 0x03: {
                                byte[] Codebyte = new byte[]{Command[5], Command[6]};
                                reverse(Codebyte);
                                switch (toInt(Codebyte)) {
                                    case 32: {
                                        byte[] Temp = new byte[32];
                                        arraycopy(Command, 6, Temp, 0, 32);
                                        String dd = new String(Temp, 0, Temp.length, "GB2312");
                                        Message msg = new Message();
                                        msg.what = toInt(Codebyte) + 1 * 100 + 32;//寄存器地址 + 转发串口 * 100 + 转发功能码   这里转发串口01，转发功能码32，所以32 + 1*100 + 32 = 164
                                        msg.obj = dd;
                                        mSoftwareVerHandler.sendMessage(msg);
                                    }
                                    break;
                                }
                            }
                            break;
                        }
                    }
                    break;
                    case 0x04: {

                    }
                    break;
                    case 0x05: {//温度反应模块
                        switch (Command[4]) {
                            case 0x04: {
                                Message msg = new Message();
                                msg.obj = (float) (toInt(new byte[]{Command[7], Command[6]}) * 0.1);
                                Log.d("TAG", bytesToHexString(Command, Command.length));
                                if (Command[3] == 0x01) {
                                    msg.what = 11;
                                } else {
                                    msg.what = 12;
                                }
                                mswdxcldyHandler.sendMessage(msg);
                            }
                        }
                    }
                    break;
                }
            } else {
                switch (Command[4]) {
                    //region 04功能码命令解析
                    /*case 0x04: {
                        Message msg = new Message();
                        msg.obj = toInt(new byte[]{Command[6], Command[7]});
                        if (Command[3] == 0x01) {
                            msg.what = 11;
                        } else {
                            msg.what = 12;
                        }
                        mswdxcldyHandler.sendMessage(msg);
                    }
                    break;*/
                    //region 08功能码命令解析(WL)
                    case 0x08: {
                        if ((Command[7] == (byte) 0x80)) {
                            cmdStatus.put(String.valueOf(Command[4]) + Command[6], context.getString(R.string.normal));
                        } else {
                            cmdStatus.put(String.valueOf(Command[4]) + Command[6], context.getString(R.string.exception));
                        }
                        if (Command[6] == (byte) 0x20) {
                            //获取当前测控板的串口配置信息
                            Message msg = new Message();
                            msg.what = 1;
                            byte[] bytes = Arrays.copyOfRange(Command, 7, Command.length - 2);
                            Bundle bundle = new Bundle();
                            bundle.putByteArray("serialsInfo", bytes);
                            msg.setData(bundle);
                            editSerialListHandler.sendMessage(msg);
                        }


                        switch (Command[7]) {
                            case (byte) 0x80: {
                                RsMessage.setMesType(ErrorLog.msgType.通讯_信息);
                                if (Command[6] == (byte) 4) {
                                    RsMessage.setMessage("组分[" + Component + "] " + context.getString(R.string.single_operation) + "[" + StrCommand + "],正常;" + Component + "_" + StrCommand);
                                } else {
                                    RsMessage.setMessage("组分[" + Component + "] 操作命令[" + StrCommand + "],正常");
                                }
                            }
                            break;
                            case (byte) 0x81: {

                                saveExceptInfo2File("组分[" + Component + "] 操作命令[" + StrCommand + "],操作编号越界");
                            }
                            break;
                            case (byte) 0x82: {
                                saveExceptInfo2File("组分[" + Component + "] 操作命令[" + StrCommand + "],参数数量越界");
                            }
                            break;
                            case (byte) 0x83: {
                                saveExceptInfo2File("组分[" + Component + "] 操作命令[" + StrCommand + "],参数越界");
                            }
                            break;
                            case (byte) 0x84: {
                                saveExceptInfo2File("组分[" + Component + "] 操作命令[" + StrCommand + "],操作未实现(如不满足动作条件无法进行制定动作)");
                            }
                            break;
                            default:
                                saveExceptInfo2File("组分[" + Component + "] 操作命令[" + StrCommand + "],返回数据解析错误");
                                break;
                        }
                    }
                    break;
                    //endregion(WL)
                    //region 03功能码命令解析，此时object为寄存器个数(WL)
                    case 0x03: {
                        /* 寄存器起始地址(WL)*/
                        byte[] Codebyte = new byte[]{Command[5], Command[6]};
                        reverse(Codebyte);
                        CmdTable cmd = getCmds(Component);

                        /* 上一次读测量历史数据的标志位（WL）*/
                        Object canReadOld = cmd.getCmd(60).getValue();

                        String rs = "";
                        for (int i = 0; i < (int) object; i++) {
                            int currIndex = toInt(Codebyte) + i;
                            Cmd currCmd = cmd.getCmd((currIndex));
                            if (currCmd == null) {
                                break;
                            }
                            if (currCmd.getLen() == 1) {
                                byte[] Temp = new byte[currCmd.getSize()];
                                arraycopy(Command, (8 + 4 * i), Temp, 0, currCmd.getSize());
                                switch (currCmd.getType()) {
                                    case "byte": {
                                        currCmd.setValue(Temp[0]);
                                        if (currCmd.getAddr() == 54 && (Temp[0] == 0x01)) {
                                            byte[] arrayOfByte = shortToByte((short) 100);
                                            reverse(arrayOfByte);
                                            //改
                                            SendManager.SendCmd(Component + "_报错查询" + "_3_" + bytesToHexString(arrayOfByte, 2).replace(" ", ""), S0, 1, 200, 32);
                                            RealTimeStatusThread.suspend();
                                        }
                                    }
                                    break;
                                    case "float": {
                                        currCmd.setValue(byte2float(Temp, 0));
                                    }
                                    break;
                                    case "short": {
                                        currCmd.setValue(byteToShort(Temp));
                                    }
                                    break;
                                    case "int": {
                                        currCmd.setValue(toInt(Temp));
                                    }
                                    break;
                                }
                                cmd.setCmd(currIndex, currCmd);
                            } else if (currCmd.getLen() == 8) {
                                byte[] Temp = new byte[currCmd.getSize()];
                                arraycopy(Command, (8 + 4 * i), Temp, 0, currCmd.getSize());
                                switch (currCmd.getType()) {
                                    case "String": {
                                        try {
                                            String dd = new String(Temp, 0, Temp.length, "GB2312");
                                            currCmd.setValue(dd);
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    break;
                                }
                                cmd.setCmd(currIndex, currCmd);
                            } else {
                            }
                            if (currCmd.isSave()) {
                                switch (currCmd.saveType) {
                                    case Log:
                                        break;
                                    case config:
                                        break;
                                    case value:
                                        break;
                                }
                            }
                            rs += currCmd.getName() + " " + currCmd.getValue();
                        }
                        RsMessage.setMessage("");
                        switch (toInt(Codebyte)) {
                            case 50: {
                                /* 当前读测量历史数据的标志位（WL）*/
                                Object canReadNow = cmd.getCmd(60).getValue();
                                if ((byte) canReadNow == 1) {
                                    try {
                                        if (getPublicConfigData("AIsShow").equals("false")) {
                                            SendManager.SendCmd(Component + "_查历史数据条数" + "_23_01", S0, 3, 200, (byte) 1);//TTT
                                            cmd.clearState();
                                        } else {
                                            // 吸光度显示功能读取数据
                                            if (getConfigData(Component, "readHistoryDataFlag").equals("true")) {
                                                SendManager.SendCmd(Component + "_查历史数据条数" + "_23_01", S0, 3, 200, (byte) 1);//TTT
                                                cmd.clearState();
                                            }
                                        }
                                    } catch (Exception e) {
                                        SendManager.SendCmd(Component + "_查历史数据条数" + "_23_01", S0, 3, 200, (byte) 1);//TTT
                                        cmd.clearState();
                                        saveExceptInfo2File("组分[" + Component + "] 查历史数据条数：" + e.toString());
                                    }
                                }
                                short nowWorking;
                                FlowTable ft = Global.getFlows(Component);
                                if (ft != null) {
                                    String status = ft.GetStatus(getCmds(Component).getCmd(51).getValue());
                                    doFlowing.put(Component, status);
                                    /*监测当前执行的流程*/
                                    nowWorking = (short) getCmds(Component).getCmd(52).getValue();
                                    doFlowWorking.put(Component, String.valueOf(nowWorking));
                                }
                            }
                            break;
                            case 100: {
                                dealWithAlarmMsg(Component, RsMessage, cmd);
                            }
                            break;
                            case 200: {
                                Message msg = new Message();
                                msg.what = toInt(Codebyte);
                                devProtectHandler.sendMessage(msg);
                            }
                            break;
                            case 201:
                            case 209:
                            case 5700: {
                                Message msg = new Message();
                                msg.what = toInt(Codebyte);
                                WinWidgetHandler.sendMessage(msg);
                            }
                            break;
                            case 270:
                            case 282:
                            case 294:
                            case 306:
                            case 318:
                            case 330:
                            case 342:
                            case 354:
                            case 366:
                            case 378:
                            case 390:
                            case 402:
                            case 414:
                            case 426:
                            case 438:
                            case 450:
                            case 462:
                            case 474:
                            case 486:
                            case 498:
                            case 5800:
                            case 5804:
                            case 5808:
                            case 5812:
                            case 5816:
                            case 5820:
                            case 5824:
                            case 5828:
                            case 5832:
                            case 5836:
                            case 5840:
                            case 5844:
                            case 5848:
                            case 5852:
                            case 5856:
                            case 5860:
                            case 5864:
                            case 5868:
                            case 5872:
                            case 5876: {
                                Message msg = new Message();
                                msg.what = toInt(Codebyte);
                                jlbszhandler.sendMessage(msg);
                            }
                            break;
                            case 550: {
                                Message msg = new Message();
                                msg.what = toInt(Codebyte);
                                msg.obj = Component;
                                flowConfigHandler.sendMessage(msg);
                            }
                            break;
                            case 557: {
                                versionID = Integer.parseInt(((getCmds(Component).getCmd(toInt(Codebyte)).getValue() == null ? "0" : getCmds(Component).getCmd(toInt(Codebyte)).getValue()).toString()));
                                switch (str557View) {
                                    case "List1_Content3": {
                                        Message msg = new Message();
                                        msg.what = toInt(Codebyte);
                                        cfgFlowHandler.sendMessage(msg);
                                    }
                                    break;
                                    case "List4_Content3_p2":
                                        break;
                                }
                            }
                            break;
                            case 600: {
                                String measCateg = QueryMeasCateg(Component);
                                switch (measCateg) {
                                    case "13": {
                                        Message msg = new Message();
                                        msg.what = toInt(Codebyte);
                                        mddzsbszHandler.sendMessage(msg);
                                    }
                                    break;
                                    default:
                                        Message msg = new Message();
                                        msg.what = toInt(Codebyte);
                                        mddbfzjHandler.sendMessage(msg);
                                        break;
                                }
                            }
                            break;
                            case 602: {
                                Message msg = new Message();
                                msg.what = toInt(Codebyte);
                                mTOCbfzjHandler.sendMessage(msg);
                            }
                            break;
                            case 1026:
                            case 1034:
                            case 1014:
                            case 5600:
                            case 5620: {
                                Message msg = new Message();
                                msg.what = toInt(Codebyte);
                                mjldyHandler.sendMessage(msg);
                            }
                            break;
                            case 10000: {
                                Message msg = new Message();
                                msg.what = toInt(Codebyte);
                                mjlcs2Handler.sendMessage(msg);
                            }
                            break;
                            case 2110:
                            case 2210:
                            case 2310:
                            case 2410:
                            case 2510:
                            case 2610:
                            case 2710:
                            case 2810:
                            case 2910:
                            case 3010:
                            case 3110:
                            case 3210:
                            case 3310:
                            case 3410:
                            case 3510:
                            case 3610:
                            case 3710:
                            case 3810:
                            case 3910:
                            case 4010:
                                if (mjlcsHandler != null) {
                                    Message msg = new Message();
                                    msg.what = toInt(Codebyte);
                                    mjlcsHandler.sendMessage(msg);
                                }
                                break;
                            case 5306:
                            case 5312: {
                                Message msg = new Message();
                                msg.what = toInt(Codebyte);
                                mExtendConnHandler.sendMessage(msg);
                            }
                            break;
                            case 0:
                            case 1:
                            case 5111:
                            case 5121:
                            case 5130:
                            case 5150:
                            case 5190:
                            case 5210:
                            case 5500:
                            case 5702: {
                                String measCateg = QueryMeasCateg(Component);
                                switch (measCateg) {
                                    case "1":
                                    case "9": {
                                        Message msg = new Message();
                                        msg.what = toInt(Codebyte);
                                        mcldyHandler.sendMessage(msg);
                                        break;
                                    }
                                    case "3":
                                    case "8": {
                                        Message msg = new Message();
                                        msg.what = toInt(Codebyte);
                                        mddcldyHandler.sendMessage(msg);
                                        break;
                                    }
                                    case "13": {
                                        Message msg = new Message();
                                        msg.what = toInt(Codebyte);
                                        mswdxcldyHandler.sendMessage(msg);
                                        break;
                                    }
                                    case "4": {
                                        Message msg = new Message();
                                        msg.what = toInt(Codebyte);
                                        mVoccldyHandler.sendMessage(msg);
                                        break;
                                    }
                                    case "11": {
                                        Message msg = new Message();
                                        msg.what = toInt(Codebyte);
                                        mTOCcldyHandler.sendMessage(msg);
                                        break;
                                    }
                                    default: {
                                        Message msg = new Message();
                                        msg.what = toInt(Codebyte);
                                        mgpcldyHandler.sendMessage(msg);
                                        break;
                                    }
                                }
                            }
                            break;
                            case 5703:
                            case 9222:
                            case 9600: {
                                Message msg = new Message();
                                msg.what = toInt(Codebyte);
                                msg.obj = Component;
                                WinWidgetHandler.sendMessage(msg);

                            }
                            break;
                            case 5711:
                            case 5720:
                            case 5730:
                            case 9300: {
                                Message msg = new Message();
                                msg.what = toInt(Codebyte);
                                mddcldyHandler.sendMessage(msg);
                            }
                            break;
                            case 9400: {
                                Message msg = new Message();
                                msg.what = toInt(Codebyte);
                                mZsbcsHandler.sendMessage(msg);
                            }
                            break;
                            case 5714:
                            case 5726:
                            case 5745: {
                                Message msg = new Message();
                                msg.what = toInt(Codebyte);
                                String measCateg = QueryMeasCateg(Component);
                                switch (measCateg) {
                                    case "3":
                                        mddcsdyHandler.sendMessage(msg);
                                        break;
                                    case "8":
                                        mddcsdy2Handler.sendMessage(msg);
                                        break;
                                }
                            }
                            break;
                            case 5735: {
                                Message msg = new Message();
                                msg.what = toInt(Codebyte);
                                mddwkdyHandler.sendMessage(msg);
                            }
                            break;
                            case 2:
                            case 10:
                            case 18:
                            case 34:
                            case 8100:
                            case 8108:
                            case 8116:
                            case 8124:
                            case 8132:
                            case 8140:
                            case 8148:
                            case 8156:
                            case 8164:
                            case 8172:
                            case 8180:
                            case 8188:
                            case 8196:
                            case 8204:
                            case 8212:
                            case 8220:
                            case 8228:
                            case 8236:
                            case 8244:
                            case 8252: {
                                Message msg = new Message();
                                msg.what = toInt(Codebyte);
                                mSoftwareVerHandler.sendMessage(msg);
                            }
                            break;
                            case 5300:
                            case 5710:
                            case 5712:
                            case 9220:
                            case 9500:
                            case 9809: {
                                Message msg = new Message();
                                msg.what = toInt(Codebyte);
                                mclcsHandler.sendMessage(msg);
                            }
                            break;
                            case 5114: {
                                Message msg = new Message();
                                msg.what = toInt(Codebyte);
                                if (mjCompSysTimeHandler != null) {
                                    mjCompSysTimeHandler.sendMessage(msg);
                                }
                            }
                            break;
                            case 6100:
                            case 6200:
                            case 6300:
                            case 6400:
                            case 6500:
                            case 6600:
                            case 6700:
                            case 6800:
                            case 6900:
                            case 7000:
                            case 7100:
                            case 7200:
                            case 7300:
                            case 7400:
                            case 7500:
                            case 7600:
                            case 7700:
                            case 7800:
                            case 7900:
                            case 8000: {
                                Message msg = new Message();
                                msg.what = toInt(Codebyte);
                                if (QueryMeasBoardType(Component).equals("2")) {
                                    mJLCS_CHandler.sendMessage(msg);
                                } else {
                                    if (mJLBjlcsHandler != null) {
                                        mJLBjlcsHandler.sendMessage(msg);
                                    }
                                }
                                break;
                            }
                            case 9000: {
                                if (mVoccldyHandler != null) {
                                    Message msg = new Message();
                                    msg.what = toInt(Codebyte);
                                    mVoccldyHandler.sendMessage(msg);
                                }
                            }
                            break;
                            case 9010:
                                break;
                            case 9100:
                                if (mSpecPeakHandler != null) {
                                    Message msg = new Message();
                                    msg.what = toInt(Codebyte);
                                    mSpecPeakHandler.sendMessage(msg);
                                }
                                break;
                            case 9200: {
                                if (pbcpbHandler != null) {
                                    Message msg = new Message();
                                    msg.what = toInt(Codebyte);
                                    pbcpbHandler.sendMessage(msg);
                                }
                            }
                            break;
                            case 9502:
                            case 9510: {
                                Message msg = new Message();
                                msg.what = toInt(Codebyte);
                                mTOCcldyHandler.sendMessage(msg);
                            }
                            break;
                            case 9530:
                            case 9540:
                            case 9550: {

                                Message msg = new Message();
                                msg.what = toInt(Codebyte);
                                mTOCjldyHandler.sendMessage(msg);
                            }
                            break;
                            case 9800: {
                                Message msg = new Message();
                                msg.what = toInt(Codebyte);
                                mddzsbszHandler.sendMessage(msg);
                            }
                            break;
                            case 9810: {
                                Message msg = new Message();
                                msg.what = toInt(Codebyte);
                                mswdxcldyHandler.sendMessage(msg);
                            }
                            break;
                            default: {
                            }
                            break;
                        }
                    }
                    break;
                    //endregion
                    //region 06功能码命令解析(WL)
                    case 0x06: {
                    }
                    break;
                    //endregion
                    //region 23功能码命令解析(WL)
                    case 0x23: {
                        switch (Command[7]) {//动作号
                            //region 下位机历史数据条数
                            case 0x01: {
                                switch (Command[8]) {
                                    case 0x01: {
                                        switch (Command[9]) {
                                            //region 有历史数据
                                            case 0x00: {
                                                /*下位机通知现在未被读取的历史数据条数*/
                                                int Cout = toInt(new byte[]{Command[10], Command[11]});
                                                dealWithHistoryCountMsg(Component, StrCommand, Cout);
                                            }
                                            break;
                                            //endregion
                                            //region 无历史数据
                                            case 0x01: {
                                                saveExceptInfo2File("组分[" + Component + "] 解析历史数据条数,下位机回复无历史数据条数");
                                                RealTimeStatusThread.resume();
                                            }
                                            break;
                                            //endregion
                                        }
                                    }
                                    break;
                                }
                            }
                            break;
                            //endregion
                            //region 下位机历史数据能量值
                            case 0x02: {
                                switch (Command[8]) {
                                    case 0x01: {
                                        switch (Command[9]) {
                                            case 0x00: {
                                                if (dealWithHistoryDataMsg(Command, Component, StrCommand))
                                                    break;
                                            }
                                            break;
                                            case 0x01: {
                                                RealTimeStatusThread.resume();
                                                //RsMessage.setMesType(com.yzlm.cyl.cfragment.Config.ContentTool.ErrorLog.LogType.通讯_错误);
                                                //RsMessage.setMessage("组分[" + Component + "] 日志命令[" + StrCommand + "],目标日志不存在");
                                            }
                                            break;
                                        }
                                    }
                                    break;
                                    /*流程数据*/
                                    case 0x02:
                                    case 5:// 配置流程数据
                                        Map<String, FlowStep> flowStepMap = new HashMap<>();
                                        int Count = toInt(new byte[]{Command[12], Command[13]});
                                        dealWithFlowDataMsg(Command, flowStepMap, Count);
                                        mListDevice = setLvDeviceFlowInfo(Component, flowStepMap);
                                        saveRunInfo2File("组分[" + Component + "] 读取配置流程数据,正常:" + mListDevice.size() + "条");
                                        break;
                                    case 3: {
                                        /*光谱数据*/
                                        dealWithSpecDataMsg(Command);
                                    }
                                    break;
                                }
                            }
                            break;
                            //endregion
                        }
                    }
                    break;
                    case 0x26: {
                        switch (Command[8]) {
                            case 2:/*流程日志*/
                            case 5:/*配置流程*/
                                if (Command[9] == 0) {
                                    /*正常接收并已写入*/
                                    flowReceiveFlag = true;
                                }
                                break;
                        }
                        break;
                    }
                    case 0x28: {
                        switch (Command[5]) {
                            case 1: {/*数据点*/
                                saveRunInfo2File("组分[" + Component + "] 解析数据点 :" + bytesToHexString(Command, Command.length));
                                saveRunInfo2File("组分[" + Component + "] 收到数据点数据 ");
                                saveRunInfo2File(str);
                                try {
                                    List<Map> list = getHistoryData(Component, 0, 1);
                                    if (list.size() > 0) {
                                        String timeStr = list.get(0).get("time").toString();
                                        int Count = toInt(new byte[]{Command[7], Command[6]});
                                        if (Count < 1100) {
                                            String strVal = "";
                                            /*for (int i = 0; i < Count; i++) {
                                                int val = toInt(new byte[]{Command[8 + i * 2], Command[9 + i * 2]});
                                                strVal += val + ",";
                                            }*/
                                            for (int i = 0; i < Count; i++) {
                                                int val = Command[8 + i]&0xff;
                                                //int result = (0xF0)&0xff;
                                                strVal += val + ",";
                                            }
                                            //Log.d("dataParsing", "dataParsing: " + strVal);
                                            if (strVal.length() > 1) {
                                                saveCurveInfo2File(Component, timeStr, strVal.substring(0, strVal.length() - 1));
                                            }

                                            Message Message = new Message();
                                            Message.obj = Component + "_" + timeStr;
                                            refreshCurve_Handler.sendMessage(Message);
                                        } else {
                                            saveExceptInfo2File("组分[" + Component + "] 收到的数据点个数异常");
                                        }
                                    }
                                } catch (Exception ex) {
                                    saveExceptInfo2File("组分[" + Component + "] 解析数据点报错:" + ex.toString());
                                }


                            }
                            break;
                        }
                    }
                    break;
                    //endregion
                    case 0x30: {
                        Message localMessage = new Message();
                        localMessage.what = 0x01;
                        localMessage.obj = Command;
                        receiveDirectoryHandler.sendMessage(localMessage);
                    }
                    break;
                }
            }
        } catch (Exception ex) {
            saveExceptInfo2File("组分[" + Component + "]进行命令[" + StrCommand + "]操作,返回数据解析错误" + ex.getClass().getName() + ":" + bytesToHexString(Command, Command.length));
            RealTimeStatusThread.resume();
        }
        return RsMessage;
    }

    private static void dealWithSpecDataMsg(byte[] Command) {
        int rsCount = 10;
        int inLoop = 0;
        for (int i = 0; i < specData.length; i++) {
            specData[i] = 0;
        }
        for (; inLoop < specSum; inLoop++) {
            specData[inLoop] = Command[rsCount++] * 0x100;
            specData[inLoop] += Command[rsCount++];
            specData[inLoop] = ((specData[inLoop] & 0xFF00) >> 8) + ((specData[inLoop] & 0x00FF) << 8);
        }
        if (mEtSpecCoefK != null) {
            double k = Double.parseDouble(mEtSpecCoefK.getText().toString());
            double b = Double.parseDouble(mEtSpecCoefB.getText().toString());
            for (int i = 0; i < arrayListEnergy.size() - 1; i++) {

                arrayListEnergy.get(i)[2] = String.valueOf(specData[(int) ((Integer.parseInt(arrayListEnergy.get(i)[1].split(" ")[0]) - b) / (k))]);
            }
            Message msg = new Message();
            msg.what = 2;
            updateLV.sendMessage(msg);
        }
    }

    private static void dealWithFlowDataMsg(byte[] Command, Map<String, FlowStep> flowStepMap, int count) {
        int num = 0;
        for (int i = 0; i < count; i += 4) {
            FlowStep flowStep = new FlowStep();

            int coding = toInt(new byte[]{Command[14 + i], Command[15 + i]});
            int times = Command[16 + i] & 0xff;
            int upDown = Command[17 + i] & 0xff;

            flowStep.setActionCoding(coding);
            flowStep.setSampleCount(times);
            flowStep.setMeasurement(upDown);

            flowStepMap.put(String.valueOf(++num), flowStep);
            if (coding == 0xffff) {
                break;
            }
        }
    }

    private static void dealWithHistoryCountMsg(String component, String strCommand, int cout) {
        saveRunInfo2File("组分[" + component + "] 解析历史数据条数[" + strCommand + "],正常:" + cout + "条");
        /*获取当前组分的流程表*/
        FlowTable ft = Global.getFlows(component);
        if (ft != null) {
            /*获取当前的仪器设备状态用以下发历史数据查询*/
            RealTimeStatusThread.suspend();
            try {
                sleep(2000);

                if (QueryMeasCateg(component).equals("8") && component.equals("CODmn")
                        && getConfigData(component, "GlucoseSwitch1").equals("open")) {//关联开关
                    //查询电极能量
                    byte[] arrayOfByte = DataUtil.shortToByte((short) 620);
                    DataUtil.reverse(arrayOfByte);
                    SendManager.SendCmd(component + "_电极能量信息" + "_3_" + DataUtil.bytesToHexString(arrayOfByte, 2).replace(" ", ""), Global.S0, 3, 200, 7);
                    sleep(2000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            LogSize.put(component, cout);
            SendManager.SendCmd(component + "_能量查询" + "_23_02", S0, 3, 1000, "01_" + cout + "_1");// TTT

        } else {
            saveExceptInfo2File("组分[" + component + "] 解析历史数据条数,当前组分为NULL");
        }
    }

    private static boolean dealWithHistoryDataMsg(byte[] Command, String component, String strCommand) throws InterruptedException {
        saveRunInfo2File("组分[" + component + "] 收到历史数据能量[" + strCommand + "],正常....");
        FlowTable ft = Global.getFlows(component);
        if (ft != null) {
            FlowClass fc = ft.getFlowByCmd(Command[23]);
            if (fc != null) {
                CalcTable ct = getCalc(component);
                if (ct != null) {
                    String range = String.valueOf(fc.getRange());
                    Calc calc = ct.getCalc(range);
                    if (calc != null) {
                        String a_formula = calc.getA_Formula();
                        int Count = 0;
                        if (QueryMeasCateg(component).equals("4")) {
                            Count = getVocMeaComponentNum(component);
                        } else if (QueryMeasCateg(component).equals("13")) {
                            Count = strFindMaxEnergyNum(a_formula);
                        } else {
                            Count = strFindEnergyNum(a_formula);
                        }
                        if (QueryMeasCateg(component).equals("1") && component.equals("TP")) {
                            // 比色平台使用主控板返回的数据进行判定有几个光强
                            Count = getNoZeroCKAEnergy(component, Command);
                            if (!(Count % 2 == 0)) {
                                Count = 2;
                                saveExceptInfo2File("组分[" + component + "] 解析历史数据能量个数异常:" + Count);
                            }
                        }
                        if (Count == 0) {
                            saveExceptInfo2File("组分[" + component + "] 解析历史数据能量[" + strCommand + "]时发生A计算表达式无E,A计算表达式为：" + a_formula);
                            return true;
                        }
                        try {
                            if (getPublicConfigData("AIsShow").equals("false")) {
                                SendManager.SendCmd(component + "_收到历史数据" + "_8_0C", S0, 3, 200, null);
                                saveRunInfo2File("组分[" + component + "] 清空历史数据" + "\r\n");
                            } else {
                                // 吸光度显示功能
                                //数据计算处理完成之后再清除CKB 数据；
                            }
                        } catch (Exception e) {
                            SendManager.SendCmd(component + "_收到历史数据" + "_8_0C", S0, 3, 200, null);
                            saveRunInfo2File("组分[" + component + "] 清空历史数据" + "\r\n");
                            saveRunInfo2File("组分[" + component + "] " + e.toString());
                        }
                        sleep(1000);

                        //region 解析历史数据能量命令
                        Energy energy = new Energy(Count);
                        energy.setComponent(component);
                        energy.setFlow(Command[23]);
                        String strHisDate = (CorrRecHistoryDataTime(byteToShort(new byte[]{Command[14], Command[15]}), Command[16], Command[17], Command[18], Command[19], Command[20]));
                        if (strHisDate.equals("")) {
                            energy.setYear(byteToShort(new byte[]{Command[14], Command[15]}));
                            energy.setMonth(Command[16]);
                            energy.setDay(Command[17]);
                            energy.setHour(Command[18]);
                            energy.setMinute(Command[19]);
                            energy.setSecond(Command[20]);
                        } else {
                            String[] strByteCorrDate = strHisDate.split("_");

                            energy.setYear(Short.parseShort(strByteCorrDate[0]));
                            energy.setMonth(Byte.valueOf(strByteCorrDate[1]));
                            energy.setDay(Byte.valueOf(strByteCorrDate[2]));
                            energy.setHour(Byte.valueOf(strByteCorrDate[3]));
                            energy.setMinute(Byte.valueOf(strByteCorrDate[4]));
                            energy.setSecond(Byte.valueOf(strByteCorrDate[5]));
                        }
                        energy.setUnit("mg/L");
                        NBEnergy[] nbEnergies = new NBEnergy[strNoBoardComponent.get(1).length];
                        switch (QueryMeasCateg(component)) {
                            case "3":
                            case "8":
                            case "11":
                                for (int i = 0; i < Count; i++) {
                                    energy.temperature[i] = byte2float(Command, (26 + i * 4));
                                    energy.fEnergy[i] = byte2float(Command, (146 + i * 4));
                                }
                                break;
                            case "4":
                                for (int i = 0; i < Count; i++) {
                                    energy.temperature[i] = byte2float(Command, (26 + i * 4));
                                    energy.fEnergy[i] = byte2float(Command, (148 + i * 4));
                                }
                                break;
                            case "7":   // 多光谱情况需要根据能量配置文件分别数据
                                float[] temperatures = new float[30];
                                int[] energys = new int[30];
                                int AllCount = getNoZeroCKAEnergy(component, Command);
                                for (int i = 0; i < AllCount; i++) {
                                    temperatures[i] = byte2float(Command, (26 + i * 4));
                                    energys[i] = ((int) byteToShort(new byte[]{Command[146 + i * 2], Command[147 + i * 2]})) & '\uffff';
                                }
                                String sEn = "";
                                for (int iEn : energys) {
                                    sEn += (iEn + "/");
                                }
                                saveRunInfo2File("组分[" + component + "] 解析历史数据能量个数:" + AllCount + "能量:" + sEn);
                                Log.i("解析历史数据能量", sEn);

                                // TN 能量选择信息解析；
                                String energysMsg = getNoBoardCalcSelectEn(component);
                                Count = strFindEnergyNum(energysMsg);
                                for (int i = 0; i < Count; i++) {
                                    int iNum = strGetEnergyNum(energysMsg, i + 1);
                                    if (iNum > 0) {
                                        energy.temperature[i] = temperatures[iNum - 1];
                                        energy.energy[i] = energys[iNum - 1];
                                    } else {
                                        saveExceptInfo2File("组分[" + component + "] 解析历史数据能量位置异常:" + iNum);
                                    }
                                }
                                // 5722 参数能量采集
                                for (int iComp = 0; iComp < strNoBoardComponent.get(1).length; iComp++) {

                                    String enMsgs = getNoBoardCalcSelectEn(strNoBoardComponent.get(1)[iComp]);
                                    Count = strFindEnergyNum(enMsgs);
                                    nbEnergies[iComp] = new NBEnergy(Count);
                                    for (int i = 0; i < Count; i++) {
                                        int iNum = strGetEnergyNum(enMsgs, i + 1);
                                        if (iNum > 0) {
                                            nbEnergies[iComp].temperature[i] = temperatures[iNum - 1];
                                            nbEnergies[iComp].energy[i] = energys[iNum - 1];
                                        }
                                    }
                                    // 信息
                                    nbEnergies[iComp].setRange(energy.getRange());
                                    nbEnergies[iComp].setComponent(strNoBoardComponent.get(1)[iComp]);
                                    nbEnergies[iComp].setYear(energy.getYear());
                                    nbEnergies[iComp].setMonth(energy.getMonth());
                                    nbEnergies[iComp].setDay(energy.getDay());
                                    nbEnergies[iComp].setHour(energy.getHour());
                                    nbEnergies[iComp].setMinute(energy.getMinute());
                                    nbEnergies[iComp].setSecond(energy.getSecond());
                                    if (strNoBoardComponent.get(1)[iComp].equals("TSS")) {
                                        nbEnergies[iComp].setUnit("NTU");
                                    } else {
                                        nbEnergies[iComp].setUnit("mg/L");
                                    }
                                    nbEnergies[iComp].setFlow(energy.getFlow());
                                }
                                break;
                            case "13": {
                                energy.setUnit("%");
                            }
                            default:
                                for (int i = 0; i < Count; i++) {
                                    energy.temperature[i] = byte2float(Command, (26 + i * 4));
                                    //接收到数据光强为0，则修正为1
                                    energy.energy[i] = (((int) byteToShort(new byte[]{Command[146 + i * 2], Command[147 + i * 2]})) & '\uffff') == 0 ? 1 : ((int) byteToShort(new byte[]{Command[146 + i * 2], Command[147 + i * 2]})) & '\uffff';
                                }
                                break;
                        }
                        //endregion
                        if (getPublicConfigData("AIsShow").equals("true")) {
                            // 测量出数有延时，需要将实时状态读取线程启动；
                            RealTimeStatusThread.resume();
                        }
                        /*收到重复数据则丢弃,否则增加*/
                        if (isDoCalc(component, energy)) {
                            try {
                                //5721参数计算 ,KBF 几测量值
                                History history = new History(context);
                                history.Add(energy, nbEnergies);

                                // 多光谱平台5722参数计算
                                if (QueryMeasCateg(component).equals("7")) {
                                    /*判断是否计算KBF值*/
                                    for (int iComp = 0; iComp < strNoBoardComponent.get(1).length; iComp++) {
                                        if (isNoBoardDoCalc(strNoBoardComponent.get(1)[iComp], nbEnergies[iComp])) {
                                            // TSS 不能自动校准
                                            if (!strNoBoardComponent.get(1)[iComp].equals("TSS")) {
                                                nbEnergies[iComp].isNoBoardCalcKBValue(strNoBoardComponent.get(1)[iComp]);
                                            }
                                        }
                                    }
                                    // 测量值计算
                                    for (int iComp = 0; iComp < strNoBoardComponent.get(1).length; iComp++) {
                                        if (isNoBoardDoCalc(strNoBoardComponent.get(1)[iComp], nbEnergies[iComp])) {
                                            try {
                                                NoBoardHistory noBoardHistory = new NoBoardHistory(context);
                                                noBoardHistory.Add(component, nbEnergies[iComp]);
                                            } catch (Exception e) {
                                                Log.i("isNoBoardDoCalc", strNoBoardComponent.get(1)[iComp] + ":" + e.toString());
                                            }
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                saveRunInfo2File("组分[" + component + "] 测量值计算异常" + e.toString() + "\r\n");
                            }
                            {
                                // 出数完成处理之后清除数据
                                if (getPublicConfigData("AIsShow").equals("true")) {
                                    updateConfigData(component, "readHistoryDataFlag", "true");
                                    updateConfigData(component, "nowHistoryDataAEn", "");
                                    SendManager.SendCmd(component + "_收到历史数据" + "_8_0C", S0, 3, 200, null);
                                    saveRunInfo2File("组分[" + component + "] 清空历史数据" + "\r\n");
                                }
                            }
                            if (getConfigData(component, "ZDQX").equals("true")) {
                                updateConfigData(component, "zdqxTimes", String.valueOf(Integer.parseInt(getConfigData(component, "zdqxTimes")) + 1));
                            }
                            if (QueryMeasCateg(component).equals("11")) {
                                //String strPath = component + "/" + byteToShort(new byte[]{Command[14], Command[15]}) + String.format("%02d", (int) Command[16]) + String.format("%02d", (int) Command[17]) + "/" + String.format("%02d", (int) Command[18]) + String.format("%02d", (int) Command[19]) + ".txt";
                                String strPath = "CO2/" + byteToShort(new byte[]{Command[14], Command[15]}) + String.format("%02d", (int) Command[16]) + String.format("%02d", (int) Command[17]) + "/" + String.format("%02d", (int) Command[18]) + String.format("%02d", (int) Command[19]) + ".txt";
                                SendManager.SendCmd(component + "_数据点" + "_28_01", S0, 3, 200, strPath.getBytes());

                                sleep(2000);
                            }
                        }
                        RealTimeStatusThread.resume();

                        if ((LogSize.get(component) - 1) == 0) {
                            RealTimeStatusThread.resume();
                        } else {
                            SendManager.SendCmd(component + "_做样次数" + "_23_01", S0, 3, 200, (byte) 1);// TTT
                        }
                    } else {
                        saveExceptInfo2File("组分[" + component + "] 解析历史数据能量[" + strCommand + "]时发生该组分无此量程算法，量程为：" + range + ";命令为:" + bytesToHexString(Command, Command.length));
                        SendManager.SendCmd(component + "_收到历史数据" + "_8_0C", S0, 3, 200, null);//TTT
                        saveRunInfo2File("组分[" + component + "] 清空历史数据-请检查流程内容" + "\r\n");
                        sleep(1000);
                        RealTimeStatusThread.resume();
                    }
                } else {
                    saveExceptInfo2File("组分[" + component + "] 解析历史数据能量[" + strCommand + "]时发生该组分算法表为NULL" + ";命令为:" + bytesToHexString(Command, Command.length));
                    RealTimeStatusThread.resume();
                }
            } else {
                SendManager.SendCmd(component + "_收到历史数据" + "_8_0C", S0, 3, 200, null);//TTT
                saveExceptInfo2File("组分[" + component + "] 解析历史数据能量[" + strCommand + "]时发生该组分无此流程，回发的流程号为：" + Command[23] + ";命令为:" + bytesToHexString(Command, Command.length));
                SendManager.SendCmd(component + "_收到历史数据" + "_8_0C", S0, 3, 200, null);//TTT
                saveRunInfo2File("组分[" + component + "] 清空历史数据-请检查流程内容" + "\r\n");
                sleep(1000);
                RealTimeStatusThread.resume();
            }
        } else {
            saveExceptInfo2File("组分[" + component + "] 解析历史数据能量[" + strCommand + "]时发生该组分流程表为NULL" + ";命令为:" + bytesToHexString(Command, Command.length));
            RealTimeStatusThread.resume();
        }
        return false;
    }

    /**
     * 最新一条数据是否和当前数据时间相同，相同则返回false
     * true:可以进行计算存储，false 不需要计算
     *
     * @param component
     * @param energy
     * @return
     */
    public static boolean isDoCalc(String component, Energy energy) {
        History history = new History(context);
        List<Map> hisMap = history.selectTop(component);

        if (hisMap.size() > 0) {
            String lastTime = hisMap.get(0).get("time").toString();

            boolean timeFlag = lastTime.equals(energy.getTime());
            return !timeFlag;
        }
        return true;
    }

    /**
     * 无背板 最新一条数据是否和当前数据时间相同，相同则返回false
     * true:可以进行计算存储，false 不需要计算
     *
     * @param component
     * @param energy
     * @return
     */
    public static boolean isNoBoardDoCalc(String component, NBEnergy energy) {
        NoBoardHistory history = new NoBoardHistory(context);
        List<Map> hisMap = history.selectTop(component);

        if (hisMap.size() > 0) {
            String lastTime = hisMap.get(0).get("time").toString();

            boolean timeFlag = lastTime.equals(energy.getTime());
            return !timeFlag;
        }
        return true;
    }

    /**
     * 串口解析产生日志解析处理
     *
     * @param component
     * @param rsMessage
     * @param cmd
     * @throws InterruptedException
     */
    private static void dealWithAlarmMsg(String component, RecMessage rsMessage, CmdTable cmd) throws InterruptedException {
        SendManager.SendCmd(component + "_清除报警" + "_8_0B", S0, 3, 200, null);// TTT
        sleep(1000);
        RealTimeStatusThread.resume();

        int iBaseError = 2000;    // 512 号报错，解析增加2000，（（512-2512）为安卓屏报错区域））
        for (int j = 0; j < 32; j++) {
            int errorVal = (int) (cmd.getCmd(100 + j).getValue());
            String errorStr = padRight(Integer.toBinaryString(errorVal), 32, "0");
            Log.v("DANG", errorStr);
            byte[] errorBitStr = errorStr.getBytes(StandardCharsets.UTF_8);
            reverse(errorBitStr);
            for (int k = 0; k <= 31; k++) {
                /*使用1的ASCII码解析*/
                if (errorBitStr[k] == 49) {
                    int iError = 0;
                    iError = (32 * j + k + 1);// 位解析报错号
                    if (j >= 16) {
                        iError += iBaseError;
                    }
                    AppError appError;
                    appError = getError(component).getErrorObj(String.valueOf(iError));
                    try {
                        saveRunInfo2File(component + "_" + "报错 - " + (iError));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (appError != null) {
                        if (appError.getErrorHMIShield()) {
                            continue;
                        }
                        if (appError.getErrorIsShow()) {
                            try {
                                workState.put(component, (iError) + "--" + appError.getErrorShowName());
                            } catch (Exception e) {
                                Log.v("DANG", String.valueOf(iError));
                            }
                        }
                        if (!appError.getErrorIsDoSample()) {
                            saveExceptInfo2File(component + "_" + "报错 - " + (iError) + "停止自动测量");
                            AutoSampleEnable.put(component, "false");
                        }
                        if (appError.getErrorSrc()) {
                            String errorType = appError.getErrorCateg();
                            if (errorType.equals(context.getResources().getString(R.string.error))) {
                                updateConfigData(component, "Volume_Cal", "false");//只要报错，就清除
                                // 标核判定
                                updateConfigData(component, "BHJZCount", "");
                                updateConfigData(component, "BHJZFlag", "false");
                                //多点线性标志位清除
                                updateConfigData(component, "Multi_Point_Linear", "false");

                                rsMessage.setMesType(ErrorLog.msgType.报错_错误);

                            } else if (errorType.equals(context.getResources().getString(R.string.warning))) {
                                rsMessage.setMesType(ErrorLog.msgType.报错_警告);
                            } else if (errorType.equals(context.getResources().getString(R.string.tip_login))) {
                                rsMessage.setMesType(ErrorLog.msgType.登录_信息);
                            } else if (errorType.equals(context.getResources().getString(R.string.tip_run))) {
                                rsMessage.setMesType(ErrorLog.msgType.运维_信息);
                            } else if (errorType.equals(context.getResources().getString(R.string.tip_other))) {
                                rsMessage.setMesType(ErrorLog.msgType.其他_信息);
                            } else {
                                if (appError.getErrorSrc()) {
                                    rsMessage.setMesType(ErrorLog.msgType.报错_信息);
                                } else {
                                    rsMessage.setMesType(ErrorLog.msgType.运行_信息);
                                }
                            }
                            rsMessage.setMessage(component + "_" + (iError) + "_" + appError.getErrorShowName() + "_" + appError.getErrorCateg());
                        }
                    }
                    Message msg = new Message();
                    msg.what = rsMessage.getMesType().ordinal();
                    msg.obj = rsMessage.getMessage();
                    mHandler.sendMessage(msg);
                }
            }
        }
        rsMessage.setMessage("");
    }

    /*判断A计算表达式中的“E”的个数*/
    public static int stringNumbers(String str, String childStr) {
        int counter = 0;

        for (int i = 1; i <= 30; i++) {
            String childStr2 = childStr + padRight(String.valueOf(i), 2, "0");

            if (str.contains(childStr2)) {
                counter++;
            }
        }

        return counter;
    }

    /*判断A计算表达式中的用到几个能量，来决定有几个光强上传上来*/
    private static int strFindEnergyNum(String str) {
        for (int i = 1; i < 30; i++) {
            if (!str.contains("E0" + i)) {
                return i - 1;
            }
        }
        return 4;
    }

    /*判断A计算表达式中的用到最大的能量，来决定有几个光强上传上来*/
    private static int strFindMaxEnergyNum(String str) {
        List<Integer> lInt = new ArrayList<>();
        for (int i = 1; i < 30; i++) {
            if (str.contains("E0" + i)) {
                lInt.add(i);
            }
        }
        if (lInt.size() > 0) {
            int iMax = lInt.get(0);
            for (int i = 1; i < lInt.size(); i++) {
                if (lInt.get(i) > iMax) {
                    iMax = lInt.get(i);
                }
            }
            return iMax;
        }

        return 4;
    }

    /*判断字符串是否为数字*/
    public static boolean isNumeric(String str) {
        for (int i = 0; i < str.length(); i++) {
            System.out.println(str.charAt(i));
            if (!Character.isDigit(str.charAt(i)) && str.charAt(i) != '.') {
                return false;
            }
        }
        return true;
    }


    /**
     * 获取配置文件中E0X 对应所有能量中取值的数组位置
     *
     * @param enStr
     * @param iENum
     * @return
     */
    private static int strGetEnergyNum(String enStr, int iENum) {

        String[] strsValve = enStr.replace("\t", "").replace("\r\n", "").replace("\n", "").split("[,，]");
        try {
            for (String items : strsValve) {
                if (items.contains("E0" + iENum)) {
                    return Integer.parseInt(items.split("=")[1].replace("\t", "").replace("\r\n", "").replace("\n", "").replace(" ", ""));
                }
            }
        } catch (Exception e) {
            saveExceptInfo2File("多光谱平台解析能量位置异常");
        }
        return 0;
    }


    /**
     * 添加日志信息(根据配置文件配置类型)
     *
     * @param compName 组分
     * @param errNum   日志号
     * @param logType  日志类型
     */
    public static void AddError(String compName, int errNum, ErrorLog.msgType logType) {

        try {
            AppError appError = new AppError();
            appError = getError(compName).getErrorObj(String.valueOf(errNum));
            RecMessage RsMessage = new RecMessage();

            if (appError != null) {
                if (appError.getErrorIsShow()) {
                    try {
                        workState.put(compName, (errNum) + "--" + appError.getErrorShowName());
                    } catch (Exception e) {
                        Log.v("DANG", String.valueOf(errNum));
                    }
                }
                if (!appError.getErrorIsDoSample()) {
                    saveExceptInfo2File(compName + "_" + appError.getErrorCateg() + " - " + (errNum) + "停止自动测量");
                    AutoSampleEnable.put(compName, "false");
                }
                /*if (appError.getErrorSrc()) {
                    String errorType = appError.getErrorCateg();
                    if (errorType.equals(context.getResources().getString(R.string.error))) {
                        RsMessage.setMesType(ErrorLog.msgType.报错_错误);
                    } else if (errorType.equals(context.getResources().getString(R.string.warning))) {
                        RsMessage.setMesType(ErrorLog.msgType.报错_警告);
                    } else {
                        RsMessage.setMesType(ErrorLog.msgType.报错_信息);
                    }
                    RsMessage.setMessage(compName + "_" + errNum + "_" + appError.getErrorShowName() + "_" + appError.getErrorCateg());

                } else {
                    RsMessage.setMessage(compName + "_" + errNum + "_" + appError.getErrorShowName() + "_" + appError.getErrorCateg());
                    RsMessage.setMesType(logType);
                }*/

                String errorType = appError.getErrorCateg();
                if (errorType.equals(context.getResources().getString(R.string.error))) {
                    RsMessage.setMesType(ErrorLog.msgType.报错_错误);
                } else if (errorType.equals(context.getResources().getString(R.string.warning))) {
                    RsMessage.setMesType(ErrorLog.msgType.报错_警告);
                } else if (errorType.equals(context.getResources().getString(R.string.tip_login))) {//登录
                    RsMessage.setMesType(ErrorLog.msgType.登录_信息);
                } else if (errorType.equals(context.getResources().getString(R.string.tip_run))) {//运维
                    RsMessage.setMesType(ErrorLog.msgType.运维_信息);
                } else if (errorType.equals(context.getResources().getString(R.string.tip_other))) {//其他
                    RsMessage.setMesType(ErrorLog.msgType.其他_信息);
                } else {
                    if (appError.getErrorSrc()) {
                        RsMessage.setMesType(ErrorLog.msgType.报错_信息);
                    } else {
                        RsMessage.setMesType(ErrorLog.msgType.运行_信息);
                    }
                }
                RsMessage.setMessage(compName + "_" + errNum + "_" + appError.getErrorShowName() + "_" + appError.getErrorCateg());

                Message msg = new Message();
                msg.what = RsMessage.getMesType().ordinal();
                msg.obj = RsMessage.getMessage();

                Log.i("aaa", RsMessage.getMesType().ordinal() + "," + RsMessage.getMessage());
                mHandler.sendMessage(msg);
            }
        } catch (
                Exception e) {
            saveExceptInfo2File("组分[" + compName + "] AddError：errNum" + errNum + e.toString());
        }

    }


    /**
     * 添加日志信息(根据配置文件配置类型)
     *
     * @param compName 组分
     * @param errNum   日志号
     * @param logType  日志类型
     */
    public static void AddError(String compName, int errNum, String value, ErrorLog.msgType logType) {

        try {
            AppError appError = new AppError();
            appError = getError(compName).getErrorObj(String.valueOf(errNum));
            RecMessage RsMessage = new RecMessage();

            if (appError != null) {
                if (appError.getErrorIsShow()) {
                    try {
                        workState.put(compName, (errNum) + "--" + appError.getErrorShowName());
                    } catch (Exception e) {
                        Log.v("DANG", String.valueOf(errNum));
                    }
                }
                if (!appError.getErrorIsDoSample()) {
                    saveExceptInfo2File(compName + "_" + appError.getErrorCateg() + " - " + (errNum) + "停止自动测量");
                    AutoSampleEnable.put(compName, "false");
                }
                /*if (appError.getErrorSrc()) {
                    String errorType = appError.getErrorCateg();
                    if (errorType.equals(context.getResources().getString(R.string.error))) {
                        RsMessage.setMesType(ErrorLog.msgType.报错_错误);
                    } else if (errorType.equals(context.getResources().getString(R.string.warning))) {
                        RsMessage.setMesType(ErrorLog.msgType.报错_警告);
                    } else {
                        RsMessage.setMesType(ErrorLog.msgType.报错_信息);
                    }
                    RsMessage.setMessage(compName + "_" + errNum + "_" + appError.getErrorShowName() + "_" + appError.getErrorCateg());

                } else {
                    RsMessage.setMessage(compName + "_" + errNum + "_" + appError.getErrorShowName() + "_" + appError.getErrorCateg());
                    RsMessage.setMesType(logType);
                }*/

                String errorType = appError.getErrorCateg();
                if (errorType.equals(context.getResources().getString(R.string.error))) {
                    RsMessage.setMesType(ErrorLog.msgType.报错_错误);
                } else if (errorType.equals(context.getResources().getString(R.string.warning))) {
                    RsMessage.setMesType(ErrorLog.msgType.报错_警告);
                } else if (errorType.equals(context.getResources().getString(R.string.tip_login))) {//登录
                    RsMessage.setMesType(ErrorLog.msgType.登录_信息);
                } else if (errorType.equals(context.getResources().getString(R.string.tip_run))) {//运维
                    RsMessage.setMesType(ErrorLog.msgType.运维_信息);
                } else if (errorType.equals(context.getResources().getString(R.string.tip_other))) {//其他
                    RsMessage.setMesType(ErrorLog.msgType.其他_信息);
                } else {
                    if (appError.getErrorSrc()) {
                        RsMessage.setMesType(ErrorLog.msgType.报错_信息);
                    } else {
                        RsMessage.setMesType(ErrorLog.msgType.运行_信息);
                    }
                }
                RsMessage.setMessage(compName + "_" + errNum + "_" + appError.getErrorShowName() + "\t" + value + "_" + appError.getErrorCateg());

                Message msg = new Message();
                msg.what = RsMessage.getMesType().ordinal();
                msg.obj = RsMessage.getMessage();

                Log.i("aaa", RsMessage.getMesType().ordinal() + "," + RsMessage.getMessage());
                mHandler.sendMessage(msg);
            }
        } catch (
                Exception e) {
            saveExceptInfo2File("组分[" + compName + "] AddError：errNum" + errNum + e.toString());
        }

    }


    /**
     * 添加日志信息
     *
     * @param compName 组分
     * @param errNum   日志号
     * @param logType  日志类型
     */
    public static void AddNoBoardError(String compName, int errNum, ErrorLog.msgType logType) {
        AppError appError = new AppError();
        appError = getNoBoardError(compName).getErrorObj(String.valueOf(errNum));
        RecMessage RsMessage = new RecMessage();
        try {
            if (appError != null) {
                if (appError.getErrorIsShow()) {
                    try {
                        noBoardWorkState.put(compName, (errNum) + "--" + appError.getErrorShowName());
                    } catch (Exception e) {
                        Log.v("DANG", String.valueOf(errNum));
                    }
                }
                /*if (appError.getErrorSrc()) {
                    String errorType = appError.getErrorCateg();
                    if (errorType.equals(context.getResources().getString(R.string.error))) {
                        RsMessage.setMesType(ErrorLog.msgType.报错_错误);
                    } else if (errorType.equals(context.getResources().getString(R.string.warning))) {
                        RsMessage.setMesType(ErrorLog.msgType.报错_警告);
                    } else {
                        RsMessage.setMesType(ErrorLog.msgType.报错_信息);
                    }
                    RsMessage.setMessage(compName + "_" + errNum + "_" + appError.getErrorShowName() + "_" + appError.getErrorCateg());

                } else {
                    RsMessage.setMessage(compName + "_" + errNum + "_" + appError.getErrorShowName() + "_" + appError.getErrorCateg());
                    RsMessage.setMesType(logType);
                }*/
                String errorType = appError.getErrorCateg();
                if (errorType.equals(context.getResources().getString(R.string.error))) {
                    RsMessage.setMesType(ErrorLog.msgType.报错_错误);
                } else if (errorType.equals(context.getResources().getString(R.string.warning))) {
                    RsMessage.setMesType(ErrorLog.msgType.报错_警告);
                } else if (errorType.equals(context.getResources().getString(R.string.tip_login))) {//登录
                    RsMessage.setMesType(ErrorLog.msgType.登录_信息);
                } else if (errorType.equals(context.getResources().getString(R.string.tip_run))) {//运维
                    RsMessage.setMesType(ErrorLog.msgType.运维_信息);
                } else if (errorType.equals(context.getResources().getString(R.string.tip_other))) {//其他
                    RsMessage.setMesType(ErrorLog.msgType.其他_信息);
                } else {
                    if (appError.getErrorSrc()) {
                        RsMessage.setMesType(ErrorLog.msgType.报错_信息);
                    } else {
                        RsMessage.setMesType(ErrorLog.msgType.运行_信息);
                    }
                }
                Message msg = new Message();
                msg.what = RsMessage.getMesType().ordinal();
                msg.obj = RsMessage.getMessage();
                mNoBoardHandler.sendMessage(msg);
            }
        } catch (Exception e) {
            saveExceptInfo2File("组分[" + compName + "] AddError：errNum" + errNum + e.toString());
        }
    }


    /**
     * 添加日志信息
     *
     * @param compName 组分
     * @param errNum   日志号
     * @param logType  日志类型
     */
    public static void AddNoBoardError(String compName, int errNum, String value, ErrorLog.msgType logType) {
        AppError appError = new AppError();
        appError = getNoBoardError(compName).getErrorObj(String.valueOf(errNum));
        RecMessage RsMessage = new RecMessage();
        try {
            if (appError != null) {
                if (appError.getErrorIsShow()) {
                    try {
                        noBoardWorkState.put(compName, (errNum) + "--" + appError.getErrorShowName());
                    } catch (Exception e) {
                        Log.v("DANG", String.valueOf(errNum));
                    }
                }
                /*if (appError.getErrorSrc()) {
                    String errorType = appError.getErrorCateg();
                    if (errorType.equals(context.getResources().getString(R.string.error))) {
                        RsMessage.setMesType(ErrorLog.msgType.报错_错误);
                    } else if (errorType.equals(context.getResources().getString(R.string.warning))) {
                        RsMessage.setMesType(ErrorLog.msgType.报错_警告);
                    } else {
                        RsMessage.setMesType(ErrorLog.msgType.报错_信息);
                    }
                    RsMessage.setMessage(compName + "_" + errNum + "_" + appError.getErrorShowName() + "_" + appError.getErrorCateg());

                } else {
                    RsMessage.setMessage(compName + "_" + errNum + "_" + appError.getErrorShowName() + "_" + appError.getErrorCateg());
                    RsMessage.setMesType(logType);
                }*/
                String errorType = appError.getErrorCateg();
                if (errorType.equals(context.getResources().getString(R.string.error))) {
                    RsMessage.setMesType(ErrorLog.msgType.报错_错误);
                } else if (errorType.equals(context.getResources().getString(R.string.warning))) {
                    RsMessage.setMesType(ErrorLog.msgType.报错_警告);
                } else if (errorType.equals(context.getResources().getString(R.string.tip_login))) {//登录
                    RsMessage.setMesType(ErrorLog.msgType.登录_信息);
                } else if (errorType.equals(context.getResources().getString(R.string.tip_run))) {//运维
                    RsMessage.setMesType(ErrorLog.msgType.运维_信息);
                } else if (errorType.equals(context.getResources().getString(R.string.tip_other))) {//其他
                    RsMessage.setMesType(ErrorLog.msgType.其他_信息);
                } else {
                    if (appError.getErrorSrc()) {
                        RsMessage.setMesType(ErrorLog.msgType.报错_信息);
                    } else {
                        RsMessage.setMesType(ErrorLog.msgType.运行_信息);
                    }
                }

                RsMessage.setMessage(compName + "_" + errNum + "_" + appError.getErrorShowName() + "\t" + value + "_" + appError.getErrorCateg());

                Message msg = new Message();
                msg.what = RsMessage.getMesType().ordinal();
                msg.obj = RsMessage.getMessage();
                mNoBoardHandler.sendMessage(msg);
            }
        } catch (Exception e) {
            saveExceptInfo2File("组分[" + compName + "] AddError：errNum" + errNum + e.toString());
        }
    }

}
