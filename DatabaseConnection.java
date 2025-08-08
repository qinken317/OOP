import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/library_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found", e);
        }
    }

    // Method to test the connection
    public static void testConnection() {
        System.out.println("Testing database connection...");

        try {
            // Explicitly load driver (not always needed in newer JDBC)
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✔ JDBC Driver loaded successfully");

            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
                System.out.println("✔ Connection established to: " + URL);
                System.out.println("✔ Database: " + conn.getCatalog());
                System.out.println("✔ Server version: " + conn.getMetaData().getDatabaseProductVersion());
            }
        } catch (ClassNotFoundException e) {
            System.err.println("✖ JDBC Driver not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("✖ Connection failed!");
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            System.err.println("Message: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Main method to run the test
    public static void main(String[] args) {
        testConnection();
    }
}