package com.example.didonglin.testapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SettingsActivity extends AppCompatActivity {

    private EditText ipText;
    private EditText portText;
    private EditText bindPortText;

    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ipText = findViewById(R.id.ipText);
        portText = findViewById(R.id.portText);
        bindPortText = findViewById(R.id.bindPortText);

        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String ip = ipText.getText().toString();
                final String port = portText.getText().toString();
                final String bindPort = bindPortText.getText().toString();

                Intent intent = new Intent();
                intent.setClass(SettingsActivity.this,MainActivity.class);
                intent.putExtra("ip",ip);
                intent.putExtra("port",port);
                intent.putExtra("bindPort",bindPort);
                startActivity(intent);
            }
        });
    }
}
