package de.finanzberg.backend.db;

import de.finanzberg.backend.Finanzberg;
import de.finanzberg.backend.config.FinanzbergConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {
    private Connection connection = null;

    private final Finanzberg finanzberg ;

    public DBManager(Finanzberg finanzberg) {
        this.finanzberg = finanzberg;
        FinanzbergConfig.MySql mysql = finanzberg.getConfig().mysql;


        //Aufbau einer Connection zur Datenbank
        initConnection(mysql.host, mysql.port, mysql.database, mysql.username, mysql.password);
        //Kreieren der DB wen nicht vorhanden
        try {
            connection.prepareStatement("CREATE TABLE IF NOT EXISTS userAccount (" +
                    "email VARCHAR(50) NOT NULL PRIMARY KEY," +
                    "name VARCHAR(50) NOT NULL," +
                    "password VARCHAR(512) NOT NULL," +
                    "avatar VARCHAR(512) NOT NULL" +
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

    public void removeConnection() {
        if (this.connection != null) {
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
        String dbUrl = "jdbc:mariadb://" + host + ":" + port + "/" + schema;
        try {
            // mit MySQL verbinden
            connection = DriverManager.getConnection(dbUrl, user, pwd);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}

