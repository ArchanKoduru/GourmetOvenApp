package com.gourmetoven.gourmetapp.unit.service;

import com.gourmetoven.gourmetapp.dtos.Request.RecipeCreateRequestDto;
import com.gourmetoven.gourmetapp.dtos.Request.SearchRequestDto;
import com.gourmetoven.gourmetapp.dtos.Response.IngredientResponseDto;
import com.gourmetoven.gourmetapp.dtos.Response.RecipeResponseDto;
import com.gourmetoven.gourmetapp.entity.Ingredients;
import com.gourmetoven.gourmetapp.entity.Instruction;
import com.gourmetoven.gourmetapp.entity.Recipe;
import com.gourmetoven.gourmetapp.entity.Users;
import com.gourmetoven.gourmetapp.repository.IngredientRepository;
import com.gourmetoven.gourmetapp.repository.InstructionRepository;
import com.gourmetoven.gourmetapp.repository.RecipeRepository;
import com.gourmetoven.gourmetapp.repository.UserRepository;
import com.gourmetoven.gourmetapp.service.GourmetOvenService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class GourmetOvenServiceTest {

    @InjectMocks
    GourmetOvenService gourmetOvenService;

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private InstructionRepository instructionsRepository;

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void givenGourmetOvenServiceWhenGetAllIngredientsIsCalledThenFetchAllIngredients() {
        //Given
        Ingredients ingredients = new Ingredients(101, "Cheese");
        List<Ingredients> ingredientsList = new ArrayList<>(Arrays.asList(ingredients));
        IngredientResponseDto ingredientResponseDto = new IngredientResponseDto(101, "Cheese");
        Mockito.when(ingredientRepository.findAll()).thenReturn(ingredientsList);

        //When
        List<IngredientResponseDto> ingredientResponseDtos = gourmetOvenService.getAllIngredients();

        //Then
        Assertions.assertNotNull(ingredientResponseDtos);
        Assertions.assertEquals(ingredientResponseDto.getIngredientName(), ingredientResponseDtos.get(0).getIngredientName());
    }

    @Test
    public void GivenGourmetOvenServiceWhenCreateArecipeIsCalledThenRecipeCreatesSuccessfully() throws Exception {
        Users user = new Users();
        user.setId(100);
        Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.of(user));
        RecipeCreateRequestDto recipeCreateRequestDto = RecipeCreateRequestDto.builder().name("Idli").dishType("vegetarian")
                .ingredients(Arrays.asList(101, 102)).instructions(Arrays.asList("More spicy")).
                servings(2).build();
        RecipeResponseDto recipeResponseDtoMock = new RecipeResponseDto();
        recipeResponseDtoMock.setRecipeId(101);
        recipeResponseDtoMock.setName("Sambar");

        Recipe recipe = new Recipe();
        recipe.setRecipeId(101);
        recipe.setName("Sambar");
        recipe.setUser(user);

        Mockito.when(recipeRepository.save(Mockito.any())).thenReturn(recipe);
        RecipeResponseDto recipeResponseDtoOut = gourmetOvenService.createArecipe(recipeCreateRequestDto, "101");

        Assertions.assertEquals(recipeResponseDtoMock.getRecipeId(), recipeResponseDtoOut.getRecipeId());
        Assertions.assertEquals(recipeResponseDtoMock.getName(), recipeResponseDtoOut.getName());
    }

    @Test
    public void GivenGourmetOvenServiceWhenGetAllRecipeIsCalledThenRecipeListFetched() {

        List<Recipe> recipes = new ArrayList<>();
        Recipe recipe = createSampleReciepe();
        recipes.add(recipe);
        Mockito.when(recipeRepository.findAll()).thenReturn(recipes);
        List<RecipeResponseDto> recipeResponseDtos = gourmetOvenService.getAllRecipes();

        Assertions.assertEquals(recipe.getRecipeId(), recipeResponseDtos.get(0).getRecipeId());
    }

    @Test
    public void GivenGourmetOvenServiceWhenSpecificRecipeIsCalledThenRecipesFetched() {
        List<Recipe> recipes = new ArrayList<>();
        Recipe recipe = createSampleReciepe();
        recipes.add(recipe);
        Mockito.when(recipeRepository.findAll()).thenReturn(recipes);
        List<RecipeResponseDto> recipeResponseDtos = gourmetOvenService.findRecipesWithType("all");

        Assertions.assertEquals(recipe.getRecipeId(), recipeResponseDtos.get(0).getRecipeId());
    }

    @Test
    public void GivenGourmetOvenServiceWhenVegRecipeIsCalledThenVegRecipesFetched() {
        List<Recipe> recipes = new ArrayList<>();
        Recipe recipe = new Recipe();
        recipe.setRecipeId(108);
        recipe.setName("sambar");
        recipe.setDishType("vegetarian");
        recipe.setServings(2);
        recipe.setUser(createSampleUser());
        recipes.add(recipe);
        Mockito.when(recipeRepository.findDistinctByDishType(Mockito.anyString())).thenReturn(recipes);
        List<RecipeResponseDto> recipeResponseDtos = gourmetOvenService.findRecipesWithType("vegetarian");

        Assertions.assertEquals(recipe.getName(), recipeResponseDtos.get(0).getName());
    }

    @Test
    public void GivenGourmetOvenServiceWhenNonvegRecipeIsCalledThenVegRecipesFetched() {
        List<Recipe> recipes = new ArrayList<>();
        ;
        Recipe recipe = createSampleReciepe();
        recipes.add(recipe);
        Mockito.when(recipeRepository.findDistinctByDishType(Mockito.anyString())).thenReturn(recipes);
        List<RecipeResponseDto> recipeResponseDtos = gourmetOvenService.findRecipesWithType("non-vegetarian");

        Assertions.assertEquals(recipe.getName(), recipeResponseDtos.get(0).getName());
    }

    @Test
    public void GivenGourmetOvenServiceWhenSpecificSearchWithDefaultValuesIsCalledThenAllRecipesFetched() {
        List<Recipe> recipes = new ArrayList<>();
        Recipe recipe = createSampleReciepe();
        recipes.add(recipe);
        SearchRequestDto searchRequestDto = new SearchRequestDto();
        searchRequestDto.setServings(null);
        Mockito.when(recipeRepository.findAll()).thenReturn(recipes);
        List<RecipeResponseDto> recipeResponseDtos = gourmetOvenService.getspecificRecipes(searchRequestDto);

        Assertions.assertEquals(recipe.getName(), recipeResponseDtos.get(0).getName());
    }

    @Test
    public void GivenGourmetOvenServiceWhenSpecificRecipeWithValuesIsCalledThenSelectedRecipesFetched() {
        List<Recipe> recipes = new ArrayList<>();
        Recipe recipe = createSampleReciepe();
        recipes.add(recipe);
        SearchRequestDto searchRequestDto = new SearchRequestDto();
        searchRequestDto.setServings(2);
        Mockito.when(recipeRepository.findDistinctByDishTypeAndServingsAndIngredientsInAndInstructionsIn(Mockito.anyString(), Mockito.anyInt(), Mockito.anyList(),
                Mockito.anyList())).thenReturn(recipes);

        List<String> instructionsList = new ArrayList<>();

        Mockito.when(instructionsRepository.findAllByInstructionIn(instructionsList)).thenReturn(new ArrayList<Instruction>());
        Mockito.when(recipeRepository.findDistinctByServings(2)).thenReturn(Arrays.asList(recipe));


        List<RecipeResponseDto> recipeResponseDtos = gourmetOvenService.getspecificRecipes(searchRequestDto);

        Assertions.assertEquals(recipe.getName(), recipeResponseDtos.get(0).getName());
    }


    @Test
    public void GivenGourmetOvenServiceWhenUpdateRecipeIsCalledThenRecipeUpdatesSuccessfully() throws Exception {
        Users user = new Users();
        user.setId(101);
        Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.of(user));
        RecipeCreateRequestDto recipeCreateRequestDto = RecipeCreateRequestDto.builder().name("Idli").dishType("vegetarian").recipeId(101).
                ingredients(Arrays.asList(101, 102)).instructions(Arrays.asList("More spicy")).
                servings(2).build();
        Mockito.when(recipeRepository.existsById(Mockito.anyInt())).thenReturn(true);
        Recipe recipe = new Recipe();
        recipe.setRecipeId(101);
        recipe.setName("Sambar");
        recipe.setServings(5);
        recipe.setDishType("vegetarian");
        recipe.setUser(user);
        Mockito.when(recipeRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(recipe));
        Mockito.when(recipeRepository.save(Mockito.any(Recipe.class))).thenReturn(recipe);

        RecipeResponseDto recipeResponseDtoMock = new RecipeResponseDto();
        recipeResponseDtoMock.setRecipeId(101);
        recipeResponseDtoMock.setName("Sambar");
        Mockito.doNothing().when(recipeRepository).delete(Mockito.any());
        RecipeResponseDto recipeResponseDtoOut = gourmetOvenService.updateArecipe(recipeCreateRequestDto, "101");

        Assertions.assertEquals(recipeResponseDtoMock.getRecipeId(), recipeResponseDtoOut.getRecipeId());
        Assertions.assertEquals(recipeResponseDtoMock.getName(), recipeResponseDtoOut.getName());
    }

    @Test
    public void GivenGourmetOvenServiceWhenDeleteRecipeIsCalledThenRecipeDeletesSuccessfully() throws Exception {
        Users user = new Users();
        user.setId(101);
        Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.of(user));
        Mockito.when(recipeRepository.existsById(Mockito.anyInt())).thenReturn(true);
        Recipe recipe = new Recipe();
        recipe.setRecipeId(101);
        recipe.setName("sambar");
        recipe.setServings(5);
        recipe.setDishType("vegetarian");
        recipe.setUser(user);
        Mockito.when(recipeRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(recipe));

        gourmetOvenService.deleteRecipe(108, "101");

        Mockito.verify(recipeRepository, Mockito.times(1)).delete(Mockito.any());
    }

    private Users createSampleUser() {
        Users user = new Users();
        user.setId(100);
        user.setUsername("David");
        user.setName("David GH");
        return user;
    }

    private Recipe createSampleReciepe() {
        Recipe recipe = new Recipe();
        recipe.setRecipeId(108);
        recipe.setName("omelette");
        recipe.setServings(2);
        recipe.setDishType("non-vegetarian");
        recipe.setUser(createSampleUser());
        return recipe;
    }
}
