package com.example.project3;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Genre extends AppCompatActivity{
    private ImageView genre;
    private ArrayList<Integer> genrelist =new ArrayList<Integer>();
//    private int swipeLeft = 0;
//    private int swipeRight = 0;
    private int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.genre);
        genrelist.add(R.drawable.one);
        genrelist.add(R.drawable.two);
        genrelist.add(R.drawable.three);
        genrelist.add(R.drawable.four);
        genrelist.add(R.drawable.five);
        genrelist.add(R.drawable.six);
        genre = findViewById(R.id.genre);
        genre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "hiphop", Toast.LENGTH_SHORT).show();
            }
        });

        genre.setOnTouchListener(new OnSwipeTouchListener(Genre.this) {
            public void onSwipeTop() {
                Toast.makeText(Genre.this, "top", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeRight() {
                Toast.makeText(Genre.this, "right", Toast.LENGTH_SHORT).show();
                position = (position==0) ? 5:position-1;
                genre.setImageResource(genrelist.get(position));

            }
            public void onSwipeLeft() {
                Toast.makeText(Genre.this, "left", Toast.LENGTH_SHORT).show();
                position = (position==5) ? 0:position+1;
                genre.setImageResource(genrelist.get(position));

            }
            public void onSwipeBottom() {
                Toast.makeText(Genre.this, "bottom", Toast.LENGTH_SHORT).show();

            }

        });
    }


}
