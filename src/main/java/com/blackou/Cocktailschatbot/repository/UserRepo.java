package com.blackou.Cocktailschatbot.repository;

import com.blackou.Cocktailschatbot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    public User findByChatId(Long chatId);
}
