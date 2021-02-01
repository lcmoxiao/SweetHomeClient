package com.banmo.sweethomeclient.mvp.singletalk;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.banmo.sweethomeclient.R;

public class FriendDetailActivity extends AppCompatActivity {

    private Button backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frienddetail);

        backBtn = findViewById(R.id.activity_frienddetail_backBtn);
        initClick();
    }

    void initClick() {
        backBtn.setOnClickListener(v -> {
            finish();
        });
    }

}
