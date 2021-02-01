package com.banmo.sweethomeclient.mvp.home.match;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.banmo.sweethomeclient.R;

public class MatchFragment extends Fragment {

    private View mRootView;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_match, container, false);
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView = mRootView.findViewById(R.id.fragment_match_recyclerView);
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.HORIZONTAL);

        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(new MsgAdapter());
    }


    private static class MsgViewHolder extends RecyclerView.ViewHolder {

        TextView tv;

        public MsgViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.fragment_match_item_content);
        }
    }

    private class MsgAdapter extends RecyclerView.Adapter<MsgViewHolder> {

        String[] words = new String[]{
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

        @NonNull
        @Override
        public MsgViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View msgView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_match_item, parent, false);
            return new MsgViewHolder(msgView);
        }

        @Override
        public void onBindViewHolder(@NonNull MsgViewHolder holder, int position) {
            holder.tv.setText(words[position]);
            holder.itemView.setOnClickListener(v -> {
                CharSequence text = holder.tv.getText();
                Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
            });
        }

        @Override
        public int getItemCount() {
            return words.length;
        }
    }

}
