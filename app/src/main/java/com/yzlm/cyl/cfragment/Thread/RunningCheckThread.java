package com.yzlm.cyl.cfragment.Thread;

import android.util.Log;

import com.yzlm.cyl.cfragment.AppFunction.AutoDeleteFiles;
import com.yzlm.cyl.cfragment.AppFunction.AutoJudgeDoFlowing;
import com.yzlm.cyl.cfragment.AppFunction.AutoRangeChangeToStartJob;
import com.yzlm.cyl.cfragment.AppFunction.AutoStandardJudgment;
import com.yzlm.cyl.cfragment.AppFunction.BackUpFileData;
import com.yzlm.cyl.cfragment.AppFunction.CountCalibrationIdleDays;
import com.yzlm.cyl.cfragment.AppFunction.GetDevProtectInfo;
import com.yzlm.cyl.clibrary.Thread.CThread;

import java.util.Calendar;

import static com.yzlm.cyl.cfragment.Frame.MainActivity.sendSysTimeToDev;
import static com.yzlm.cyl.cfragment.Global.strComponent;

/*
 * Created by zwj on 2017/6/9.
 */

public class RunningCheckThread extends CThread {

    private static int timerCountSec = 0;
    private static int updateDevTimerCount = 0;


    public RunningCheckThread(String name, boolean suspend) {
        super(name, suspend);
        SUSPEND_TIME_MILLISECONDS = 1000;
    }

    @Override
    public void stop() {
        super.stop();
    }

    @Override
    public void process() {

        try {
            for (String item : strComponent.get(1)) {
                /*校准间隔时间统计*/
                new CountCalibrationIdleDays(item);
                /*量程切换后启动做样*/
                new AutoRangeChangeToStartJob(item);
                /*标核判定*/
                new AutoStandardJudgment(item);
                /*通知W300板子仪表启动测量*/
                new AutoJudgeDoFlowing(item);
            }
            Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int min = c.get(Calendar.MINUTE);

            /*更新一次测控板时间*/
            if (updateDevTimerCount++ > 3600) {
                updateDevTimerCount = 0;
                sendSysTimeToDev(null);
            }
            /*延保时间检查**/
            if (updateDevTimerCount % 600 == 0) {
                devProtectCheck();
            }
            /*后台日志清除*/
            if (hour == 0 && min == 1) {
                new AutoDeleteFiles();
            }

            /*执行数据备份----*/
            if (timerCountSec % 30 == 0) {
                if (hour == 0 && min == 5) {
                    new Thread(new BackUpFileData()).start();
                }
            }
            if (timerCountSec++ >= 60) {
                timerCountSec = 0;
            }


        } catch (Exception e) {
            Log.i("except", e.toString());
        }
    }


    @Override
    public void onDestory() {

    }


    private void devProtectCheck() {

        /*获取延保信息*/
        new GetDevProtectInfo();
    }

}
