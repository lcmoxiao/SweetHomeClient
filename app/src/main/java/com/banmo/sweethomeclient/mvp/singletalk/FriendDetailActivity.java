package com.banmo.sweethomeclient.mvp.singletalk;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.banmo.sweethomeclient.R;
import com.banmo.sweethomeclient.client.UserInfos;
import com.banmo.sweethomeclient.client.service.FriendService;
import com.banmo.sweethomeclient.proto.User;

public class FriendDetailActivity extends AppCompatActivity {

    private static final String TAG = "FriendDetailActivity";
    //数据缓存区
    int friendID;
    private Button backBtn;
    private Button addFriendBtn;
    private Button reportBtn;
    private TextView createTimeTv;
    private TextView usernameTv;
    private TextView sexyTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frienddetail);

        backBtn = findViewById(R.id.activity_frienddetail_backBtn);
        addFriendBtn = findViewById(R.id.activity_frienddetail_addFriendBtn);
        reportBtn = findViewById(R.id.activity_frienddetail_reportBtn);

        createTimeTv = findViewById(R.id.activity_frienddetail_createTimeTv);
        usernameTv = findViewById(R.id.activity_frienddetail_usernameTv);
        sexyTv = findViewById(R.id.activity_frienddetail_sexyTv);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initView();
        initClick();
    }

    private void initView() {
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        User user = UserInfos.getUserInfoByUsernbame(username);
        assert user != null;
        createTimeTv.setText(user.getUserbirth().toString());
        usernameTv.setText(user.getUsername());
        //性别
        Byte usersex = user.getUsersex();
        String sexS = "男";
        if (usersex == 2) sexS = "女";
        sexyTv.setText(sexS);

        //没有在匹配状态中，说明是好友
        if (!UserInfos.isOnMatching()) {
            addFriendBtn.setText("删除好友");
            friendID = intent.getIntExtra("friendID", 0);
            Log.e(TAG, "initView: friendID " + friendID);
        }
    }

    void initClick() {
        backBtn.setOnClickListener(v -> finish());
        reportBtn.setOnClickListener(v -> Toast.makeText(this, "已举报", Toast.LENGTH_SHORT).show());
        addFriendBtn.setOnClickListener(v -> {
            if (!UserInfos.isOnMatching()) {
                checkDeleteDialog();
            } else {
                FriendService.wantToBeFriend(UserInfos.user.getUserid(), UserInfos.matchUser.getUserid());
                Toast.makeText(this, "已申请", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("FriendDetailActivity", "onPause: ");
    }

    @Override
    public void finish() {
        super.finish();
    }

    private void checkDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("确认", (dialog, which) -> {
            new Thread(() -> {
                FriendService.deleteFriendRelation(UserInfos.getFriendRelation(friendID));
                UserInfos.deleteFriendRelation(friendID);
                Intent intent = new Intent();
                intent.putExtra("flag", "finish");
                setResult(RESULT_OK, intent);
                finish();
            }).start();
        });
        builder.setNegativeButton("取消", (dialog, which) -> {

        });
        builder.setTitle("提示");
        builder.setMessage("确认要删除该好友吗");

        builder.show();
    }

}
