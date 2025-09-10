
package rescuelink;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import rescuelink.User;
import rescuelink.Volunteer;

public class AlertDAO {
    private final Connection con;

    public AlertDAO() {
        con = DBCONNECT.ConnectToDB();
    }

    // Send an alert
    public boolean sendAlert(Alert alert) {
    String query = "INSERT INTO alerts (recipient_id, recipient_type, message, created_at, is_read) VALUES (?, ?, ?, ?, ?)";

    try (PreparedStatement pst = con.prepareStatement(query)) {
        pst.setInt(1, alert.getRecipient().getId());
        pst.setString(2, (alert.getRecipient() instanceof Volunteer) ? "VOLUNTEER" : "VICTIM");
        pst.setString(3, alert.getMessage());
        pst.setTimestamp(4, Timestamp.valueOf(alert.getCreatedAt()));
        pst.setBoolean(5, alert.isRead());

        return pst.executeUpdate() > 0;
    } catch (SQLException e) {
        return false;
    }
}


    // Get alerts for a specific user
    public List<Alert> getAlertsForUser(User user) {
        List<Alert> alerts = new ArrayList<>();
        String query = "SELECT * FROM alerts WHERE recipient_id = ? AND recipient_type = ? ORDER BY created_at DESC";

        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, user.getId());
            pst.setString(2, user instanceof Volunteer ? "VOLUNTEER" : "VICTIM");

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                alerts.add(new Alert(
                        rs.getInt("id"),
                        user,
                        rs.getString("message"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getBoolean("is_read")
                ));
            }

        } catch (SQLException e) {
        }

        return alerts;
    }
}
