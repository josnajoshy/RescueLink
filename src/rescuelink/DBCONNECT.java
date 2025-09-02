package rescuelink;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBCONNECT {
    static Connection con = null;

    public static Connection ConnectToDB() {
        try {
            // Load JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to your rescuetable database
            con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/rescuetable",
                "root",
                "ANNAKU@2006" // replace with your MySQL password
            );
            System.out.println("✅ Database Connected Successfully!");
        } catch (ClassNotFoundException ex) {
            System.out.println("❌ JDBC Driver not found.");
            Logger.getLogger(DBCONNECT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            System.out.println("❌ Database Connection Failed.");
            Logger.getLogger(DBCONNECT.class.getName()).log(Level.SEVERE, null, ex);
        }
        return con;
    }
}