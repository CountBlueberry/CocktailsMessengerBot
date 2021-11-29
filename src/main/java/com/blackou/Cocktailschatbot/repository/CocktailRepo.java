package com.blackou.Cocktailschatbot.repository;


import com.blackou.Cocktailschatbot.entity.Cocktail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CocktailRepo extends JpaRepository<Cocktail, Long> {
    Cocktail findByStrDrink(String strDrink);
}
