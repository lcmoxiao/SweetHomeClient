package com.banmo.sweethomeclient.mvp.singletalk;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.banmo.sweethomeclient.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SingleTalkActivity extends AppCompatActivity {

    static MsgAdapter adapter;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singletalk);

        TextView dstUsernameTv = findViewById(R.id.activity_singleTalk_dstUsername);
        dstUsernameTv.setText(getIntent().getStringExtra("dstUsername"));
        TextView dstUserStateTv = findViewById(R.id.activity_singleTalk_dstUserState);
        dstUserStateTv.setText(getIntent().getStringExtra("dstUserState"));

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
    }

    private void initMsgList() {
        recyclerView.setLayoutManager(new LinearLayoutManager(
                getBaseContext(),
                LinearLayoutManager.VERTICAL,
                false
        ));
        List<MsgDateBean> msgDateBeans = new ArrayList<>();
        Bitmap bmp = Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888);
        bmp.eraseColor(Color.parseColor("#FFEC808D"));
        msgDateBeans.add(new MsgDateBean(bmp, "LC", "吃了吗", 0, true, null));
        msgDateBeans.add(new MsgDateBean(bmp, "LC", "吃了吗", 1, false, null));
        adapter = new MsgAdapter(msgDateBeans, this);
        recyclerView.setAdapter(adapter);
    }

    private void initClick() {
        sendBtn.setOnClickListener(v -> {
            Bitmap bmp = Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888);
            bmp.eraseColor(Color.parseColor("#FFEC808D"));
            adapter.addItem(new MsgDateBean(bmp, "LC", "吃了吗", 1, true, null));
        });

        detailBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, FriendDetailActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
        makePhotoBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, PhotoActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
            Bitmap bmp = Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888);
            bmp.eraseColor(Color.parseColor("#FFEC808D"));
            adapter.addItem(new MsgDateBean(bmp, "录音", Calendar.getInstance().getTime().toString(), 5, true, null));
        });
    }


}
