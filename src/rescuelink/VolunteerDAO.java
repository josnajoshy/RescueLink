package rescuelink;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VolunteerDAO {
    private final Connection con;

    public VolunteerDAO() {
        this.con = DBCONNECT.ConnectToDB();
    }

    // Add a new volunteer to the database
    public boolean addVolunteer(Volunteer v) {
        String sql = "INSERT INTO Volunteer (name, location, phone_no, skill, availability) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, v.getName());
            stmt.setString(2, v.getLocation());
            stmt.setString(3, v.getPhoneNo());
            stmt.setString(4, v.getSkill());
            stmt.setBoolean(5, v.isAvailability());
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("Error adding volunteer.");
            return false;
        }
    }

    // Fetch all volunteers as a list of Volunteer objects
    public List<Volunteer> getVolunteerList() {
        List<Volunteer> list = new ArrayList<>();
        String sql = "SELECT * FROM Volunteer";
        try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("volunteer_id");
                String name = rs.getString("name");
                String location = rs.getString("location");
                String phone = rs.getString("phone_no");
                String skill = rs.getString("skill");
                boolean available = rs.getBoolean("availability");

                Volunteer v = new Volunteer(id, name, location, phone, skill, available);
                list.add(v);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching volunteers.");
        }
        return list;
    }

    // Update availability status
    public boolean updateAvailability(int volunteerId, boolean availability) {
        String sql = "UPDATE Volunteer SET availability = ? WHERE volunteer_id = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setBoolean(1, availability);
            stmt.setInt(2, volunteerId);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("Error updating availability.");
            return false;
        }
    }
}
