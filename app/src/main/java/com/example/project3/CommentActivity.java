package com.example.project3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CommentActivity extends AppCompatActivity {
    private ArrayList<Comment> mMyData = new ArrayList<>();
    private ArrayList<Comment> newData = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RecyclerCommentAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private View view;
    int genre;
    String videoid;
    private static Context context;
    String Token;
    String Username;
    EditText yourcomment;
    String comment;
    Button savecomment;

    RetroClient retroClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        SharedPreferences sf = PreferenceManager.getDefaultSharedPreferences(context);
        Token = sf.getString("Token", null);
        Username = sf.getString("Id", null);
        setContentView(R.layout.comment);
        view = findViewById(R.id.comment_rootview);
        savecomment = (Button) findViewById(R.id.saveyourcomment);

        Intent intent = getIntent();
        genre = (int) intent.getIntExtra("genre", 0);
        videoid = (String) intent.getStringExtra("videoid");
        retroClient = RetroClient.getInstance(this).createBaseApi();
        initCommentByGenreAndVideoId(genre, videoid);
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        savecomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yourcomment = (EditText) findViewById(R.id.yourcomment);
                comment = yourcomment.getText().toString();
                if (comment.matches("")){
                    Toast.makeText(context, "댓글을 입력하세요", Toast.LENGTH_SHORT).show();
                }else{
                    postComment(videoid, genre, comment);
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(yourcomment.getWindowToken(), 0);
                    yourcomment.setText(null);
                }
            }
        });

    }

    private void initCommentByGenreAndVideoId(int genre, String videoid) {
        retroClient.getCommentByGenreAndVideoId(genre, videoid, new RetroCallback() {
            @Override
            public void onError(Throwable t) {
                Log.d("실패", t.toString());
                Log.e("error", "initVideoByGenre error");
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                List<Comment> data = ((ResponseInfo_comment) receivedData).getData();
                //String str = data.get(0).toString();
                //Log.d("성공", str);
                for (int i = 0; i < data.size(); i++) {
                    Comment item = new Comment();
                    item.setUsername(data.get(i).getUsername());
                    item.setGenre(data.get(i).getGenre());
                    item.setVideoId(data.get(i).getVideoId());
                    item.setComment(data.get(i).getComment());
                    item.setCreatedAt(data.get(i).getCreatedAt());
                    mMyData.add(item);
                }
                initRecyclerView();
            }
            @Override
            public void onFailure(int code) {
            }
        });
    }

    private void initRecyclerView() {
        mAdapter = new RecyclerCommentAdapter(mMyData);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.comment_recycler);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(0);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }
    private void postComment(String videoId, final int genre, final String Description) {
        retroClient = RetroClient.getInstance(this).createBaseApi();
        Comment commentinfo = new Comment();
        commentinfo.setVideoId(videoId);
        commentinfo.setGenre(genre);
        commentinfo.setComment(Description);
        retroClient.addComment(commentinfo, Username, new RetroCallback() {
            @Override
            public void onError(Throwable t) {
                Log.d("DEBUGDEBUGDEBUGDEBUG", t.toString());
                Log.e("error", "addvideo error");
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                Comment data = ((ResponseInfo_comment_posting) receivedData).getData();
                Log.d("aa", data.getUsername());
                mMyData.clear();
                initCommentByGenreAndVideoId(genre, videoid);
                //AddComment(genre, videoid, Description);
                //finish();
            }

            @Override
            public void onFailure(int code) {
            }
        });

    }
    private void AddComment(int genre, String videoid, String description) {
        mMyData.clear();
        initCommentByGenreAndVideoId(genre, videoid);
        Comment item = new Comment();
        item.setUsername(Username);
        item.setGenre(genre);
        item.setVideoId(videoid);
        item.setComment(description);
        //mMyData.add(item);
        //mAdapter.notifyItemInserted(mAdapter.getItemCount());
    }

}
