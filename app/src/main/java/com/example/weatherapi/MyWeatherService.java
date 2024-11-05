package com.example.weatherapi;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MyWeatherService {

    @GET("weather")
    Call<Weather> getWeatherForCity(
            @Query("q") String city,
            @Query("appid") String apiKey
    );
}


