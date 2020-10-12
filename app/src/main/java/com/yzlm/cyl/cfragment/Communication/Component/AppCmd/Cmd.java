package com.yzlm.cyl.cfragment.Communication.Component.AppCmd;

/**
 * Created by caoyiliang on 2017/2/10.
 */

public class Cmd<T> {
    //        Class.forName("java.lang.String")
    //        classType.newInstance();
    private int Addr;
    private String Name;
    private T value;
    private String Type;
    private int Size;
    private int Len = 1;
    private boolean isSave = false;
    public SaveType saveType;

    public enum SaveType {
        config,
        Log,
        value
    }

    public Cmd(int Addr, String Name, String type, int size) {
        this.Addr = Addr;
        this.Name = Name;
        this.Type = type;
        this.Size = size / 8;
    }

    public int getAddr() {
        return Addr;
    }

    public String getName() {
        return Name;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public String getType() {
        return Type;
    }

    public int getLen() {
        return Len;
    }

    public void setLen(int len) {
        Len = len;
    }

    public int getSize() {
        return Size;
    }

    public void setSave(boolean save) {
        isSave = save;
    }

    public boolean isSave() {
        return isSave;
    }
}
