package com.example.weatherapi;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
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

        // Initialisation des vues
        imageViewRecipe = findViewById(R.id.imageViewRecipe);
        textViewRecipeSuggestion = findViewById(R.id.textViewRecipeSuggestion);
        recyclerView = findViewById(R.id.recyclerViewRecipes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Récupérer la suggestion de recette et la liste d'autres recettes
        String recipeSuggestion = getIntent().getStringExtra("recipeSuggestion");
        recipeList = (List<Recipe>) getIntent().getSerializableExtra("recipeList");

        // Trouver la recette affichée en fonction de la suggestion
        displayedRecipe = recipeList.stream()
                .filter(recipe -> recipe.toString().equals(recipeSuggestion))
                .findFirst()
                .orElse(null);

        if (displayedRecipe != null) {
            showRecipe(displayedRecipe);
        }

        // Configurer l'adaptateur pour la liste des recettes
        recipeAdapter = new RecipeAdapter(recipeList, this::showRecipe);
        recyclerView.setAdapter(recipeAdapter);

        // Configurer le bouton retour
        Button buttonBackToMain = findViewById(R.id.buttonBackToMain);
        buttonBackToMain.setOnClickListener(v -> finish());

        // Ajouter l'écouteur sur l'image
        imageViewRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String videoUrl = displayedRecipe.getVideoUrl(); // Utiliser displayedRecipe ici
                if (videoUrl != null && !videoUrl.isEmpty()) {
                    // Créer un Intent pour ouvrir l'URL de la vidéo dans YouTube
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));
                    intent.putExtra(Intent.EXTRA_REFERRER, Uri.parse("android-app://com.google.android.youtube"));
                    startActivity(intent);
                } else {
                    Toast.makeText(RecipeActivity.this, "Vidéo non disponible", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Méthode pour afficher une recette sélectionnée
    private void showRecipe(Recipe recipe) {
        textViewRecipeSuggestion.setText(recipe.toString());
        Picasso.get().load(recipe.getImageUrl()).into(imageViewRecipe);
        displayedRecipe = recipe;
    }
}
