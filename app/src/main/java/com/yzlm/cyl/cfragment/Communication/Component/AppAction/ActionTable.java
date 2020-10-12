package com.yzlm.cyl.cfragment.Communication.Component.AppAction;

import com.yzlm.cyl.cfragment.R;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.yzlm.cyl.cfragment.Global.context;

/**
 * Created by caoyiliang on 2017/2/18.
 */

@SuppressWarnings("unchecked")
public class ActionTable {
    private Map<String, Action> LAction = new LinkedHashMap();

    public void Add(String Name, Action action) {
        LAction.put(Name, action);
    }

    public Map<String, Action> getLAction() {
        return LAction;
    }

    public Action getAction(String Name) {
        return LAction.get(Name);
    }

    public String GetActionName(Object value) {
        try {
            short action = (short) value;

            if (action == 0) return context.getString(R.string.ready);//"准备";
            for (Map.Entry<String, Action> stringActionEntry : LAction.entrySet()) {
                Map.Entry entry = (Map.Entry) stringActionEntry;
                Action ac = (Action) entry.getValue();
                if (ac.getCmd() == action) return ac.getName();
            }
        } catch (Exception ex) {
            ex.getMessage();
        }
        return "——————";
    }

    public String GetActionCode(Object value) {
        try {
            short action = (short) value;
            for (Map.Entry<String, Action> stringActionEntry : LAction.entrySet()) {
                Map.Entry entry = (Map.Entry) stringActionEntry;
                Action ac = (Action) entry.getValue();
                if (ac.getCmd() == action) return ac.getCode();
            }
        } catch (Exception ex) {
            ex.getMessage();
        }
        return "——————";
    }


    /** 根据动作名称，获取命令编码（动作号）
     * @param value     名称
     * @return
     */
    public short getActionCmd(Object value) {
        try {
            String actionName = (String) value;
            for (Map.Entry<String, Action> stringActionEntry : LAction.entrySet()) {
                Map.Entry entry = (Map.Entry) stringActionEntry;
                Action ac = (Action) entry.getValue();
                if (ac.getName().equals(actionName)) return ac.getCmd();
            }
        } catch (Exception ex) {
            ex.getMessage();
        }
        return 0;
    }


}
