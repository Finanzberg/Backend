package de.finanzberg.backend.db;

import de.finanzberg.backend.Finanzberg;
import de.finanzberg.backend.util.CipherUtils;

import java.sql.PreparedStatement;
import java.util.UUID;

public class User {
    private final Finanzberg finanzberg;
    private final UUID session = UUID.randomUUID();
    private String email;
    private String name;
    private String password;


    public User(String email, String name, String password, Finanzberg finanzberg) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.finanzberg = finanzberg;

        save();
    }

    public void save() {

        String passwort = CipherUtils.byteToString(CipherUtils.encryptAES(password, finanzberg.getConfig().key), true);
        try {
            PreparedStatement preparedStatement = finanzberg.getDBManager().getConnection().prepareStatement("INSERT INTO useraccount (email, name, password) VALUES (?,?,?) " +
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

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public UUID getSession() {
        return session;
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
