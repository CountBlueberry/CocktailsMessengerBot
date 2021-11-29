package com.blackou.Cocktailschatbot.service;

import com.blackou.Cocktailschatbot.builder.RequestBuilder;
import com.blackou.Cocktailschatbot.builder.TemplateBuilder;
import com.blackou.Cocktailschatbot.client.CocktailAPIClient;
import com.blackou.Cocktailschatbot.constant.ChatState;
import com.blackou.Cocktailschatbot.constant.PostbackPayload;
import com.blackou.Cocktailschatbot.constant.UrlParameters;
import com.blackou.Cocktailschatbot.entity.Cocktail;
import com.blackou.Cocktailschatbot.entity.CocktailUser;
import com.blackou.Cocktailschatbot.entity.User;
import com.blackou.Cocktailschatbot.model.incomming.CocktailResponseDto;
import com.blackou.Cocktailschatbot.repository.CocktailRepo;
import com.blackou.Cocktailschatbot.repository.CocktailUserRepo;
import com.botscrew.messengercdk.model.outgoing.element.TemplateElement;
import com.botscrew.messengercdk.model.outgoing.element.button.PostbackButton;
import com.botscrew.messengercdk.model.outgoing.element.quickreply.PostbackQuickReply;
import com.botscrew.messengercdk.model.outgoing.element.quickreply.QuickReply;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class CocktailService {
    private static final String WRONG_COCKTAIL_NAME = "Sorry couldn't find cocktail with name %s";
    private static final String WRONG_COCKTAIL_LETTER = "Sorry couldn't find cocktails with first letter %s";
    private static final String WRONG_COCKTAIL_INGREDIENT = "Sorry couldn't find cocktails with ingredient %s";
    private static final String NEXT_PACK_MESSAGE = "Next pack";
    private static final String TRY_AGAIN_MESSAGE = "Try again.";
    private static final String ADD_COCKTAIL_TO_FAVORITE_MESSAGE = "Cocktail %s added to your favorite cocktails";
    private static final String REMOVE_COCKTAIL_FROM_FAVORITE_MESSAGE = "Cocktail %s removed from your favorite cocktails";
    private static final String ANOTHER_ONE_MESSAGE = "Get me another one.";
    private static final String MENU_MESSAGE = "Back to menu.";

    private final CocktailAPIClient cocktailAPIClient;
    private final SenderService senderService;
    private final TemplateBuilder templateBuilder;
    private final RequestBuilder requestBuilder;
    private final UserService userService;
    private final CocktailRepo cocktailRepo;
    private final CocktailUserRepo cocktailUserRepo;

    public void getRandomCocktail(User user) {
        Cocktail cocktail = cocktailAPIClient.getRandomCocktail().getCocktailList().get(0);
        userService.changeState(user, ChatState.RANDOM);
        senderService.sendMessage(
                requestBuilder.buildRequestForTemplate(user,
                        templateBuilder.checkIfCocktailInFavorite(user, cocktail),
                        buildQuickReplyList()), user);
    }

    public void getCocktailByName(User user, String cocktailName) {
        Optional<List<Cocktail>> cocktailListOpt = cocktailAPIClient.getCocktailByNameOptional(cocktailName);
        if (cocktailListOpt.isPresent()) {
            Cocktail cocktail = cocktailListOpt.get().get(0);
            userService.changeState(user, ChatState.BY_NAME);
            senderService.sendMessage(
                    requestBuilder.buildRequestForTemplate(user,
                            templateBuilder.checkIfCocktailInFavorite(user, cocktail),
                            buildQuickReplyList()), user);
        } else
            senderService.sendMessage(
                    requestBuilder.buildRequestForQuickReply(user,
                            buildQuickReplyListWrongInput(new PostbackQuickReply(TRY_AGAIN_MESSAGE, PostbackPayload.FIND_COCKTAIL_BY_NAME)),
                            String.format(WRONG_COCKTAIL_NAME, cocktailName)), user);
    }

    public void getCocktailByFirstLetter(User user, String letter, Integer loop) {
        userService.changeState(user, ChatState.BY_LATTER);
        List<TemplateElement> templateElementList = new ArrayList<>();
        Optional<CocktailResponseDto> cocktailResponseDtoOpt = cocktailAPIClient.getCocktailByFirstLetterOptional(letter);
        if (cocktailResponseDtoOpt.isPresent()) {
            cocktailResponseDtoOpt.get().getCocktailList().forEach(cocktail -> {
                if ((templateElementList.size() + 1) % 10 == 0) {
                    templateElementList.add(templateBuilder.checkIfCocktailInFavoriteWithButton(user,
                            cocktail,
                            new PostbackButton(NEXT_PACK_MESSAGE,
                                    PostbackPayload.NEXT_BY_LETTER +
                                            UrlParameters.LOOP_PARAM +
                                            (templateElementList.size() + 1) +
                                            UrlParameters.COCKTAIL_FIRST_LETTER_PARAM +
                                            letter)));
                } else {
                    templateElementList.add(templateBuilder.checkIfCocktailInFavorite(user, cocktail));
                }
            });
            if (templateElementList.size() < loop + 10) {
                senderService.sendMessage(
                        requestBuilder.buildRequestForTemplate(user,
                                templateElementList.subList(loop, templateElementList.size()),
                                buildQuickReplyList()), user);
            } else {
                senderService.sendMessage(
                        requestBuilder.buildRequestForTemplate(user,
                                templateElementList.subList(loop, loop + 10),
                                buildQuickReplyList()),
                        user);
            }
        } else
            senderService.sendMessage(
                    requestBuilder.buildRequestForQuickReply(user,
                            buildQuickReplyListWrongInput(new PostbackQuickReply(TRY_AGAIN_MESSAGE, PostbackPayload.FIND_COCKTAIL_BY_FIRST_LETTER)),
                            String.format(WRONG_COCKTAIL_LETTER, letter)),
                    user);
    }

    public void getCocktailByIngredient(User user, String ingredient, Integer loop) {
        userService.changeState(user, ChatState.BY_INGREDIENT);
        List<TemplateElement> templateElementList = new ArrayList<>();
        Optional<CocktailResponseDto> cocktailResponseDtoOpt = cocktailAPIClient.getCocktailByIngredient(ingredient);
        if (cocktailResponseDtoOpt.isPresent()) {
            List<Cocktail> cocktailList = new ArrayList<>();
            cocktailResponseDtoOpt.get().getCocktailList().forEach(cocktail -> cocktailList.add(cocktailAPIClient.getCocktailByName(cocktail.getStrDrink()).getCocktailList().get(0)));
            CocktailResponseDto cocktailResponseDtoFull = new CocktailResponseDto();
            cocktailResponseDtoFull.setCocktailList(cocktailList);
            cocktailResponseDtoFull.getCocktailList().forEach(cocktail -> {
                if ((templateElementList.size() + 1) % 10 == 0) {
                    templateElementList.add(templateBuilder.checkIfCocktailInFavoriteWithButton(user,
                            cocktail,
                            new PostbackButton(
                                    NEXT_PACK_MESSAGE,
                                    PostbackPayload.FIND_COCKTAIL_BY_INGREDIENT +
                                            UrlParameters.LOOP_PARAM +
                                            (templateElementList.size() + 1) +
                                            UrlParameters.INGREDIENT_PARAM +
                                            ingredient)));
                } else {
                    templateElementList.add(templateBuilder.checkIfCocktailInFavorite(user, cocktail));
                }
            });
            if (templateElementList.size() < loop + 10) {
                senderService.sendMessage(
                        requestBuilder.buildRequestForTemplate(user, templateElementList.subList(loop, templateElementList.size()), buildQuickReplyList()), user);
            } else {
                senderService.sendMessage(
                        requestBuilder.buildRequestForTemplate(user, templateElementList.subList(loop, loop + 10), buildQuickReplyList()), user);
            }
        } else
            senderService.sendMessage(
                    requestBuilder.buildRequestForQuickReply(
                            user,
                            buildQuickReplyListWrongInput(new PostbackQuickReply(TRY_AGAIN_MESSAGE, PostbackPayload.FIND_COCKTAIL_BY_INGREDIENT)),
                            String.format(WRONG_COCKTAIL_INGREDIENT, ingredient)), user);
    }

    public void addCocktailToFavorite(User user, String cocktailName) {
        Cocktail cocktail = cocktailAPIClient.getCocktailByName(cocktailName).getCocktailList().get(0);
        saveCocktailToDb(cocktail);
        CocktailUser cocktailUser = cocktailUserRepo.findByUser(user);
        if (cocktailUser == null) {
            cocktailUser = new CocktailUser();
            cocktailUser.setUser(user);
        }
        Set<Cocktail> cocktailSet = new HashSet<>();
        if (cocktailUser.getCocktails() != null) {
            cocktailSet = cocktailUser.getCocktails();
            if (!cocktailSet.contains(cocktail)) {
                cocktailSet.add(cocktail);
                cocktailUser.setCocktails(cocktailSet);
            }
        } else {
            cocktailSet.add(cocktail);
            cocktailUser.setCocktails(cocktailSet);
        }
        cocktailUserRepo.save(cocktailUser);
        senderService.sendMessage(requestBuilder.buildRequestForQuickReply(user,
                buildQuickReplyList(), String.format(ADD_COCKTAIL_TO_FAVORITE_MESSAGE, cocktailName)), user);
    }

    private void saveCocktailToDb(Cocktail cocktail) {
        if (cocktailRepo.findByStrDrink(cocktail.getStrDrink()) == null) {
            cocktailRepo.save(cocktail);
        }
    }

    public void removeCocktailFromFavorite(User user, String cocktailName) {
        Cocktail cocktail = cocktailRepo.findByStrDrink(cocktailName);
        CocktailUser cocktailUser = cocktailUserRepo.findByUser(user);
        Set<Cocktail> cocktails = cocktailUser.getCocktails();
        cocktails.remove(cocktail);
        cocktailUserRepo.delete(cocktailUser);
        cocktailUser = new CocktailUser();
        cocktailUser.setUser(user);
        cocktailUser.setCocktails(cocktails);
        cocktailUserRepo.save(cocktailUser);
        senderService.sendMessage(
                requestBuilder.buildRequestForQuickReply(
                        user,
                        buildQuickReplyList(),
                        String.format(REMOVE_COCKTAIL_FROM_FAVORITE_MESSAGE, cocktailName)),
                user);
    }

    public void showFavoriteCocktails(User user, Integer loop) {
        Set<Cocktail> cocktailSet = new HashSet<>(cocktailUserRepo.findByUser(user).getCocktails());
        List<TemplateElement> templateElementList = new ArrayList<>();
        cocktailSet.forEach(cocktail -> {
            if ((templateElementList.size() + 1) % 10 == 0) {
                templateElementList.add(templateBuilder.buildCocktailTemplateWithButtonRemove(
                        cocktail,
                        new PostbackButton(NEXT_PACK_MESSAGE, PostbackPayload.NEXT_FAVORITE + UrlParameters.LOOP_PARAM + (templateElementList.size() + 1))));
            } else {
                templateElementList.add(templateBuilder.buildCocktailTemplateRemove(cocktail));
            }
        });
        if (templateElementList.size() < loop + 10) {
            senderService.sendMessage(requestBuilder.buildRequestForTemplate(user, templateElementList.subList(loop, templateElementList.size()), buildQuickReplyList()), user);
        } else {
            senderService.sendMessage(requestBuilder.buildRequestForTemplate(user, templateElementList.subList(loop, loop + 10), buildQuickReplyList()), user);
        }
    }

    private List<QuickReply> buildQuickReplyList() {
        QuickReply getAnotherCocktail = new PostbackQuickReply(ANOTHER_ONE_MESSAGE, PostbackPayload.ANOTHER_ONE);
        QuickReply backToMenu = new PostbackQuickReply(MENU_MESSAGE, PostbackPayload.MENU);
        List<QuickReply> quickReplies = new ArrayList<>();
        quickReplies.add(getAnotherCocktail);
        quickReplies.add(backToMenu);
        return quickReplies;
    }

    private List<QuickReply> buildQuickReplyListWrongInput(QuickReply tryAgain) {
        QuickReply backToMenu = new PostbackQuickReply(MENU_MESSAGE, PostbackPayload.MENU);
        List<QuickReply> quickReplies = new ArrayList<>();
        quickReplies.add(tryAgain);
        quickReplies.add(backToMenu);
        return quickReplies;
    }
}