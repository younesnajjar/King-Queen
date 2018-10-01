package com.example.younes.hotornot.ui.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.younes.hotornot.R;
import com.example.younes.hotornot.utils.SaveSharedPreference;

public class StartingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);
        if(SaveSharedPreference.getLoggedStatus(getApplicationContext())){
            Intent intent = new Intent(this, SwiperScreenActivity.class);
            startActivity(intent);
        }
        else{

        }
    }
}
