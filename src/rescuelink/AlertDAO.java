package rescuelink;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlertDAO {

    public AlertDAO() {
        
    }

    /** Send a new alert
     * @param alert
     * @return  */
    public boolean sendAlert(Alert alert) {
        String sql = "INSERT INTO alerts (recipient_id, recipient_type, message, sent_at, is_read) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DBCONNECT.ConnectToDB();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, alert.getRecipient().getId());
            pst.setString(2, (alert.getRecipient() instanceof Volunteer) ? "volunteer" : "victim");
            pst.setString(3, alert.getMessage());
            pst.setTimestamp(4, Timestamp.valueOf(alert.getSentAt())); // maps to sent_at
            pst.setBoolean(5, alert.isRead());

            int rows = pst.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.err.println("Error sending alert: " + e.getMessage());
            return false;
        }
    }

    /** Get all alerts for a specific user
     * @param user
     * @return  */
    public List<Alert> getAlertsForUser(User user) {
        List<Alert> alerts = new ArrayList<>();
        String sql = "SELECT * FROM alerts WHERE recipient_id = ? AND recipient_type = ? ORDER BY sent_at DESC";

        try (Connection con = DBCONNECT.ConnectToDB();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, user.getId());
            pst.setString(2, (user instanceof Volunteer) ? "volunteer" : "victim");

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    alerts.add(new Alert(
                            rs.getInt("alert_id"),
                            user,
                            rs.getString("message"),
                            rs.getTimestamp("sent_at").toLocalDateTime(),
                            rs.getBoolean("is_read")
                    ));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error fetching alerts: " + e.getMessage());
        }

        return alerts;
    }
}