package com.example.project3;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class PostingVideo extends AppCompatActivity {
    //genre list
    String[] genres = new String[]{"HIPHOP", "POPPIN", "URBAN", "GIRLS", "WAACKING", "LOCKING"};

    //shared preference
    SharedPreferences sf;
    SharedPreferences.Editor editor;

    //argument
    String Username;
    String VideoId;
    String token;
    String Title;
    String description;
    String youtube_url;
    int selectedIndex = 0;
    boolean user_select = false;

    //for http request
    Context context;
    RetroClient retroClient;

    //xml object
    Button selectGenre;
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initial setting
        context = getApplicationContext();
        sf = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sf.edit();
        setContentView(R.layout.posting_video);

        //toolbar
        Toolbar toolbar = findViewById(R.id.posting_toolbar);
        setSupportActionBar(toolbar);

        //initialize argument
        token = sf.getString("Token", null);
        Username = sf.getString("Id", null);
        VideoId = parsing(sf.getString("youtube_url", null));
        ImageView thumbnail = findViewById(R.id.post_thumbnail);

        Glide.with(context).load("https://img.youtube.com/vi/" + VideoId + "/0.jpg").into(thumbnail);
        youtube_url = sf.getString("youtube_url",null);

        try {
            Title = new getTitle().execute(VideoId).get();
            if(Title != null){
                Log.d("DEBUG", Title);
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        selectGenre = (Button) findViewById(R.id.select_genre);
        selectGenre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(PostingVideo.this);
                dialog.setTitle("Select Genre").setSingleChoiceItems(genres, selectedIndex, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d("INDEX",Integer.toString(i));
                        selectedIndex = i;
                        user_select = true;
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_posting, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_post:
                description = ((EditText) findViewById(R.id.description)).getText().toString();
                if (!user_select) {
                    Toast.makeText(PostingVideo.this, "장르를 선택해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    if(Title == null){
                        Log.d("ERROR","Failed to get title");
                    }
                    postVideo(Username, Title, VideoId, selectedIndex, description);
                }
            default:
                return super.onOptionsItemSelected(item);
        }
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
                Log.d("DEBUG", t.toString());
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                editor.remove("youtube_url");
                editor.apply();
                finish();
            }

            @Override
            public void onFailure(int code) {

            }
        });

    }

    private class getTitle extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                return GET(params[0]);
            } catch (IOException e) {
                return null;
            }
        }

        @Override
        public void onPostExecute(String result) {
            Log.d("Title", result);
            Title = result;
        }

    }

    private String GET(String VideoId) throws IOException {
        InputStream inputStream = null;
        try {
            URL url = new URL("http://www.youtube.com/oembed?url=" + youtube_url + "&format=json");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            InputStream in = con.getInputStream();
            con.connect();

            String line = null;
            String page = "";
            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            while ((line = reader.readLine()) != null) {
                page += line;
            }
            JSONObject jsonObj = new JSONObject(page);
            String str = jsonObj.getString("title");
            Log.d("TITLE", str);
            return str;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null)
                inputStream.close();
        }
        return null;
    }
}
