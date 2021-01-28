package com.banmo.sweethomeclient.mvp.home.friend;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.banmo.sweethomeclient.R;
import com.banmo.sweethomeclient.customview.CircleImageView;
import com.banmo.sweethomeclient.mvp.singletalk.SingleTalkActivity;

import java.util.ArrayList;
import java.util.List;

public class FriendFragment extends Fragment {

    private View mRootView;
    private TextView friendNumTv;
    private ImageView friendRemind;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_friend, container, false);
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        friendNumTv = mRootView.findViewById(R.id.fragment_friend_numTv);
        friendRemind = mRootView.findViewById(R.id.fragment_friend_remind);
        recyclerView = mRootView.findViewById(R.id.fragment_friend_msgRv);
    }

    @Override
    public void onStart() {
        super.onStart();
        friendNumTv.setText("2/2");
        initMsgList();
    }

    void initMsgList() {
        List<MsgDateBean> msgDateBeans = new ArrayList<>();
        Bitmap bmp = Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888);
        bmp.eraseColor(Color.parseColor("#FFEC808D"));
        Log.e("213", bmp.toString());
        msgDateBeans.add(new MsgDateBean(bmp, "LC", "吃了吗", "2021/1/26 18:34", "在线"));
        msgDateBeans.add(new MsgDateBean(bmp, "LC", "吃了吗", "2021/1/26 18:34", "在线"));

        recyclerView.setLayoutManager(new LinearLayoutManager(
                getContext(),
                LinearLayoutManager.VERTICAL,
                false
        ));
        recyclerView.setAdapter(new MsgAdapter(msgDateBeans));
    }

    private static class MsgDateBean {

        private final Bitmap headImg;
        private final String name;
        private final String lastMsg;
        private final String lastTime;
        private final String state;

        public MsgDateBean(Bitmap headImg, String name, String lastMsg, String lastTime, String state) {
            this.headImg = headImg;
            this.name = name;
            this.lastMsg = lastMsg;
            this.lastTime = lastTime;
            this.state = state;
        }

        public Bitmap getHeadImg() {
            return headImg;
        }

        public String getName() {
            return name;
        }

        public String getLastMsg() {
            return lastMsg;
        }

        public String getState() {
            return state;
        }

        public String getLastTime() {
            return lastTime;
        }
    }

    private static class MsgViewHolder extends RecyclerView.ViewHolder {

        CircleImageView headIv;
        TextView nameTv;
        TextView lastMsgTv;
        TextView lastTimeTv;

        public MsgViewHolder(@NonNull View itemView) {
            super(itemView);
            headIv = itemView.findViewById(R.id.fragment_friend_msg_item_headImg);
            nameTv = itemView.findViewById(R.id.fragment_friend_msg_item_username);
            lastMsgTv = itemView.findViewById(R.id.fragment_friend_msg_item_lastMsg);
            lastTimeTv = itemView.findViewById(R.id.fragment_friend_msg_item_timeStamp);
        }
    }

    private class MsgAdapter extends RecyclerView.Adapter<MsgViewHolder> {

        private final List<MsgDateBean> msgDateBeans;

        MsgAdapter(@NonNull List<MsgDateBean> msgDateBeans) {
            this.msgDateBeans = msgDateBeans;
        }

        @NonNull
        @Override
        public MsgViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View msgView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_friend_msg_item, parent, false);
            return new MsgViewHolder(msgView);
        }

        @Override
        public void onBindViewHolder(@NonNull MsgViewHolder holder, int position) {
            MsgDateBean msgDateBean = msgDateBeans.get(position);
            String username = msgDateBean.getName();

            holder.headIv.setImageBitmap(msgDateBean.getHeadImg());
            holder.nameTv.setText(username);
            holder.lastMsgTv.setText(msgDateBean.getLastMsg());
            holder.lastTimeTv.setText(msgDateBean.getLastTime());
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), SingleTalkActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("dstUsername", msgDateBean.getName());
                intent.putExtra("dstUserState", msgDateBean.getState());
                startActivity(intent);
            });

        }

        @Override
        public int getItemCount() {
            return msgDateBeans.size();
        }
    }


}
