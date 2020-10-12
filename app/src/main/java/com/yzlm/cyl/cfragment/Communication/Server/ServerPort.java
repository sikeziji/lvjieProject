package com.yzlm.cyl.cfragment.Communication.Server;

import com.yzlm.cyl.cfragment.Communication.Communication;

/**
 * Create by duyang
 * Create on 2020/9/21
 * description
 */
public class ServerPort extends Communication {

    private ServerConnect serverConnect;

    public ServerPort(int port) {
        try {
            serverConnect = new ServerConnect(port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public <T> void sendData(T data) {
        serverConnect.sendData(data);
    }

    @Override
    public <T> T receiveData(T data) {
        return serverConnect.Receive(data);
    }

    @Override
    public void close() {
        serverConnect.disconnect();
    }

    @Override
    public boolean isClose() {
        return false;
    }
}
