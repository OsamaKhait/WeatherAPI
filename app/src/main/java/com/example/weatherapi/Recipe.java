package com.example.weatherapi;

import java.io.Serializable;
import java.util.List;

public class Recipe implements Serializable {
    private String name;
    private String imageUrl;
    private List<String> ingredients;
    private List<String> instructions;
    private String videoUrl; // Ajouter le champ videoUrl

    public Recipe(String name, String imageUrl, List<String> ingredients, List<String> instructions, String videoUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.videoUrl = videoUrl; // Initialiser videoUrl
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public List<String> getInstructions() {
        return instructions;
    }

    public String getVideoUrl() {
        return videoUrl; // Récupérer l'URL de la vidéo
    }

    @Override
    public String toString() {
        StringBuilder recipeText = new StringBuilder(name).append("\n\nIngrédients:\n");
        for (String ingredient : ingredients) {
            recipeText.append("- ").append(ingredient).append("\n");
        }
        recipeText.append("\nInstructions:\n");
        for (int i = 0; i < instructions.size(); i++) {
            recipeText.append(i + 1).append(". ").append(instructions.get(i)).append("\n");
        }
        return recipeText.toString();
    }
}
