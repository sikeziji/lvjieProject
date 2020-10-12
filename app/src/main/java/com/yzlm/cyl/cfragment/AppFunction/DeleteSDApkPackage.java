package com.yzlm.cyl.cfragment.AppFunction;

import android.os.Environment;

import java.io.File;
import java.util.List;

import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.getFilesAllName;
import static com.yzlm.cyl.clibrary.Util.SDCardUtils.getStoragePath;

public class DeleteSDApkPackage {

    public static File getPathFile(String path) {
        String apkName = path.substring(path.lastIndexOf("/"));
        File outputFile = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.FROYO) {
            outputFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), apkName);
        }
        return outputFile;
    }

    public static boolean removeFile(String path) {
        /*  File file = getPathFile(path);*/
        File file = new File(path);
        return file.delete();
    }

    private static List<String> getFiles() {
        String SdcardPath = (getStoragePath(context, false).size() == 0 ? null : (getStoragePath(context, false).get(0) + File.separator));
        String extSdcardPath = (getStoragePath(context, true).size() == 0 ? SdcardPath : (getStoragePath(context, true).get(0) + File.separator));
        String dir;
        if (SdcardPath == null) {
            if (extSdcardPath == null) {
                return null;
            } else {
                dir = extSdcardPath;
            }
        } else {
            dir = SdcardPath;
        }
        return getFilesAllName(dir);
    }

    /*删除包含 Water 或者 app 或者 jiagu_sign 的.apk文件*/
    public static void removeMyApkFile() {
        new Thread() {
            @Override
            public void run() {
                removeDirMyApkFile("/sdcard/");
                removeDirMyApkFile("/sdcard/Csoft");
            }
        }.start();


    }

    private static void removeDirMyApkFile(String dir) {
        List<String> list = getFiles();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                String fileName = list.get(i);
                if ((fileName.contains("Water") || fileName.contains("app") || fileName.contains("jiagu_sign")) && fileName.contains(".apk"))
                    removeFile(list.get(i));
            }
        }
    }

}
