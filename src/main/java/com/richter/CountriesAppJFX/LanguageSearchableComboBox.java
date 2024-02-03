package com.richter.CountriesAppJFX;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.controlsfx.control.SearchableComboBox;

import java.util.Set;

public class LanguageSearchableComboBox extends SearchableComboBox<String> {

    public LanguageSearchableComboBox() {
        super();
        setEditable(true);
        setPromptText("Select Language");
        fetchAllLanguages();
    }

    private void fetchAllLanguages() {
        new Thread(() -> {
            try {
                CountryService service = new CountryService();
                Set<String> languageNames = service.fetchAllLanguages();
                ObservableList<String> allLanguages = FXCollections.observableArrayList(languageNames);
                Platform.runLater(() -> {
                    setItems(allLanguages);
                });
            } catch (Exception e) {
                e.printStackTrace(); // Handle exceptions appropriately
            }
        }).start();
    }
}
