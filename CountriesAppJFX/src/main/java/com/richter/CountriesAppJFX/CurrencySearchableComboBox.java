package com.richter.CountriesAppJFX;

import ApiFetcher.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.controlsfx.control.SearchableComboBox;

import java.util.Set;

public class CurrencySearchableComboBox extends SearchableComboBox<String> {

    public CurrencySearchableComboBox() {
        super();
        setEditable(true);
        setPromptText("Select Currency");
        fetchAllCurrencies();
    }

    private void fetchAllCurrencies() {
        new Thread(() -> {
            try {
                CountryService service = new CountryService();
                Set<String> currencyNames = service.fetchAllCurrencyNames();
                ObservableList<String> allCurrencies = FXCollections.observableArrayList(currencyNames);
                Platform.runLater(() -> {
                    setItems(allCurrencies);
                });
            } catch (Exception e) {
                e.printStackTrace(); 
            }
        }).start();
    }
}
