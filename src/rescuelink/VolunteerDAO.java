package rescuelink;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VolunteerDAO {

    /** Get all volunteers with badges
     * @return  */
    public List<Volunteer> getVolunteerList() {
    List<Volunteer> volunteers = new ArrayList<>();
    String sql = "SELECT * FROM volunteers";

    try (Connection con = DBCONNECT.ConnectToDB();
         Statement stmt = con.createStatement();
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
        e.printStackTrace(); // Add this for full error trace
    }

    // Load badges after ResultSet is closed
    for (Volunteer v : volunteers) {
        v.setBadges(getBadgesForVolunteer(v.getId()));
    }

    return volunteers;
}

    /** Get badges for a specific volunteer */
    private List<Badge> getBadgesForVolunteer(int volunteerId) {
        List<Badge> badges = new ArrayList<>();
        String sql = "SELECT b.badge_id, b.name, b.description " +
                     "FROM badges b " +
                     "JOIN volunteer_badges vb ON b.badge_id = vb.badge_id " +
                     "WHERE vb.volunteer_id = ?";

        try (Connection con = DBCONNECT.ConnectToDB();
             PreparedStatement pst = con.prepareStatement(sql)) {

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

    /** Get latest assignment status for a volunteer
     * @param volunteerId
     * @return  */
    public String getAssignmentStatus(int volunteerId) {
        String status = "No assignment";
        String sql = "SELECT status FROM assignments WHERE volunteer_id = ? ORDER BY assigned_at DESC LIMIT 1";

        try (Connection con = DBCONNECT.ConnectToDB();
             PreparedStatement pst = con.prepareStatement(sql)) {

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

    /** Add a new volunteer
     * @param volunteer
     * @return  */
    public boolean addVolunteer(Volunteer volunteer) {
        String sql = "INSERT INTO volunteers (name, location, phone_no, skill, availability) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DBCONNECT.ConnectToDB();
             PreparedStatement pst = con.prepareStatement(sql)) {

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

    /** Volunteer login using name and phone number
     * @param name
     * @param phone
     * @return  */
    public Volunteer login(String name, String phone) {
        String sql = "SELECT * FROM volunteers WHERE name = ? AND phone_no = ?";

        try (Connection con = DBCONNECT.ConnectToDB();
             PreparedStatement pst = con.prepareStatement(sql)) {

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
        }

        return null;
    }

    /** Update volunteer availability
     * @param volunteerId
     * @param available
     * @return  */
    public boolean updateAvailability(int volunteerId, boolean available) {
        String sql = "UPDATE volunteers SET availability = ? WHERE volunteer_id = ?";

        try (Connection con = DBCONNECT.ConnectToDB();
             PreparedStatement pst = con.prepareStatement(sql)) {

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