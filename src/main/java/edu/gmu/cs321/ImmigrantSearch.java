package edu.gmu.cs321;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import edu.gmu.cs321.Immigrant;

public class ImmigrantSearch {

    public static List<Immigrant> searchImmigrant(Connection conn, String aNumber, String firstName, String lastName, String dob) throws SQLException {
        List<Immigrant> immigrants = new ArrayList<>();
        String sql = "SELECT * FROM immigrant WHERE a_number = ? AND first_name = ? AND last_name = ? AND dob = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, aNumber);
            pstmt.setString(2, firstName);
            pstmt.setString(3, lastName); 
            pstmt.setString(4, dob); 

            ResultSet rs = pstmt.executeQuery();
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
        }
        System.out.println("Search results: " + immigrants.size() + " immigrants found.");
        return immigrants;
    }
}
