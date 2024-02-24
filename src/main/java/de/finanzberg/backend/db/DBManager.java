package de.finanzberg.backend.db;

import de.finanzberg.backend.Finanzberg;
import de.finanzberg.backend.config.FinanzbergConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {

    private static final Logger LOGGER = LoggerFactory.getLogger("Finanzberg-DB");
    private final Finanzberg finanzberg;
    private Connection connection = null;

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
                    "avatar MEDIUMTEXT NOT NULL" +
                    ");").executeUpdate();
            connection.prepareStatement("CREATE TABLE IF NOT EXISTS bankStatement (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "bankInternalId INT PRIMARY KEY," +
                    "bankname VARCHAR(50) PRIMARY KEY," +
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
                    "monthlyBalance INT," +
                    "balance DOUBLE(30,2)," +
                    "startDate DATE NOT NULL," +
                    "userAccount_email VARCHAR(50)," +
                    "FOREIGN KEY (userAccount_email) REFERENCES userAccount(email)" +
                    "ON DELETE CASCADE " +
                    ");").executeUpdate();
        } catch (SQLException exception) {
            LOGGER.error("Error while creating tables", exception);
        }
    }

    public void removeConnection() {
        if (this.connection != null) {
            try {
                this.connection.close();
            } catch (SQLException exception) {
                LOGGER.error("Error while closing connection", exception);
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
        } catch (SQLException exception) {
            LOGGER.error("Error while connecting to database", exception);
        }
    }
}

