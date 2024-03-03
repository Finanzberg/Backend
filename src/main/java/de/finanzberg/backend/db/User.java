package de.finanzberg.backend.db;

import com.google.gson.JsonObject;
import de.finanzberg.backend.Finanzberg;
import de.finanzberg.backend.util.CipherUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.UUID;

public class User {
    private final Finanzberg finanzberg;
    private final UUID session = UUID.randomUUID();
    private String email;
    private String name;
    private String password;
    private String avatar;

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
        try (Connection connection = finanzberg.getDBManager().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO userAccount (email, name, password, avatar) VALUES (?,?,?,?) " +
                     "ON DUPLICATE KEY UPDATE name=?, password=?, avatar=?");
        ) {
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

    public JsonObject toJson(boolean withAvatar) {
        JsonObject json = new JsonObject();
        json.addProperty("email", email);
        json.addProperty("name", name);
        if (withAvatar) {
            json.addProperty("avatar", avatar);
        }
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

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
