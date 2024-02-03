package com.richter.CountriesAppJFX;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

public class App extends Application {

    private TableView<Country> tableView = new TableView<>();
    private ComboBox<String> searchCriteriaComboBox;
    private ComboBox<String> searchHistoryComboBox;
    private ComboBox<String> countrySearchComboBox = new ComboBox<>();
    private Button searchButton = new Button("Search");
    private Button fetchAllButton; // Button for fetching all countries
    
   
    @Override
    public void start(Stage primaryStage) {
        fetchAllButton = new Button("Search All Countries");
        fetchAllButton.setOnAction(event -> fetchAllCountries());

        searchCriteriaComboBox = new ComboBox<>();
        searchCriteriaComboBox.getItems().addAll("By Country Name", "By Language", "By Currency");
        searchCriteriaComboBox.setPromptText("Select Search Criteria");

        setupCountrySearchComboBox();

        searchHistoryComboBox = new ComboBox<>();
        searchHistoryComboBox.setPromptText("Previous Searches");

        searchButton.setOnAction(event -> performSearch());

        Button resetButton = new Button("Reset");
        resetButton.setOnAction(event -> {
            tableView.getItems().clear();
            searchCriteriaComboBox.getSelectionModel().clearSelection();
            countrySearchComboBox.getSelectionModel().clearSelection();
            countrySearchComboBox.getEditor().clear();
        });

        setupTableView();

        HBox searchInputBox = new HBox(countrySearchComboBox, searchButton);
        searchInputBox.setAlignment(Pos.CENTER);
        searchInputBox.setSpacing(10);

        HBox searchBox = new HBox(searchHistoryComboBox, fetchAllButton, searchCriteriaComboBox, searchInputBox, resetButton);
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
    
    private void setupCountrySearchComboBox() {
        // Initialize the ComboBox with all country names
        new Thread(() -> {
            try {
                CountryService service = new CountryService();
                List<String> allCountryNames = service.fetchAllCountryNames(); // Fetch country names

                Platform.runLater(() -> {
                    countrySearchComboBox.setEditable(true); // Enable free text input
                    countrySearchComboBox.getItems().addAll(allCountryNames);

                    // Listener to filter the list based on user input
                    countrySearchComboBox.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
                        // Check if the space key is pressed and return early
                        if (newValue.contains(" ")) {
                            return;
                        }
                      
                        final String filter = newValue.toLowerCase().replace(" ", ""); // Remove spaces
                        List<String> filteredItems = allCountryNames.stream()
                                .filter(s -> s.toLowerCase().contains(filter))
                                .collect(Collectors.toList());
                        countrySearchComboBox.getItems().clear();
                        countrySearchComboBox.getItems().addAll(filteredItems);
                        countrySearchComboBox.show(); // Show the dropdown list
                    });

                    // Listener to handle selections properly
                    countrySearchComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                        if (newVal != null) {
                            // Perform the search when the user selects an option
                            performSearch();
                        }
                    });
                    
                    // Show the dropdown list when the user clicks on the ComboBox
                    countrySearchComboBox.setOnMouseClicked(event -> countrySearchComboBox.show());
                });
            } catch (Exception e) {
                e.printStackTrace(); // Handle exceptions appropriately
            }
        }).start();
    }







   
    private void performSearch() {
        String selectedCriteria = searchCriteriaComboBox.getValue();
        String searchTerm = countrySearchComboBox.getEditor().getText().trim(); // Trim whitespace

        if (searchTerm.isEmpty()) {
            // Handle the case where the input is empty or consists only of whitespace
            // You can display a message or simply return without sending the API request
            return;
        }

        if ("Fetch All Countries".equals(selectedCriteria)) {
            fetchAllCountries();
        } else if (selectedCriteria != null && !selectedCriteria.trim().isEmpty()) {
            updateSearchHistory(selectedCriteria, searchTerm);
            tableView.getItems().clear(); // Clear table for new search
            switch (selectedCriteria) {
                case "By Country Name":
                    fetchCountryData(searchTerm, true); // Pass true for exact match
                    break;
                case "By Language":
                    fetchCountriesByLanguage(searchTerm, true);
                    break;
                case "By Currency":
                    fetchCountriesByCurrency(searchTerm, true);
                    break;
                default:
                    break;
            }
        }
        
        // You don't need to filter the results here for exact match since it will be handled in the fetchCountryData method.
    }

    
    
    private void setupTableView() {
        // Common Name Column
        TableColumn<Country, String> commonNameCol = new TableColumn<>("Common Name");
        commonNameCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getName().getCommon()));
        commonNameCol.setPrefWidth(200); // Set preferred width

        // Official Name Column
        TableColumn<Country, String> officialNameCol = new TableColumn<>("Official Name");
        officialNameCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getName().getOfficial()));
        officialNameCol.setPrefWidth(200); // Set preferred width

        // Capital Column
        TableColumn<Country, String> capitalCol = new TableColumn<>("Capital");
        capitalCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCapital().isEmpty() ? "" : cellData.getValue().getCapital().get(0)));
        capitalCol.setPrefWidth(150);

        // Currency Column
        TableColumn<Country, String> currencyCol = new TableColumn<>("Currency");
        currencyCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCurrenciesAsString()));
        currencyCol.setPrefWidth(150);

        // Population Column
        TableColumn<Country, Number> populationCol = new TableColumn<>("Population");
        populationCol.setCellValueFactory(new PropertyValueFactory<>("population"));
        populationCol.setPrefWidth(100);

        // Continent Column
        TableColumn<Country, String> continentCol = new TableColumn<>("Continent");
        continentCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getContinents().isEmpty() ? "" : cellData.getValue().getContinents().get(0)));
        continentCol.setPrefWidth(100);

        // Subregion Column
        TableColumn<Country, String> subregionCol = new TableColumn<>("Subregion");
        subregionCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getSubregion()));
        subregionCol.setPrefWidth(120);

        // Languages Column
        TableColumn<Country, String> languagesCol = new TableColumn<>("Languages");
        languagesCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getLanguagesAsString()));
        languagesCol.setPrefWidth(150);

        // Add columns to the table
        tableView.getColumns().addAll(commonNameCol, officialNameCol, capitalCol, currencyCol, populationCol, continentCol, subregionCol, languagesCol);

        tableView.setPrefHeight(900); // Set the preferred height to 400 pixels
    }



    private void fetchCountriesByLanguage(String language, boolean exactMatch) {
        new Thread(() -> {
            try {
                // Create a final copy of language
                final String finalLanguage = language.trim();

                // Check if the finalLanguage is empty
                if (finalLanguage.isEmpty()) {
                    // Handle the case where the input is empty (e.g., only spaces)
                    return;
                }

                // Encode the language to handle spaces
                String adjustedLanguage = finalLanguage.replace(" ", "%20");

                CountryService service = new CountryService();

                // Get the list of countries by language directly from the service
                List<Country> countries = service.getCountriesByLanguage(adjustedLanguage, exactMatch);

                // Update the UI on the JavaFX application thread
                Platform.runLater(() -> {
                    tableView.getItems().clear();
                    tableView.getItems().addAll(countries);
                });
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                // Consider showing an error message to the user
            }
        }).start();
    }

    
    
    private void fetchCountriesByCurrency(String currency, boolean exactMatch) {
        new Thread(() -> {
            try {
                // Create a final copy of currency
                final String finalCurrency = currency.trim();

                // Check if the finalCurrency is empty
                if (finalCurrency.isEmpty()) {
                    // Handle the case where the input is empty (e.g., only spaces)
                    return;
                }

                // Encode the currency to handle spaces
                String adjustedCurrency = finalCurrency.replace(" ", "%20");

                CountryService service = new CountryService();

                // Get the list of countries by currency directly from the service
                Country[] countries = service.getCountriesByCurrency(adjustedCurrency, exactMatch);

                // Update the UI on the JavaFX application thread
                Platform.runLater(() -> {
                    tableView.getItems().clear();
                    tableView.getItems().addAll(Arrays.asList(countries));
                });
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                // Consider showing an error message to the user
            }
        }).start();
    }


 
    
    private void fetchAllCountries() {
        new Thread(() -> {
            try {
                CountryService service = new CountryService();
                Country[] allCountries = service.getAllCountries();
                System.out.println("Fetched " + allCountries.length + " countries."); // Debugging line

                Platform.runLater(() -> {
                    tableView.getItems().clear(); // Clear existing items
                    tableView.getItems().addAll(Arrays.asList(allCountries)); // Add all fetched countries
                    tableView.refresh(); // Refresh the table view
                    updateSearchHistory("Fetch All Countries", null); // null or empty string for no specific term

                });
            } catch (IOException | InterruptedException e) {
                e.printStackTrace(); // Log the error
                Platform.runLater(() -> {
                    // Optionally update the UI to inform the user that an error occurred
                });
            }
        }).start();
    }

    private void fetchCountryData(String countryName, boolean exactMatch) {
        new Thread(() -> {
            try {
                // Create a final copy of countryName
                final String finalCountryName = countryName.trim();

                // Check if the finalCountryName is empty
                if (finalCountryName.isEmpty()) {
                    // Handle the case where the input is empty (e.g., only spaces)
                    return;
                }

                // Encode the country name to handle spaces
                String adjustedCountryName = finalCountryName.replace(" ", "%20");

                CountryService service = new CountryService();

                // Get the list of countries directly from the service
                List<Country> countries = service.getCountriesByName(adjustedCountryName, exactMatch);

                // Update the UI on the JavaFX application thread
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