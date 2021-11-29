package com.blackou.Cocktailschatbot.model.incomming;

import com.blackou.Cocktailschatbot.entity.Cocktail;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CocktailResponseDto {
    @JsonProperty("drinks")
    private List<Cocktail> cocktailList;
}
