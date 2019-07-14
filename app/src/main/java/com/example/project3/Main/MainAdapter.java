package com.example.project3.Main;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.example.project3.Genre.Genre;
import com.example.project3.R;

public class MainAdapter extends PagerAdapter {
    private int[] images = {R.drawable.one, R.drawable.two, R.drawable.three, R.drawable.four, R.drawable.five, R.drawable.six};
    private String[] genres = {"HIPHOP", "POPPIN", "URBAN", "GIRLS", "WAACKING", "LOCKING"};
    private LayoutInflater inflater;
    private Context mcontext = null;
    private int length = images.length;
    public MainAdapter(){

    }
    public MainAdapter(Context context){
        mcontext = context;
    }
    @Override
    public int getCount() {
        return length;
    }
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View) object);
    }
    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        Log.d("position",Integer.toString(position));
        inflater = (LayoutInflater)mcontext.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.slider, container, false);
        ImageView imageView = (ImageView)v.findViewById(R.id.circleimage);
        TextView textView = (TextView)v.findViewById(R.id.genre_description);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("imageview","onclicked");
                Intent intent=new Intent(MainActivity.getParentContext(), Genre.class);
                intent.putExtra("position", position);
                MainActivity.getParentContext().startActivity(intent);
            }
        });
        imageView.setImageResource(images[position]);
        textView.setText(genres[position]);
        container.addView(v);
        return v;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.invalidate();
    }
}