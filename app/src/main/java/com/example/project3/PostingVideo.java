package com.example.project3;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class PostingVideo extends AppCompatActivity {
    String Username;
    String VideoId;
    String Title;
    String[] genres = new String[]{"HIPHOP", "POPPIN", "URBAN", "GIRLS", "WAACKING", "LOCKING"};
    int selectedIndex = 0;
    RetroClient retroClient;
    String token;
    Context context;
    Button selectGenre;
    String description;
    Button save;
    SharedPreferences sf;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        sf = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sf.edit();

        token = sf.getString("Token", null);
        Username = sf.getString("Id", null);
//        youtubeUrl = sf.getString("youtube_url", null);
//        VideoId = youtubeUrl.split("=")[1];
        setContentView(R.layout.posting_video);
        VideoId = parsing(sf.getString("youtube_url", null));
        Log.d("dsgfsdfg", VideoId);
        ImageView thumbnail = findViewById(R.id.thumnail);
        Glide.with(context).load("https://img.youtube.com/vi/" + VideoId + "/0.jpg").into(thumbnail);
        selectGenre = (Button) findViewById(R.id.select_genre);
        selectGenre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(PostingVideo.this);
                dialog.setTitle("Select Genre").setSingleChoiceItems(genres, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selectedIndex = i;
                    }
                }).setPositiveButton("save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(PostingVideo.this, genres[selectedIndex], Toast.LENGTH_SHORT).show();
                        selectGenre.setText(genres[selectedIndex]);
                    }
                }).create().show();
            }
        });
        save = (Button) findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                description = ((EditText) findViewById(R.id.description)).getText().toString();
                postVideo(Username, "Dsa", VideoId, selectedIndex, description);
            }
        });
    }

    public String parsing(String url) {
        String[] words = url.split("/");
        return words[3];
    }

    private void postVideo(String username, String title, String videoId, int genre, String Description) {
        retroClient = RetroClient.getInstance(this).createBaseApi();
        VideoInfo videoinfo = new VideoInfo();
        videoinfo.setUsername(username);
        videoinfo.setTitle(title);
        videoinfo.setVideoId(videoId);
        videoinfo.setGenre(genre);
        videoinfo.setDescription(Description);
        retroClient.addVideo(videoinfo, new RetroCallback() {
            @Override
            public void onError(Throwable t) {
                Log.d("DEBUGDEBUGDEBUGDEBUG", t.toString());
                Log.e("error", "addvideo error");
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                VideoInfo data = ((ResponseInfo_posting) receivedData).getData();
                Log.d("aa", data.getTitle());
                editor.remove("youtube_url");
                finish();
            }

            @Override
            public void onFailure(int code) {
            }
        });

    }
}
