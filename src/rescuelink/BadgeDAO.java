package rescuelink;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BadgeDAO {
    private final Connection con;

    public BadgeDAO() {
        con = DBCONNECT.ConnectToDB();
    }

    // Fetch all badge definitions from the database
    public List<Badge> getAllBadges() {
        List<Badge> badges = new ArrayList<>();
        String query = "SELECT * FROM badges";

        try (PreparedStatement pst = con.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Badge badge = new Badge(
                    rs.getInt("badge_id"),
                    rs.getString("name"),
                    rs.getString("description")
                );
                badges.add(badge);
            }

        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
        }

        return badges;
    }

    // Optional: Add a new badge to the system
    public boolean addBadge(Badge badge) {
        String query = "INSERT INTO badges (name, description) VALUES (?, ?)";

        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, badge.getName());
            pst.setString(2, badge.getDescription());
            return pst.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            return false;
        }
    }
}
