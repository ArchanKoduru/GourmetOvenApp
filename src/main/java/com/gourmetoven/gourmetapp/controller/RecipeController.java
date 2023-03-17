package com.gourmetoven.gourmetapp.controller;

import com.gourmetoven.gourmetapp.dtos.Request.RecipeCreateRequestDto;
import com.gourmetoven.gourmetapp.dtos.Request.SearchRequestDto;
import com.gourmetoven.gourmetapp.dtos.Response.IngredientResponseDto;
import com.gourmetoven.gourmetapp.dtos.Response.RecipeResponseDto;
import com.gourmetoven.gourmetapp.exception.DataNotAvailableException;
import com.gourmetoven.gourmetapp.service.GourmetOvenService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping(value = "/recipes")
public class RecipeController {

    @Value("${app.version}")
    private String appVersion;

    @Autowired
    private GourmetOvenService gourmetOvenService;
    @Operation(summary = "Get GourmetOven App version")
    @GetMapping("/version")
    public ResponseEntity<String> getVersion() {
        return ResponseEntity.ok().body(appVersion);
    }
    @Operation(summary = "Get All Ingredients")
    @GetMapping("/ingredients")
    public ResponseEntity<List<IngredientResponseDto>> getAllIngredient() {
        return ResponseEntity.ok().body(gourmetOvenService.getAllIngredients());
    }
    @Operation(summary = "Get All Recipes")
    @GetMapping
    public ResponseEntity<List<RecipeResponseDto>> getAllRecipes() {
        List<RecipeResponseDto> recipeResponseDtoList = gourmetOvenService.getAllRecipes();
        return ResponseEntity.ok().body(recipeResponseDtoList);
    }
    @Operation(summary = "Fetch Recipes by type")
    @GetMapping("/type")
    public ResponseEntity<List<RecipeResponseDto>> getSpecificRecipeType(@RequestParam("type") String type) {
        List<RecipeResponseDto> recipeResponseDtoList = gourmetOvenService.findRecipesWithType(type);
        return ResponseEntity.ok().body(recipeResponseDtoList);
    }
    @Operation(summary = "Search Recipes on custom criteria")
    @PostMapping("/search")
    public ResponseEntity<List<RecipeResponseDto>> searchForRecipes(@RequestBody(required = false) SearchRequestDto searchRequestDto) {
        List<RecipeResponseDto> recipeResponseDtoList = gourmetOvenService.getspecificRecipes(searchRequestDto);
        return ResponseEntity.ok().body(recipeResponseDtoList);
    }
    @Operation(summary = "Create Recipe")
    @PostMapping
    public ResponseEntity<RecipeResponseDto> createARecipe(@RequestBody RecipeCreateRequestDto recipeCreateRequestDto,
                                                           Authentication authentication) throws Exception {
        try {
            RecipeResponseDto recipeResponseDto  = gourmetOvenService.
                    createArecipe(recipeCreateRequestDto, authentication.getName());
            return ResponseEntity.ok().body(recipeResponseDto);
        } catch (DataNotAvailableException e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new RecipeResponseDto());
        }

    }
    @Operation(summary = "Update existing Recipes")
    @PatchMapping
    public ResponseEntity<RecipeResponseDto> updateARecipe(@RequestBody RecipeCreateRequestDto recipeCreateRequestDto,
                                                           Authentication authentication) throws Exception {
        RecipeResponseDto recipeResponseDto = gourmetOvenService.
                updateArecipe(recipeCreateRequestDto, authentication.getName());
        return ResponseEntity.ok().body(recipeResponseDto);
    }
    @Operation(summary = "Delete Recipe")
    @DeleteMapping("/{recipeId}")
    public ResponseEntity<String> deleteRecipe(@PathVariable("recipeId") Integer recipeId,
                                                Authentication authentication) throws Exception {
        gourmetOvenService.deleteRecipe(recipeId, authentication.getName());
        return ResponseEntity.ok().body("Recipe deleted successfully");
    }
}
