package com.example.didonglin.testapplication;

import android.util.Log;
import android.widget.Button;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * Created by zchai on 2018/4/11.
 */

public class SenderSocket extends Thread{

    //接收消息socket\
    private Socket socket;

    //共享数据区域
    private SharedArea sharedArea;

    //handler
    private Button button;

    private String serverIp;

    private int serverPort;

    public SenderSocket(Button button, String serverIp, int serverPort, SharedArea sharedArea) throws IOException {
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        this.button = button;
        this.sharedArea = sharedArea;
    }

    @Override
    public void run() {
//        super.run();

        try {
            socket = new Socket(serverIp,serverPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.e("ReceiverSocket","接收线程启动");
        byte[] reveivedMsg = new byte[1024];

        while (sharedArea.flag){

            try {

                Log.e("RS","开始循环");
                InputStream inputStream = socket.getInputStream();
                inputStream.read(reveivedMsg);
                String msg = new String(reveivedMsg);
//
//                Message message = new Message();
//                message.what = 1;
//
//                Bundle bundle = new Bundle();
//                bundle.putString("msg", msg);
//
//                message.setData(bundle);
//
//                handler.sendMessage(message);

                sharedArea.flag = false;
            } catch (IOException e) {

                System.out.println("ReceiverSocket.run获得socketInputStream异常");
                e.printStackTrace();
            }
        }
    }
}
