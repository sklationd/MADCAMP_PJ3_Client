package com.example.project3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.project3.login.LoginActivity;
import com.example.project3.login.MyLogin;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends Activity {
    public static Context contextOfApplication;

    private String[] permissions = {
    };
    private static final int MULTIPLE_PERMISSIONS = 101;

    public static Context getContextOfApplication() {
        return contextOfApplication;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contextOfApplication = getApplicationContext();
        checkPermissions();
    }

    private boolean checkPermissions() {
        int result;
        List<String> permissionList = new ArrayList<>();
        for (String pm : permissions) {
            result = ContextCompat.checkSelfPermission(this, pm);
            if (result != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(pm);
            }
        }
        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionList.toArray(new String[permissionList.size()]), MULTIPLE_PERMISSIONS);
            return false;
        } else {
            initial_set();
        }
        return true;
    }

    private void initial_set() {
        SharedPreferences sf = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sf.edit();
        String youtube_url = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            youtube_url = extras.getString(Intent.EXTRA_TEXT);
            if (youtube_url != null) {
                editor.putString("youtube_url", youtube_url);
                editor.apply();
            }
        }
        if (sf.getString("Id", null) != null) {
            if (youtube_url != null) {
                Log.d("asdfasdf", youtube_url);
                startActivity(new Intent(this, PostingVideo.class));
                finish();
            } else {
                String url = getApplicationContext().getString(R.string.login_uri);
                String Id = sf.getString("Id", null);
                String Pw = sf.getString("Pw", null);
                try {
                    new MyLogin().execute(url, Id, Pw).get();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
        } else {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    private void showNoPermissionToastAndFinish() {
        Toast.makeText(this, "모든 권한 요청에 동의 해주셔야 이용 가능합니다. 설정에서 권한 허용 하시기 바랍니다.", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++) {
                        if (permissions[i].equals(this.permissions[i])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                showNoPermissionToastAndFinish();
                                return;
                            }
                        }
                    }
                } else {
                    showNoPermissionToastAndFinish();
                    return;
                }
            }
            initial_set();
        }
    }
}
