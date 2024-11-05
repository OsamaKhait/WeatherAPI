package com.example.weatherapi;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.weatherapi.databinding.ActivityMainBinding;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private OpenWeatherServices weatherService;
    private Map<String, List<Recipe>> recipeMap;
    private Map<String, Integer> recipeIndexMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize recipe data and tracking
        initRecipeData();
        recipeIndexMap = new HashMap<>();

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

    private void initRecipeData() {
        recipeMap = new HashMap<>();

        // Recipes for "clear" weather
        recipeMap.put("clear", Arrays.asList(
                new Recipe("Salade d'été",
                        Arrays.asList("Laitue", "Tomates", "Concombres", "Huile d'olive"),
                        Arrays.asList("Coupez tous les légumes.", "Mélangez avec de l'huile d'olive.", "Servez frais.")),
                new Recipe("Salade de fruits",
                        Arrays.asList("Pommes", "Bananes", "Fraises", "Jus d'orange"),
                        Arrays.asList("Coupez les fruits.", "Mélangez avec du jus d'orange.", "Servez frais.")),
                new Recipe("Smoothie tropical",
                        Arrays.asList("Mangue", "Ananas", "Banane", "Lait de coco"),
                        Arrays.asList("Mettez tous les ingrédients dans un mixeur.", "Mixez jusqu'à consistance lisse.", "Servez frais.")),
                new Recipe("Sorbet aux fruits rouges",
                        Arrays.asList("Fraises", "Framboises", "Sucre", "Eau"),
                        Arrays.asList("Mélangez les fruits et le sucre.", "Placez au congélateur jusqu'à ce qu'il soit ferme.", "Servez glacé.")),
                new Recipe("Tarte aux fruits",
                        Arrays.asList("Pâte feuilletée", "Fruits de saison", "Sucre", "Crème pâtissière"),
                        Arrays.asList("Étalez la pâte et ajoutez la crème pâtissière.", "Disposez les fruits sur la crème.", "Faites cuire au four pendant 20 minutes."))
        ));

        // Recipes for "rain" weather
        recipeMap.put("rain", Arrays.asList(
                new Recipe("Soupe de légumes chaude",
                        Arrays.asList("Carottes", "Pommes de terre", "Oignons", "Bouillon de légumes"),
                        Arrays.asList("Coupez tous les légumes.", "Faites bouillir dans le bouillon de légumes.", "Laissez mijoter pendant 20 minutes.")),
                new Recipe("Risotto crémeux",
                        Arrays.asList("Riz", "Champignons", "Bouillon de poulet", "Crème"),
                        Arrays.asList("Faites revenir les champignons.", "Ajoutez le riz et le bouillon.", "Mijotez et ajoutez de la crème.")),
                new Recipe("Quiche aux poireaux",
                        Arrays.asList("Pâte brisée", "Poireaux", "Crème fraîche", "Fromage râpé"),
                        Arrays.asList("Étalez la pâte dans un moule.", "Ajoutez les poireaux et la crème.", "Parsemez de fromage et faites cuire.")),
                new Recipe("Pot-au-feu",
                        Arrays.asList("Viande de bœuf", "Carottes", "Poireaux", "Pommes de terre"),
                        Arrays.asList("Faites cuire la viande dans l'eau.", "Ajoutez les légumes et laissez mijoter.", "Servez chaud.")),
                new Recipe("Curry de légumes",
                        Arrays.asList("Pommes de terre", "Carottes", "Courgettes", "Épices de curry"),
                        Arrays.asList("Faites revenir les légumes dans une poêle.", "Ajoutez les épices et un peu d'eau.", "Laissez mijoter jusqu'à cuisson des légumes."))
        ));

        // Recipes for "snow" weather
        recipeMap.put("snow", Arrays.asList(
                new Recipe("Chocolat chaud",
                        Arrays.asList("Lait", "Cacao en poudre", "Sucre", "Crème fouettée"),
                        Arrays.asList("Chauffez le lait dans une casserole.", "Ajoutez le cacao en poudre et le sucre.", "Servez avec de la crème fouettée.")),
                new Recipe("Gâteau aux épices",
                        Arrays.asList("Farine", "Cannelle", "Gingembre", "Miel"),
                        Arrays.asList("Mélangez tous les ingrédients.", "Versez dans un moule et faites cuire au four.", "Laissez refroidir avant de servir.")),
                new Recipe("Fondue au fromage",
                        Arrays.asList("Fromage à fondue", "Vin blanc", "Pain", "Ail"),
                        Arrays.asList("Chauffez le fromage avec le vin blanc.", "Frottez le pain avec de l'ail.", "Trempez le pain dans le fromage fondu.")),
                new Recipe("Tartiflette",
                        Arrays.asList("Pommes de terre", "Lardons", "Oignons", "Reblochon"),
                        Arrays.asList("Faites cuire les pommes de terre et les lardons.", "Ajoutez les oignons et le fromage.", "Faites gratiner au four.")),
                new Recipe("Vin chaud",
                        Arrays.asList("Vin rouge", "Sucre", "Cannelle", "Clous de girofle"),
                        Arrays.asList("Chauffez le vin avec les épices et le sucre.", "Laissez infuser quelques minutes.", "Servez chaud."))
        ));

        // Recipes for "clouds" weather
        recipeMap.put("clouds", Arrays.asList(
                new Recipe("Croque-monsieur",
                        Arrays.asList("Pain", "Fromage", "Beurre", "Jambon"),
                        Arrays.asList("Beurrez une face de chaque tranche de pain.", "Ajoutez le fromage et le jambon.", "Faites griller jusqu'à dorure.")),
                new Recipe("Gratin dauphinois",
                        Arrays.asList("Pommes de terre", "Crème fraîche", "Ail", "Fromage râpé"),
                        Arrays.asList("Coupez les pommes de terre en fines tranches.", "Disposez-les dans un plat avec la crème et l'ail.", "Parsemez de fromage et faites cuire au four.")),
                new Recipe("Omelette aux champignons",
                        Arrays.asList("Œufs", "Champignons", "Sel", "Poivre"),
                        Arrays.asList("Battez les œufs et assaisonnez-les.", "Ajoutez les champignons dans une poêle.", "Versez les œufs battus et faites cuire.")),
                new Recipe("Sandwich club",
                        Arrays.asList("Pain de mie", "Mayonnaise", "Laitue", "Tomates", "Poulet"),
                        Arrays.asList("Tartinez le pain de mayonnaise.", "Ajoutez la laitue, les tomates et le poulet.", "Assemblez les tranches et coupez en triangles.")),
                new Recipe("Pizza maison",
                        Arrays.asList("Pâte à pizza", "Sauce tomate", "Fromage", "Olives"),
                        Arrays.asList("Étalez la sauce tomate sur la pâte.", "Ajoutez le fromage et les olives.", "Faites cuire au four jusqu'à ce que la pâte soit dorée."))
        ));
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

        // Display cycling recipe suggestion based on weather condition
        String weatherCondition = data.getWeatherCondition().toLowerCase();
        String recipeSuggestion = getCyclingRecipe(weatherCondition);
        binding.textViewRecipe.setText(recipeSuggestion);
    }

    // Method to return a cycling recipe for a given weather condition
    private String getCyclingRecipe(String weatherCondition) {
        List<Recipe> recipes = recipeMap.get(weatherCondition);
        if (recipes == null || recipes.isEmpty()) {
            return "Recette non disponible pour cette condition météorologique.";
        }

        // Get current index, default to 0 if not set
        int currentIndex = recipeIndexMap.getOrDefault(weatherCondition, 0);

        // Get recipe, then increment and cycle index
        Recipe recipe = recipes.get(currentIndex);
        recipeIndexMap.put(weatherCondition, (currentIndex + 1) % recipes.size()); // Cycle through recipes

        return recipe.toString(); // This calls the overridden toString method in Recipe to format the output
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
