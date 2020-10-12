package com.yzlm.cyl.cfragment.Communication.Thread;


import android.util.Log;

import com.yzlm.cyl.cfragment.Communication.Communication;

import static com.yzlm.cyl.cfragment.Communication.Component.Cmd.CommandPackaging.getBoardCmd;
import static com.yzlm.cyl.cfragment.Communication.Component.Cmd.CommandPackaging.getCmd;
import static com.yzlm.cyl.cfragment.Communication.Component.Cmd.CommandPackaging.switchBoardSetCmdPackaging;
import static com.yzlm.cyl.cfragment.Communication.Component.Cmd.CommandParsing.communicationTest;
import static com.yzlm.cyl.cfragment.Content.Component.List4.List4_Content3_UpdateBoard.DataReceived;
import static com.yzlm.cyl.cfragment.Global.RealTimeStatusThread;
import static com.yzlm.cyl.cfragment.Global.S0;
import static com.yzlm.cyl.cfragment.Global.S1;
import static com.yzlm.cyl.cfragment.Global.S2;
import static com.yzlm.cyl.cfragment.Global.S3;
import static com.yzlm.cyl.cfragment.Global.TCP1;
import static com.yzlm.cyl.cfragment.Global.TCP2;
import static com.yzlm.cyl.cfragment.Global.TCP3;
import static com.yzlm.cyl.cfragment.Global.TCPServer1;
import static com.yzlm.cyl.cfragment.Global.TCPServer2;
import static com.yzlm.cyl.cfragment.Global.blUpdateCKBFlag;
import static com.yzlm.cyl.cfragment.Global.blWatchDogFeedFlagForCK;
import static com.yzlm.cyl.cfragment.Global.saveRunInfo2File;
import static com.yzlm.cyl.clibrary.Util.DataUtil.bytesToHexString;
import static java.lang.System.arraycopy;

/**
 * Created by caoyiliang on 2017/1/19.
 */

public class SendThread extends Thread {
    private String strName;
    private String Component;
    private String Command;
    private byte Number;
    private String Code;
    private Object mObject;
    private Communication sp;
    private int Try;
    private int sleep;

    public static volatile boolean synchronizationS1 = false;
    public static volatile boolean synchronizationS2 = false;
    public static volatile boolean synchronizationS3 = false;

    /*mCompName+"更新测控板" + "_8_20"*/
    public SendThread(String name, Communication sp, int Try, int sleep, Object object) {
        strName = name;
        Component = name.split("_")[0];
        Command = name.split("_")[1];
        Number = Byte.valueOf(name.split("_")[2], 16);
        Code = name.split("_")[3];


        mObject = object;
        this.sp = sp;
        this.Try = Try;
        this.sleep = sleep;

//        if (sp == S0) {
//            System.out.println("测试 ："+"Component = " + Component +"Command = " +
//                    Command+"Number = " + Number+"Code = " + Code+"mObject = " + mObject);
//        }
    }

    public void run() {
        try {
            int i = 0;
            byte[] rs;
            do {
                rs = new byte[0];
                //若i > 0,则表示命令发送超时，保存超时状态到文件
                if ((i > (Try - 1))) {
                    if (Try != 1) {
                        if (!Command.equals("实时状态")) {
                            String str = "组分[" + Component + "] 发送命令[" + Command + "]超时，通讯失败...";
                            saveRunInfo2File(str);
                        }
                    }
                    RealTimeStatusThread.resume();
                    return;
                }

                if (i != 0) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                byte[] cmd = null;
                if (sp == S1) {//接口板
                    synchronizationS1 = false;
                    cmd = getBoardCmd(Number, Code, mObject);
                } else if (sp == S2) {//通讯板
                    synchronizationS2 = false;
                    switch (Component) {
                        case "SWITCHBOARD":
                            cmd = switchBoardSetCmdPackaging(Number, mObject);
                            break;
                        case "IO":
                            cmd = (byte[]) mObject;
                            break;
                        default:
                            cmd = (byte[]) mObject;
                            break;
                    }
                } else if (sp == S0) {//测控板
                    if (blUpdateCKBFlag) {
                        if (!Command.equals("更新测控板")) {
                            return;
                        }
                        cmd = getCmd(Component, mObject);
                        Log.d("更新测控板", "发送指令:" + bytesToHexString(cmd, cmd.length));
                    } else {
                        if (Command.equals("更新测控板")) {
                            return;
                        }
                        cmd = getCmd(Component, Command, Number, Code, mObject);
//                        Log.d("无更新测控板", "发送指令:" + bytesToHexString(cmd, cmd.length));
                    }
                } else if (sp == S3) {//DTU
                    synchronizationS3 = false;
                    if (Component.equals("DTU")) {
                        cmd = (byte[]) mObject;
                    }
                } else if (sp == TCP1 || sp == TCP2 || sp == TCP3 || sp == TCPServer1 || sp == TCPServer2) {
                    cmd = (byte[]) mObject;
                }

                sp.sendData(cmd);
                if (Try != 1) {
                    if (!Command.equals("实时状态")) {
                        String str = "组分[" + Component + "] 发送命令[" + Command + "]:" + bytesToHexString(cmd, cmd.length);
                        saveRunInfo2File(str);
                    }
                }


                if (sp == S0) {
                    byte[] byRs;
                    byte[] reData = new byte[2000];
                    int len = 0;
                    int outTime = 0;
                    while (sp != null) {
                        byRs = new byte[0];
                        byRs = sp.receiveData(byRs);

                        if (byRs != null) {
                            if (byRs.length > 0) {
                                if (byRs.length < reData.length && byRs.length < (reData.length - len)) {
                                    arraycopy(byRs, 0, reData, len, byRs.length);
                                    len += byRs.length;
                                }
                            }
                        } else {
                            if (len > 0) {
                                rs = new byte[len];
                                arraycopy(reData, 0, rs, 0, len);
                                break;
                            }
                            len = 0;
                        }


                        try {
                            Thread.sleep(12);
                            if ((++outTime * 12) > sleep) {
                                break;
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    Thread.sleep(sleep);
                }
                i++;
            }
            while (!(communicationTest(Component, Number, Code, rs, sp)
                    && sp == S0)
                    && !(synchronizationS1 && sp == S1)
                    && !(synchronizationS2 && sp == S2)
                    && !(synchronizationS3 && sp == S3));


            if (sp == S0) {//测控板
                if (!blUpdateCKBFlag) {
                    ReceiveParsing thread = new ReceiveParsing(strName, rs, mObject);
                    thread.start();
                    blWatchDogFeedFlagForCK = true;
                } else {
                    Log.d("更新测控板", "接收指令:" + bytesToHexString(rs, rs.length));
                    DataReceived(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
