package com.yzlm.cyl.cfragment.AppFunction;

import com.yzlm.cyl.cfragment.R;

import static com.yzlm.cyl.cfragment.Content.Module.ModuleDistinction.QueryMeasCateg;
import static com.yzlm.cyl.cfragment.Global.NoticeW300FlowStart;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.doFlowing;
import static com.yzlm.cyl.cfragment.Global.oldDoFlowing;

public class AutoJudgeDoFlowing {
    public AutoJudgeDoFlowing(String compName) {
        if (!oldDoFlowing.get(compName).equals(doFlowing.get(compName))) {
            if (QueryMeasCateg(compName).equals("1") && compName.equals("CODcr")) {
                if (doFlowing.get(compName).equals(context.getString(R.string.waiting_for_instructions))) {
                    //通知W300板子仪表关闭测量
                    NoticeW300FlowStart(compName, oldDoFlowing.get(compName), false);
                } else {
                    //通知W300板子仪表启动测量
                    NoticeW300FlowStart(compName, doFlowing.get(compName), true);
                }
            }
        }
        oldDoFlowing.put(compName, doFlowing.get(compName));
    }
}
