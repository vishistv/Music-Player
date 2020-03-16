package com.vishistv.musicplayer;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    Animation animFadeIn0, animFadeIn1;

    TextView tvDesc;
    EditText etSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        int SPLASH_DISPLAY_LENGTH = 2000;

        tvDesc = findViewById(R.id.tv_desc);
        etSearch = findViewById(R.id.sv_search);

        animFadeIn0 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        animFadeIn1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);

        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tvDesc.setVisibility(View.VISIBLE);
                tvDesc.startAnimation(animFadeIn0);
                etSearch.setVisibility(View.VISIBLE);
                etSearch.startAnimation(animFadeIn1);
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    public void onClickSearchSongs(View view) {
        startActivity(new Intent(SplashActivity.this, SearchActivity.class));
        finish();
    }
}
