package rescuelink;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TESTDB {

    public static void main(String[] args) {
        Connection c = null;
        try {
            c = DBCONNECT.ConnectToDB(); // single connection
            if (c != null) {
                System.out.println("Connected to rescuetable successfully!");

                try (Statement stmt = c.createStatement()) {

                    // ========================
                    // 1) Display Volunteer Data
                    // ========================
                    System.out.println("\n--- Volunteer Table ---");
                    try (ResultSet rs1 = stmt.executeQuery("SELECT * FROM Volunteer")) {
                        while (rs1.next()) {
                            int id = rs1.getInt("volunteer_id");
                            String name = rs1.getString("name");
                            String location = rs1.getString("location");
                            String phone = rs1.getString("phone_no");
                            String availability = rs1.getString("availability");
                            String skill = rs1.getString("skill");
                            int points = rs1.getInt("reward_points");
                            int attended = rs1.getInt("attended_requests");

                            System.out.println(id + " | " + name + " | " + location + " | " + phone
                                    + " | " + availability + " | " + skill
                                    + " | Points: " + points + " | Requests: " + attended);
                        }
                    }

                    // =======================
                    // 2) Display Victims Data
                    // =======================
                    System.out.println("\n--- Victims Table ---");
                    try (ResultSet rs2 = stmt.executeQuery("SELECT * FROM victims")) {
                        while (rs2.next()) {
                            int vid = rs2.getInt("victim_id");
                            String vname = rs2.getString("name");
                            String vlocation = rs2.getString("location");
                            String vcondition = rs2.getString("condition");
                            String incident = rs2.getString("incident_type");
                            String severity = rs2.getString("severity");
                            int people = rs2.getInt("people_affected");
                            String immediate = rs2.getBoolean("immediate_rescue") ? "Yes" : "No";
                            String reportedAt = rs2.getString("reported_at");

                            System.out.println(vid + " | " + vname + " | " + vlocation + " | " + vcondition
                                    + " | " + incident + " | " + severity
                                    + " | People: " + people
                                    + " | Immediate: " + immediate
                                    + " | Reported: " + reportedAt);
                        }
                    }

                    // =====================
                    // 3) Display Badges Data
                    // =====================
                    System.out.println("\n--- Badges Table ---");
                    try (ResultSet rs3 = stmt.executeQuery("SELECT * FROM badges")) {
                        while (rs3.next()) {
                            int bid = rs3.getInt("badge_id");
                            String bname = rs3.getString("name");
                            String desc = rs3.getString("description");

                            System.out.println(bid + " | " + bname + " | " + desc);
                        }
                    }

                    // =================================
                    // 4) Display Volunteer-Badge Assignments
                    // =================================
                    System.out.println("\n--- Volunteer-Badges Table ---");
                    try (ResultSet rs4 = stmt.executeQuery("SELECT * FROM volunteer_badges")) {
                        while (rs4.next()) {
                            int vid = rs4.getInt("volunteer_id");
                            int bid = rs4.getInt("badge_id");
                            String awardedAt = rs4.getString("awarded_at");

                            System.out.println("VolunteerID: " + vid + " | BadgeID: " + bid + " | Awarded: " + awardedAt);
                        }
                    }

                    // ===========================
                    // 5) Display Assignments Data
                    // ===========================
                    System.out.println("\n--- Assignments Table ---");
                    try (ResultSet rs5 = stmt.executeQuery("SELECT * FROM Assignment")) {
                        while (rs5.next()) {
                            int aid = rs5.getInt("assignment_id");
                            int vid = rs5.getInt("volunteer_id");
                            int vicid = rs5.getInt("victim_id");
                            String assignedAt = rs5.getString("assigned_at");
                            String status = rs5.getString("status");

                            System.out.println("AssignmentID: " + aid + " | VolunteerID: " + vid 
                                    + " | VictimID: " + vicid + " | Status: " + status 
                                    + " | AssignedAt: " + assignedAt);
                        }
                    }

                } catch (SQLException e) {
                    System.err.println("Error executing queries: " + e.getMessage());
                }

            } else {
                System.out.println("Connection failed.");
            }
        } catch (SQLException e) {
            System.err.println("Could not connect to database: " + e.getMessage());
        } finally {
            // Close connection safely
            DBCONNECT.closeConnection();
        }
    }
}

