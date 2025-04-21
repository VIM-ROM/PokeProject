package org.vim.pokeproject.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class EvolutionResponse {
    private int pokemonId;
    private String pokemonName;
    private List<String> evolutionChain;

    public EvolutionResponse(int pokemonId, String pokemonName, List<String> evolutionChain) {
        this.pokemonId = pokemonId;
        this.pokemonName = pokemonName;
        this.evolutionChain = evolutionChain;
    }
}