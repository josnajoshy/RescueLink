package rescuelink;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RescueModule {
    private final Connection con;

    public RescueModule() throws SQLException {
        con = DBCONNECT.ConnectToDB();
        con.setAutoCommit(true);
    }

    public RescueTeam getTeamByCredentials(int teamId, String teamName) {
        String sql = "SELECT * FROM rescue_teams WHERE team_id = ? AND team_name = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, teamId);
            ps.setString(2, teamName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new RescueTeam(
                        rs.getInt("team_id"),
                        rs.getString("team_name"),
                        rs.getString("specialization"),
                        rs.getString("phone_no"),
                        rs.getString("password")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Fetch all victims (for display)
    public List<Victim> getAllVictims() {
        List<Victim> victims = new ArrayList<>();
        String sql = "SELECT * FROM victims";

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                victims.add(new Victim(
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
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return victims;
    }

    public Victim getVictimById(int victimId) {
    String sql = "SELECT * FROM victims WHERE victim_id = ?";
    try (Connection con = DBCONNECT.ConnectToDB();
         PreparedStatement pst = con.prepareStatement(sql)) {
        pst.setInt(1, victimId);
        try (ResultSet rs = pst.executeQuery()) {
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
}