package rescuelink;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VolunteerDAO {
    private final Connection con;

    public VolunteerDAO() {
        con = DBCONNECT.ConnectToDB();
    }

    // Add a new volunteer
    public boolean addVolunteer(Volunteer v) {
        String query = "INSERT INTO Volunteer (name, location, phone_no, availability, skill) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, v.getName());
            pst.setString(2, v.getLocation());
            pst.setString(3, v.getPhoneNo());
            pst.setString(4, v.isAvailability() ? "Yes" : "No");
            pst.setString(5, v.getSkill());

            return pst.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
        }
        return false;
    }

    // Get all volunteers with badges
    public List<Volunteer> getVolunteerList() {
        List<Volunteer> volunteers = new ArrayList<>();
        String query = "SELECT * FROM Volunteer";

        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

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
            System.err.println("SQL Error: " + e.getMessage());
        }

        return volunteers;
    }

    // Get badges for a specific volunteer
    private List<Badge> getBadgesForVolunteer(int volunteerId) {
        List<Badge> badges = new ArrayList<>();
        String query = """
            SELECT b.badge_id, b.name, b.description
            FROM badges b
            JOIN volunteer_badges vb ON b.badge_id = vb.badge_id
            WHERE vb.volunteer_id = ?
            """;

        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, volunteerId);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                badges.add(new Badge(
                    rs.getInt("badge_id"),
                    rs.getString("name"),
                    rs.getString("description")
                ));
            }

        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
        }

        return badges;
    }
}
