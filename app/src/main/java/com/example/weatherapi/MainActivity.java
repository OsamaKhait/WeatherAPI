package com.example.weatherapi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.weatherapi.databinding.ActivityMainBinding;
import java.io.Serializable;
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
    private String currentWeatherCondition; // Store current weather condition for recipe selection

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize recipe data and tracking
        initRecipeData();
        recipeIndexMap = new HashMap<>();
        binding.buttonGetRecipe.setEnabled(false); // Disable recipe button initially

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

        // Set up "Show Recipe" button click
        binding.buttonGetRecipe.setOnClickListener(v -> {
            if (currentWeatherCondition != null) {
                String recipeSuggestion = getCyclingRecipe(currentWeatherCondition);
                List<Recipe> recipeList = recipeMap.get(currentWeatherCondition);

                Intent intent = new Intent(MainActivity.this, RecipeActivity.class);
                intent.putExtra("recipeSuggestion", recipeSuggestion); // Pass the recipe suggestion
                intent.putExtra("recipeList", (Serializable) recipeList); // Pass the recipe list if needed
                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, "Please fetch weather information first", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initRecipeData() {
        recipeMap = new HashMap<>();

        // Recipes for "clear" weather
        recipeMap.put("clear", Arrays.asList(
                new Recipe("Salade d'été",
                        "https://img.freepik.com/photos-premium/aliments-sains-equilibres-thon-conserve-tomates-concombres-laitue-oignon-rouge-salade-huile-olive_624178-2244.jpg?w=1380",
                        Arrays.asList("Laitue", "Tomates", "Concombres", "Huile d'olive"),
                        Arrays.asList("Coupez tous les légumes.", "Mélangez avec de l'huile d'olive.", "Servez frais.")),
                new Recipe("Salade de fruits",
                        "https://i-mom.unimedias.fr/2020/09/16/salade-de-fruits.jpg?auto=format%2Ccompress&crop=faces&cs=tinysrgb&fit=crop&h=501&w=890",
                        Arrays.asList("Pommes", "Bananes", "Fraises", "Jus d'orange"),
                        Arrays.asList("Coupez les fruits.", "Mélangez avec du jus d'orange.", "Servez frais.")),
                new Recipe("Smoothie tropical",
                        "https://img.hellofresh.com/c_fit,f_auto,fl_lossy,h_1100,q_50,w_2600/hellofresh_s3/image/tropische-smoothie-met-ananas-en-banaan-0dabe6fc-e2dd4899.jpg",
                        Arrays.asList("Mangue", "Ananas", "Banane", "Lait de coco"),
                        Arrays.asList("Mettez tous les ingrédients dans un mixeur.", "Mixez jusqu'à consistance lisse.", "Servez frais.")),
                new Recipe("Sorbet aux fruits rouges",
                        "https://www.bettybossi.ch/static/rezepte/x/bb_debu200302_0114a_x.jpg",
                        Arrays.asList("Fraises", "Framboises", "Sucre", "Eau"),
                        Arrays.asList("Mélangez les fruits et le sucre.", "Placez au congélateur jusqu'à ce qu'il soit ferme.", "Servez glacé.")),
                new Recipe("Tarte aux fruits",
                        "https://files.meilleurduchef.com/mdc/photo/recette/tarte-fruits/tarte-fruits-1200.jpg",
                        Arrays.asList("Pâte feuilletée", "Fruits de saison", "Sucre", "Crème pâtissière"),
                        Arrays.asList("Étalez la pâte et ajoutez la crème pâtissière.", "Disposez les fruits sur la crème.", "Faites cuire au four pendant 20 minutes."))
        ));

        // Recipes for "rain" weather
        recipeMap.put("rain", Arrays.asList(
                new Recipe("Soupe de légumes chaude",
                        "https://lespetitssecretsdelolo.com/ezoimgfmt/i0.wp.com/lespetitssecretsdelolo.com/wp-content/uploads/2024/01/Soupe-de-legumes.jpeg?w=800&ssl=1&ezimgfmt=ngcb1/notWebP",
                        Arrays.asList("Carottes", "Pommes de terre", "Oignons", "Bouillon de légumes"),
                        Arrays.asList("Coupez tous les légumes.", "Faites bouillir dans le bouillon de légumes.", "Laissez mijoter pendant 20 minutes.")),
                new Recipe("Risotto crémeux",
                        "https://img.over-blog-kiwi.com/0/65/19/23/20190307/ob_4f9134_risotto-poulet-champignons.JPG",
                        Arrays.asList("Riz", "Champignons", "Bouillon de poulet", "Crème"),
                        Arrays.asList("Faites revenir les champignons.", "Ajoutez le riz et le bouillon.", "Mijotez et ajoutez de la crème.")),
                new Recipe("Quiche aux poireaux",
                        "https://www.papillesetpupilles.fr/wp-content/uploads/2020/05/Quiche-aux-poireaux-Copyright-omgponies2-CC-BY-NC-ND-2.0-.jpg",
                        Arrays.asList("Pâte brisée", "Poireaux", "Crème fraîche", "Fromage râpé"),
                        Arrays.asList("Étalez la pâte dans un moule.", "Ajoutez les poireaux et la crème.", "Parsemez de fromage et faites cuire.")),
                new Recipe("Pot-au-feu",
                        "https://lemoulindarius.fr/wp-content/uploads/2021/03/Pot-au-feu-de-boeuf.jpg",
                        Arrays.asList("Viande de bœuf", "Carottes", "Poireaux", "Pommes de terre"),
                        Arrays.asList("Faites cuire la viande dans l'eau.", "Ajoutez les légumes et laissez mijoter.", "Servez chaud.")),
                new Recipe("Curry de légumes",
                        "https://julienutritionsimple.com/wp-content/uploads/2019/02/curry-vegetarien.jpg?w=1458",
                        Arrays.asList("Pommes de terre", "Carottes", "Courgettes", "Épices de curry"),
                        Arrays.asList("Faites revenir les légumes dans une poêle.", "Ajoutez les épices et un peu d'eau.", "Laissez mijoter jusqu'à cuisson des légumes."))
        ));

        // Recipes for "snow" weather
        recipeMap.put("snow", Arrays.asList(
                new Recipe("Chocolat chaud",
                        "https://francais.redpathsugar.com/sites/francais_redpathsugar_com/files/styles/m/public/Spiked_Hot_Chocolate_landscape_hero_web_1_copy.jpg.webp?itok=lkXUlaXn",
                        Arrays.asList("Lait", "Cacao en poudre", "Sucre", "Crème fouettée"),
                        Arrays.asList("Chauffez le lait dans une casserole.", "Ajoutez le cacao en poudre et le sucre.", "Servez avec de la crème fouettée.")),
                new Recipe("Gâteau aux épices",
                        "https://grainedepices.fr/wp-content/uploads/2024/06/1718839363_recette-simplifiee-de-gateau-de-pain-depices-facile-et-rapide.jpg",
                        Arrays.asList("Farine", "Cannelle", "Gingembre", "Miel"),
                        Arrays.asList("Mélangez tous les ingrédients.", "Versez dans un moule et faites cuire au four.", "Laissez refroidir avant de servir.")),
                new Recipe("Fondue au fromage",
                        "https://www.laiterie-gilbert.fr/wp-content/uploads/2023/01/fondue.jpg",
                        Arrays.asList("Fromage à fondue", "Vin blanc", "Pain", "Ail"),
                        Arrays.asList("Chauffez le fromage avec le vin blanc.", "Frottez le pain avec de l'ail.", "Trempez le pain dans le fromage fondu.")),
                new Recipe("Tartiflette",
                        "https://img.cuisineaz.com/660x495/2023/05/26/i194047-tartiflette-oignons-confits-reblochons-et-lardons-fait-maison.webp",
                        Arrays.asList("Pommes de terre", "Lardons", "Oignons", "Reblochon"),
                        Arrays.asList("Faites cuire les pommes de terre et les lardons.", "Ajoutez les oignons et le fromage.", "Faites gratiner au four.")),
                new Recipe("Vin chaud",
                        "https://cache.marieclaire.fr/data/photo/w800_ci/1bk/vin-chaud-cannelle.webp",
                        Arrays.asList("Vin rouge", "Sucre", "Cannelle", "Clous de girofle"),
                        Arrays.asList("Chauffez le vin avec les épices et le sucre.", "Laissez infuser quelques minutes.", "Servez chaud."))
        ));

        // Recipes for "clouds" weather
        recipeMap.put("clouds", Arrays.asList(
                new Recipe("Croque-monsieur",
                        "https://www.toutesrecettes.com/wp-content/uploads/2023/06/Croque-monsieur-au-fromage-et-au-jambon.webp",
                        Arrays.asList("Pain", "Fromage", "Beurre", "Jambon"),
                        Arrays.asList("Beurrez une face de chaque tranche de pain.", "Ajoutez le fromage et le jambon.", "Faites griller jusqu'à dorure.")),
                new Recipe("Gratin dauphinois",
                        "https://dxm.dam.savencia.com/api/wedia/dam/transform/fix635d9eidk6tmu7z7fbsioph89b916z43f76e/gratin-de-pommes-de-terre-ia.transform.webp?t=resize&width=2000",
                        Arrays.asList("Pommes de terre", "Crème fraîche", "Ail", "Fromage râpé"),
                        Arrays.asList("Coupez les pommes de terre en fines tranches.", "Disposez-les dans un plat avec la crème et l'ail.", "Parsemez de fromage et faites cuire au four.")),
                new Recipe("Omelette aux champignons",
                        "https://scontent-cdg4-2.xx.fbcdn.net/v/t39.30808-6/228435148_4101473006596344_6685039052173075252_n.jpg?_nc_cat=103&ccb=1-7&_nc_sid=833d8c&_nc_ohc=pjLunj3muSwQ7kNvgG8iuN1&_nc_zt=23&_nc_ht=scontent-cdg4-2.xx&_nc_gid=AiIGvERz62nerj2R847UEdc&oh=00_AYArjvZnJMiwcadyt0S10d3mGNhulEGl7tCkGXJEN5xnfA&oe=6731003F",
                        Arrays.asList("Œufs", "Champignons", "Sel", "Poivre"),
                        Arrays.asList("Battez les œufs et assaisonnez-les.", "Ajoutez les champignons dans une poêle.", "Versez les œufs battus et faites cuire.")),
                new Recipe("Sandwich club",
                        "https://i-re.unimedias.fr/2024/04/29/club-sandwich-pouletistock-662f57c0b00da.jpg?auto=format%2Ccompress&crop=faces&cs=tinysrgb&fit=crop&h=501&w=890",
                        Arrays.asList("Pain de mie", "Mayonnaise", "Laitue", "Tomates", "Poulet"),
                        Arrays.asList("Tartinez le pain de mayonnaise.", "Ajoutez la laitue, les tomates et le poulet.", "Assemblez les tranches et coupez en triangles.")),
                new Recipe("Pizza maison",
                        "https://img.cuisineaz.com/660x495/2018/03/19/i137572-pizza-rapide-et-facile-au-jambon-tomates-fromage-olives.webp",
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
                    currentWeatherCondition = data.getWeatherCondition().toLowerCase();
                    binding.buttonGetRecipe.setEnabled(true); // Enable the recipe button
                } else {
                    Toast.makeText(MainActivity.this, "City not found. Please try again.", Toast.LENGTH_SHORT).show();
                    binding.buttonGetRecipe.setEnabled(false); // Disable the recipe button
                }
            }

            @Override
            public void onFailure(Call<ForecastData> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failed to retrieve data. Please check your internet connection.", Toast.LENGTH_SHORT).show();
                binding.buttonGetRecipe.setEnabled(false); // Disable the recipe button
            }
        });
    }

    private void updateUI(ForecastData data) {
        binding.textViewRTemp.setText(String.format("%s°C", data.getTemperature()));
        binding.textViewMin.setText(String.format("%s°C", data.getMinTemperature()));
        binding.textViewRMax.setText(String.format("%s°C", data.getMaxTemperature()));
        binding.textViewDateTime.setText(data.getDate());
    }

    private String getCyclingRecipe(String weatherCondition) {
        List<Recipe> recipes = recipeMap.get(weatherCondition);
        if (recipes == null || recipes.isEmpty()) {
            return "Recette non disponible pour cette condition météorologique.";
        }

        int currentIndex = recipeIndexMap.getOrDefault(weatherCondition, 0);
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