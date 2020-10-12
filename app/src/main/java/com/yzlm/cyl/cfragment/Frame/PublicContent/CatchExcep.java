package com.yzlm.cyl.cfragment.Frame.PublicContent;

import android.app.Activity;
import android.app.Application;
import android.os.Message;

import java.util.ArrayList;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

import com.yzlm.cyl.cfragment.Frame.MainActivity;
import com.yzlm.cyl.cfragment.Server.LocalService;

/**
 * Created by WL on 2017/4/17.
 */

public class CatchExcep extends Application {

    private ArrayList<Activity> list = new ArrayList<>();
    private static MainActivity mainActivity = null;

    public void init() {
        //设置该CrashHandler为程序的默认处理器
        UnCeHandler catchExcep = new UnCeHandler(this);
        Thread.setDefaultUncaughtExceptionHandler(catchExcep);
    }

    /**
     * Activity关闭时，删除Activity列表中的Activity对象
     */
    public void removeActivity(Activity a) {
        list.remove(a);
    }

    /**
     * 向Activity列表中添加Activity对象
     */
    public void addActivity(Activity a) {
        list.add(a);
    }

    /**
     * 关闭Activity列表中的所有Activity
     */
    public void finishActivity() {
        for (Activity activity : list) {
            if (null != activity) {
                activity.finish();
            }
        }
        //杀死该应用进程
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    /*双进程守护***/

    public static MainActivity getMainActivity() {
        return mainActivity;
    }

    public static void setMainActivity(MainActivity activity) {
        mainActivity = activity;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (isMainProcess(getApplicationContext())) {
            startService(new Intent(this, LocalService.class));
        } else {
            return;
        }
    }

    /**
     * 获取当前进程名
     */
    public String getCurrentProcessName(Context context) {
        int pid = android.os.Process.myPid();
        String processName = "";
        ActivityManager manager = (ActivityManager) context.getApplicationContext().getSystemService
                (Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo process : manager.getRunningAppProcesses()) {
            if (process.pid == pid) {
                processName = process.processName;
            }
        }
        return processName;
    }

    public boolean isMainProcess(Context context) {
        /**
         * 是否为主进程
         */
        boolean isMainProcess;
        isMainProcess = context.getApplicationContext().getPackageName().equals
                (getCurrentProcessName(context));
        return isMainProcess;
    }

    /**
     * 程序终止的时候执行
     * 真机不执行，模拟器执行
     */
    @Override
    public void onTerminate() {
        //Log.e("APP", "onTerminate");
        super.onTerminate();
    }

    /**
     * 低内存的时候执行
     */
    @Override
    public void onLowMemory() {
        //Log.e("APP", "onLowMemory");
        super.onLowMemory();

        /*if (list.size() > 0) {
            Message msg = new Message();
            msg.what = 110;
            WinWidgetHandler.sendMessage(msg);
        }*/
    }

    /**
     * 程序在内存清理的时候执行
     * 界面不可见，在后台时会清理
     *
     * @param level
     */
    @Override
    public void onTrimMemory(int level) {
        //Log.e("APP", "onTrimMemory");
        super.onTrimMemory(level);
        /*if (list.size() > 0 && level >= TRIM_MEMORY_COMPLETE) {
            Message msg = new Message();
            msg.what = 110;
            WinWidgetHandler.sendMessage(msg);
        }*/
    }
}
