package rescuelink;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AdminDAO {
    private final Connection con;

    public AdminDAO() {
        con = DBCONNECT.ConnectToDB();
    }

    public boolean assignBadgeToVolunteer(int volunteerId, int badgeId, int pointsToAdd) {
        try {
            con.setAutoCommit(false); // Begin transaction

            // Update reward points and attended requests
            String updateVolunteer = """
                UPDATE Volunteer
                SET reward_points = reward_points + ?, attended_requests = attended_requests + 1
                WHERE volunteer_id = ?
            """;
            try (PreparedStatement pst1 = con.prepareStatement(updateVolunteer)) {
                pst1.setInt(1, pointsToAdd);
                pst1.setInt(2, volunteerId);
                pst1.executeUpdate();
            }

            // Insert badge assignment (ignore duplicates)
            String insertBadge = """
                INSERT IGNORE INTO volunteer_badges (volunteer_id, badge_id)
                VALUES (?, ?)
            """;
            try (PreparedStatement pst2 = con.prepareStatement(insertBadge)) {
                pst2.setInt(1, volunteerId);
                pst2.setInt(2, badgeId);
                pst2.executeUpdate();
            }

            con.commit();
            return true;

        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException rollbackEx) {
                System.err.println("️SQL Exception occurred:");

            }
            return false;

        } finally {
            try {
                con.setAutoCommit(true);
            } catch (SQLException ignored) {
            System.err.println("SQL Exception occurred:");
}
        }
    }
}
