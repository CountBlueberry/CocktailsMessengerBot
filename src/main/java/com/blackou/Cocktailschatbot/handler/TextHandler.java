package com.blackou.Cocktailschatbot.handler;


import com.blackou.Cocktailschatbot.constant.ChatState;
import com.blackou.Cocktailschatbot.service.*;
import com.blackou.Cocktailschatbot.entity.User;
import com.botscrew.botframework.annotation.ChatEventsProcessor;
import com.botscrew.botframework.annotation.Text;
import lombok.RequiredArgsConstructor;

@ChatEventsProcessor
@RequiredArgsConstructor
public class TextHandler {
    private static final Integer DEFAULT_LOOP = 0;

    private final MenuService menuService;
    private final CocktailService cocktailService;

    @Text(states = ChatState.DEFAULT)
    public void handleText(User user) {
        menuService.defaultAnswer(user);
    }

    @Text(states = ChatState.ENTER_NAME)
    public void getCocktailByName(User user, @Text String name) {
        cocktailService.getCocktailByName(user, name);
    }

    @Text(states = ChatState.BY_LATTER)
    public void getCocktailByFirstName(User user, @Text String letter) {
        cocktailService.getCocktailByFirstLetter(user, letter, DEFAULT_LOOP);
    }

    @Text(states = ChatState.ENTER_INGREDIENT)
    public void getCocktailByIngredient(User user, @Text String ingredient) {
        cocktailService.getCocktailByIngredient(user, ingredient, DEFAULT_LOOP);
    }
}
