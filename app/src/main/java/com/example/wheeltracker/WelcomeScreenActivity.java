package com.example.wheeltracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class WelcomeScreenActivity extends AppCompatActivity {

    //only for welcome screen
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
              Class LoginData;
                try {
                    SharedPreferences prefs = getApplicationContext().getSharedPreferences("LoginData", MODE_PRIVATE);
                    if (prefs.contains("LastActivity")) {
//                        Log.e("TAG2", "IN TRY");
                        LoginData = Class.forName(prefs.getString("LastActivity", MainActivity.class.getName()));
                        //getString(key,default_value) - default-value is MainActivity.class
                    }
                  else
                      LoginData = MainActivity.class;

                } catch(ClassNotFoundException ex) {
//                    Log.e("tag4","IN CATCH");
                    LoginData = MainActivity.class;
                }
                new CallIntent(WelcomeScreenActivity.this,LoginData).callIntent();
                finish();
            }
        },InitConfig.TIME);
    }
}
