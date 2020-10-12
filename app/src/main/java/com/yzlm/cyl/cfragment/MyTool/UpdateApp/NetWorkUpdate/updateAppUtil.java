package com.yzlm.cyl.cfragment.MyTool.UpdateApp.NetWorkUpdate;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Message;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStreamReader;

import static com.yzlm.cyl.cfragment.Global.SdcardPath;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.MyTool.UpdateApp.NetWorkUpdate.downloadAppUtils.getFileFromServer;
import static com.yzlm.cyl.cfragment.MyTool.UpdateApp.NetWorkUpdate.downLoadFromUrl.downLoadTxtFromUrl;
import static com.yzlm.cyl.clibrary.Util.FileUtil.readString;
import static com.yzlm.cyl.clibrary.Util.SDCardUtils.getStoragePath;

/*
 * Created by zwj on 2018/8/1.
 */

class updateAppUtil {


    /*
     * 获取当前apk的版本号 currentVersionCode
     * @param ctx
     * @return
     */
    private static String getAPPLocalVersion(Context ctx) {
        try {
            // 获取packagemanager的实例
            PackageManager packageManager = ctx.getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(ctx.getPackageName(), 0);
            return packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }

    }


    /*
     * 下载文件---返回下载后的文件存储路径
     *
     * @param url 文件地址
     * @param dir 存储目录
     * @param fileName 存储文件名
     * @return
     */
    public static void downloadHttpUrl(String url, String dir, String fileName) {
        try {
            downLoadTxtFromUrl(url, fileName, dir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*
     * 获取服务器上版本信息
     * @param context
     * @param callBack
     */
    public static updateAppInfo getAPPServerVersion(Context context, String url) {

        downloadHttpUrl(url, "/sdcard/Csoft/", "readme.txt");


        String actionDir;
        SdcardPath = (getStoragePath(context, false).size() == 0 ? null : (getStoragePath(context, false).get(0) + File.separator));
        actionDir = SdcardPath;
        updateAppInfo appInfo = new updateAppInfo();
        actionDir += "Csoft" + File.separator;
        String path = actionDir + "readme.txt";
        File dbFile = new File(path);
        if (dbFile.exists()) {
            String strFileContent = readString(path, "GBK");
            if (strFileContent.contains("appName")) {
                String[] appName = strFileContent.substring(strFileContent.indexOf("appName")).split("[;；]");
                String strAppName = appName[0].split("=")[1].substring(0, 1).equals(" ") ? appName[0].split("= ")[1] : appName[0].split("=")[1];
                appInfo.setAppName(strAppName);
            }
            if (strFileContent.contains("versionName")) {
                String[] versionNames = strFileContent.substring(strFileContent.indexOf("versionName")).split("[;；]");
                String strVersionNames = versionNames[0].split("=")[1].substring(0, 1).equals(" ") ? versionNames[0].split("= ")[1] : versionNames[0].split("=")[1];
                appInfo.setVersionName(strVersionNames);
            }
            if (strFileContent.contains("versionCode")) {
                String[] versionCode = strFileContent.substring(strFileContent.indexOf("versionCode")).split("[;；]");
                String strVersionCode = versionCode[0].split("=")[1].substring(0, 1).equals(" ") ? versionCode[0].split("= ")[1] : versionCode[0].split("=")[1];
                appInfo.setVersionCode(strVersionCode);
            }
        }
        return appInfo;
    }

    //
    public static boolean versionCompare() {
        String url = "http://hzztkj.com:1937/soft/water/app/8000/General/readme.txt";
        String strLocalVersion = getAPPLocalVersion(context);
        String strVersion = getAPPServerVersion(context, url).getVersionName();
        int iLocalVersion = Integer.parseInt(strLocalVersion.replace(".", ""));
        int iVersion = Integer.parseInt(strVersion.replace(".", ""));
        return iLocalVersion < iVersion;
    }


    ///////////////////////test
    /*
     * 下载新版本程序
     */
    private static void loadNewVersionProgress() {
        String url = "http://hzztkj.com:1937/soft/water/app/8000/General/readme.txt";
        final String apkName = getAPPServerVersion(context, url).getAppName();
        final String apkUrl = "http://hzztkj.com:1937/soft/water/app/8000/General/" + apkName.replace(" ", "%20");

        //启动子线程下载任务
        new Thread() {
            @Override
            public void run() {
                try {

                    File file = getFileFromServer(apkUrl, null, apkName);
                    sleep(3000);
                    Message msg = new Message();
                    msg.what = 11112;
                    msg.obj = file;
                    //msg.obj =file.getPath();
                    //mjCompSysTimeHandler.sendMessage(msg);

                } catch (Exception e) {
                    //下载apk失败
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /*
     * 安装apk
     */
    protected static void installApk(File file) {
        Intent intent = new Intent();//执行动作
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }


    /*
     * 静默安装
     */
    private static void installSlient(String filePathName) {
        String cmd = "pm install -r " + filePathName;
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
