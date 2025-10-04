package rescuelink;

import java.sql.*;
import java.util.ArrayList;

public class VictimDAO {

    /** Add a new victim */
    public boolean addVictim(Victim victim) {
        String sql = "INSERT INTO victims (name, location, `condition`, incident_type, severity, people_affected, immediate_rescue, status, phone_no) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBCONNECT.ConnectToDB();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, victim.getName());
            stmt.setString(2, victim.getLocation() != null ? victim.getLocation() : "Unknown");
            stmt.setString(3, victim.getCondition());
            stmt.setString(4, victim.getIncidentType());
            stmt.setString(5, victim.getSeverity());
            stmt.setInt(6, victim.getPeopleAffected());
            stmt.setBoolean(7, victim.isImmediateRescue());
            stmt.setString(8, victim.getStatus() != null ? victim.getStatus() : "Pending");
            stmt.setString(9, victim.getPhone());

            System.out.println("✅ Inserting victim:");
            System.out.println("Condition: " + victim.getCondition());
            System.out.println("Incident: " + victim.getIncidentType());
            System.out.println("Severity: " + victim.getSeverity());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("❌ Error inserting victim: " + e.getMessage());
            return false;
        }
    }

    /** Get all victims */
    public ArrayList<Victim> getAllVictims() {
        ArrayList<Victim> victims = new ArrayList<>();
        String sql = "SELECT * FROM victims";

        try (Connection con = DBCONNECT.ConnectToDB();
             PreparedStatement stmt = con.prepareStatement(sql);
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
                    rs.getString("status"),
                    rs.getString("phone_no")
                );
                victims.add(v);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error fetching victims: " + e.getMessage());
        }

        return victims;
    }

    /** Update victim status */
    public boolean updateVictimStatus(int victimId, String status) {
        String sql = "UPDATE victims SET status=? WHERE victim_id=?";

        try (Connection con = DBCONNECT.ConnectToDB();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setInt(2, victimId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("❌ Error updating victim status: " + e.getMessage());
            return false;
        }
    }

    /** Fetch single victim by ID */
    public Victim getVictimById(int victimId) {
        String sql = "SELECT * FROM victims WHERE victim_id=?";

        try (Connection con = DBCONNECT.ConnectToDB();
             PreparedStatement stmt = con.prepareStatement(sql)) {

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
                        rs.getString("status"),
                        rs.getString("phone_no")
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error fetching victim by ID: " + e.getMessage());
        }

        return null;
    }

    /** Fetch single victim by phone */
    public Victim getVictimByPhone(String phone) {
        String sql = "SELECT * FROM victims WHERE phone_no=?";

        try (Connection con = DBCONNECT.ConnectToDB();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, phone);
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
                        rs.getString("status"),
                        rs.getString("phone_no")
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error fetching victim by phone: " + e.getMessage());
        }

        return null;
    }
}