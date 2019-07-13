package com.example.project3.Comment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.project3.Networking.ResponseInfo_comment;
import com.example.project3.Networking.ResponseInfo_comment_posting;
import com.example.project3.Networking.RetroCallback;
import com.example.project3.Networking.RetroClient;
import com.example.project3.R;
import com.example.project3.SwipeController;
import com.example.project3.SwipeControllerActions;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;

import java.util.ArrayList;
import java.util.List;

public class CommentActivity extends AppCompatActivity {
    private ArrayList<Comment> mMyData = new ArrayList<>();
    private ArrayList<String> commentId = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RecyclerCommentAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SwipeController swipeController = null;
    private View view;
    private SlidrInterface slidr;
    int genre;
    String videoid;
    private static Context context;
    String Token;
    String Username;
    EditText yourcomment;
    String comment;
    Button savecomment;
    SwipeRefreshLayout refreshLayout;
    String deletedcomment;
    String deletedauthor;
    int position;

    RetroClient retroClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.comment);
        view = findViewById(R.id.comment_rootview);

        //Shared Preferences
        SharedPreferences sf = PreferenceManager.getDefaultSharedPreferences(context);
        Token = sf.getString("Token", null);
        Username = sf.getString("Id", null);

        savecomment = (Button) findViewById(R.id.saveyourcomment);
        Intent intent = getIntent();
        genre = (int) intent.getIntExtra("genre", 0);
        videoid = (String) intent.getStringExtra("videoid");

        //pull to request
        refreshLayout = findViewById(R.id.comment_refresh_layout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initCommentByGenreAndVideoId(genre, videoid);
            }
        });

        retroClient = RetroClient.getInstance(this).createBaseApi();
        initCommentByGenreAndVideoId(genre, videoid);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        savecomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yourcomment = (EditText) findViewById(R.id.yourcomment);
                comment = yourcomment.getText().toString();
                if (comment.matches("")) {
                    Toast.makeText(context, "댓글을 입력하세요", Toast.LENGTH_SHORT).show();
                } else {
                    postComment(videoid, genre, comment);
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(yourcomment.getWindowToken(), 0);
                    yourcomment.setText(null);
                }
            }
        });

        slidr = Slidr.attach(this);
        slidr.unlock();
    }

    private void initCommentByGenreAndVideoId(int genre, String videoid) {
        refreshLayout.setRefreshing(true);
        retroClient.getCommentByGenreAndVideoId(genre, videoid, new RetroCallback() {
            @Override
            public void onError(Throwable t) {
                refreshLayout.setRefreshing(false);
                Log.e("error", "initVideoByGenre error");
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                refreshLayout.setRefreshing(false);
                mMyData.clear();
                commentId.clear();
                List<Comment> data = ((ResponseInfo_comment) receivedData).getData();
                for (int i = 0; i < data.size(); i++) {
                    Comment item = new Comment();
                    item.setUsername(data.get(i).getUsername());
                    item.setGenre(data.get(i).getGenre());
                    item.setVideoId(data.get(i).getVideoId());
                    item.setComment(data.get(i).getComment());
                    item.setCreatedAt(data.get(i).getCreatedAt());
                    mMyData.add(item);
                    //String com = data.get(i).get_id();
                    commentId.add(data.get(i).get_id());
                }
                initRecyclerView();
            }

            @Override
            public void onFailure(int code) {
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void initRecyclerView() {
        mAdapter = new RecyclerCommentAdapter(mMyData);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.comment_recycler);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        if (mMyData.size()==0){
            position=0;
        }else{
            position=mMyData.size()-1;
        }
        mRecyclerView.scrollToPosition(position);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onSwiped(final int position){

            }

            @Override
            public void onRightClicked(final int position) {
                //RetroClient retroClient = RetroClient.getInstance(this).createBaseApi();
                deletedcomment = commentId.get(position);
                deletedauthor = mMyData.get(position).getUsername();
                retroClient.deleteComment(deletedauthor, deletedcomment, new RetroCallback() {
                    @Override
                    public void onError(Throwable t) {
                        Log.e("error", "deletecommenterror");
                    }

                    @Override
                    public void onSuccess(int code, Object receivedData) {
                        // 아답타에게 알린다
                        mAdapter.mData.remove(position);
                        mAdapter.notifyItemRemoved(position);
                        mAdapter.notifyItemRangeChanged(position, mAdapter.getItemCount());
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
        });
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(mRecyclerView);
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });
    }

    private void postComment(String videoId, final int genre, final String Description) {
        Comment commentinfo = new Comment();
        commentinfo.setVideoId(videoId);
        commentinfo.setGenre(genre);
        commentinfo.setComment(Description);
        retroClient.addComment(commentinfo, Username, new RetroCallback() {
            @Override
            public void onError(Throwable t) {
                Log.d("DEBUG", t.toString());
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                Comment data = ((ResponseInfo_comment_posting) receivedData).getData();
                Log.d("aa", data.getUsername());
                mMyData.clear();
                initCommentByGenreAndVideoId(genre, videoid);
            }

            @Override
            public void onFailure(int code) {
            }
        });

    }


}
