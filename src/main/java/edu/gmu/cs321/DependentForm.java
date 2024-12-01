package edu.gmu.cs321;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.geometry.Pos;

public class DependentForm {

    private String aNumber;
    private String firstName;
    private String lastName;
    private String dob;
    private String country;
    private String relationship;

    public DependentForm() {
    }

    public static void openDependentForm(Stage parentStage, Immigrant immigrant) {
        Stage dependentStage = new Stage();
        dependentStage.setTitle("Add Dependent");

        // Create grid pane for the form
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        // Form fields
        Label firstNameLabel = new Label("First Name:");
        TextField firstNameField = new TextField();
        grid.add(firstNameLabel, 0, 1);
        grid.add(firstNameField, 1, 1);

        Label lastNameLabel = new Label("Last Name:");
        TextField lastNameField = new TextField();
        grid.add(lastNameLabel, 0, 2);
        grid.add(lastNameField, 1, 2);

        Label dobLabel = new Label("Date of Birth:");
        DatePicker dobPicker = new DatePicker();
        grid.add(dobLabel, 0, 3);
        grid.add(dobPicker, 1, 3);

        Label countryLabel = new Label("Country:");
        TextField countryField = new TextField();
        grid.add(countryLabel, 0, 4);
        grid.add(countryField, 1, 4);

        Label relationshipLabel = new Label("Relationship:");
        TextField relationshipField = new TextField();
        grid.add(relationshipLabel, 0, 5);
        grid.add(relationshipField, 1, 5);

        Button submitButton = new Button("Submit");
        grid.add(submitButton, 1, 6);

        // Handle form submission
        submitButton.setOnAction(event -> {

            LocalDate lddob = dobPicker.getValue();
            String dob = null;

            if (lddob != null) {

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); 
                String dobString = lddob.format(formatter);
                dob = dobString;
            }

            // Collect the dependent information from the form
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            // String dob = dobPicker.getValue().toString(); // Date picker value as String
            String country = countryField.getText();
            String relationship = relationshipField.getText();

            // Create a dependent object and add it to the immigrant's dependents list
            Dependent dependent = new Dependent(firstName, lastName, dob, country, relationship);
            immigrant.addDependent(dependent);

            // Pass the immigrant's data and the dependent info to Workflow to save to file
            Workflow.saveDependentToFile(immigrant, dependent);

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Dependent submitted successfully!");
            alert.showAndWait();

            // Close the dependent form window
            dependentStage.close();
        });

        // Set up and show the scene for the dependent form
        Scene scene = new Scene(grid, 400, 300);
        dependentStage.setScene(scene);
        dependentStage.show();
    }

    // Getters for the data collected in the form
    public String getANumber() {
        return aNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDob() {
        return dob;
    }

    public String getCountry() {
        return country;
    }

    public String getRelationship() {
        return relationship;
    }
}