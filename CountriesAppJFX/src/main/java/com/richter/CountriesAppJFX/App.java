package com.richter.CountriesAppJFX;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import ApiFetcher.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.control.Alert;

public class App extends Application {

    private TableView<Country> tableView = new TableView<>();// Table view για τα data μας
    private ComboBox<String> searchHistoryComboBox; // Προηγούμενες αναζητήσεις
    private Button searchButton = new Button("Search");
    private Button fetchAllButton; 
    private CountrySearchableComboBox countrySearchableComboBox;
    private LanguageSearchableComboBox languageSearchableComboBox;
    private CurrencySearchableComboBox currencySearchableComboBox;
    
    @Override
    public void start(Stage primaryStage) {
        fetchAllButton = new Button("Search All Countries");
        fetchAllButton.setOnAction(event -> fetchAllCountries());
        
    
        Label countrySearchLabel = new Label("Search by Country Name:");
        // αρχικοποίηση των Combo Box απο την βιβλιοθήκη controls.fx 
        countrySearchableComboBox = new CountrySearchableComboBox();
        
        Label languageSearchLabel = new Label("Search by a Language:");
        languageSearchableComboBox = new LanguageSearchableComboBox();
       
        Label currencySearchLabel = new Label("Search by a Currency:");
        currencySearchableComboBox = new CurrencySearchableComboBox();

        // αρχικοποίηση του κουμπίου για το ιστορικό αναζητήσεων
        searchHistoryComboBox = new ComboBox<>();
        searchHistoryComboBox.setPromptText("Previous Searches");
        
        searchHistoryComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                executeSearchFromHistory(newSelection);
            }
        });
        
        searchButton.setOnAction(event -> performSearch());

        // εκκαθάριση επιλογών
        Button resetButton = new Button("Reset");
        resetButton.setOnAction(event -> {
            tableView.getItems().clear();
            countrySearchableComboBox.getSelectionModel().clearSelection();
            languageSearchableComboBox.getSelectionModel().clearSelection();
            currencySearchableComboBox.getSelectionModel().clearSelection();
        });

       
        VBox countrySearchBox = new VBox(countrySearchLabel, countrySearchableComboBox);
        VBox languageSearchBox = new VBox(languageSearchLabel, languageSearchableComboBox);
        VBox currencySearchBox = new VBox(currencySearchLabel, currencySearchableComboBox);
        
        setupTableView();

        HBox searchInputBox = new HBox( searchButton);
        searchInputBox.setAlignment(Pos.CENTER);
        searchInputBox.setSpacing(10);

        HBox searchBox = new HBox(searchHistoryComboBox, fetchAllButton, countrySearchBox,languageSearchBox,currencySearchBox, searchInputBox, resetButton);
        searchBox.setAlignment(Pos.CENTER);
        searchBox.setSpacing(10);

        VBox vbox = new VBox(searchBox, tableView);
        vbox.setSpacing(20);
        vbox.setPadding(new Insets(20, 0, 20, 0));

        Scene scene = new Scene(vbox, 1200, 900);
        scene.getStylesheets().add(getClass().getResource("/styling.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setTitle("Απαλλακτική Εργασία Java ΠΜΣ Προηγμένα Πληροφοριακά Συστήματα");
        Image icon = new Image("UNIPI.jpg");
        primaryStage.getIcons().add(icon);
        primaryStage.show();

    }
 
    private void executeSearchFromHistory(String historyEntry) {
        if (historyEntry.startsWith("By Country Name:")) {
            String searchTerm = historyEntry.replace("By Country Name:", "").trim();
            fetchCountryData(searchTerm, true);
        } else if (historyEntry.startsWith("By Language:")) {
            String searchTerm = historyEntry.replace("By Language:", "").trim();
            fetchCountriesByLanguage(searchTerm, true);
        } else if (historyEntry.startsWith("By Currency:")) {
            String searchTerm = historyEntry.replace("By Currency:", "").trim();
            fetchCountriesByCurrency(searchTerm, false);
        } else if (historyEntry.equals("Fetch All Countries")) {
            fetchAllCountries();
        }
    }
    
    private void performSearch() {

        if (!countrySearchableComboBox.getSelectionModel().isEmpty()) {
            performCountrySearch();
        } else if (!languageSearchableComboBox.getSelectionModel().isEmpty()) {
            performLanguageSearch();
        } else if (!currencySearchableComboBox.getSelectionModel().isEmpty()) {
            performCurrencySearch();
        }
        
    	String selectedCountry = countrySearchableComboBox.getValue();
        if (isValidSearchTerm(selectedCountry)) {
            fetchCountryData(selectedCountry, true);
            updateSearchHistory("By Country Name", selectedCountry);
            return; 
        }
        
        String selectedLanguage = languageSearchableComboBox.getValue();
        if (isValidSearchTerm(selectedLanguage)) {
            fetchCountriesByLanguage(selectedLanguage, true);
            updateSearchHistory("By Language", selectedLanguage);
            return; 
        }
        
        String selectedCurrency = currencySearchableComboBox.getValue();
        if (isValidSearchTerm(selectedCurrency)) {
            fetchCountriesByCurrency(selectedCurrency, false);
            updateSearchHistory("By Currency", selectedCurrency);
            return; 
        }
    }

    private void performCountrySearch() {
        if (!languageSearchableComboBox.getSelectionModel().isEmpty() || !currencySearchableComboBox.getSelectionModel().isEmpty()) {
            showAlert("Please clear your previous search before searching by country name.");
        } else {
            String selectedCountry = countrySearchableComboBox.getValue();
            if (isValidSearchTerm(selectedCountry)) {
                fetchCountryData(selectedCountry, true);
                updateSearchHistory("By Country Name", selectedCountry);
            }
        }
    }

    private void performLanguageSearch() {
        if (!countrySearchableComboBox.getSelectionModel().isEmpty() || !currencySearchableComboBox.getSelectionModel().isEmpty()) {
            showAlert("Please clear your previous search before searching by language.");
        } else {
            String selectedLanguage = languageSearchableComboBox.getValue();
            if (isValidSearchTerm(selectedLanguage)) {
                fetchCountriesByLanguage(selectedLanguage, true);
                updateSearchHistory("By Language", selectedLanguage);
            }
        }
    }

    private void performCurrencySearch() {
        if (!countrySearchableComboBox.getSelectionModel().isEmpty() || !languageSearchableComboBox.getSelectionModel().isEmpty()) {
            showAlert("Please clear your previous search before searching by currency.");
        } else {
            String selectedCurrency = currencySearchableComboBox.getValue();
            if (isValidSearchTerm(selectedCurrency)) {
                fetchCountriesByCurrency(selectedCurrency, false);
                updateSearchHistory("By Currency", selectedCurrency);
            }
        }
    }
    
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Search Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean isValidSearchTerm(String searchTerm) {
        return searchTerm != null && !searchTerm.trim().isEmpty();
    }

    private void setupTableView() {

    	TableColumn<Country, String> commonNameCol = new TableColumn<>("Common Name");
        commonNameCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getName().getCommon()));
        commonNameCol.setPrefWidth(200); // Set preferred width

        TableColumn<Country, String> officialNameCol = new TableColumn<>("Official Name");
        officialNameCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getName().getOfficial()));
        officialNameCol.setPrefWidth(200); // Set preferred width

        TableColumn<Country, String> capitalCol = new TableColumn<>("Capital");
        capitalCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCapital().isEmpty() ? "" : cellData.getValue().getCapital().get(0)));
        capitalCol.setPrefWidth(150);

        TableColumn<Country, String> currencyCol = new TableColumn<>("Currency");
        currencyCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCurrenciesAsString()));
        currencyCol.setPrefWidth(150);

        TableColumn<Country, Number> populationCol = new TableColumn<>("Population");
        populationCol.setCellValueFactory(new PropertyValueFactory<>("population"));
        populationCol.setPrefWidth(100);

        TableColumn<Country, String> continentCol = new TableColumn<>("Continent");
        continentCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getContinents().isEmpty() ? "" : cellData.getValue().getContinents().get(0)));
        continentCol.setPrefWidth(100);

        TableColumn<Country, String> subregionCol = new TableColumn<>("Subregion");
        subregionCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getSubregion()));
        subregionCol.setPrefWidth(120);

        TableColumn<Country, String> languagesCol = new TableColumn<>("Languages");
        languagesCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getLanguagesAsString()));
        languagesCol.setPrefWidth(150);
        
        tableView.getColumns().add(commonNameCol);
        tableView.getColumns().add(officialNameCol);
        tableView.getColumns().add(capitalCol);
        tableView.getColumns().add(currencyCol);
        tableView.getColumns().add(populationCol);
        tableView.getColumns().add(continentCol);
        tableView.getColumns().add(subregionCol);
        tableView.getColumns().add(languagesCol);
        tableView.setPrefHeight(900); 
    }

    private void fetchCountriesByLanguage(String language, boolean exactMatch) {
        new Thread(() -> {
            try {
                final String finalLanguage = language.trim();

                if (finalLanguage.isEmpty()) {
                    return;
                }

                String adjustedLanguage = finalLanguage.replace(" ", "%20");

                CountryService service = new CountryService();

                List<Country> countries = service.getCountriesByLanguage(adjustedLanguage, exactMatch);

                Platform.runLater(() -> {
                    tableView.getItems().clear();
                    tableView.getItems().addAll(countries);
                });
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void fetchCountriesByCurrency(String currency, boolean exactMatch) {
        new Thread(() -> {
            try {
                final String finalCurrency = currency.trim();
                if (finalCurrency.isEmpty()) {
                    return;
                }

                String adjustedCurrency = finalCurrency.replace(" ", "%20");
                CountryService service = new CountryService();
                Country[] countries = service.getCountriesByCurrency(adjustedCurrency, exactMatch);
                
                Platform.runLater(() -> {
                    tableView.getItems().clear();
                    tableView.getItems().addAll(Arrays.asList(countries));
                });
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
               
            }
        }).start();
    }


    private void fetchAllCountries() {
        new Thread(() -> {
            try {
                CountryService service = new CountryService();
                Country[] allCountries = service.getAllCountries();

                Platform.runLater(() -> {
                    tableView.getItems().clear(); 
                    tableView.getItems().addAll(Arrays.asList(allCountries)); 
                    tableView.refresh(); 
                    updateSearchHistory("Fetch All Countries", null); 

                });
            } catch (IOException | InterruptedException e) {
                e.printStackTrace(); 
                Platform.runLater(() -> {
                });
            }
        }).start();
    }

    private void fetchCountryData(String countryName, boolean exactMatch) {
        new Thread(() -> {
            try {
                final String finalCountryName = countryName.trim();

                if (finalCountryName.isEmpty()) {
                    return;
                }

                String adjustedCountryName = finalCountryName.replace(" ", "%20");

                CountryService service = new CountryService();

                List<Country> countries = service.getCountriesByName(adjustedCountryName, exactMatch);

                Platform.runLater(() -> {
                    tableView.getItems().clear();
                    tableView.getItems().addAll(countries);
                });
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                // Handle errors appropriately
            }
        }).start();
    }

    private void updateSearchHistory(String criteria, String term) {
        String searchEntry = (term == null || term.isEmpty()) ? criteria : criteria + ": " + term;
        if (!searchHistoryComboBox.getItems().contains(searchEntry)) {
            searchHistoryComboBox.getItems().add(0, searchEntry);

            while (searchHistoryComboBox.getItems().size() > 5) {
                searchHistoryComboBox.getItems().remove(5);
            }
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}