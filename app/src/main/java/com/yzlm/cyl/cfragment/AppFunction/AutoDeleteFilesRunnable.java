package com.yzlm.cyl.cfragment.AppFunction;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.yzlm.cyl.cfragment.File.MFile.deleteDir;
import static com.yzlm.cyl.cfragment.File.MFile.getDirSize;

/*
 * 自动删除后台存储的相关运行文件
 * Created by zwj on 2018/1/11.
 */
class AutoDeleteFilesRunnable implements Runnable {

    @Override
    public void run() {

        double dirWaterRunSize = getDirSize(new File("/sdcard/waterrun/"));
        if (dirWaterRunSize > 200) {
            deleteDirFile("/sdcard/waterrun/", -30);
            dirWaterRunSize = getDirSize(new File("/sdcard/waterrun/"));
            if (dirWaterRunSize > 200) {
                deleteDir("/sdcard/waterrun/");
            }
        }

        double dirWSWaterRunSize = getDirSize(new File("/sdcard/WSwaterrun/"));
        if (dirWSWaterRunSize > 200) {
            deleteDirFile("/sdcard/WSwaterrun/", -30);
            dirWSWaterRunSize = getDirSize(new File("/sdcard/WSwaterrun/"));
            if (dirWSWaterRunSize> 200) {
                deleteDir("/sdcard/WSwaterrun/");
            }
        }

        double dirWaterCrashSize = getDirSize(new File("/sdcard/watercrash/"));
        if (dirWaterCrashSize > 10) {
            deleteDirFile("/sdcard/watercrash/", -30);
            dirWaterCrashSize = getDirSize(new File("/sdcard/watercrash/"));
            if (dirWaterCrashSize > 10) {
                deleteDir("/sdcard/watercrash/");
            }
        }


        double dirWaterExcepSize = getDirSize(new File("/sdcard/waterexcep/"));
        if (dirWaterExcepSize > 100) {
            deleteDirFile("/sdcard/waterexcep/", -30);
            dirWaterExcepSize = getDirSize(new File("/sdcard/waterexcep/"));
            if (dirWaterExcepSize > 100) {
                deleteDir("/sdcard/waterexcep/");
            }
        }

    }


    //删除文件夹内最近30条之外的文件
    private void deleteDirFile(final String pPath, int saveFileNum) {
        File dir = new File(pPath);
        deleteDirWithFile(dir, saveFileNum);
    }

    private void deleteDirWithFile(File dir, int n) {
        if (dir == null || !dir.exists() || !dir.isDirectory())
            return;
        try {
            for (File file : dir.listFiles()) {
                if (file.isFile()) {

                    Date time = stringToDate(file.getName().split("-")[1] + "-" + file.getName().split("-")[2] + "-" + (file.getName().split("-")[3].toString()).split("\\.")[0]);//string转为date
                    Date now = new Date();

                    if (file.getName().split("-")[2].length() > 3) {
                        file.delete();
                    } else if (!belongDate(time, now, n)) {
                        file.delete();
                    }
                } else if (file.isDirectory()) {
                    deleteDirWithFile(file, 0); // 递规的方式删除文件夹
                }
            }
        }catch (Exception e){

        }
    }

    /*
     * 判断time是否在now的n天之内
     *
     * @param time
     * @param now
     * @param n    正数表示在条件时间n天之后，负数表示在条件时间n天之前
     * @return
     */
    private static boolean belongDate(Date time, Date now, int n) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();  //得到日历
        calendar.setTime(now);//把当前时间赋给日历
        calendar.add(Calendar.DAY_OF_MONTH, n);
        Date beforeDays = calendar.getTime();   //得到n前的时间
        return beforeDays.getTime() < time.getTime();
    }


    /*
     * 字符串转换至 Date 格式
     */
    private Date stringToDate(String szBeginTime) {

        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");


        Date date = null;
        try {
            date = fmt.parse(szBeginTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
