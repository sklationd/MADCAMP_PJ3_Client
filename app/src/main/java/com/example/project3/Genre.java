package com.example.project3;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project3.Video.RecyclerVideoAdapter;
import com.example.project3.Video.VideoRecyclerItem;

import java.io.File;
import java.util.ArrayList;

public class Genre extends AppCompatActivity {
    //private String[] images = {"HIPHOP", "POPPIN", "URBAN", "GIRLS", "WAACKING", "LOCKING"};

    private RecyclerView mRecyclerView;
    private RecyclerVideoAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<VideoRecyclerItem> mMyData = new ArrayList<VideoRecyclerItem>();
    private View view;
    private Context context;
    String url;
    public ArrayList<String> UserNames = new ArrayList<>();
    public ArrayList<String> VideoIds = new ArrayList<>();
    public ArrayList<String> Descriptions = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.genre);
        context = getApplicationContext();
        view = findViewById(R.id.genre_rootview);
        Toolbar toolbar = findViewById(R.id.genre_toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        int position = (int) intent.getIntExtra("position", 0);
        viewInit();
    }

    private void viewInit(){
        mMyData = new ArrayList<>();
        VideoRecyclerItem item1 = new VideoRecyclerItem();
        VideoRecyclerItem item2 = new VideoRecyclerItem();
        VideoRecyclerItem item3 = new VideoRecyclerItem();
        VideoRecyclerItem item4 = new VideoRecyclerItem();
        item1.setTitle("비행");
        item2.setTitle("Anecdote");
        item3.setTitle("손님");
        item4.setTitle("Back in time");
        item1.setVideoId("-gZlOkTAU08");
        item2.setVideoId("AmFhq6fRnpk");
        item3.setVideoId("RbpgHqLrkTI");
        item4.setVideoId("ndSn6uRZNsU");
        mMyData.add(item1);
        mMyData.add(item2);
        mMyData.add(item3);
        mMyData.add(item4);

        mAdapter = new RecyclerVideoAdapter(mMyData);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.video_recycler);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(0);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("로그아웃");
                alertDialogBuilder.setMessage("로그아웃 하시겠습니까??").setCancelable(false).setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog, int id) {
                                SharedPreferences sf = PreferenceManager.getDefaultSharedPreferences(context);
                                SharedPreferences.Editor editor = sf.edit();
                                editor.remove("Id");
                                editor.remove("Pw");
                                editor.remove("Token");
                                editor.apply();
                                startActivity(new Intent(getApplicationContext(), SplashActivity.class));
                                finish();
                            }
                        })
                        .setNegativeButton("취소",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
