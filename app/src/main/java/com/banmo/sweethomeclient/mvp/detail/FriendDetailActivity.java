package com.banmo.sweethomeclient.mvp.detail;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.banmo.sweethomeclient.client.UserInfos;
import com.banmo.sweethomeclient.client.service.FriendService;
import com.banmo.sweethomeclient.client.service.ReportService;
import com.banmo.sweethomeclient.client.service.UserService;
import com.banmo.sweethomeclient.databinding.ActivityFrienddetailBinding;
import com.banmo.sweethomeclient.pojo.Report;
import com.banmo.sweethomeclient.pojo.User;
import com.banmo.sweethomeclient.tool.DateTools;

import java.util.Date;

public class FriendDetailActivity extends AppCompatActivity {

    private static final Handler handler = new Handler(Looper.getMainLooper());

    private static final String TAG = "FriendDetailActivity";
    ActivityFrienddetailBinding inflate;
    //数据缓存区
    int friendID;

    ReportDialog reportDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        inflate = ActivityFrienddetailBinding.inflate(getLayoutInflater());
        setContentView(inflate.getRoot());

    }

    @Override
    protected void onStart() {
        super.onStart();
        initView();
        initClick();
    }

    private void initView() {
        Intent intent = getIntent();
        //没有在匹配状态中，说明是好友
        if (!UserInfos.isOnMatching()) {
            inflate.activityFrienddetailAddFriendBtn.setText("删除好友");
            friendID = intent.getIntExtra("friendID", 0);
            Log.e(TAG, "initView: friendID " + friendID);
        }

        String username = intent.getStringExtra("username");
        new Thread(() -> {

            User user;
            if (username != null) {
                user = UserInfos.getUserInfoByUsername(username);
            } else {
                user = UserService.getUserByUserID(friendID);
            }
            int friendSize = FriendService.getFriendSize(friendID);

            handler.postDelayed(() -> {
                assert user != null;
                inflate.activityFrienddetailHeadImg.setImageBitmap(UserService.getHeadImgByUserid(friendID));
                inflate.activityFrienddetailCreateTimeTv.setText(DateTools.formatToDay(user.getUserbirth()));
                inflate.activityFrienddetailUsernameTv.setText(user.getUsername());
                //性别
                Byte usersex = user.getUsersex();
                String sexS = "男";
                if (usersex == 2) sexS = "女";
                inflate.activityFrienddetailSexyTv.setText(sexS);
                String text = friendSize + "/" + user.getUserfriendsize();
                inflate.activityFrienddetailFriendNumTv.setText(text);
            }, 0);


        }).start();


    }

    void initClick() {
        inflate.activityFrienddetailBackBtn.setOnClickListener(v -> finish());
        inflate.activityFrienddetailReportBtn.setOnClickListener(v -> {
            View.OnClickListener onClickListener = v1 -> {
                Report report = new Report();
                report.setReportcreatetime(new Date());
                report.setUserid(friendID);
                report.setReason(reportDialog.getContent());
                new Thread(() -> {
                    ReportService.postReport(report);
                    handler.postDelayed(() -> Toast.makeText(this, "举报成功", Toast.LENGTH_SHORT).show(), 0);
                }).start();
                reportDialog.dismiss();
            };
            reportDialog = new ReportDialog(this, 4, onClickListener);
            reportDialog.show();
        });
        inflate.activityFrienddetailAddFriendBtn.setOnClickListener(v -> {
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
        DialogInterface.OnClickListener onClickListener = (dialog, which) -> new Thread(() -> {
            FriendService.deleteFriendRelation(UserInfos.getFriendRelation(friendID));
            UserInfos.deleteFriendRelation(friendID);
            Intent intent = new Intent();
            intent.putExtra("flag", "finish");
            setResult(RESULT_OK, intent);
            finish();
        }).start();
        builder.setPositiveButton("确认", onClickListener);
        builder.setNegativeButton("取消", (dialog, which) -> {

        });
        builder.setTitle("提示");
        builder.setMessage("确认要删除该好友吗");

        builder.show();
    }

}
