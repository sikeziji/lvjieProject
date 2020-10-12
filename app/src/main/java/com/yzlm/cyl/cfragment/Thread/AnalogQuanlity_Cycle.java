package com.yzlm.cyl.cfragment.Thread;

import android.util.Log;

import com.yzlm.cyl.cfragment.Communication.Thread.SendManager;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.cfragment.ThreadHelper.ZThread;

import static com.yzlm.cyl.cfragment.Communication.CommunitcationTest.S1_SendTimes;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.getPublicConfigData;
import static com.yzlm.cyl.cfragment.Content.Component.ContentTool.getLastValue;
import static com.yzlm.cyl.cfragment.Global.IOBoardUsed;
import static com.yzlm.cyl.cfragment.Global.S1;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.floatToBytes;
import static com.yzlm.cyl.cfragment.Global.strComponent;
import static com.yzlm.cyl.clibrary.Util.DataUtil.bytesToHexString;
import static com.yzlm.cyl.clibrary.Util.DataUtil.copybyte;
import static com.yzlm.cyl.clibrary.Util.DataUtil.reverse;
import static com.yzlm.cyl.clibrary.Util.DataUtil.shortToByte;

/**
 * Created by WL on 2017/5/3.
 * 模拟量输入输出线程
 */

public class AnalogQuanlity_Cycle extends ZThread {

    public AnalogQuanlity_Cycle(String name, boolean suspend) {
        super(name, suspend);
        SUSPEND_TIME_MILLISECONDS = 10 * 1000;
    }

    @Override
    public void process() {

        // 模拟量输入
        {
            /*水站不需要接口板，不读取流量*/
            if (!IOBoardUsed) {
                return;
            }
            if (Integer.parseInt(getPublicConfigData("CONTROL_DEV")) != 0 && ((Integer.parseInt(getPublicConfigData("CONTROL_CHANNEL")) == 2)
                    || (Integer.parseInt(getPublicConfigData("CONTROL_CHANNEL")) == 3))) {
                byte[] arrayOfByte = shortToByte((short) 1);
                reverse(arrayOfByte);
                S1_SendTimes++;
                Log.i("4-20mA输入发送条数", String.valueOf(S1_SendTimes));
                SendManager.SendCmd("所有组分" + "_读取4-20mA输入" + "_3_" + bytesToHexString(arrayOfByte, 2).replace(" ", ""), S1, 1, 200, 8);
            }
        }
        float lastVal1, lastVal2;

        // 模拟量输出
        if (getPublicConfigData("MNTD1").equals("-1") || Integer.parseInt(getPublicConfigData("MNTD1")) >= strComponent.get(1).length) {
            lastVal1 = 4;
        } else {
            try {
                String comp1 = strComponent.get(1)[Integer.parseInt(getPublicConfigData("MNTD1"))];
                String Unit = getConfigData(comp1, "UNIT");

                String RangeLow = getConfigData(comp1, "YBLCL");
                String Range = getConfigData(comp1, "YBLCH");
                float fullRangeLow = Float.parseFloat(RangeLow);
                float fullRange1 = Float.parseFloat(Range);

                String flow = null;
                if (!getPublicConfigData("CAL_DATA_UPDATE").equals("true")) {
                    flow = context.getString(R.string.ZY);
                }
                lastVal1 = Float.parseFloat(getLastValue(comp1, flow, Unit));
                if (lastVal1 <= 0) {
                    lastVal1 = 4;
                } else if (lastVal1 >= fullRange1) {
                    lastVal1 = 20;
                } else {
                    lastVal1 = ((lastVal1 - fullRangeLow) / (fullRange1 - fullRangeLow)) * 16 + 4;
                }
            } catch (Exception e) {
                lastVal1 = 4;
            }
        }
        if (getPublicConfigData("MNTD2").equals("-1") || Integer.parseInt(getPublicConfigData("MNTD2")) >= strComponent.get(1).length) {
            lastVal2 = 4;
        } else {
            try {
                String comp2 = strComponent.get(1)[Integer.parseInt(getPublicConfigData("MNTD2"))];
                String RangeLow = getConfigData(comp2, "YBLCL");
                String Range = getConfigData(comp2, "YBLCH");
                float fullRangeLow = Float.parseFloat(RangeLow);
                float fullRange2 = Float.parseFloat(Range);
                String Unit = getConfigData(comp2, "UNIT");

                String flow = null;
                if (!getPublicConfigData("CAL_DATA_UPDATE").equals("true")) {
                    flow = context.getString(R.string.ZY);
                }
                lastVal2 = Float.parseFloat(getLastValue(comp2, flow, Unit));
                if (lastVal2 <= 0) {
                    lastVal2 = 4;
                } else if (lastVal2 >= fullRange2) {
                    lastVal2 = 20;
                } else {
                    lastVal2 = ((lastVal2 - fullRangeLow) / (fullRange2 - fullRangeLow)) * 16 + 4;
                }
            } catch (Exception e) {
                lastVal2 = 4;
            }
        }
        if ((!(getPublicConfigData("MNTD2").equals("-1"))) || (!(getPublicConfigData("MNTD1").equals("-1")))) {
            byte[] analogOutByte = copybyte(floatToBytes(lastVal1), floatToBytes(lastVal2));

            SendManager.SendCmd("所有组分" + "_设置4-20mA输出" + "_6_0", S1, 1, 0, analogOutByte);
        }
    }

    @Override
    public void onDestory() {

    }
}
