package com.blackou.Cocktailschatbot.builder;

import com.blackou.Cocktailschatbot.constant.PostbackPayload;
import com.blackou.Cocktailschatbot.constant.UrlParameters;
import com.blackou.Cocktailschatbot.entity.Cocktail;
import com.blackou.Cocktailschatbot.entity.CocktailUser;
import com.blackou.Cocktailschatbot.entity.User;
import com.blackou.Cocktailschatbot.repository.CocktailRepo;
import com.blackou.Cocktailschatbot.repository.CocktailUserRepo;
import com.botscrew.messengercdk.model.outgoing.element.TemplateElement;
import com.botscrew.messengercdk.model.outgoing.element.button.Button;
import com.botscrew.messengercdk.model.outgoing.element.button.PostbackButton;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TemplateBuilder {
    private static final String NEW_LINE = "\n";
    private static final String ADD_TO_FAVORITE_MESSAGE = "Add to Favorite";
    private static final String REMOVE_FROM_FAVORITE_MESSAGE = "Remove from Favorite";
    private static final String HOW_TO_MAKE_MESSAGE = "How to make it?";
    private static final String COMMA = ", ";

    private final CocktailUserRepo cocktailUserRepo;
    private final CocktailRepo cocktailRepo;

    public TemplateElement buildCocktailTemplateAdd(Cocktail cocktail) {
        return TemplateElement.builder()
                .title(cocktail.getStrDrink())
                .subtitle(subtitleBuilder(cocktail))
                .imageUrl(cocktail.getStrDrinkThumb())
                .button(new PostbackButton(ADD_TO_FAVORITE_MESSAGE, PostbackPayload.ADD_TO_FAVORITE + UrlParameters.COCKTAIL_NAME_PARAM + cocktail.getStrDrink()))
                .button(new PostbackButton(HOW_TO_MAKE_MESSAGE, PostbackPayload.COCKTAIL_INSTRUCTION + UrlParameters.COCKTAIL_NAME_PARAM + cocktail.getStrDrink()))
                .build();
    }

    public TemplateElement buildCocktailTemplateRemove(Cocktail cocktail) {
        return TemplateElement.builder()
                .title(cocktail.getStrDrink())
                .subtitle(subtitleBuilder(cocktail))
                .imageUrl(cocktail.getStrDrinkThumb())
                .button(new PostbackButton(REMOVE_FROM_FAVORITE_MESSAGE,  PostbackPayload.REMOVE_FROM_FAVORITE + UrlParameters.COCKTAIL_NAME_PARAM + cocktail.getStrDrink()))
                .button(new PostbackButton(HOW_TO_MAKE_MESSAGE, PostbackPayload.COCKTAIL_INSTRUCTION + UrlParameters.COCKTAIL_NAME_PARAM + cocktail.getStrDrink()))
                .build();
    }

    public TemplateElement buildCocktailTemplateWithButtonAdd(Cocktail cocktail, Button button) {
        return TemplateElement.builder()
                .title(cocktail.getStrDrink())
                .subtitle(subtitleBuilder(cocktail))
                .imageUrl(cocktail.getStrDrinkThumb())
                .button(button)
                .button(new PostbackButton(ADD_TO_FAVORITE_MESSAGE, PostbackPayload.ADD_TO_FAVORITE + UrlParameters.COCKTAIL_NAME_PARAM + cocktail.getStrDrink()))
                .button(new PostbackButton(HOW_TO_MAKE_MESSAGE, PostbackPayload.COCKTAIL_INSTRUCTION + UrlParameters.COCKTAIL_NAME_PARAM + cocktail.getStrDrink()))
                .build();
    }

    public TemplateElement buildCocktailTemplateWithButtonRemove(Cocktail cocktail, Button button) {
        return TemplateElement.builder()
                .title(cocktail.getStrDrink())
                .subtitle(subtitleBuilder(cocktail))
                .imageUrl(cocktail.getStrDrinkThumb())
                .button(button)
                .button(new PostbackButton(REMOVE_FROM_FAVORITE_MESSAGE,  PostbackPayload.REMOVE_FROM_FAVORITE + UrlParameters.COCKTAIL_NAME_PARAM + cocktail.getStrDrink()))
                .button(new PostbackButton(HOW_TO_MAKE_MESSAGE, PostbackPayload.COCKTAIL_INSTRUCTION + UrlParameters.COCKTAIL_NAME_PARAM + cocktail.getStrDrink()))
                .build();
    }

    public TemplateElement checkIfCocktailInFavoriteWithButton(User user, Cocktail cocktail, Button button){
        Cocktail cocktailFromDb = cocktailRepo.findByStrDrink(cocktail.getStrDrink());
        Optional<CocktailUser> cocktailUserOpt = cocktailUserRepo.findByUser(user);
        if(cocktailUserOpt.isPresent() && cocktailUserOpt.get().getCocktails().contains(cocktailFromDb)){
            return buildCocktailTemplateWithButtonRemove(cocktail,button);
        }else{
            return buildCocktailTemplateWithButtonAdd(cocktail,button);
        }
    }

    public TemplateElement checkIfCocktailInFavorite(User user, Cocktail cocktail){
        Cocktail cocktailFromDb = cocktailRepo.findByStrDrink(cocktail.getStrDrink());
        Optional<CocktailUser> cocktailUserOpt = cocktailUserRepo.findByUser(user);
        if(cocktailUserOpt.isPresent() && cocktailUserOpt.get().getCocktails().contains(cocktailFromDb)){
            return buildCocktailTemplateRemove(cocktail);
        }else{
            return buildCocktailTemplateAdd(cocktail);
        }
    }

    private String subtitleBuilder(Cocktail cocktail) {
        return cocktail.getStrAlcoholic() +
                NEW_LINE +
                cocktail
                        .getAllIngredients()
                        .stream()
                        .filter(Objects::nonNull)
                        .collect(Collectors.joining(COMMA));
    }
}
