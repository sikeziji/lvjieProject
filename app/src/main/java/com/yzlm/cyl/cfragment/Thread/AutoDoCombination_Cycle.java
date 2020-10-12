package com.yzlm.cyl.cfragment.Thread;


import com.yzlm.cyl.cfragment.Communication.Thread.SendManager;
import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.Thread.CThread;

import java.util.HashMap;
import java.util.Map;

import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.updateConfigData;
import static com.yzlm.cyl.cfragment.Content.Component.List1.List1_Content2.getFlowName;
import static com.yzlm.cyl.cfragment.Global.AutoSampleEnable;
import static com.yzlm.cyl.cfragment.Global.S0;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.doControlJob;
import static com.yzlm.cyl.cfragment.Global.doFlowing;
import static com.yzlm.cyl.cfragment.Global.strComponent;
import static com.yzlm.cyl.clibrary.Util.DataUtil.bytesToHexString;
import static com.yzlm.cyl.clibrary.Util.DataUtil.reverse;
import static com.yzlm.cyl.clibrary.Util.DataUtil.shortToByte;
import static java.lang.Thread.sleep;

/**
 * 组合测试 间隔发下流程
 */
public class AutoDoCombination_Cycle extends CThread {

    private static Map<String, Integer> mCount = new HashMap();

    public AutoDoCombination_Cycle(String name, boolean suspend) {
        super(name, suspend);
        for (int i = 0; i < strComponent.get(1).length; i++) {
            mCount.put(strComponent.get(1)[i], 0);
        }
        SUSPEND_TIME_MILLISECONDS = 1000 * 10;
    }

    @Override
    public void process() {

        try {
            for (int i = 0; i < strComponent.get(1).length; i++) {
                if (getConfigData(strComponent.get(1)[i], "zhcsSwitch").equals("true") &&
                        getConfigData(strComponent.get(1)[i], "zhcsStartFlag").equals("true")) {
                    if (AutoSampleEnable.get(strComponent.get(1)[i]).equals("true") &&
                            doFlowing.get(strComponent.get(1)[i]).equals(context.getString(R.string.waiting_for_instructions))) {
                        Integer num = mCount.get(strComponent.get(1)[i]);
                        num++;
                        if (num > 5) {
                            mCount.put(strComponent.get(1)[i], 0);
                        } else {
                            mCount.put(strComponent.get(1)[i], num);
                            continue;
                        }

                        //判断若组合测试流程不为空，则更新实时状态，否则关闭自动做样开关、开始标志
                        if (!getFlowName(strComponent.get(1)[i], 0).equals("")) {
                            byte[] arrayOfByte = shortToByte((short) 50);
                            reverse(arrayOfByte);
                            SendManager.SendCmd(strComponent.get(1)[i] + "_实时状态" + "_3_" + bytesToHexString(arrayOfByte, 2).replace(" ", ""), S0, 3, 200, 4);
                            sleep(1000);
                            if (doControlJob(strComponent.get(1)[i], getFlowName(strComponent.get(1)[i], 0))) {
                                DeleteNowDoingJob(strComponent.get(1)[i]);
                            }
                        } else {
                            updateConfigData(strComponent.get(1)[i], "zhcsStartFlag", "false");
                            updateConfigData(strComponent.get(1)[i], "zhcsSwitch", "false");
                        }
                    } else {
                        mCount.put(strComponent.get(1)[i], 0);
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void onDestory() {

    }

    private static void DeleteNowDoingJob(String mCompname) {

        String[] str = getConfigData(mCompname, "zhcsFlow").split(";");
        int num = Integer.valueOf(str[0].split(",")[1]);
        if (num > 0) {
            num--;
            if (num <= 0) {
                // 清除第一个流程
                String strFlow = "";
                for (int i = 1; i < str.length; i++) {
                    strFlow += str[i] + ";";
                }
                updateConfigData(mCompname, "zhcsFlow", strFlow);
            } else {
                // 更改第一个流程的次数后重新写入
                String strFlow1 = str[0].split(",")[0] + "," + num + ";";
                String strFlow = "";
                for (int i = 1; i < str.length; i++) {
                    strFlow += str[i] + ";";
                }
                updateConfigData(mCompname, "zhcsFlow", strFlow1 + strFlow);
            }
        }
    }
}
