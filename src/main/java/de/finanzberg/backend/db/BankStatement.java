package de.finanzberg.backend.db;

import com.google.gson.JsonObject;
import de.finanzberg.backend.Finanzberg;
import de.finanzberg.backend.util.CipherUtils;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.Instant;
import java.util.Collection;
import java.util.UUID;

public class BankStatement {
    private final Finanzberg finanzberg;
    private int id;
    private Instant date;
    private String description;
    private double withdrawal;
    private double deposit;
    private double balance;
    private String analysedName;
    private BankStatementCategory category;


    public BankStatement(Finanzberg finanzberg, int id, Instant date, String description, double withdrawal, double deposit, double balance, String analysedName, BankStatementCategory category) {
        this.finanzberg = finanzberg;
        this.id = id;
        this.date = date;
        this.description = description;
        this.withdrawal = withdrawal;
        this.deposit = deposit;
        this.balance = balance;
        this.analysedName = analysedName;
        this.category = category;
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
        json.addProperty("date", date.toEpochMilli());
        json.addProperty("description", description);
        json.addProperty("withdrawal",withdrawal);
        json.addProperty("deposit",deposit);
        json.addProperty("balance",balance);
        json.addProperty("analysedName",analysedName);
        json.addProperty("category",category.name());
        return json;
    }

    @Override
    public String toString() {
        return "BankStatement{" +
                "finanzberg=" + finanzberg +
                ", id=" + id +
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
