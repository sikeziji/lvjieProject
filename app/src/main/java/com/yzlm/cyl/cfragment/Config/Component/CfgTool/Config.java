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

public class Config {
    protected ConfigDBHelper configDBHelper;
    protected Map<String, Object> configMap = new HashMap<>();
    public String point;

    protected void syncDB() {
        Set set = configMap.keySet();
        Iterator iter = set.iterator();
        while (iter.hasNext()) {
            String Name = iter.next().toString();
            List<Map> list = configDBHelper.queryListMap("select * from config where Name=? and point=?", new String[]{Name, point});
            if (list.size() == 0) {
                Map<String, Object> map = new HashMap<>();
                map.put("Name", Name);
                map.put("value", configMap.get(Name));
                map.put("point", point);
                configDBHelper.insert("config", map);
            }
        }
        List<Map> list = configDBHelper.queryListMap("select * from config where point=?", new String[]{point});

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
        configDBHelper.update("config", up, col);

        syncDB();

    }


    /*保存数据到数据库
     *  组份，数据key， 数据值
     *
     * **/
    public static void updateConfigData(String compName, String key, String value){
        ConfigDBHelper configDBHelper;
        configDBHelper = ConfigDBHelper.getInstance(context);
        Map<String, Object> up = new HashMap<>();
        up.put("value", value);
        Map<String, String> col = new HashMap<>();
        col.put("Name", key);
        col.put("point", compName);
        configDBHelper.update("config", up, col);

    }


    /*
     * 获取配置数据
     * 组份， 参数key
     * **/
    public static String getConfigData(String compName,String key){
        ConfigDBHelper configDBHelper;
        configDBHelper = ConfigDBHelper.getInstance(context);
        List<Map> list = configDBHelper.queryListMap("select * from config where Name=? and point=?", new String[]{key, compName});
        if(list.size()>0){
            return list.get(0).get("value").toString();
        }else{
            return "";
        }
    }

}
