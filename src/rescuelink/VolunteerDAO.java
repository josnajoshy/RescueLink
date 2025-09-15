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
            throw new RuntimeException("❌ Database connection failed!", e);
        }
    }

    /** Add a new volunteer */
    public boolean addVolunteer(Volunteer v) {
        String sql = "INSERT INTO Volunteer (name, location, phone_no, availability, skill) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, v.getName());
            pst.setString(2, v.getLocation());
            pst.setString(3, v.getPhoneNo());
            pst.setString(4, v.isAvailability() ? "Yes" : "No");
            pst.setString(5, v.getSkill());

            int rows = pst.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** Get all volunteers with their badges */
    public List<Volunteer> getVolunteerList() {
        List<Volunteer> volunteers = new ArrayList<>();
        String sql = "SELECT * FROM Volunteer";

        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Volunteer v = new Volunteer(
                        rs.getInt("volunteer_id"),
                        rs.getString("name"),
                        rs.getString("location"),
                        rs.getString("phone_no"),
                        rs.getString("skill"),
                        rs.getString("availability").equalsIgnoreCase("Yes"),
                        rs.getInt("reward_points"),
                        rs.getInt("attended_requests")
                );
                v.setBadges(getBadgesForVolunteer(v.getId()));
                volunteers.add(v);
            }

        } catch (SQLException e) {
            e.printStackTrace();
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
            e.printStackTrace();
        }

        return badges;
    }

    /** Get latest assignment status for a volunteer */
    public String getAssignmentStatus(int volunteerId) {
        String status = "No assignment";
        String sql = "SELECT status FROM Assignment WHERE volunteer_id = ? ORDER BY assigned_at DESC LIMIT 1";

        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, volunteerId);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    status = rs.getString("status");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return status;
    }

    /** Update volunteer availability */
    public boolean updateAvailability(int volunteerId, boolean available) {
        String sql = "UPDATE Volunteer SET availability=? WHERE volunteer_id=?";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, available ? "Yes" : "No");
            pst.setInt(2, volunteerId);
            int rows = pst.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
