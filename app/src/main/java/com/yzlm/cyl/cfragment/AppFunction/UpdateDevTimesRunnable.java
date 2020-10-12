package com.yzlm.cyl.cfragment.AppFunction;


import com.yzlm.cyl.cfragment.Communication.Thread.SendManager;
import com.yzlm.cyl.cfragment.Global;

import java.util.Calendar;

import static com.yzlm.cyl.cfragment.Global.strComponent;
import static com.yzlm.cyl.clibrary.Util.DataUtil.copybyte;
import static com.yzlm.cyl.clibrary.Util.DataUtil.toByteArray;
import static java.lang.Thread.sleep;

/**
 * Created by zwj on 2017/8/1.
 */

public class UpdateDevTimesRunnable implements Runnable {

    private String compName = null;

    public UpdateDevTimesRunnable(String mCompName) {
        this.compName = mCompName;
    }

    @Override
    public void run() {

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);
        byte[] timeByte = copybyte(toByteArray(year, 4), toByteArray(month, 4),
                toByteArray(day, 4), toByteArray(hour, 4),
                toByteArray(minute, 4), toByteArray(second, 4));

        if (compName == null) {
            if (strComponent.get(1).length > 0) {
                for (String item : strComponent.get(1)) {
                    SendManager.SendCmd(item + "_时间管理_06_0", Global.S0, 1, 200, timeByte);
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            SendManager.SendCmd(compName + "_时间管理_06_0", Global.S0, 1, 200, timeByte);
            try {
                sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
