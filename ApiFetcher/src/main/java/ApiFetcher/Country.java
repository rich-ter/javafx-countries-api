package ApiFetcher;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


// Λέει στην βιλβιοθήκη jackson να αγνοησει πεδία που δεν σχετιζονται με τα πεδια στην κλαση Country 
// βοηθαει στην αποσειροποιηση οταν υπαρχουν εξτρα πεδια.
@JsonIgnoreProperties(ignoreUnknown = true)
public class Country {
	/* Η pojo κλάση μας που επιτρέπει την συλλογή χωρών απο το API
	 * Εφόσων μας ενδιαφέρουν συγκεκριμένα στοιχεία όπως ορίζει η άσκηση
	 * capital, currency, population, continent etc. θα φτιάξουμε μια κλάση μονο με αυτά τα δεδομενα
	 */
    private Name name;
    private Map<String, Currency> currencies;
    private List<String> capital;
    private int population;
    private List<String> continents;
    private String subregion;
    private Map<String, String> languages;

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    /*
     * λόγω της περίεργης δομής των δεδομένων και αρκετά nested response,
     * η δομή του api endpoint γυρναει αποτελεσματα σαν list όποτε
     */

    // καλυτερο απο list 
    // καθως δεν επιτρεπει διπλες εισαγωγες νομισματος 
    public Map<String, Currency> getCurrencies() {
        return currencies;
    }

    // ΑΥΤΟ ΕΔΩ ΤΟ ΒΑΖΟΥΜΕ ΓΙΑΤΙ ΑΝ ΜΙΑ ΧΩΡΑ ΕΧΕΙ
    // ΠΑΝΩ ΑΠΟ ΕΝΑ ΝΟΜΙΣΜΑ ΤΟΤΕ ΘΑ ΚΡΑΣΑΡΕΙ ΤΟ UI
    // ΟΠΟΤΕ ΜΕ ΑΥΤΟΝ ΤΟΝ ΤΡΟΠΟ ΤΑ ΕΝΩΝΟΥΜΕ ΓΙΑ ΝΑ ΤΑ
    // ΠΑΡΟΥΣΙΑΣΟΥΜΕ ΣΤΟ JAVAFX MAVEN PROJECT
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
        return capital != null ? capital : List.of("No capital"); 
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
        return continents != null ? continents : List.of("No continent"); 
    }

    public void setContinents(List<String> continents) {
        this.continents = continents;
    }

    public String getSubregion() {
        return subregion != null ? subregion : "No subregion"; 
    }

    public void setSubregion(String subregion) {
        this.subregion = subregion;
    }

    public Map<String, String> getLanguages() {
        return languages != null ? languages : Map.of(); 
    }

    public void setLanguages(Map<String, String> languages) {
        this.languages = languages;
    }

    // ΞΑΝΑ, ΑΥΤΟ ΕΔΩ ΤΟ ΒΑΖΟΥΜΕ ΓΙΑΤΙ ΜΕΡΙΚΕΣ ΧΩΡΕΣ ΕΧΟΥΝ 
    // ΔΥΟ ΓΛΩΣΣΕΣ ΟΠΟΤΕ ΓΙΑ ΝΑ ΜΗΝ ΚΡΑΣΑΡΕΙ ΤΟ APP
    // ΤΙΣ ΕΝΩΝΟΥΜΕ
    public String getLanguagesAsString() {
        if (languages == null || languages.isEmpty()) {
            return "No languages"; 
        }
        return languages.values().stream().collect(Collectors.joining(", "));
    }


    // συμπεριλαμβάνουμε την override 
    // για να ενημερώσουμε πως χρησιμοποιεί μια 
    // custom υλοποιήση της toString method
    @Override
    public String toString() {
        return "Country{" +
                "name=" + name +
                ", currencies=" + getCurrenciesAsString() +
                ", capital=" + capital +
                ", population=" + population +
                ", continents=" + continents +
                ", subregion='" + subregion + '\'' +
                ", languages=" + getLanguagesAsString() +
                '}';
    }


    public static class Name {
        private String common;
        private String official;
        private Map<String, NativeName> nativeName;

        public String getCommon() {
            return common != null ? common : "No common name"; 
        }

        public void setCommon(String common) {
            this.common = common;
        }

        public String getOfficial() {
            return official != null ? official : "No official name"; 
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
