package com.example.project3.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.project3.MainActivity;
import com.example.project3.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

public class RegisterActivity extends AppCompatActivity {
    SharedPreferences sf;
    SharedPreferences.Editor editor;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        sf = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sf.edit();
        final EditText usernameEditText = findViewById(R.id.username_register);
        final EditText nicknameEditText = findViewById(R.id.nickname_register);
        final EditText passwordEditText = findViewById(R.id.password_register);
        final EditText passwordEditTextConfirm = findViewById(R.id.password_register_confirm);

        final Button registerButton = findViewById(R.id.register_request);

        passwordEditTextConfirm.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    register(usernameEditText.getText().toString(), passwordEditText.getText().toString(), nicknameEditText.getText().toString(), passwordEditTextConfirm.getText().toString());
                }
                return false;
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register(usernameEditText.getText().toString(), passwordEditText.getText().toString(), nicknameEditText.getText().toString(), passwordEditTextConfirm.getText().toString());
            }
        });
    }

    private void register(String id, String password, String nickname, String password_confirm){
        String url = getApplicationContext().getString(R.string.register);
        try{
            new MyRegister().execute(url, id, nickname, password, password_confirm).get();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private class MyRegister extends AsyncTask<String, Void, Boolean> {
        final ProgressBar loadingProgressBar = findViewById(R.id.loading_register);
        String Errormessage = null;
        @Override
        public void onPreExecute(){
            loadingProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public Boolean doInBackground(String... params) {
            String line;
            String page = "";
            try{
                JSONObject json = new JSONObject();
                json.accumulate("username", params[1]);
                json.accumulate("nickname", params[2]);
                json.accumulate("password", params[3]);
                json.accumulate("passwordConfirmation", params[4]);

                //connect
                URL url = new URL(params[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.connect();

                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                writer.write(json.toString());
                writer.flush();
                writer.close();

                int response = conn.getResponseCode();
                if(response == 200) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                    while ((line = reader.readLine()) != null) {
                        page += line;
                    }
                }
                if(conn != null){
                    conn.disconnect();
                }
                JSONObject loginresult = new JSONObject(page);
                if(loginresult.getBoolean("success")){
                    editor.putString("Id",params[1]);
                    editor.putString("Pw",params[2]);
                    editor.apply();
                    return true;
                }
                else {
                    JSONObject errors = loginresult.getJSONObject("errors");
                    Iterator<String> i = errors.keys();
                    String error_name = i.next();
                    Errormessage = errors.getJSONObject(error_name).getString("message");
                    return false;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        public void onPostExecute(Boolean result){
            // if token is null -> login fail
            if(result){
                String url = getApplicationContext().getString(R.string.login_uri);
                try {
                    String Token = new MyLogin().execute(url, sf.getString("Id", null), sf.getString("Pw", null)).get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                finish();
            }
            else{
                Toast.makeText(getApplicationContext(), Errormessage , Toast.LENGTH_SHORT).show();
            }
            loadingProgressBar.setVisibility(View.INVISIBLE);
        }
    }

}
