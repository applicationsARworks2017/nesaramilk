package com.nesara.amaresh.demosell;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import demosell.amaresh.android.com.nesara.Activity.AdminActivity;
import demosell.amaresh.android.com.nesara.Activity.EnterPhone;
import demosell.amaresh.android.com.nesara.Activity.Home;
import demosell.amaresh.android.com.nesara.Util.Constants;

public class SplashScreen extends AppCompatActivity {
    private static final int SPLASH_INTERVAL_TIME = 3000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        final int user_id = getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getInt(Constants.N_USER_ID, 0);
        final String user_type = getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.N_USER_TYPE, null);
        //final String phone_number = "";


        if (user_id==0) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashScreen.this, EnterPhone.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    finish();
                }
            }, SPLASH_INTERVAL_TIME);
        }

        else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                   if(user_type.contains("User")){
                       Intent intent = new Intent(SplashScreen.this, Home.class);
                       intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                       intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                       intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                       startActivity(intent);
                   }
                   else{
                       Intent intent = new Intent(SplashScreen.this, AdminActivity.class);
                       intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                       intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                       intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                       startActivity(intent);
                   }
                    finish();
                }
            }, SPLASH_INTERVAL_TIME);
        }

    }
}
