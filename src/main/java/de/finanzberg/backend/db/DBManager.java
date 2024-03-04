package de.finanzberg.backend.db;

import de.finanzberg.backend.Finanzberg;
import de.finanzberg.backend.config.FinanzbergConfig;
import org.mariadb.jdbc.MariaDbDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {

    private static final Logger LOGGER = LoggerFactory.getLogger("Finanzberg-DB");

    private final MariaDbDataSource dataSource;
    private final Finanzberg finanzberg;

    public DBManager(Finanzberg finanzberg) {
        this.finanzberg = finanzberg;
        FinanzbergConfig.MySql mysql = finanzberg.getConfig().mysql;

        try {
            //Aufbau einer Connection zur Datenbank
            this.dataSource = new MariaDbDataSource(("jdbc:mariadb://%s:%s/%s?socketTimeout=30000&" +
                    "cachePrepStmts=true&useServerPrepStmts=true")
                    .formatted(mysql.host, mysql.port, mysql.database));
            this.dataSource.setUser(mysql.username);
            this.dataSource.setPassword(mysql.password);

            //Kreieren der DB wen nicht vorhanden
            LOGGER.info("Creating default tables...");
            try (Connection connection = this.dataSource.getConnection()) {
                connection.prepareStatement("CREATE TABLE IF NOT EXISTS userAccount (" +
                        "email VARCHAR(50) NOT NULL PRIMARY KEY," +
                        "name VARCHAR(50) NOT NULL," +
                        "password VARCHAR(512) NOT NULL," +
                        "avatar MEDIUMTEXT NOT NULL" +
                        ");").executeUpdate();
                connection.prepareStatement("CREATE TABLE IF NOT EXISTS bankStatement (" +
                        "bankInternalId INT," +
                        "bankname VARCHAR(50)," +
                        "date DATE NOT NULL," +
                        "description MEDIUMTEXT," +
                        "withdrawal DOUBLE(30,2)," +
                        "deposit DOUBLE(30,2)," +
                        "balance DOUBLE(30,2)," +
                        "analysedName VARCHAR(50)," +
                        "category VARCHAR(50)," +
                        "userAccount_email VARCHAR(50)," +
                        "PRIMARY KEY (bankInternalId, bankname, userAccount_email)," +
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
               // connection.prepareStatement("CREATE TABLE IF NOT EXISTS sessions()");
            }
        } catch (SQLException exception) {
            throw new RuntimeException("Error while creating default tables", exception);
        }
    }

    public Connection getConnection() throws SQLException {
        return this.dataSource.getConnection();
    }
}

