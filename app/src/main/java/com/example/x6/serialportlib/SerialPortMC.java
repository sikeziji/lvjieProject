package com.example.x6.serialportlib;

import android.content.Context;

import com.yzlm.cyl.cfragment.Communication.Communication;

import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;


public class SerialPortMC extends Communication {
    Context context;

    private SerailPortOpt serialportopt;
    private InputStream mInputStream;
    public boolean isOpen = false;


    String data;

    public void close() {
        closeSerial();
    }

    @Override
    public boolean isClose() {
        return !isOpen;
    }

    public SerialPortMC(String devNum, int speed, int dataBits, int stopBits,
                        int parity) {
        serialportopt = new SerailPortOpt();
        openSerial(devNum, speed, dataBits, stopBits, parity);
    }


    private boolean openSerial(String devNum, int speed, int dataBits,
                               int stopBits, int parity) {
        serialportopt.mDevNum = devNum;
        serialportopt.mDataBits = dataBits;
        serialportopt.mSpeed = speed;
        serialportopt.mStopBits = stopBits;
        serialportopt.mParity = parity;


        FileDescriptor fd = serialportopt.openDev(serialportopt.mDevNum);
        if (fd == null) {
            return false;
        } else {

            serialportopt.setSpeed(fd, speed);
            serialportopt.setParity(fd, dataBits, stopBits, parity);
            mInputStream = serialportopt.getInputStream();
            isOpen = true;
            return true;
        }
    }


    public void closeSerial() {
        if (serialportopt.mFd != null) {
            serialportopt.closeDev(serialportopt.mFd);
            isOpen = false;
        }
    }


    public void sendData(String data, String type) {
        try {
            serialportopt.writeBytes(type.equals("HEX") ? HexString2Bytes(data
                    .length() % 2 == 1 ? data += "0" : data.replace(" ", ""))
                    : HexString2Bytes(toHexString(data)));
        } catch (Exception e) {

        }
    }


    public String receiveData(String type) {
        byte[] buf = new byte[1024];
        int size;
        if (mInputStream == null) {
            return null;
        }
        size = serialportopt.readBytes(buf);
        if (size > 0) {
            try {
                data = type.equals("HEX") ? bytesToHexString(buf, size)
                        : new String(buf, 0, size, "gb2312").trim().toString();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return data;
        } else {
            return null;
        }
    }


    private String toHexString(String s) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = (int) s.charAt(i);
            String s4 = Integer.toHexString(ch);
            str = str + s4;
        }
        return str;
    }


    private static byte[] HexString2Bytes(String src) {
        byte[] ret = new byte[src.length() / 2];
        byte[] tmp = src.getBytes();
        for (int i = 0; i < tmp.length / 2; i++) {
            ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
        }
        return ret;
    }


    public static String bytesToHexString(byte[] src, int size) {
        String ret = "";
        if (src == null || size <= 0) {
            return null;
        }
        for (int i = 0; i < size; i++) {
            String hex = Integer.toHexString(src[i] & 0xFF);
            if (hex.length() < 2) {
                hex = "0" + hex;
            }
            hex += " ";
            ret += hex;
        }
        return ret.toUpperCase();
    }


    private static byte uniteBytes(byte src0, byte src1) {
        byte _b0 = Byte.decode("0x" + new String(new byte[]{src0}))
                .byteValue();
        _b0 = (byte) (_b0 << 4);
        byte _b1 = Byte.decode("0x" + new String(new byte[]{src1}))
                .byteValue();
        byte ret = (byte) (_b0 ^ _b1);
        return ret;
    }

    @Override
    public <T> void sendData(T data) {
        try {
            serialportopt.writeBytes((byte[]) data);
        } catch (Exception e) {

        }
    }

    @Override
    public <T> T receiveData(T data) {
        if (mInputStream == null) {
            return null;
        }
        int num = 1024;
        try {
            num = mInputStream.available();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] buffer = new byte[num];
        int size;
        size = serialportopt.readBytes(buffer);
        if (size > 0) {
            try {
                data = (T) buffer;
                //data = (T) (new String(buffer, 0, size, "gb2312")).toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
//            catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
            return data;
        } else {
            return null;
        }
    }
}
