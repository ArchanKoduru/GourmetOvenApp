package com.gourmetoven.gourmetapp.entity;

import javax.persistence.*;

@Entity
@SequenceGenerator(name = "ING_SEQ")
public class Ingredients {
    @Id
    @Column(name = "ingredient_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "ING_SEQ")
    private Integer ingredientId;

    @Column(name = "ingredient_name")
    private String name;


    public Ingredients() {
    }

    public Ingredients(Integer ingredientId, String name) {
        this.ingredientId = ingredientId;
        this.name = name;
    }

    public Integer getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(Integer ingredientId) {
        this.ingredientId = ingredientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Ingredients{" +
                "ingredientId=" + ingredientId +
                ", name='" + name + '\'' +
                '}';
    }
}
