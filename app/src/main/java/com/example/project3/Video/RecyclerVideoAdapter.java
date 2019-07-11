package com.example.project3.Video;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project3.R;

import java.util.ArrayList;

public class RecyclerVideoAdapter extends RecyclerView
        .Adapter<RecyclerVideoAdapter.ViewHolder>{

    private ArrayList<VideoRecyclerItem> mData = null ;

    public class ViewHolder extends RecyclerView.ViewHolder {

        View mView;
        ImageView thumbnail;
        TextView title;

        ViewHolder(View itemView) {
            super(itemView) ;
            mView = itemView;
            thumbnail = itemView.findViewById(R.id.thumbnail);
            title = itemView.findViewById(R.id.title);
        }
    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    public RecyclerVideoAdapter(ArrayList<VideoRecyclerItem> list) {
        mData = list ;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
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
        holder.title.setText(item.getTitle());

        //holder 내용 추가(여기서 초기화)
//        holder.icon.setImageDrawable(item.getIcon()) ;
//        holder.name.setText(item.getName()) ;
//        holder.phone.setText(item.getPhone()) ;
//
//        holder.mView.setOnLongClickListener(new View.OnLongClickListener(){
//            @Override
//            public boolean onLongClick(View v){
//                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TabFragment1.getContextOfApplication());
//                alertDialogBuilder.setTitle("연락처 삭제");
//                alertDialogBuilder.setMessage("연락처를 삭제하시겠습니까?").setCancelable(false).setPositiveButton("삭제",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(
//                                    DialogInterface dialog, int id) {
//                                removeItem(position);
//                            }
//                        })
//                        .setNegativeButton("취소",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(
//                                            DialogInterface dialog, int id) {
//                                        dialog.cancel();
//                                    }
//                                });
//                AlertDialog alertDialog = alertDialogBuilder.create();
//                alertDialog.show();
//                return true;
//            }
//        });
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