package com.yzlm.cyl.cfragment.Thread;

import com.yzlm.cyl.cfragment.Communication.Thread.SendManager;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.cfragment.ThreadHelper.ZThread;

import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.getPublicConfigData;
import static com.yzlm.cyl.cfragment.Global.IOBoardUsed;
import static com.yzlm.cyl.cfragment.Global.S1;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.digitalChannelVal;
import static com.yzlm.cyl.cfragment.Global.doControlJob;
import static com.yzlm.cyl.cfragment.Global.doFlowing;
import static com.yzlm.cyl.cfragment.Global.strComponent;
import static com.yzlm.cyl.clibrary.Util.DataUtil.bytesToHexString;
import static com.yzlm.cyl.clibrary.Util.DataUtil.reverse;
import static com.yzlm.cyl.clibrary.Util.DataUtil.shortToByte;
import static java.lang.Integer.parseInt;
import static java.lang.Thread.sleep;

/**
 * Created by ZWJ on 2019/8/26
 * 开关量输入 读取
 */

public class DigitalAnalogInQuanlity_Cycle extends ZThread {

    public DigitalAnalogInQuanlity_Cycle(String name, boolean suspend) {
        super(name, suspend);
        SUSPEND_TIME_MILLISECONDS = 3 * 1000;
    }

    @Override
    public void process() {
        try {
            if (IOBoardUsed) {
                int channelId = parseInt(getPublicConfigData("CONTROL_CHANNEL"));
                int channelDev = Integer.parseInt(getPublicConfigData("CONTROL_DEV"));

                /**开关量输入*/  /**采样器*/
                if ((channelId == 0 || channelId == 1) && (channelDev == 1)) {
                    digitalChannelVal[channelId] = 0;
                    byte[] arrayOfByte = shortToByte((short) 2);
                    reverse(arrayOfByte);
                    SendManager.SendCmd("所有组分" + "_读取开关量输入" + "_3_" + bytesToHexString(arrayOfByte, 2).replace(" ", ""), S1, 3, 200, 4);
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (strComponent.get(1).length > 0) {
                        for (String item : strComponent.get(1)) {
                            if (doFlowing.get(item).equals(context.getString(R.string.waiting_for_instructions))) {
                                if (digitalChannelVal[channelId] == 1 && Integer.parseInt(getPublicConfigData("CONTROL_DEV")) == 1) {
                                    doControlJob(item, context.getString(R.string.autoJob));
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
    }


    @Override
    public void onDestory() {

    }
}
