package com.banmo.sweethomeclient.login;

import android.os.Handler;
import android.os.Looper;

import com.banmo.sweethomeclient.login.ILoginView;

class LoginPresenterImpl implements ILoginPresenter {
    private ILoginView loginView; //视图

    private Handler handler;

    public LoginPresenterImpl(ILoginView loginView) {
        this.loginView = loginView;
        handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void doLogin(String mail, String pwd) {
        boolean result = checkLogin(mail, pwd);
        if (!result) clear();
        handler.postDelayed(() -> loginView.onLoginResult(result), 3000);
    }

    private boolean checkLogin(String mail, String pwd) {
        return mail.equals("a") && pwd.equals("1");
    }

    @Override
    public void clear() {
        loginView.onClearText();
    }
}
