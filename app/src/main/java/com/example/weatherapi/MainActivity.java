package com.example.weatherapi;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.weatherapi.databinding.ActivityMainBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private OpenWeatherServices weatherService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Setup Retrofit client instance
        weatherService = RetrofitClientInstance.getRetrofitInstance().create(OpenWeatherServices.class);

        binding.buttonGetWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = binding.editTextVille.getText().toString().trim();
                if (!city.isEmpty()) {
                    fetchWeatherData(city);
                } else {
                    Toast.makeText(MainActivity.this, "Please enter a city name", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void fetchWeatherData(String city) {
        Call<ForecastData> call = weatherService.getWeather(city, "d29ca15a87ad1f9508b15001a6403a05", "metric");
        call.enqueue(new Callback<ForecastData>() {
            @Override
            public void onResponse(Call<ForecastData> call, Response<ForecastData> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ForecastData data = response.body();
                    updateUI(data);
                } else {
                    Toast.makeText(MainActivity.this, "City not found. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ForecastData> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failed to retrieve data. Please check your internet connection.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI(ForecastData data) {
        binding.textViewRTemp.setText(String.format("%s°C", data.getTemperature()));
        binding.textViewMin.setText(String.format("%s°C", data.getMinTemperature()));
        binding.textViewRMax.setText(String.format("%s°C", data.getMaxTemperature()));
        binding.textViewDateTime.setText(data.getDate());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}