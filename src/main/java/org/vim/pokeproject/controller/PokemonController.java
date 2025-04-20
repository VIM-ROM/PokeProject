package org.vim.pokeproject.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.vim.pokeproject.controller.dto.EvolutionRequest;
import org.vim.pokeproject.controller.dto.EvolutionResponse;
import org.vim.pokeproject.controller.services.PokeApiService;

import java.io.IOException;

@RestController
public class PokemonController {

    private final PokeApiService pokeAPIService;

    public PokemonController(PokeApiService pokeAPIService) {
        this.pokeAPIService = pokeAPIService;
    }

    @GetMapping("/evolutions")
    public ResponseEntity<?> getEvolutions(@RequestBody EvolutionRequest request) {
        EvolutionResponse response = pokeAPIService.getEvolutionChain(request);
        return ResponseEntity.ok(response);
    }
}

