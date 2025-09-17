package rescuelink;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AdminDAO {
    private final Connection con;

    public AdminDAO() {
        try {
            con = DBCONNECT.ConnectToDB();
        } catch (SQLException e) {
            throw new RuntimeException("❌ Failed to connect to database: " + e.getMessage(), e);
        }
    }

    /**
     * Assigns a badge to a volunteer, updates points and attended requests.
     *
     * @param volunteerId  the volunteer's ID
     * @param badgeId      the badge ID
     * @param pointsToAdd  reward points to add
     * @return true if successful, false otherwise
     */
    public boolean assignBadgeToVolunteer(int volunteerId, int badgeId, int pointsToAdd) {
        String updateVolunteer = """
                UPDATE volunteers
                SET reward_points = reward_points + ?, attended_requests = attended_requests + 1
                WHERE volunteer_id = ?
            """;

        String insertBadge = """
                INSERT IGNORE INTO volunteer_badges (volunteer_id, badge_id)
                VALUES (?, ?)
            """;

        try {
            con.setAutoCommit(false); // Begin transaction

            // Update volunteer points & requests
            try (PreparedStatement pst1 = con.prepareStatement(updateVolunteer)) {
                pst1.setInt(1, pointsToAdd);
                pst1.setInt(2, volunteerId);
                pst1.executeUpdate();
            }

            // Insert badge assignment
            try (PreparedStatement pst2 = con.prepareStatement(insertBadge)) {
                pst2.setInt(1, volunteerId);
                pst2.setInt(2, badgeId);
                pst2.executeUpdate();
            }

            con.commit();
            return true;

        } catch (SQLException e) {
            System.err.println("❌ Error in assignBadgeToVolunteer: " + e.getMessage());
            try {
                con.rollback();
            } catch (SQLException rollbackEx) {
                System.err.println("⚠ Rollback failed: " + rollbackEx.getMessage());
            }
            return false;

        } finally {
            try {
                con.setAutoCommit(true);
            } catch (SQLException ignored) {
                System.err.println("⚠ Could not reset autocommit.");
            }
        }
    }
}
