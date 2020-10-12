package com.yzlm.cyl.cfragment.Config.NoBoard.CfgTool;

import android.content.Context;

import com.yzlm.cyl.cfragment.Config.MyDBHelper.CompoentNoBoadrdHelper.CompNoBoardDataDBHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.getPublicConfigData;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.clibrary.Util.DataUtil.copy;

/**
 * 项目名称    water
 * 类描述
 * 创建人      hp
 * 创建时间    2019/7/22
 */
public class NoBoardErrorLog {

    private CompNoBoardDataDBHelper ErrorDBHelper;

    public NoBoardErrorLog(Context context) {
        ErrorDBHelper = CompNoBoardDataDBHelper.getInstance(context);
    }


    public void Add(String compName, String errNum, String errInfo, NoBoardErrorLog.msgType errType, String errHelpInfo) {
        Map<String, Object> map = new HashMap<>();
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        boolean timeFlag = false;
        String lastDataTime = getLastErrorTime(compName);
        if (lastDataTime != null) {
            String nowTime = sDateFormat.format(new Date());
            timeFlag = lastDataTime.equals(nowTime);
        }

        String lastErrorNum = getLastErrorNum(compName);
        String name = getPublicConfigData("LogInName");
        String loginName = "";
        switch (name) {
            case "0":
                loginName = "公共";
                break;
            case "1":
                loginName = "用户";
                break;
            case "2":
                loginName = "运维";
                break;
        }


        if (!timeFlag || (!lastErrorNum.equals(errNum))) {
            map.put("compName", compName);
            map.put("time", sDateFormat.format(new Date()));
            map.put("errNum", errNum);
            map.put("errInfo", errInfo);
            map.put("errType", String.valueOf(errType));
            map.put("errHelpInfo", errHelpInfo);
            map.put("account", loginName);
            ErrorDBHelper.insert("ERROR", map);
        }
    }

    public List<Map> select(String Component, String startTime, String endTime, NoBoardErrorLog.msgType[] types, int index) {

        String sql = "select * from ERROR where compName =?";
        String[] params = new String[]{Component};

        if (!(startTime == null || endTime == null || startTime.equals("") || endTime.equals(""))) {
            sql += " and (datetime(time) between datetime(?) and datetime(?))";

            params = copy(params, new String[]{startTime, endTime}).toArray(new String[params.length + 2]);
        }
        if (types != null) {
            sql += " and(";
            for (NoBoardErrorLog.msgType item : types) {
                sql += " errType=? or";
                params = copy(params, new String[]{String.valueOf(NoBoardErrorLog.msgType.values()[item.ordinal()])}).toArray(new String[params.length + 1]);
            }
            sql = sql.substring(0, sql.length() - 3);
            sql += ")";
        }
        sql += " order by id desc limit ?,100";
        params = copy(params, new String[]{Integer.toString(index)}).toArray(new String[params.length + 1]);
        return ErrorDBHelper.queryListMap(sql, params);
    }
    public List<Map> selectAlarmAll(String Component, String startTime, String endTime, NoBoardErrorLog.msgType[] types, int index) {

        String sql = "select * from ERROR where compName =?";
        String[] params = new String[]{Component};

        if (!(startTime == null || endTime == null || startTime.equals("") || endTime.equals(""))) {
            sql += " and (datetime(time) between datetime(?) and datetime(?))";

            params = copy(params, new String[]{startTime, endTime}).toArray(new String[params.length + 2]);
        }
        if (types != null) {
            sql += " and(";
            for (NoBoardErrorLog.msgType item : types) {
                sql += " errType=? or";
                params = copy(params, new String[]{String.valueOf(NoBoardErrorLog.msgType.values()[item.ordinal()])}).toArray(new String[params.length + 1]);
            }
            sql = sql.substring(0, sql.length() - 3);
            sql += ")";
        }
        return ErrorDBHelper.queryListMap(sql, params);
    }
    public void clearErrorData() {
        ErrorDBHelper.execSQL("delete from ERROR");
        ErrorDBHelper.execSQL("delete from sqlite_sequence WHERE name =" + "\'ERROR\'");
    }

    public void clearSelectErrorData(String Component, NoBoardErrorLog.msgType[] types) {
        String sql = "";
        if (types != null) {
            sql += " and (";
            for (NoBoardErrorLog.msgType item : types) {
                sql += " errType=" + "\'" + item + "\'" + "or";
            }
            sql = sql.substring(0, sql.length() - 2);
            sql += " )";
        }
        ErrorDBHelper.execSQL("delete from ERROR WHERE compName =" + "\'" + Component + "\'" + sql);
    }

    private List<Map> selectErrorTop(String Component) {
        String sql = "select * from ERROR where compName=?";
        String[] params = new String[]{Component};
        sql += " order by id desc limit 0,1";

        return ErrorDBHelper.queryListMap(sql, params);
    }

    /**
     * 最后一条数据的数据时间
     */
    String getLastErrorTime(String cmpName) {
        String time = null;

        NoBoardErrorLog error = new NoBoardErrorLog(context);

        List<Map> errMap = error.selectErrorTop(cmpName);

        if (errMap.size() > 0) {
            time = errMap.get(0).get("time").toString();
        }
        return time;
    }

    /**
     * 获取最后一条报警号
     */
    String getLastErrorNum(String cmpName) {
        String num = null;

        NoBoardErrorLog error = new NoBoardErrorLog(context);

        List<Map> errMap = error.selectErrorTop(cmpName);

        if (errMap.size() > 0) {
            num = errMap.get(0).get("errNum").toString();
        }
        return num;
    }
    public enum msgType {
        通讯_信息,
        通讯_错误,
        报错_信息,
        报错_警告,
        报错_错误,
        运行_信息,
        校准_信息,
        操作_信息,
        密码_输入,
    }

}
