package com.abc.daily.interfaces;

import java.util.Map;

import com.abc.daily.Objects.WeatherModels;
import com.abc.daily.R;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface DailyApi {
    @GET("weather?appid=6c7b0789e344c8bdd8f0935ff4568e72")
    Call<WeatherModels> getCurrentWeather(@QueryMap Map<String,String> data);
}
