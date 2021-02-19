package com.banmo.sweethomeclient.mvp.home.friend;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import com.banmo.sweethomeclient.client.UserInfos;
import com.banmo.sweethomeclient.client.service.UserService;
import com.banmo.sweethomeclient.customview.CircleImageView;
import com.banmo.sweethomeclient.mvp.home.Flush;
import com.banmo.sweethomeclient.mvp.singletalk.SingleTalkActivity;
import com.banmo.sweethomeclient.pojo.User;
import com.banmo.sweethomeclient.tool.SqLiteTOOLs;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.banmo.sweethomeclient.mvp.home.HomeActivity.switchFragment;

public class FriendFragment extends Fragment implements Flush {

    private static final Handler handler = new Handler(Looper.getMainLooper());
    private static final String TAG = "FriendFragment";

    private View mRootView;
    private TextView friendNumTv;
    private ImageView friendRemind;
    // 数据
    private RecyclerView recyclerView;
    private FriendRVAdapter friendRVAdapter;

    private static List<FriendFragment.MsgDateBean> fromFriendsToDataBeans(List<User> friends) {
        if (friends == null) return null;
        List<MsgDateBean> msgDateBeans = new ArrayList<>();
        //读取所有的缓存的聊天信息

        for (int i = 0; i < friends.size(); i++) {
            User user = friends.get(i);
            com.banmo.sweethomeclient.mvp.singletalk.MsgDateBean msgDateBean = SqLiteTOOLs.selectLastByUserid(user.getUserid());
            MsgDateBean mdb = null;
            String username = user.getUsername();
            Bitmap headImg = UserService.getHeadImgByUseridAndFlushCache(user.getUserid());

            if (msgDateBean == null) {
                mdb = new MsgDateBean(headImg, username, "无消息", "", "在线");
            } else {
                int msgType = msgDateBean.getMsgType();
                String createTime = msgDateBean.getCreateTime();
                if (msgType == 0) {
                    mdb = new MsgDateBean(headImg, username, username + "：" + new String(msgDateBean.getContent()), createTime, "在线");
                } else if (msgType == 1) {
                    mdb = new MsgDateBean(headImg, username, "我：" + new String(msgDateBean.getContent()), createTime, "在线");
                } else if (msgType == 2) {
                    mdb = new MsgDateBean(headImg, username, "图片", createTime, "在线");
                } else if (msgType == 3) {
                    mdb = new MsgDateBean(headImg, username, "我：" + "图片", createTime, "在线");
                } else if (msgType == 4) {
                    mdb = new MsgDateBean(headImg, username, "语音", createTime, "在线");
                } else if (msgType == 5) {
                    mdb = new MsgDateBean(headImg, username, "我：" + "语音", createTime, "在线");
                }
            }
            msgDateBeans.add(mdb);
        }
        return msgDateBeans;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate: ");

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume: ");
        flushViewFromData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView: ");
        mRootView = inflater.inflate(R.layout.fragment_friend, container, false);
        friendNumTv = mRootView.findViewById(R.id.fragment_friend_numTv);
        friendRemind = mRootView.findViewById(R.id.fragment_friend_remind);
        recyclerView = mRootView.findViewById(R.id.fragment_friend_msgRv);
        return mRootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG, "onStart: ");
        friendRemind.setOnClickListener(v -> switchFragment(3));
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.e(TAG, "onActivityResult: " + requestCode + " " + resultCode);
        //从SingleTalk返回
        if (requestCode == 10) {
            if (resultCode == RESULT_OK) {
                Log.e(TAG, "onActivityResult: 从SingleTalk返回 flushViewFromData");
                flushViewFromData();
            }
        }
    }

    @Override
    public void flushViewFromData() {
        new Thread(() -> {
            UserInfos.flushFriendsInfo();
            String s = UserInfos.friends.size() + "/" + UserInfos.user.getUserfriendsize();
            List<MsgDateBean> msgDateBeans = fromFriendsToDataBeans(UserInfos.friends);
            handler.postDelayed(() -> {
                friendNumTv.setText(s);
                if (friendRVAdapter == null) {
                    friendRVAdapter = new FriendRVAdapter(this, msgDateBeans);
                    recyclerView.setAdapter(friendRVAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(
                            getContext(),
                            LinearLayoutManager.VERTICAL,
                            false
                    ));
                } else {
                    friendRVAdapter.setMsgDateBeans(msgDateBeans);
                }
            }, 0);
        }).start();

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


    private static class FriendRVAdapter extends RecyclerView.Adapter<FriendFragment.MsgViewHolder> {

        private final FriendFragment friendFragment;
        private List<FriendFragment.MsgDateBean> msgDateBeans;

        FriendRVAdapter(FriendFragment friendFragment, List<FriendFragment.MsgDateBean> msgDateBeans) {
            this.friendFragment = friendFragment;
            this.msgDateBeans = msgDateBeans;
        }

        public void setMsgDateBeans(List<MsgDateBean> msgDateBeans) {
            this.msgDateBeans = msgDateBeans;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public FriendFragment.MsgViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View msgView = LayoutInflater.from(friendFragment.getContext()).inflate(R.layout.fragment_friend_msg_item, parent, false);
            return new FriendFragment.MsgViewHolder(msgView);
        }

        @Override
        public void onBindViewHolder(@NonNull FriendFragment.MsgViewHolder holder, int position) {
            FriendFragment.MsgDateBean msgDateBean = msgDateBeans.get(position);
            String username = msgDateBean.getName();

            holder.headIv.setImageBitmap(msgDateBean.getHeadImg());
            holder.nameTv.setText(username);
            holder.lastMsgTv.setText(msgDateBean.getLastMsg());
            holder.lastTimeTv.setText(msgDateBean.getLastTime());
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(friendFragment.getContext(), SingleTalkActivity.class);
                intent.putExtra("dstUsername", msgDateBean.getName());
                intent.putExtra("dstUserState", msgDateBean.getState());
                intent.putExtra("flag", "friend");
                intent.putExtra("friendID", UserInfos.friends.get(position).getUserid());
                friendFragment.startActivityForResult(intent, 10);
            });
        }


        @Override
        public int getItemCount() {
            return msgDateBeans.size();
        }
    }

}
