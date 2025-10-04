package rescuelink;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDAO {

    /** Validate admin login credentials */
    public boolean validateAdmin(String username, String password) {
        String sql = "SELECT * FROM admin WHERE username = ? AND password_hash = ?";

        try (Connection con = DBCONNECT.ConnectToDB();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, username);
            pst.setString(2, password);

            try (ResultSet rs = pst.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.err.println("❌ Admin login error: " + e.getMessage());
            return false;
        }
    }

    /** Assign badge to volunteer and update points/requests */
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

        try (Connection con = DBCONNECT.ConnectToDB()) {
            con.setAutoCommit(false); // Begin transaction

            try (
                PreparedStatement pst1 = con.prepareStatement(updateVolunteer);
                PreparedStatement pst2 = con.prepareStatement(insertBadge)
            ) {
                pst1.setInt(1, pointsToAdd);
                pst1.setInt(2, volunteerId);
                pst1.executeUpdate();

                pst2.setInt(1, volunteerId);
                pst2.setInt(2, badgeId);
                pst2.executeUpdate();

                con.commit();
                return true;

            } catch (SQLException e) {
                System.err.println("❌ Error during badge assignment: " + e.getMessage());
                con.rollback();
                return false;
            }

        } catch (SQLException e) {
            System.err.println("❌ Connection or rollback failure: " + e.getMessage());
            return false;
        }
    }
}