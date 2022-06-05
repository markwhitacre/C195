package Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 * The type Db connection.
 */
public class DBConnection {

    // JDBC URL parts
    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String ipAddress = "//wgudb.ucertify.com:3306/";
    private static final String dbName = "WJ06CsD";

    //JDBC URL, forcing time zone to server time
    private static final String jdbcURL = protocol + vendor + ipAddress + dbName + "?connectionTimeZone=SERVER";

    // Driver Interface Reference
    private static final String MYSQLJDBCDriver = "com.mysql.cj.jdbc.Driver";
    private static Connection conn = null;

    // Username
    private static final String userName = "U06CsD";

    // Password
    private static final String password = "53688724432";

    /**
     * Start connection at launch, catches and displays errors
     *
     * @return the connection
     */
    public static Connection startConnection() {
        try {
            Class.forName(MYSQLJDBCDriver);
            conn = DriverManager.getConnection(jdbcURL, userName, password);
            System.out.println("Connection successful");

        }
        catch(ClassNotFoundException | SQLException e)
        {
            //System.out.println(e.getMessage()); commented out as suggested in second webinar
            e.printStackTrace();
        }

        return conn;

    }

    /**
     * Get connection during system operation, instead of starting a new one every time a CRUD function needs to execute
     *
     * @return the connection
     */
    public static Connection getConnection(){
        return conn;
    }

    /**
     * Closes connection at the end of the program.
     */
    public static void closeConnection() {

        try {
            conn.close();
            System.out.println("Connection closed!");
        }
        catch(SQLException e)
        {
            //System.out.println(e.getMessage());
        }
    }

}
