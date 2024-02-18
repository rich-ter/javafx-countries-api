import com.fasterxml.jackson.databind.ObjectMapper;

import ApiFetcher.Country;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DeserializationTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void testCountryDeserialization() throws Exception {
        String json = """
                {
                    "name": {"common": "Greece", "official": "Hellenic Republic"},
                    "currencies": {"EUR": {"name": "Euro", "symbol": "€"}},
                    "capital": ["Athens"],
                    "population": 10724599,
                    "continents": ["Europe"],
                    "subregion": "Southern Europe",
                    "languages": {"gre": "Greek"}
                }
                """;

        Country country = mapper.readValue(json, Country.class);

        assertNotNull(country);
        assertEquals("Greece", country.getName().getCommon());
        assertEquals("Euro", country.getCurrencies().get("EUR").getName());
        assertEquals("€", country.getCurrencies().get("EUR").getSymbol());
        assertEquals("Athens", country.getCapital().get(0));
        assertEquals(10724599, country.getPopulation());
        assertTrue(country.getContinents().contains("Europe"));
        assertEquals("Southern Europe", country.getSubregion());
        assertEquals("Greek", country.getLanguages().get("gre"));
    }
}
