package com.banmo.sweethomeclient.mvp.home.match;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.banmo.sweethomeclient.R;
import com.banmo.sweethomeclient.client.UserInfos;
import com.banmo.sweethomeclient.client.handler.MsgHandler;
import com.banmo.sweethomeclient.client.service.MatchService;
import com.banmo.sweethomeclient.mvp.singletalk.SingleTalkActivity;

import java.lang.ref.WeakReference;

public class MatchFragment extends Fragment {

    private static final Handler handler = new Handler(Looper.getMainLooper());
    //hobbies
    String[] hobbies = new String[]{
            "英雄联盟",
            "编程",
            "哲学",
            "历史",
            "养猫",
            "养猪",
            "种植",
            "健身",
            "摸鱼",
            "创作"
    };
    //等待对话框
    private WaitDialog waitDialog;
    private View mRootView;
    private RecyclerView recyclerView;
    private Button groupMatchBtn;
    private Button singleMatchBtn;
    private Button manBtn;
    private Button manwomanBtn;
    private Button womanBtn;
    private TextView hobbyTv;
    private TextView minAgeTv;
    private TextView maxAgeTv;
    //状态记录
    private int dstSex = 0;
    private int dstHobby = 0;

    public void onMatchSuccess() {
        handler.postDelayed(() -> {
            Toast.makeText(getContext(), "匹配成功", Toast.LENGTH_SHORT).show();
        }, 1);
        waitDialog.dismiss();

        Intent intent = new Intent(getContext(), SingleTalkActivity.class);
        if (UserInfos.usingState == UserInfos.UsingState.SINGLE_MATCH) {
            intent.putExtra("flag", "singleMatch");
        } else {
            intent.putExtra("flag", "groupMatch");
        }
        startActivity(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_match, container, false);
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        groupMatchBtn = mRootView.findViewById(R.id.fragment_match_groupMatchBtn);
        singleMatchBtn = mRootView.findViewById(R.id.fragment_match_singleMatchBtn);
        manBtn = mRootView.findViewById(R.id.fragment_match_manBtn);
        manwomanBtn = mRootView.findViewById(R.id.fragment_match_manwomanBtn);
        womanBtn = mRootView.findViewById(R.id.fragment_match_womanBtn);
        hobbyTv = mRootView.findViewById(R.id.fragment_match_hobbyTv);
        minAgeTv = mRootView.findViewById(R.id.fragment_match_minAgeTv);
        maxAgeTv = mRootView.findViewById(R.id.fragment_match_maxAgeTv);
        recyclerView = mRootView.findViewById(R.id.fragment_match_recyclerView);
        waitDialog = new WaitDialog(getContext()) {
            @Override
            public void cancel() {
                super.cancel();
                MatchService.cancelMatch(UserInfos.user, dstHobby, UserInfos.isGroupMatching());
                Toast.makeText(getContext(), "取消匹配", Toast.LENGTH_SHORT).show();
                UserInfos.usingState = UserInfos.UsingState.NULL;
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        hobbyTv.setText(hobbies[0]);
        initRecyclerList();
        initClick();
    }

    private void initRecyclerList() {
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(new MsgAdapter());
    }

    private void initClick() {
        manBtn.setOnClickListener(v -> dstSex = 0);
        manwomanBtn.setOnClickListener(v -> dstSex = 1);
        womanBtn.setOnClickListener(v -> dstSex = 2);
        singleMatchBtn.setOnClickListener(v -> {
            int minAge = Integer.parseInt(minAgeTv.getText().toString());
            int maxAge = Integer.parseInt(maxAgeTv.getText().toString());
            MatchService.singleMatch(UserInfos.user, minAge, maxAge, dstSex, dstHobby);
            UserInfos.usingState = UserInfos.UsingState.SINGLE_MATCH;
            MsgHandler.matchFragment = new WeakReference<>(this);
            handler.postDelayed(() -> waitDialog.show(), 1);//显示等待对话框
        });
        groupMatchBtn.setOnClickListener(v -> {
            MatchService.groupMatch(UserInfos.user, dstHobby);
            UserInfos.usingState = UserInfos.UsingState.GROUP_MATCH;

        });
    }


    private static class MsgViewHolder extends RecyclerView.ViewHolder {

        TextView tv;

        public MsgViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.fragment_match_item_content);
        }
    }

    private class MsgAdapter extends RecyclerView.Adapter<MsgViewHolder> {

        @NonNull
        @Override
        public MsgViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View msgView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_match_item, parent, false);
            return new MsgViewHolder(msgView);
        }

        @Override
        public void onBindViewHolder(@NonNull MsgViewHolder holder, int position) {
            holder.tv.setText(hobbies[position]);
            holder.itemView.setOnClickListener(v -> {
                dstHobby = position;
                hobbyTv.setText(holder.tv.getText());
                CharSequence text = holder.tv.getText();
                Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
            });
        }

        @Override
        public int getItemCount() {
            return hobbies.length;
        }
    }

}
