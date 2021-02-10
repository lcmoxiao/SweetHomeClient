package com.banmo.sweethomeclient.mvp.login;

import android.os.Handler;
import android.os.Looper;

import com.banmo.sweethomeclient.client.ConnectorClient;
import com.banmo.sweethomeclient.client.UserInfos;
import com.banmo.sweethomeclient.client.service.LoginService;
import com.banmo.sweethomeclient.proto.User;

import static com.banmo.sweethomeclient.client.tool.MsgGenerateTools.generateConnectMessage;

class LoginPresenterImpl implements ILoginPresenter {
    private ILoginView loginView; //视图

    private Handler handler;

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
                System.out.println("登录失败");
                return;
            }
            System.out.println("登陆成功" + user);
            UserInfos.user = user;
            ConnectorClient.getChannel().writeAndFlush(generateConnectMessage(UserInfos.user.getUserid()));
            handler.postDelayed(() -> loginView.onLoginResult(true), 1);
        }).start();
    }

    @Override
    public void clear() {
        loginView.onClearText();
    }
}
