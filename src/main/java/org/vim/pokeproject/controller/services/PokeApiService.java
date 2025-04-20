package org.vim.pokeproject.controller.services;

import org.vim.pokeproject.controller.dto.EvolutionRequest;
import org.vim.pokeproject.controller.dto.EvolutionResponse;

public interface PokeApiService {
    EvolutionResponse getEvolutionChain (EvolutionRequest request);
}
