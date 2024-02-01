package com.richter.CountriesAppJFX;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class CountryService {
    private final HttpClient client;
    private final ObjectMapper mapper;

    public CountryService() {
        this.client = HttpClient.newHttpClient();
        this.mapper = new ObjectMapper();
    }

    //kai na ftiaxw to url na einai analogo
    // ISWS NA MIKRINO TO API ENDPOINT NA TRAVAEI MONO AUTA POY ME ENDIAFEROUN GIA NA EINAI PIO GRIGORO?
    public Country[] getAllCountries() throws IOException, InterruptedException {
        return sendRequest("https://restcountries.com/v3.1/all");
    }

    public Country getCountryByName(String name) throws IOException, InterruptedException {
        return sendRequest("https://restcountries.com/v3.1/name/" + URLEncoder.encode(name, StandardCharsets.UTF_8))[0];
    }

    public Country[] getCountriesByLanguage(String language) throws IOException, InterruptedException {
        return sendRequest(
                "https://restcountries.com/v3.1/lang/" + URLEncoder.encode(language, StandardCharsets.UTF_8));
    }

    public Country[] getCountriesByCurrency(String currency) throws IOException, InterruptedException {
        return sendRequest(
                "https://restcountries.com/v3.1/currency/" + URLEncoder.encode(currency, StandardCharsets.UTF_8));
    }

    private Country[] sendRequest(String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(response.body(), Country[].class);
    }
}
