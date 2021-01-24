package com.banmo.sweethomeclient.regist;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.banmo.sweethomeclient.R;
import com.banmo.sweethomeclient.login.LoginActivity;

public class RegistActivity extends AppCompatActivity implements IRegistView {

    private EditText mailEt;
    private EditText pwdEt;
    private EditText reCheckPwdEt;
    private Button signBtn;
    private Button loginSwitchBtn;

    private IRegistPresenter registPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        mailEt = findViewById(R.id.activity_regist_mail_et);
        pwdEt = findViewById(R.id.activity_regist_pwd_et);
        reCheckPwdEt = findViewById(R.id.activity_regist_recheckpwd_et);
        signBtn = findViewById(R.id.activity_regist_sign_btn);
        loginSwitchBtn = findViewById(R.id.activity_regist_loginswitch_btn);

        registPresenter = new RegistPresenterImpl(this);
        initBtnListener();
    }


    void initBtnListener() {
        signBtn.setOnClickListener(v -> {
            if (pwdEt.getText().toString().equals(reCheckPwdEt.getText().toString())) {
                registPresenter.doRegist(mailEt.getText().toString(), pwdEt.getText().toString());
            } else {
                Toast.makeText(this, "两次输入的密码不一致，请重新输入密码", Toast.LENGTH_SHORT).show();
                pwdEt.setText("");
                reCheckPwdEt.setText("");
            }
        });
        loginSwitchBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }

    @Override
    public void onRegistResult(Boolean result) {
        signBtn.setEnabled(true);
        loginSwitchBtn.setEnabled(true);
        if (result) {
            Toast.makeText(this, "Regist Success", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Regist Fail", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClearText() {
        mailEt.setText("");
        pwdEt.setText("");
        reCheckPwdEt.setText("");
    }
}