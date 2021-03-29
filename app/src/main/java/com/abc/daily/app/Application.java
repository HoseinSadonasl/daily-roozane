package com.abc.daily.app;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Calendar;

import com.abc.daily.interfaces.DailyApi;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Application extends android.app.Application {


    private static Context context;
    private static Retrofit retrofit;
    private static DailyApi api;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;


        String iranSans = "isans_light.ttf";
        FontOverride.setDefaultFont(getContext(), "MONOSPACE", iranSans);

        Gson gson = new GsonBuilder().setLenient().create();

        OkHttpClient client = new OkHttpClient();

        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(DailyApi.class);

    }


    public static Context getContext(){
        return context;

    }


    public static DailyApi getApi(){
        return api;
    }

    public void getDate() {
        Calendar cal = Calendar.getInstance();
        cal.get(Calendar.DAY_OF_MONTH);
    }

    public static Boolean isNetworkEnabled() {
        ConnectivityManager manager =
                (ConnectivityManager) Application.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return manager.getActiveNetworkInfo() != null && manager.getActiveNetworkInfo().isConnected();
    }







}
