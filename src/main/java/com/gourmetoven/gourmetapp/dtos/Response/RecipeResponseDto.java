package com.gourmetoven.gourmetapp.dtos.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecipeResponseDto {
    private Integer recipeId;
    private String name;
    private Integer servings;
    private String dishType;
    private String creationType;
    private String user;
    List<IngredientResponseDto> ingredients;
    List<String> instructions;
}


