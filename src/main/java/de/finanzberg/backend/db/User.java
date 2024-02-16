package de.finanzberg.backend.db;

import de.finanzberg.backend.Finanzberg;
import de.finanzberg.backend.util.CipherUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class User {
    private String email;
    private String name;
    private String password;
    private Finanzberg finanzberg;

    public User(String email, String name, String password, Finanzberg finanzberg) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.finanzberg = finanzberg;
    }

    public void save() {
        String passwort = CipherUtils.byteToString(CipherUtils.encryptAES(password, finanzberg.getConfig().key), true);
        try {
            PreparedStatement preparedStatement = finanzberg.getDBManager().getConnection().prepareStatement("INSERT INTO user (email, name, password) VALUES (?,?,?,?) " +
                    "ON DUPLICATE KEY UPDATE name=?, password=?");
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, passwort);
            preparedStatement.setString(4, name);
            preparedStatement.setString(5, passwort);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
