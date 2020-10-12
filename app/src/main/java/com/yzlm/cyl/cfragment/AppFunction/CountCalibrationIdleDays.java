package com.yzlm.cyl.cfragment.AppFunction;

import android.util.Log;

import java.util.Calendar;

import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.updateConfigData;

/**
 * 校准天数统计
 * Created by zwj on 2017/6/9.
 */

public class CountCalibrationIdleDays {
    //定时计数器，10秒钟查询一次校准提示开关
    private static int timeCount = 0;

    private static boolean mutexFlag = false;

    public CountCalibrationIdleDays(String compName) {
        //10秒钟查询一次
        if (++timeCount >= 10) {
            timeCount = 0;
            try {
                //查询该组分的校准提示功能是否打开
                if (getConfigData(compName , "CALIBRATION_HIT").equals("true")) {
                    Calendar c = Calendar.getInstance();
                    int hour = c.get(Calendar.HOUR_OF_DAY);
                    int minute = c.get(Calendar.MINUTE);

                    if (hour == 23 && minute == 59) {
                        if (!mutexFlag) {

                            int days = Integer.valueOf(getConfigData(compName , "CALIBRATION_INTERVAL_DAYS"));
                            int CALIBRATION_IDLE_MAX_DAYS = 10;
                            if (++days >= CALIBRATION_IDLE_MAX_DAYS) {
                                days = CALIBRATION_IDLE_MAX_DAYS;
                            }
                            updateConfigData(compName, "CALIBRATION_INTERVAL_DAYS", String.valueOf(days));

                            mutexFlag = true;
                        }
                    } else {
                        mutexFlag = false;
                    }
                }
            } catch (Exception e) {
                Log.e("CountCalIdleDays", e.toString());
            }
        }
    }

    public static void ClearCalibrationCountDays(String component) {

        if (getConfigData(component, "CALIBRATION_HIT").equals("true")) {
            updateConfigData(component, "CALIBRATION_INTERVAL_DAYS", "0");
        }
    }
}
