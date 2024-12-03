package edu.gmu.cs321;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Login extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Immigration System Login"); 

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Image logo = new Image("file:src/resources/uscis_logo.png");
        ImageView logoView = new ImageView(logo);
        logoView.setFitHeight(100);
        logoView.setFitWidth(300);
        grid.add(logoView, 0, 0, 2, 1);

        Label userIdLabel = new Label("User ID:");
        grid.add(userIdLabel, 0, 1);
        TextField userIdField = new TextField();
        grid.add(userIdField, 1, 1);

        Label passwordLabel = new Label("Password:");
        grid.add(passwordLabel, 0, 2);
        PasswordField passwordField = new PasswordField();
        grid.add(passwordField, 1, 2);

        Button loginButton = new Button("Login");
        grid.add(loginButton, 1, 3);

        Hyperlink forgotPassword = new Hyperlink("Forgot password?");
        grid.add(forgotPassword, 1, 4);

        try {
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/afc", "root", "password1");

        loginButton.setOnAction(e -> {
        String userId = userIdField.getText();
        String password = passwordField.getText();

        if (authenticateDataEntry(userId, password)) {

            primaryStage.close();

            Stage dataEntryStage = new Stage();
            DataEntry dataEntry = new DataEntry(Workflow.getInstance(userId), connection);
            dataEntry.start(dataEntryStage);
        } else if (authenticateReview(userId, password)) {

            primaryStage.close();

            Stage reviewStage = new Stage();
            Review review = new Review();
            review.start(reviewStage);
        } else {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Invalid User ID or Password.");
            errorAlert.showAndWait();
        }
        });

        } catch (SQLException ex) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Error accessing the database: " + ex.getMessage());
            errorAlert.showAndWait();
            ex.printStackTrace();
        }

        
        Scene scene = new Scene(grid, 400, 350);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private boolean authenticateDataEntry(String userId, String password) {
        return "westeaston".equals(userId) && "password123".equals(password);
    }

    private boolean authenticateReview(String userId, String password) {
        return "johnsonnia".equals(userId) && "angela07".equals(password);
    }

    public static void main(String[] args) {
        launch(args);
    }
}