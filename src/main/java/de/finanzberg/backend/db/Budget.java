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
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Collection;

public class Budget {

    private static final Logger LOGGER = LoggerFactory.getLogger("BankStatement");

    private final Finanzberg finanzberg;
    private String name;
    private int monthlyBalance;
    private double balance;
    private Instant startDate;

    public Budget(Finanzberg finanzberg, String name, int monthlyBalance,double balance , Instant startDate) {
        this.finanzberg = finanzberg;
        this.name = name;
        this.monthlyBalance = monthlyBalance;
        this.balance = balance;
        this.startDate = startDate;
    }

    public static void save(User user, Finanzberg finanzberg, Budget budget) {
        try (Connection connection = finanzberg.getDBManager().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO budget (name,monthlyBalance,balance,startDate, userAccount_email) VALUES (?,?,?,?,?) " +
                     "ON DUPLICATE KEY UPDATE userAccount_email=userAccount_email");
        ) {
            budget.save(user, preparedStatement);
            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            LOGGER.error("Error while saving bank statements", exception);
        }
    }

    public void save(User user, PreparedStatement statement) throws SQLException {
        statement.setString(1, name);
        statement.setInt(2, monthlyBalance);
        statement.setDouble(3, balance);
        statement.setDate(4, new Date(startDate.toEpochMilli()));
        statement.setString(5, user.getEmail());
    }

    public void refreshBalance() {
        LocalDate start = LocalDate.ofInstant(startDate, ZoneOffset.UTC);
        LocalDate end = LocalDate.now();
        int months = start.until(end).getMonths() + 1;
        balance = monthlyBalance * months;
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("name", name);
        json.addProperty("monthlyBalance", monthlyBalance);
        json.addProperty("balance",balance);
        json.addProperty("startDate",startDate.getEpochSecond());
        return json;
    }

    @Override
    public String toString() {
        return "Budget{" +
                "finanzberg=" + finanzberg +
                ", name='" + name + '\'' +
                ", monthlyBalance=" + monthlyBalance +
                ", balance=" + balance +
                ", startDate=" + startDate +
                '}';
    }
}
