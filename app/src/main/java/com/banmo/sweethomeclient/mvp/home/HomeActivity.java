package com.banmo.sweethomeclient.mvp.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.banmo.sweethomeclient.R;
import com.banmo.sweethomeclient.client.UserInfos;
import com.banmo.sweethomeclient.databinding.ActivityHomeBinding;
import com.banmo.sweethomeclient.mvp.home.friend.FriendFragment;
import com.banmo.sweethomeclient.mvp.home.match.MatchFragment;
import com.banmo.sweethomeclient.mvp.home.mine.MineFragment;
import com.banmo.sweethomeclient.mvp.home.msgcenter.MsgCenterFragment;
import com.banmo.sweethomeclient.mvp.login.LoginActivity;
import com.banmo.sweethomeclient.tool.SqLiteTOOLs;

public class HomeActivity extends AppCompatActivity {

    private static final Fragment[] fragments = new Fragment[4];
    private static final String TAG = "HomeActivity";
    private static int fragmentIndex = 0;
    private static FragmentManager manager;
    ActivityHomeBinding inflate;


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
            ((Flush) fragments[index]).flushViewFromData();
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        inflate = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(inflate.getRoot());

        initClickListener();
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
        if (requestCode == 1) {
            //只有LoginActivity会返回到这里，显示为登陆成功，跳转到friendFragment界面。
            if (resultCode == RESULT_OK) {

                int userid = UserInfos.getUserid();
                SqLiteTOOLs.init(this, userid);
                initFragmentModel();
                switchFragment(1);
            } else if (resultCode == RESULT_CANCELED) {
                finish();
            }
        }
    }

    private void initClickListener() {
        inflate.homeFindBt.setOnClickListener(v -> switchFragment(0));
        inflate.homeFriendBt.setOnClickListener(v -> switchFragment(1));
        inflate.homeMineBt.setOnClickListener(v -> switchFragment(2));
    }

    private void initFragmentModel() {
        if (manager == null) manager = getSupportFragmentManager();
        // 每次重启都要清空fragments的内容
        for (int i = 0; i < fragments.length; i++) {
            if (fragments[i] != null) {
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.remove(fragments[i]);
                transaction.commit();
                fragments[i] = null;
            }
        }
    }
}