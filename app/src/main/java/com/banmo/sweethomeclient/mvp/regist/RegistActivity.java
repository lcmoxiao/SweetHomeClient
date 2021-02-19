package com.banmo.sweethomeclient.mvp.regist;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.banmo.sweethomeclient.R;
import com.banmo.sweethomeclient.client.ConnectorClient;
import com.banmo.sweethomeclient.mvp.login.LoginActivity;

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
            if (ConnectorClient.getChannel() == null) {
                Toast.makeText(this, "网络未连接", Toast.LENGTH_SHORT).show();
            } else {
                if (pwdEt.getText().toString().equals(reCheckPwdEt.getText().toString())) {
                    registPresenter.doRegist(mailEt.getText().toString(), pwdEt.getText().toString());
                } else {
                    Toast.makeText(this, "两次输入的密码不一致，请重新输入密码", Toast.LENGTH_SHORT).show();
                    pwdEt.setText("");
                    reCheckPwdEt.setText("");
                }
            }
        });
        loginSwitchBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            overridePendingTransition(R.anim.top_to_bottom, R.anim.bottom_to_top);
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.top_to_bottom, R.anim.bottom_to_top);
    }

    @Override
    public void onRegistResult(Boolean result) {
        signBtn.setEnabled(true);
        loginSwitchBtn.setEnabled(true);
        if (result) {
            Toast.makeText(this, "注册成功，请登录", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else {
            Toast.makeText(this, "邮箱已注册，注册失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClearText() {
        mailEt.setText("");
        pwdEt.setText("");
        reCheckPwdEt.setText("");
    }
}