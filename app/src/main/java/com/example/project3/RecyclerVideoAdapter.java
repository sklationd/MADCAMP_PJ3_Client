package com.example.project3;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RecyclerVideoAdapter extends RecyclerView
        .Adapter<RecyclerVideoAdapter.ViewHolder>{

    private ArrayList<VideoRecyclerItem> mData = null ;
    Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {

        View mView;
        TextView user_post;
        ImageView thumbnail;
        TextView title;
        TextView description;
        Button comment;

        ViewHolder(View itemView) {
            super(itemView) ;
            mView = itemView;
            user_post=itemView.findViewById(R.id.user_post);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            comment=itemView.findViewById(R.id.comment);
        }
    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    public RecyclerVideoAdapter(ArrayList<VideoRecyclerItem> list) {
        mData = list ;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
        View view = inflater.inflate(R.layout.video_item, parent, false) ;
        ViewHolder vh = new ViewHolder(view) ;
        return vh ;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(ViewHolder holder,final int position) {
        final VideoRecyclerItem item = mData.get(position) ;
        Glide.with(holder.thumbnail.getContext()).load("https://img.youtube.com/vi/"+item.getVideoId()+"/0.jpg").into(holder.thumbnail);
        //Clickable imageview
        holder.thumbnail.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v="+item.getVideoId()));
                context.startActivity(webIntent);
            }
        });
        holder.title.setText(item.getTitle());
        holder.user_post.setText(item.getAuthor());
        holder.description.setText(item.getDescription());

        holder.comment.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view){
                Intent intent=new Intent(context, CommentActivity.class);
                intent.putExtra("genre", item.getGenre());
                intent.putExtra("videoid", item.getVideoId());
                context.startActivity(intent);
            }
        });
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        if(mData==null){
            return 0;
        }
        return mData.size() ;
    }
}