package org.vim.pokeproject.controller.services.Implemetation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.vim.pokeproject.controller.dto.EvolutionRequest;
import org.vim.pokeproject.controller.dto.EvolutionResponse;
import org.vim.pokeproject.controller.services.PokeApiService;

@Service
public class PokeApiServiceImpl implements PokeApiService {
    private static final String BASE_POKEMON_URL = "https://pokeapi.co/api/v2/pokemon/";
    private static final String BASE_SPECIES_URL = "https://pokeapi.co/api/v2/pokemon-species/";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final CloseableHttpClient httpClient = HttpClients.createDefault();

    public EvolutionResponse getEvolutionChain(EvolutionRequest request){
        JsonNode pokemonNode = fetchJson(BASE_POKEMON_URL + request.getPokemonId() + ".json");
        String pokemonName = pokemonNode.get("name").asText();

        String speciesUrl = pokemonNode.get("species").get("url").asText();
        JsonNode speciesNode = fetchJson(speciesUrl);

        String evolutionChainUrl = speciesNode.get("evolution_chain").get("url").asText();

        JsonNode evolutionChainNode = fetchJson(evolutionChainUrl);
        List<String> evolutionChain = extractEvolutionChain(evolutionChainNode.get("chain"));

        return new EvolutionResponse(request.getPokemonId(), pokemonName, evolutionChain);
    }

    private List<String> extractEvolutionChain(JsonNode chainNode) {
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
