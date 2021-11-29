package com.blackou.Cocktailschatbot.client;

import com.blackou.Cocktailschatbot.entity.Cocktail;
import com.blackou.Cocktailschatbot.model.incomming.CocktailResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CocktailAPIClient {
    private final static String DEFAULT_URL = "https://www.thecocktaildb.com/api/json/v1/1/";
    private final static String RANDOM_COCKTAIL_URL = "random.php";
    private final static String COCKTAIL_BY_NAME_URL = "search.php?s=";
    private final static String COCKTAIL_BY_FIRST_LETTER_URL = "search.php?f=";
    private final static String COCKTAIL_BY_INGREDIENT_URL = "filter.php?i=";

    private final RestTemplate restTemplate;

    public CocktailResponseDto getRandomCocktail() {
        return restTemplate.getForObject(DEFAULT_URL + RANDOM_COCKTAIL_URL, CocktailResponseDto.class);
    }

    public CocktailResponseDto getCocktailByName(String cocktailName) {
        return restTemplate.getForObject(DEFAULT_URL + COCKTAIL_BY_NAME_URL + cocktailName, CocktailResponseDto.class);
    }

    public Optional<List<Cocktail>> getCocktailByNameOptional(String cocktailName) {
        CocktailResponseDto cocktailResponseDto = restTemplate.getForObject(DEFAULT_URL + COCKTAIL_BY_NAME_URL + cocktailName, CocktailResponseDto.class);
        return Optional.ofNullable(cocktailResponseDto.getCocktailList());
    }

    public Optional<CocktailResponseDto> getCocktailByFirstLetterOptional(String letter) {
        return Optional.ofNullable(restTemplate.getForObject(DEFAULT_URL + COCKTAIL_BY_FIRST_LETTER_URL + letter, CocktailResponseDto.class));
    }

    public Optional<CocktailResponseDto> getCocktailByIngredient(String ingredient) {
        return Optional.ofNullable(restTemplate.getForObject(DEFAULT_URL + COCKTAIL_BY_INGREDIENT_URL + ingredient, CocktailResponseDto.class));
    }
}
