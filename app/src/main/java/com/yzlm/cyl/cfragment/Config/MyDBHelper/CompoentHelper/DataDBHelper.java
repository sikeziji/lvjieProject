package com.yzlm.cyl.cfragment.Config.MyDBHelper.CompoentHelper;

import android.content.Context;

import com.yzlm.cyl.cfragment.Config.MyDBHelper.DataBaseHelper.ZDataBaseHelper;

import static com.yzlm.cyl.cfragment.Global.SdcardPath;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;

/**
 * Created by caoyiliang on 2016/11/14.
 */

public class DataDBHelper extends ZDataBaseHelper {
    /* 使用单例模式，保证该类只有一个实例化对象（WL注释）*/
    private static DataDBHelper mDBHelper_Data;
    private static int version = 3;


    private DataDBHelper(Context context) {
        super(context);
    }

    public static DataDBHelper getInstance(Context context) {
        if (mDBHelper_Data == null) {
            while (true) {
                try {
                    getDBHelper_Data(context);
                } catch (Exception e) {
                    if (e.toString().contains("code 1")) {
                        e.printStackTrace();
                        if (version > 0) {
                            version -= 1;
                            mDBHelper_Data = null;
                            saveExceptInfo2File("数据库" + version);
                            continue;
                        } else {
                            saveExceptInfo2File("数据库打开失败，请检查数据库!");
                            break;
                        }
                    } else {
                        saveExceptInfo2File("数据库打开失败，请检查数据库!" + e.toString());
                        break;
                    }
                }
                break;
            }
        }
        return mDBHelper_Data;
    }

    private static void getDBHelper_Data(Context context) {
        synchronized (ZDataBaseHelper.class) {
            if (mDBHelper_Data == null) {
                mDBHelper_Data = new DataDBHelper(context);
                if (mDBHelper_Data.getDB() == null || !mDBHelper_Data.getDB().isOpen()) {
                    mDBHelper_Data.open();
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
        return "ZT_Water_Data.db";
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
     * <p>
     * //数据库1.0 表
     * a[0] = "CREATE TABLE Log (id INTEGER PRIMARY KEY AUTOINCREMENT,compName,time,content,type)";
     * a[1] = "CREATE TABLE History (id INTEGER PRIMARY KEY AUTOINCREMENT,time,flow,component,C,unit,temperature,energy,A)";
     * a[2] = "CREATE TABLE KBF (id INTEGER PRIMARY KEY AUTOINCREMENT,time,component,range,K,B,F)";
     * a[3] = "CREATE TABLE ERROR (id INTEGER PRIMARY KEY AUTOINCREMENT,compName,time,errNum,errInfo,errType,errHelpInfo)";
     */
    @Override
    protected String[] getDbCreateSql(Context context) {
        String[] a = new String[4];
        switch (getMDbVersion(context)) {
            case 1:
                a[0] = "CREATE TABLE Log (id INTEGER PRIMARY KEY AUTOINCREMENT,compName,time,content,type)";
                a[1] = "CREATE TABLE History (id INTEGER PRIMARY KEY AUTOINCREMENT,time,flow,component,C,unit,temperature,energy,A)";
                a[2] = "CREATE TABLE KBF (id INTEGER PRIMARY KEY AUTOINCREMENT,time,component,range,K,B,F)";
                a[3] = "CREATE TABLE ERROR (id INTEGER PRIMARY KEY AUTOINCREMENT,compName,time,errNum,errInfo,errType,errHelpInfo)";
                break;
            case 2:
                a[0] = "CREATE TABLE Log (id INTEGER PRIMARY KEY AUTOINCREMENT,compName,time,content,type)";
                //表 History 2.0
                a[1] = "CREATE TABLE History (id INTEGER PRIMARY KEY AUTOINCREMENT,time,flow,component,C,unit,temperature,energy,A,tag)";
                a[2] = "CREATE TABLE KBF (id INTEGER PRIMARY KEY AUTOINCREMENT,time,component,range,K,B,F)";
                a[3] = "CREATE TABLE ERROR (id INTEGER PRIMARY KEY AUTOINCREMENT,compName,time,errNum,errInfo,errType,errHelpInfo)";
                break;
            case 3:
                a[0] = "CREATE TABLE Log (id INTEGER PRIMARY KEY AUTOINCREMENT,compName,time,content,type)";
                //表 History 3.0
                a[1] = "CREATE TABLE History (id INTEGER PRIMARY KEY AUTOINCREMENT,time,flow,component,C,unit,temperature,energy,A,tag,C2)";
                a[2] = "CREATE TABLE KBF (id INTEGER PRIMARY KEY AUTOINCREMENT,time,component,range,K,B,F)";
                a[3] = "CREATE TABLE ERROR (id INTEGER PRIMARY KEY AUTOINCREMENT,compName,time,errNum,errInfo,errType,errHelpInfo,account)";
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
            cmd[0] = "alter table History rename to _temp_History";
            //第二、建立2.0的表结构
            cmd[1] = "CREATE TABLE History (id INTEGER PRIMARY KEY AUTOINCREMENT,time,flow,component,C,unit,temperature,energy,A)";
            //第三、把备份的数据，copy到 新建的2.0的表
            cmd[2] = "insert into History select id,time,flow,component,C,unit,temperature,energy,A from _temp_History";
            //第四、把备份表drop掉
            cmd[3] = "drop table if exists _temp_History";

            return cmd;
        } else if (oldVersion == 3 && newVersion == 2) {
            //数据库2 降级到 1  表 History 变更
            String[] cmd = new String[8];
            //第一、先把t_message 未来的表，改名
            cmd[0] = "alter table History rename to _temp_History";
            //第二、建立2.0的表结构
            cmd[1] = "CREATE TABLE History (id INTEGER PRIMARY KEY AUTOINCREMENT,time,flow,component,C,unit,temperature,energy,A,tag)";
            //第三、把备份的数据，copy到 新建的2.0的表
            cmd[2] = "insert into History select id,time,flow,component,C,unit,temperature,energy,A,tag from _temp_History";
            //第四、把备份表drop掉
            cmd[3] = "drop table if exists _temp_History";
            //第一、先把t_message 未来的表，改名
            cmd[4] = "alter table ERROR rename to _temp_ERROR";
            //第二、建立2.0的表结构
            cmd[5] = "CREATE TABLE ERROR (id INTEGER PRIMARY KEY AUTOINCREMENT,compName,time,errNum,errInfo,errType,errHelpInfo)";
            //第三、把备份的数据，copy到 新建的2.0的表
            cmd[6] = "insert into ERROR select id,compName,time,errNum,errInfo,errType,errHelpInfo from _temp_ERROR";
            //第四、把备份表drop掉
            cmd[7] = "drop table if exists _temp_ERROR";

            return cmd;
        } else if (oldVersion == 3 && newVersion == 1) {
            //数据库2 降级到 1  表 History 变更
            String[] cmd = new String[8];
            //第一、先把t_message 未来的表，改名
            cmd[0] = "alter table History rename to _temp_History";
            //第二、建立2.0的表结构
            cmd[1] = "CREATE TABLE History (id INTEGER PRIMARY KEY AUTOINCREMENT,time,flow,component,C,unit,temperature,energy,A)";
            //第三、把备份的数据，copy到 新建的2.0的表
            cmd[2] = "insert into History select id,time,flow,component,C,unit,temperature,energy,A from _temp_History";
            //第四、把备份表drop掉
            cmd[3] = "drop table if exists _temp_History";
            //第一、先把t_message 未来的表，改名
            cmd[4] = "alter table ERROR rename to _temp_ERROR";
            //第二、建立2.0的表结构
            cmd[5] = "CREATE TABLE ERROR (id INTEGER PRIMARY KEY AUTOINCREMENT,compName,time,errNum,errInfo,errType,errHelpInfo)";
            //第三、把备份的数据，copy到 新建的2.0的表
            cmd[6] = "insert into ERROR select id,compName,time,errNum,errInfo,errType,errHelpInfo from _temp_ERROR";
            //第四、把备份表drop掉
            cmd[7] = "drop table if exists _temp_ERROR";

            return cmd;
        }
        return new String[0];

    }


    private String[] update(int oldVersion, int newVersion) {
        String[] cmd = new String[8];
        /*升级处理*/
        if (oldVersion == 1) {
            switch (newVersion) {
                case 2:
                    cmd = new String[4];
                    //数据库1 升级到数据2  表 History 变更
                    cmd[0] = "alter table History rename to _temp_History";
                    cmd[1] = "create table History(id INTEGER PRIMARY KEY AUTOINCREMENT,time,flow,component,C,unit,temperature,energy,A,tag)";
                    cmd[2] = "insert into History select *,'' from _temp_History";
                    cmd[3] = "drop table _temp_History";
                    return cmd;
                case 3:
                    //数据库1 升级到数据3  表 History 变更
                    cmd[0] = "alter table History rename to _temp_History";
                    cmd[1] = "create table History(id INTEGER PRIMARY KEY AUTOINCREMENT,time,flow,component,C,unit,temperature,energy,A,tag,C2)";
                    cmd[2] = "insert into History select *,'' from _temp_History";
                    cmd[3] = "drop table _temp_History";
                    //数据库1 升级到数据3  表 ERROR 变更
                    cmd[4] = "alter table ERROR rename to _temp_ERROR";
                    cmd[5] = "create table ERROR(id INTEGER PRIMARY KEY AUTOINCREMENT,compName,time,errNum,errInfo,errType,errHelpInfo,account)";
                    cmd[6] = "insert into ERROR select *,'' from _temp_ERROR";
                    cmd[7] = "drop table _temp_ERROR";
                    return cmd;
            }
        } else if (oldVersion == 2) {
            switch (newVersion) {
                case 3:
                    //数据库2 升级到数据3  表 History 变更
                    cmd[0] = "alter table History rename to _temp_History";
                    cmd[1] = "create table History(id INTEGER PRIMARY KEY AUTOINCREMENT,time,flow,component,C,unit,temperature,energy,A,tag,C2)";
                    cmd[2] = "insert into History select *,'' from _temp_History";
                    cmd[3] = "drop table _temp_History";
                    //数据库2 升级到数据3  表 ERROR 变更
                    cmd[4] = "alter table ERROR rename to _temp_ERROR";
                    cmd[5] = "create table ERROR(id INTEGER PRIMARY KEY AUTOINCREMENT,compName,time,errNum,errInfo,errType,errHelpInfo,account)";
                    cmd[6] = "insert into ERROR select *,'' from _temp_ERROR";
                    cmd[7] = "drop table _temp_ERROR";
                    return cmd;
            }
        }
        return new String[0];
    }

}
