package com.yzlm.cyl.cfragment.Communication.CommProtocol.Modbus.RTU2018;


import com.yzlm.cyl.cfragment.Communication.CommProtocol.Modbus.Rtu.RtuDataStruct;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by zwj on 2017/12/20.
 */

@SuppressWarnings("unchecked")
public class ModbusRtuTable {

    private Map<Integer, RtuDataStruct> LModbusRweReg = new HashMap();
    private String mapInfo;

    /**
     * 存储Map的信息
     */
    public void setMapInfo(String info) {
        this.mapInfo = info;

    }

    /**
     * 获取Map的信息
     */
    public String getMapInfo() {
        return mapInfo;
    }

    public void Add(RtuDataStruct data) {
        LModbusRweReg.put(data.getRegAddr(), data);
    }

    public RtuDataStruct getRegObj(int reg) {
        if (LModbusRweReg.containsKey(reg)) {
            return LModbusRweReg.get(reg);
        } else {
            return null;
        }
    }

    public int getSize() {
        return LModbusRweReg.size();
    }

    public Map<Integer, RtuDataStruct> getLModbusRweReg() {
        return this.LModbusRweReg;
    }

    @Override
    public String toString() {
        return "ModbusRtuTable{" +
                "LModbusRweReg=" + HashMapToString(LModbusRweReg) +
                ", mapInfo='" + mapInfo + '\'' +
                '}';
    }


    private String HashMapToString(Map hashMap) {
        Set<Map.Entry> ms = hashMap.entrySet();
        for (Map.Entry entry : ms) {
            System.out.print("Key = " + entry.getKey() + ":" + " value = " + entry.getValue());
        }
        return "已打印";
    }

}
