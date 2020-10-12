package com.yzlm.cyl.cfragment.Thread;

import android.util.Log;

import com.yzlm.cyl.cfragment.R;
import com.yzlm.cyl.cfragment.ThreadHelper.WThread;

import java.util.Calendar;

import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.getConfigData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config.updateConfigData;
import static com.yzlm.cyl.cfragment.Content.Component.List2.List2_Content3.setUseRange;
import static com.yzlm.cyl.cfragment.Global.AutoSampleEnable;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.doControlJob;
import static com.yzlm.cyl.cfragment.Global.doFlowing;

/**
 * Created by caoyiliang on 2017/2/25.
 */

public class AutoDoSample_cycle extends WThread {
    private String component;
    /*时间计数器，用于计时间隔时间*/
    private int timeCount = 0;
    /*时间计数器，用于计时间隔时间*/
    private int qxTimeCount = 0;

    public AutoDoSample_cycle(String component, String name, boolean[] suspend) {
        super(name, suspend);
        this.component = component;
    }

    public void setTime(int SUSPEND_TIME_MILLISECONDS, int index) {
        this.SUSPEND_TIME_MILLISECONDS[index] = SUSPEND_TIME_MILLISECONDS;
    }

    public int getTime(int index) {
        return SUSPEND_TIME_MILLISECONDS[index];
    }

    public void setCalibDate(Calendar calibDate) {
        this.calibDate = calibDate;
    }

    public Calendar getCalibDate() {
        return this.calibDate;
    }

    @Override
    public void process() {
        try {
            try {
                timeCount = Integer.parseInt(getConfigData(component, "zdzyTimer"));
                qxTimeCount = Integer.parseInt(getConfigData(component, "zdqxTimer"));
            } catch (Exception e) {
                Log.i("AutoDoSample_cycle", String.valueOf(timeCount));
            }
            if (getConfigData(component, "zhcsSwitch").equals("true")) {
                /*开启组合测试的情况*/
                Log.i("组合测试", "true");
                timeCount = 0;
            } else if (getConfigData(component, "autoCalFlowFlag").equals("true")) {
                /*W300反控自动校准的情况*/
                Log.i("W300反控自动校准", "true");

                if (doFlowing.get(component).equals(context.getString(R.string.waiting_for_instructions))) {
                    doControlJob(component, context.getString(R.string.auto_calibration));
                    updateConfigData(component, "autoCalFlowFlag", "false");
                }
                timeCount = 0;
            } else if (AutoSampleEnable.get(component).equals("true")) {
                /*由自动校准流程功能去判断自动清洗功能和自动做样功能*/
                autoCalibProcedure();
            } else {
                timeCount = 0;
            }
            saveZdzyTimer();
        } catch (Exception e) {

        }

    }

    /* 自动校准的流程*/
    private void autoCalibProcedure() {
        if (!this.getSuspendFlag()[1]) {
            if (this.calibDate != null) {
                Calendar nextDate = (Calendar) calibDate.clone();
                nextDate.add(Calendar.DATE, SUSPEND_TIME_MILLISECONDS[1]);
                int nextDateYear = nextDate.get(Calendar.YEAR);
                int nextDateMonth = (nextDate.get(Calendar.MONTH)) + 1;
                int nextDateDay = nextDate.get(Calendar.DAY_OF_MONTH);


                if ((((SUSPEND_TIME_MILLISECONDS[1] == 0)//当天
                        || ((nextDateYear == Calendar.getInstance().get(Calendar.YEAR)
                        && nextDateMonth == (Calendar.getInstance().get(Calendar.MONTH) + 1))
                        && (nextDateDay == Calendar.getInstance().get(Calendar.DAY_OF_MONTH))))// 几天后的日期天
                        && (SUSPEND_TIME_MILLISECONDS[2] == (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)))//时间点 整点
                        && (Calendar.getInstance().get(Calendar.MINUTE) < 1)//时间 分钟点
                        && (Calendar.getInstance().get(Calendar.SECOND) < 10))//时间 秒
                        && (getConfigData(component, "ZDJZ").equals("true"))) {

                    if (doFlowing.get(component).equals(context.getString(R.string.waiting_for_instructions))) {
                        //自动校准量程为量程1并且当前量程不为量程1 (为主量程或者当前量程为量程1则无需动作)
                        if (!getConfigData(component, "jzRange").equals("1") && !getConfigData(component, "Range").equals("1")) {
                            //先将当前量程切换量程1，然后将校准后量程切换至原来量程。这样等量程1校准启动后，切换回原来量程
                            String strRange = getConfigData(component, "RANGE");
                            setUseRange(component, "1");
                            updateConfigData(component, "CalSelectRange", strRange);
                        }
                        doControlJob(component, context.getString(R.string.auto_calibration));
                        calibDate = Calendar.getInstance();
                    }
                    timeCount = 0;
                } else if (getConfigData(component, "autoWashDoOnceFlag").equals("true")) {
                    // 高指 新增测量值大于设定值执行一次冲洗
                    autoFlushingProcedure();
                } else if (nextDateYear == Calendar.getInstance().get(Calendar.YEAR) && nextDateMonth == (Calendar.getInstance().get(Calendar.MONTH) + 1)
                        && nextDateDay == Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                        && (SUSPEND_TIME_MILLISECONDS[2] + 1) == (Calendar.getInstance().get(Calendar.HOUR_OF_DAY))) {
                    autoWashProcedure();
                } else {
                    autoWashProcedure();
                }
            } else if (getConfigData(component, "autoWashDoOnceFlag").equals("true")) {
                // 高指 新增测量值大于设定值执行一次冲洗
                autoFlushingProcedure();
            } else {
                autoWashProcedure();
            }
        } else if (getConfigData(component, "autoWashDoOnceFlag").equals("true")) {
            // 高指 新增测量值大于设定值执行一次冲洗
            autoFlushingProcedure();
        } else {
            autoWashProcedure();
        }
    }

    private void autoFlushingProcedure() {
        // 高指 新增测量值大于设定值执行一次冲洗
        if (doFlowing.get(component).equals(context.getString(R.string.waiting_for_instructions))) {
            doControlJob(component, context.getString(R.string.auto) + context.getString(R.string.GLCX));
            updateConfigData(component, "autoWashDoOnceFlag", "false");
        }
        timeCount = 0;
    }

    /*自动清洗的流程*/
    private void autoWashProcedure() {
        if (!this.getSuspendFlag()[2]) {

            if ((getConfigData(component, "ZDQX").equals("true")) &&
                    Integer.parseInt(getConfigData(component, "zdqxTimes")) >= SUSPEND_TIME_MILLISECONDS[3]) {
                if (doFlowing.get(component).equals(context.getString(R.string.waiting_for_instructions))) {
                    if (qxTimeCount > 30) {
                        if (doControlJob(component, context.getString(R.string.auto) + context.getString(R.string.YBQX))) {
                            updateConfigData(component, "zdqxTimes", "0");
                            qxTimeCount = 0;
                        }
                    }
                    updateConfigData(component, "zdqxTimer", String.valueOf(++qxTimeCount));
                } else {
                    qxTimeCount = 0;
                    updateConfigData(component, "zdqxTimer", "0");
                }
                timeCount = 0;
            } else {
                autoDoSampleProcedure();
            }
        } else {
            autoDoSampleProcedure();
        }
    }

    /* 自动做样的流程*/
    private void autoDoSampleProcedure() {

        if (!this.getSuspendFlag()[0]) {
            if (getConfigData(component, "LXCL").equals("true")
                    && getConfigData(component, "isAuto").equals("true")) {
                if (doFlowing.get(component).equals(context.getString(R.string.waiting_for_instructions))) {
                    if (timeCount >= SUSPEND_TIME_MILLISECONDS[0] * 60) {

                        doControlJob(component, context.getString(R.string.autoJob));
                        timeCount = 0;
                    }
                } else {
                    timeCount = 0;
                }
            }
        } else {
            timeCount = 0;
        }
    }


    @Override
    public void onDestory() {

    }

    private void saveZdzyTimer() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                updateConfigData(component, "zdzyTimer", String.valueOf(++timeCount));

            }
        });
        thread.start();
    }
}
