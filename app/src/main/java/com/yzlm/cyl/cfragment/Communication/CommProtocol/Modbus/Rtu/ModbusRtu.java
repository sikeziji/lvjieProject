package com.yzlm.cyl.cfragment.Communication.CommProtocol.Modbus.Rtu;
import android.os.Bundle;


import com.yzlm.cyl.cfragment.Communication.CommProtocol.Modbus.RTU2018.ModbusRtuTable;
import com.yzlm.cyl.cfragment.Dialog.Dialog_Err;

import java.io.File;

import static com.yzlm.cyl.cfragment.Frame.MainActivity.main;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.mfb;
import static com.yzlm.cyl.cfragment.Global.SdcardPath;
import static com.yzlm.cyl.cfragment.Global.extSdcardPath;
import static com.yzlm.cyl.cfragment.Global.lRtuCmdData;
import static com.yzlm.cyl.clibrary.CFragment.CSingleFragmentActivity.fm;
import static com.yzlm.cyl.clibrary.Util.FileUtil.readString;

/**
 * Created by zwj on 2017/12/19.
 */

class ModbusRtu {

    public static void syncListModbusRtu() {

        String modbusRtuDir;
        if (SdcardPath == null) {
            if (extSdcardPath == null) {
                return;
            } else {
                modbusRtuDir = extSdcardPath;
            }
        } else {
            modbusRtuDir = SdcardPath;
        }
        modbusRtuDir += "Csoft" + "/Protocol/Modbus/";
        String errorPath = modbusRtuDir + "Rtu" + ".txt";
        File dbFile = new File(errorPath);
        if (dbFile.exists()) {
            String strData = readString(errorPath, "GBK");
            String[] strsData = strData.replace("\t", "").replace(" ", "").replace("\r\n", "").replace("\n", "").split("#");

            String strFuncCode;
            ModbusRtuTable rrt = new ModbusRtuTable();

            ModbusRtuTable rwt = new ModbusRtuTable();

            for (int i = 0; i < strsData.length; i++) {
                try {
                    if (strsData[i].contains("//")) {
                        continue;
                    }
                    if (strsData[i].contains("功能码03")) {

                        for (i += 1; i < strsData.length; i++) {
                            RtuDataStruct rrds = new RtuDataStruct();
                            if (strsData[i].contains("功能码06")) {
                                break;
                            }
                            if (strsData[i].contains("//")) {
                                continue;
                            }
                            String[] strsItem = strsData[i].split("[,，]");
                            rrds.setRegAddr(Integer.valueOf(strsItem[0]));
                            rrds.setDataLen(Integer.valueOf(strsItem[1]));
                            rrds.setDescribe(strsItem[2]);
                            rrt.Add(rrds);
                        }
                        strFuncCode = "03";
                        if (rrt.getSize() > 0) {
                            lRtuCmdData.put(strFuncCode, rrt);
                        }
                    }
                    if (strsData[i].contains("功能码06")) {

                        for (i = i + 1; i < strsData.length; i++) {
                            RtuDataStruct rwds = new RtuDataStruct();
                            if (strsData[i].contains("//")) {
                                continue;
                            }
                            if (strsData[i].contains("功能码03")) {
                                break;
                            }
                            String[] strsItem = strsData[i].split("[,，]");
                            rwds.setRegAddr(Integer.valueOf(strsItem[0]));
                            rwds.setDataLen(Integer.valueOf(strsItem[1]));
                            rwds.setDescribe(strsItem[2]);
                            rwt.Add(rwds);
                        }
                        strFuncCode = "06";
                        if (rwt.getSize() > 0) {
                            lRtuCmdData.put(strFuncCode, rwt);
                        }
                    }
                } catch (Exception ex) {
                    main.removeDestopText(mfb);
                    Dialog_Err st = new Dialog_Err();
                    Bundle bundle = new Bundle();
                    bundle.putString("alert-err", "ModbusRtu 文件有误 ！");
                    st.setArguments(bundle);
                    st.show(fm, "Dialog_err");
                }
            }
        } else {
            main.removeDestopText(mfb);
            Dialog_Err st = new Dialog_Err();
            Bundle bundle = new Bundle();
            bundle.putString("alert-err", " MudbusRtu文件丢失！");
            st.setArguments(bundle);
            st.show(fm, "Dialog_err");
        }
    }
}
