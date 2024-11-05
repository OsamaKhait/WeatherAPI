package com.example.weatherapi;

import java.util.List;

public class Recipe {
    private String name;
    private List<String> ingredients;
    private List<String> instructions;

    public Recipe(String name, List<String> ingredients, List<String> instructions) {
        this.name = name;
        this.ingredients = ingredients;
        this.instructions = instructions;
    }

    public String getName() {
        return name;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public List<String> getInstructions() {
        return instructions;
    }

    @Override
    public String toString() {
        StringBuilder recipeText = new StringBuilder(name + "\n\nIngrédients :\n");
        for (String ingredient : ingredients) {
            recipeText.append("- ").append(ingredient).append("\n");
        }
        recipeText.append("\nInstructions :\n");
        for (int i = 0; i < instructions.size(); i++) {
            recipeText.append((i + 1)).append(". ").append(instructions.get(i)).append("\n");
        }
        return recipeText.toString();
    }
}
