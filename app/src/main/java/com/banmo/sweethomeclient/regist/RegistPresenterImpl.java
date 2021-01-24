package com.banmo.sweethomeclient.regist;

import android.os.Handler;
import android.os.Looper;

class RegistPresenterImpl implements IRegistPresenter {

    private IRegistView registView; //视图

    private Handler handler;

    public RegistPresenterImpl(IRegistView registView) {
        this.registView = registView;
        handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void doRegist(String mail, String pwd) {

        boolean result = checkRegist(mail, pwd);
        if (!result) clear();
        handler.postDelayed(() -> registView.onRegistResult(result), 3000);

    }

    //ToDo 使用rest申请注册
    private boolean checkRegist(String mail, String pwd) {
        return mail.equals("a") && pwd.equals("1");
    }

    @Override
    public void clear() {
        registView.onClearText();
    }
}
