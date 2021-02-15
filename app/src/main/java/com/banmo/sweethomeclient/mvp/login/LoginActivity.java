package com.banmo.sweethomeclient.mvp.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.banmo.sweethomeclient.R;
import com.banmo.sweethomeclient.databinding.ActivityLoginBinding;
import com.banmo.sweethomeclient.mvp.regist.RegistActivity;

public class LoginActivity extends AppCompatActivity implements ILoginView {

    private ActivityLoginBinding inflate;

    private ILoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        inflate = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_login);

        loginPresenter = new LoginPresenterImpl(this);
        initBtnListener();
    }


    void initBtnListener() {
        inflate.activityLoginSignBtn.setOnClickListener(v -> {
            String mail = inflate.activityLoginMailEt.getText().toString();
            String pwd = inflate.activityLoginPwdEt.getText().toString();
            loginPresenter.doLogin(mail, pwd);
        });
        inflate.activityLoginRegistswitchBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegistActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            overridePendingTransition(R.anim.top_to_bottom, R.anim.bottom_to_top);
        });
    }

    @Override
    public void onLoginResult(Boolean result) {
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
        inflate.activityLoginMailEt.setText("");
        inflate.activityLoginPwdEt.setText("");
    }

}