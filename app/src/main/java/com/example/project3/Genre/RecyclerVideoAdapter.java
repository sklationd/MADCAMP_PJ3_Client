package com.example.project3.Genre;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project3.Comment.CommentActivity;
import com.example.project3.Networking.RetroCallback;
import com.example.project3.Networking.RetroClient;
import com.example.project3.R;

import java.util.ArrayList;

public class RecyclerVideoAdapter extends RecyclerView.Adapter<RecyclerVideoAdapter.ViewHolder>{

    private ArrayList<VideoRecyclerItem> mData = null ;
    Context context;
    RetroClient retroClient;

    public class ViewHolder extends RecyclerView.ViewHolder {
//implements View.OnCreateContextMenuListener
        View mView;
        TextView user_post;
        ImageView delete;
        ImageView thumbnail;
        TextView title;
        TextView description;
        TextView comment;
        TextView createdAt;

        public ViewHolder(View itemView) {
            super(itemView) ;
            mView = itemView;
            user_post=itemView.findViewById(R.id.user_post);
            delete=itemView.findViewById(R.id.delete);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            comment=itemView.findViewById(R.id.comment);
            createdAt = itemView.findViewById(R.id.createdat);
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
        retroClient= RetroClient.getInstance(context).createBaseApi();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
        View view = inflater.inflate(R.layout.video_item, parent, false) ;
        ViewHolder vh = new ViewHolder(view) ;
        return vh ;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
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
        holder.createdAt.setText(item.getCreatedAt());
        holder.comment.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view){
                Intent intent=new Intent(context, CommentActivity.class);
                intent.putExtra("genre", item.getGenre());
                intent.putExtra("videoid", item.getVideoId());
                context.startActivity(intent);
            }
        });
        holder.delete.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view){
                AlertDialog.Builder alt_bld = new AlertDialog.Builder(view.getContext());
                alt_bld.setMessage("Do you want to delete the photo?").setCancelable(
                        false).setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                final int position = holder.getAdapterPosition();
                                String deletedauthor = mData.get(position).getAuthor();
                                int deletedgenre = mData.get(position).getGenre();
                                String deletedvideoid = mData.get(position).getVideoId();
                                retroClient.deleteVideo(deletedauthor, deletedgenre, deletedvideoid, new RetroCallback() {
                                    @Override
                                    public void onError(Throwable t) {
                                        Log.e("error", "deletecommenterror");
                                    }
                                    @Override
                                    public void onSuccess(int code, Object receivedData) {
                                        // 아답타에게 알린다
                                        mData.remove(position);
                                        notifyItemRemoved(position);
                                        notifyItemRangeChanged(position,mData.size());
                                    }
                                    @Override
                                    public void onFailure(int code) {
                                        Log.e("error", String.valueOf(code));
                                        if (code == 401) {
                                            Toast.makeText(context, "권한이 없습니다", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }).setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = alt_bld.create();
                // Title for AlertDialog
                alert.setTitle("DELETE");
                // Icon for AlertDialog
                alert.show();

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