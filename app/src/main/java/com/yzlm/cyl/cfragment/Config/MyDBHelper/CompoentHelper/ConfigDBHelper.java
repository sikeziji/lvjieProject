package com.yzlm.cyl.cfragment.Config.MyDBHelper.CompoentHelper;

import android.content.Context;

import com.yzlm.cyl.cfragment.Config.MyDBHelper.DataBaseHelper.ZDataBaseHelper;

import static com.yzlm.cyl.cfragment.Global.SdcardPath;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;

/**
 * Created by caoyiliang on 2016/11/14.
 */

public class ConfigDBHelper extends ZDataBaseHelper {
    /* 使用单例模式，保证该类只有一个实例化对象（WL注释）*/
    private static ConfigDBHelper mDBHelper_Config;
    private static int version = 2;

    private ConfigDBHelper(Context context) {
        super(context);
    }

    public static ConfigDBHelper getInstance(Context context) {
        if (mDBHelper_Config == null) {
            while (true) {
                try {
                    getDBHelper_Config(context);
                }catch (Exception e) {
                    if (e.toString().contains("code 1")) {
                        e.printStackTrace();
                        if (version > 0) {
                            version -= 1;
                            mDBHelper_Config = null;
                            saveExceptInfo2File("config数据库" + version);
                            continue;
                        } else {
                            saveExceptInfo2File("config数据库打开失败，请检查数据库!");
                            break;
                        }
                    } else {
                        saveExceptInfo2File("config数据库打开失败，请检查数据库!" + e.toString());
                        break;
                    }
                }
                break;
            }
        }

        return mDBHelper_Config;
    }

    private static void getDBHelper_Config(Context context) {
        synchronized (ZDataBaseHelper.class) {
            if (mDBHelper_Config == null) {
                mDBHelper_Config = new ConfigDBHelper(context);
                if (mDBHelper_Config.getDB() == null || !mDBHelper_Config.getDB().isOpen()) {
                    mDBHelper_Config.open();
                }
            }
        }
    }

    /**
     * 若数据库有结构更新，请升级该版本号字段或者删除原有的系统数据库文件，否则出现异常（WL注释）
     */
    @Override
    protected int getMDbVersion(Context context) {
        return version;
    }

    /**
     * 获取数据库名（WL注释）
     */
    @Override
    protected String getDbName(Context context) {
        return "ZT_Water_Config.db";
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
        String[] a = new String[3];
        switch (version) {
            case 1:
                /* 数据库组分表增加一列组分地址,测量单元类型(WL)*/
                a[0] = "CREATE TABLE Component (id INTEGER PRIMARY KEY AUTOINCREMENT,Name,used,addr,measCategory)";
                a[1] = "CREATE TABLE config (id INTEGER PRIMARY KEY AUTOINCREMENT,Name,value,point)";// 组份不同的参数不同的内容项存储   point:组份
                a[2] = "CREATE TABLE pconfig (id INTEGER PRIMARY KEY AUTOINCREMENT,Name,value)";    // 组份共有参数的存储
                break;
            case 2:
                /* 数据库组分表增加一列组分地址,测量单元类型(WL),主控板硬件平台*/
                a[0] = "CREATE TABLE Component (id INTEGER PRIMARY KEY AUTOINCREMENT,Name,used,addr,measCategory,boardType)";
                a[1] = "CREATE TABLE config (id INTEGER PRIMARY KEY AUTOINCREMENT,Name,value,point)";// 组份不同的参数不同的内容项存储   point:组份
                a[2] = "CREATE TABLE pconfig (id INTEGER PRIMARY KEY AUTOINCREMENT,Name,value)";    // 组份共有参数的存储
                break;
        }
        return a;
    }

    @Override
    protected String[] getDbUpdateSql(int oldVersion, int newVersion) {
        return update(oldVersion, newVersion);
    }

    @Override
    protected String[] getDbDowngradeSql(int oldVersion, int newVersion) {
        return dbDowngrade(oldVersion, newVersion);
    }


    /*数据库降级
     * // 数据库降级请注意数据差异的地方会丢失
     * **/
    private String[] dbDowngrade(int oldVersion, int newVersion) {

        /// 数据库2 降级到  数据库1
        if (oldVersion == 2 && newVersion == 1) {
            //数据库2 降级到 1  表 History 变更
            String[] cmd = new String[4];
            //第一、先把t_message 未来的表，改名
            cmd[0] = "alter table Component rename to _temp_Component";
            //第二、建立2.0的表结构
            cmd[1] = "CREATE TABLE Component (id INTEGER PRIMARY KEY AUTOINCREMENT,Name,used,addr,measCategory)";
            //第三、把备份的数据，copy到 新建的2.0的表
            cmd[2] = "insert into Component select id,Name,used,addr,measCategory from _temp_Component";
            //第四、把备份表drop掉
            cmd[3] = "drop table if exists _temp_Component";

            return cmd;
        }
        return new String[0];

    }


    private String[] update(int oldVersion, int newVersion) {
        String[] cmd = new String[4];
        /*升级处理*/
        if (oldVersion == 1) {
            switch (newVersion) {
                case 2:
                    //数据库1 升级到数据2  表 History 变更
                    cmd[0] = "alter table Component rename to _temp_Component";
                    cmd[1] = "create table Component(id INTEGER PRIMARY KEY AUTOINCREMENT,Name,used,addr,measCategory,boardType)";
                    cmd[2] = "insert into Component select *,'' from _temp_Component";
                    cmd[3] = "drop table _temp_Component";
                    return cmd;
            }
        }
        return new String[0];
    }


}
