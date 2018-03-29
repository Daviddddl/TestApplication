package com.example.didonglin.testapplication;

import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class BookActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        Button testButton = findViewById(R.id.bt_content_book1);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String res = submitDataByDoGet();
                    System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" + res + "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public String submitDataByDoGet(/*Map<String, String> map, String path*/) throws Exception {


        new Thread() {
            public void run() {

                try {
        /*// 拼凑出请求地址
        StringBuilder sb = new StringBuilder(path);
        sb.append("?");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue());
            sb.append("&");
        }
        sb.deleteCharAt(sb.length() - 1);
        String str = sb.toString();
        System.out.println(str);
        URL Url = new URL(str);*/

                    String urlstr = "https://www.doveminr.com/smartQA/stuoperate/listMyCourse?userid=12";
                    URL url = new URL(urlstr);
                    HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
                    httpConn.setRequestMethod("GET");
                    httpConn.setReadTimeout(5000);
                    httpConn.setDoOutput(true);
                    httpConn.setDoInput(true);
                    httpConn.setRequestProperty("Content-type", "application/x-java-serialized-object");

        /*OutputStream outputStream = httpConn.getOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("121231234");

        objectOutputStream.writeObject(stringBuffer);

        objectOutputStream.flush();

        httpConn.getInputStream();*/

                    InputStream inputStream = httpConn.getInputStream();

                    StringBuffer stringBuffer = new StringBuffer();
                    String line;
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    while ((line = reader.readLine()) != null) {
                        stringBuffer.append(line);
                    }

                    System.out.println(stringBuffer.toString());
                } catch (Exception e) {
                    System.out.println("Error!!!");
                }
            }
        }.start();
        return null;
    }
}
