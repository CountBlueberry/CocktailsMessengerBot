package com.blackou.Cocktailschatbot.service;


import com.blackou.Cocktailschatbot.builder.RequestBuilder;
import com.blackou.Cocktailschatbot.constant.PostbackPayload;
import com.blackou.Cocktailschatbot.entity.User;
import com.botscrew.messengercdk.model.outgoing.element.button.Button;
import com.botscrew.messengercdk.model.outgoing.element.button.PostbackButton;
import com.botscrew.messengercdk.service.Messenger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuService {
    private static final String SEARCH_MESSAGE = "Search for cocktail";
    private static final String RANDOM_COCKTAIL_MESSAGE = "Random cocktail please";
    private static final String SHOW_FAVORITE_MESSAGE = "Show favorite cocktails";
    private static final String GREETING_MESSAGE = "Hi %s! Would you like to find a cocktail?";

    private final Messenger messenger;
    private final SenderService senderService;
    private final RequestBuilder requestBuilder;

    public void defaultAnswer(User user) {
        String message = String.format(GREETING_MESSAGE, messenger.getProfile(user.getChatId()).getFirstName());
        senderService.sendMessage(requestBuilder.buildRequestForButtonReply(user, buildQuickReplyList(), message),user);
    }

    private List<Button> buildQuickReplyList() {
        PostbackButton cocktailSearch = new PostbackButton(SEARCH_MESSAGE, PostbackPayload.SEARCH_COCKTAIL);
        PostbackButton randomCocktail = new PostbackButton(RANDOM_COCKTAIL_MESSAGE, PostbackPayload.FIND_RANDOM);
        PostbackButton favoriteCocktails = new PostbackButton(SHOW_FAVORITE_MESSAGE, PostbackPayload.SHOW_FAVORITE);
        List<Button> quickReplies = new ArrayList<>();
        quickReplies.add(cocktailSearch);
        quickReplies.add(randomCocktail);
        quickReplies.add(favoriteCocktails);
        return quickReplies;
    }
}
