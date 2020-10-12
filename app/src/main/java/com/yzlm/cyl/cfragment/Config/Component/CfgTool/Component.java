package com.yzlm.cyl.cfragment.Config.Component.CfgTool;

import android.content.Context;

import com.yzlm.cyl.cfragment.Config.MyDBHelper.CompoentHelper.ConfigDBHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by caoyiliang on 2016/11/28.
 */
public class Component {
    private ConfigDBHelper ComponentDBHelper;
    /* 未使用的组分名（WL注释）*/
    private static String[] Name_unused;
    /* 已使用的组分名（WL注释）*/
    private static String[] Name_used;

    /* 未使用的组分的组分地址（WL）*/
    private static String[] unusedAddr;
    /* 已使用的组分的组分地址（WL）*/
    private static String[] usedAddr;
    /* 未使用的组分的测量单元类型（WL）*/
    private static String[] unusedMeasCateg;
    /* 已使用的组分的测量单元类型（WL）*/
    private static String[] usedMeasCateg;

    /* 已使用组分主控板硬件信息*/
    private static String[] usedBoardType;
    /* 未使用组分主控板硬件信息*/
    private static String[] unusedBoardType;

    /* 使用单例模式，保证该类只有一个实例化对象（WL注释）*/
    private static Component sComponent = null;

    public static Component newInstance(Context context) {
        if (sComponent == null) {
            sComponent = new Component(context);
        }
        return sComponent;
    }

    private Component(Context context) {
        ComponentDBHelper = ConfigDBHelper.getInstance(context);
    }



    /**
     * 从数据库同步组分的配置信息（WL注释）
     */
    private void syncComponent( boolean allFlag) {
        List<Map> list = ComponentDBHelper.queryListMap("select * from Component", null);
        List<String> RS_used = new ArrayList<>();
        List<String> RS_unused = new ArrayList<>();

        /* 未使用与已使用的组分的组分地址和测量单元类型列表（WL）*/
        List<String> RS_usedAddr = new ArrayList<>();
        List<String> RS_unusedAddr = new ArrayList<>();
        List<String> RS_usedMeasCateg = new ArrayList<>();
        List<String> RS_unusedMeasCateg = new ArrayList<>();

        List<String> RS_usedBoardType = new ArrayList<>();
        List<String> RS_unusedBoardType = new ArrayList<>();

        for (Map item : list) {
            if ((int) item.get("used") == 1) {
                // 不是稀释背板的情况下，稀释背板不算常规组份
                if(isNotaModule(item) ||allFlag) {
                    RS_used.add(item.get("Name").toString());

                /*同步已使用的组分的组分地址和测量单元类型列表（WL）*/
                    RS_usedAddr.add(item.get("addr").toString());
                    RS_usedMeasCateg.add(item.get("measCategory").toString());
                    if(item.get("boardType")!=null) {
                        RS_usedBoardType.add(item.get("boardType").toString());
                    }
                }
            } else {
                // 不是稀释背板的情况下，稀释背板不算常规组份
                if((isNotaModule(item))||allFlag) {
                    RS_unused.add(item.get("Name").toString());

                /*同步未使用的组分的组分地址和测量单元类型列表（WL）*/
                    RS_unusedAddr.add(item.get("addr").toString());
                    RS_unusedMeasCateg.add(item.get("measCategory").toString());
                    if(item.get("boardType")!=null) {
                        RS_unusedBoardType.add(item.get("boardType").toString());
                    }
                }
            }
        }
        Name_used = RS_used.toArray(new String[RS_used.size()]);
        Name_unused = RS_unused.toArray(new String[RS_unused.size()]);

        /*同步未使用与已使用的组分的组分地址和测量单元类型数组（WL）*/
        usedAddr = RS_usedAddr.toArray(new String[RS_usedAddr.size()]);
        usedMeasCateg = RS_usedMeasCateg.toArray(new String[RS_usedMeasCateg.size()]);
        unusedAddr = RS_unusedAddr.toArray(new String[RS_unusedAddr.size()]);
        unusedMeasCateg = RS_unusedMeasCateg.toArray(new String[RS_unusedMeasCateg.size()]);

        usedBoardType =  RS_usedBoardType.toArray(new String[RS_usedBoardType.size()]);
        unusedBoardType =  RS_unusedBoardType.toArray(new String[RS_unusedBoardType.size()]);

    }

    /** 该组分不是模块
     * @param item
     *
     * @return
     */
    private boolean isNotaModule(Map item) {
        return !item.get("measCategory").toString().equals("5") && !item.get("measCategory").toString().equals("6");
    }

    /**
     * 获取未使用和已使用的组分配置信息（WL注释）
     */
    public List<String[]> getComponent() {
        syncComponent(false);
        List<String[]> RS = new ArrayList<>();

        RS.add(Name_unused);
        RS.add(Name_used);

        /*获取组分的组分地址和测量单元类型信息（WL）*/
        RS.add(unusedAddr);
        RS.add(usedAddr);
        RS.add(unusedMeasCateg);
        RS.add(usedMeasCateg);

        RS.add(unusedBoardType);
        RS.add(usedBoardType);

        return RS;
    }

    /**所有5721参数，包括稀释模块*/
    public List<String[]>getAll5721Component(){
        syncComponent(true);
        List<String[]> RS = new ArrayList<>();

        RS.add(Name_unused);
        RS.add(Name_used);

        /*获取组分的组分地址和测量单元类型信息（WL）*/
        RS.add(unusedAddr);
        RS.add(usedAddr);
        RS.add(unusedMeasCateg);
        RS.add(usedMeasCateg);

        RS.add(unusedBoardType);
        RS.add(usedBoardType);
        return RS;
    }



    /**
     * 获取该组分名是否存在于组分数据表中（WL注释）
     */
    public boolean Exist(String name) {
        List<Map> list = ComponentDBHelper.queryListMap("select * from Component where Name=?", new String[]{name});
        return (list.size() > 0);
    }

    /**
     * 增加该组分至组分数据表中（WL注释）
     */
    public void AddZf(String name, String addr, String measCategory,String boardType) {
        Map<String, Object> map = new HashMap<>();
        map.put("Name", name);
        map.put("used", false);
        /* 添加组分的组分地址和测量单元类型（WL）*/
        map.put("addr", addr);
        map.put("measCategory", measCategory);

        map.put("boardType", boardType);

        ComponentDBHelper.insert("Component", map);
    }

    /**
     * 修改该组分的组分名为newName至组分数据表中（WL注释）
     */
    public void UpdateZfName(String newName, String Name) {
        Map<String, Object> columnValues = new HashMap<>();
        columnValues.put("Name", newName);
        Map<String, String> whereParam = new HashMap<>();
        whereParam.put("Name", Name);
        ComponentDBHelper.update("Component", columnValues, whereParam);
    }

    /**
     * 修改Name组分的使用标志值至组分数据表中（WL注释）
     */
    public void UpdateZfUsed(String Name, boolean Used) {
        Map<String, Object> columnValues = new HashMap<>();
        columnValues.put("used", Used);
        Map<String, String> whereParam = new HashMap<>();
        whereParam.put("Name", Name);
        ComponentDBHelper.update("Component", columnValues, whereParam);
    }

    /**
     * 从组分数据表中删除Name组分（WL注释）
     */
    public void DeleteZf(String Name) {
        Map<String, String> whereParam = new HashMap<>();
        whereParam.put("Name", Name);
        ComponentDBHelper.delete("Component", whereParam);
    }
}
