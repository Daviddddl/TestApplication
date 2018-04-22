package com.example.didonglin.testapplication;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MyChatActivity extends AppCompatActivity {

    private Button button;
    //private Button button2;
    private Button cancelbutton;
    private EditText editText;

    //private TextView history;
    private MsgAdapter adapter;
    private ListView listView;
    private List<Msg> mMsgList;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private ServerSocket mServerSocket;
    private TextView ipView;
    private TextView portView;
    private TextView bindPortView;

    private String serverIp = "192.168.2.16";
    private int serverPort = 7777;
    private int bindPort = 2555;

    //handler
    private Handler handler;

    private OutputStream outputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_chat);

        handler = new Handler();

        //初始化界面组件
        ipView = findViewById(R.id.ipView);
        portView = findViewById(R.id.portView);
        bindPortView = findViewById(R.id.bindPortView);
        listView = findViewById(R.id.listView);
        editText = findViewById(R.id.inputView);
        button = findViewById(R.id.inputButton);
        //button2 = findViewById(R.id.button2);
        mMsgList = new ArrayList<>();
        adapter = new MsgAdapter(this,mMsgList);
        //history.setMovementMethod(ScrollingMovementMethod.getInstance());
        listView.setAdapter(adapter);
        button = findViewById(R.id.inputButton);
        cancelbutton = findViewById(R.id.cancelButton);
        editText = findViewById(R.id.inputView);

        //设置ip端口和监听端口
        //获得传参过来的ip和port
        Intent intent = getIntent();
        if(intent != null){
            serverIp = intent.getStringExtra("ip");
            serverPort = Integer.parseInt(intent.getStringExtra("port"));
            bindPort = Integer.parseInt(intent.getStringExtra("bindPort"));

            /*ipView.setText("我的ip:" + serverIp);
            portView.setText("开放端口:"+ serverPort);
            bindPortView.setText("绑定端口:" + bindPort);*/
        }

       /* button2.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {
                                           ipView.setError("此处不能为空");
                                           portView.setError("此处不能为空");
                                           bindPortView.setError("此处不能为空");
                                       }
                                   }

        );*/

        cancelbutton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                editText.setText("");
                                            }
                                        }

        );

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String editMessage = editText.getText().toString();
                editText.setText("");
                try {
                    Thread thread = new Thread(){

                        @Override
                        public void run() {
                            //新建socket连接
                            Log.e("aaa", "新建mainRun");
                            try {
                                Socket socket = new Socket(serverIp, serverPort);
                                outputStream = socket.getOutputStream();

                                //将用户输入通过socket发送
                                String input = editMessage;

                                //显示到界面
                                //Msg msg = new Msg(input,Msg.TYPE_SEND,formatter.format(new Date()));
                                Msg msg = new Msg(input,Msg.TYPE_SEND,"");


                                Message message = new Message();
                                message.what = 2;

                                Bundle bundle = new Bundle();
                                bundle.putString("msg", msg.getText());

                                message.setData(bundle);

                                handler.sendMessage(message);

                                Log.e("run", "editText内容:" +  input);
                                outputStream.write(input.getBytes());
                                outputStream.flush();


//                                sleep(1000);
                                socket.close();
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    };
                    thread.start();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Log.e("handler","继续执行handler");

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
//                super.handleMessage(msg);
                Log.e("MainActivity","处理消息的回调函数" + msg.getData());
//                editText.setText(msg.getData().getString("msg"));
//                Toast.makeText(MainActivity.this,msg.getData().getString("msg"),Toast.LENGTH_SHORT);
//                editText.invalidate();

                Msg message = new Msg(msg.getData().getString("msg"),Msg.TYPE_RECEIVED,formatter.format(new Date()));
                switch (msg.what){
                    case 1:
                        message.setType(Msg.TYPE_RECEIVED);
                        break;
                    case 2:
                        message.setType(Msg.TYPE_SEND);
                        break;
                }
                //显示到界面

                mMsgList.add(message);
                adapter.refresh(mMsgList);
            }
        };

        try {
            //创建接收消息Socket
            ReceiverSocket receiverSocket = new ReceiverSocket(handler,serverIp,bindPort,new SharedArea(true));
            receiverSocket.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



}
