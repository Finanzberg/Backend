package de.finanzberg.backend.db;

import com.google.gson.JsonObject;
import de.finanzberg.backend.Finanzberg;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.Instant;
import java.util.Collection;

public class Budget {
    private final Finanzberg finanzberg;
    private int id;
    private String name;
    private int percentage;
    private double balance;
    private Instant startDate;

    public Budget(Finanzberg finanzberg, int id, String name, int percentage, double balance, Instant startDate) {
        this.finanzberg = finanzberg;
        this.id = id;
        this.name = name;
        this.percentage = percentage;
        this.balance = balance;
        this.startDate = startDate;
    }

    public static void save(User user, Finanzberg finanzberg, Collection<BankStatement> statements){
        if (statements.isEmpty()){
            return;
        }
        try {
            PreparedStatement preparedStatement = finanzberg.getDBManager().getConnection().prepareStatement("INSERT INTO bankStatement (date,description,withdrawal,deposit,balance,analysedName,category,userAccount_email) VALUES (?,?,?,?,?,?,?,?) " +
                    "ON DUPLICATE KEY UPDATE name=?, password=?, avatar=?");
            for (BankStatement statement : statements) {
                statement.save(user,preparedStatement);
            }
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save(User user, PreparedStatement preparedStatementUser) {
        try {
            PreparedStatement preparedStatement = finanzberg.getDBManager().getConnection().prepareStatement("INSERT INTO bankStatement (date,description,withdrawal,deposit,balance,analysedName,category,userAccount_email) VALUES (?,?,?,?,?,?,?,?) " +
                    "ON DUPLICATE KEY UPDATE name=?, password=?, avatar=?");
            preparedStatement.setDate(1, new Date(date.toEpochMilli()));
            preparedStatement.setString(2, description);
            preparedStatement.setDouble(3, withdrawal);
            preparedStatement.setDouble(4, deposit);
            preparedStatement.setDouble(5, balance);
            preparedStatement.setString(6, analysedName);
            preparedStatement.setString(7, category.name());
            preparedStatement.setString(7, user.getEmail());
            preparedStatement.addBatch();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("id", id);
        json.addProperty("name", name);
        json.addProperty("percentage", percentage);
        json.addProperty("balance",balance);
        json.addProperty("startDate",startDate.getEpochSecond());
        return json;
    }

    @Override
    public String toString() {
        return "Budget{" +
                "finanzberg=" + finanzberg +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", percentage=" + percentage +
                ", balance=" + balance +
                ", startDate=" + startDate +
                '}';
    }
}
