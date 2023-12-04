package com.abc.daily;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.abc.daily.app.spref;

public class SplashScreen extends AppCompatActivity {

    ConstraintLayout parent;
    AppCompatImageView logo;
    String color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_splash);
        parent = findViewById(R.id.parent);
        logo = findViewById(R.id.logo);

        restoreTheme();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation animation = AnimationUtils.loadAnimation(SplashScreen.this, android.R.anim.fade_in);
                animation.setDuration(1000);
                logo.setVisibility(View.VISIBLE);
                logo.startAnimation(animation);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(SplashScreen.this, MainActivity.class));
                    }
                }, 2000);

            }
        }, 250);

    }
    private void restoreTheme() {
        color = spref.get(spref.THEME).getString(spref.Theme.THEME_COLOR, spref.Theme.DEFAULT_THEME_COLOR);
        switch (color) {
            case spref.Theme.PURPLE_COLOR : {
                parent.setBackgroundColor(getResources().getColor(R.color.deep_purple_400));
                break;
            }
            case spref.Theme.RED_COLOR : {
                parent.setBackgroundColor(getResources().getColor(R.color.red_500));
                break;
            }
            case spref.Theme.ORANGE_COLOR : {
                parent.setBackgroundColor(getResources().getColor(R.color.orange_400));
                break;
            }
            case spref.Theme.BLUE_COLOR : {
                parent.setBackgroundColor(getResources().getColor(R.color.blue_500));
                break;
            }
            case spref.Theme.GREEN_COLOR : {
                parent.setBackgroundColor(getResources().getColor(R.color.green_500));
                break;
            }
            case spref.Theme.TEAL_COLOR: {
                parent.setBackgroundColor(getResources().getColor(R.color.teal_500));
                break;
            }
            default:parent.setBackgroundColor(getResources().getColor(R.color.teal_500));
        }
    }
}