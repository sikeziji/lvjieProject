package com.yzlm.cyl.cfragment.Server;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.yzlm.cyl.cfragment.Frame.MainActivity;

import static com.yzlm.cyl.cfragment.AppFunction.DeleteSDApkPackage.removeFile;


/*
 * Created by caoyiliang on 2017/1/3.
 */

public class UpdateReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("android.intent.action.PACKAGE_REPLACED")) {
            //  Toast.makeText(context, "升级了一个安装包，重新启动此程序", Toast.LENGTH_SHORT).show();

            Intent intent2 = new Intent(context, MainActivity.class);
            intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent2);
            Log.i("安装apk", "升级了一个安装包");

        }
        //接收安装广播
        if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {
            String packageName = intent.getDataString();
            System.out.println("安装了:" + packageName + "包名的程序");

            Log.i("安装apk", "安装了:" + packageName + "包名的程序");

           /* Intent uninstall_intent = new Intent(Intent.ACTION_DELETE);
            uninstall_intent.setData(Uri.parse("package:包名"));
            uninstall_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(uninstall_intent);*/

            if (removeFile("/sdcard/app-debug.apk")) {
                Log.i("安装apk", "卸载了:" + packageName + "包名的程序");
            }
        }
        //接收卸载广播
        if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {
            String packageName = intent.getDataString();

            System.out.println("卸载了:" + packageName + "包名的程序");
            Log.i("安装apk", "卸载了:" + packageName + "包名的程序");


           /* Intent uninstall_intent = new Intent(Intent.ACTION_DELETE);
            uninstall_intent.setData(Uri.parse("package:包名"));
            uninstall_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(uninstall_intent);*/


           /* if (removeFile("/sdcard/app-debug.apk")) {
                Log.i("安装apk", "卸载了:" + packageName + "包名的程序");
            }*/


        }
    }
}
