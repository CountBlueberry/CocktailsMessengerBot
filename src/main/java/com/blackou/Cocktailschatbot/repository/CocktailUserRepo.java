package com.blackou.Cocktailschatbot.repository;

import com.blackou.Cocktailschatbot.entity.CocktailUser;
import com.blackou.Cocktailschatbot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CocktailUserRepo extends JpaRepository<CocktailUser, Long> {
    Optional<CocktailUser> findByUser(User user);
}
