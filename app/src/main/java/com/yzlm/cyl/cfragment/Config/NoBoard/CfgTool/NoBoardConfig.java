package com.yzlm.cyl.cfragment.Config.NoBoard.CfgTool;


import com.yzlm.cyl.cfragment.Config.MyDBHelper.CompoentNoBoadrdHelper.CompNoBoardConfigDBHelper;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.yzlm.cyl.cfragment.Global.context;

/**
 * 项目名称    water
 * 类描述
 * 创建人      hp
 * 创建时间    2019/7/22
 */
public class NoBoardConfig {

    protected CompNoBoardConfigDBHelper configDBHelper;
    protected Map<String, Object> configMap = new HashMap<>();
    public String point;

    protected void syncDB() {
        Set set = configMap.keySet();
        Iterator iter = set.iterator();
        while (iter.hasNext()) {
            String Name = iter.next().toString();
            List<Map> list = configDBHelper.queryListMap("select * from Config where Name=? and point=?", new String[]{Name, point});
            if (list.size() == 0) {
                Map<String, Object> map = new HashMap<>();
                map.put("Name", Name);
                map.put("value", configMap.get(Name));
                map.put("point", point);
                configDBHelper.insert("Config", map);
            }
        }
        List<Map> list = configDBHelper.queryListMap("select * from Config where point=?", new String[]{point});

        set = configMap.keySet();
        iter = set.iterator();
        while (iter.hasNext()) {
            String temp = (String) iter.next();
            for (Map item : list) {
                if (item.get("Name").equals(temp)) {
                    configMap.put(temp, item.get("value"));
                    break;
                }
            }
        }
    }

    public void UpDB(String compName, String Name, String value) {
        Map<String, Object> up = new HashMap<>();
        up.put("value", value);
        Map<String, String> col = new HashMap<>();
        col.put("Name", Name);
        col.put("point", point);
        configDBHelper.update("Config", up, col);

        syncDB();

    }


    /*保存数据到数据库
     *  组份，数据key， 数据值
     *
     * **/
    public static void updateNoBoardConfigData(String compName, String key, String value) {
        CompNoBoardConfigDBHelper configDBHelper;
        configDBHelper = CompNoBoardConfigDBHelper.getInstance(context);
        Map<String, Object> up = new HashMap<>();
        up.put("value", value);
        Map<String, String> col = new HashMap<>();
        col.put("Name", key);
        col.put("point", compName);
        configDBHelper.update("Config", up, col);

    }


    /*
     * 获取配置数据
     * 组份， 参数key
     * **/
    public static String getNoBoardConfigData(String compName, String key) {
        CompNoBoardConfigDBHelper configDBHelper;
        configDBHelper = CompNoBoardConfigDBHelper.getInstance(context);
        List<Map> list = configDBHelper.queryListMap("select * from Config where Name=? and point=?", new String[]{key, compName});
        if (list.size() > 0) {
            return list.get(0).get("value").toString();
        } else {
            return "";
        }
    }


}
