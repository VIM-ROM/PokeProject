package org.vim.pokeproject.controller.services.Implemetation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.vim.pokeproject.controller.dto.EvolutionRequest;
import org.vim.pokeproject.controller.dto.EvolutionResponse;
import org.vim.pokeproject.controller.services.PokeApiService;
import org.vim.pokeproject.model.entity.Access;
import org.vim.pokeproject.model.entity.Pokemon;
import org.vim.pokeproject.model.entity.Usuario;
import org.vim.pokeproject.model.repository.AccessRepository;
import org.vim.pokeproject.model.repository.PokemonRepository;
import org.vim.pokeproject.model.repository.UsuarioRepository;

@Service
@Slf4j
public class PokeApiServiceImpl implements PokeApiService {
    private static final String BASE_POKEMON_URL = "https://pokeapi.co/api/v2/pokemon/";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final CloseableHttpClient httpClient = HttpClients.createDefault();
    private final AccessRepository accessRepository;
    private final PokemonRepository pokemonRepository;
    private final UsuarioRepository usuarioRepository;

    public PokeApiServiceImpl(AccessRepository accessRepository, PokemonRepository pokemonRepository, UsuarioRepository usuarioRepository) {
        this.accessRepository = accessRepository;
        this.pokemonRepository = pokemonRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public EvolutionResponse getEvolutionChain(EvolutionRequest request){
        JsonNode pokemonNode = fetchJson(BASE_POKEMON_URL + request.getPokemonId());
        String pokemonName = pokemonNode.get("name").asText();

        String speciesUrl = pokemonNode.get("species").get("url").asText();
        JsonNode speciesNode = fetchJson(speciesUrl);

        int height = pokemonNode.get("height").asInt();
        int weight = pokemonNode.get("weight").asInt();
        int baseExperience = pokemonNode.get("base_experience").asInt();
        log.info("pokemon: {} altura {} peso {} experiencia {}",pokemonName,height,weight,baseExperience);
        log.info("guardando pokemon: {}",pokemonName);
        Pokemon pokemon = saveDataPokemon(height, baseExperience, weight, pokemonName);
        Usuario usuario = saveUsuario(request.getUserName());
        saveDataAcess(Instant.now(), "", pokemon, usuario);

        String evolutionChainUrl = speciesNode.get("evolution_chain").get("url").asText();

        JsonNode evolutionChainNode = fetchJson(evolutionChainUrl);
        List<String> evolutionChain = extractEvolutionChain(evolutionChainNode.get("chain"));
        log.debug("cadena de evolucion: {}",evolutionChain);
        log.info("Regresando respuesta");
        return new EvolutionResponse(request.getPokemonId(), pokemonName, evolutionChain);
    }

    private Pokemon saveDataPokemon(int height, int baseExperince, int weight, String name){
        Pokemon existingPokemon = pokemonRepository.findByName(name);

        if (existingPokemon!=null) {
            return existingPokemon;
        }

        Pokemon pokemon = new Pokemon();
        pokemon.setName(name);
        pokemon.setHeight(height);
        pokemon.setWeight(weight);
        pokemon.setBaseExperience(baseExperince);

        pokemonRepository.save(pokemon);

        return pokemonRepository.findByName(name);
    }

    private Usuario saveUsuario(String name){
        Usuario existingUsuario = usuarioRepository.findByName(name);
        if (existingUsuario!=null) {
            return existingUsuario;
        }

        Usuario usuario = new Usuario();
        usuario.setName(name);

        usuarioRepository.save(usuario);

        return usuarioRepository.findByName(name);
    }


    private void saveDataAcess(Instant accessed_at, String client_ip, Pokemon idpokemon, Usuario idname){
        log.info("Guardando datos de acceso");
        Access access = new Access();
        access.setAccessedAt(accessed_at);
        access.setClientIp(client_ip);
        access.setPokemon(idpokemon);
        access.setUsuario(idname);

        accessRepository.save(access);
    }

    private List<String> extractEvolutionChain(JsonNode chainNode) {
        log.info("Obteniendo cadena de evolucion");
        List<String> names = new ArrayList<>();
        names.add(chainNode.get("species").get("name").asText());
        JsonNode evolvesTo = chainNode.get("evolves_to");
        if (evolvesTo.isArray()) {
            for (JsonNode evolution : evolvesTo) {
                names.addAll(extractEvolutionChain(evolution));
            }
        }
        return names;
    }

    private JsonNode fetchJson(String url){
        log.info("Enviando peticion a api de pokemon");
        HttpGet request = new HttpGet(url);
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            return objectMapper.readTree(response.getEntity().getContent());
        } catch (ClientProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
