package com.example.weatherapi;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Forecast implements Serializable {
    int a;
    public Forecast (int faketemp){
        a=faketemp;
    }
    @SerializedName("main")
    Weather weather;
    @SerializedName("dt")
    private int datetime;
    public Forecast(Weather weather, int datetime) {  this.weather = weather;
        this.datetime = datetime;
    }
    @SerializedName("dt_txt")
    private String dateText;

    public String getDateText() {
        return this.dateText;
    }


    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }

    public int getDatetime() {
        return datetime;
    }

    public void setDatetime(int datetime) {
        this.datetime = datetime;
    }
}

