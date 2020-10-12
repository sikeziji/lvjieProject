package com.yzlm.cyl.cfragment.AppFunction;

import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.getPublicConfigData;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.RelayStatus;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;

/**
 * Created by zwj on 2017/6/2.
 */

public class AlarmLimit {

    public AlarmLimit(String compName, double data, double dAlarmL, double dAlarmH) {
        try {

            if (getPublicConfigData("SYS_RELAYCONF").equals("2")) {

                if (data < dAlarmL || data > dAlarmH) {
                    RelayStatus |= 1;
                } else {
                    RelayStatus &= 0xfffffffe;
                }
                Thread AlarmLimitUpdate = new Thread(new AlarmLimitRunnable(compName, RelayStatus));
                AlarmLimitUpdate.start();
            }
        } catch (Exception e) {
            saveExceptInfo2File("组分[" + compName + "] 测量完成后继电器报警上下限异常");
        }
    }
}
