package com.yzlm.cyl.cfragment.Thread;

import android.util.Log;

import com.yzlm.cyl.cfragment.Communication.CommProtocol.GB2005.GB2005;
import com.yzlm.cyl.cfragment.Communication.CommProtocol.GB2017.GB2017;
import com.yzlm.cyl.cfragment.Communication.CommProtocol.GB2017.GB2017_V2;
import com.yzlm.cyl.cfragment.Communication.CommProtocol.HeNan.HeNan;
import com.yzlm.cyl.cfragment.Communication.CommProtocol.ProtocolTool.DataStruct;
import com.yzlm.cyl.cfragment.Communication.CommProtocol.jiangsu.JiangSu;
import com.yzlm.cyl.cfragment.Communication.Communication;
import com.yzlm.cyl.cfragment.Communication.Thread.SendManager;
import com.yzlm.cyl.clibrary.Thread.CThread;

import static com.yzlm.cyl.cfragment.Communication.CommProtocol.GB2005.GB2005.getDataStructs;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.GB2005.GB2005.sendHeartbeatCmd;
import static com.yzlm.cyl.cfragment.Communication.CommProtocol.Modbus.RTU2018.ModbusRtu2018Protocol.getTcpHeartBeatMsg;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.getPublicConfigData;
import static com.yzlm.cyl.cfragment.Global.IOBoardUsed;
import static com.yzlm.cyl.cfragment.Global.S1;
import static com.yzlm.cyl.cfragment.Global.S2;
import static com.yzlm.cyl.cfragment.Global.TCP1;
import static com.yzlm.cyl.cfragment.Global.TCP2;
import static com.yzlm.cyl.cfragment.Global.TCP3;
import static com.yzlm.cyl.cfragment.Global.TCPServer1;
import static com.yzlm.cyl.cfragment.Global.TCPServer2;
import static com.yzlm.cyl.cfragment.Global.blAuthenticationFunction;
import static com.yzlm.cyl.cfragment.Global.protocolList;
import static com.yzlm.cyl.cfragment.Global.protocolName;

/*
 * Created by zwj on 2017/5/26.
 */

public class ProtocolUpdateChecking extends CThread {
    @Override
    public void onDestory() {
    }

    private static int numD1 = 2, numD2 = 2, numD3 = 2, numT1 = 2, numT2 = 2, numT3 = 2;
    private static int counter = 0, counterHeartBeat = 0;

    private boolean getMeaDataToUpdate() {

        try {
            serialPortProtocolUpdate(S1);

            tcpOutPortProtocolDigOut(TCP1, 1);
            tcpOutPortProtocolDigOut(TCP2, 2);
            tcpOutPortProtocolDigOut(TCP3, 3);
            serverOutPortProtocolDigOut(TCPServer1,1);
            serverOutPortProtocolDigOut(TCPServer2,2);


        } catch (Exception e) {
            Log.i("except", e.toString());
        }
        return true;
    }


    private void tcpOutPortProtocolDigOut(Communication port, int portNum) {

        // TCP
        String strTCPProtocol = protocolList[Integer.parseInt(getPublicConfigData("TCP_" + portNum + "_Protocol"))];
        String strTCPEnable = getPublicConfigData("TCP_" + portNum);

        // TCP GB2017
        if ((strTCPEnable.equals("true") && (strTCPProtocol.equals(protocolName[1])))) {
            sendGB2017Protocol(port, 2);
        }

        // TCP GB2005
        if ((strTCPEnable.equals("true") && (strTCPProtocol.equals(protocolName[0])))) {
            sendGB2005Protocol(port, 0);
            if (blAuthenticationFunction) {
                // 2分钟上传一次
                if (++numT1 >= 2) {
                    numT1 = 0;
                    sendHeartbeatCmd(port, 1);
                }
            }
        }
        // 江苏协议
        if ((strTCPEnable.equals("true") && (strTCPProtocol.equals(protocolName[5])))) {
            sendJiangsuProtocol(port, 2);
        }
        // gb2017v2协议
        if ((strTCPEnable.equals("true") && (strTCPProtocol.equals(protocolName[7])))) {
            new GB2017_V2().activeProtocol(port, 2);
        }
        // 河南协议
        if ((strTCPEnable.equals("true") && (strTCPProtocol.equals(protocolName[8])))) {
            new HeNan().activeProtocol(port, 2);
        }

    }



    private void serverOutPortProtocolDigOut(Communication port, int portNum) {

        // Server
        String strTCPProtocol = protocolList[Integer.parseInt(getPublicConfigData("Server_" + portNum + "_Protocol"))];
        String strTCPEnable = getPublicConfigData("Server_" + portNum);

        //        // TCP GB2017
        //        if ((strTCPEnable.equals("true") && (strTCPProtocol.equals(protocolName[1])))) {
        //            sendGB2017Protocol(port, 2);
        //        }
        //
        //        // TCP GB2005
        //        if ((strTCPEnable.equals("true") && (strTCPProtocol.equals(protocolName[0])))) {
        //            sendGB2005Protocol(port, 0);
        //            if (blAuthenticationFunction) {
        //                // 2分钟上传一次
        //                if (++numT1 >= 2) {
        //                    numT1 = 0;
        //                    sendHeartbeatCmd(port, 1);
        //                }
        //            }
        //        }
        //        // gb2017v2协议
        //        if ((strTCPEnable.equals("true") && (strTCPProtocol.equals(protocolName[7])))) {
        //            new GB2017_V2().activeProtocol(port, 2);
        //        }

    }

    private void serialPortProtocolIoDigOut1(Communication port) {
        // 串口 GB2005
        if ((protocolList[Integer.parseInt(getPublicConfigData("DIGITAL1"))].equals(protocolName[0]))) {
            sendGB2005Protocol(port, 1);
            if (blAuthenticationFunction) {
                // 2分钟上传一次
                if (++numD1 >= 2) {
                    numD1 = 0;
                    sendHeartbeatCmd(port, 1);
                }
            }
        }
        // 串口 GB2017
        if ((protocolList[Integer.parseInt(getPublicConfigData("DIGITAL1"))].equals(protocolName[1]))) {
            sendGB2017Protocol(port, 1);
        }
        // 串口 江苏动态管控
        if ((protocolList[Integer.parseInt(getPublicConfigData("DIGITAL1"))].equals(protocolName[5]))) {
            sendJiangsuProtocol(port, 1);
        }
        // 串口 gb2017v2
        if ((protocolList[Integer.parseInt(getPublicConfigData("DIGITAL1"))].equals(protocolName[7]))) {
            new GB2017_V2().activeProtocol(port, 1);
        }
        // 串口 河南协议
        if ((protocolList[Integer.parseInt(getPublicConfigData("DIGITAL1"))].equals(protocolName[8]))) {
            new HeNan().activeProtocol(port, 1);
        }
    }

    private void serialPortProtocolIoDigOut2(Communication port) {

        if ((protocolList[Integer.parseInt(getPublicConfigData("DIGITAL2"))].equals(protocolName[0]))) {

            sendGB2005Protocol(port, 2);
            if (blAuthenticationFunction) {
                // 2分钟上传一次
                if (++numD2 >= 2) {
                    numD2 = 0;
                    sendHeartbeatCmd(port, 2);
                }
            }
        }

        if ((protocolList[Integer.parseInt(getPublicConfigData("DIGITAL2"))].equals(protocolName[1]))) {
            sendGB2017Protocol(port, 2);
        }

        if ((protocolList[Integer.parseInt(getPublicConfigData("DIGITAL2"))].equals(protocolName[5]))) {
            sendJiangsuProtocol(port, 2);
        }
        if ((protocolList[Integer.parseInt(getPublicConfigData("DIGITAL2"))].equals(protocolName[7]))) {
            new GB2017_V2().activeProtocol(port, 2);
        }
        if ((protocolList[Integer.parseInt(getPublicConfigData("DIGITAL2"))].equals(protocolName[8]))) {
            new HeNan().activeProtocol(port, 2);
        }
    }

    private void serialPortProtocolIoDigOut3(Communication port) {

        if ((protocolList[Integer.parseInt(getPublicConfigData("DIGITAL3"))].equals(protocolName[0]))) {

            sendGB2005Protocol(port, 3);
            if (blAuthenticationFunction) {
                // 2分钟上传一次
                if (++numD3 >= 2) {
                    numD3 = 0;
                    sendHeartbeatCmd(port, 3);
                }
            }
        }

        if ((protocolList[Integer.parseInt(getPublicConfigData("DIGITAL3"))].equals(protocolName[1]))) {
            sendGB2017Protocol(port, 3);
        }

        if ((protocolList[Integer.parseInt(getPublicConfigData("DIGITAL3"))].equals(protocolName[5]))) {
            sendJiangsuProtocol(port, 3);
        }
        if ((protocolList[Integer.parseInt(getPublicConfigData("DIGITAL3"))].equals(protocolName[7]))) {
            new GB2017_V2().activeProtocol(port, 3);
        }
        if ((protocolList[Integer.parseInt(getPublicConfigData("DIGITAL3"))].equals(protocolName[8]))) {
            new HeNan().activeProtocol(port, 3);
        }
    }

    private void serialPortProtocolCom1OutPut(Communication port) {

        if ((protocolList[Integer.parseInt(getPublicConfigData("COM1_DIGITAL"))].equals(protocolName[0]))) {

            sendGB2005Protocol(port, 2);
            if (blAuthenticationFunction) {
                // 2分钟上传一次
                if (++numD2 >= 2) {
                    numD2 = 0;
                    sendHeartbeatCmd(port, 2);
                }
            }
        }

        if ((protocolList[Integer.parseInt(getPublicConfigData("COM1_DIGITAL"))].equals(protocolName[1]))) {
            sendGB2017Protocol(port, 2);
        }

        if ((protocolList[Integer.parseInt(getPublicConfigData("COM1_DIGITAL"))].equals(protocolName[5]))) {
            sendJiangsuProtocol(port, 2);
        }
        if ((protocolList[Integer.parseInt(getPublicConfigData("COM1_DIGITAL"))].equals(protocolName[7]))) {
            new GB2017_V2().activeProtocol(port, 2);
        }
        if ((protocolList[Integer.parseInt(getPublicConfigData("COM1_DIGITAL"))].equals(protocolName[8]))) {
            new HeNan().activeProtocol(port, 2);
        }
    }

    private void serialPortProtocolCom3OutPut(Communication port) {

        if ((protocolList[Integer.parseInt(getPublicConfigData("COM3_DIGITAL"))].equals(protocolName[0]))) {

            sendGB2005Protocol(port, 2);
            if (blAuthenticationFunction) {
                // 2分钟上传一次
                if (++numD2 >= 2) {
                    numD2 = 0;
                    sendHeartbeatCmd(port, 2);
                }
            }
        }
        if ((protocolList[Integer.parseInt(getPublicConfigData("COM3_DIGITAL"))].equals(protocolName[1]))) {
            sendGB2017Protocol(port, 2);
        }
        if ((protocolList[Integer.parseInt(getPublicConfigData("COM3_DIGITAL"))].equals(protocolName[5]))) {
            sendJiangsuProtocol(port, 2);
        }
        if ((protocolList[Integer.parseInt(getPublicConfigData("COM3_DIGITAL"))].equals(protocolName[7]))) {
            new GB2017_V2().activeProtocol(port, 2);
        }
        if ((protocolList[Integer.parseInt(getPublicConfigData("COM3_DIGITAL"))].equals(protocolName[8]))) {
            new HeNan().activeProtocol(port, 2);
        }
    }

    private void serialPortProtocolUpdate(Communication port) {
        try {
            // IO 配置选择的协议传输
            if (IOBoardUsed) {
                serialPortProtocolIoDigOut1(S1);
                serialPortProtocolIoDigOut2(S1);
                serialPortProtocolIoDigOut3(S1);
            } else {
                // IO 未配置选择的协议传输
                serialPortProtocolCom1OutPut(S2);
                serialPortProtocolCom3OutPut(S1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendHeartBeat(Communication port) {

        // 串口 GB2005
        if ((protocolList[Integer.parseInt(getPublicConfigData("DIGITAL1"))].equals(protocolName[0]))) {
            if (blAuthenticationFunction) {
                // 2分钟上传一次
                if (++numD1 >= 2) {
                    numD1 = 0;
                    sendHeartbeatCmd(port, 1);
                }
            }
        }
        if ((protocolList[Integer.parseInt(getPublicConfigData("DIGITAL2"))].equals(protocolName[0]))) {

            if (blAuthenticationFunction) {
                // 2分钟上传一次
                if (++numD2 >= 2) {
                    numD2 = 0;
                    sendHeartbeatCmd(port, 2);
                }
            }
        }
        if ((protocolList[Integer.parseInt(getPublicConfigData("DIGITAL3"))].equals(protocolName[0]))) {

            if (blAuthenticationFunction) {
                // 2分钟上传一次
                if (++numD3 >= 2) {
                    numD3 = 0;
                    sendHeartbeatCmd(port, 2);
                }
            }
        }

        // TCP  心跳包
        String strTCP1Protocol = protocolList[Integer.parseInt(getPublicConfigData("TCP_1_Protocol"))];
        String strTCP2Protocol = protocolList[Integer.parseInt(getPublicConfigData("TCP_2_Protocol"))];
        String strTCP3Protocol = protocolList[Integer.parseInt(getPublicConfigData("TCP_3_Protocol"))];
        String strTCP1Enable = getPublicConfigData("TCP_1");
        String strTCP2Enable = getPublicConfigData("TCP_2");
        String strTCP3Enable = getPublicConfigData("TCP_3");


        if ((strTCP1Enable.equals("true") && (strTCP1Protocol.equals(protocolName[4])))) {
            if (TCP1 != null) {
                TCP1.sendData(getTcpHeartBeatMsg(strTCP1Protocol));
            }
        }
        if ((strTCP2Enable.equals("true") && (strTCP2Protocol.equals(protocolName[4])))) {
            if (TCP2 != null) {
                TCP2.sendData(getTcpHeartBeatMsg(strTCP2Protocol));
            }
        }
        if ((strTCP3Enable.equals("true") && (strTCP3Protocol.equals(protocolName[4])))) {
            if (TCP3 != null) {
                TCP3.sendData(getTcpHeartBeatMsg(strTCP3Protocol));
            }
        }

        //Server
        String strServer1Protocol = protocolList[Integer.parseInt(getPublicConfigData("Server_1_Protocol"))];
        String strServer2Protocol = protocolList[Integer.parseInt(getPublicConfigData("Server_2_Protocol"))];
        String strServer1Enable = getPublicConfigData("Server_1");
        String strServer2Enable = getPublicConfigData("Server_2");

        //        if ((strServer1Enable.equals("true") && (strServer1Protocol.equals(protocolName[4])))) {
        //            if (TCPServer1 != null) {
        //                TCPServer1.sendData(getTcpHeartBeatMsg(strServer1Protocol));
        //            }
        //        }
        //        if ((strServer2Enable.equals("true") && (strServer2Protocol.equals(protocolName[4])))) {
        //            if (TCPServer2 != null) {
        //                TCPServer2.sendData(getTcpHeartBeatMsg(strServer2Protocol));
        //            }
        //        }

    }

    public void process() {
        short times = 60;
        try {
            times = Short.parseShort(getPublicConfigData("ProtocolUpdateTime"));
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
        try {
            if (++counter >= times) {
                counter = 0;
                getMeaDataToUpdate();
            }
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }

        try {
            // 60S 传输
            if (++counterHeartBeat >= 60) {
                counterHeartBeat = 0;
                sendHeartBeat(S1);
                sendHeartBeat(S2);
            }
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
    }

    public ProtocolUpdateChecking(String name, boolean suspend) {
        super(name, suspend);
        SUSPEND_TIME_MILLISECONDS = 1000;
    }

    //sCom: port 为S1时候有用，用于指定协议内串口号
    private void sendGB2005Protocol(Communication port, int sCom) {
        DataStruct[] data = getDataStructs(sCom, true);
        try {
            String cmd = new GB2005().getMeaDataUpdateCmd(data[0], true);
            if (!cmd.equals("")) {
                SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : "国标浓度上传_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 500, cmd.getBytes());
                Log.i("GB2005主动上传", cmd);
            }
        } catch (Exception e) {
            Log.i("except", e.toString());
        }
    }

    //sCom: port 为S1时候有用，用于指定协议内串口号
    private void sendGB2017Protocol(Communication port, int sCom) {
        try {
            String cmd = new GB2017().getMeaDataUpdateCmd2017(new DataStruct(), true, (byte) 0);
            if (!cmd.equals("")) {
                SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : "国标浓度上传_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 500, cmd.getBytes());
            }

        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
    }

    //sCom: port 为S1时候有用，用于指定协议内串口号
    private void sendJiangsuProtocol(Communication port, int sCom) {
        try {
            String cmd = new JiangSu().getMeaDataUpdateCmdJiangSu(new DataStruct(), true);
            if (!cmd.equals("")) {
                SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "打印输出_0_0" : "江苏协议浓度上传_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 500, cmd.getBytes());
            }
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }

    }



}
