
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class OldApp extends Application {

    private TableView<Country> tableView = new TableView<>();

    @Override
    public void start(Stage primaryStage) {
    	
    	
        TableColumn<Country, String> commonNameCol = new TableColumn<>("Common Name");
        commonNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Country, String> officialNameCol = new TableColumn<>("Official Name");
        officialNameCol.setCellValueFactory(new PropertyValueFactory<>("capital"));
        
        TableColumn<Country, String> capitalCol = new TableColumn<>("Capital");
        capitalCol.setCellValueFactory(new PropertyValueFactory<>("capital"));
        
        TableColumn<Country, String> currencyNameCol = new TableColumn<>("Currency Name");
        currencyNameCol.setCellValueFactory(new PropertyValueFactory<>("capital"));
        
        TableColumn<Country, String> currencySymbolCol = new TableColumn<>("Currency Symbol");
        currencySymbolCol.setCellValueFactory(new PropertyValueFactory<>("capital"));
                
        TableColumn<Country, String> populationCol = new TableColumn<>("Population");
        populationCol.setCellValueFactory(new PropertyValueFactory<>("capital"));
        
        TableColumn<Country, String> continentCol = new TableColumn<>("Continent");
        continentCol.setCellValueFactory(new PropertyValueFactory<>("capital"));
        // Repeat for currency, population, continent

        tableView.getColumns().addAll(commonNameCol, officialNameCol, capitalCol,currencyNameCol,currencySymbolCol,populationCol,continentCol ); // Add other columns

        VBox vbox = new VBox(tableView);
        Scene scene = new Scene(vbox, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();

        fetchCountries(); // Implement this method to fetch data and populate the table
    }

    private void fetchCountries() {
        // Use CountryService to fetch countries and update tableView
        // Make sure to run UI updates on the JavaFX Application Thread
    }

    public static void main(String[] args) {
        launch(args);
    }
}