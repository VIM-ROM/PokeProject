package org.vim.pokeproject.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping("/evolutions")
    public ResponseEntity<?> getEvolutions(@RequestBody EvolutionRequest request) {
        EvolutionResponse response = pokeAPIService.getEvolutionChain(request);
        return ResponseEntity.ok(response);
    }
}

