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

	// Table view για τα data μας
    private TableView<Country> tableView = new TableView<>();
    // Προηγούμενες αναζητήσεις
    private ComboBox<String> searchHistoryComboBox; 
    private Button searchButton = new Button("Search");
    private Button fetchAllButton; 
    // τα 3 παρακάτω ειναι components απο το javafxcontrols module
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
        
        //δηλώνουμε την μέθοδο performsearch στο κουμπί searchButton το οποίο είναι το searchInputBox
        searchButton.setOnAction(event -> performSearch());

        // εκκαθάριση επιλογών, αν θέλαμε θα μπορούσαμε να βάλουμε και το ιστορικό εδώ. 
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

        // initiation για το κουμπι το οποιό καθορίζει την μέθοδο της αναζήτησης. 
        HBox searchInputBox = new HBox( searchButton);
        searchInputBox.setAlignment(Pos.CENTER);
        searchInputBox.setSpacing(10);

        // Παρουσίαση του header μας και τον επιλογών 
        HBox searchBox = new HBox(searchHistoryComboBox, fetchAllButton, countrySearchBox,languageSearchBox,currencySearchBox, searchInputBox, resetButton);
        searchBox.setAlignment(Pos.CENTER);
        searchBox.setSpacing(10);

        VBox vbox = new VBox(searchBox, tableView);
        vbox.setSpacing(20);
        vbox.setPadding(new Insets(20, 0, 20, 0));

        // μέγεθος οθόνης
        Scene scene = new Scene(vbox, 1200, 900);
        // κώδικας CSS
        scene.getStylesheets().add(getClass().getResource("/styling.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setTitle("Απαλλακτική Εργασία Java ΠΜΣ Προηγμένα Πληροφοριακά Συστήματα");
        Image icon = new Image("UNIPI.jpg");
        primaryStage.getIcons().add(icon);
        primaryStage.show();

    }
 
    // μέθοδος για την αναζήτη απο το history box 
    // καλείτε αυτόματα χωρίς το κουμπί search 
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
    
    // κοιτάει αν είναι άδεια τα πεδία και αν δεν είναι να βγάλει error στον χρήστη 
    private void performSearch() {
        // Check which search criteria is selected and call the corresponding method
        if (!countrySearchableComboBox.getSelectionModel().isEmpty()) {
            try {
                performCountrySearch();
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("An error occurred during country search: " + e.getMessage());
            }
        } else if (!languageSearchableComboBox.getSelectionModel().isEmpty()) {
            try {
                performLanguageSearch();
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("An error occurred during language search: " + e.getMessage());
            }
        } else if (!currencySearchableComboBox.getSelectionModel().isEmpty()) {
            try {
                performCurrencySearch();
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("An error occurred during currency search: " + e.getMessage());
            }
        }
    }

    // το ίδιο με παραπάνω αλλα για το κάθε component
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
    
    // το ίδιο με παραπάνω αλλα για το κάθε component
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
    
    // το ίδιο με παραπάνω αλλα για το κάθε component
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
    
    // Την ορίζουμε ως public για να την χρησιμοποιήσουμε 
    // για try catch σε αλλες κλασεις.
    public void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Search Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // κοιτάει αν ένα search term είναι άδειο 
    private boolean isValidSearchTerm(String searchTerm) {
        return searchTerm != null && !searchTerm.trim().isEmpty();
    }

    // παρουσίαση των δεδομένων
    private void setupTableView() {

    	// παρακάτω είναι όλοι οι πίνακες που υπάρχουν στο UI
    	
    	
    	TableColumn<Country, String> commonNameCol = new TableColumn<>("Common Name");
        commonNameCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getName().getCommon()));
        commonNameCol.setPrefWidth(200); 

        TableColumn<Country, String> officialNameCol = new TableColumn<>("Official Name");
        officialNameCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getName().getOfficial()));
        officialNameCol.setPrefWidth(200); 

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

    // γυρνάει τις χώρες ανα γλώσσα, μέσω της μεθόδου στο CountryService
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
                Platform.runLater(() -> showAlert("Error fetching countries by language: " + e.getMessage()));
            }
        }).start();
    }

    // γυρνάει τις χώρες ανα νόμισμα, μέσω της μεθόδου στο CountryService
    private void fetchCountriesByCurrency(String currency, boolean exactMatch) {
        new Thread(() -> {
            try {
                final String finalCurrency = currency.trim();
                if (finalCurrency.isEmpty()) {
                    return;
                }
                // σε περίπτωση κενού, πρόσθεσε το %20.
                String adjustedCurrency = finalCurrency.replace(" ", "%20");
                CountryService service = new CountryService();
                Country[] countries = service.getCountriesByCurrency(adjustedCurrency, exactMatch);
                
                Platform.runLater(() -> {
                    tableView.getItems().clear();
                    tableView.getItems().addAll(Arrays.asList(countries));
                });
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                Platform.runLater(() -> showAlert("Error fetching countries by currency: " + e.getMessage()));
            }
        }).start();
    }

    // γυρνάει όλες τις χώρες
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
                Platform.runLater(() -> showAlert("Error fetching all countries: " + e.getMessage()));
            }
        }).start();
    }

    // γυρνάει μία χώρα με βάση το όνομα της, μέσω της μεθόδου στο CountryService
    private void fetchCountryData(String countryName, boolean exactMatch) {
        new Thread(() -> {
            try {
                final String finalCountryName = countryName.trim();

                if (finalCountryName.isEmpty()) {
                    return;
                }

                // ξανά το πρόβλημα με το κενό καθώς το api endpoint δεν δουλευέι αλλιώς.
                String adjustedCountryName = finalCountryName.replace(" ", "%20");

                CountryService service = new CountryService();

                List<Country> countries = service.getCountriesByName(adjustedCountryName, exactMatch);

                Platform.runLater(() -> {
                    tableView.getItems().clear();
                    tableView.getItems().addAll(countries);
                });
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                Platform.runLater(() -> showAlert("Error fetching country data: " + e.getMessage()));
            }
        }).start();
    }

    // η μέθοδος που κρατάει το ιστορικό για τις αναζητήσεις του χρήστη 
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