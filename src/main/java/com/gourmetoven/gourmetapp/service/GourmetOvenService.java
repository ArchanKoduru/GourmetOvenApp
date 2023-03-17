package com.gourmetoven.gourmetapp.service;

import com.gourmetoven.gourmetapp.dtos.Request.RecipeCreateRequestDto;
import com.gourmetoven.gourmetapp.dtos.Request.SearchRequestDto;
import com.gourmetoven.gourmetapp.dtos.Response.IngredientResponseDto;
import com.gourmetoven.gourmetapp.dtos.Response.RecipeResponseDto;
import com.gourmetoven.gourmetapp.exception.AccessErrorException;
import com.gourmetoven.gourmetapp.exception.DataFormatException;
import com.gourmetoven.gourmetapp.exception.DataNotAvailableException;
import com.gourmetoven.gourmetapp.entity.Ingredients;
import com.gourmetoven.gourmetapp.entity.Instruction;
import com.gourmetoven.gourmetapp.entity.Recipe;
import com.gourmetoven.gourmetapp.entity.Users;
import com.gourmetoven.gourmetapp.repository.IngredientRepository;
import com.gourmetoven.gourmetapp.repository.InstructionRepository;
import com.gourmetoven.gourmetapp.repository.RecipeRepository;
import com.gourmetoven.gourmetapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.gourmetoven.gourmetapp.util.Constants.*;

@Service
public class GourmetOvenService {

    private IngredientRepository ingredientRepository;

    private RecipeRepository recipeRepository;

    private UserRepository userRepository;

    private InstructionRepository instructionRepository;


    public List<IngredientResponseDto> getAllIngredients() {
        List<Ingredients> ingredients = ingredientRepository.findAll();
        List<IngredientResponseDto> ingredientResponseDtoList = new ArrayList<>();
        if (ingredients != null && !ingredients.isEmpty()) {
            ingredients.forEach(p -> extractIngredients(ingredientResponseDtoList, p));
        }
        return ingredientResponseDtoList;
    }

    public List<RecipeResponseDto> getAllRecipes() {
        List<Recipe> recipes = recipeRepository.findAll();
        List<RecipeResponseDto> recipeResponseDtos = new ArrayList<>();
        if (recipes != null && !recipes.isEmpty()) {
            recipes.forEach(p -> extractRecipe(recipeResponseDtos, p));
        }
        return recipeResponseDtos;
    }


    public List<RecipeResponseDto> findRecipesWithType(String type) {
        List<Recipe> recipes;
        if (type == null || type.isEmpty()) {
            type = ALL;
        }
        if (!type.equals(ALL) && !type.equals(VEG) && !type.equals(NONVEG)) {
            type = ALL;
        }
        if (type.equals(ALL)) {
            recipes = recipeRepository.findAll();
        } else {
            recipes = recipeRepository.findDistinctByDishType(type);
        }


        List<RecipeResponseDto> recipeResponseDtos = new ArrayList<>();
        if (recipes != null && !recipes.isEmpty()) {
            recipes.forEach(p -> extractRecipe(recipeResponseDtos, p));
        }
        return recipeResponseDtos;
    }

    public List<RecipeResponseDto> getspecificRecipes(SearchRequestDto searchRequestDto) {
        //ALL default is as good as returning all recipes
        if (isAllDefault(searchRequestDto)) {
            return getAllRecipes();
        }
        return processSearchRequest(searchRequestDto);
    }

    public RecipeResponseDto createArecipe(RecipeCreateRequestDto recipeCreateRequestDto, String name) throws Exception {
        //check user exists
        Optional<Users> user = userRepository.findByUsername(name);
        if (!user.isPresent()) {
            throw new DataNotAvailableException("User doesn't exists");
        }
        if(Objects.isNull(recipeCreateRequestDto.getName())&&recipeCreateRequestDto.getName().isEmpty())
        {
            throw new DataNotAvailableException("Recipe name is empty or null");
        }

        Recipe saveRecipe = recipeRepository.save(createRecipeFromRequestDto(recipeCreateRequestDto, user.get()));

        return createRecipeResponseDto(saveRecipe);

    }

    public RecipeResponseDto updateArecipe(RecipeCreateRequestDto recipeCreateRequestDto, String name) throws Exception {
        //check user exists
        Optional<Users> user = userRepository.findByUsername(name);
        //check recipe exists
        if (recipeCreateRequestDto.getRecipeId() == null || recipeCreateRequestDto.getRecipeId() <= 0) {
            throw new DataNotAvailableException("Recipe ID cannot be empty");
        }
        if (!recipeRepository.findById(recipeCreateRequestDto.getRecipeId()).isPresent()) {
            throw new DataNotAvailableException("Recipe not found");
        }
        if (!user.isPresent()) {
            throw new DataNotAvailableException("User doesn't exists");
        }
        //Now we should update only if the owning user is same as the logged in user
        Recipe recipe = recipeRepository.findById(recipeCreateRequestDto.getRecipeId()).get();
        if (user.get().getId() != recipe.getUser().getId()) {
            throw new AccessErrorException("You cannot update someone's recipe");
        }
        //Now we simply delete all entries from recipe, recipe-ingredientmapping, recipe-instruction mapping
        recipeRepository.delete(recipe);

        return createArecipe(recipeCreateRequestDto, name);
    }

    public void deleteRecipe(Integer recipeId, String name) throws Exception {
        //check user exists
        Optional<Users> user = userRepository.findByUsername(name);
        //check recipe exists
        if (recipeId == null || recipeId <= 0) {
            throw new DataFormatException("Recipe ID cannot be empty");
        }
        if (!recipeRepository.findById(recipeId).isPresent()) {
            throw new DataNotAvailableException("Recipe not found");
        }
        if (!user.isPresent()) {
            throw new DataNotAvailableException("User doesn't exists");
        }
        //Now we should update only if the owning user is same as the logged in user
        Recipe recipe = recipeRepository.findById(recipeId).get();
        if (user.get().getId() != recipe.getUser().getId()) {
            throw new AccessErrorException("You cannot update someone's recipe");
        }
        //Now we simply delete all entries from recipe, recipe-ingredientmapping, recipe-instruction mapping
        recipeRepository.delete(recipe);
    }

    private List<RecipeResponseDto> processSearchRequest(SearchRequestDto searchRequestDto) {
        String type = searchRequestDto.getType();
        Integer servings = searchRequestDto.getServings();

        List<Ingredients> excludeIngredietsList =
                ingredientRepository.findAllByNameIn(searchRequestDto.getExcludeIngredients());
        List<Ingredients> includeIngredietsList =
                ingredientRepository.findAllByNameIn(searchRequestDto.getIncludeIngredients());

        List<Ingredients> finalIngredientList =
                getRequireIngredientList(excludeIngredietsList, includeIngredietsList);

        List<String> instructions = searchRequestDto.getInstructions();
        List<Instruction> instructionList = new ArrayList<>();

        if (Objects.nonNull(instructions) && !instructions.isEmpty()) {
            instructionList = instructionRepository.findAllByInstructionIn(instructions);
        }
        //If type is something other than vegetarian/non-vegetarian/all then default it to all
        if (!type.equals(ALL) && !type.equals(VEG) && !type.equals(NONVEG)) {
            type = ALL;
        }

        List<Recipe> recipes;

        if (type.equals(ALL)) {
            if (instructionList.isEmpty()) {
                if (finalIngredientList.isEmpty()) {
                    if (servings == 999999) {
                        recipes = recipeRepository.findAll();
                    } else {
                        recipes = recipeRepository.findDistinctByServings(servings);
                    }
                } else {
                    if (servings == 999999) {
                        recipes = recipeRepository.findDistinctByIngredientsIn(finalIngredientList);
                    } else {
                        recipes = recipeRepository.findDistinctByServingsAndIngredientsIn(servings, finalIngredientList);
                    }
                }
            } else {
                if (finalIngredientList.isEmpty()) {
                    if (servings == 999999) {
                        recipes = recipeRepository.findDistinctByInstructionsIn(instructionList);
                    } else {
                        recipes = recipeRepository.findDistinctByServingsAndInstructionsIn(servings, instructionList);
                    }
                } else {
                    if (servings == 999999) {
                        recipes = recipeRepository.findDistinctByIngredientsInAndInstructionsIn(
                                finalIngredientList, instructionList);
                    } else {
                        recipes = recipeRepository.findDistinctByServingsAndIngredientsInAndInstructionsIn(servings,
                                finalIngredientList, instructionList);
                    }
                }

            }
        } else {
            if (instructionList.isEmpty()) {
                if (finalIngredientList.isEmpty()) {
                    if (servings == 999999) {
                        recipes = recipeRepository.findDistinctByDishType(type);
                    } else {
                        recipes = recipeRepository.findDistinctByDishTypeAndServings(type, servings);
                    }

                } else {
                    if (servings == 999999) {
                        recipes = recipeRepository.findDistinctByDishTypeAndIngredientsIn(type, finalIngredientList);
                    } else {
                        recipes = recipeRepository.findDistinctByDishTypeAndServingsAndIngredientsIn(type, servings, finalIngredientList);
                    }
                }

            } else {
                if (finalIngredientList.isEmpty()) {
                    if (servings == 999999) {
                        recipes = recipeRepository.findDistinctByDishTypeAndInstructionsIn(type, instructionList);
                    } else {
                        recipes = recipeRepository.findDistinctByDishTypeAndServingsAndInstructionsIn(type, servings, instructionList);
                    }
                } else {
                    if (servings == 999999) {
                        recipes = recipeRepository.findDistinctByDishTypeAndIngredientsInAndInstructionsIn(type,
                                finalIngredientList, instructionList);
                    } else {
                        recipes = recipeRepository.findDistinctByDishTypeAndServingsAndIngredientsInAndInstructionsIn(type, servings,
                                finalIngredientList, instructionList);
                    }
                }

            }
        }


        List<RecipeResponseDto> recipeResponseDtos = new ArrayList<>();

        if (recipes != null && !recipes.isEmpty()) {
            recipes.forEach(p -> extractRecipe(recipeResponseDtos, p));
        }

        return recipeResponseDtos;
    }

    private boolean isAllDefault(SearchRequestDto searchRequestDto) {
        if(Objects.isNull(searchRequestDto)){
            return true;
        }
        String type = searchRequestDto.getType();
        Integer servings = searchRequestDto.getServings();
        List<String> excludeIngredients = searchRequestDto.getExcludeIngredients();
        List<String> includeIngredients = searchRequestDto.getIncludeIngredients();
        List<String> instructions = searchRequestDto.getInstructions();
        return type.equals(ALL) && (servings == null||servings == 999999)
                && (Objects.isNull(excludeIngredients)||excludeIngredients.isEmpty())
                && (Objects.isNull(includeIngredients)||includeIngredients.isEmpty())
                && (Objects.isNull(instructions)||instructions.isEmpty());
    }

    private void extractRecipe(List<RecipeResponseDto> recipeResponseDtos, Recipe recipe) {
        recipeResponseDtos.add(createRecipeResponseDto(recipe));
    }

    private void extractIngredients(List<IngredientResponseDto> ingredientResponseDtoList, Ingredients ingredients) {
        if (Objects.nonNull(ingredients)) {
            ingredientResponseDtoList.add(IngredientResponseDto.builder().ingredientId(ingredients.
                    getIngredientId()).ingredientName(ingredients.getName()).build());
        }

    }

    private void extractInstruction(List<String> instructionList, Instruction instruction) {
        if (Objects.nonNull(instruction)) {
            instructionList.add(instruction.getInstruction());
        }

    }

    public RecipeResponseDto createRecipeResponseDto(Recipe recipe) {
        List<IngredientResponseDto> ingredientResponseDtoList = new ArrayList<>();
        if (Objects.nonNull(recipe.getIngredients()) && !recipe.getIngredients().isEmpty()) {
            recipe.getIngredients().forEach(p -> extractIngredients(ingredientResponseDtoList, p));
        }
        List<String> instructionList = new ArrayList<>();
        if (Objects.nonNull(recipe.getInstructions()) && !recipe.getInstructions().isEmpty()) {
            recipe.getInstructions().forEach(p -> extractInstruction(instructionList, p));
        }


        return RecipeResponseDto.builder()
                .recipeId(recipe.getRecipeId())
                .name(recipe.getName())
                .dishType(recipe.getDishType())
                .creationType(recipe.getCreationType())
                .servings(recipe.getServings())
                .user(recipe.getUser().getUsername())
                .ingredients(ingredientResponseDtoList)
                .instructions(instructionList).build();
    }

    public Recipe createRecipeFromRequestDto(RecipeCreateRequestDto recipeCreateRequestDto, Users user) {
        List<Ingredients> ingredientsList = ingredientRepository.findAllById(recipeCreateRequestDto.getIngredients());
        List<Instruction> instructionList = instructionRepository.findAllByInstructionIn(recipeCreateRequestDto.getInstructions());

        Recipe recipe = new Recipe();
        recipe.setName(recipeCreateRequestDto.getName());
        recipe.setServings(recipeCreateRequestDto.getServings());
        recipe.setDishType(recipeCreateRequestDto.getDishType());
        recipe.setCreationType("Custom");
        recipe.setUser(user);
        recipe.setIngredients(ingredientsList);
        recipe.setInstructions(instructionList);

        return recipe;
    }

    private List<Ingredients> getRequireIngredientList(List<Ingredients> excludeIngredietsList,
                                                       List<Ingredients> includeIngredietsList) {
        List<Ingredients> finalIngredientList;
        if (!excludeIngredietsList.isEmpty() && !includeIngredietsList.isEmpty()) {
            finalIngredientList = includeIngredietsList.stream()
                    .filter(item -> !excludeIngredietsList.contains(item))
                    .collect(Collectors.toList());
        } else if (!excludeIngredietsList.isEmpty() && includeIngredietsList.isEmpty()) {
            finalIngredientList = ingredientRepository.findAll().stream()
                    .filter(item -> !excludeIngredietsList.contains(item))
                    .collect(Collectors.toList());
        } else if (excludeIngredietsList.isEmpty() && !includeIngredietsList.isEmpty()) {
            finalIngredientList = includeIngredietsList;
        } else {
            finalIngredientList = ingredientRepository.findAll();
        }

        return finalIngredientList;
    }

    public IngredientRepository getIngredientRepository() {
        return ingredientRepository;
    }

    public RecipeRepository getRecipeRepository() {
        return recipeRepository;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public InstructionRepository getInstructionRepository() {
        return instructionRepository;
    }

    @Autowired
    public void setIngredientRepository(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    @Autowired
    public void setInstructionRepository(InstructionRepository instructionRepository) {
        this.instructionRepository = instructionRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setRecipeRepository(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }
}
