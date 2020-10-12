package com.yzlm.cyl.cfragment.Communication.SerialPort;

import com.yzlm.cyl.cfragment.Communication.Communication;
import com.yzlm.cyl.cfragment.Communication.SerialPort.ZserialPort.Z_SerialPort;


/**
 * Created by caoyiliang on 2017/1/18.
 */
public class WeiQianPort extends Communication {
    private Z_SerialPort sp;
    private boolean blSpEnable = false;

    public WeiQianPort(String devNum, int speed, int dataBits, int stopBits, String parity) {
        sp = new Z_SerialPort();
        sp.open(devNum, speed, dataBits, parity, stopBits);
        blSpEnable = true;

    }

    public void close() {
        sp.close();
        blSpEnable = false;
    }

    @Override
    public boolean isClose() {
        return !blSpEnable;
    }


    @Override
    public <T> void sendData(T data) {
        sp.sendData(data);
    }

    @Override
    public <T> T receiveData(T data) {
        return sp.receiveData(data);
    }
}
