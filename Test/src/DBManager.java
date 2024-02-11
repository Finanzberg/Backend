import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBManager {
    private static DBManager dbManager = new DBManager();
    private Connection connection = null;


    private DBManager() {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("src/config.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //Aufbau einer Connection zur Datenbank
        initConnection(properties.getProperty("dbHost"), Integer.parseInt(properties.getProperty("dbPort")), properties.getProperty("dbSchema"), properties.getProperty("dbUser"), properties.getProperty("dbPw"));
        //Kreieren der DB wen nicht vorhanden
        try {
            connection.prepareStatement("CREATE TABLE IF NOT EXISTS userAccount (" +
                    "email VARCHAR(50) NOT NULL PRIMARY KEY," +
                    "name VARCHAR(50) NOT NULL," +
                    "password VARCHAR(50) NOT NULL" +
                    ");").executeUpdate();
            connection.prepareStatement("CREATE TABLE IF NOT EXISTS bankStatement (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "date DATE NOT NULL," +
                    "description VARCHAR(300)," +
                    "withdrawal DOUBLE(30,2)," +
                    "deposit DOUBLE(30,2)," +
                    "balance DOUBLE(30,2)," +
                    "analysedName VARCHAR(50)," +
                    "category VARCHAR(50)," +
                    "userAccount_email VARCHAR(50)," +
                    "FOREIGN KEY (userAccount_email) REFERENCES userAccount(email)" +
                    "ON DELETE CASCADE " +
                    ");").executeUpdate();
            connection.prepareStatement("CREATE TABLE IF NOT EXISTS budget (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "name VARCHAR(300) NOT NULL," +
                    "percentage INT," +
                    "balance DOUBLE(30,2)," +
                    "startDate DATE NOT NULL," +
                    "userAccount_email VARCHAR(50)," +
                    "FOREIGN KEY (userAccount_email) REFERENCES userAccount(email)" +
                    "ON DELETE CASCADE " +
                    ");").executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static DBManager getInstance() {
        return dbManager;
    }

    public void removeConnection() {
        if (dbManager != null && this.connection != null) {
            try {
                this.connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Connection getConnection() {
        return connection;
    }

    private void initConnection(String host, int port, String schema, String user, String pwd) {
        String dbUrl = "jdbc:mysql://" + host + ":" + port + "/" + schema;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // mit MySQL verbinden
            connection = DriverManager.getConnection(dbUrl, user, pwd);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

    }

}

