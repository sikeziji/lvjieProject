package com.yzlm.cyl.cfragment.AppFunction;

import com.yzlm.cyl.cfragment.Communication.Thread.SendManager;
import com.yzlm.cyl.cfragment.R;

import static com.yzlm.cyl.cfragment.Global.S1;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.doFlowing;
import static com.yzlm.cyl.cfragment.Global.getCmds;
import static com.yzlm.cyl.cfragment.Global.workState;
import static java.lang.Thread.sleep;

public class GetRunFlowsInfoRunnable implements Runnable {
    private String component;
    //是否下发启动开采样
    private boolean bSampling = false;
    private int timeout = 0;

    public GetRunFlowsInfoRunnable(String component) {
        this.component = component;
    }

    @Override
    public void run() {
        while (true) {
            try {
                sleep(1000);
                if (!doFlowing.get(component).equals(context.getResources().getString(R.string.waiting_for_instructions))) {
                    timeout = 0;
                    int iAction = 0;
                    try {
                        iAction = Integer.parseInt(getCmds(component).getCmd(52).getValue().toString());
                    } catch (Exception e) {
                        iAction = 0;
                    }
                    if (iAction == 568) {
                        //开采样
                        if (!bSampling) {
                            bSampling = true;
                            SendManager.SendCmd("IO" + "_" + "接口板开采样_06_02", S1, 3, 500, new byte[]{0x00, 0x01, 0x00, 0x00});
                        }
                    } else if (iAction == 569) {
                        //关采样
                        SendManager.SendCmd("IO" + "_" + "接口板停止_06_02", S1, 3, 500, new byte[]{0x00, 0x00, 0x00, 0x00});
                        break;
                    }
                    //工作状态不等于正常，关闭并推出
                    if (!workState.get(component).equals(context.getString(R.string.normal))) {
                        if (bSampling) {
                            //关采样
                            SendManager.SendCmd("IO" + "_" + "接口板停止_06_02", S1, 3, 500, new byte[]{0x00, 0x00, 0x00, 0x00});
                        }
                        break;
                    }
                } else {
                    timeout++;
                    if (timeout > 5) {
                        //关采样
                        SendManager.SendCmd("IO" + "_" + "接口板停止_06_02", S1, 3, 500, new byte[]{0x00, 0x00, 0x00, 0x00});
                        break;
                    }
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
