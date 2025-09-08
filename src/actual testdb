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
                    String skill = rs1.getString("skill");

                    System.out.println(id + " | " + name + " | " + location + " | " + phone + " | " + skill);
                }

                // =====================
                // 2) Display Request Data
                // =====================
                System.out.println("\n--- Request Table ---");
                ResultSet rs2 = stmt.executeQuery("SELECT * FROM request");

                while (rs2.next()) {
                    int reqNo = rs2.getInt("request_no");
                    String userName = rs2.getString("user_name");
                    String location = rs2.getString("location");
                    String disaster = rs2.getString("disaster_type");
                    String conditions = rs2.getString("rescue_needed_conditions");
                    String status = rs2.getString("status");

                    System.out.println(reqNo + " | " + userName + " | " + location + " | " + disaster + " | " + conditions + " | " + status);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("❌ Connection failed.");
        }
    }
}
