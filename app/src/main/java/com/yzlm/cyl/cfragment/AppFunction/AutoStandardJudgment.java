package com.yzlm.cyl.cfragment.AppFunction;

import com.yzlm.cyl.cfragment.R;

import static com.yzlm.cyl.cfragment.Communication.Component.Cmd.CommandParsing.AddError;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.updateConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog.msgType.运维_信息;
import static com.yzlm.cyl.cfragment.Global.AutoSampleEnable;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.doControlJob;
import static com.yzlm.cyl.cfragment.Global.doFlowing;
import static com.yzlm.cyl.cfragment.Global.saveRunInfo2File;

/**
 * 标核判定
 */
public class AutoStandardJudgment {
    public AutoStandardJudgment(String compName) {
        if (getConfigData(compName, "BHJZFlag").equals("true")) {
            // "等待指令"
            if (doFlowing.get(compName).equals(context.getString(R.string.waiting_for_instructions))) {

                if (AutoSampleEnable.get(compName).equals("true")) {
                    int iBHJZCount = Integer.parseInt(getConfigData(compName, "BHJZCount"));
                    if (iBHJZCount > 0) {
                        doControlJob(compName, context.getResources().getString(R.string.auto_calibration) + "_" + context.getResources().getString(R.string.BYHC));
                        updateConfigData(compName, "BHJZCount", String.valueOf(--iBHJZCount));
                        saveRunInfo2File("组分[" + compName + "] 次数" + String.valueOf(iBHJZCount) + "_启动校准标核");
                        AddError(compName, 670, 运维_信息);//自动校准标样核查
                    } else {
                        updateConfigData(compName, "BHJZCount", "");
                    }
                    updateConfigData(compName, "BHJZFlag", "false");
                } else {
                    saveRunInfo2File("组分[" + compName + "]  " + "报错自动流程停止，则校准标核不启动");
                    updateConfigData(compName, "BHJZCount", "");
                    updateConfigData(compName, "BHJZFlag", "false");
                }
            }
        }
    }
}
