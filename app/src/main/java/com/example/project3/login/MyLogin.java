package com.example.project3.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.example.project3.SplashActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MyLogin extends AsyncTask<String, Void, String> {
    SharedPreferences sf;
    SharedPreferences.Editor editor;
    Context applicationContext;
    @Override
    public void onPreExecute(){
        applicationContext = SplashActivity.getContextOfApplication();
        sf = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        editor = sf.edit();
    }

    @Override
    public String doInBackground(String... params) {
        String line;
        String page = "";
        try{
            JSONObject json = new JSONObject();
            json.accumulate("username", params[1]);
            json.accumulate("password", params[2]);

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
            Log.d("TTTTTTTTTT", "OK");
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
                Log.d("tokentokentoken", loginresult.getString("data"));
                editor.putString("Id",params[1]);
                editor.putString("Pw",params[2]);
                editor.apply();
                return loginresult.getString("data");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onPostExecute(String result){
        // if token is null -> login fail
        if(result != null){
            editor.putString("Token",result);
            editor.apply();
            Toast.makeText(applicationContext, "반가워요 "+sf.getString("Id",null)+"님!" , Toast.LENGTH_SHORT).show();
        }
    }
}