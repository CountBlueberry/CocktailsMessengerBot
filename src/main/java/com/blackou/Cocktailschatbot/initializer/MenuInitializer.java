package com.blackou.Cocktailschatbot.initializer;


import com.blackou.Cocktailschatbot.constant.PostbackPayload;
import com.botscrew.botframework.annotation.ChatEventsProcessor;
import com.botscrew.messengercdk.model.outgoing.element.button.GetStartedButton;
import com.botscrew.messengercdk.model.outgoing.profile.Greeting;
import com.botscrew.messengercdk.model.outgoing.profile.menu.PersistentMenu;
import com.botscrew.messengercdk.model.outgoing.profile.menu.PostbackMenuItem;
import com.botscrew.messengercdk.service.Messenger;
import lombok.RequiredArgsConstructor;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@ChatEventsProcessor
@RequiredArgsConstructor
public class MenuInitializer {
    private static final String GREETING_MESSAGE = "HI! Do you want to find some cocktails?";
    private static final String SEARCH_MESSAGE = "Search for cocktail";
    private static final String RANDOM_COCKTAIL_MESSAGE = "Random cocktail please";
    private static final String SHOW_FAVORITE_MESSAGE = "Show favorite cocktails";

    private final Messenger messenger;

    @PostConstruct
    public void initMessengerProfile() {
        messenger.setGetStartedButton(new GetStartedButton(PostbackPayload.MENU));
        messenger.setGreeting(new Greeting(GREETING_MESSAGE));
        PersistentMenu menu = new PersistentMenu(
                Arrays.asList(
                        new PostbackMenuItem(SEARCH_MESSAGE, PostbackPayload.SEARCH_COCKTAIL),
                        new PostbackMenuItem(RANDOM_COCKTAIL_MESSAGE, PostbackPayload.FIND_RANDOM),
                        new PostbackMenuItem(SHOW_FAVORITE_MESSAGE, PostbackPayload.SHOW_FAVORITE))
        );
        messenger.setPersistentMenu(menu);
    }
}
