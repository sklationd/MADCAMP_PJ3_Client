package com.example.project3.Login;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.project3.Main.MainActivity;
import com.example.project3.PostingVideo;
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

public class LoginActivity extends AppCompatActivity {
    private LoginViewModel loginViewModel;
    SharedPreferences sf;
    SharedPreferences.Editor editor;
    boolean auto = false;
    final Context context = this;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sf = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sf.edit();
        String autologin = sf.getString("Id", null);
        if (autologin != null) {
            setContentView(R.layout.activity_autologin);
            String id = sf.getString("Id", null);
            String pw = sf.getString("Pw", null);
            login(id, pw);
            Toast.makeText(getApplicationContext(), "반가워요 " + id + "님!", Toast.LENGTH_SHORT).show();
        } else {
            setContentView(R.layout.activity_login);
            loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
                    .get(LoginViewModel.class);

            final EditText usernameEditText = findViewById(R.id.username);
            final EditText passwordEditText = findViewById(R.id.password);
            final Button loginButton = findViewById(R.id.login);
            final Button registerButton = findViewById(R.id.register);

            loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
                @Override
                public void onChanged(@Nullable LoginFormState loginFormState) {
                    if (loginFormState == null) {
                        return;
                    }
                    loginButton.setEnabled(loginFormState.isDataValid());
                    if (loginFormState.getUsernameError() != null) {
                        usernameEditText.setError(getString(loginFormState.getUsernameError()));
                    }
                    if (loginFormState.getPasswordError() != null) {
                        passwordEditText.setError(getString(loginFormState.getPasswordError()));
                    }
                }
            });
            TextWatcher afterTextChangedListener = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    // ignore
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // ignore
                }

                @Override
                public void afterTextChanged(Editable s) {
                    loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
            };
            usernameEditText.addTextChangedListener(afterTextChangedListener);
            passwordEditText.addTextChangedListener(afterTextChangedListener);
            passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        InputMethodManager keyboard = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        keyboard.hideSoftInputFromWindow(passwordEditText.getWindowToken(), 0);
                        login(usernameEditText.getText().toString(), passwordEditText.getText().toString());
                    }
                    return false;
                }
            });
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    login(usernameEditText.getText().toString(), passwordEditText.getText().toString());
                }
            });

            registerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                    finish();
                }
            });
        }
    }

    public void login(String id, String password) {
        String url = getApplicationContext().getString(R.string.login_uri);
        try {
            new MyLogin().execute(url, id, password);
            //new MyLogin().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url, id, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showLoginFailed() {
        Toast.makeText(getApplicationContext(), "입력하신 정보가 올바르지 않습니다", Toast.LENGTH_SHORT).show();
    }

    private class MyLogin extends AsyncTask<String, Void, String> {
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        @Override
        public void onPreExecute() {
            loadingProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public String doInBackground(String... params) {
            String line;
            String page = "";
            try {
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
                if (response == 200) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                    while ((line = reader.readLine()) != null) {
                        page += line;
                    }
                }
                if (conn != null) {
                    conn.disconnect();
                }
                JSONObject loginresult = new JSONObject(page);
                if (loginresult.getBoolean("success")) {
                    Log.d("tokentokentoken", loginresult.getString("data"));
                    editor.putString("Id", params[1]);
                    editor.putString("Pw", params[2]);
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
        public void onPostExecute(String result) {
            // if token is null -> login fail
            if (result != null) {
                editor.putString("Token", result);
                editor.apply();
                if (sf.getString("youtube_url", null) != null) {
                    startActivity(new Intent(LoginActivity.this, PostingVideo.class));
                    loadingProgressBar.setVisibility(View.INVISIBLE);
                    finish();
                } else {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    Toast.makeText(getApplicationContext(), "반가워요 " + sf.getString("Id", null) + "님!", Toast.LENGTH_SHORT).show();
                    loadingProgressBar.setVisibility(View.INVISIBLE);
                    finish();
                }
            } else {
                showLoginFailed();
            }
            loadingProgressBar.setVisibility(View.INVISIBLE);
        }
    }
}

