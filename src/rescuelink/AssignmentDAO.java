package rescuelink;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.sql.ResultSet;

public class AssignmentDAO {
    private final Connection con;

    public AssignmentDAO() {
        Connection tempCon = null;
        try {
            tempCon = DBCONNECT.ConnectToDB(); // Connect to your database
        } catch (SQLException e) {
            System.err.println("Database connection failed in AssignmentDAO: " + e.getMessage());
        }
        con = tempCon;
    }

    // Assign a volunteer to a victim
    public boolean assignVolunteerToVictim(Volunteer volunteer, Victim victim) {
        if (con == null) {
            System.err.println("No database connection available.");
            return false;
        }

        String query = "INSERT INTO assignments (volunteer_id, victim_id, assigned_at, is_active) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, volunteer.getVolunteerId());
            pst.setInt(2, victim.getId());
            System.out.println("Assigning volunteer ID: " + volunteer.getVolunteerId());
            System.out.println("Assigning victim ID: " + victim.getId());

            pst.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            pst.setBoolean(4, true);

            int rows = pst.executeUpdate();
            System.out.println("Assignment inserted successfully. Rows affected: " + rows);
            return rows > 0;

        } catch (SQLException e) {
            System.err.println("Error inserting assignment: " + e.getMessage());
            return false;
        }
    }
    public List<Victim> getAssignedVictims(int volunteerId) {
    List<Victim> victims = new ArrayList<>();
    String sql = "SELECT v.* FROM victims v JOIN assignments a ON v.id = a.victim_id WHERE a.volunteer_id = ?";

    try (Connection con = DBCONNECT.ConnectToDB();
         PreparedStatement pst = con.prepareStatement(sql)) {

        pst.setInt(1, volunteerId);
        try (ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                victims.add(new Victim(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("location"),
                    rs.getString("condition"),
                    rs.getString("incident_type"),
                    rs.getString("severity"),
                    rs.getInt("people_affected"),
                    rs.getBoolean("immediate_rescue"),
                    rs.getString("status")
                ));
            }
        }
    } catch (SQLException e) {
    }
    return victims;
}
}