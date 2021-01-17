package com.banmo.sweethomeclient;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.banmo.sweethomeclient.client.ConnectorClient;
import com.banmo.sweethomeclient.client.UserFunction;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        new Thread() {
            @Override
            public void run() {
                ConnectorClient.connect();
            }
        }.start();

        findViewById(R.id.btn1).setOnClickListener(v -> {
            UserFunction.sendTestConnectMessage(1);
        });
        findViewById(R.id.btn2).setOnClickListener(v -> {
            UserFunction.sendTestTransMessage(1, 2);
        });


    }
}