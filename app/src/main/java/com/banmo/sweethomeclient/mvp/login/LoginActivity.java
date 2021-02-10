package com.banmo.sweethomeclient.mvp.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.banmo.sweethomeclient.R;
import com.banmo.sweethomeclient.mvp.regist.RegistActivity;

public class LoginActivity extends AppCompatActivity implements ILoginView {

    private EditText mailEt;
    private EditText pwdEt;
    private Button loginBtn;
    private Button registSwitchBtn;

    private ILoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mailEt = findViewById(R.id.activity_login_mail_et);
        pwdEt = findViewById(R.id.activity_login_pwd_et);
        loginBtn = findViewById(R.id.activity_login_sign_btn);
        registSwitchBtn = findViewById(R.id.activity_login_registswitch_btn);

        loginPresenter = new LoginPresenterImpl(this);
        initBtnListener();
    }


    void initBtnListener() {
        loginBtn.setOnClickListener(v -> loginPresenter.doLogin(mailEt.getText().toString(), pwdEt.getText().toString()));
        registSwitchBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegistActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            overridePendingTransition(R.anim.top_to_bottom, R.anim.bottom_to_top);
        });
    }

    @Override
    public void onLoginResult(Boolean result) {
        loginBtn.setEnabled(true);
        registSwitchBtn.setEnabled(true);
        if (result) {
            Toast.makeText(this, "登陆成功", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, "登录失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.top_to_bottom, R.anim.bottom_to_top);
    }

    @Override
    public void onClearText() {
        mailEt.setText("");
        pwdEt.setText("");
    }

}