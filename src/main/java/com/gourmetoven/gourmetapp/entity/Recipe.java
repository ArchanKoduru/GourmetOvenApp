package com.gourmetoven.gourmetapp.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;


@Entity
@Table(name = "recipe")
@SequenceGenerator(name = "RCP_SEQ")
public class Recipe {
    @Id
    @Column(name = "recipe_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "RCP_SEQ")
    private Integer recipeId;

    @Column(name = "recipe_name",nullable = false)
    private String name;

    @Column(name = "servings")
    private Integer servings;

    @Column(name = "dish_type")
    private String dishType;

    @Column(name = "creation_type")
    private String creationType;

    @ManyToOne
    @JoinColumn(name = "owning_user")
    private Users user;

    @ManyToMany
    @JoinColumn(name = "recpie_ingredient", referencedColumnName = "ingredient_id")
    private List<Ingredients> ingredients;

    @ManyToMany
    @JoinColumn(name = "recpie_instruction", referencedColumnName = "instruction_id")
    private List<Instruction> instructions;

    public Users getUser() {
        return user;
    }

    public Recipe() {
    }


    public Integer getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Integer recipeId) {
        this.recipeId = recipeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public String getDishType() {
        return dishType;
    }

    public void setDishType(String dishType) {
        this.dishType = dishType;
    }

    public String getCreationType() {
        return creationType;
    }

    public void setCreationType(String creationType) {
        this.creationType = creationType;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public List<Ingredients> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredients> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Instruction> getInstructions() {
        return instructions;
    }

    public void setInstructions(List<Instruction> instructions) {
        this.instructions = instructions;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "recipeId=" + recipeId +
                ", name='" + name + '\'' +
                ", servings=" + servings +
                ", dishType='" + dishType + '\'' +
                ", creationType='" + creationType + '\'' +
                ", ingredients=" + ingredients +
                ", instructions=" + instructions +
                '}';
    }
}
