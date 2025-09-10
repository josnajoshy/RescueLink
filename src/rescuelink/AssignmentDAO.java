
package rescuelink;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class AssignmentDAO {
    private final Connection con;

    public AssignmentDAO() {
        con = DBCONNECT.ConnectToDB(); // Connect to your database
    }

    // Assign a volunteer to a victim
    public boolean assignVolunteerToVictim(Volunteer volunteer, Victim victim) {
        String query = "INSERT INTO assignments (volunteer_id, victim_id, assigned_at, is_active) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, volunteer.getVolunteerId());
            pst.setInt(2, victim.getId());
            pst.setTimestamp(3, java.sql.Timestamp.valueOf(LocalDateTime.now()));
            pst.setBoolean(4, true);

            int rows = pst.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            return false;
        }
    }
}
