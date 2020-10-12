package com.yzlm.cyl.cfragment.Communication.SerialPort.Interface;

/**
 * Created by caoyiliang on 2017/1/18.
 * 串口接口隔离
 */

public interface Port {
    boolean isOpen();

    void close();
}
