package com.example.weatherapi;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface MyWeatherService {
    @GET("forecast")
    Call<ForecastData> getForecast(
            @Query("q") String location,
            @Query("appid") String apiKey,
            @Query("unit") String unit
    );

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}

