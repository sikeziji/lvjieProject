package com.yzlm.cyl.cfragment.Cal.Component;

import com.yzlm.cyl.cfragment.Config.Component.CfgTool.History;
import com.yzlm.cyl.cfragment.R;

import java.util.List;
import java.util.Map;

import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Kbf.getNewestKBF;
import static com.yzlm.cyl.cfragment.Global.context;

public class CalcReagentVolume {
    public static double CalReagentVolume(String compName) {
        History history = new History(context);
        String[] strFlows = new String[]{context.getString(R.string.ZY), context.getString(R.string.BYCL), context.getString(R.string.BY2CL), context.getString(R.string.LYCL)
                , context.getString(R.string.B1), context.getString(R.string.B2), context.getString(R.string.JZ), context.getString(R.string.BYHC)
                , context.getString(R.string.LDHC), context.getString(R.string.KDHC), context.getString(R.string.PXY), context.getString(R.string.JBHS)};
        List<Map> hisMap = history.selectAvg(compName, strFlows, 0, 20);
        double dVal = 0;
        if (hisMap.size() > 0 && hisMap.get(0).get("cAvg") != null) {
            dVal = (Double.parseDouble(hisMap.get(0).get("cAvg").toString()) * Double.parseDouble(getConfigData(compName, "fyzs_k1"))
                    + (Double.parseDouble(getConfigData(compName, "fyzs_b")) - getNewestKBF(compName, getConfigData(compName, "RANGE"), "B")) * Double.parseDouble(getConfigData(compName, "fyzs_k2"))
                    + Double.parseDouble(getConfigData(compName, "fyzs_b1")));
            if (dVal > 10) {
                dVal = 10;
            }
        }
        return dVal;
    }
}
