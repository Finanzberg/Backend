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