package com.example.project3;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class PostingVideo extends AppCompatActivity {
    RetroClient retroClient;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.posting_video);
        SharedPreferences sf = PreferenceManager.getDefaultSharedPreferences(context);
        String videoId = parsing(sf.getString("youtube_url", null));
        ImageView thumbnail = findViewById(R.id.thumnail);
        Glide.with(context).load("https://img.youtube.com/vi/"+videoId+"/0.jpg").into(thumbnail);
        retroClient= RetroClient.getInstance(this).createBaseApi();
        Button selectGenre = (Button) findViewById(R.id.select_genre);
        selectGenre.


    }
    public String parsing(String url){
        String[] words = url.split("/");
        return words[3];
    }

}
