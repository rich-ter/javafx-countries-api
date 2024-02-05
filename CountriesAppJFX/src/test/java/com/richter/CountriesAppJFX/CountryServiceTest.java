package com.richter.CountriesAppJFX;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CountryServiceTest {

    private final CountryService service = new CountryService();

    @Test
    void testGetAllCountries() throws Exception {
        Country[] countries = service.getAllCountries();
        assertNotNull(countries, "Countries should not be null");
        assertTrue(countries.length > 0, "Countries array should not be empty");
    }

    @Test
    void testGetCountryByName() throws Exception {
        // Use a well-known country to ensure the API response consistency
        Country country = service.getCountryByName("Greece");
        assertNotNull(country, "Country should not be null");
        assertEquals("Greece", country.getName().getCommon(), "Country name should match the query");
    }

    @Test
    void testGetCountriesByLanguage() throws Exception {
        Country[] countries = service.getCountriesByLanguage("Greek");
        assertNotNull(countries, "Countries should not be null");
        assertTrue(countries.length > 0, "Countries array should not be empty for Greek language");
    }

    @Test
    void testGetCountriesByCurrency() throws Exception {
        Country[] countries = service.getCountriesByCurrency("EUR");
        assertNotNull(countries, "Countries should not be null");
        assertTrue(countries.length > 0, "Countries array should not be empty for EUR currency");
    }
}
