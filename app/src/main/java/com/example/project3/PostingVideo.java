package com.example.project3;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class PostingVideo extends AppCompatActivity {
    //RetroClient retroClient;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.posting_video);
        SharedPreferences sf = PreferenceManager.getDefaultSharedPreferences(context);
        String videoId = parsing(sf.getString("youtube_url", null));
        Log.d("dsgfsdfg", videoId);
        ImageView thumbnail = findViewById(R.id.thumnail);
        Glide.with(context).load("https://img.youtube.com/vi/" + videoId + "/0.jpg").into(thumbnail);
        //retroClient = RetroClient.getInstance(this).createBaseApi();
        Button selectGenre = (Button) findViewById(R.id.select_genre);
        //selectGenre.


    }

    public String parsing(String url) {
        String[] words = url.split("/");
        return words[3];
    }

//    private void postVideo(String username, String videoId, int genre, String Description) {
//        VideoInfo videoinfo = new VideoInfo;
//        videoinfo.setUsername(username);
//        videoinfo.setVideoId(videoId);
//        videoinfo.setGenre(genre);
//        videoinfo.setDescription(Description);
//        retroClient.addVideo(videoinfo, new RetroCallback() {
//            @Override
//            public void onError(Throwable t) {
//                Log.d("DEBUGDEBUGDEBUGDEBUG", t.toString());
//                Log.e("error", "addvideo error");
//            }
//
//            @Override
//            public void onSuccess(int code, Object receivedData) {
//                List<VideoInfo> data = ((ResponseInfo) receivedData).getData();
//                for (int i = 0; i < data.size(); i++) {
//                    UserNames.add(data.get(i).getUsername());
//                    VideoIds.add(data.get(i).getVideoId());
//                    Descriptions.add(data.get(i).getDescription());
//                }
//                Toast.makeText(context, Token, Toast.LENGTH_SHORT).show();
//                initRecyclerView(UserNames, VideoIds, Descriptions);
//            }
//
//            @Override
//            public void onFailure(int code) {
//            }
//        });

}
