package com.banmo.sweethomeclient.mvp.singletalk;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.banmo.sweethomeclient.R;
import com.banmo.sweethomeclient.client.UserInfos;
import com.banmo.sweethomeclient.client.handler.MsgHandler;
import com.banmo.sweethomeclient.client.service.FriendService;
import com.banmo.sweethomeclient.client.service.MatchService;
import com.banmo.sweethomeclient.client.service.TransService;
import com.banmo.sweethomeclient.databinding.ActivitySingletalkBinding;
import com.banmo.sweethomeclient.mvp.detail.FriendDetailActivity;
import com.banmo.sweethomeclient.tool.DateTools;
import com.banmo.sweethomeclient.tool.SqLiteTOOLs;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
    ActivitySingletalkBinding inflate;
    boolean isPreviewing = false;//记录播放状态
    //数据缓存区
    int friendID;
    ByteArrayOutputStream recordOut = new ByteArrayOutputStream(); //  录音缓存
    AudioRecordImpl audioRecord = new AudioRecordImpl() {
        @Override
        void fireData(byte[] buffer) {
            try {
                recordOut.write(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
    AudioTrackImpl audioTrack = new AudioTrackImpl() {
        @Override
        void onFinished() {
            handler.postDelayed(() -> {
                inflate.activitySingleTalkRecordPreviewBtn.setBackgroundResource(R.drawable.ic_recordplay1);
                inflate.activitySingleTalkRecordPreviewBtnIc.setBackgroundResource(R.drawable.ic_recordplay2);
                inflate.activitySingleTalkRecordPreviewTimer.setBase(SystemClock.elapsedRealtime());
                inflate.activitySingleTalkRecordPreviewTimer.stop();
                Toast.makeText(SingleTalkActivity.this, "播放结束", Toast.LENGTH_SHORT).show();
                isPreviewing = !isPreviewing;
            }, 0);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate: ");

        inflate = ActivitySingletalkBinding.inflate(getLayoutInflater());
        setContentView(inflate.getRoot());

        initClick();
        initMsgList();

        Intent intent = getIntent();
        String flag = intent.getStringExtra("flag");
        switch (flag) {
            case "friend": {
                inflate.activitySingleTalkDstUsername.setText(intent.getStringExtra("dstUsername"));
                inflate.activitySingleTalkDstUserState.setText(intent.getStringExtra("dstUserState"));
                friendID = intent.getIntExtra("friendID", 0);
                UserInfos.usingState = UserInfos.UsingState.FRIEND_TALK;
                LinkedList<MsgDateBean> msgDateBeans = SqLiteTOOLs.selectByUserid(friendID);
                msgDateBeans.forEach(v -> adapter.addItem(v));
                break;
            }
            case "singleMatch": {
                new Thread(() -> inflate.activitySingleTalkDstUsername.setText(UserInfos.matchUser.getUsername())).start();
                UserInfos.usingState = UserInfos.UsingState.ON_SINGLE_MATCH;
                String mt = "matching";
                inflate.activitySingleTalkDstUserState.setText(mt);
                break;
            }
            case "groupMatch": {
                UserInfos.usingState = UserInfos.UsingState.ON_GROUP_MATCH;
                String mt = "XX群组";
                inflate.activitySingleTalkDstUsername.setText(mt);
                String gmt = "groupMatching";
                inflate.activitySingleTalkDstUserState.setText(gmt);
                inflate.activitySingleTalkDetailBtn.setVisibility(View.GONE);
                break;
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        MsgHandler.singleTalkActivity = new WeakReference<>(this);
    }

    private void initMsgList() {
        inflate.activitySingleTalkRecyclerView.setLayoutManager(new LinearLayoutManager(
                getBaseContext(),
                LinearLayoutManager.VERTICAL,
                false
        ));
        List<MsgDateBean> msgDateBeans = new ArrayList<>();
        adapter = new MsgAdapter(msgDateBeans, this, handler);
        inflate.activitySingleTalkRecyclerView.setAdapter(adapter);
    }

    private void initClick() {
        inflate.activitySingleTalkBackBtn.setOnClickListener(v -> finish());

        inflate.activitySingleTalkSendBtn.setOnClickListener(v -> {
            byte[] bytes = inflate.activitySingleTalkInputEt.getText().toString().getBytes();

            if (UserInfos.isOnMatching()) {
                if (UserInfos.isGroupMatching()) {
                    TransService.sendTextMsg(0, UserInfos.getUserid(), 0, UserInfos.groupid, bytes);
                } else {
                    TransService.sendTextMsg(0, UserInfos.getUserid(), UserInfos.getMatcherID(), 0, bytes);
                }
            } else {
                //从左到右分别为 msgType，UserID，TIME，CONTENT
                SqLiteTOOLs.insert(1, friendID, DateTools.getNowTimeToSecond(), bytes, "无");
                TransService.sendTextMsg(UUID.randomUUID().hashCode(), UserInfos.getUserid(), friendID, 0, bytes);
            }

            Bitmap bmp = Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888);
            bmp.eraseColor(Color.parseColor("#FFEC808D"));
            adapter.addItem(new MsgDateBean(inflate.activitySingleTalkInputEt.getText().toString().getBytes(), DateTools.getNowTimeToSecond(), 1, 1, "无"));
            inflate.activitySingleTalkInputEt.setText("");
        });

        inflate.activitySingleTalkDetailBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, FriendDetailActivity.class);
            intent.putExtra("username", inflate.activitySingleTalkDstUsername.getText());
            intent.putExtra("friendID", friendID);
            startActivityForResult(intent, 10);
        });
        inflate.activitySingleTalkMakePhotoBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, PhotoActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("friendID", friendID);
            intent.putExtra("flag", "makePhoto");
            startActivity(intent);
        });
        inflate.activitySingleTalkPhotoBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, PhotoActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("friendID", friendID);
            intent.putExtra("flag", "selectPhoto");
            startActivity(intent);
        });
        inflate.activitySingleTalkRecordBtn.setOnClickListener(v -> {
            inflate.activitySingleTalkRecordLayout.setVisibility(View.VISIBLE);
            inflate.activitySingleTalkRecordStartLayout.setVisibility(View.VISIBLE);
        });

        inflate.activitySingleTalkRecordStartBtn.setOnClickListener(v -> startRecord());
        inflate.activitySingleTalkRecordStopBtn.setOnClickListener(v -> stopRecord());
        inflate.activitySingleTalkRecordPreviewBtn.setOnClickListener(v -> previewRecord());
        inflate.activitySingleTalkRecordSendBtn.setOnClickListener(v -> sendRecord());
        inflate.activitySingleTalkRecordRerecordBtn.setOnClickListener(v -> {
            inflate.activitySingleTalkRecordLayout.setVisibility(View.GONE);
            inflate.activitySingleTalkRecordPreviewLayout.setVisibility(View.GONE);
            inflate.activitySingleTalkRecordLayout.setVisibility(View.VISIBLE);
            inflate.activitySingleTalkRecordStartLayout.setVisibility(View.VISIBLE);
            recordOut.reset();
            audioRecord.release();
            audioTrack.stopPlay();
        });

    }

    private void stopRecord() {
        inflate.activitySingleTalkRecordStopLayout.setVisibility(View.GONE);
        inflate.activitySingleTalkRecordPreviewLayout.setVisibility(View.VISIBLE);

        //结束录音
        audioRecord.stopRecord();
        //结束计时
        inflate.activitySingleTalkRecordStopTimer.stop();
        String s = inflate.activitySingleTalkRecordStopTimer.getText().toString();
        inflate.activitySingleTalkRecordPreviewTimeTv.setText(s);
        isPreviewing = false;
    }

    private void previewRecord() {

        if (!isPreviewing) {
            handler.postDelayed(() -> {
                inflate.activitySingleTalkRecordPreviewBtn.setBackgroundResource(R.drawable.ic_recordpause1);
                inflate.activitySingleTalkRecordPreviewBtnIc.setBackgroundResource(R.drawable.ic_recordpause2);
                //开始播放计时
                inflate.activitySingleTalkRecordPreviewTimer.setBase(SystemClock.elapsedRealtime());
                inflate.activitySingleTalkRecordPreviewTimer.start();
            }, 0);
            //播放
            if (!audioTrack.isPlaying()) {
                audioTrack.setData(recordOut.toByteArray());
            }
            audioTrack.firstPlay();
        } else {
            inflate.activitySingleTalkRecordPreviewBtn.setBackgroundResource(R.drawable.ic_recordplay1);
            inflate.activitySingleTalkRecordPreviewBtnIc.setBackgroundResource(R.drawable.ic_recordplay2);
            inflate.activitySingleTalkRecordPreviewTimer.stop();
            audioTrack.pausePlay();
        }
        isPreviewing = !isPreviewing;

    }

    private void sendRecord() {
        inflate.activitySingleTalkRecordLayout.setVisibility(View.GONE);
        inflate.activitySingleTalkRecordPreviewLayout.setVisibility(View.GONE);
        String recordTime = inflate.activitySingleTalkRecordPreviewTimeTv.getText().toString();
        if (UserInfos.isOnMatching()) {
            if (UserInfos.isGroupMatching()) {
                TransService.sendVoiceMsg(0, UserInfos.getUserid(), 0, UserInfos.groupid, recordOut.toByteArray(), recordTime);
            } else {
                TransService.sendVoiceMsg(0, UserInfos.getUserid(), UserInfos.getMatcherID(), 0, recordOut.toByteArray(), recordTime);
            }
        } else {
            //从左到右分别为 msgType，UserID，TIME，CONTENT
            SqLiteTOOLs.insert(5, friendID, DateTools.getNowTimeToSecond(), recordOut.toByteArray(), recordTime);
            TransService.sendVoiceMsg(UUID.randomUUID().hashCode(), UserInfos.getUserid(), friendID, 0, recordOut.toByteArray(), recordTime);
        }
        Bitmap bmp = Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888);
        bmp.eraseColor(Color.parseColor("#FFEC808D"));
        adapter.addItem(new MsgDateBean(recordOut.toByteArray(), DateTools.getNowTimeToSecond(), UserInfos.getUserid(), 5,
                recordTime));
    }

    private void startRecord() {
        inflate.activitySingleTalkRecordStartLayout.setVisibility(View.GONE);
        inflate.activitySingleTalkRecordStopLayout.setVisibility(View.VISIBLE);
        //开始录音
        recordOut.reset();
        audioRecord.startRecord(); //会通过fireData方法写入cacheOut中
        //开启计时
        inflate.activitySingleTalkRecordStopTimer.setBase(SystemClock.elapsedRealtime());
        inflate.activitySingleTalkRecordStopTimer.start();
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

    public void onReceiveMsg(MsgDateBean msgDateBean) {
        Log.e(TAG, "onReceiveMsg: ");
        handler.postDelayed(() -> adapter.addItem(msgDateBean), 0);
    }

    public void matchReject() {
        Log.e("SingleTalkActivity", "matchReject");
        if (UserInfos.isOnMatching()) {
            new Thread(() ->
            {
                int userid = UserInfos.matchUser == null ? 0 : UserInfos.matchUser.getUserid();
                MatchService.rejectMatch(UserInfos.isGroupMatching(), UserInfos.user.getUserid(), userid, UserInfos.groupid);
            }).start();
        }
    }

    public void onMatchReject() {
        UserInfos.usingState = UserInfos.UsingState.NULL;
        handler.postDelayed(() -> Toast.makeText(this, "匹配被取消", Toast.LENGTH_SHORT).show(), 0);
        finish();
    }

    public void onFriendWant(int userid1, int userid2) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("同意", (dialog, which) -> FriendService.agreeToBeFriend(userid1, userid2));
        builder.setNegativeButton("拒绝", (dialog, which) -> FriendService.disagreeToBeFriend(userid1, userid2));
        builder.setTitle("提示");
        builder.setMessage("对方申请和你成为好友");

        handler.postDelayed(builder::show, 0);
    }

    public void onFriendAgree() {
        handler.postDelayed(() -> Toast.makeText(this, "好友申请成功", Toast.LENGTH_SHORT).show(), 0);
        switchFragment(3);
    }

    public void onFriendReject() {
        handler.postDelayed(() -> Toast.makeText(this, "好友申请被拒绝,对话结束", Toast.LENGTH_SHORT).show(), 0);
        finish();
    }


}
