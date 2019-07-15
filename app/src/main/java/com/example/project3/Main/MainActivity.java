package com.example.project3.Main;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.example.project3.R;
import com.example.project3.SplashActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    int GLOBAL_TOUCH_POSITION_X = 0;
    int GLOBAL_TOUCH_CURRENT_POSITION_X = 0;
    private ArrayList<Integer> genrelist =new ArrayList<Integer>();
    MainAdapter adapter;
    ViewPager viewPager;
    View view;
    public static Context context;

    public static Context getParentContext(){
        return context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.activity_main);
        view = findViewById(R.id.main_rootview);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //"HIPHOP", "POPPIN", "URBAN", "GIRLS", "WAACKING", "LOCKING"
        genrelist.add(R.drawable.one);
        genrelist.add(R.drawable.two);
        genrelist.add(R.drawable.three);
        genrelist.add(R.drawable.four);
        genrelist.add(R.drawable.five);
        genrelist.add(R.drawable.six);

        view.setOnTouchListener(new View.OnTouchListener(){
                @Override
                public boolean onTouch(View v, MotionEvent m) {
                    handleTouch(m);
                    return true;
                }
        });

        viewPager= (ViewPager) findViewById(R.id.viewpager);
        adapter = new MainAdapter(this);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
        //swipe to youtube
//        viewPager.setOnTouchListener(new OnSwipeTouchListener(context) {
//            public void onSwipeTop() {
//                Toast.makeText(MainActivity.this, "top", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(
//                        Intent.ACTION_VIEW,
//                        Uri.parse( "http://youtu.be/"));
//                startActivity( intent );
//
//            }
//            public void onSwipeRight() {
//                Toast.makeText(MainActivity.this, "right", Toast.LENGTH_SHORT).show();
//            }
//            public void onSwipeLeft() {
//                Toast.makeText(MainActivity.this, "left", Toast.LENGTH_SHORT).show();
//            }
//            public void onSwipeBottom() {
//                Toast.makeText(MainActivity.this, "bottom", Toast.LENGTH_SHORT).show();
//            }
//
//        });
    }
    void handleTouch(MotionEvent m){
        //Number of touches
        int pointerCount = m.getPointerCount();
        if(pointerCount == 2){
            int action = m.getActionMasked();
            int actionIndex = m.getActionIndex();
            String actionString;
            switch (action)
            {
                case MotionEvent.ACTION_DOWN:
                    GLOBAL_TOUCH_POSITION_X = (int) m.getX(1);
                    actionString = "DOWN"+" current "+GLOBAL_TOUCH_CURRENT_POSITION_X+" prev "+GLOBAL_TOUCH_POSITION_X;
                    Toast.makeText(context, actionString, Toast.LENGTH_SHORT).show();
                    Log.d("손가락", "ACTION_DOWN");
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    Log.d("차이포인터업", String.valueOf(GLOBAL_TOUCH_CURRENT_POSITION_X-GLOBAL_TOUCH_POSITION_X));
                    if (GLOBAL_TOUCH_CURRENT_POSITION_X-GLOBAL_TOUCH_POSITION_X>30){
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse( "http://youtu.be/"));
                        startActivity(intent);
                        GLOBAL_TOUCH_POSITION_X = 0;
                        GLOBAL_TOUCH_CURRENT_POSITION_X = 0;
                    }
                    GLOBAL_TOUCH_POSITION_X = 0;
                    GLOBAL_TOUCH_CURRENT_POSITION_X = 0;
                    break;
                case MotionEvent.ACTION_UP:
                    actionString = "UP"+" current "+GLOBAL_TOUCH_CURRENT_POSITION_X+" prev "+GLOBAL_TOUCH_POSITION_X;
                    Log.d("손가락", "ACTION_UP");
                    break;
                case MotionEvent.ACTION_MOVE:
                    GLOBAL_TOUCH_CURRENT_POSITION_X = (int) m.getX(1);
                    int diff = GLOBAL_TOUCH_POSITION_X-GLOBAL_TOUCH_CURRENT_POSITION_X;
//                    actionString = "Diff "+diff+" current "+GLOBAL_TOUCH_CURRENT_POSITION_X+" prev "+GLOBAL_TOUCH_POSITION_X;
//                    Toast.makeText(context, actionString, Toast.LENGTH_SHORT).show();
                    Log.d("손가락", "ACTION_MOVE");
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    GLOBAL_TOUCH_POSITION_X = (int) m.getX(1);
//                    actionString = "DOWN"+" current "+GLOBAL_TOUCH_CURRENT_POSITION_X+" prev "+GLOBAL_TOUCH_POSITION_X;
//                    Toast.makeText(context, actionString, Toast.LENGTH_SHORT).show();
                    Log.d("손가락2", "ACTION_POINTER_DOWN");
                    break;
                default:
                    actionString = "";
            }
            pointerCount = 0;
        }
        else {
            GLOBAL_TOUCH_POSITION_X = 0;
            GLOBAL_TOUCH_CURRENT_POSITION_X = 0;
        }
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
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                alertDialogBuilder.setTitle("로그아웃");
                alertDialogBuilder.setMessage("로그아웃 하시겠습니까??").setCancelable(false).setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog, int id) {
                                SharedPreferences sf = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
