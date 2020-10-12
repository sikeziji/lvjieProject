package com.yzlm.cyl.cfragment.AppFunction;

import com.yzlm.cyl.cfragment.R;

import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.updateConfigData;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.doFlowing;
import static com.yzlm.cyl.cfragment.Global.saveRunInfo2File;

import static java.lang.Thread.sleep;

class CheckOneKeyCalStartStatus {

    public CheckOneKeyCalStartStatus(final String compName) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    try {
                        sleep(1000);

                        if (!doFlowing.get(compName).equals(context.getResources().getString(R.string.BY2CL))) {
                            updateConfigData(compName, "OneKeyCalStartFlag", "true");
                            updateConfigData(compName, "OneKeyCalForceXFlag", "true");
                            saveRunInfo2File("组分[" + compName + "]" + "CheckOneKeyCalStartStatus" + "启动一次清除状态");
                        }
                    } catch (Exception E) {
                        saveRunInfo2File("组分[" + compName + "]" + "CheckOneKeyCalStartStatus" + E.toString());
                    }
                }
            }
        }).start();


    }

}
