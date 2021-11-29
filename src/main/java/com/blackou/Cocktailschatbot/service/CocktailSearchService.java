package com.blackou.Cocktailschatbot.service;

import com.blackou.Cocktailschatbot.builder.RequestBuilder;
import com.blackou.Cocktailschatbot.constant.PostbackPayload;
import com.botscrew.messengercdk.model.MessengerUser;
import com.botscrew.messengercdk.model.outgoing.element.button.Button;
import com.botscrew.messengercdk.model.outgoing.element.button.PostbackButton;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CocktailSearchService {
    private final static String SEARCH_MESSAGE = "How you want to search for a cocktail?";
    private static final String BY_NAME_MESSAGE = "By name";
    private static final String BY_FIRST_LETTER_MESSAGE = "By first letter";
    private static final String BY_INGREDIENT_MESSAGE = "By ingredient";

    private final SenderService senderService;
    private final RequestBuilder requestBuilder;

    public void searchMenu(MessengerUser user) {
        senderService.sendMessage(requestBuilder.buildRequestForButtonReply(user, quickRepliesBuilder(), SEARCH_MESSAGE), user);
    }

    private List<Button> quickRepliesBuilder() {
        PostbackButton findByName = new PostbackButton(BY_NAME_MESSAGE, PostbackPayload.FIND_COCKTAIL_BY_NAME);
        PostbackButton findByFirstLetter = new PostbackButton(BY_FIRST_LETTER_MESSAGE, PostbackPayload.FIND_COCKTAIL_BY_FIRST_LETTER);
        PostbackButton findByIngredient = new PostbackButton(BY_INGREDIENT_MESSAGE, PostbackPayload.FIND_COCKTAIL_BY_INGREDIENT);
        List<Button> quickReplies = new ArrayList<>();
        quickReplies.add(findByName);
        quickReplies.add(findByFirstLetter);
        quickReplies.add(findByIngredient);
        return quickReplies;
    }
}
