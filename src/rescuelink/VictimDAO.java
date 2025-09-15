package rescuelink;

import java.sql.*;
import java.util.ArrayList;

public class VictimDAO {

    private final Connection con;

    public VictimDAO() {
        try {
            con = DBCONNECT.ConnectToDB();
        } catch (SQLException e) {
            throw new RuntimeException("❌ Database connection failed!", e);
        }
    }

    /** Add a new victim */
    public boolean addVictim(Victim victim) {
        String sql = "INSERT INTO victims " +
                "(name, location, `condition`, incident_type, severity, people_affected, immediate_rescue, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, victim.getName());
            stmt.setString(2, victim.getLocation());
            stmt.setString(3, victim.getCondition());
            stmt.setString(4, victim.getIncidentType());
            stmt.setString(5, victim.getSeverity());
            stmt.setInt(6, victim.getPeopleAffected());
            stmt.setBoolean(7, victim.isImmediateRescue());
            stmt.setString(8, (victim.getStatus() == null || victim.getStatus().isEmpty()) ? "Pending" : victim.getStatus());

            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** Get all victims */
    public ArrayList<Victim> getAllVictims() {
        ArrayList<Victim> victims = new ArrayList<>();
        String sql = "SELECT * FROM victims";

        try (PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

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
            e.printStackTrace();
        }

        return victims;
    }

    /** Update victim status */
    public boolean updateVictimStatus(int victimId, String status) {
        String sql = "UPDATE victims SET status=? WHERE victim_id=?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, victimId);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** Fetch single victim by ID */
    public Victim getVictimById(int victimId) {
        String sql = "SELECT * FROM victims WHERE victim_id=?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, victimId);
            try (ResultSet rs = stmt.executeQuery()) {
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

