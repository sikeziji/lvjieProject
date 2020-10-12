package com.yzlm.cyl.cfragment.AppFunction;

import android.util.Log;

import java.io.File;

import static com.yzlm.cyl.cfragment.File.MFile.fileCopy;
import static com.yzlm.cyl.cfragment.File.MFile.getFileSize;

public class BackUpFileData implements Runnable {

    private static final String COMMAND_BACKUP = "backup";
    private static final String COMMAND_RESTORE = "restore";

    @Override
    public void run() {
        backupOrRestoreFiles("/sdcard/Csoft/", "ZT_Water_Data.db", "/sdcard/CsoftBackUpFile/", "ZTWaterData", COMMAND_BACKUP);
        backupOrRestoreFiles("/sdcard/Csoft/", "ZT_Water_Config.db", "/sdcard/CsoftBackUpFile/", "ZTWaterData", COMMAND_BACKUP);
    }


    /*
    备份或者恢复已备份文件
    par1:文件目录，par2：文件名称,par3:备份文件目录，par4：备份文件目录内子文件夹,par5:指令 备份或恢复
    * **/
    private int backupOrRestoreFiles(String formFilePath, String formFileName, String toFilePath, String toDir, String cmd) {

        // 需要备份的数据库路径
        File dbFile = new File(formFilePath, formFileName);
        // 创建数据库目录路径
        File exportDir = new File(toFilePath, toDir);
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }
        File backup = new File(exportDir, dbFile.getName());
        switch (cmd) {
            case COMMAND_BACKUP:
                try {
                    if (getFileSize(dbFile) >= getFileSize(backup)) {
                        backup.createNewFile();
                        fileCopy(dbFile, backup);
                        return 1;
                    }
                } catch (Exception e) {
                    Log.e("FileBackups", "数据备份异常" + e.getMessage());
                    return 2;
                }
                return 0;
            case COMMAND_RESTORE:
                try {
                    if (getFileSize(dbFile) <= getFileSize(backup) && getFileSize(backup) != 0) {
                        fileCopy(backup, dbFile);
                        return 1;
                    }
                } catch (Exception e) {
                    Log.e("FileBackups", "数据还原异常" + e.getMessage());
                    return 2;
                }
            default:
                return -1;
        }
    }

}
