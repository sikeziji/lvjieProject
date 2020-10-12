package com.yzlm.cyl.cfragment.Thread;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.clibrary.Timer.Timer_Tick;

import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;

import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Frame.MainActivity.sendSysTimeToDev;
import static com.yzlm.cyl.cfragment.Global.AutoSampleEnable;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.doControlJob;
import static com.yzlm.cyl.cfragment.Global.doFlowing;
import static com.yzlm.cyl.cfragment.Global.getTimer_cycle;
import static com.yzlm.cyl.cfragment.Global.getTimer_topHour;
import static com.yzlm.cyl.cfragment.Global.saveRunInfo2File;
import static java.lang.Thread.sleep;

/**
 * Created by caoyiliang on 2017/2/25.
 */

public class AutoDoSample_topHour {
    public AutoDoSample_topHour(Activity activity, boolean suspend) {
        Timer_Tick tt = new Timer_Tick(activity, suspend, true);
        tt.setTimeEvent(new ttShow());
    }

    private class ttShow implements Timer_Tick.ICallBack {
        @Override
        public void timerEvent(Context context, Intent intent, int hour) {

            Map<String, String[]> timer = getTimer_topHour();
            Iterator iter = timer.entrySet().iterator();

            while (iter.hasNext()) {

                Map.Entry entry = (Map.Entry) iter.next();
                String[] Hs = (String[]) entry.getValue();

                if (getConfigData(entry.getKey().toString(), "zhcsSwitch").equals("true")) {
                    /*开启组合测试的情况*/
                    saveRunInfo2File("开启组合测试,该整点不启动周期测量");

                } else if (AutoSampleEnable.get(entry.getKey().toString()).equals("true")
                        && getConfigData(entry.getKey().toString(), "isAuto").equals("true")
                        && getConfigData(entry.getKey().toString(), "ZQCL").equals("true")) {
                    for (String item : Hs) {
                        AutoDoSample_cycle ads = getTimer_cycle(entry.getKey().toString());
                        if (ads != null) {
                            int cycleHour = ads.SUSPEND_TIME_MILLISECONDS[2];
                            Calendar calibTime = ads.getCalibDate();
                            if (calibTime != null) {
                                Calendar nextDate = (Calendar) calibTime.clone();
                                nextDate.add(Calendar.DATE, ads.SUSPEND_TIME_MILLISECONDS[1]);
                                int nextDateYear = nextDate.get(Calendar.YEAR);
                                int nextDateMonth = (nextDate.get(Calendar.MONTH)) + 1;
                                int nextDateDay = nextDate.get(Calendar.DAY_OF_MONTH);

                                if (!(((!ads.getSuspendFlag()[1] && (nextDateYear == Calendar.getInstance().get(Calendar.YEAR) && nextDateMonth == (Calendar.getInstance().get(Calendar.MONTH) + 1)
                                        && nextDateDay == Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                                        && cycleHour == hour)) || (!ads.getSuspendFlag()[1] && (((ads.SUSPEND_TIME_MILLISECONDS[1] == 0) && ads.SUSPEND_TIME_MILLISECONDS[2] == (Calendar.getInstance().get(Calendar.HOUR_OF_DAY))))))
                                        || (!ads.getSuspendFlag()[2] && Integer.parseInt(getConfigData(entry.getKey().toString(), "zdqxTimes")) >= ads.SUSPEND_TIME_MILLISECONDS[3]))) {
                                    if (item.equals(String.valueOf(hour))) {

                                        sendSysTimeToDev(entry.getKey().toString());
                                        try {
                                            sleep(200);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        if (doFlowing.get(entry.getKey().toString()).equals(context.getString(R.string.waiting_for_instructions))) {

                                            doControlJob(entry.getKey().toString(), getHourDoFlow(entry.getKey().toString(), hour));
                                        }
                                    }
                                }
                            } else {
                                if (item.equals(String.valueOf(hour))) {
                                    sendSysTimeToDev(entry.getKey().toString());
                                    try {
                                        sleep(200);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    if (doFlowing.get(entry.getKey().toString()).equals(context.getString(R.string.waiting_for_instructions))) {
                                        doControlJob(entry.getKey().toString(), getHourDoFlow(entry.getKey().toString(), hour));
                                    }
                                }
                            }
                        } else {
                            if (item.equals(String.valueOf(hour))) {
                                sendSysTimeToDev(entry.getKey().toString());
                                try {
                                    sleep(200);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                if (doFlowing.get(entry.getKey().toString()).equals(context.getString(R.string.waiting_for_instructions))) {
                                    doControlJob(entry.getKey().toString(), getHourDoFlow(entry.getKey().toString(), hour));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 周期测量执行的流程内容
     *
     * @param compName compName 组分
     * @param hour     hour 0 - 23
     * @return
     */
    public static String getHourDoFlow(String compName, int hour) {
        String flowName;

        String[] sFlow = context.getResources().getStringArray(R.array.strDDCL);
        String strSetHourFlow = getConfigData(compName, "zqclFlow");


        if (strSetHourFlow.equals("")) {
            flowName = sFlow[0];
        } else {
            String[] sHourFlow = strSetHourFlow.split("，");
            flowName = sFlow[Integer.parseInt(sHourFlow[hour])];
        }
        if (flowName.contains(context.getString(R.string.auto))) {
            return flowName;
        } else {
            flowName = context.getString(R.string.auto) + flowName;
        }

        return flowName;
    }
}
