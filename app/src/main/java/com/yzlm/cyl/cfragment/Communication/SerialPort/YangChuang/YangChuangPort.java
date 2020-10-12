package com.yzlm.cyl.cfragment.Communication.SerialPort.YangChuang;

import com.ychmi.sdk.YcApi;
import com.yzlm.cyl.cfragment.Communication.Communication;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class YangChuangPort extends Communication {
    private FileDescriptor fp;
    private FileInputStream mInputStream;
    private FileOutputStream mOutputStream;
    private ReadThread mReadThread;
    private byte[] Buffer = null;
    private boolean receive = false;
    YcApi ycapi;

    private boolean blSpEnable = false;

    public YangChuangPort(String devNum, int speed, int dataBits, int paritybit, int stopbit) {
        ycapi = new YcApi();
        fp = ycapi.openComNonBlock(devNum,speed,dataBits,paritybit,stopbit);
        //fp = ycapi.openCom("dev/ttyAMA1", 19200, 8, 1, 1);
        if (fp == null)
        {
            try
            {
                throw new IOException();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        else
        {
            mInputStream = new FileInputStream(fp);
            mOutputStream = new FileOutputStream(fp);
            blSpEnable = true;
//            mReadThread = new ReadThread();
//            mReadThread.start();
        }
    }

    public void close() {
        if (fp != null){
            ycapi.closeCom(fp);
            blSpEnable = false;
            fp = null;
        }
    }

    @Override
    public boolean isClose() {
        return !blSpEnable;
    }

    @Override
    public <T> void sendData(T data) {
        try {
            if(((byte[]) data).length < 3){
                String sss = "dasdasd";
            }
            mOutputStream.write((byte[]) data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public <T> T receiveData(T data) {
        int size;
        try
        {
            byte[] buffer = new byte[1024];
            if (mInputStream == null) return null;
            size = mInputStream.read(buffer);
            if (size > 0)
            {
                try {
                    if (data instanceof byte[]) {
                        byte[] e = new byte[size];
                        System.arraycopy(buffer, 0, e, 0, size);
                        data = (T) e;
                    } else {
                        data = (T) (new String(buffer, 0, size, "gb2312")).toString();
                    }
                    //data = (T) (new String(buffer, 0, size, "gb2312")).toString();
                } catch (Exception e){
                    e.printStackTrace();
                }
//                String sss = "";
//                for(int i=0;i<size;i++){
//                    sss += buffer[i] + " ";
//                }
//                Log.d("ddaaddaa1", sss);
                return data;

            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;

        //        try {
//            if(Buffer != null){
//                data = (T) Buffer;
//                Buffer = null;
//                return data;
//            }else {
//                return null;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
    }

    private class ReadThread extends Thread {

        @Override
        public void run() {
            super.run();
            while(!isInterrupted())
            {
                int size;
                    try
                    {
                        byte[] buffer = new byte[1024];
                        if (mInputStream == null) return;
                        size = mInputStream.read(buffer);
                        if (size > 0)
                        {
                            Buffer =  buffer;
//                            String sss = "";
//                            for(int i=0;i<size;i++){
//                                sss += buffer[i] + " ";
//                            }
//                            Log.d("ddaaddaa1", sss);
                        }
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                        return;
                    }
            }
        }
    }
}