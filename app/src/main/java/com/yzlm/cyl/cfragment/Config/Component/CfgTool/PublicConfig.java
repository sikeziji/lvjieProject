package com.yzlm.cyl.cfragment.Config.Component.CfgTool;

import com.yzlm.cyl.cfragment.Config.MyDBHelper.CompoentHelper.ConfigDBHelper;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.yzlm.cyl.cfragment.Global.context;


/*
 * Created by caoyiliang on 2016/12/29.
 */

public class PublicConfig {
    protected ConfigDBHelper configDBHelper;
    protected Map<String, Object> configMap = new HashMap<>();

    protected void syncDB() {
        Set set = configMap.keySet();
        Iterator iter = set.iterator();
        while (iter.hasNext()) {
            String Name = iter.next().toString();
            List<Map> list = configDBHelper.queryListMap("select * from pconfig where Name=?", new String[]{Name});
            if (list.size() == 0) {
                Map<String, Object> map = new HashMap<>();
                map.put("Name", Name);
                map.put("value", configMap.get(Name));
                configDBHelper.insert("pconfig", map);
            }
        }
        List<Map> list = configDBHelper.queryListMap("select * from pconfig", null);

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

    public void UpDB(String Name, String value) {
        Map<String, Object> up = new HashMap<>();
        up.put("value", value);
        Map<String, String> col = new HashMap<>();
        col.put("Name", Name);
        configDBHelper.update("pconfig", up, col);
        syncDB();
    }


    /*
     *  获取组份的变量值    待测试 180103
     * */
    public static String getPublicConfigData(String dataName) {
        ConfigDBHelper configDBHelper;
        configDBHelper = ConfigDBHelper.getInstance(context);
        List<Map> list = configDBHelper.queryListMap("select * from pconfig where Name=?", new String[]{dataName});
        if (list.size() > 0) {
            return list.get(0).get("value").toString();
        } else {
            return "";
        }
    }


    /*
     * 更新组份的变量到数据库  待测试 180103
     * */
    public static void updatePublicConfigData(String Name, String value) {
        ConfigDBHelper configDBHelper;
        configDBHelper = ConfigDBHelper.getInstance(context);

        Map<String, Object> up = new HashMap<>();
        up.put("value", value);
        Map<String, String> col = new HashMap<>();
        col.put("Name", Name);
        configDBHelper.update("pconfig", up, col);
    }
}
