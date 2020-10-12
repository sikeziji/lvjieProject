package com.yzlm.cyl.cfragment.Communication.TCP;

import com.yzlm.cyl.cfragment.Communication.Communication;


public class TcpPort extends Communication {

    private SocketConnect myTcp;

    public TcpPort(String ip, int port) {
        try {
            myTcp = new SocketConnect(ip, port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public <T> void sendData(T data) {
        myTcp.sendData(data);
    }

    @Override
    public <T> T receiveData(T data) {
        return myTcp.Receive(data);
    }

    public void close() {
        myTcp.disconnect();
    }

    @Override
    public boolean isClose() {
        return false;
    }

}
