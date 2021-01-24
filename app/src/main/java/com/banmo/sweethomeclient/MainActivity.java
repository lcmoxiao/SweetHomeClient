package com.banmo.sweethomeclient;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.banmo.sweethomeclient.client.ConnectorClient;
import com.banmo.sweethomeclient.client.UserFunction;
import com.banmo.sweethomeclient.regist.RegistActivity;

public class MainActivity extends AppCompatActivity {

    private EditText mailEt;
    private EditText pwdEt;
    private EditText recheckpwd;
    private Button signBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        {
            mailEt = findViewById(R.id.activity_regist_mail_et);
        }

        {
            pwdEt = findViewById(R.id.activity_regist_pwd_et);
        }

        {
            recheckpwd = findViewById(R.id.activity_regist_recheckpwd_et);
        }

        {
            signBtn = findViewById(R.id.activity_regist_sign_btn);
        }


        signBtn.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), RegistActivity.class)));
    }
}