package com.yzlm.cyl.cfragment.AppFunction;

import com.yzlm.cyl.cfragment.Communication.Thread.SendManager;
import com.yzlm.cyl.cfragment.Global;
import com.yzlm.cyl.clibrary.Util.DataUtil;

import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Global.S0;
import static com.yzlm.cyl.cfragment.Global.getCmds;
import static com.yzlm.cyl.cfragment.Global.strAll5721Component;
import static com.yzlm.cyl.cfragment.Global.strComponent;
import static java.lang.Thread.sleep;

/**
 * Created by zwj on 2017/11/9.
 */

public class GetDevProtectInfoRunnable implements Runnable {

    @Override
    public void run() {
        String strLockInfo;
        for (String item : strAll5721Component.get(1)) {

            if (!getConfigData(item, "FACTORY_TIME").equals("")) {
                byte[] arrayOfByte2 = DataUtil.shortToByte((short) 201);
                DataUtil.reverse(arrayOfByte2);
                SendManager.SendCmd(item + "_查延保信息" + "_3_" + DataUtil.bytesToHexString(arrayOfByte2, 2).replace(" ", ""), Global.S0, 1, 200, 1);
                try {
                    sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                strLockInfo = ((getCmds(item).getCmd(201).getValue() == null ? "" : getCmds(item).getCmd(201).getValue()).toString());
                if (!strLockInfo.equals("0")) {

                    byte[] arrayOfByte3 = DataUtil.shortToByte((short) 209);
                    DataUtil.reverse(arrayOfByte3);
                    SendManager.SendCmd(item + "_查延保信息" + "_3_" + DataUtil.bytesToHexString(arrayOfByte3, 2).replace(" ", ""), Global.S0, 1, 200, 1);
                }
                try {
                    sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
