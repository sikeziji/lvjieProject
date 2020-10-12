package com.yzlm.cyl.cfragment.Thread;

import android.util.Log;

import com.yzlm.cyl.cfragment.AppFunction.OpenDoorCheck;
import com.yzlm.cyl.clibrary.Thread.CThread;

import static com.yzlm.cyl.cfragment.AppFunction.WatchDog.feedWatchDogBoard;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.getPublicConfigData;

/**
 * *******************************************
 * 项目名称：water
 *
 * @Author zwj
 * 创建时间：2019/6/21   9:23
 * 用途   认证开门检查，喂狗，
 * *******************************************
 **/
public class AuthFuncThread extends CThread {

    private static int watchDogTimerCount = 0;

    public AuthFuncThread(String name, boolean suspend) {
        super(name, suspend);
        SUSPEND_TIME_MILLISECONDS = 1000;
    }

    @Override
    public void process() {

        try {
            /*主控板开门检测----*/
            new OpenDoorCheck();
            /*复位板喂狗*/
            if (getPublicConfigData("watchDogSwitch").equals("open")) {
                if (watchDogTimerCount++ > 35) {
                    feedWatchDogBoard();
                    watchDogTimerCount = 0;
                }
            }
        } catch (Exception e) {
            Log.i("except", e.toString());
        }
    }

    @Override
    public void onDestory() {

    }
}
