package com.blackou.Cocktailschatbot.entity;


import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "cocktails")
public class Cocktail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cocktailId;
    private String strDrink;
    private String strAlcoholic;
    private String strGlass;
    @Column(length = 5000)
    private String strInstructions;
    private String strDrinkThumb;
    private String strIngredient1;
    private String strIngredient2;
    private String strIngredient3;
    private String strIngredient4;
    private String strIngredient5;
    private String strIngredient6;
    private String strIngredient7;
    private String strIngredient8;
    private String strIngredient9;
    private String strIngredient10;
    private String strIngredient11;
    private String strIngredient12;
    private String strIngredient13;
    private String strIngredient14;
    private String strIngredient15;
    private String strIngredient16;

    public List<String> getAllIngredients() {
        List<String> ingredientsList = new ArrayList<>();
        ingredientsList.add(strIngredient1);
        ingredientsList.add(strIngredient2);
        ingredientsList.add(strIngredient3);
        ingredientsList.add(strIngredient4);
        ingredientsList.add(strIngredient5);
        ingredientsList.add(strIngredient6);
        ingredientsList.add(strIngredient7);
        ingredientsList.add(strIngredient8);
        ingredientsList.add(strIngredient9);
        ingredientsList.add(strIngredient10);
        ingredientsList.add(strIngredient11);
        ingredientsList.add(strIngredient12);
        ingredientsList.add(strIngredient13);
        ingredientsList.add(strIngredient14);
        ingredientsList.add(strIngredient15);
        ingredientsList.add(strIngredient16);
        return ingredientsList;
    }
}
