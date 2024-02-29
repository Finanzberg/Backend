package de.finanzberg.backend.db;

import com.google.gson.JsonObject;
import de.finanzberg.backend.Finanzberg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Collection;

public class BankStatement {

    private static final Logger LOGGER = LoggerFactory.getLogger("BankStatement");

    private final Finanzberg finanzberg;
    private int id;
    private int bankInternalId;
    private String bankName;
    private Instant date;
    private String description;
    private double withdrawal;
    private double deposit;
    private double balance;
    private String analysedName;
    private BankStatementCategory category;


    public BankStatement(Finanzberg finanzberg, int id, int bankInternalId, String bankName, Instant date, String description, double withdrawal, double deposit, double balance, String analysedName, BankStatementCategory category) {
        this.finanzberg = finanzberg;
        this.id = id;
        this.bankInternalId = bankInternalId;
        this.bankName = bankName;
        this.date = date;
        this.description = description;
        this.withdrawal = withdrawal;
        this.deposit = deposit;
        this.balance = balance;
        this.analysedName = analysedName;
        this.category = category;
    }

    public static void save(User user, Finanzberg finanzberg) {
        try (Connection connection = finanzberg.getDBManager().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO bankStatement (bankInternalId,bankname,date,description,withdrawal,deposit,balance,analysedName,category,userAccount_email) VALUES (?,?,?,?,?,?,?,?,?,?) " +
                     "ON DUPLICATE KEY UPDATE name=name");
        ) {
            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            LOGGER.error("Error while saving bank statements", exception);
        }
    }

    public void save(User user, PreparedStatement statement) throws SQLException {
        statement.setInt(1, bankInternalId);
        statement.setString(2, bankName);
        statement.setDate(3, new Date(date.toEpochMilli()));
        statement.setString(4, description);
        statement.setDouble(5, withdrawal);
        statement.setDouble(6, deposit);
        statement.setDouble(7, balance);
        statement.setString(8, analysedName);
        statement.setString(9, category.name());
        statement.setString(10, user.getEmail());
        statement.addBatch();
    }


    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("id", id);
        json.addProperty("bankInternalId", bankInternalId);
        json.addProperty("bankName", bankName);
        json.addProperty("date", date.toEpochMilli());
        json.addProperty("description", description);
        json.addProperty("withdrawal", withdrawal);
        json.addProperty("deposit", deposit);
        json.addProperty("balance", balance);
        json.addProperty("analysedName", analysedName);
        json.addProperty("category", category.name());
        return json;
    }

    @Override
    public String toString() {
        return "BankStatement{" +
                "finanzberg=" + finanzberg +
                ", id=" + id +
                ", bankInternalId=" + bankInternalId +
                ", bankName='" + bankName + '\'' +
                ", date=" + date +
                ", description='" + description + '\'' +
                ", withdrawal=" + withdrawal +
                ", deposit=" + deposit +
                ", balance=" + balance +
                ", analysedName='" + analysedName + '\'' +
                ", category=" + category +
                '}';
    }
}
