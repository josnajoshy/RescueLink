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
                ResultSet rs = stmt.executeQuery("SELECT * FROM Volunteer");

                while (rs.next()) {
                    int id = rs.getInt("volunteer_id");
                    String name = rs.getString("name");
                    String location = rs.getString("location");
                    String phone = rs.getString("phone_no");
                    String skill = rs.getString("skill");

                    System.out.println(id + " | " + name + " | " + location + " | " + phone + " | " + skill);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("❌ Connection failed.");
        }
    }
}
