package com.banmo.sweethomeclient.mvp.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.banmo.sweethomeclient.R;
import com.banmo.sweethomeclient.client.UserInfos;
import com.banmo.sweethomeclient.client.tool.SqLiteTOOLs;
import com.banmo.sweethomeclient.mvp.home.friend.FriendFragment;
import com.banmo.sweethomeclient.mvp.home.match.MatchFragment;
import com.banmo.sweethomeclient.mvp.home.mine.MineFragment;
import com.banmo.sweethomeclient.mvp.home.msgcenter.MsgCenterFragment;
import com.banmo.sweethomeclient.mvp.login.LoginActivity;

public class HomeActivity extends AppCompatActivity {

    private static final Fragment[] fragments = new Fragment[4];
    private static final String TAG = "HomeActivity";
    private static int fragmentIndex = 0;
    private static FragmentManager manager;

    private Button findBtn;
    private Button friendBtn;
    private Button mineBtn;

    private boolean isFirstIn;

    public static void switchFragment(int index) {
        FragmentTransaction transaction = manager.beginTransaction();
        if (fragmentIndex != index) {
            if (fragments[index] != null) {
                transaction.hide(fragments[fragmentIndex]);
                transaction.show(fragments[index]);
            } else {
                if (fragments[fragmentIndex] != null) transaction.hide(fragments[fragmentIndex]);
                fragments[index] = getFragment(index);
                transaction.add(R.id.home_fragment_layout, fragments[index]);
            }
            transaction.commit();
            fragmentIndex = index;
        }
    }

    private static Fragment getFragment(int index) {
        Log.e(TAG, "getFragment: " + index);
        switch (index) {
            case 0:
                return new MatchFragment();
            case 1:
                return new FriendFragment();
            case 2:
                return new MineFragment();
            case 3:
                return new MsgCenterFragment();
            default:
                return null;
        }
    }

    private void bindView() {
        findBtn = findViewById(R.id.home_find_bt);
        friendBtn = findViewById(R.id.home_friend_bt);
        mineBtn = findViewById(R.id.home_mine_bt);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        manager = getSupportFragmentManager();
        bindView();
        initClick();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (UserInfos.user == null) {
            Toast.makeText(this, "请登录", Toast.LENGTH_SHORT).show();
            startActivityForResult(new Intent(HomeActivity.this, LoginActivity.class), 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult: " + requestCode + " " + resultCode);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                SqLiteTOOLs.init(this,UserInfos.getUserid());
                switchFragment(1);
            }

        }

    }

    void initClick() {
        findBtn.setOnClickListener(v -> {
            switchFragment(0);
        });
        friendBtn.setOnClickListener(v -> {
            switchFragment(1);
        });

        mineBtn.setOnClickListener(v -> {
            switchFragment(2);
        });

    }

}