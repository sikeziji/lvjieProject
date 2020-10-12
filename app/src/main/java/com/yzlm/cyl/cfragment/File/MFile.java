package com.yzlm.cyl.cfragment.File;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zwj on 2018/1/10.
 */

public class MFile {

    //删除文件夹和文件夹里面的文件
    public static void deleteDir(final String pPath) {
        File dir = new File(pPath);
        deleteDirWithFile(dir);
    }

    private static void deleteDirWithFile(File dir) {

        if (dir == null || !dir.exists() || !dir.isDirectory())
            return;
        for (File file : dir.listFiles()) {
            if (file.isFile()) {

                file.delete(); // 删除所有文件

            } else if (file.isDirectory()) {
                deleteDirWithFile(file); // 递规的方式删除文件夹
            }
        }
        dir.delete();// 删除目录本身
    }

    /***
     * 获取文件夹的大小
     * */
    public static double getDirSize(File file) {
        //判断文件是否存在
        if (file.exists()) {
            //如果是目录则递归计算其内容的总大小
            if (file.isDirectory()) {
                File[] children = file.listFiles();
                double size = 0;
                for (File f : children)
                    size += getDirSize(f);
                return size;
            } else {//如果是文件则直接返回其大小,以“兆”为单位
                return (double) file.length() / 1024 / 1024;
            }
        } else {
            System.out.println("文件或者文件夹不存在，请检查路径是否正确！");
            return 0.0;
        }
    }

    /**
     * 获取文件长度
     *
     * @param file
     */
    public static long getFileSize(File file) {
        if (file.exists() && file.isFile()) {
            return file.length();
        } else {
            return -1;
        }
    }

    /**
     * 获取某个路径下后缀为"XX"的所有文件
     *
     * @param path 文件路径
     * @param type 文件后缀
     * @return
     */
    public static List<String> getFiles(String path, String type) {
        List<String> files = new ArrayList<String>();
        File file = new File(path);
        File[] tempList = file.listFiles();

        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isFile()) {
                //文件名，包含路径
                //String fileName = tempList[i].toString();
                //文件名，不包含路径
                String fileName = tempList[i].getName();
                if (type.equals("")) {
                    files.add(fileName);
                } else {
                    if (fileName.endsWith(type)) {
                        files.add(fileName);
                    }
                }
            }
        }
        return files;
    }

    /*拷贝文件
     *
     * */
    public static void fileCopy(File dbFile, File backup) throws IOException {
        FileChannel inChannel = new FileInputStream(dbFile).getChannel();
        FileChannel outChannel = new FileOutputStream(backup).getChannel();
        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } catch (IOException e) {

            Log.e("FileBackups", "文件备份操作异常" + e.getMessage());
        } finally {
            if (inChannel != null) {
                inChannel.close();
            }
            if (outChannel != null) {
                outChannel.close();
            }
        }
    }


    /**
     * 复制文件目录
     *
     * @param srcDir  要复制的源目录 eg:/mnt/sdcard/DB
     * @param destDir 复制到的目标目录 eg:/mnt/sdcard/db/
     * @return
     */

    private static boolean copyDir(String srcDir, String destDir) {

        File sourceDir = new File(srcDir);

        //判断文件目录是否存在
        if (!sourceDir.exists()) {
            return false;
        }
        //判断是否是目录
        if (sourceDir.isDirectory()) {
            File[] fileList = sourceDir.listFiles();
            File targetDir = new File(destDir);
            //创建目标目录
            if (!targetDir.exists()) {
                targetDir.mkdirs();
            }
            //遍历要复制该目录下的全部文件
            for (File file : fileList) {
                if (file.isDirectory()) {//如果如果是子目录进行递归
                    copyDir(file.getPath() + "/",
                            destDir + file.getName() + "/");
                } else {//如果是文件则进行文件拷贝
                    copyFile(file.getPath(), destDir + file.getName());
                }
            }
            return true;
        } else {
            copyFileToDir(srcDir, destDir);
            return true;
        }
    }


    /**
     * 复制文件（非目录）
     *
     * @param srcFile  要复制的源文件
     * @param destFile 复制到的目标文件
     * @return
     */

    private static boolean copyFile(String srcFile, String destFile) {

        try {

            InputStream streamFrom = new FileInputStream(srcFile);

            OutputStream streamTo = new FileOutputStream(destFile);

            byte[] buffer = new byte[1024];

            int len;

            while ((len = streamFrom.read(buffer)) > 0) {

                streamTo.write(buffer, 0, len);

            }

            streamFrom.close();

            streamTo.close();

            return true;

        } catch (Exception ex) {

            return false;

        }

    }


    /**
     * 把文件拷贝到某一目录下
     *
     * @param srcFile
     * @param destDir
     * @return
     */

    private static boolean copyFileToDir(String srcFile, String destDir) {

        File fileDir = new File(destDir);

        if (!fileDir.exists()) {

            fileDir.mkdir();

        }

        String destFile = destDir + "/" + new File(srcFile).getName();

        try {

            InputStream streamFrom = new FileInputStream(srcFile);

            OutputStream streamTo = new FileOutputStream(destFile);

            byte[] buffer = new byte[1024];

            int len;

            while ((len = streamFrom.read(buffer)) > 0) {

                streamTo.write(buffer, 0, len);

            }

            streamFrom.close();

            streamTo.close();

            return true;

        } catch (Exception ex) {

            return false;

        }

    }


    /**
     * 移动文件目录到某一路径下
     *
     * @param srcFile
     * @param destDir
     * @return
     */

    public static boolean moveFile(String srcFile, String destDir) {

        //复制后删除原目录

        if (copyDir(srcFile, destDir)) {

            deleteFile(new File(srcFile));

            return true;

        }

        return false;

    }


    /**
     * 删除文件（包括目录）
     *
     * @param delFile
     */

    private static void deleteFile(File delFile) {

        //如果是目录递归删除

        if (delFile.isDirectory()) {

            File[] files = delFile.listFiles();

            for (File file : files) {

                deleteFile(file);

            }

        } else {

            delFile.delete();

        }

        //如果不执行下面这句，目录下所有文件都删除了，但是还剩下子目录空文件夹

        delFile.delete();

    }
}
