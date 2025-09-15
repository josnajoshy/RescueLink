package rescuelink;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBCONNECT {

    private static final String URL = "jdbc:mysql://localhost:3306/rescuetable";
    private static final String USER = "root";          // replace with your DB username
    private static final String PASSWORD = "ANNAKU@2006";  // replace with your DB password

    private static Connection con = null;

    // Private constructor to prevent instantiation
    private DBCONNECT() { }

    // Returns a single connection instance
    public static Connection ConnectToDB() throws SQLException {
        if (con == null || con.isClosed()) {
            try {
                // Load MySQL JDBC Driver
                Class.forName("com.mysql.cj.jdbc.Driver");

                // Establish connection
                con = DriverManager.getConnection(URL, USER, PASSWORD);

                // Ensure auto-commit is ON
                con.setAutoCommit(true);

                System.out.println("✅ Database Connected Successfully: " + con.getMetaData().getURL());
            } catch (ClassNotFoundException e) {
                System.err.println("❌ MySQL JDBC Driver not found: " + e.getMessage());
            } catch (SQLException e) {
                System.err.println("❌ Failed to connect to database: " + e.getMessage());
                throw e; // re-throw to let caller handle it
            }
        }
        return con;
    }

    // Close the connection when application shuts down
    public static void closeConnection() {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
                System.out.println("✅ Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("❌ Error closing connection: " + e.getMessage());
        }
    }
}
