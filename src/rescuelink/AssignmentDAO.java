package rescuelink;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class AssignmentDAO {
    private final Connection con;

    public AssignmentDAO() {
        Connection tempCon = null;
        try {
            tempCon = DBCONNECT.ConnectToDB(); // Connect to your database
        } catch (SQLException e) {
            System.err.println("❌ Database connection failed in AssignmentDAO: " + e.getMessage());
        }
        con = tempCon;
    }

    // Assign a volunteer to a victim
    public boolean assignVolunteerToVictim(Volunteer volunteer, Victim victim) {
        if (con == null) {
            System.err.println("❌ No database connection available.");
            return false;
        }

        String query = "INSERT INTO assignments (volunteer_id, victim_id, assigned_at, is_active) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, volunteer.getVolunteerId());
            pst.setInt(2, victim.getId());
            pst.setTimestamp(3, java.sql.Timestamp.valueOf(LocalDateTime.now()));
            pst.setBoolean(4, true);

            int rows = pst.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.err.println("❌ Error inserting assignment: " + e.getMessage());
            return false;
        }
    }
}
