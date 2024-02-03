package com.richter.CountriesAppJFX;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.richter.CountriesAppJFX.Country.Currency;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class CountryService {
    private final HttpClient client;
    private final ObjectMapper mapper;
    private final String BASE_URL = "https://restcountries.com/v3.1";

    public CountryService() {
        this.client = HttpClient.newHttpClient();
        this.mapper = new ObjectMapper();
    }

    public Country[] getAllCountries() throws IOException, InterruptedException {
        return sendRequest(BASE_URL + "/all");
    }


    public List<Country> getCountriesByName(String name, boolean exactMatch) throws IOException, InterruptedException {
        String url = BASE_URL + "/name/" + name + (exactMatch ? "?fullText=true" : "");
        Country[] countries = sendRequest(url);

        // Convert the array of countries to a List
        List<Country> countryList = Arrays.asList(countries);

        return countryList;
    }


    public Country[] getCountriesByLanguage(String language) throws IOException, InterruptedException {
        return sendRequest(
                BASE_URL + "/lang/" + URLEncoder.encode(language, StandardCharsets.UTF_8));
    }

    public Country[] getCountriesByCurrency(String currency) throws IOException, InterruptedException {
        return sendRequest(
                BASE_URL + "/currency/" + URLEncoder.encode(currency, StandardCharsets.UTF_8));
    }

    private Country[] sendRequest(String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(response.body(), Country[].class);
    }

    public List<String> fetchAllCountryNames() throws IOException, InterruptedException {
        Country[] countries = getAllCountries();
        return Arrays.stream(countries)
                     .map(country -> country.getName().getCommon())
                     .collect(Collectors.toList());
    }

    public Set<String> fetchAllLanguages() throws IOException, InterruptedException {
        Country[] countries = getAllCountries();
        return Arrays.stream(countries)
                     .flatMap(country -> country.getLanguages().values().stream())
                     .collect(Collectors.toSet());
    }

    public Set<String> fetchAllCurrencyNames() throws IOException, InterruptedException {
        Country[] allCountries = getAllCountries();
        return Arrays.stream(allCountries)
                     .map(Country::getCurrencies)
                     .filter(Objects::nonNull)
                     .flatMap(map -> map.values().stream())
                     .map(Currency::getName)
                     .collect(Collectors.toSet());
    }
}
