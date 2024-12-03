package edu.gmu.cs321;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private static final String url = "jdbc:mysql://localhost:3306/afc";
    private static final String userId = "root";  
    private static final String password = "password1"; 

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, userId, password);
    }

    public static Immigrant getImmigrantByANumber(String aNumber) {
        String query = "SELECT * FROM immigrant WHERE a_number = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, aNumber);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Immigrant(
                    rs.getInt("id"),
                    rs.getString("a_number"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("dob"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("address")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; 
    }

    public static List<Dependent> getDependentsByANumber(String aNumber) {
        String query = "SELECT * FROM dependent WHERE a_number = ?";
        List<Dependent> dependents = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, aNumber);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                dependents.add(new Dependent(
                    rs.getString("a_number"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("dob"),
                    rs.getString("country"),
                    rs.getString("relationship")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dependents;
    }

    public static void updateDependent(Dependent dependent) {
        String query = "UPDATE dependent SET first_name = ?, last_name = ?, dob = ?, country = ?, relationship = ? WHERE a_number = ? AND first_name = ? AND last_name = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, dependent.getFirstName());
            stmt.setString(2, dependent.getLastName());
            stmt.setString(3, dependent.getDob());
            stmt.setString(4, dependent.getCountry());
            stmt.setString(5, dependent.getRelationship());
            stmt.setString(6, dependent.getANumber());
            stmt.setString(7, dependent.getFirstName());
            stmt.setString(8, dependent.getLastName());

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Dependent data updated successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void submitDependentForApproval(Dependent dependent) {
        String query = "INSERT INTO dependent (a_number, first_name, last_name, dob, country, relationship) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, dependent.getANumber());
            stmt.setString(2, dependent.getFirstName());
            stmt.setString(3, dependent.getLastName());
            stmt.setString(4, dependent.getDob());
            stmt.setString(5, dependent.getCountry());
            stmt.setString(6, dependent.getRelationship());

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Dependent data submitted for approval.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
	
    public static String isUniqueDependent(Dependent dependent) {
        String query = "SELECT * FROM dependent";
        try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                if(rs.getString("first_name").equals(dependent.getFirstName()) && rs.getString("last_name").equals(dependent.getLastName()) && rs.getString("dob").equals(dependent.getDob())){
					return rs.getString("a_number");
				}
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
		
		return null;
    }
}
