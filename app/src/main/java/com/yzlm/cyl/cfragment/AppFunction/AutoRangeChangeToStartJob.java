package com.yzlm.cyl.cfragment.AppFunction;

import com.yzlm.cyl.cfragment.R;

import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.updateConfigData;
import static com.yzlm.cyl.cfragment.Global.allAlarmInfo;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.doControlJob;
import static com.yzlm.cyl.cfragment.Global.doFlowing;

/**
 * Created by zwj on 2017/6/15.
 */

public class AutoRangeChangeToStartJob {
    public AutoRangeChangeToStartJob(String compName) {

        if (getConfigData(compName, "RANGE_AUTO_CHANGE_START_FLAG").equals("true")
                && getConfigData(compName, "AUTORANGE").equals("true")) {
            // "等待指令"
            if (doFlowing.get(compName).equals(context.getString(R.string.waiting_for_instructions))) {

                //启动量程切换了，暂存一次切换记录
                String strTempAlarmInfo = allAlarmInfo.get(compName);
                // 获取量程切换记录的切换流程
                String flowName = getConfigData(compName, "RANGE_AUTO_CHANGE_FLOW");
                if (flowName.equals("")) {
                    flowName = context.getString(R.string.autoJob);
                }
                if (flowName.contains(context.getString(R.string.ZY))) {
                    flowName = context.getString(R.string.autoJob);
                } else {
                    flowName = context.getString(R.string.JBHS);
                }
                doControlJob(compName, flowName);
                updateConfigData(compName, "RANGE_AUTO_CHANGE_START_FLAG", "false");
                //启动量程切换再存储
                allAlarmInfo.put(compName, strTempAlarmInfo);
            }
        }
    }
}
