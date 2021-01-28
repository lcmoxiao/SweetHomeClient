package com.banmo.sweethomeclient.mvp.singletalk;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.banmo.sweethomeclient.R;

public class SingleTalkActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singletalk);

        TextView dstUsernameTv = findViewById(R.id.activity_singleTalk_dstUsername);
        dstUsernameTv.setText(getIntent().getStringExtra("dstUsername"));
        TextView dstUserStateTv = findViewById(R.id.activity_singleTalk_dstUserState);
        dstUserStateTv.setText(getIntent().getStringExtra("dstUserState"));
    }

}
