package com.richter.CountriesAppJFX;

public class TestApi {
    public static void main(String[] args) {
        testGetCountryByName(); // Existing call to test fetching a single country
//        testGetAllCountries(); // New call to test fetching all countries
    }
    
    public static void testGetCountryByName() {
        try {
            CountryService service = new CountryService();
            // Assuming Country.toString() is overridden to display relevant info
            Country greece = service.getCountryByName("Greece");
            System.out.println(greece);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // New method to test fetching all countries
    public static void testGetAllCountries() {
        try {
            CountryService service = new CountryService();
            Country[] allCountries = service.getAllCountries(); // Fetch all countries
            
            // Print all countries
            for (Country country : allCountries) {
                System.out.println(country);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
