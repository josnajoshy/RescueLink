package rescuelink;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VolunteerDAO {

    private final Connection con;

    public VolunteerDAO() {
        try {
            con = DBCONNECT.ConnectToDB();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to DB", e);
        }
    }

    /** Get all volunteers with badges */
    public List<Volunteer> getVolunteerList() {
        List<Volunteer> volunteers = new ArrayList<>();
        String sql = "SELECT * FROM volunteers";

        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Volunteer v = new Volunteer(
                        rs.getInt("volunteer_id"),
                        rs.getString("name"),
                        rs.getString("location"),
                        rs.getString("phone_no"),
                        rs.getString("skill"),
                        rs.getBoolean("availability"),
                        rs.getInt("reward_points"),
                        rs.getInt("attended_requests")
                );
                volunteers.add(v);
            }

        } catch (SQLException e) {
            System.err.println("Something went wrong: " + e.getMessage());
            e.printStackTrace();
        }

        // Load badges after ResultSet is closed
        for (Volunteer v : volunteers) {
            v.setBadges(getBadgesForVolunteer(v.getId()));
        }

        return volunteers;
    }

    /** Get count of rescued victims assigned to a volunteer */
    public int getRescuedVictimCount(int volunteerId) {
        String sql = "SELECT COUNT(*) FROM victims v " +
                     "JOIN assignments a ON v.victim_id = a.victim_id " +
                     "WHERE a.volunteer_id = ? AND v.status = 'Rescued'";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, volunteerId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /** Get badges for a specific volunteer */
    private List<Badge> getBadgesForVolunteer(int volunteerId) {
        List<Badge> badges = new ArrayList<>();
        String sql = "SELECT b.badge_id, b.name, b.description " +
                     "FROM badges b " +
                     "JOIN volunteer_badges vb ON b.badge_id = vb.badge_id " +
                     "WHERE vb.volunteer_id = ?";

        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, volunteerId);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    badges.add(new Badge(
                            rs.getInt("badge_id"),
                            rs.getString("name"),
                            rs.getString("description")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Something went wrong: " + e.getMessage());
        }

        return badges;
    }

    /** Get latest assignment status for a volunteer */
    public String getAssignmentStatus(int volunteerId) {
        String status = "No assignment";
        String sql = "SELECT status FROM assignments WHERE volunteer_id = ? ORDER BY assigned_at DESC LIMIT 1";

        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, volunteerId);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    status = rs.getString("status");
                }
            }
        } catch (SQLException e) {
            System.err.println("Something went wrong: " + e.getMessage());
        }

        return status;
    }

    /** Add a new volunteer */
    public boolean addVolunteer(Volunteer volunteer) {
        String sql = "INSERT INTO volunteers (name, location, phone_no, skill, availability) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, volunteer.getName());
            pst.setString(2, volunteer.getLocation());
            pst.setString(3, volunteer.getPhoneNo());
            pst.setString(4, volunteer.getSkill());
            pst.setBoolean(5, volunteer.isAvailability());

            int rows = pst.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.err.println("Failed to add volunteer: " + e.getMessage());
            return false;
        }
    }

    /** Volunteer login using name and phone number */
    public Volunteer login(String name, String phone) {
        String sql = "SELECT * FROM volunteers WHERE name = ? AND phone_no = ?";

        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, name);
            pst.setString(2, phone);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Volunteer v = new Volunteer(
                            rs.getInt("volunteer_id"),
                            rs.getString("name"),
                            rs.getString("location"),
                            rs.getString("phone_no"),
                            rs.getString("skill"),
                            rs.getBoolean("availability"),
                            rs.getInt("reward_points"),
                            rs.getInt("attended_requests")
                    );
                    v.setBadges(getBadgesForVolunteer(v.getId()));
                    return v;
                }
            }
        } catch (SQLException e) {
            System.err.println("Login error: " + e.getMessage());
        }

        return null;
    }

    /** Update volunteer availability */
    public boolean updateAvailability(int volunteerId, boolean available) {
        String sql = "UPDATE volunteers SET availability = ? WHERE volunteer_id = ?";

        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setBoolean(1, available);
            pst.setInt(2, volunteerId);

            int rows = pst.executeUpdate();
            System.out.println("Updated availability for volunteer ID " + volunteerId + " to " + available);
            return rows > 0;

        } catch (SQLException e) {
            System.err.println("Error updating availability: " + e.getMessage());
            return false;
        }
    }
}