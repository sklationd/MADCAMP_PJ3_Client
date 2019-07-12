package com.example.project3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerCommentAdapter extends RecyclerView.Adapter<RecyclerCommentAdapter.ViewHolder> {
    private ArrayList<Comment> mData = null;

    public class ViewHolder extends RecyclerView.ViewHolder {

        View mView;
        TextView author;
        TextView content;
        TextView time;

        ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            author = itemView.findViewById(R.id.author);
            content = itemView.findViewById(R.id.content);
            time = itemView.findViewById(R.id.time);
        }
    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    public RecyclerCommentAdapter(ArrayList<Comment> list) {
        mData = list;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public RecyclerCommentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.comment_item, parent, false);
        RecyclerCommentAdapter.ViewHolder vh = new RecyclerCommentAdapter.ViewHolder(view);
        return vh;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(RecyclerCommentAdapter.ViewHolder holder, final int position) {
        final Comment item = mData.get(position);
        holder.author.setText(item.getUsername());
        holder.content.setText(item.getComment());
        holder.time.setText(String.valueOf(item.getCreatedAt()));


    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        if (mData == null) {
            return 0;
        }
        return mData.size();
    }
}
