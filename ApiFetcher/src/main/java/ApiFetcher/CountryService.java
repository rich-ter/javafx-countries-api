package ApiFetcher;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class CountryService {
    private final HttpClient client;
    private final ObjectMapper mapper;
    // Μία ερώτηση που έχω είναι αν αλλάξω το url σε ενα endpoint
    // το οποιο γυρναει μονο τα αποτελεσματα που με ενδιαφερουν 
    // το οποίο είναι: 
    //https://restcountries.com/v3.1/name/country_name?fields=name,currencies,capital,population,continents
    //θα είχε σημαντικη διαφορα 
    // sto runtime και performance της εφαρμογης; θα ήθελα να το συζητησω στην θεωρητική εξέταση
    private final String BASE_URL = "https://restcountries.com/v3.1";

    // Αρχικοποιεί την country service με έναν HTTP client και το jackson object mapper 
    // το ένα για την σύνδεση με το api και το αλλο για την αποσειριοποιηση 
    // θα το χρησιμοποιήσουμε στο app.java για την ανάκτηση δεδομένων 
    public CountryService() {
        this.client = HttpClient.newHttpClient();
        this.mapper = new ObjectMapper();
    }

    // Στέλνει ενα http request με παραμετρο ένα url 
    // και μετα κανει deserialize τα δεδομένα που παιρνει απο αυτο το url
    // και τα βαζει στην κλαση Country pojo 
    private Country[] sendRequest(String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(response.body(), Country[].class);
    }
    
    // καλεί το url με το endpoint all για όλες τις χώρες 
    // καλεί την sendRequest για να περάσει τα δεδομένα στην pojo.
    public Country[] getAllCountries() throws IOException, InterruptedException {
        return sendRequest(BASE_URL + "/all");
    }

    // το ίδιο για το όνομα
    public List<Country> getCountriesByName(String name, boolean exactMatch) throws IOException, InterruptedException {
        String url = BASE_URL + "/name/" + name + (exactMatch ? "?fullText=true" : "");
        Country[] countries = sendRequest(url);
        List<Country> countryList = Arrays.asList(countries);
        return countryList;
    }

    // το ίδιο για τη γλωσσα
    public List<Country> getCountriesByLanguage(String language, boolean exactMatch) throws IOException, InterruptedException {
        String url = BASE_URL + "/lang/" + language + (exactMatch ? "?fullText=true" : "");
        Country[] countries = sendRequest(url);

        List<Country> countryList = Arrays.asList(countries);

        return countryList;
    }

	// το ίδιο για το νόμισμα
    public Country[] getCountriesByCurrency(String currency, boolean exactMatch) throws IOException, InterruptedException {
        String url = BASE_URL + "/currency/" + currency.toLowerCase();
        return sendRequest(url);
    }

    // Φτιάξαμε μία κλάση για να γυρνάει μόνο τα όνοματα όλων των χωρών ώστε να μπορούμε να 
    // τα παρουσιάσουμε σαν διαθέσιμες επιλογές οταν ένας χρήστης ψάχνει μια χώρα για ένα όνομα
    public List<String> fetchAllCountryNames() throws IOException, InterruptedException {
    	//καλούμε την μέθοδο getAllCountries για να πάρουμε τις πληροφορίες για όλες τις χώρες
        Country[] countries = getAllCountries();
        // γυρνάμε μία λίστα απο ονόματα τα οποία τραβήξαμε μέσω της getName.getCommon 
        return Arrays.stream(countries)
                     .map(country -> country.getName().getCommon())
                     .collect(Collectors.toList());
    }

    // Το ίδιο για τις γλώσσες
    public Set<String> fetchAllLanguages() throws IOException, InterruptedException {
        Country[] countries = getAllCountries();
        return Arrays.stream(countries)
                     .flatMap(country -> country.getLanguages().values().stream())
                     //
                     .collect(Collectors.toSet());
    }

    // Το ίδιο για τα συναλλάγματα 
    public Set<String> fetchAllCurrencyNames() throws IOException, InterruptedException {
        Country[] allCountries = getAllCountries();
        return Arrays.stream(allCountries)
                     .map(Country::getCurrencies)
                     .filter(Objects::nonNull)
                     .flatMap(map -> map.values().stream())
                     .map(Country.Currency::getName)
                     .collect(Collectors.toSet());
    }
}