package com.blackou.Cocktailschatbot.repository;

import com.blackou.Cocktailschatbot.entity.CocktailUser;
import com.blackou.Cocktailschatbot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CocktailUserRepo extends JpaRepository<CocktailUser, Long> {
    CocktailUser findByUser(User user);
}
