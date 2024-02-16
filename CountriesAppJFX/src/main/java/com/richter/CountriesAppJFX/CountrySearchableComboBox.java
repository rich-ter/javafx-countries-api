package com.richter.CountriesAppJFX;

import ApiFetcher.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.controlsfx.control.SearchableComboBox;

import java.util.List;

// Χρησιμοποιήουμε απο την βιβλοθήκη controls.fx την μέθοδο
// SearchableComboBox το οποιό παιίρνει παράμετρο μία λίστα 
// και μας δίνει την δυνατότητα να παρουσιάσουμε την λίστα απο διαθέσιμες 
// χωρές όταν ο χρήστης πατήσει το combo box για να αναζητήσει για το όνομα μίας χώρας
// ακριβώς το ίδιο υλοποιηούμε για το CurrencySearchableComboBox και το LanguageComboBox 
public class CountrySearchableComboBox extends SearchableComboBox<String> {

    public CountrySearchableComboBox() {
        super();
        setEditable(true);
        setPromptText("Select Country by Name");
        fetchAllCountries();
    }

 // Φτιάχνουμε μία μέθοδο fetchAllCountries η οποία καλεί μέσα της 
 // την fetchAllCountryNames() απο την CountryService.java απο το maven project ApiFetcher
    private void fetchAllCountries() {
        new Thread(() -> {
            try {
                CountryService service = new CountryService();
                List<String> countryNames = service.fetchAllCountryNames();
                ObservableList<String> allCountries = FXCollections.observableArrayList(countryNames);
                // μέσω της παρακάτω lamda expression κάνουμε set
                // την λίστα να παίρνει της τιμές του countryNames 
                Platform.runLater(() -> {
                    setItems(allCountries);
                });
            } catch (Exception e) {
                e.printStackTrace(); 
            }
        }).start();
    }
}
