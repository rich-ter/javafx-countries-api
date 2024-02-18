package ApiFetcher;

import java.util.List;
import java.util.Set;


// Η ΚΛΑΣΗ ΠΑΡΑΚΑΤΩ ΕΧΕΙ ΥΛΟΠΟΙΗΘΕΙ ΓΙΑ ΔΙΚΗ ΜΑΣ ΔΙΕΥΚΟΛΥΝΣΗ ΚΑΙ ΔΕΝ ΑΠΟΤΕΛΕΙ 
// ΜΕΡΟΣ ΤΗΣ ΕΡΓΑΣΙΑΣ. ΤΗΝ ΕΧΟΥΜΕ ΑΦΗΣΕΙ ΓΙΑ ΕΝΔΕΙΚΤΙΚΟΥΣ ΛΟΓΟΥΣ.
public class TestApi {
    public static void main(String[] args) {
//        testGetCountryByName(); 
        testGetAllCountries(); 
//        testFetchAllCountryNames(); 
//        testFetchAllLanguages();
//        testFetchAllCurrencyNames(); 
    }
    
//    public static void testGetCountryByName() {
//        try {
//            CountryService service = new CountryService();
//            Country greece = service.getCountryByName("United States");
//            System.out.println(greece);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public static void testGetAllCountries() {
        try {
            CountryService service = new CountryService();
            Country[] allCountries = service.getAllCountries();
            System.out.println("Fetched countries:");
            for (Country country : allCountries) {
                System.out.println(country); 
            }
            System.out.println("Total number of countries fetched: " + allCountries.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void testFetchAllCountryNames() {
        try {
            CountryService service = new CountryService();
            List<String> countryNames = service.fetchAllCountryNames();
            System.out.println("All Country Names: " + countryNames);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void testFetchAllLanguages() {
        try {
            CountryService service = new CountryService();
            Set<String> languages = service.fetchAllLanguages();
            System.out.println("All Languages: " + languages);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


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
