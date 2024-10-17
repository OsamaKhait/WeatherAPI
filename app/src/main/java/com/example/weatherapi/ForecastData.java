package com.example.weatherapi;

import com.google.gson.annotations.SerializedName;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ForecastData {
    @SerializedName("main")
    private Main main;

    @SerializedName("dt")
    private long date;

    public String getTemperature() {
        return String.valueOf(main.temp);
    }

    public String getMinTemperature() {
        return String.valueOf(main.tempMin);
    }

    public String getMaxTemperature() {
        return String.valueOf(main.tempMax);
    }

    public String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy", Locale.FRENCH);
        String formattedDate = sdf.format(new Date(date * 1000));
        return formattedDate.substring(0, 1).toUpperCase() + formattedDate.substring(1);
    }

    private static class Main {
        @SerializedName("temp")
        private float temp;

        @SerializedName("temp_min")
        private float tempMin;

        @SerializedName("temp_max")
        private float tempMax;
    }
}