package rescuelink;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BadgeDAO {
    private Connection con;

    public BadgeDAO() {
        try {
            con = DBCONNECT.ConnectToDB(); // ✅ handle SQLException
        } catch (SQLException e) {
            System.err.println("❌ Database connection failed in BadgeDAO: " + e.getMessage());
        }
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
            System.err.println("❌ SQL Error in getAllBadges(): " + e.getMessage());
        }

        return badges;
    }

    // Add a new badge
    public boolean addBadge(Badge badge) {
        String query = "INSERT INTO badges (name, description) VALUES (?, ?)";

        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, badge.getName());
            pst.setString(2, badge.getDescription());
            return pst.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("❌ SQL Error in addBadge(): " + e.getMessage());
            return false;
        }
    }
}
