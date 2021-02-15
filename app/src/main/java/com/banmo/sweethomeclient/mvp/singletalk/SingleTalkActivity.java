package com.banmo.sweethomeclient.mvp.singletalk;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.banmo.sweethomeclient.R;
import com.banmo.sweethomeclient.client.UserInfos;
import com.banmo.sweethomeclient.client.handler.MsgHandler;
import com.banmo.sweethomeclient.client.service.FriendService;
import com.banmo.sweethomeclient.client.service.MatchService;
import com.banmo.sweethomeclient.client.service.TransService;
import com.banmo.sweethomeclient.client.tool.DateFormatTools;
import com.banmo.sweethomeclient.client.tool.SqLiteTOOLs;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static com.banmo.sweethomeclient.mvp.home.HomeActivity.switchFragment;

public class SingleTalkActivity extends Activity {

    private static final Handler handler = new Handler(Looper.getMainLooper());
    private static final String TAG = "SingleTalkActivity";

    public static MsgAdapter adapter;
    TextView dstUsernameTv;
    TextView dstUserStateTv;
    EditText inputEt;
    Button sendBtn;
    Button makePhotoBtn;
    Button selectPhotoBtn;
    Button recordBtn;
    Button backBtn;
    Button detailBtn;
    RecyclerView recyclerView;
    //录音功能区
    ConstraintLayout recordLayout;
    ConstraintLayout recordStartLayout;
    ConstraintLayout recordStopLayout;
    ConstraintLayout recordPreviewLayout;
    Button recordStartBtn;
    Button recordStopBtn;
    TextView recordingTimeTv;
    Button recordPreviewBtn;
    TextView recordPreviewBtnIc;
    TextView recordPreviewBtnTv;
    TextView recordPlayingTimeTv;
    Button recordSendBtn;
    Button recordRerecordBtn;
    boolean isPlaying = false;//记录播放状态
    //数据缓存区
    int friendID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate: ");
        
        setContentView(R.layout.activity_singletalk);

        dstUsernameTv = findViewById(R.id.activity_singleTalk_dstUsername);
        dstUserStateTv = findViewById(R.id.activity_singleTalk_dstUserState);

        inputEt = findViewById(R.id.activity_singleTalk_inputEt);
        sendBtn = findViewById(R.id.activity_singleTalk_sendBtn);
        makePhotoBtn = findViewById(R.id.activity_singleTalk_makePhotoBtn);
        selectPhotoBtn = findViewById(R.id.activity_singleTalk_photoBtn);
        recordBtn = findViewById(R.id.activity_singleTalk_recordBtn);
        backBtn = findViewById(R.id.activity_singleTalk_backBtn);
        detailBtn = findViewById(R.id.activity_singleTalk_dstUserInfoBtn);
        recyclerView = findViewById(R.id.activity_singleTalk_recyclerView);

        recordLayout = findViewById(R.id.activity_singleTalk_recordLayout);
        recordStartLayout = findViewById(R.id.activity_singleTalk_recordStartLayout);
        recordStopLayout = findViewById(R.id.activity_singleTalk_recordStopLayout);
        recordPreviewLayout = findViewById(R.id.activity_singleTalk_recordPreviewLayout);
        recordStartBtn = findViewById(R.id.activity_singleTalk_record_startBtn);
        recordStopBtn = findViewById(R.id.activity_singleTalk_record_stopBtn);
        recordPreviewBtn = findViewById(R.id.activity_singleTalk_record_previewBtn);
        recordPreviewBtnIc = findViewById(R.id.activity_singleTalk_record_previewBtnIc);
        recordPreviewBtnTv = findViewById(R.id.activity_singleTalk_record_previewBtnTv);
        recordPlayingTimeTv = findViewById(R.id.activity_singleTalk_record_previewTimeTv);
        recordSendBtn = findViewById(R.id.activity_singleTalk_record_sendBtn);
        recordRerecordBtn = findViewById(R.id.activity_singleTalk_record_rerecordBtn);

        recordingTimeTv = findViewById(R.id.activity_singleTalk_record_stopTv);
        initClick();
        initMsgList();

        Intent intent = getIntent();
        String flag = intent.getStringExtra("flag");
        if (flag.equals("friend")) {
            dstUsernameTv.setText(intent.getStringExtra("dstUsername"));
            dstUserStateTv.setText(intent.getStringExtra("dstUserState"));
            friendID = intent.getIntExtra("friendID", 0);
            UserInfos.usingState = UserInfos.UsingState.FRIEND_TALK;
            LinkedList<MsgDateBean> msgDateBeans = SqLiteTOOLs.selectByUserid(friendID);
            msgDateBeans.forEach(v -> adapter.addItem(v));
        } else if (flag.equals("singleMatch")) {
            new Thread(() -> {
                UserInfos.usingState = UserInfos.UsingState.ON_SINGLE_MATCH;
                dstUsernameTv.setText(UserInfos.matchUser.getUsername());
                String mt = "matching";
                dstUserStateTv.setText(mt);
            }).start();
        } else if (flag.equals("groupMatch")) {
            UserInfos.usingState = UserInfos.UsingState.ON_GROUP_MATCH;
            String mt = "XX群组";
            dstUsernameTv.setText(mt);
            String gmt = "groupMatching";
            dstUserStateTv.setText(gmt);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        MsgHandler.singleTalkActivity = new WeakReference<>(this);
    }

    private void initMsgList() {
        recyclerView.setLayoutManager(new LinearLayoutManager(
                getBaseContext(),
                LinearLayoutManager.VERTICAL,
                false
        ));
        List<MsgDateBean> msgDateBeans = new ArrayList<>();
        adapter = new MsgAdapter(msgDateBeans, this);
        recyclerView.setAdapter(adapter);
    }

    private void initClick() {
        sendBtn.setOnClickListener(v -> {
            byte[] bytes = inputEt.getText().toString().getBytes();

            if (UserInfos.isOnMatching()) {
                if (UserInfos.isGroupMatching()) {
                    TransService.sendTextMsg(0, UserInfos.getUserid(), 0, UserInfos.groupid, bytes);
                } else {
                    TransService.sendTextMsg(0, UserInfos.getUserid(), UserInfos.getMatcherID(), 0, bytes);
                }
            } else {
                //从左到右分别为 msgType，UserID，TIME，CONTENT
                SqLiteTOOLs.insert(1, friendID, DateFormatTools.getNowTime(), bytes);
                TransService.sendTextMsg(UUID.randomUUID().hashCode(), UserInfos.getUserid(), friendID, 0, bytes);
            }

            Bitmap bmp = Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888);
            bmp.eraseColor(Color.parseColor("#FFEC808D"));
            adapter.addItem(new MsgDateBean(bmp, inputEt.getText().toString().getBytes(), DateFormatTools.getNowTime(), 1, 1));
            inputEt.setText("");
        });

        detailBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, FriendDetailActivity.class);
            intent.putExtra("username", dstUsernameTv.getText());
            intent.putExtra("friendID", friendID);
            startActivityForResult(intent, 10);
        });
        makePhotoBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, PhotoActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("friendID", friendID);
            intent.putExtra("flag", "makePhoto");
            startActivity(intent);
        });
        selectPhotoBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, PhotoActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("flag", "selectPhoto");
            startActivity(intent);
        });
        recordBtn.setOnClickListener(v -> {
            recordLayout.setVisibility(View.VISIBLE);
            recordStartLayout.setVisibility(View.VISIBLE);
        });
        recordStartBtn.setOnClickListener(v -> {
            recordStartLayout.setVisibility(View.GONE);
            recordStopLayout.setVisibility(View.VISIBLE);
        });
        recordStopBtn.setOnClickListener(v -> {
            recordStopLayout.setVisibility(View.GONE);
            recordPreviewLayout.setVisibility(View.VISIBLE);
        });
        recordPreviewBtn.setOnClickListener(v -> {
            if (isPlaying) {
                recordPreviewBtn.setBackgroundResource(R.drawable.ic_recordplay1);
                recordPreviewBtnIc.setBackgroundResource(R.drawable.ic_recordplay2);
            } else {
                recordPreviewBtn.setBackgroundResource(R.drawable.ic_recordpause1);
                recordPreviewBtnIc.setBackgroundResource(R.drawable.ic_recordpause2);
            }
            isPlaying = !isPlaying;
        });
        recordSendBtn.setOnClickListener(v -> {
            recordLayout.setVisibility(View.GONE);
            recordPreviewLayout.setVisibility(View.GONE);
            if (UserInfos.isOnMatching()) {
                if (UserInfos.isGroupMatching()) {
                    TransService.sendTextMsg(0, UserInfos.getUserid(), 0, UserInfos.groupid, null);
                } else {
                    TransService.sendTextMsg(0, UserInfos.getUserid(), UserInfos.getMatcherID(), 0, null);
                }
            } else {
                //从左到右分别为 msgType，UserID，TIME，CONTENT
                SqLiteTOOLs.insert(1, friendID, DateFormatTools.getNowTime(), null);
                TransService.sendTextMsg(UUID.randomUUID().hashCode(), UserInfos.getUserid(), friendID, 0, null);
            }
            Bitmap bmp = Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888);
            bmp.eraseColor(Color.parseColor("#FFEC808D"));
            adapter.addItem(new MsgDateBean(bmp, "录音".getBytes(), DateFormatTools.getNowTime(), UserInfos.getUserid(), 5));
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(TAG, "onActivityResult: " + requestCode + " " + resultCode);
        if (requestCode == 10) {
            if (resultCode == RESULT_OK)
                if (data != null)
                    if (data.getStringExtra("flag").equals("finish")) {
                        setResult(RESULT_OK);
                        finish();
                    }
        }
    }

    @Override
    public void finish() {
        super.finish();
        matchReject();
        UserInfos.usingState = UserInfos.UsingState.NULL;
    }

    public void onReceiveMsg() {
        Log.e(TAG, "onReceiveMsg: ");
        handler.postDelayed(() -> adapter.addItem(SqLiteTOOLs.selectLastByUserid(friendID)), 0);
    }

    public void matchReject() {
        Log.e("SingleTalkActivity", "matchReject");
        if (UserInfos.isOnMatching()) {
            new Thread(() ->
                    MatchService.rejectMatch(UserInfos.isGroupMatching(), UserInfos.user.getUserid(), UserInfos.matchUser.getUserid(), UserInfos.groupid)).start();
        }
    }

    public void onMatchReject() {
        UserInfos.usingState = UserInfos.UsingState.NULL;
        handler.postDelayed(() -> Toast.makeText(this, "匹配被取消", Toast.LENGTH_SHORT).show(), 0);
        finish();
    }

    public void onFriendWant(int userid1, int userid2) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("同意", (dialog, which) -> {
            FriendService.agreeToBeFriend(userid1, userid2);
        });
        builder.setNegativeButton("拒绝", (dialog, which) -> {
            FriendService.disagreeToBeFriend(userid1, userid2);
        });
        builder.setTitle("提示");
        builder.setMessage("对方申请和你成为好友");

        handler.postDelayed(builder::show, 0);
    }

    public void onFriendAgree(int userid1, int userid2) {
        handler.postDelayed(() -> Toast.makeText(this, "好友申请成功", Toast.LENGTH_SHORT).show(), 0);
        switchFragment(3);
    }

    public void onFriendReject(int userid1, int userid2) {
        handler.postDelayed(() -> Toast.makeText(this, "好友申请被拒绝,对话结束", Toast.LENGTH_SHORT).show(), 0);
        finish();
    }
}
