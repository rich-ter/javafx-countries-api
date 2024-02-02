package com.richter.CountriesAppJFX;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//import jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/* Η pojo κλάση μας που επιτρέπει την συλλογή χωρών απο το API
 * Εφόσων μας ενδιαφέρουν συγκεκριμένα στοιχεία όπως ορίζει η άσκηση
 * capital, currency, population, continent etc. θα φτιάξουμε μια κλάση μονο με αυτά τα δεδομενα 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Country {
    private Name name;
    private Map<String, Currency> currencies;
    private List<String> capital;
    private int population;
    private List<String> continents;

    public Name getName() {
        return name;
    }

    // giati einai void edw?//
    public void setName(Name name) {
        this.name = name;
    }

    /*
     * λόγω της περίεργης δομής των δεδομένων και αρκετά nested response,
     * η δομή του api endpoint γυρναει αποτελεσματα σαν list όποτε
     */

    // giati einai map edw kai oxi string?//
    public Map<String, Currency> getCurrencies() {
        return currencies;
    }

// ΑΥΤΟ ΕΔΩ ΤΟ ΒΑΖΟΥΜΕ ΓΙΑΤΙ ΑΝ ΜΙΑ ΧΩΡΑ ΕΧΕΙ 
// ΠΑΝΩ ΑΠΟ ΕΝΑ ΝΟΜΙΣΜΑ ΤΟΤΕ ΘΑ ΚΡΑΣΑΡΕΙ ΤΟ UI
// ΟΠΟΤΕ ΜΕ ΑΥΤΟΝ ΤΟΝ ΤΡΟΠΟ ΤΑ ΕΝΩΝΟΥΜΕ ΓΙΑ ΝΑ ΤΑ 
    // ΠΑΡΟΥΣΙΑΣΟΥΜΕ
    public String getCurrenciesAsString() {
        if (currencies == null) {
            return "No currencies"; // Or any other default value you see fit
        }
        return currencies.entrySet().stream()
                .map(entry -> entry.getValue().getName() + " (" + entry.getValue().getSymbol() + ")")
                .collect(Collectors.joining(", "));
    }
    
    public void setCurrencies(Map<String, Currency> currencies) {
        this.currencies = currencies;
    }

    public List<String> getCapital() {
        return capital != null ? capital : List.of("No capital"); // Default value for null or empty capital
    }

    public void setCapital(List<String> capital) {
        this.capital = capital;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public List<String> getContinents() {
        return continents != null ? continents : List.of("No continent"); // Default value for null or empty continents
    }

    public void setContinents(List<String> continents) {
        this.continents = continents;
    }

    // ti einai auti i override?//

    @Override
    public String toString() {
        return "Country{" +
                "name=" + name +
                ", currencies=" + currencies +
                ", capital=" + capital +
                ", population=" + population +
                ", continents=" + continents +
                '}';
    }

    public static class Name {
        private String common;
        private String official;
        private Map<String, NativeName> nativeName;

        public String getCommon() {
            return common != null ? common : "No common name"; // Default for null common name
        }

        public void setCommon(String common) {
            this.common = common;
        }

        public String getOfficial() {
            return official != null ? official : "No official name"; // Default for null official name
        }

        public void setOfficial(String official) {
            this.official = official;
        }

        public Map<String, NativeName> getNativeName() {
            return nativeName;
        }

        public void setNativeName(Map<String, NativeName> nativeName) {
            this.nativeName = nativeName;
        }

        @Override
        public String toString() {
            return "Name{" +
                    "common='" + common + '\'' +
                    ", official='" + official + '\'' +
                    ", nativeName=" + nativeName +
                    '}';
        }
    }

    public static class NativeName {
        private String official;
        private String common;

        public String getOfficial() {
            return official;
        }

        public void setOfficial(String official) {
            this.official = official;
        }

        public String getCommon() {
            return common;
        }

        public void setCommon(String common) {
            this.common = common;
        }

        @Override
        public String toString() {
            return "NativeName{" +
                    "official='" + official + '\'' +
                    ", common='" + common + '\'' +
                    '}';
        }
    }

    public static class Currency {
        private String name;
        private String symbol;

        // Getters and setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        @Override
        public String toString() {
            return "Currency{" +
                    "name='" + name + '\'' +
                    ", symbol='" + symbol + '\'' +
                    '}';
        }
    }
}
