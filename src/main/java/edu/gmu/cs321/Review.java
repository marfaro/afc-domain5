package edu.gmu.cs321;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class Review extends Application {

    private List<Dependent> dependents = new ArrayList<>();
    private List<Immigrant> immigrants = new ArrayList<>();
    private int currentIndex = 0;  // Index to track the current dependent being edited

    private TextField aNumberField, firstNameField, lastNameField, dobField, countryField, relationshipField, emailField, phoneField, addressField;
    private Label immigrantDataLabel;  // To display immigrant's non-editable data
    private Label immigrantANumberLabel = new Label("ANumber:");
    private Label immigrantFirstNameLabel = new Label("First Name:");
    private Label immigrantLastNameLabel = new Label("Last Name:");
    private Label immigrantDobLabel = new Label("Date of Birth:");
    private Label immigrantEmailLabel = new Label("Email:");
    private Label immigrantPhoneLabel = new Label("Phone:");
    private Label immigrantAddressLabel = new Label("Address:");

    private Button submitToApprovalButton = new Button("Submit to Approval");

    private Immigrant immigrant;  // Store the immigrant data

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Review Dependents");

        // Load dependents and immigrant data from workflow.txt
        loadWorkflowData();

        if (dependents.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "No dependents found in the workflow.");
            alert.showAndWait();
            primaryStage.close();
            return;
        }

        // Create the UI
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(50, 25, 50, 25));

        // Immigrant data labels (non-editable)
    immigrantDataLabel = new Label("Immigrant: ");
    immigrantDataLabel.setStyle("-fx-font-weight: bold;"); 
    grid.add(immigrantDataLabel, 0, 0, 2, 1);

    // Add labels for Immigrant Data fields
    immigrantANumberLabel = new Label("ANumber:");
    grid.add(immigrantANumberLabel, 0, 1);
    this.immigrantANumberLabel = new Label();  // To display immigrant's ANumber
    grid.add(this.immigrantANumberLabel, 1, 1);

    immigrantFirstNameLabel = new Label("First Name:");
    grid.add(immigrantFirstNameLabel, 0, 2);
    this.immigrantFirstNameLabel = new Label();  // To display immigrant's First Name
    grid.add(this.immigrantFirstNameLabel, 1, 2);

    immigrantLastNameLabel = new Label("Last Name:");
    grid.add(immigrantLastNameLabel, 0, 3);
    this.immigrantLastNameLabel = new Label();  // To display immigrant's Last Name
    grid.add(this.immigrantLastNameLabel, 1, 3);

    immigrantDobLabel = new Label("Date of Birth:");
    grid.add(immigrantDobLabel, 0, 4);
    this.immigrantDobLabel = new Label();  // To display immigrant's Date of Birth
    grid.add(this.immigrantDobLabel, 1, 4);

    immigrantEmailLabel = new Label("Email:");
    grid.add(immigrantEmailLabel, 0, 5);
    this.immigrantEmailLabel = new Label();  // To display immigrant's Email
    grid.add(this.immigrantEmailLabel, 1, 5);

    immigrantPhoneLabel = new Label("Phone:");
    grid.add(immigrantPhoneLabel, 0, 6);
    this.immigrantPhoneLabel = new Label();  // To display immigrant's Phone
    grid.add(this.immigrantPhoneLabel, 1, 6);

    immigrantAddressLabel = new Label("Address:");
    grid.add(immigrantAddressLabel, 0, 7);
    this.immigrantAddressLabel = new Label();  // To display immigrant's Address
    grid.add(this.immigrantAddressLabel, 1, 7);

    Label dependentDataLabel = new Label("Dependent: ");
    dependentDataLabel.setStyle("-fx-font-weight: bold;"); 
    grid.add(dependentDataLabel, 0, 8, 2, 1);

        // Dependent data fields (editable)
        Label aNumberLabel = new Label("ANumber:");
        grid.add(aNumberLabel, 0, 9);

        aNumberField = new TextField();
        grid.add(aNumberField, 1, 9);

        Label firstNameLabel = new Label("First Name:");
        grid.add(firstNameLabel, 0, 10);

        firstNameField = new TextField();
        grid.add(firstNameField, 1, 10);

        Label lastNameLabel = new Label("Last Name:");
        grid.add(lastNameLabel, 0, 11);

        lastNameField = new TextField();
        grid.add(lastNameField, 1, 11);

        Label dobLabel = new Label("Date of Birth:");
        grid.add(dobLabel, 0, 12);

        dobField = new TextField();
        grid.add(dobField, 1, 12);

        Label countryLabel = new Label("Country:");
        grid.add(countryLabel, 0, 13);

        countryField = new TextField();
        grid.add(countryField, 1, 13);

        Label relationshipLabel = new Label("Relationship:");
        grid.add(relationshipLabel, 0, 14);

        relationshipField = new TextField();
        grid.add(relationshipField, 1, 14);

        // Navigation buttons
        Button previousButton = new Button("Previous");
        grid.add(previousButton, 0, 15);

        Button nextButton = new Button("Next");
        grid.add(nextButton, 1, 15);

        // Add Save button
        Button saveButton = new Button("Save");
        grid.add(saveButton, 2, 15, 2, 1);
        
        grid.add(submitToApprovalButton,4, 15, 2, 1);


        // Set initial data
        updateDependentFields(currentIndex);

        previousButton.setOnAction(e -> {
            if (currentIndex > 0) {
                currentIndex--;
                // Skip submitted dependents when moving backward
                while (currentIndex > 0 && dependents.get(currentIndex).isSubmitted()) {
                    currentIndex--;
                }
                updateDependentFields(currentIndex);
                submitToApprovalButton.setDisable(false);
            }
        });
        
        nextButton.setOnAction(e -> {
            if (currentIndex < dependents.size() - 1) {
                currentIndex++;
                // Skip submitted dependents when moving forward
                while (currentIndex < dependents.size() && dependents.get(currentIndex).isSubmitted()) {
                    currentIndex++;
                }
                updateDependentFields(currentIndex);
                submitToApprovalButton.setDisable(false);
            }
        });
        

        saveButton.setOnAction(e -> {
            // Validate the input fields
            if (isInputValid()) {
                // Get the current dependent
                Dependent dependent = dependents.get(currentIndex);
        
                // Update the dependent object with the new values
                dependent.setANumber(aNumberField.getText());
                dependent.setFirstName(firstNameField.getText());
                dependent.setLastName(lastNameField.getText());
                dependent.setDob(dobField.getText());
                dependent.setCountry(countryField.getText());
                dependent.setRelationship(relationshipField.getText());
        
                // Write updated data back to workflow.txt
                saveWorkflowData();
        
                // Notify the user
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Dependent data saved successfully!");
                alert.showAndWait();
                //updateUI();
            } else {
                // If input is invalid, show an alert
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please fill in all required fields.");
                alert.showAndWait();
            }
        });

        submitToApprovalButton.setOnAction(e -> {
            // Remove dependent data from workflow.txt and save it to workflow2.txt
            submitToApproval();
        });
        

        // Create and show the scene
        Scene scene = new Scene(grid, 550, 550);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    private void submitToApproval() {
        // Define the source (workflow.txt) and target (workflow2.txt) files
        File sourceFile = new File("src/resources/workflow.txt");
        File targetFile = new File("src/resources/workflow2.txt");
    
        // Get the current dependent being edited
        Dependent dependent = dependents.get(currentIndex);

        dependent.setSubmitted(true); 
    
        submitToApprovalButton.setDisable(true);

        // Temporary list to hold the updated content of workflow.txt (without the submitted dependent)
        List<String> updatedWorkflowData = new ArrayList<>();
        boolean dependentRemoved = false;
    
        try (
            BufferedReader reader = new BufferedReader(new FileReader(sourceFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(targetFile, true))
        ) {
            String line;
            List<String> dependentData = new ArrayList<>();
            boolean isSubmittingDependent = false;
    
            // Read each line from workflow.txt
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
    
                // Start collecting data when we find the dependent block based on ANumber
                if (line.startsWith("ANumber: " + dependent.getANumber())) {
                    isSubmittingDependent = true;
                    // Write the dependent's data to workflow2.txt
                    writeDependentToApprovalFile(writer, dependent);
                    dependentRemoved = true; // Flag that the dependent has been removed
                }
    
                // If we are currently processing the dependent block, skip writing it to workflow.txt
                if (isSubmittingDependent) {
                    if (line.startsWith("Relationship: ") && dependentRemoved) {
                        isSubmittingDependent = false; // End the dependent block when we reach the end of it
                    }
                } else {
                    // If not submitting, add the line to the updated data for workflow.txt
                    updatedWorkflowData.add(line);
                    // Preserve the newline between dependent blocks in workflow.txt
                    if (line.startsWith("Relationship: ")) {
                        updatedWorkflowData.add(""); // Add a newline after each dependent block
                    }
                }
            }
    
            // Write the updated workflow.txt data (excluding the submitted dependent) back to workflow.txt
            try (BufferedWriter writerUpdate = new BufferedWriter(new FileWriter(sourceFile))) {
                for (String lineToWrite : updatedWorkflowData) {
                    writerUpdate.write(lineToWrite);
                    writerUpdate.newLine();
                }
            }
    
            // Optionally, notify the user
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Dependent submitted to approval!");
            alert.showAndWait();
    
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error submitting dependent to approval.");
            alert.showAndWait();
        }
    }
    
    private void writeDependentToApprovalFile(BufferedWriter writer, Dependent dependent) throws IOException {
        // Write the specific dependent's data to workflow2.txt
        writer.write("ANumber: " + dependent.getANumber());
        writer.newLine();
        writer.write("First Name: " + dependent.getFirstName());
        writer.newLine();
        writer.write("Last Name: " + dependent.getLastName());
        writer.newLine();
        writer.write("DOB: " + dependent.getDob());
        writer.newLine();
        writer.write("Country: " + dependent.getCountry());
        writer.newLine();
        writer.write("Relationship: " + dependent.getRelationship());
        writer.newLine();
        
        // Add a blank line after each dependent's data in workflow2.txt
        writer.newLine();
    }
    
    private boolean isInputValid() {
        return !aNumberField.getText().trim().isEmpty() &&
               !firstNameField.getText().trim().isEmpty() &&
               !lastNameField.getText().trim().isEmpty() &&
               !dobField.getText().trim().isEmpty() &&
               !countryField.getText().trim().isEmpty() &&
               !relationshipField.getText().trim().isEmpty();
    }

    private void saveWorkflowData() {
        File file = new File("src/resources/workflow.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Immigrant immigrant : immigrants) {
    
                // Write each dependent
                for (Dependent dependent : immigrant.getDependents()) {
                    writer.write("ANumber: " + dependent.getANumber());
                    writer.newLine();
                    writer.write("First Name: " + dependent.getFirstName());
                    writer.newLine();
                    writer.write("Last Name: " + dependent.getLastName());
                    writer.newLine();
                    writer.write("DOB: " + dependent.getDob());
                    writer.newLine();
                    writer.write("Country: " + dependent.getCountry());
                    writer.newLine();
                    writer.write("Relationship: " + dependent.getRelationship());
                    writer.newLine();
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to save data.");
            alert.showAndWait();
        }
    }
    

    private void loadWorkflowData() {
        File file = new File("src/resources/workflow.txt");
        if (!file.exists()) {
            return;
        }
    
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            Immigrant immigrant = null; // To store the immigrant related to the current dependent
            Dependent dependent = null; // To store the current dependent being processed
    
            while ((line = reader.readLine()) != null) {
                line = line.trim(); // Remove leading/trailing whitespaces
    
                // Skip empty lines or lines that don't contain a ":"
                if (line.isEmpty() || !line.contains(":")) {
                    continue;  // Skip malformed or empty lines
                }
    
                String[] parts = line.split(":");
                if (parts.length < 2) {
                    System.out.println("Skipping malformed line: " + line);
                    continue; // Skip lines where there is no valid data after ":"
                }
    
                String key = parts[0].trim();
                String value = parts[1].trim();
    
                if (key.equals("ANumber")) {
                    String aNumber = value;
    
                    // Check if this ANumber belongs to an existing Immigrant
                    immigrant = findImmigrantByANumber(aNumber);
                    if (immigrant == null) {
                        immigrant = new Immigrant(aNumber, "Unknown", "Unknown", "Unknown", "Unknown", "Unknown", "Unknown");
                        immigrants.add(immigrant);
                    }
    
                    // Display immigrant details in non-editable fields (assuming you have a UI for this)
                    displayImmigrantData(immigrant);
                } else if (key.equals("First Name")) {
                    if (immigrant != null) {
                        immigrant.setFirstName(value);  // Set for the immigrant
                    }
                } else if (key.equals("Last Name")) {
                    if (immigrant != null) {
                        immigrant.setLastName(value);  // Set last name for the immigrant
                    }
                } else if (key.equals("DOB")) {
                    if (immigrant != null) {
                        immigrant.setDob(value);  // Set for the immigrant
                    }
                } else if (key.equals("Country") && immigrant != null) {
                    // Process dependent information if available
                    String country = value;
                    String relationship = reader.readLine().trim().split(":")[1].trim(); // Next line is Relationship
                    dependent = new Dependent(immigrant.getANumber(), immigrant.getFirstName(), immigrant.getLastName(), immigrant.getDob(), country, relationship);
                    dependents.add(dependent);
                    immigrant.addDependent(dependent);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

    private Immigrant findImmigrantByANumber(String aNumber) {
        for (Immigrant immigrant : immigrants) {
            if (immigrant.getANumber().equals(aNumber)) {
                return immigrant;
            }
        }
        return null; // Return null if no immigrant found with this ANumber
    }

    private void loadImmigrantData(String aNumber) {
        // Fetch immigrant data from the database
        Immigrant immigrant = getImmigrantFromDatabase(aNumber);
    
        // Populate the fields with the retrieved immigrant data
        if (immigrant != null) {
            immigrantANumberLabel.setText(immigrant.getANumber());
            immigrantFirstNameLabel.setText(immigrant.getFirstName());
            immigrantLastNameLabel.setText(immigrant.getLastName());
            immigrantDobLabel.setText(immigrant.getDob());
            immigrantEmailLabel.setText(immigrant.getEmail());
            immigrantPhoneLabel.setText(immigrant.getPhone());
            immigrantAddressLabel.setText(immigrant.getAddress());
        } else {
            // Handle case where the immigrant data is not found
            System.out.println("Immigrant not found with ANumber: " + aNumber);
        }
    }
    
    private void displayImmigrantData(Immigrant immigrant) {
        // Assuming you have text fields in the UI for each immigrant field, populate them
        immigrantANumberLabel.setText(immigrant.getANumber());
        immigrantFirstNameLabel.setText(immigrant.getFirstName());
        immigrantLastNameLabel.setText(immigrant.getLastName());
        immigrantDobLabel.setText(immigrant.getDob());
        immigrantEmailLabel.setText(immigrant.getEmail());
        immigrantPhoneLabel.setText(immigrant.getPhone());
        immigrantAddressLabel.setText(immigrant.getAddress());
    }

    public Immigrant getImmigrantFromDatabase(String aNumber) {
    Immigrant immigrant = null;

    // SQL query to fetch the immigrant data by ANumber
    String query = "SELECT a_number, first_name, last_name, dob, email, phone, address FROM immigrant WHERE a_number = ?";

    try (Connection conn = Database.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {

        // Set the ANumber parameter for the query
        stmt.setString(1, aNumber);

        // Execute the query
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                // Create the immigrant object from the result set
                immigrant = new Immigrant(
                    rs.getString("a_number"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("dob"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("address")
                );
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return immigrant;
}

    
    private void updateDependentFields(int index) {
        // Skip dependents that have already been submitted
        while (index < dependents.size() && dependents.get(index).isSubmitted()) {
            index++;
        }

        if (index >= 0 && index < dependents.size()) {
            Dependent dependent = dependents.get(index);
            aNumberField.setText(dependent.getANumber());
            firstNameField.setText(dependent.getFirstName());
            lastNameField.setText(dependent.getLastName());
            dobField.setText(dependent.getDob());
            countryField.setText(dependent.getCountry());
            relationshipField.setText(dependent.getRelationship());

            loadImmigrantData(dependent.getANumber());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
