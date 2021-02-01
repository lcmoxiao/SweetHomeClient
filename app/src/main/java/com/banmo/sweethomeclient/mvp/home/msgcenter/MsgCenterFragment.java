package com.banmo.sweethomeclient.mvp.home.msgcenter;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.banmo.sweethomeclient.R;

import java.util.ArrayList;
import java.util.List;

import static com.banmo.sweethomeclient.mvp.home.HomeActivity.switchFragment;

public class MsgCenterFragment extends Fragment {

    private View mRootView;
    private Button backBtn;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_msgcenter, container, false);
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView = mRootView.findViewById(R.id.fragment_msgcenter_msgRv);
        backBtn = mRootView.findViewById(R.id.fragment_msgcenter_backBtn);
    }

    @Override
    public void onStart() {
        super.onStart();
        backBtn.setOnClickListener(v -> {
            switchFragment(1);
        });
        initMsgList();
    }

    void initMsgList() {
        List<MsgDateBean> msgDateBeans = new ArrayList<>();
        Bitmap bmp = Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888);
        bmp.eraseColor(Color.parseColor("#FFEC808D"));
        Log.e("213", bmp.toString());
        msgDateBeans.add(new MsgDateBean("2021/1/26 18:34", "在线"));
        msgDateBeans.add(new MsgDateBean("2021/1/26 18:34", "在线"));

        recyclerView.setLayoutManager(new LinearLayoutManager(
                getContext(),
                LinearLayoutManager.VERTICAL,
                false
        ));
        recyclerView.setAdapter(new MsgAdapter(msgDateBeans));
    }

    private static class MsgDateBean {

        private final String msg;
        private final String time;

        public MsgDateBean(String msg, String time) {
            this.msg = msg;
            this.time = time;
        }

        public String getMsg() {
            return msg;
        }

        public String getTime() {
            return time;
        }
    }

    private static class MsgViewHolder extends RecyclerView.ViewHolder {

        TextView msgTv;
        TextView timeTv;

        public MsgViewHolder(@NonNull View itemView) {
            super(itemView);
            msgTv = itemView.findViewById(R.id.fragment_msgcenter_item_msg);
            timeTv = itemView.findViewById(R.id.fragment_msgcenter_item_timeStamp);
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
            View msgView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_msgcenter_item, parent, false);
            return new MsgViewHolder(msgView);
        }

        @Override
        public void onBindViewHolder(@NonNull MsgViewHolder holder, int position) {
            MsgDateBean msgDateBean = msgDateBeans.get(position);
            holder.msgTv.setText(msgDateBean.getMsg());
            holder.timeTv.setText(msgDateBean.getTime());
        }

        @Override
        public int getItemCount() {
            return msgDateBeans.size();
        }
    }


}
