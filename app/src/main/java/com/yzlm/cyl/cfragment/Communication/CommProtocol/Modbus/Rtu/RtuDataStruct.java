package com.yzlm.cyl.cfragment.Communication.CommProtocol.Modbus.Rtu;

import java.io.Serializable;

/**
 * Created by zwj on 2017/12/20.
 */

public class RtuDataStruct implements Serializable {

    private int regAddr;
    private int dataLen;
    private String describe;
    private String rw;
    private String data;
    private String dataType;/*数据类型 Date FLOAT WORD*/
    private String strUp;
    private String strDown;


    /**
     * *设置寄存器值
     */
    public void setData(String data) {
        this.data = data;
    }

    /**
     * 获取数据类型
     */
    public String getDataType() {
        return dataType;
    }

    /**
     * 设置数据类型  FLOAT  WORD  DWORD  Date
     */
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    /**
     * 获取寄存器中的数据
     */
    public String getData() {
        return data;
    }

    /**
     * 设置数据长度
     */
    public void setDataLen(int dataLen) {
        this.dataLen = dataLen;
    }

    /**
     * 设置寄存器内容描述
     */
    public void setDescribe(String describe) {
        this.describe = describe;
    }

    /**
     * 设置寄存器地址
     */
    public void setRegAddr(int regAddr) {
        this.regAddr = regAddr;
    }

    /**
     * 设置数据长度
     */
    public int getDataLen() {
        return dataLen;
    }

    /**
     * 获取寄存器地址
     */
    public int getRegAddr() {
        return regAddr;
    }

    /**
     * 获取寄存器内容描述
     */
    public String getDescribe() {
        return describe;
    }

    /**
     * 设置寄存器变量读写权限
     */
    public void setRw(String rw) {
        this.rw = rw;
    }

    /**
     * 获取读写权限
     */
    public String getRw() {
        return rw;
    }


    /**
     * 设置上限
     **/
    public void setUp(String up) {
        this.strUp = up;
    }

    /**
     * 设置下限
     **/
    public void setDown(String down) {
        this.strDown = down;
    }

    /**
     * 获取上限
     **/
    public String getUp() {
        return strUp;
    }

    /**
     * 获取下限
     **/
    public String getDown() {
        return strDown;
    }

    @Override
    public String toString() {
        return "RtuDataStruct{" +
                "regAddr=" + regAddr +
                ", dataLen=" + dataLen +
                ", describe='" + describe + '\'' +
                ", rw='" + rw + '\'' +
                ", data='" + data + '\'' +
                ", dataType='" + dataType + '\'' +
                ", strUp='" + strUp + '\'' +
                ", strDown='" + strDown + '\'' +
                '}';
    }
}
