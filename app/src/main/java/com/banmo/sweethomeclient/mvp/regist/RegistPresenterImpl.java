package com.banmo.sweethomeclient.mvp.regist;

import android.os.Handler;
import android.os.Looper;

import com.banmo.sweethomeclient.proto.User;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.UUID;

import static com.banmo.sweethomeclient.client.tool.OkHttpTools.post;


class RegistPresenterImpl implements IRegistPresenter {

    private final IRegistView registView; //视图

    private final Handler handler;

    public RegistPresenterImpl(IRegistView registView) {
        this.registView = registView;
        handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void doRegist(String mail, String pwd) {
        User user = new User();
        user.setUsermail(mail);
        user.setUserpassword(pwd);
        user.setUsername(UUID.randomUUID().toString());
        Gson gson = new Gson();
        new Thread(() -> {
            String resp = null;
            try {
                resp = post("user", gson.toJson(user));
            } catch (IOException e) {
                e.printStackTrace();
                handler.postDelayed(() -> registView.onRegistResult(false), 1);
            }
            if (resp.equals("-2") || resp.equals("")) {
                clear();
                handler.postDelayed(() -> registView.onRegistResult(false), 1);
            } else {
                handler.postDelayed(() -> registView.onRegistResult(true), 1);
            }
        }).start();
    }


    @Override
    public void clear() {
        registView.onClearText();
    }
}
