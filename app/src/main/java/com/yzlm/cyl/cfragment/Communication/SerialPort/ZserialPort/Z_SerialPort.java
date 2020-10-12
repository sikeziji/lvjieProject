package com.yzlm.cyl.cfragment.Communication.SerialPort.ZserialPort;

import java.io.UnsupportedEncodingException;

import weiqian.hardware.SerialPort;

/**
 * Created by zwj on 2017/8/7.
 */

@SuppressWarnings("unchecked")
public class Z_SerialPort extends SerialPort {

    public <T> T receiveData(T data) {
        byte[] buf = new byte[1024];
        int size = this.read(buf, buf.length);
        if (size > 0) {
            try {
                if (data instanceof byte[]) {
                    byte[] e = new byte[size];
                    System.arraycopy(buf, 0, e, 0, size);
                    data = (T) e;
                } else {
                    data = (T) (new String(buf, 0, size, "gb2312")).toString();
                }
            } catch (UnsupportedEncodingException var5) {
                var5.printStackTrace();
            }
            return data;
        } else {
            return null;
        }
    }
}
