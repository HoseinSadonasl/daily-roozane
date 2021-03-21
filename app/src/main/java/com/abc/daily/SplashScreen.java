package com.abc.daily;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.abc.daily.app.Application;
import com.abc.daily.app.spref;

import java.lang.annotation.Annotation;

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
        setContentView(R.layout.activity_splash_screen);
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
        }, 500);

    }
    private void restoreTheme() {
        color = spref.get(spref.tags.THEME).getString(spref.Theme.THEME_COLOR, spref.Theme.DEFAULT_THEME_COLOR);
        switch (color) {
            case "purple" : {
                parent.setBackgroundColor(getResources().getColor(R.color.deep_purple_400));
                break;
            }
            case "red" : {
                parent.setBackgroundColor(getResources().getColor(R.color.red_500));
                break;
            }
            case "orange" : {
                parent.setBackgroundColor(getResources().getColor(R.color.orange_400));
                break;
            }
            case "blue" : {
                parent.setBackgroundColor(getResources().getColor(R.color.blue_500));
                break;
            }
            case "green" : {
                parent.setBackgroundColor(getResources().getColor(R.color.green_500));
                break;
            }
            case "teal" : {
                parent.setBackgroundColor(getResources().getColor(R.color.teal_500));
                break;
            }
            default:parent.setBackgroundColor(getResources().getColor(R.color.teal_500));
        }
    }
}