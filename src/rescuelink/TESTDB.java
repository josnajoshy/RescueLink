package rescuelink;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class TESTDB {
    public static void main(String[] args) {
        Connection c = DBCONNECT.ConnectToDB();
        if (c != null) {
            System.out.println("✅ Connected to rescuetable successfully!");

            try {
                Statement stmt = c.createStatement();

                // ========================
                // 1) Display Volunteer Data
                // ========================
                System.out.println("\n--- Volunteer Table ---");
                ResultSet rs1 = stmt.executeQuery("SELECT * FROM Volunteer");

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

                // =======================
                // 2) Display Victims Data
                // =======================
                System.out.println("\n--- Victims Table ---");
                ResultSet rs2 = stmt.executeQuery("SELECT * FROM victims");

                while (rs2.next()) {
                    int vid = rs2.getInt("victim_id");
                    String vname = rs2.getString("name");
                    String vlocation = rs2.getString("location");
                    String vcondition = rs2.getString("condition"); // escaped in SQL
                    String incident = rs2.getString("incident_type");
                    String severity = rs2.getString("severity");
                    int people = rs2.getInt("people_affected");
                    boolean immediate = rs2.getBoolean("immediate_rescue");
                    String reportedAt = rs2.getString("reported_at");

                    System.out.println(vid + " | " + vname + " | " + vlocation + " | " + vcondition
                            + " | " + incident + " | " + severity 
                            + " | People: " + people 
                            + " | Immediate: " + immediate 
                            + " | Reported: " + reportedAt);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("❌ Connection failed.");
        }
    }
}
