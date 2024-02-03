package com.richter.CountriesAppJFX;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.controlsfx.control.SearchableComboBox;

import java.util.List;

public class CountrySearchableComboBox extends SearchableComboBox<String> {

    public CountrySearchableComboBox() {
        super();
        setEditable(true);
        setPromptText("Select Country by Name");
        fetchAllCountries();
    }

    private void fetchAllCountries() {
        new Thread(() -> {
            try {
                CountryService service = new CountryService();
                List<String> countryNames = service.fetchAllCountryNames();
                ObservableList<String> allCountries = FXCollections.observableArrayList(countryNames);
                Platform.runLater(() -> {
                    setItems(allCountries);
                });
            } catch (Exception e) {
                e.printStackTrace(); 
            }
        }).start();
    }
}
