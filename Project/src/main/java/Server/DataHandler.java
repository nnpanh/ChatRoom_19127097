package Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataHandler {
    private Connection connection = null;

    public DataHandler() throws ClassNotFoundException, SQLException {
        String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        Class.forName(driverName);
        String url = "jdbc:sqlserver://" +"127.0.0.1"  + ":" + "3000" + ";databaseName=" + "Socket";
        connection = DriverManager.getConnection(url, "sa", "1");
        if (connection != null) {
            System.out.println("Connect to database successfully");
        }
    }

}
