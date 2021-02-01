package com.banmo.sweethomeclient.mvp.home;

import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.banmo.sweethomeclient.R;
import com.banmo.sweethomeclient.mvp.home.friend.FriendFragment;
import com.banmo.sweethomeclient.mvp.home.match.MatchFragment;
import com.banmo.sweethomeclient.mvp.home.mine.MineFragment;
import com.banmo.sweethomeclient.mvp.home.msgcenter.MsgCenterFragment;

public class HomeActivity extends AppCompatActivity {

    private static final Fragment[] fragments = new Fragment[4];
    private static int fragmentIndex = 0;
    private static FragmentManager manager;
    private static FragmentTransaction transaction;
    private FrameLayout fragmentSpace;
    private Button findBtn;
    private Button friendBtn;
    private Button mineBtn;

    public static void switchFragment(int index) {
        transaction = manager.beginTransaction();
        if (fragmentIndex != index) {
            if (fragments[index] == null) {
                fragments[index] = getFragment(index);
            }
            if (!transaction.isEmpty()) transaction.remove(fragments[fragmentIndex]);
            transaction.replace(R.id.home_fragment_layout, fragments[index]);
            transaction.commit();
            fragmentIndex = index;
        }
    }

    static Fragment getFragment(int index) {
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
                return new FriendFragment();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_home);
        fragmentSpace = findViewById(R.id.home_fragment_layout);
        findBtn = findViewById(R.id.home_find_bt);
        friendBtn = findViewById(R.id.home_friend_bt);
        mineBtn = findViewById(R.id.home_mine_bt);

        manager = getSupportFragmentManager();


        switchFragment(1);

        //启动按钮切换事务
        initClick();
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