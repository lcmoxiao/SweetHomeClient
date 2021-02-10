package com.banmo.sweethomeclient.mvp.home.mine;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.banmo.sweethomeclient.R;
import com.banmo.sweethomeclient.client.UserInfos;
import com.banmo.sweethomeclient.client.tool.DateFormatTools;
import com.banmo.sweethomeclient.mvp.home.HomeActivity;
import com.banmo.sweethomeclient.mvp.login.LoginActivity;

public class MineFragment extends Fragment {

    private static final String TAG = "MineFragment";
    private View mRootView;

    private EditText idEt;
    private EditText createTimeEt;
    private EditText usernameEt;
    private Spinner sexySp;
    private EditText mailEt;
    private EditText phoneEt;


    private Button logoutBtn;
    private Button changeFriendSizeBtn;
    private Button saveChangeBtn;
    private Button changePwdBtn;
    private Button DestroyUserBtn;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_mine, container, false);
        bindView();
        initView();

        return mRootView;
    }

    private void bindView() {
        idEt = mRootView.findViewById(R.id.fragment_mine_idEt);
        createTimeEt = mRootView.findViewById(R.id.fragment_mine_createTimeEt);
        usernameEt = mRootView.findViewById(R.id.fragment_mine_usernameEt);
        sexySp = mRootView.findViewById(R.id.fragment_mine_sexySp);
        mailEt = mRootView.findViewById(R.id.fragment_mine_mailEt);
        phoneEt = mRootView.findViewById(R.id.fragment_mine_phoneEt);

        logoutBtn = mRootView.findViewById(R.id.fragment_mine_LogoutBtn);
        changeFriendSizeBtn = mRootView.findViewById(R.id.fragment_mine_changeFriendSizeBtn);
        saveChangeBtn = mRootView.findViewById(R.id.fragment_mine_saveChangeBtn);
        changePwdBtn = mRootView.findViewById(R.id.fragment_mine_changePwdBtn);
        DestroyUserBtn = mRootView.findViewById(R.id.fragment_mine_DestroyUserBtn);
    }

    private void initView() {
        String userid = UserInfos.user.getUserid().toString();
        idEt.setText(userid);
        createTimeEt.setText(DateFormatTools.formatToSecond(UserInfos.user.getUsercreatetime()));
        usernameEt.setText(UserInfos.user.getUsername());
        Byte usersex = UserInfos.user.getUsersex();
        sexySp.setSelection(usersex);
        mailEt.setText(UserInfos.user.getUsermail());
        phoneEt.setText(UserInfos.user.getUserphone());
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        logoutBtn.setOnClickListener(v -> {
            UserInfos.clearAllInfos();
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivityForResult(intent, 1);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult: " + requestCode + " " + resultCode);
        if (requestCode == 1) {
            HomeActivity.switchFragment(1);
        }

    }
}
