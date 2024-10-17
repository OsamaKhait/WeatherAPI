package com.example.weatherapi;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.DateFormat;

public class Weather implements Serializable {
    @SerializedName("temp")
    private double temp;
    @SerializedName("feels_like")
    private double feelsLike;
    @SerializedName("temp_min")
    private double tempMin;
    @SerializedName("temp_max")
    private double tempMax;


    public Weather(int a, int b, int c, int d){
        temp = a;
        feelsLike = b;
        tempMin = c;
        tempMax = d;

    }


    public double getTemp() {
        return Math.round( temp - 273);
    }
    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getFeelsLike() {
        return feelsLike;
    }

    public void setFeelsLike(double feelsLike) {
        this.feelsLike = feelsLike;
    }

    public double getTempMin() {
        return Math.round(tempMin - 273);
    }

    public void setTempMin(double tempMin) {
        this.tempMin = tempMin;
    }

    public double getTempMax() {
        return Math.round(tempMax - 273);
    }

    public void setTempMax(double tempMax) {
        this.tempMax = tempMax;
    }
}

