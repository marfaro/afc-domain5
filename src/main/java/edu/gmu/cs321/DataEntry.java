package edu.gmu.cs321;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class DataEntry {
    private Workflow workflow;
    private Connection connection; // Database connection

    public DataEntry(Workflow workflow, Connection connection) {
        this.workflow = workflow;
        this.connection = connection;
    }

    public void start(Stage primaryStage) {
        primaryStage.setTitle("Immigrant Search");

        // Create the search form UI layout
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 20, 20));

        Label aNumberLabel = new Label("ANumber:");
        TextField aNumberField = new TextField();
        grid.add(aNumberLabel, 0, 0);
        grid.add(aNumberField, 1, 0);

        Label firstNameLabel = new Label("First Name:");
        TextField firstNameField = new TextField();
        grid.add(firstNameLabel, 0, 1);
        grid.add(firstNameField, 1, 1);

        Label lastNameLabel = new Label("Last Name:");
        TextField lastNameField = new TextField();
        grid.add(lastNameLabel, 0, 2);
        grid.add(lastNameField, 1, 2);

        Label dobLabel = new Label("Date of Birth:");
        TextField dobField = new TextField();  // We'll allow the user to enter dob as a string for simplicity
        grid.add(dobLabel, 0, 3);
        grid.add(dobField, 1, 3);

        Button submitButton = new Button("Search");
        grid.add(submitButton, 1, 4);


        submitButton.setOnAction(e -> {
            String aNumber = aNumberField.getText();
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String dob = dobField.getText(); // Expecting the format as mm/dd/yyyy or similar

            // Validate inputs before searching
            if (aNumber.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || dob.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "All fields must be filled in.");
                alert.showAndWait();
            } else {
                // Perform the immigrant search using ImmigrantSearch
                try {
                    List<Immigrant> immigrants = ImmigrantSearch.searchImmigrant(connection, aNumber, firstName, lastName, dob);

                    if (immigrants.isEmpty()) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "No immigrant found with the given details.");
                        alert.showAndWait();
                    } else {
                        // Assuming we have only one result, we show it in a new window
                        Immigrant immigrant = immigrants.get(0);  // Assuming first match is the correct one
                        showImmigrantDetails(immigrant);
                    }

                } catch (SQLException ex) {
                    ex.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Error accessing the database.");
                    alert.showAndWait();
                }
            }
        });

        // Set up the scene and show it
        Scene scene = new Scene(grid, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Method to show the immigrant details in a new window
    private void showImmigrantDetails(Immigrant immigrant) {
        Stage detailsStage = new Stage();
        detailsStage.setTitle("Immigrant Details");

        // Create the details UI layout
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 20, 20));

        // Populate the fields with immigrant details
        Label aNumberLabel = new Label("ANumber: " + immigrant.getANumber());
        grid.add(aNumberLabel, 0, 0);

        Label firstNameLabel = new Label("First Name: " + immigrant.getFirstName());
        grid.add(firstNameLabel, 0, 1);

        Label lastNameLabel = new Label("Last Name: " + immigrant.getLastName());
        grid.add(lastNameLabel, 0, 2);

        Label dobLabel = new Label("Date of Birth: " + immigrant.getDob());
        grid.add(dobLabel, 0, 3);

        Label emailLabel = new Label("Email: " + immigrant.getEmail());
        grid.add(emailLabel, 0, 4);

        Label phoneLabel = new Label("Phone: " + immigrant.getPhone());
        grid.add(phoneLabel, 0, 5);

        Label addressLabel = new Label("Address: " + immigrant.getAddress());
        grid.add(addressLabel, 0, 6);

         // Add Dependent button
    Button addDependentButton = new Button("Add Dependent");
    grid.add(addDependentButton, 1, 7); // Position it after the Address label or wherever you want

    // Action for Add Dependent button
    addDependentButton.setOnAction(e -> {
        // Call the method to open the DependentForm window to enter dependent info
        DependentForm.openDependentForm(detailsStage, immigrant);
    });

        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> detailsStage.close());
        grid.add(closeButton, 0, 7);

        // Set the scene and show it
        Scene scene = new Scene(grid, 400, 400);
        detailsStage.setScene(scene);
        detailsStage.show();
    }

}

// package edu.gmu.cs321;

// import java.time.LocalDate;
// import java.time.format.DateTimeFormatter;

// import javafx.geometry.Insets;
// import javafx.geometry.Pos;
// import javafx.scene.Scene;
// import javafx.scene.control.Alert;
// import javafx.scene.control.Button;
// import javafx.scene.control.DatePicker;
// import javafx.scene.control.Label;
// import javafx.scene.control.TextField;
// import javafx.scene.layout.GridPane;
// import javafx.stage.Stage;

// public class DataEntry {
//     private Workflow workflow;

//     public DataEntry(Workflow workflow) {
//         this.workflow = workflow;
//     }

//     public void start(Stage primaryStage) {
//         primaryStage.setTitle("Data Entry - Immigrant Application");

//         GridPane grid = new GridPane();
//         grid.setAlignment(Pos.CENTER);
//         grid.setHgap(10);
//         grid.setVgap(10);
//         grid.setPadding(new Insets(20, 20, 20, 20));

//         Label aNumberLabel = new Label("ANumber:"); 
//         TextField aNumberField = new TextField();
//         grid.add(aNumberLabel, 0, 0);
//         grid.add(aNumberField, 1, 0);

//         Label firstNameLabel = new Label("First Name:");
//         TextField firstNameField = new TextField();
//         grid.add(firstNameLabel, 0, 1);
//         grid.add(firstNameField, 1, 1);

//         Label lastNameLabel = new Label("Last Name:");
//         TextField lastNameField = new TextField();
//         grid.add(lastNameLabel, 0, 2);
//         grid.add(lastNameField, 1, 2);

//         Label dobLabel = new Label("Date of Birth:");
//         DatePicker dobPicker = new DatePicker();
//         grid.add(dobLabel, 0, 3);
//         grid.add(dobPicker, 1, 3);

//         Label emailLabel = new Label("Email:");
//         TextField emailField = new TextField();
//         grid.add(emailLabel, 0, 4);
//         grid.add(emailField, 1, 4);

//         Label phoneLabel = new Label("Phone:");
//         TextField phoneField = new TextField();
//         grid.add(phoneLabel, 0, 5);
//         grid.add(phoneField, 1, 5);

//         Label addressLabel = new Label("Address:");
//         TextField addressField = new TextField();
//         grid.add(addressLabel, 0, 6);
//         grid.add(addressField, 1, 6);

//         Button submitButton = new Button("Submit");
//         grid.add(submitButton, 1, 7);

//         Button clearButton = new Button("Clear");
//         Button cancelButton = new Button("Cancel");
//         grid.add(clearButton, 0, 7);
//         grid.add(cancelButton, 2, 7);

//         submitButton.setOnAction(e -> {

//             LocalDate lddob = dobPicker.getValue();
//             String dob = null;

//             if (lddob != null) {

//                 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); 
//                 String dobString = lddob.format(formatter);
//                 dob = dobString;
//             }

//             String aNumber = aNumberField.getText();
//             String firstName = firstNameField.getText();
//             String lastName = lastNameField.getText();
//             // String dob = dobPicker.toString();
//             String email = emailField.getText();
//             String phone = phoneField.getText();
//             String address = addressField.getText();

//             Form form = new Form(aNumber, firstName, lastName, dob, email, phone, address);

//             workflow.addForm(form);

//             aNumberField.clear();
//             firstNameField.clear();
//             lastNameField.clear();
//             dobPicker.setValue(null);
//             emailField.clear();
//             phoneField.clear();
//             addressField.clear();

//             Alert alert = new Alert(Alert.AlertType.INFORMATION, "Form submitted successfully!");
//             alert.showAndWait();
//         });

//         Scene scene = new Scene(grid, 400, 300);
//         primaryStage.setScene(scene);
//         primaryStage.show();
//     }
// }