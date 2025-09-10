package rescuelink;

import java.sql.*;
import java.util.ArrayList;

public class VictimModule {
    private final Connection con;

    public VictimModule() {
        con = DBCONNECT.ConnectToDB();
    }

    /**
     * Add a new victim report (victim side)
     * @param victim
     */
    public void addVictim(Victim victim) {
        try {
            String sql = "INSERT INTO victims " +
                    "(name, location, condition, incident_type, severity, people_affected, immediate_rescue, status) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, victim.getName());
            stmt.setString(2, victim.getLocation());
            stmt.setString(3, victim.getCondition());
            stmt.setString(4, victim.getIncidentType());
            stmt.setString(5, victim.getSeverity());
            stmt.setInt(6, victim.getPeopleAffected());
            stmt.setBoolean(7, victim.isImmediateRescue());

            // Default to "Pending" if no status given
            if (victim.getStatus() == null || victim.getStatus().isEmpty()) {
                stmt.setString(8, "Pending");
            } else {
                stmt.setString(8, victim.getStatus());
            }

            stmt.executeUpdate();
        } catch (SQLException e) {
        }
    }

    /**
     * Fetch all victims (for Admin dashboard)
     * @return 
     */
    public ArrayList<Victim> getAllVictims() {
        ArrayList<Victim> victims = new ArrayList<>();
        try {
            String sql = "SELECT * FROM victims";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Victim v = new Victim(
                        rs.getInt("victim_id"),
                        rs.getString("name"),
                        rs.getString("location"),
                        rs.getString("condition"),
                        rs.getString("incident_type"),
                        rs.getString("severity"),
                        rs.getInt("people_affected"),
                        rs.getBoolean("immediate_rescue"),
                        rs.getString("status")
                );
                victims.add(v);
            }
        } catch (SQLException e) {
        }
        return victims;
    }

    /**
     * Update victim status (Admin functionality)
     * @return 
     */
    public boolean updateVictimStatus(int victimId, String status) {
        try {
            String sql = "UPDATE victims SET status=? WHERE victim_id=?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, status);
            stmt.setInt(2, victimId);
            int rows = stmt.executeUpdate();
            return rows > 0; // success if update worked
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Fetch single victim by ID (optional for details view)
     * @return 
     */
    public Victim getVictimById(int victimId) {
        try {
            String sql = "SELECT * FROM victims WHERE victim_id=?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, victimId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Victim(
                        rs.getInt("victim_id"),
                        rs.getString("name"),
                        rs.getString("location"),
                        rs.getString("condition"),
                        rs.getString("incident_type"),
                        rs.getString("severity"),
                        rs.getInt("people_affected"),
                        rs.getBoolean("immediate_rescue"),
                        rs.getString("status")
                );
            }
        } catch (SQLException e) {
        }
        return null;
    }
}
