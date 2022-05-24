package com.efjerryyang.defendthefrontline.application;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.efjerryyang.defendthefrontline.R;

public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration appBarConfiguration;
    private static final String TAG = "MainActivity";
    public static int screenWidth;
    public static int screenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getScreenSize();
        setContentView(R.layout.activity_main);
        Button simpleButton = findViewById(R.id.simpleButton);
        simpleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                intent.putExtra("game_index", 1);
                startActivity(intent);
            }
        });
        Button mediumButton = findViewById(R.id.mediumButton);
        mediumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                intent.putExtra("game_index", 2);
                startActivity(intent);
            }
        });

        Button difficultButton = findViewById(R.id.difficultButton);
        difficultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                intent.putExtra("game_index", 4);
                startActivity(intent);
            }
        });

        @SuppressLint("UseSwitchCompatOrMaterialCode")
        Switch audioSwitch = findViewById(R.id.audioSwitch);
        audioSwitch.setChecked(false);
        Config.setEnableAudio(false);
        audioSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(TAG, "audioOn: " + isChecked);
                Config.setEnableAudio(isChecked);
            }
        });

    }

    // ppt 第二讲
    public void getScreenSize() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        Log.i(TAG, "screenWidth: " + screenWidth);
        screenHeight = dm.heightPixels;
        Log.i(TAG, "screenHeight: " + screenHeight);
    }
}