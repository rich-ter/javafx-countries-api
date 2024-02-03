package com.richter.CountriesAppJFX;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
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

        // java κλασείς για τα 3 κουμπια με βαση το search
        countrySearchableComboBox = new CountrySearchableComboBox();
        languageSearchableComboBox = new LanguageSearchableComboBox();
        currencySearchableComboBox = new CurrencySearchableComboBox();

        searchHistoryComboBox = new ComboBox<>();
        searchHistoryComboBox.setPromptText("Previous Searches");

        searchButton.setOnAction(event -> performSearch());

        Button resetButton = new Button("Reset");
        resetButton.setOnAction(event -> {
            tableView.getItems().clear();
            countrySearchableComboBox.getSelectionModel().clearSelection();
            languageSearchableComboBox.getSelectionModel().clearSelection();
            currencySearchableComboBox.getSelectionModel().clearSelection();
        });

       
        setupTableView();

        HBox searchInputBox = new HBox( searchButton);
        searchInputBox.setAlignment(Pos.CENTER);
        searchInputBox.setSpacing(10);

        HBox searchBox = new HBox(searchHistoryComboBox, fetchAllButton, countrySearchableComboBox,languageSearchableComboBox,currencySearchableComboBox, searchInputBox, resetButton);
        searchBox.setAlignment(Pos.CENTER);
        searchBox.setSpacing(10);

        VBox vbox = new VBox(searchBox, tableView);
        vbox.setSpacing(20);
        vbox.setPadding(new Insets(20, 0, 20, 0));

        Scene scene = new Scene(vbox, 1200, 900);
        scene.getStylesheets().add(getClass().getResource("/styling.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setTitle("Απαλλακτική Εργασία Java ΠΜΣ Προηγμένα Πληροφοριακά Συστήματα");
        primaryStage.show();

    }
 


    private void performSearch() {

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

        tableView.getColumns().addAll(commonNameCol, officialNameCol, capitalCol, currencyCol, populationCol, continentCol, subregionCol, languagesCol);

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