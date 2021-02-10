package com.banmo.sweethomeclient.mvp.singletalk;

import android.content.Context;
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
import com.banmo.sweethomeclient.customview.CircleImageView;

import java.lang.ref.WeakReference;
import java.util.List;

public class MsgAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<MsgDateBean> msgDateBeans;
    private final WeakReference<Context> mContext;

    MsgAdapter(@NonNull List<MsgDateBean> msgDateBeans, Context mContext) {
        this.msgDateBeans = msgDateBeans;
        this.mContext = new WeakReference<>(mContext);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View msgView = null;
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
        }
        return new TextMsgViewHolder(msgView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MsgDateBean msgDateBean = msgDateBeans.get(position);
        if (msgDateBean.getMsgType() == 0 || msgDateBean.getMsgType() == 1) {
            TextMsgViewHolder tholder = (TextMsgViewHolder) holder;
            tholder.headIv.setImageBitmap(msgDateBean.getHeadImg());
            tholder.msgTv.setText(new String(msgDateBean.getContent()));
            tholder.timeTv.setText(msgDateBean.getTime());
        } else if (msgDateBean.getMsgType() == 2 || msgDateBean.getMsgType() == 3) {
            ImgMsgViewHolder iholder = (ImgMsgViewHolder) holder;
            iholder.headIv.setImageBitmap(msgDateBean.getHeadImg());
            iholder.msgIv.setImageBitmap(BitmapFactory.decodeByteArray(msgDateBean.getContent(), 0, msgDateBean.getContent().length));
            iholder.timeTv.setText(msgDateBean.getTime());
        } else if (msgDateBean.getMsgType() == 4 || msgDateBean.getMsgType() == 5) {
            TextMsgViewHolder tholder = (TextMsgViewHolder) holder;
            tholder.headIv.setImageBitmap(msgDateBean.getHeadImg());
            tholder.msgTv.setText(new String(msgDateBean.getContent()));
            tholder.msgTv.setGravity(Gravity.CENTER);
            tholder.msgTv.setBackgroundColor(Color.parseColor("#FFEC808D"));
            tholder.timeTv.setText(msgDateBean.getTime());
        }
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
