package com.yzlm.cyl.cfragment.Config.MyDBHelper.CompoentNoBoadrdHelper;

import android.content.Context;

import com.yzlm.cyl.cfragment.Config.MyDBHelper.DataBaseHelper.ZDataBaseHelper;

import static com.yzlm.cyl.cfragment.Global.SdcardPath;

/**
 * Created by zwj on 2018/4/16.
 */

public class CompNoBoardConfigDBHelper extends ZDataBaseHelper {
    /* 使用单例模式，保证该类只有一个实例化对象（WL注释）*/
    private static CompNoBoardConfigDBHelper mDBHelper_ModuleConfig;

    private CompNoBoardConfigDBHelper(Context context) {
        super(context);
    }

    public static CompNoBoardConfigDBHelper getInstance(Context context) {
        if (mDBHelper_ModuleConfig == null) {

            synchronized (ZDataBaseHelper.class) {
                if (mDBHelper_ModuleConfig == null) {
                    mDBHelper_ModuleConfig = new CompNoBoardConfigDBHelper(context);
                    if (mDBHelper_ModuleConfig.getDB() == null || !mDBHelper_ModuleConfig.getDB().isOpen()) {
                        mDBHelper_ModuleConfig.open();
                    }
                }
            }
        }
        return mDBHelper_ModuleConfig;
    }

    /**
     * 若数据库有结构更新，请升级该版本号字段或者删除原有的系统数据库文件，否则出现异常（WL注释）
     */
    @Override
    protected int getMDbVersion(Context context) {
        return 1;
    }

    /**
     * 获取数据库名（WL注释）
     */
    @Override
    protected String getDbName(Context context) {
        return "ZT_Water_NoBoardConfig.db";
    }

    /**
     * 获取数据库文件路径（WL注释）
     */
    @Override
    protected String getDbPath(Context context) {
        return SdcardPath == null ? SdcardPath : (SdcardPath + "Csoft");
    }

    /**
     * 获取数据库的创建数据表的SQL语句（WL注释）
     */
    @Override
    protected String[] getDbCreateSql(Context context) {
        String[] a = new String[2];
        /* 数据库组分表增加一列组分地址,测量单元类型(WL)*/
        a[0] = "CREATE TABLE Component (id INTEGER PRIMARY KEY AUTOINCREMENT,Name,used,addr,measCategory)";
        a[1] = "CREATE TABLE Config (id INTEGER PRIMARY KEY AUTOINCREMENT,Name,value,point)";// 组份不同的参数不同的内容项存储   point:组份
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