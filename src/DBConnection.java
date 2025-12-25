import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DBConnection - Database connection manager for Apache Derby
 * @author acv
 */
public class DBConnection {
    
    private static Connection connection;
    private static final String user = "java";
    private static final String password = "java";
    private static final String database = "jdbc:derby://localhost:1527/CourseSchedulerDB";
    
    // Get database connection (creates if doesn't exist)
    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(database, user, password);
                
                var md = connection.getMetaData();
                var rs = md.getTables(null, "JAVA", null, null);
                
                System.out.println("TABLES IN JAVA:");
                while (rs.next()) {
                    System.out.println(" - " + rs.getString("TABLE_NAME"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Could not open database.");
                System.exit(1);
            }
        }
        return connection;
    }
}
