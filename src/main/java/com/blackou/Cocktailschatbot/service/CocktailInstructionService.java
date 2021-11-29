package com.blackou.Cocktailschatbot.service;

import com.blackou.Cocktailschatbot.builder.RequestBuilder;
import com.blackou.Cocktailschatbot.client.CocktailAPIClient;
import com.blackou.Cocktailschatbot.constant.PostbackPayload;
import com.blackou.Cocktailschatbot.entity.Cocktail;
import com.blackou.Cocktailschatbot.entity.User;
import com.botscrew.messengercdk.model.outgoing.element.quickreply.PostbackQuickReply;
import com.botscrew.messengercdk.model.outgoing.element.quickreply.QuickReply;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CocktailInstructionService {
    private final static String NEW_LINE = "\n";
    private static final String COMMA = ", ";
    private static final String ANOTHER_ONE_MESSAGE = "Get me another one.";
    private static final String MENU_MESSAGE = "Back to menu.";

    private final SenderService senderService;
    private final RequestBuilder requestBuilder;
    private final CocktailAPIClient cocktailAPIClient;

    public void instructionForCocktailByName(User user, String name) {
        senderService.sendMessage(requestBuilder.buildRequestForQuickReply(user, buildQuickReplyList(), textBuilder(cocktailAPIClient.getCocktailByName(name).getCocktailList().get(0))), user);
    }

    private String textBuilder(Cocktail cocktail) {
        return cocktail.getStrDrink() +
                NEW_LINE +
                cocktail
                        .getAllIngredients()
                        .stream()
                        .filter(Objects::nonNull)
                        .collect(Collectors.joining(COMMA)) +
                NEW_LINE +
                cocktail.getStrGlass() +
                NEW_LINE +
                cocktail.getStrInstructions();
    }

    private List<QuickReply> buildQuickReplyList() {
        QuickReply getAnotherCocktail = new PostbackQuickReply(ANOTHER_ONE_MESSAGE, PostbackPayload.ANOTHER_ONE);
        QuickReply backToMenu = new PostbackQuickReply(MENU_MESSAGE, PostbackPayload.MENU);
        List<QuickReply> quickReplies = new ArrayList<>();
        quickReplies.add(getAnotherCocktail);
        quickReplies.add(backToMenu);
        return quickReplies;
    }
}
