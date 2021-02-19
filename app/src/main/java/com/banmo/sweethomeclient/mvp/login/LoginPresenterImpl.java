package com.banmo.sweethomeclient.mvp.login;

import android.os.Handler;
import android.os.Looper;

import com.banmo.sweethomeclient.client.ConnectorClient;
import com.banmo.sweethomeclient.client.UserInfos;
import com.banmo.sweethomeclient.client.service.LoginService;
import com.banmo.sweethomeclient.pojo.User;

import static com.banmo.sweethomeclient.tool.MsgGenerateTools.generateConnectMessage;

class LoginPresenterImpl implements ILoginPresenter {
    private final ILoginView loginView; //视图

    private final Handler handler;

    public LoginPresenterImpl(ILoginView loginView) {
        this.loginView = loginView;
        handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void doLogin(String mail, String pwd) {
        new Thread(() -> {
            User user = LoginService.login(mail, pwd);
            if (user == null) {
                handler.postDelayed(() -> loginView.onLoginResult(false), 1);
            } else {
                UserInfos.user = user;
                ConnectorClient.getChannel().writeAndFlush(generateConnectMessage(UserInfos.user.getUserid()));
                handler.postDelayed(() -> loginView.onLoginResult(true), 1);
            }
        }).start();
    }

    @Override
    public void clear() {
        loginView.onClearText();
    }
}
