package com.blackou.Cocktailschatbot.handler;

import com.blackou.Cocktailschatbot.builder.RequestBuilder;
import com.blackou.Cocktailschatbot.constant.ChatState;
import com.blackou.Cocktailschatbot.constant.PostbackPayload;
import com.blackou.Cocktailschatbot.service.*;
import com.blackou.Cocktailschatbot.entity.User;
import com.blackou.Cocktailschatbot.service.MenuService;
import com.blackou.Cocktailschatbot.service.SenderService;
import com.botscrew.botframework.annotation.*;
import lombok.RequiredArgsConstructor;

@ChatEventsProcessor
@RequiredArgsConstructor
public class PostbackHandler {
    private static final String ENTER_COCKTAIL_NAME_MESSAGE = "Write name of the cocktail";
    private static final String ENTER_COCKTAIL_LETTER_MESSAGE = "Write first letter of the name of the cocktail";
    private static final String ENTER_COCKTAIL_INGREDIENT_MESSAGE = "Write name of the ingredient";
    private static final Integer DEFAULT_LOOP = 0;

    private final CocktailService cocktailService;
    private final MenuService menuService;
    private final CocktailInstructionService makingCocktailService;
    private final CocktailSearchService cocktailSearchService;
    private final SenderService senderService;
    private final RequestBuilder requestBuilder;
    private final UserService userService;

    @Postback(PostbackPayload.FIND_RANDOM)
    public void handleRandomCocktail(User user) {
        cocktailService.getRandomCocktail(user);
    }

    @Postback(value = PostbackPayload.ANOTHER_ONE, states = ChatState.RANDOM)
    public void handleAnotherRandomCocktail(User user) {
        cocktailService.getRandomCocktail(user);
    }

    @Postback(value = PostbackPayload.ANOTHER_ONE, states = ChatState.BY_NAME)
    public void handleAnotherByNameCocktail(User user) {
        handleSearchCocktailByName(user);
    }

    @Postback(value = PostbackPayload.ANOTHER_ONE, states = ChatState.BY_LATTER)
    public void handleAnotherByLetterCocktail(User user) {
        handleSearchCocktailByFirstLetter(user);
    }

    @Postback(value = PostbackPayload.ANOTHER_ONE, states = ChatState.BY_INGREDIENT)
    public void handleAnotherByIngredient(User user) {
        handleSearchCocktailByIngredient(user);
    }

    @Postback(value = PostbackPayload.COCKTAIL_INSTRUCTION)
    public void handleHowToMake(User user, @Param("cocktail") String cocktailName) {
        makingCocktailService.instructionForCocktailByName(user, cocktailName);
    }

    @Postback(PostbackPayload.SEARCH_COCKTAIL)
    public void handleSearchCocktail(User user) {
        cocktailSearchService.searchMenu(user);
    }

    @Postback(PostbackPayload.MENU)
    public void handleMenu(User user) {
        userService.changeState(user, ChatState.DEFAULT);
        menuService.defaultAnswer(user);
    }

    @Postback(PostbackPayload.FIND_COCKTAIL_BY_NAME)
    public void handleSearchCocktailByName(User user) {
        senderService.sendMessage(requestBuilder.buildTextRequest(user, ENTER_COCKTAIL_NAME_MESSAGE), user);
        userService.changeState(user, ChatState.ENTER_NAME);
    }

    @Postback(PostbackPayload.FIND_COCKTAIL_BY_FIRST_LETTER)
    public void handleSearchCocktailByFirstLetter(User user) {
        senderService.sendMessage(requestBuilder.buildTextRequest(user, ENTER_COCKTAIL_LETTER_MESSAGE), user);
        userService.changeState(user, ChatState.ENTER_LETTER);
    }

    @Postback(PostbackPayload.FIND_COCKTAIL_BY_INGREDIENT)
    public void handleSearchCocktailByIngredient(User user) {
        senderService.sendMessage(requestBuilder.buildTextRequest(user, ENTER_COCKTAIL_INGREDIENT_MESSAGE), user);
        userService.changeState(user, ChatState.ENTER_INGREDIENT);
    }

    @Postback(PostbackPayload.NEXT_BY_INGREDIENT)
    public void handleNextPackOfCocktailsByIngredient(User user, @Param("loop") Integer loop, @Param("ingredient") String ingredient) {
        cocktailService.getCocktailByIngredient(user, ingredient, loop);
    }

    @Postback(PostbackPayload.NEXT_BY_LETTER)
    public void handleNextPackOfCocktailsByName(User user, @Param("loop") Integer loop, @Param("letter") String letter) {
        cocktailService.getCocktailByFirstLetter(user, letter, loop);
    }

    @Postback(PostbackPayload.NEXT_FAVORITE)
    public void handleNextPackOfFavoriteCocktails(User user, @Param("loop") Integer loop) {
        cocktailService.showFavoriteCocktails(user, loop);
    }

    @Postback(PostbackPayload.ADD_TO_FAVORITE)
    public void handleAddToFavorite(User user, @Param("cocktail") String cocktailName) {
        cocktailService.addCocktailToFavorite(user, cocktailName);
    }

    @Postback(PostbackPayload.REMOVE_FROM_FAVORITE)
    public void handleRemoveFromFavorite(User user, @Param("cocktail") String cocktailName) {
        cocktailService.removeCocktailFromFavorite(user, cocktailName);
    }

    @Postback(PostbackPayload.SHOW_FAVORITE)
    public void handleShowFavoriteCocktails(User user) {
        cocktailService.showFavoriteCocktails(user, DEFAULT_LOOP);
    }
}
