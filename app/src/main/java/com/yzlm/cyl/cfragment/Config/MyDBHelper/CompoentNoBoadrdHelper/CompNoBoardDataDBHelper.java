package com.yzlm.cyl.cfragment.Config.MyDBHelper.CompoentNoBoadrdHelper;

import android.content.Context;

import com.yzlm.cyl.cfragment.Config.MyDBHelper.DataBaseHelper.ZDataBaseHelper;

import static com.yzlm.cyl.cfragment.Global.SdcardPath;

/**
 * Created by zwj on 2018/4/16.
 */

public class CompNoBoardDataDBHelper extends ZDataBaseHelper {

    private static CompNoBoardDataDBHelper mDBHelper_ModuleData;

    private CompNoBoardDataDBHelper(Context context) {
        super(context);
    }

    public static CompNoBoardDataDBHelper getInstance(Context context) {
        if (mDBHelper_ModuleData == null) {

            synchronized (ZDataBaseHelper.class) {
                if (mDBHelper_ModuleData == null) {
                    mDBHelper_ModuleData = new CompNoBoardDataDBHelper(context);
                    if (mDBHelper_ModuleData.getDB() == null || !mDBHelper_ModuleData.getDB().isOpen()) {
                        mDBHelper_ModuleData.open();
                    }
                }
            }
        }
        return mDBHelper_ModuleData;
    }

    /**
     * 若数据库有结构更新，请升级该版本号字段或者删除原有的系统数据库文件，否则出现异常（WL注释）
     */
    @Override
    protected int getMDbVersion(Context context) {
        return 1;
    }

    /**
     * 获取数据库名
     */
    @Override
    protected String getDbName(Context context) {
        return "ZT_Water_CompNoBoardData.db";
    }

    /**
     * 获取数据库文件路径
     */
    @Override
    protected String getDbPath(Context context) {
        return SdcardPath == null ? SdcardPath : (SdcardPath + "Csoft");
    }

    /**
     * 获取数据库的创建数据表的SQL语句
     */
    @Override
    protected String[] getDbCreateSql(Context context) {
        String[] a = new String[4];
        a[0] = "CREATE TABLE Log (id INTEGER PRIMARY KEY AUTOINCREMENT,compName,time,content,type)";
        a[1] = "CREATE TABLE History (id INTEGER PRIMARY KEY AUTOINCREMENT,time,flow,component,C,unit,temperature,energy,A,tag)";
        a[2] = "CREATE TABLE KBF (id INTEGER PRIMARY KEY AUTOINCREMENT,time,component,range,K,B,F)";
        a[3] = "CREATE TABLE ERROR (id INTEGER PRIMARY KEY AUTOINCREMENT,compName,time,errNum,errInfo,errType,errHelpInfo)";
        return a;
    }

    @Override
    protected String[] getDbUpdateSql(int oldVersion, int newVersion) {
        return new String[0];
    }

    @Override
    protected String[] getDbDowngradeSql(int oldVersion, int newVersion) {
        return new String[0];
    }
}