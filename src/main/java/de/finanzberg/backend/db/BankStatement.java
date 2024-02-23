package de.finanzberg.backend.db;

import com.google.gson.JsonObject;
import de.finanzberg.backend.Finanzberg;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.Instant;
import java.util.Collection;

public class BankStatement {
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

    public static void save(User user, Finanzberg finanzberg, Collection<BankStatement> statements){
        if (statements.isEmpty()){
            return;
        }
        try {
            PreparedStatement preparedStatement = finanzberg.getDBManager().getConnection().prepareStatement("INSERT INTO bankStatement (bankInternalId,bankname,date,description,withdrawal,deposit,balance,analysedName,category,userAccount_email) VALUES (?,?,?,?,?,?,?,?,?,?) " +
                    "ON DUPLICATE KEY UPDATE name=name");
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
            PreparedStatement preparedStatement = finanzberg.getDBManager().getConnection().prepareStatement("INSERT INTO bankStatement (bankInternalId,bankname,date,description,withdrawal,deposit,balance,analysedName,category,userAccount_email) VALUES (?,?,?,?,?,?,?,?,?,?) " +
                    "ON DUPLICATE KEY UPDATE name=name");

            preparedStatement.setInt(1, bankInternalId);
            preparedStatement.setString(2, bankName);
            preparedStatement.setDate(3, new Date(date.toEpochMilli()));
            preparedStatement.setString(4, description);
            preparedStatement.setDouble(5, withdrawal);
            preparedStatement.setDouble(6, deposit);
            preparedStatement.setDouble(7, balance);
            preparedStatement.setString(8, analysedName);
            preparedStatement.setString(9, category.name());
            preparedStatement.setString(10, user.getEmail());
            preparedStatement.addBatch();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("id", id);
        json.addProperty("bankInternalId",bankInternalId);
        json.addProperty("bankName",bankName);
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
