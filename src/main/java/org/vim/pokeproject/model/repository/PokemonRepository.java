package org.vim.pokeproject.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.vim.pokeproject.model.entity.Pokemon;

@Repository
public interface PokemonRepository extends JpaRepository <Pokemon, Integer>{
    Pokemon findByName(String name);
}