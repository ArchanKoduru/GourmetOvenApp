package com.gourmetoven.gourmetapp.repository;

import com.gourmetoven.gourmetapp.entity.Ingredients;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IngredientRepository extends JpaRepository<Ingredients, Integer>, JpaSpecificationExecutor<Ingredients> {
    Ingredients findByName(String names);
    List<Ingredients> findAllByNameIn(List<String> ingredeintList);

    @Query("SELECT MAX(ind.ingredientId) FROM Ingredients ind")
    Long findMaxIngredientsId();
}
