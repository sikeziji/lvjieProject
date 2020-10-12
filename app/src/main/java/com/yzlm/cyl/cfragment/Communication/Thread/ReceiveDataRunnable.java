package com.yzlm.cyl.cfragment.Communication.Thread;

import com.yzlm.cyl.cfragment.Communication.Communication;
import com.yzlm.cyl.cfragment.Communication.Component.Cmd.COM1ReceiveParse;
import com.yzlm.cyl.cfragment.Communication.Component.Cmd.COM3ReceiveParse;
import com.yzlm.cyl.cfragment.Communication.Component.Cmd.COM4ReceiveParse;
import com.yzlm.cyl.cfragment.Communication.Component.Cmd.IOBoardReceiveParse;

import java.util.Arrays;

import static com.yzlm.cyl.cfragment.Global.IOBoardUsed;
import static com.yzlm.cyl.cfragment.Global.S1;
import static com.yzlm.cyl.cfragment.Global.S2;
import static com.yzlm.cyl.cfragment.Global.S3;
import static com.yzlm.cyl.cfragment.Global.blWatchDogFeedFlagForIO;
import static com.yzlm.cyl.clibrary.Util.DataUtil.crc16;
import static java.lang.System.arraycopy;

/**
 * Created by zwj on 2017/5/23.
 */

public class ReceiveDataRunnable implements Runnable {

    private Communication serialPort;

    public ReceiveDataRunnable(Communication serialPort) {
        this.serialPort = serialPort;
    }

    public void run() {
        byte[] rs;
        byte[] reData = new byte[2000];
        int len = 0;

        while (this.serialPort != null) {
            if (this.serialPort.isClose()) {
                return;
            }
            rs = new byte[0];
            rs = serialPort.receiveData(rs);
            if (rs != null) {
                if (rs.length > 0) {
                    if (rs.length < reData.length && rs.length < (reData.length - len)) {
                        arraycopy(rs, 0, reData, len, rs.length);
                        len += rs.length;
                    }
                }
            } else {

                if (len > 0) {

                    byte[] tempData = new byte[len];
                    arraycopy(reData, 0, tempData, 0, len);
                    if (!IOBoardUsed) {

                        if (this.serialPort == S1) {
                            // 用于对外协议
                            /*String str = "接收到工控机命令:" + bytesToHexString(tempData, tempData.length);
                            saveRunInfo2File(str);*/
                            COM3ReceiveParse com3ReceiveParse = new COM3ReceiveParse(S1, tempData);//接口板
                            com3ReceiveParse.start();
                        } else if (this.serialPort == S2) {
                            // 用于对外协议
                            COM1ReceiveParse com1ReceiveParse = new COM1ReceiveParse(S2, tempData);//通讯板
                            com1ReceiveParse.start();
                        } else if (this.serialPort == S3) {
                            // 用于内部对外协议
                            COM4ReceiveParse COM4ReceiveParse = new COM4ReceiveParse(tempData);
                            COM4ReceiveParse.start();
                        }
                    } else {

                        if (ReceiveDataCheck(tempData)) {
                            if (this.serialPort == S1) {
                                // 用于对内接口板通讯
                                blWatchDogFeedFlagForIO = true;
                                /*String str = "接收到工控机命令:" + bytesToHexString(tempData, tempData.length);
                                saveRunInfo2File(str);*/
                                IOBoardReceiveParse ioBoardDataParsing = new IOBoardReceiveParse(S1, tempData);
                                ioBoardDataParsing.start();
                            }
                        } else if (this.serialPort == S2) {
                            // 用于对外协议
                            COM1ReceiveParse com1ReceiveParse = new COM1ReceiveParse(S2, tempData);
                            com1ReceiveParse.start();
                        } else if (this.serialPort == S3) {
                            // 用于内部对外协议
                            COM4ReceiveParse COM4ReceiveParse = new COM4ReceiveParse(tempData);
                            COM4ReceiveParse.start();
                        }
                    }
                } else {
                    for (int j = 0; j < reData.length; j++) {
                        reData[j] = 0;
                    }
                }
                len = 0;
            }
            try {
                Thread.sleep(60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /*CRC 校验*/
    private Boolean ReceiveDataCheck(byte[] Command) {

        byte[] crc = new byte[2];

        if (Command.length > 2) {
            arraycopy(Command, Command.length - 2, crc, 0, 2);
            return Arrays.equals(crc16(Command, Command.length - 2), crc);
        }
        return false;
    }
}
