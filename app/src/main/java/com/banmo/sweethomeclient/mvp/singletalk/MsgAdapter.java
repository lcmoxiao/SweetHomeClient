package com.banmo.sweethomeclient.mvp.singletalk;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.banmo.sweethomeclient.R;
import com.banmo.sweethomeclient.client.UserInfos;
import com.banmo.sweethomeclient.client.service.UserService;
import com.banmo.sweethomeclient.customview.CircleImageView;
import com.banmo.sweethomeclient.mvp.detail.FriendDetailActivity;

import java.lang.ref.WeakReference;
import java.util.List;

public class MsgAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<MsgDateBean> msgDateBeans;
    private final WeakReference<Activity> mContext;
    private final android.os.Handler handler;

    MsgAdapter(@NonNull List<MsgDateBean> msgDateBeans, Activity activity, android.os.Handler handler) {
        this.msgDateBeans = msgDateBeans;
        this.mContext = new WeakReference<>(activity);
        this.handler = handler;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View msgView;
        if (viewType == 0 || viewType == 4) {
            msgView = LayoutInflater.from(mContext.get()).inflate(R.layout.activity_singletalk_lefttext_item, parent, false);
            return new TextMsgViewHolder(msgView);
        } else if (viewType == 1 || viewType == 5) {
            msgView = LayoutInflater.from(mContext.get()).inflate(R.layout.activity_singletalk_righttext_item, parent, false);
            return new TextMsgViewHolder(msgView);
        } else if (viewType == 2) {
            msgView = LayoutInflater.from(mContext.get()).inflate(R.layout.activity_singletalk_leftimg_item, parent, false);
            return new ImgMsgViewHolder(msgView);
        } else if (viewType == 3) {
            msgView = LayoutInflater.from(mContext.get()).inflate(R.layout.activity_singletalk_rightimg_item, parent, false);
            return new ImgMsgViewHolder(msgView);
        } else {
            msgView = LayoutInflater.from(mContext.get()).inflate(R.layout.activity_singletalk_lefttext_item, parent, false);
            return new TextMsgViewHolder(msgView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MsgDateBean msgDateBean = msgDateBeans.get(position);
        int msgType = msgDateBean.getMsgType();
        new Thread(() -> {
            Bitmap headImg;
            if (msgType % 2 == 0) {
                headImg = UserService.getHeadImgByUserid(msgDateBean.getFriendid());
            } else {
                headImg = UserService.getHeadImgByUserid(UserInfos.getUserid());
            }
            handler.postDelayed(() -> {
                if (msgType == 0 || msgType == 1) {
                    TextMsgViewHolder tholder = (TextMsgViewHolder) holder;
                    tholder.headIv.setImageBitmap(headImg);
                    if (msgType % 2 == 0)
                        tholder.headIv.setOnClickListener(v -> {
                            Intent intent = new Intent(mContext.get(), FriendDetailActivity.class);
                            intent.putExtra("friendID", msgDateBean.getFriendid());
                            mContext.get().startActivityForResult(intent, 10);
                        });
                    tholder.msgTv.setText(new String(msgDateBean.getContent()));
                    tholder.timeTv.setText(msgDateBean.getCreateTime());
                } else if (msgType == 2 || msgType == 3) {
                    ImgMsgViewHolder iholder = (ImgMsgViewHolder) holder;
                    iholder.headIv.setImageBitmap(headImg);
                    if (msgType % 2 == 0)
                        iholder.headIv.setOnClickListener(v -> {
                            Intent intent = new Intent(mContext.get(), FriendDetailActivity.class);
                            intent.putExtra("friendID", msgDateBean.getFriendid());
                            mContext.get().startActivityForResult(intent, 10);
                        });
                    iholder.msgIv.setImageBitmap(BitmapFactory.decodeByteArray(msgDateBean.getContent(), 0, msgDateBean.getContent().length));
                    iholder.timeTv.setText(msgDateBean.getCreateTime());
                } else if (msgType == 4 || msgType == 5) {
                    TextMsgViewHolder tholder = (TextMsgViewHolder) holder;
                    tholder.headIv.setImageBitmap(headImg);
                    if (msgType % 2 == 0)
                        tholder.headIv.setOnClickListener(v -> {
                            Intent intent = new Intent(mContext.get(), FriendDetailActivity.class);
                            intent.putExtra("friendID", msgDateBean.getFriendid());
                            mContext.get().startActivityForResult(intent, 10);
                        });
                    String text = "录音 时长：" + msgDateBean.getRecordTime();
                    tholder.msgTv.setText(text);
                    tholder.msgTv.setGravity(Gravity.CENTER);
                    tholder.msgTv.setBackgroundColor(Color.parseColor("#FFEC808D"));
                    tholder.timeTv.setText(msgDateBean.getCreateTime());
                    tholder.itemView.setOnClickListener(v -> {
                        AudioTrackImpl audioTrack = new AudioTrackImpl() {
                            @Override
                            void onFinished() {
                            }
                        };
                        audioTrack.setData(msgDateBean.getContent());
                        audioTrack.firstPlay();
                    });
                }
            }, 0);
        }).start();

    }

    @Override
    public int getItemViewType(int position) {
        return msgDateBeans.get(position).getMsgType();
    }

    @Override
    public int getItemCount() {
        return msgDateBeans.size();
    }


    public void addItem(MsgDateBean msgDateBean) {
        msgDateBeans.add(msgDateBean);
        notifyItemInserted(msgDateBeans.size());
    }

    static class TextMsgViewHolder extends RecyclerView.ViewHolder {

        CircleImageView headIv;
        TextView msgTv;
        TextView timeTv;

        public TextMsgViewHolder(@NonNull View itemView) {
            super(itemView);
            headIv = itemView.findViewById(R.id.activity_singletalk_text_item_headImg);
            msgTv = itemView.findViewById(R.id.activity_singletalk_text_item_msg);
            timeTv = itemView.findViewById(R.id.activity_singletalk_text_item_timeStamp);
        }
    }

    static class ImgMsgViewHolder extends RecyclerView.ViewHolder {

        CircleImageView headIv;
        ImageView msgIv;
        TextView timeTv;

        public ImgMsgViewHolder(@NonNull View itemView) {
            super(itemView);
            headIv = itemView.findViewById(R.id.activity_singletalk_img_item_headImg);
            msgIv = itemView.findViewById(R.id.activity_singletalk_img_item_msg);
            timeTv = itemView.findViewById(R.id.activity_singletalk_img_item_timeStamp);
        }
    }
}
