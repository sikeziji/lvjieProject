package com.yzlm.cyl.cfragment.Communication.Server;

import android.util.Log;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import static com.yzlm.cyl.clibrary.Util.DataUtil.bytesToHexString;

/**
 * Create by duyang
 * Create on 2020-09-03
 * description
 */
public class ServerConnect {

    public Map<String, Socket> listSocket = new HashMap<>();
    private ServerSocket server = null;
    private Socket socket = null;
    private OutputStream os = null;
    private DataInputStream is = null;
    private int port;

    public ServerConnect(final int port) {

        this.port = port;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    server = new ServerSocket(port);
                    socket = server.accept();//等待连接
                    Log.d("net", "服务端口：" + port + "已启动，等待连接。。。");
                    try {
                        Log.d("net", "IP:" + socket.getInetAddress().getHostAddress() + "已上线");
                        while (true) {
                            listSocket.put(socket.getInetAddress().getHostAddress(), socket);
                        }
                    } finally {
                        socket.close();
                        server.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }


    /*
    发送数据
    * */
    <T> void sendData(T data) {
        try {
            if (socket == null || data == null) {
                Log.i("Server Send", "Server:Socket null ");
                return;
            }
            Log.i("sendData", bytesToHexString((byte[]) data, ((byte[]) data).length));
            if (socket.isConnected()) {
                // 步骤1：从Socket 获得输出流对象OutputStream
                // 该对象作用：发送数据
                os = socket.getOutputStream();
                // 步骤2：写入需要发送的数据到输出流对象中
                os.write((byte[]) data);
                // 特别注意：数据的结尾加上换行符才可让服务器端的readline()停止阻塞
                // 步骤3：发送数据到服务端
                os.flush();
            } else {
                Log.i("Server Send", "socket 连接错误，请重试....");
            }
        } catch (Exception e) {
            Log.e("Server Send", "Server:Socket Exception " + e.toString());
        }
    }


    /*
    接收客戶端数据
    * */
    <T> T Receive(T data) {
        try {
            if (socket != null) {
                is = new DataInputStream(socket.getInputStream());//读取客户端传来的信息
                byte[] buf = new byte[4096];
                if (socket.isConnected()) {
                    int size = is.read(buf);
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
                            Log.e("exception", var5.toString());
                        }
                        if (data instanceof byte[]) {
                            Log.i("Server Receive", "接收到" + socket.getInetAddress().getHostAddress() + "发送的数据" + bytesToHexString((byte[]) data, ((byte[]) data).length));
                            for (int i = 0; i < ((byte[]) data).length; i++) {
                                System.out.print(i + "\t");
                            }
                        }
                        return data;
                    } else {
                        return null;
                    }
                }
            }
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
        return null;
    }

    <T> T disconnect() {
        try {
            if (socket != null) {
                if (!socket.isClosed()) {
                    if (!socket.isInputShutdown()) {
                        socket.shutdownInput();
                    }
                    if (!socket.isOutputShutdown()) {
                        socket.shutdownOutput();
                    }
                    if (is != null) {
                        is.close();
                        is = null;
                    }
                    if (os != null) {
                        os.close();
                        os = null;
                    }
                    socket.close();
                }
                socket = null;
                Log.e("log", "onDisconnected");
            }
        } catch (Exception e) {
            Log.e("exception", "onError");
        }
        return null;
    }

}
