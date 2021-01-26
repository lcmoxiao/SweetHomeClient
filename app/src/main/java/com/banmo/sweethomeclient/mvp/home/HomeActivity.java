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

public class HomeActivity extends AppCompatActivity {

    private FrameLayout fragmentSpace;
    private Button findBtn;
    private Button friendBtn;
    private Button mineBtn;
    private int fragmentIndex = 0; //记录当前的fragment角标

    private Fragment[] fragments = new Fragment[3];
    private FragmentManager manager;
    private FragmentTransaction transaction;


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

    void switchFragment(int index) {
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

    Fragment getFragment(int index) {
        switch (index) {
            case 0:
                return new MatchFragment();
            case 1:
                return new FriendFragment();
            case 2:
                return new MineFragment();
            default:
                return new FriendFragment();
        }
    }

}