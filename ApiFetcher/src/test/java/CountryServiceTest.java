
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ApiFetcher.Country;
import ApiFetcher.CountryService;

import java.util.List;
import java.util.Set;

class CountryServiceTest {

    private CountryService service;

    @BeforeEach
    void setUp() {
        service = new CountryService();
    }

    @Test
    void getAllCountriesTest() throws Exception {
        Country[] countries = service.getAllCountries();
        assertNotNull(countries);
        assertTrue(countries.length > 0, "Should return an array of countries");
    }

    @Test
    void getCountriesByNameTest() throws Exception {
        List<Country> countries = service.getCountriesByName("France", true);
        assertNotNull(countries);
        assertFalse(countries.isEmpty(), "Should return a list of countries with the name 'France'");
        assertTrue(countries.stream().anyMatch(country -> "France".equals(country.getName().getCommon())), "List should contain France");
    }

    @Test
    void getCountriesByLanguageTest() throws Exception {
        List<Country> countries = service.getCountriesByLanguage("French", true);
        assertNotNull(countries);
        assertFalse(countries.isEmpty(), "Should return a list of countries where French is spoken");
    }

    @Test
    void getCountriesByCurrencyTest() throws Exception {
        Country[] countries = service.getCountriesByCurrency("EUR", true);
        assertNotNull(countries);
        assertTrue(countries.length > 0, "Should return an array of countries using the EUR currency");
    }

    @Test
    void fetchAllCountryNamesTest() throws Exception {
        List<String> countryNames = service.fetchAllCountryNames();
        assertNotNull(countryNames);
        assertFalse(countryNames.isEmpty(), "Should return a list of country names");
    }

    @Test
    void fetchAllLanguagesTest() throws Exception {
        Set<String> languages = service.fetchAllLanguages();
        assertNotNull(languages);
        assertFalse(languages.isEmpty(), "Should return a set of languages");
    }

    @Test
    void fetchAllCurrencyNamesTest() throws Exception {
        Set<String> currencyNames = service.fetchAllCurrencyNames();
        assertNotNull(currencyNames);
        assertFalse(currencyNames.isEmpty(), "Should return a set of currency names");
    }
}
