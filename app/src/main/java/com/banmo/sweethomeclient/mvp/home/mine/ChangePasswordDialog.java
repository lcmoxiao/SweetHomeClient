package com.banmo.sweethomeclient.mvp.home.mine;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.banmo.sweethomeclient.databinding.FragmentMinePasswordDialogBinding;


public class ChangePasswordDialog extends Dialog {

    /**
     * 上下文对象 *
     */
    Activity context;
    FragmentMinePasswordDialogBinding inflate;

    private View.OnClickListener mClickListener;

    public ChangePasswordDialog(Activity context) {
        super(context);
        this.context = context;
    }

    public ChangePasswordDialog(Activity context, int theme, View.OnClickListener clickListener) {
        super(context, theme);
        this.context = context;
        this.mClickListener = clickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 指定布局
        inflate = FragmentMinePasswordDialogBinding.inflate(getLayoutInflater());
        setContentView(inflate.getRoot());


        /*
         * 获取圣诞框的窗口对象及参数对象以修改对话框的布局设置, 可以直接调用getWindow(),表示获得这个Activity的Window
         * 对象,这样这可以以同样的方式改变这个Activity的属性.
         */
        Window dialogWindow = this.getWindow();

        WindowManager m = context.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        DisplayMetrics outMetrics = new DisplayMetrics();
        d.getRealMetrics(outMetrics);
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值 
        p.height = (int) (outMetrics.heightPixels * 0.6); // 高度设置为屏幕的0.6
        p.width = (int) (outMetrics.widthPixels * 0.8); // 宽度设置为屏幕的0.8
        dialogWindow.setAttributes(p);

        // 为按钮绑定点击事件监听器
        inflate.fragmentMineDialogSubmitBtn.setOnClickListener(mClickListener);

        this.setCancelable(true);
    }

    public String getOldPWD() {
        return inflate.fragmentMinePasswordOldPwdEt.getText().toString();
    }

    public String getNewPWD1() {
        return inflate.fragmentMinePasswordNewPwdEt1.getText().toString();
    }

    public String getNewPWD2() {
        return inflate.fragmentMinePasswordNewPwdEt2.getText().toString();
    }
} 