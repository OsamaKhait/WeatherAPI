package com.example.weatherapi;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ForecastData {
    @SerializedName("main")
    private Main main;

    @SerializedName("dt")
    private long date;

    @SerializedName("weather")
    private List<Weather> weather;

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

    // Getter for weather condition
    public String getWeatherCondition() {
        if (weather != null && !weather.isEmpty()) {
            return weather.get(0).main; // Get the main weather condition
        }
        return "Unknown"; // Default value if weather is not available
    }

    // Setter methods (for testing)
    public void setMain(Main main) {
        this.main = main;
    }

    public void setDate(long date) {
        this.date = date;
    }

    // Inner class for Main
    public static class Main {
        @SerializedName("temp")
        private float temp;

        @SerializedName("temp_min")
        private float tempMin;

        @SerializedName("temp_max")
        private float tempMax;

        public Main(float temp) {
            this.temp = temp;
            this.tempMin = temp - 2; // Sample values for testing
            this.tempMax = temp + 2;
        }
    }

    // Inner class for Weather
    public static class Weather {
        @SerializedName("main")
        private String main;
    }
}
