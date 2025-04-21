package org.vim.pokeproject.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EvolutionRequest {
    private int pokemonId;
    private String userName;

    public EvolutionRequest(int pokemonId, String userName) {
        this.pokemonId = pokemonId;
        this.userName = userName;
    }
}