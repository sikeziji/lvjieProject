package com.yzlm.cyl.cfragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStreamReader;

import zwj.hz.zj.zwj.UpdateManager;

/*
 * Created by zwj on 2018/5/25.
 */

class UpdateAPKManager2 extends UpdateManager {
    private static Context mContext;
    private static String filePath;
    private static String fileName;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message paramMessage) {
            switch (paramMessage.what) {
                case 1:
                    UpdateAPKManager2.installSlient();
                default:
            }
        }
    };

    public UpdateAPKManager2(Context context) {
        super(context);
    }

    public void setFilePathName(String path, String filename) {
        filePath = path;
        fileName = filename;
    }

    private static void installAPK() {
        File localFile = new File(filePath + fileName);
        Intent localIntent = new Intent("android.intent.action.VIEW");
        localIntent.setDataAndType(Uri.fromFile(localFile), "application/vnd.android.package-archive");
        mContext.startActivity(localIntent);
    }

    public void checkUpdateInfo() {
        Message localMessage = new Message();
        localMessage.what = 1;
        this.mHandler.sendMessage(localMessage);
    }


    /*
     * 静默安装
     */
    private static void installSlient() {
        String cmd = "pm install -r " + filePath + fileName;
        Process process = null;
        DataOutputStream os = null;
        BufferedReader successResult = null;
        BufferedReader errorResult = null;
        StringBuilder successMsg = null;
        StringBuilder errorMsg = null;
        try {
            //静默安装需要root权限
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.write(cmd.getBytes());
            os.writeBytes("\n");
            os.writeBytes("exit\n");
            os.flush();
            //执行命令
            process.waitFor();
            //获取返回结果
            successMsg = new StringBuilder();
            errorMsg = new StringBuilder();
            successResult = new BufferedReader(new InputStreamReader(process.getInputStream()));
            errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String s;
            while ((s = successResult.readLine()) != null) {
                successMsg.append(s);
            }
            while ((s = errorResult.readLine()) != null) {
                errorMsg.append(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (process != null) {
                    process.destroy();
                }
                if (successResult != null) {
                    successResult.close();
                }
                if (errorResult != null) {
                    errorResult.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
