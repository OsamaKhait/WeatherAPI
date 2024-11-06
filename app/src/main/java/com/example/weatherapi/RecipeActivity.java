package com.example.weatherapi;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast; // Add this import
import android.widget.TextView;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipeActivity extends AppCompatActivity {
    private ImageView imageViewRecipe;
    private TextView textViewRecipeSuggestion;
    private RecyclerView recyclerView;
    private RecipeAdapter recipeAdapter;
    private List<Recipe> recipeList;
    private Recipe displayedRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        imageViewRecipe = findViewById(R.id.imageViewRecipe);
        textViewRecipeSuggestion = findViewById(R.id.textViewRecipeSuggestion);
        recyclerView = findViewById(R.id.recyclerViewRecipes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Retrieve the selected recipe suggestion and list of other recipes
        String recipeSuggestion = getIntent().getStringExtra("recipeSuggestion");
        recipeList = (List<Recipe>) getIntent().getSerializableExtra("recipeList");

        // Find the initially displayed recipe and set it
        displayedRecipe = recipeList.stream()
                .filter(recipe -> recipe.toString().equals(recipeSuggestion))
                .findFirst()
                .orElse(null);

        if (displayedRecipe != null) {
            showRecipe(displayedRecipe);
        }

        recipeAdapter = new RecipeAdapter(recipeList, this::showRecipe);
        recyclerView.setAdapter(recipeAdapter);

        // Set up the back button
        Button buttonBackToMain = findViewById(R.id.buttonBackToMain);
        buttonBackToMain.setOnClickListener(v -> finish());
    }

    // Method to display a selected recipe
    private void showRecipe(Recipe recipe) {
        textViewRecipeSuggestion.setText(recipe.toString());
        Picasso.get().load(recipe.getImageUrl()).into(imageViewRecipe);
        displayedRecipe = recipe;
    }
}


