package rescuelink;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VictimModule {

    private final Connection con;

    // Constructor establishes a single connection
    public VictimModule() throws SQLException {
        con = DBCONNECT.ConnectToDB();
        con.setAutoCommit(true); // ensures inserts/updates are saved immediately
    }

    // Add new victim to DB (corrected parameter order)
    public void addVictim(Victim v) {
        String sql = "INSERT INTO victims (name, location, `condition`, incident_type, severity, people_affected, immediate_rescue, status, phone_no) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, v.getName());                          // name
            ps.setString(2, v.getLocation() != null ? v.getLocation() : "Unknown"); // location
            ps.setString(3, v.getCondition());                     // condition (ENUM: Stable/Critical)
            ps.setString(4, v.getIncidentType());                  // incident_type
            ps.setString(5, v.getSeverity());                      // severity
            ps.setInt(6, v.getPeopleAffected());                   // people_affected
            ps.setBoolean(7, v.isImmediateRescue());               // immediate_rescue
            ps.setString(8, v.getStatus() != null ? v.getStatus() : "Pending"); // status
            ps.setString(9, v.getPhone());                         // phone_no

            int rows = ps.executeUpdate();
            System.out.println("Victim added successfully! Rows affected: " + rows);

        } catch (SQLException e) {
            System.err.println("Error inserting victim: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Update victim status
    public boolean updateVictimStatus(int victimId, String newStatus) {
        String sql = "UPDATE victims SET status = ? WHERE victim_id = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setInt(2, victimId);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Victim status updated!");
                return true;
            } else {
                System.out.println("No victim found with ID " + victimId);
                return false;
            }

        } catch (SQLException e) {
            System.err.println("Error updating victim status: " + e.getMessage());
            return false;
        }
    }

    // Fetch single victim by phone
    public Victim getVictimByPhone(String phone) {
        String sql = "SELECT * FROM victims WHERE phone_no = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, phone);
            ResultSet rs = ps.executeQuery();
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
        } catch (SQLException e) {
            System.err.println("Error fetching victim by phone: " + e.getMessage());
        }
        return null;
    }

    // Fetch all victims
    public List<Victim> getAllVictims() {
        List<Victim> victims = new ArrayList<>();
        String sql = "SELECT * FROM victims";

        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

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
            System.err.println("Error fetching victims: " + e.getMessage());
        }
        return victims;
    }

    // Fetch single victim by ID
    public Victim getVictimById(int victimId) {
        String sql = "SELECT * FROM victims WHERE victim_id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, victimId);
            ResultSet rs = ps.executeQuery();
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
        } catch (SQLException e) {
            System.err.println("Error fetching victim by ID: " + e.getMessage());
        }
        return null;
    }

    // Close the connection
    public void closeConnection() {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
}