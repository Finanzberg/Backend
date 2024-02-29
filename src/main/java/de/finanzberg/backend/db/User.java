package de.finanzberg.backend.db;

import com.google.gson.JsonObject;
import de.finanzberg.backend.Finanzberg;
import de.finanzberg.backend.util.CipherUtils;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User {
    private final Finanzberg finanzberg;
    private final UUID session = UUID.randomUUID();
    private String email;
    private String name;
    private String password;
    private String avatar;
    private List<BankStatement> bankStatements =new ArrayList<>();

    public User(String email, String name, String password, String avatar, Finanzberg finanzberg) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.avatar = avatar;
        this.finanzberg = finanzberg;

        save();
    }

    public void save() {
        String password = CipherUtils.byteToString(CipherUtils.encryptAES(this.password, finanzberg.getConfig().key), true);
        String avatar = CipherUtils.byteToString(CipherUtils.encryptAES(this.avatar, finanzberg.getConfig().key), true);
        try {
            PreparedStatement preparedStatement = finanzberg.getDBManager().getConnection().prepareStatement("INSERT INTO userAccount (email, name, password, avatar) VALUES (?,?,?,?) " +
                    "ON DUPLICATE KEY UPDATE name=?, password=?, avatar=?");
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, password);
            preparedStatement.setString(4, avatar);
            preparedStatement.setString(5, name);
            preparedStatement.setString(6, password);
            preparedStatement.setString(7, avatar);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        BankStatement.save(this,finanzberg,bankStatements);
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getAvatar() {
        return avatar;
    }

    public UUID getSession() {
        return session;
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("email", email);
        json.addProperty("name", name);
        json.addProperty("avatar", avatar);
        return json;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", finanzberg=" + finanzberg +
                '}';
    }
}
