package rescuelink;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DonationDAO {
    private final Connection con;

    public DonationDAO() {
        try {
            con = DBCONNECT.ConnectToDB();
        } catch (SQLException e) {
            throw new RuntimeException("DB connection failed: " + e.getMessage());
        }
    }

    // Register new donation
    public boolean registerDonation(Donation donation) {
        String sql = "INSERT INTO donations (donor_name, type, amount, status) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, donation.getDonorName());
            pst.setString(2, donation.getType());
            pst.setDouble(3, donation.getAmount());
            pst.setString(4, donation.getStatus());
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error saving donation: " + e.getMessage());
            return false;
        }
    }

    // Retrieve all donations (for Admin)
    public List<Donation> getAllDonations() {
        List<Donation> list = new ArrayList<>();
        String sql = "SELECT * FROM donations";
        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Donation(
                        rs.getInt("donation_id"),
                        rs.getString("donor_name"),
                        rs.getString("type"),
                        rs.getDouble("amount"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching donations: " + e.getMessage());
        }
        return list;
    }

    // Update donation status
    public boolean updateStatus(int donationId, String newStatus) {
        String sql = "UPDATE donations SET status = ? WHERE donation_id = ?";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, newStatus);
            pst.setInt(2, donationId);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating status: " + e.getMessage());
            return false;
        }
    }
}