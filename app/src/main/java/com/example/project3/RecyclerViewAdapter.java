package com.example.project3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";
    // private String tel;

    public ArrayList<String> UserNames = new ArrayList<>();
    public ArrayList<String> VideoIds = new ArrayList<>();
    public ArrayList<String> Descriptions = new ArrayList<>();
    public Context mContext;

    public RecyclerViewAdapter(ArrayList<String> UserNames, ArrayList<String> VideoIds, ArrayList<String> Descriptions, Context mContext) {
        this.UserNames = UserNames;
        this.VideoIds = VideoIds;
        this.Descriptions = Descriptions;
        this.mContext = mContext;
    }

    // video didn't have @NonNull
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    // this is where the images and their corresponding names are loaded from layout_listitem.xml
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Log.d(TAG, "onBindViewHolder: called.");  // this is simply for helping with debugging if needed
        holder.videoid.setText(VideoIds.get(position));
        holder.description.setText(Descriptions.get(position));
        holder.username.setText(UserNames.get(position));
        // trying to open new page if you click a contact
//        holder.name.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d(TAG, "onClick: clicked on: " +  tel);
//                //String tel = "tel" + holder.name.getText();
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + tel));
//                view.getContext().startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return UserNames.size();
    }

//    @Override
//    public int getItemViewType(int position){
//        int viewType = 1;
//        return viewType;
//    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView videoid;
        TextView description;
        TextView username;
        RelativeLayout parentLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            videoid = itemView.findViewById(R.id.videoid);
            description = itemView.findViewById(R.id.description);
            username = itemView.findViewById(R.id.username);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
