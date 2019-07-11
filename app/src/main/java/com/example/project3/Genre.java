package com.example.project3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class Genre extends AppCompatActivity {
    private ArrayList<String> UserNames = new ArrayList<>();
    private ArrayList<String> VideoIds = new ArrayList<>();
    private ArrayList<String> Descriptions = new ArrayList<>();
    private static Context context;
    String Token;
    String Username;
    private String[] images = {"HIPHOP", "POPPIN", "URBAN", "GIRLS", "WAACKING", "LOCKING"};

    RetroClient retroClient;

    public static Context getParentContext(){
        return context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        SharedPreferences sf = PreferenceManager.getDefaultSharedPreferences(context);
        Token = sf.getString("Token",null);
        Username = sf.getString("Id",null);
        setContentView(R.layout.genre);
        Toolbar toolbar = findViewById(R.id.genre_toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        int position = (int) intent.getIntExtra("position",0);
        TextView mainGenre = (TextView) findViewById(R.id.maingenre);
        mainGenre.setText("Main Genre is " + images[position]);
        retroClient= RetroClient.getInstance(this).createBaseApi();
        initVideoByGenre(position);
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
                SharedPreferences sf = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sf.edit();
                editor.remove("Id");
                editor.remove("Pw");
                editor.remove("Token");
                editor.apply();
                startActivity(new Intent(this, SplashActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initVideoByGenre(int genre){
        retroClient.getVideoByGenre(genre, new RetroCallback() {
            @Override
            public void onError(Throwable t) {
                Log.d("DEBUGDEBUGDEBUGDEBUG",t.toString());
                Log.e("error", "initVideoByGenre error");
            }
            @Override
            public void onSuccess(int code, Object receivedData) {
                List<VideoInfo> data = ((ResponseInfo) receivedData).getData();
                for (int i=0; i<data.size(); i++){
                    UserNames.add(data.get(i).getUsername());
                    VideoIds.add(data.get(i).getVideoId());
                    Descriptions.add(data.get(i).getDescription());
                }
                Toast.makeText(context, Token , Toast.LENGTH_SHORT).show();
                initRecyclerView(UserNames, VideoIds, Descriptions);
            }
            @Override
            public void onFailure(int code) {
            }
        });
    }
    private void initRecyclerView(final ArrayList<String> UserNames, final ArrayList<String> VideoIds, final ArrayList<String> Descriptions) {
        final RecyclerView recyclerView = findViewById(R.id.recycler_view);
        final RecyclerViewAdapter adapter = new RecyclerViewAdapter(UserNames, VideoIds, Descriptions,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        swipeController = new SwipeController(new SwipeControllerActions() {
//            @Override
//            public void onRightClicked(final int position) {
//                //RetroClient retroClient = RetroClient.getInstance(this).createBaseApi();
//                String deletename = Names.get(position);
////                Names.remove(position);
////                Numbers.remove(position);
////                PhotosStr.remove(position);
//                retroClient.deleteContact(name, deletename, new RetroCallback() {
//                    @Override
//                    public void onError(Throwable t) {
//                        Log.e("error", "deletecintacterror");
//                    }
//                    @Override
//                    public void onSuccess(int code, Object receivedData) {
//                        // 아답타에게 알린다
//                        adapterTab1.Names.remove(position);
//                        adapterTab1.Numbers.remove(position);
//                        adapterTab1.PhotosStr.remove(position);
//                        adapterTab1.notifyItemRemoved(position);
//                        adapterTab1.notifyItemRangeChanged(position, adapterTab1.getItemCount());
//                    }
//                    @Override
//                    public void onFailure(int code) {
//                        Log.e("error", "ddddd");
//                    }
//                });
//            }
//        });
//        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
//        itemTouchhelper.attachToRecyclerView(recyclerViewtab1);
//        recyclerViewtab1.addItemDecoration(new RecyclerView.ItemDecoration() {
//            @Override
//            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
//                swipeController.onDraw(c);
//            }
//        });
    }
    private void postVideo(String username, String videoId, int genre, String Description){
        VideoInfo videoinfo = new VideoInfo;
        videoinfo.setUsername(username);
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
                List<VideoInfo> data = ((ResponseInfo) receivedData).getData();
                for (int i=0; i<data.size(); i++){
                    UserNames.add(data.get(i).getUsername());
                    VideoIds.add(data.get(i).getVideoId());
                    Descriptions.add(data.get(i).getDescription());
                }
                Toast.makeText(context, Token , Toast.LENGTH_SHORT).show();
                initRecyclerView(UserNames, VideoIds, Descriptions);
            }
            @Override
            public void onFailure(int code) {
            }
        });
    }


}
