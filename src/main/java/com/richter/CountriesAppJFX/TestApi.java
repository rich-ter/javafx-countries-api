package com.richter.CountriesAppJFX;

public class TestApi {
    public static void main(String[] args) {
        testGetCountryByName();
    }
    
    public static void testGetCountryByName() {
        try {
            CountryService service = new CountryService();

            // Assuming Country toString() is overridden to display relevant info
            Country greece = service.getCountryByName("Greece");
            System.out.println(greece);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
