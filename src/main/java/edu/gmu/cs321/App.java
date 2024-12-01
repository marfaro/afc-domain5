package edu.gmu.cs321;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    public static void main(String[] args) {
        ArrayList<Immigrant> immigrants = new ArrayList<>();
        String url = "jdbc:mysql://localhost:3306/afc";
        String username = "root";
        String password = "password1";

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            String sql = "SELECT * FROM immigrant ORDER BY last_name";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                immigrants.add(new Immigrant(
                    rs.getInt("id"),
                    rs.getString("a_number"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("dob"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("address")
                ));
            }

            for(Immigrant imm: ImmigrantSearch.searchImmigrant(conn, "A102030405", "Li Wei", "Cheng","1956-12-06")){
                System.out.println(imm.aNumber + "\n" + imm.firstName + "\n" + imm.lastName + "\n" + imm.dob + "\n" + imm.email + "\n" + imm.phone + "\n" + imm.address);
            }
            


        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        // for (Immigrant immigrant : immigrants) {
        //     System.out.println("Immigrant: " + immigrant.firstName + " " + immigrant.lastName + " " + immigrant.dob);
        // }

        
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
