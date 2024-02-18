package ApiFetcher;

import java.util.List;
import java.util.Set;

public class TestApi {
    public static void main(String[] args) {
//        testGetCountryByName(); // Existing call to test fetching a single country
        testGetAllCountries(); // Call to test fetching all countries
//        testFetchAllCountryNames(); // New call to test fetching all country names
//        testFetchAllLanguages(); // New call to test fetching all languages
//        testFetchAllCurrencyNames(); // New call to test fetching all currency names
    }
    
    // Existing method to test fetching a country by name
//    public static void testGetCountryByName() {
//        try {
//            CountryService service = new CountryService();
//            Country greece = service.getCountryByName("United States");
//            System.out.println(greece);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    // Existing method to test fetching all countries
    public static void testGetAllCountries() {
        try {
            CountryService service = new CountryService();
            Country[] allCountries = service.getAllCountries();
            System.out.println("Fetched countries:");
            for (Country country : allCountries) {
                System.out.println(country); // This assumes Country.toString() is overridden to print meaningful info
            }
            // After printing all countries, print the total count
            System.out.println("Total number of countries fetched: " + allCountries.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // New method to test fetching all country names
    public static void testFetchAllCountryNames() {
        try {
            CountryService service = new CountryService();
            List<String> countryNames = service.fetchAllCountryNames();
            System.out.println("All Country Names: " + countryNames);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // New method to test fetching all languages
    public static void testFetchAllLanguages() {
        try {
            CountryService service = new CountryService();
            Set<String> languages = service.fetchAllLanguages();
            System.out.println("All Languages: " + languages);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // New method to test fetching all currency names
    public static void testFetchAllCurrencyNames() {
        try {
            CountryService service = new CountryService();
            Set<String> currencyNames = service.fetchAllCurrencyNames();
            System.out.println("All Currency Names: " + currencyNames);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
