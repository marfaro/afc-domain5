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
    private int currentIndex = 0;  

    private TextField aNumberField, firstNameField, lastNameField, dobField, countryField, relationshipField, emailField, phoneField, addressField;
    private Label immigrantDataLabel;  
    private Label immigrantANumberLabel = new Label("ANumber:");
    private Label immigrantFirstNameLabel = new Label("First Name:");
    private Label immigrantLastNameLabel = new Label("Last Name:");
    private Label immigrantDobLabel = new Label("Date of Birth:");
    private Label immigrantEmailLabel = new Label("Email:");
    private Label immigrantPhoneLabel = new Label("Phone:");
    private Label immigrantAddressLabel = new Label("Address:");

    private Button submitToApprovalButton = new Button("Submit to Approval");
    private Button saveButton = new Button("Save");

    private Immigrant immigrant;  

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Review Dependents");

        loadWorkflowData();

        if (dependents.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "No dependents found in the workflow.");
            alert.showAndWait();
            primaryStage.close();
            return;
        }

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(50, 25, 50, 25));

    immigrantDataLabel = new Label("Immigrant: ");
    immigrantDataLabel.setStyle("-fx-font-weight: bold;"); 
    grid.add(immigrantDataLabel, 0, 0, 2, 1);

    immigrantANumberLabel = new Label("ANumber:");
    grid.add(immigrantANumberLabel, 0, 1);
    this.immigrantANumberLabel = new Label();  
    grid.add(this.immigrantANumberLabel, 1, 1);

    immigrantFirstNameLabel = new Label("First Name:");
    grid.add(immigrantFirstNameLabel, 0, 2);
    this.immigrantFirstNameLabel = new Label();  
    grid.add(this.immigrantFirstNameLabel, 1, 2);

    immigrantLastNameLabel = new Label("Last Name:");
    grid.add(immigrantLastNameLabel, 0, 3);
    this.immigrantLastNameLabel = new Label();  
    grid.add(this.immigrantLastNameLabel, 1, 3);

    immigrantDobLabel = new Label("Date of Birth:");
    grid.add(immigrantDobLabel, 0, 4);
    this.immigrantDobLabel = new Label();  
    grid.add(this.immigrantDobLabel, 1, 4);

    immigrantEmailLabel = new Label("Email:");
    grid.add(immigrantEmailLabel, 0, 5);
    this.immigrantEmailLabel = new Label(); 
    grid.add(this.immigrantEmailLabel, 1, 5);

    immigrantPhoneLabel = new Label("Phone:");
    grid.add(immigrantPhoneLabel, 0, 6);
    this.immigrantPhoneLabel = new Label();  
    grid.add(this.immigrantPhoneLabel, 1, 6);

    immigrantAddressLabel = new Label("Address:");
    grid.add(immigrantAddressLabel, 0, 7);
    this.immigrantAddressLabel = new Label();  
    grid.add(this.immigrantAddressLabel, 1, 7);

    Label dependentDataLabel = new Label("Dependent: ");
    dependentDataLabel.setStyle("-fx-font-weight: bold;"); 
    grid.add(dependentDataLabel, 0, 8, 2, 1);

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

        grid.add(saveButton, 2, 15, 2, 1);
        
        grid.add(submitToApprovalButton,4, 15, 2, 1);

        updateDependentFields(currentIndex);

        previousButton.setOnAction(e -> {
            if (currentIndex > 0) {
                currentIndex--;
                while (currentIndex > 0 && dependents.get(currentIndex).isSubmitted()) {
                    currentIndex--;
                }
                updateDependentFields(currentIndex);
                submitToApprovalButton.setDisable(false);
                saveButton.setDisable(false);
            }
        });
        
        nextButton.setOnAction(e -> {
            if (currentIndex < dependents.size() - 1) {
                currentIndex++;
                while (currentIndex < dependents.size() && dependents.get(currentIndex).isSubmitted()) {
                    currentIndex++;
                }
                updateDependentFields(currentIndex);
                submitToApprovalButton.setDisable(false);
                saveButton.setDisable(false);
            }
        });
        

        saveButton.setOnAction(e -> {
            if (isInputValid()) {

                Dependent dependent = dependents.get(currentIndex);

				String unique = Database.isUniqueDependent(dependent);
				if(unique == null){

					dependent.setANumber(aNumberField.getText());
					dependent.setFirstName(firstNameField.getText());
					dependent.setLastName(lastNameField.getText());
					dependent.setDob(dobField.getText());
					dependent.setCountry(countryField.getText());
					dependent.setRelationship(relationshipField.getText());

					saveWorkflowData();

					Alert alert = new Alert(Alert.AlertType.INFORMATION, "Dependent data saved successfully!");
					alert.showAndWait();
				} else {

					Alert alert = new Alert(Alert.AlertType.ERROR, "Dependent already exists under entry " + unique + ".");
					alert.showAndWait();
				}
            } else {

                Alert alert = new Alert(Alert.AlertType.ERROR, "Please fill in all required fields.");
                alert.showAndWait();
            }
        });

        submitToApprovalButton.setOnAction(e -> {
			String unique = Database.isUniqueDependent(dependents.get(currentIndex));
			if(unique == null){

				submitToApproval();
			} else {
				Alert alert = new Alert(Alert.AlertType.ERROR, "Dependent already exists under entry " + unique + ".");
                alert.showAndWait();
			}
        });

        Scene scene = new Scene(grid, 550, 550);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void submitToApproval() {
        File sourceFile = new File("src/resources/workflow.txt");
        File targetFile = new File("src/resources/workflow2.txt");

        Dependent dependent = dependents.get(currentIndex);
    
        dependent.setSubmitted(true);

        submitToApprovalButton.setDisable(true);
        saveButton.setDisable(true);

        List<String> updatedWorkflowData = new ArrayList<>();
        boolean dependentFound = false; 
    
        try (
            BufferedReader reader = new BufferedReader(new FileReader(sourceFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(targetFile, true)) // 'true' for appending
        ) {
            StringBuilder currentBlock = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                
                if (line.isEmpty()) {
                    if (currentBlock.length() > 0) {
                        if (!dependentFound && isTargetDependentBlock(currentBlock.toString(), dependent)) {
                            writeDependentToApprovalFile(writer, currentBlock.toString());
                            dependentFound = true; 
                        } else {
                            updatedWorkflowData.add(currentBlock.toString().trim());
                        }
                    }
                    currentBlock.setLength(0);
                } else {
                    currentBlock.append(line).append(System.lineSeparator());
                }
            }

            if (currentBlock.length() > 0) {
                if (!dependentFound && isTargetDependentBlock(currentBlock.toString(), dependent)) {
                    writeDependentToApprovalFile(writer, currentBlock.toString());
                    dependentFound = true;
                } else {
                    updatedWorkflowData.add(currentBlock.toString().trim());
                }
            }

            try (BufferedWriter writerUpdate = new BufferedWriter(new FileWriter(sourceFile))) {
                for (String block : updatedWorkflowData) {
                    writerUpdate.write(block);
                    writerUpdate.newLine();
                    writerUpdate.newLine();
                }
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Dependent submitted to approval!");
            alert.showAndWait();
    
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error submitting dependent to approval.");
            alert.showAndWait();
        }
    }
    

    private boolean isTargetDependentBlock(String block, Dependent dependent) {
        String firstName = dependent.getFirstName();
        String lastName = dependent.getLastName();
        String dob = dependent.getDob();  

        return block.contains("First Name: " + firstName) &&
               block.contains("Last Name: " + lastName) &&
               block.contains("DOB: " + dob);
    }
    
    private void writeDependentToApprovalFile(BufferedWriter writer, String dependentBlock) throws IOException {
        writer.write(dependentBlock);
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
            Immigrant immigrant = null; 
            Dependent dependent = null; 
    
            while ((line = reader.readLine()) != null) {
                line = line.trim(); 

                if (line.isEmpty() || !line.contains(":")) {
                    continue;  
                }
    
                String[] parts = line.split(":");
                if (parts.length < 2) {
                    System.out.println("Skipping malformed line: " + line);
                    continue; 
                }
    
                String key = parts[0].trim();
                String value = parts[1].trim();
    
                if (key.equals("ANumber")) {
                    String aNumber = value;

                    immigrant = findImmigrantByANumber(aNumber);
                    if (immigrant == null) {
                        immigrant = new Immigrant(aNumber, "Unknown", "Unknown", "Unknown", "Unknown", "Unknown", "Unknown");
                        immigrants.add(immigrant);
                    }

                    displayImmigrantData(immigrant);
                } else if (key.equals("First Name")) {
                    if (immigrant != null) {
                        immigrant.setFirstName(value); 
                    }
                } else if (key.equals("Last Name")) {
                    if (immigrant != null) {
                        immigrant.setLastName(value);  
                    }
                } else if (key.equals("DOB")) {
                    if (immigrant != null) {
                        immigrant.setDob(value); 
                    }
                } else if (key.equals("Country") && immigrant != null) {
                    String country = value;
                    String relationship = reader.readLine().trim().split(":")[1].trim(); 
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
        return null; 
    }

    private void loadImmigrantData(String aNumber) {
        Immigrant immigrant = getImmigrantFromDatabase(aNumber);

        if (immigrant != null) {
            immigrantANumberLabel.setText(immigrant.getANumber());
            immigrantFirstNameLabel.setText(immigrant.getFirstName());
            immigrantLastNameLabel.setText(immigrant.getLastName());
            immigrantDobLabel.setText(immigrant.getDob());
            immigrantEmailLabel.setText(immigrant.getEmail());
            immigrantPhoneLabel.setText(immigrant.getPhone());
            immigrantAddressLabel.setText(immigrant.getAddress());
        } else {
            System.out.println("Immigrant not found with ANumber: " + aNumber);
        }
    }
    
    private void displayImmigrantData(Immigrant immigrant) {
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

    String query = "SELECT a_number, first_name, last_name, dob, email, phone, address FROM immigrant WHERE a_number = ?";

    try (Connection conn = Database.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {

        stmt.setString(1, aNumber);

        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
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
