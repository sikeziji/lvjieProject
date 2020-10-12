package com.yzlm.cyl.cfragment.Config.NoBoard.CfgTool;

import android.content.Context;
import android.os.Message;

import com.yzlm.cyl.cfragment.Communication.Component.AppCmd.RecMessage;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.DevLog;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.ErrorLog;
import com.yzlm.cyl.cfragment.Config.MyDBHelper.CompoentNoBoadrdHelper.CompNoBoardDataDBHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yzlm.cyl.cfragment.Config.NoBoard.CfgTool.NoBoardConfig.getNoBoardConfigData;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.cfragment.Global.mNoBoardHandler;
import static com.yzlm.cyl.cfragment.Global.saveExceptInfo2File;
import static com.yzlm.cyl.clibrary.Util.DataUtil.copy;

/**
 * 项目名称    water
 * 类描述
 * 创建人      hp
 * 创建时间    2019/7/22
 */
public class NoBoardDevLog {

    private CompNoBoardDataDBHelper LogDBHelper;

    public NoBoardDevLog(Context context) {
        LogDBHelper = CompNoBoardDataDBHelper.getInstance(context);
    }

    public void Add(String compName, String content, NoBoardErrorLog.msgType logType) {
        Map<String, Object> map = new HashMap<>();
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        boolean timeFlag = false;
        String lastDataTime = getLastLogTime(compName);
        if (lastDataTime != null) {
            String nowTime = sDateFormat.format(new Date());
            timeFlag = lastDataTime.equals(nowTime);
        }
        if (!timeFlag) {
            map.put("compName", compName);
            map.put("time", sDateFormat.format(new Date()));
            map.put("content", String.valueOf(content));
            map.put("type", String.valueOf(logType));
            LogDBHelper.insert("Log", map);
        }
    }

    public List<Map> select(String Component, String startTime, String endTime, ErrorLog.msgType[] types, int index) {

        String sql = "select * from Log where compName =?";
        String[] params = new String[]{Component};

        if (!(startTime == null || endTime == null || startTime.equals("") || endTime.equals(""))) {
            sql += " and (datetime(time) between datetime(?) and datetime(?))";

            params = copy(params, new String[]{startTime, endTime}).toArray(new String[params.length + 2]);
        }
        if (types != null) {
            sql += " and(";
            for (ErrorLog.msgType item : types) {
                sql += " type=? or";
                params = copy(params, new String[]{String.valueOf(ErrorLog.msgType.values()[item.ordinal()])}).toArray(new String[params.length + 1]);
            }
            sql = sql.substring(0, sql.length() - 3);
            sql += ")";
        }
        sql += " order by id desc limit ?,100";
        params = copy(params, new String[]{Integer.toString(index)}).toArray(new String[params.length + 1]);
        return LogDBHelper.queryListMap(sql, params);
    }

    public List<Map> selectAlarmAll(String Component, String startTime, String endTime, ErrorLog.msgType[] types) {

        String sql = "select * from Log where compName =?";
        String[] params = new String[]{Component};

        if (!(startTime == null || endTime == null || startTime.equals("") || endTime.equals(""))) {
            sql += " and (datetime(time) between datetime(?) and datetime(?))";

            params = copy(params, new String[]{startTime, endTime}).toArray(new String[params.length + 2]);
        }
        if (types != null) {
            sql += " and(";
            for (ErrorLog.msgType item : types) {
                sql += " type=? or";
                params = copy(params, new String[]{String.valueOf(ErrorLog.msgType.values()[item.ordinal()])}).toArray(new String[params.length + 1]);
            }
            sql = sql.substring(0, sql.length() - 3);
            sql += ")";
        }
        return LogDBHelper.queryListMap(sql, params);
    }

    public void clearErrorData() {
        LogDBHelper.execSQL("delete from Log");
        LogDBHelper.execSQL("delete from sqlite_sequence WHERE name =" + "\'Log\'");
    }

    public void clearSelectErrorData(String Component, ErrorLog.msgType[] types) {
        String sql = "";
        if (types != null) {
            sql += " and (";
            for (ErrorLog.msgType item : types) {
                sql += " type=" + "\'" + item + "\'" + "or";
            }
            sql = sql.substring(0, sql.length() - 2);
            sql += " )";
        }
        LogDBHelper.execSQL("delete from Log WHERE compName =" + "\'" + Component + "\'" + sql);
    }

    public List<Map> selectLogTop(String Component) {
        String sql = "select * from Log where compName=?";
        String[] params = new String[]{Component};
        sql += " order by id desc limit 0,1";

        return LogDBHelper.queryListMap(sql, params);
    }

    /**
     * 最后一条数据的数据时间
     */
    private String getLastLogTime(String cmpName) {
        String time = null;

        DevLog devLog = new DevLog(context);

        List<Map> errMap = devLog.selectLogTop(cmpName);

        if (errMap.size() > 0) {
            time = errMap.get(0).get("time").toString();
        }
        return time;
    }

    /**
     * 无背板 组分 增加日志
     *
     * @param compName 组分
     * @param content  日志内容
     * @param logType  日志类型
     */
    private static void AddNoBoardDevOperationLog(String compName, String content, ErrorLog.msgType logType) {


        RecMessage RsMessage = new RecMessage();
        try {
            RsMessage.setMesType(logType);
            RsMessage.setMessage(compName + "_" + content + "_" + logType);
        } catch (Exception e) {
            saveExceptInfo2File("无背板 组分[" + compName + "] AddNoBoardDevOperationLog：" + content + e.toString());
        }
        Message msg = new Message();
        msg.what = RsMessage.getMesType().ordinal();
        msg.obj = RsMessage.getMessage();
        mNoBoardHandler.sendMessage(msg);
    }

    /*
     * 无背板 组分 保存数据库变量内容
     * Key : 数据库的参数key  newValue:要存储的数据库值  日志保存的描述
     */
    public static void saveNoBoardOperationLogDataModifyMsg(String compName, String Key, String newValue, String msgInfo, ErrorLog.msgType logType) {
        try {
            if (!getNoBoardConfigData(compName, Key).equals(newValue)) {
                AddNoBoardDevOperationLog(compName, msgInfo + "" + newValue, logType);
            }
        } catch (Exception e) {
            saveExceptInfo2File("无背板保存组份参数至数据库变量内容异常" + e.toString());
        }
    }


    /*
     * 无背板 组分 报错操作文字描述内容
     */
    public static void saveNoBoardOperationLogMsg(String compName, String msgInfo, ErrorLog.msgType logType) {
        try {
            AddNoBoardDevOperationLog(compName, msgInfo, logType);
        } catch (Exception e) {
            saveExceptInfo2File("无背板保存操作日志异常" + e.toString());
        }
    }

}
