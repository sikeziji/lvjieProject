package com.yzlm.cyl.cfragment.Communication.Component.Cmd;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.yzlm.cyl.cfragment.Communication.Communication;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.clibrary.Util.DataUtil;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static com.yzlm.cyl.cfragment.Communication.CommunitcationTest.S1_ReadTimes;
import static com.yzlm.cyl.cfragment.Communication.Component.Cmd.CommandParsing.AddError;
import static com.yzlm.cyl.cfragment.Communication.Thread.SendThread.synchronizationS1;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.getPublicConfigData;
import static com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content2.commTestHandler;
import static com.yzlm.cyl.cfragment.Content.Component.List5.List5_Content1.id;
import static com.yzlm.cyl.cfragment.Content.Component.List5.List5_Content1.mSoftwareVerHandler;
import static com.yzlm.cyl.cfragment.Content.Component.List5.List5_Content1.verStrArr;
import static com.yzlm.cyl.cfragment.DBConvert.Tool.Tool.dataFormat;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.WinWidgetHandler;
import static com.yzlm.cyl.cfragment.Global.channel1Val;
import static com.yzlm.cyl.cfragment.Global.channel2Val;
import static com.yzlm.cyl.cfragment.Global.channelValUpdateFlag;
import static com.yzlm.cyl.cfragment.Global.digitalChannelVal;
import static com.yzlm.cyl.cfragment.Global.digitalTest;
import static com.yzlm.cyl.cfragment.Global.protocolList;
import static com.yzlm.cyl.cfragment.Global.sampleDoingFlag;
import static com.yzlm.cyl.cfragment.Global.strComponent;
import static com.yzlm.cyl.clibrary.Util.DataUtil.crc16;
import static com.yzlm.cyl.clibrary.Util.DataUtil.toInt;
import static java.lang.System.arraycopy;


/**
 * 接口板回发协议解析线程类
 * Created by WL on 2017/5/4.
 */

public class IOBoardReceiveParse extends Thread {

    private byte[] rs;
    private byte dataLength;
    private Communication port;

    public IOBoardReceiveParse(Communication port, byte[] rs) {
        this.rs = rs;
        this.port = port;

        if (rs.length > 5) {
            this.dataLength = rs[5];
        } else {
            this.dataLength = 1;
        }
    }

    @Override
    public void run() {

        try {
            if (rs[0] == 0x1A) {
                synchronizationS1 = true;
                switch (rs[1]) {
                    case 0x03: {
                        switch (rs[3]) {
                            case 0x01: {
                                channelValUpdateFlag = true;
                                byte[] dataByte = new byte[dataLength];
                                System.arraycopy(rs, 6, dataByte, 0, dataLength);
                                channel1Val = DataUtil.byte2float(dataByte, 0);
                                channel2Val = DataUtil.byte2float(dataByte, 4);

                                channel1Val = Float.parseFloat(dataFormat(channel1Val, 3));
                                channel2Val = Float.parseFloat(dataFormat(channel2Val, 3));
                                Message msg = new Message();
                                msg.what = 3;
                                WinWidgetHandler.sendMessage(msg);
                                S1_ReadTimes++;
                                Log.i("4-20mA输入接收条数", String.valueOf(S1_ReadTimes));
                                synchronizationS1 = true;
                                Log.i(" 接收03 synchronizationS1", String.valueOf(synchronizationS1));
                            }
                            break;
                            case 0x02:
                                synchronizationS1 = true;
                                if (rs[5] == 0x04) {
                                    digitalChannelVal[0] = (byte) (rs[6] == 1 ? 1 : 0);
                                    digitalChannelVal[1] = (byte) (rs[7] == 1 ? 1 : 0);
                                    digitalChannelVal[2] = (byte) (rs[8] == 1 ? 1 : 0);
                                    digitalChannelVal[3] = (byte) (rs[9] == 1 ? 1 : 0);

                                    Message msg = new Message();
                                    msg.what = 3;
                                    WinWidgetHandler.sendMessage(msg);
                                    if (getPublicConfigData("SAMPLE_LEVEL_SWITCH").equals("1")) {
                                        for (String item : strComponent.get(1)) {
                                            /*增加一条没有液体记录*/
                                            if (sampleDoingFlag.get(item).equals("true")) {
                                                if (digitalChannelVal[0] == 1) {
                                                    AddError(item, 541, ErrorLog.msgType.运行_信息);
                                                }
                                                sampleDoingFlag.put(item, "false");
                                            }
                                        }
                                    }
                                }
                                break;
                            case 0x06: {
                                byte[] dataByte = new byte[dataLength];
                                System.arraycopy(rs, 6, dataByte, 0, dataLength);
                                verStrArr[id][3] = new String(dataByte, 0, dataByte.length, StandardCharsets.UTF_8);
                                Message msg = new Message();
                                msg.what = 6;
                                mSoftwareVerHandler.sendMessage(msg);
                                synchronizationS1 = true;
                                Log.i(" 接收06 synchronizationS1", String.valueOf(synchronizationS1));
                            }
                            break;
                            case 0x07: {
                                synchronizationS1 = true;
                                byte[] dataByte = new byte[dataLength];
                                System.arraycopy(rs, 6, dataByte, 0, dataLength);

                                Message msg = new Message();
                                msg.what = 4;
                                msg.obj = toInt(dataByte);
                                WinWidgetHandler.sendMessage(msg);
                            }
                            break;
                        }
                    }
                    break;
                    case 0x09: {
                        switch (rs[2] * 0x100 + rs[3]) {
                            case 1:/*数采仪*/
                            case 2:/*pc*/
                            case 3:/*spec*/
                                try {
                                    if (digitalTest) {
                                        Message msg = new Message();
                                        msg.what = (rs[2] * 0x100 + rs[3]) == 2 ? 10 :((rs[2] * 0x100 + rs[3]) == 1 ? 3 : 4);
                                        Bundle bundleData = new Bundle();
                                        bundleData.putByteArray("IOReceiveData", rs);
                                        msg.setData(bundleData);
                                        commTestHandler.sendMessage(msg);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                if (isIOData(rs)) {
                                    byte[] rsData = new byte[rs.length - 8];
                                    arraycopy(rs, 6, rsData, 0, rsData.length);

                                    ProtocolResponse protocolResponse = new ProtocolResponse();
                                    protocolResponse.ParsingProtocol(port, (((rs[2] * 0x100 + rs[3]) == 2) ? 3 : (((rs[2] * 0x100 + rs[3]) == 1) ? 1 : 2)), protocolList[Integer.parseInt(getPublicConfigData("DIGITAL" + (((rs[2] * 0x100 + rs[3]) == 2) ? "3" : (((rs[2] * 0x100 + rs[3]) == 1) ? "1" : "2"))))], rsData);
                                }
                                break;
                            case 4:/*Print*/
                                break;
                        }
                    }
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isIOData(byte[] rs) {
        // 将包含接口板协议内容去除
        if (rs != null) {
            // 接口板协议内容最小字节数为8
            if (rs.length > 8) {
                byte[] crc = new byte[2];
                arraycopy(rs, rs.length - 2, crc, 0, 2);
                byte[] rsData = new byte[rs.length - 2];
                arraycopy(rs, 0, rsData, 0, rsData.length);

                return Arrays.equals(crc16(rsData, rsData.length), crc);
            }
        }
        return false;
    }
}
