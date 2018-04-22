package com.example.didonglin.testapplication;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class ReceiverSocket extends Thread {

    //接收消息socket\
    private ServerSocket serverSocket;

    //共享数据区域
    private SharedArea sharedArea;

    //handler
    private Handler handler;

    private String serverIp;

    private int serverPort;

    public ReceiverSocket(Handler handler, String serverIp, int serverPort, SharedArea sharedArea) throws IOException {
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        this.handler = handler;
        this.sharedArea = sharedArea;
    }

    @Override
    public void run() {
//        super.run();

        try {
            serverSocket = new ServerSocket(serverPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.e("ReceiverSocket","接收线程启动");

        while (true){

            byte[] msgByte = new byte[1024];
            try {

                Log.e("RS","开始循环");
                Socket socket = serverSocket.accept();
                InputStream inputStream = socket.getInputStream();
                inputStream.read(msgByte);

                String msg = new String(msgByte);
                msg = msg.trim();
                Log.e("aaa", msg);

                Message message = new Message();
                message.what = 1;

                Bundle bundle = new Bundle();
                bundle.putString("msg", msg);

                message.setData(bundle);

                handler.sendMessage(message);

//                sharedArea.flag = false;
            } catch (IOException e) {

                System.out.println("ReceiverSocket.run获得socketInputStream异常");
                e.printStackTrace();
            }
        }
    }

}