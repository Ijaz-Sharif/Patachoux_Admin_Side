package com.patach.patachoux.Screen;

import static com.patach.patachoux.Utils.Constant.getUserLoginStatus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.patach.patachoux.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Thread thread=new Thread(){
            @Override
            public void run() {
                try {
                    sleep(4000);
                    if(getUserLoginStatus(SplashActivity.this)){
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    }else{
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        finish();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }
}