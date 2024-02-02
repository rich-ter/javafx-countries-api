package com.richter.CountriesAppJFX;
import java.io.IOException;
import java.util.Arrays;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
    private TextField searchTextField;
    private Button searchButton;
    private Button fetchAllButton; // Button for fetching all countries
    
    
    @Override
    public void start(Stage primaryStage) {
    	
        fetchAllButton = new Button("Search All Countries");
        fetchAllButton.setOnAction(event -> fetchAllCountries());

        searchCriteriaComboBox = new ComboBox<>();
        searchCriteriaComboBox.getItems().addAll("By Country Name", "By Language", "By Currency");
        searchCriteriaComboBox.setPromptText("Select Search Criteria");

        
        
        searchHistoryComboBox = new ComboBox<>();
        searchHistoryComboBox.setPromptText("Previous Searches");
        searchHistoryComboBox.setOnAction(event -> {
            String selectedSearch = searchHistoryComboBox.getSelectionModel().getSelectedItem();
            if (selectedSearch != null && !selectedSearch.isEmpty()) {
                // Extract criteria and term from the selectedSearch
                String[] parts = selectedSearch.split(": ", 2);
                if (parts.length > 1) {
                    String criteria = parts[0];
                    String term = parts[1];
                    searchCriteriaComboBox.setValue(criteria);
                    searchTextField.setText(term);
                    performSearch();
                }
            }
        });        
        
        
        searchTextField = new TextField();
        searchTextField.setPromptText("Enter search term");

        searchButton = new Button("Search");
        searchButton.setOnAction(event -> performSearch());

        // Create a reset button
        Button resetButton = new Button("Reset");
        resetButton.setOnAction(event -> {
            tableView.getItems().clear();
            searchCriteriaComboBox.getSelectionModel().clearSelection();
            searchTextField.clear();
        });
        
        setupTableView();

        HBox searchBox = new HBox(searchHistoryComboBox, fetchAllButton, searchCriteriaComboBox, searchTextField, searchButton, resetButton);
        searchBox.setAlignment(Pos.CENTER); // Align items in the HBox to the center horizontally
        searchBox.setSpacing(10); // Adjust spacing between items in the HBox as needed

        VBox vbox = new VBox(searchBox, tableView);
        vbox.setSpacing(20); // Increase space between VBox children, e.g., between searchBox and tableView
        vbox.setPadding(new Insets(20, 0, 20, 0)); // Add padding to the top and bottom
       
        Scene scene = new Scene(vbox, 1200, 900);
        scene.getStylesheets().add(getClass().getResource("/styling.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setTitle("Country Information");
        primaryStage.show();
    }
    
    
    private void performSearch() {
        String selectedCriteria = searchCriteriaComboBox.getValue();
        String searchTerm = searchTextField.getText();

        
        if ("Fetch All Countries".equals(selectedCriteria)) {
            fetchAllCountries();
        } else if (selectedCriteria != null && !selectedCriteria.trim().isEmpty()) {
            updateSearchHistory(selectedCriteria, searchTerm);
            tableView.getItems().clear(); // Clear table for new search
            switch (selectedCriteria) {
                case "By Country Name":
                    fetchCountryData(searchTerm);
                    break;
                case "By Language":
                    fetchCountriesByLanguage(searchTerm);
                    break;
                case "By Currency":
                    fetchCountriesByCurrency(searchTerm);
                    break;
                default:
                    break;
            }
        }
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
        
        TableColumn<Country, String> currencyColumn = new TableColumn<>("Currency");
        currencyColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCurrenciesAsString()));
        currencyColumn.setPrefWidth(150);

        // Population Column
        TableColumn<Country, Number> populationCol = new TableColumn<>("Population");
        populationCol.setCellValueFactory(new PropertyValueFactory<>("population"));
        populationCol.setPrefWidth(100);

        // Continent Column
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

        // Add columns to the table
        tableView.getColumns().addAll(commonNameCol, officialNameCol, capitalCol, currencyColumn, populationCol, continentCol, subregionCol, languagesCol);
        
        tableView.setPrefHeight(900); // Set the preferred height to 400 pixels

    }

    
    private void fetchCountriesByLanguage(String language) {
        new Thread(() -> {
            try {
                CountryService service = new CountryService();
                Country[] countries = service.getCountriesByLanguage(language);

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
    
    
    private void fetchCountriesByCurrency(String currency) {
        new Thread(() -> {
            try {
                CountryService service = new CountryService();
                Country[] countries = service.getCountriesByCurrency(currency);

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

    private void fetchCountryData(String countryName) {
        new Thread(() -> {
            try {
                CountryService service = new CountryService();
                Country country = service.getCountryByName(countryName);

                Platform.runLater(() -> {
                    tableView.getItems().clear();
                    tableView.getItems().add(country);
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
