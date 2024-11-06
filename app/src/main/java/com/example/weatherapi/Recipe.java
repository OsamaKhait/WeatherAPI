package com.example.weatherapi;

import java.io.Serializable;
import java.util.List;

public class Recipe implements Serializable {
    private String name;
    private String imageUrl; // Make sure this is spelled correctly
    private List<String> ingredients;
    private List<String> instructions;

    public Recipe(String name, String imageUrl, List<String> ingredients, List<String> instructions) {
        this.name = name;
        this.imageUrl = imageUrl; // Correct spelling here
        this.ingredients = ingredients;
        this.instructions = instructions;
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
