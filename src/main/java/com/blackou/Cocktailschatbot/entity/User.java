package com.blackou.Cocktailschatbot.entity;

import com.botscrew.messengercdk.model.MessengerUser;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "users")
public class User implements MessengerUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long userId;
    private Long chatId;
    private String state;

    @OneToOne
    @JoinColumn(name = "cocktail_user_id")
    private CocktailUser cocktailUser;

    @Override
    public Long getChatId() {
        return chatId;
    }

    @Override
    public String getState() {
        return state;
    }
}
