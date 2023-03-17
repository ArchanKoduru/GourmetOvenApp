package com.gourmetoven.gourmetapp.repository;

import com.gourmetoven.gourmetapp.entity.Ingredients;
import com.gourmetoven.gourmetapp.entity.Instruction;
import com.gourmetoven.gourmetapp.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Integer>, JpaSpecificationExecutor<Recipe> {

    @Query("SELECT MAX(rcp.recipeId) FROM Recipe rcp")
    Long findMaxRecipeId();

    List<Recipe> findDistinctByDishType(String type);

    List<Recipe> findDistinctByServings(Integer servincgs);

    List<Recipe> findDistinctByIngredientsIn(List<Ingredients> ingredientsList);

    List<Recipe> findDistinctByInstructionsIn(List<Instruction> instructionList);

    List<Recipe> findDistinctByServingsAndInstructionsIn(Integer servings, List<Instruction> instructionList);

    List<Recipe> findDistinctAllByDishType(String type);

    List<Recipe> findDistinctByDishTypeAndServings(String type, Integer servings);

    List<Recipe> findDistinctByServingsAndIngredientsIn(Integer servings, List<Ingredients> ingredientsList);

    List<Recipe> findDistinctByIngredientsInAndInstructionsIn(List<Ingredients> ingredientsList, List<Instruction> instructionList);

    List<Recipe> findDistinctByServingsAndIngredientsInAndInstructionsIn(Integer servings, List<Ingredients> ingredientsList, List<Instruction> instructionList);

    List<Recipe> findDistinctByDishTypeAndIngredientsIn(String type, List<Ingredients> ingredientsList);

    List<Recipe> findDistinctByDishTypeAndServingsAndIngredientsIn(String type, Integer servings, List<Ingredients> ingredientsList);

    List<Recipe> findDistinctByDishTypeAndInstructionsIn(String type, List<Instruction> instructionList);

    List<Recipe> findDistinctByDishTypeAndServingsAndInstructionsIn(String type, Integer servings, List<Instruction> instructionList);

    List<Recipe> findDistinctByDishTypeAndIngredientsInAndInstructionsIn(String type, List<Ingredients> ingredientsList, List<Instruction> instructionList);
    List<Recipe> findDistinctByDishTypeAndServingsAndIngredientsInAndInstructionsIn(String type, Integer servings, List<Ingredients> ingredientsList, List<Instruction> instructionList);
}
