package rescuelink;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AssignmentDAO {
    private final AlertDAO alertDAO = new AlertDAO();

    public boolean assignVolunteerToVictim(Volunteer volunteer, Victim victim) {
        String query = "INSERT INTO assignments (volunteer_id, victim_id, assigned_at, is_active) VALUES (?, ?, ?, ?)";

        try (Connection con = DBCONNECT.ConnectToDB();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setInt(1, volunteer.getVolunteerId());
            pst.setInt(2, victim.getId());
            pst.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            pst.setBoolean(4, true);

            int rows = pst.executeUpdate();
            System.out.println("✅ Assignment inserted. Rows affected: " + rows);

            if (rows > 0) {
                sendAlerts(volunteer, victim);
                return true;
            }

        } catch (SQLException e) {
            System.err.println("❌ Error inserting assignment: " + e.getMessage());
        }

        return false;
    }

    private void sendAlerts(Volunteer volunteer, Victim victim) {
        LocalDateTime now = LocalDateTime.now();

        Alert volunteerAlert = new Alert(volunteer,
            String.format("You have been assigned to assist victim %s at %s.",
                victim.getName(), victim.getLocation()),
            now, false);

        Alert victimAlert = new Alert(victim,
            String.format("Volunteer %s has been assigned to help you. Please stay safe.",
                volunteer.getName()),
            now, false);

        alertDAO.sendAlert(volunteerAlert);
        alertDAO.sendAlert(victimAlert);
    }

    public List<Victim> getAssignedVictims(int volunteerId) {
        List<Victim> victims = new ArrayList<>();
        String sql = "SELECT v.* FROM victims v JOIN assignments a ON v.id = a.victim_id WHERE a.volunteer_id = ?";

        try (Connection con = DBCONNECT.ConnectToDB();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, volunteerId);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Victim victim = new Victim(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("location"),
                        rs.getString("condition"),
                        rs.getString("incident_type"),
                        rs.getString("severity"),
                        rs.getInt("people_affected"),
                        rs.getBoolean("immediate_rescue"),
                        rs.getString("status")
                    );
                    victims.add(victim);
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error fetching assigned victims: " + e.getMessage());
        }

        return victims;
    }
}