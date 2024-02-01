package com.richter.CountriesAppJFX;
import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
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
    private TextField searchTextField;
    private Button searchButton;
    
    
    @Override
    public void start(Stage primaryStage) {
        searchCriteriaComboBox = new ComboBox<>();
        searchCriteriaComboBox.getItems().addAll("Search all countries","By Country Name", "By Language", "By Currency");
        searchCriteriaComboBox.setPromptText("Select Search Criteria");

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

        HBox searchBox = new HBox(searchCriteriaComboBox, searchTextField, searchButton, resetButton);
        VBox vbox = new VBox(searchBox, tableView);
        Scene scene = new Scene(vbox, 800, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Country Information");
        primaryStage.show();
    }
    
    
    private void performSearch() {
        String selectedCriteria = searchCriteriaComboBox.getValue();
        String searchTerm = searchTextField.getText();

        if ("All Countries".equals(selectedCriteria)) {
            fetchAllCountries();
        } else if (!searchTerm.isEmpty()) {
            switch (selectedCriteria) {
                case "By Country Name":
                    fetchCountryData(searchTerm);
                    break;
                case "By Language":
                    // Add method to fetch data by language
                    break;
                case "By Currency":
                    // Add method to fetch data by currency
                    break;
                default:
                    // Handle default case
                    break;
            }
        }
    }
    

    private void setupTableView() {
        // Common Name Column
        TableColumn<Country, String> commonNameCol = new TableColumn<>("Common Name");
        commonNameCol.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getName().getCommon()));

        // Official Name Column
        TableColumn<Country, String> officialNameCol = new TableColumn<>("Official Name");
        officialNameCol.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getName().getOfficial()));

        // Capital Column
        TableColumn<Country, String> capitalCol = new TableColumn<>("Capital");
        capitalCol.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getCapital().isEmpty() ? "" : cellData.getValue().getCapital().get(0)));

        TableColumn<Country, String> currencyColumn = new TableColumn<>("Currency");
        currencyColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCurrenciesAsString()));
//
//        // Currency Symbol Column
//        TableColumn<Country, String> currencySymbolCol = new TableColumn<>("Currency Symbol");
//        currencySymbolCol.setCellValueFactory(cellData -> {
//            Currency currency = cellData.getValue().getCurrencies().values().iterator().next();
//            return new SimpleStringProperty(currency.getSymbol());
//        });

        // Population Column
        TableColumn<Country, Number> populationCol = new TableColumn<>("Population");
        populationCol.setCellValueFactory(new PropertyValueFactory<>("population"));

        // Continent Column
        TableColumn<Country, String> continentCol = new TableColumn<>("Continent");
        continentCol.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getContinents().isEmpty() ? "" : cellData.getValue().getContinents().get(0)));

        // Add columns to the table
        tableView.getColumns().addAll(commonNameCol, officialNameCol, capitalCol, currencyColumn, populationCol, continentCol);
    }

    private void fetchAllCountries() {
        new Thread(() -> {
            try {
                CountryService service = new CountryService();
                Country[] countries = service.getAllCountries(); // Assuming this method exists and fetches all countries

                Platform.runLater(() -> {
                    tableView.getItems().addAll(FXCollections.observableArrayList(countries));
                });
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                // Handle errors appropriately
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

    public static void main(String[] args) {
        launch(args);
    }
}
