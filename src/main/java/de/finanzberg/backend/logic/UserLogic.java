package de.finanzberg.backend.logic;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import de.finanzberg.backend.Finanzberg;
import de.finanzberg.backend.db.User;
import de.finanzberg.backend.util.CipherUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class UserLogic {

    private final Finanzberg finanzberg;
    private final Cache<UUID, User> activeUsers = Caffeine.newBuilder()
            .expireAfterAccess(30, TimeUnit.MINUTES)
            .build();

    public UserLogic(Finanzberg finanzberg) {
        this.finanzberg = finanzberg;
    }

    public User login(String email, String password) {
        password = CipherUtils.byteToString(CipherUtils.encryptAES(password, finanzberg.getConfig().key), true);
        try {
            PreparedStatement preparedStatement = finanzberg.getDBManager().getConnection().prepareStatement("SELECT * FROM useraccount WHERE email = ? AND password = ?");
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                User user = new User(resultSet.getString("email"), resultSet.getString("name"), CipherUtils.decryptAES(CipherUtils.stringToByte(resultSet.getString("password"), true), finanzberg.getConfig().key), finanzberg);
                this.activeUsers.put(user.getSession(), user);
                return user;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public User getUser(UUID session) {
        return this.activeUsers.getIfPresent(session);
    }

    public boolean createUser(String email, String name, String password, String avatar) {
        try {
            PreparedStatement preparedStatement = finanzberg.getDBManager().getConnection().prepareStatement("INSERT INTO useraccount (email, name, password,avatar) VALUES (?, ?, ?,?)");
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, CipherUtils.byteToString(CipherUtils.encryptAES(password, finanzberg.getConfig().key), true));
            preparedStatement.setString(4, CipherUtils.byteToString(CipherUtils.encryptAES(avatar, finanzberg.getConfig().key), true));

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ignored) {
            return false;
        }
    }

    public User checkSession(UUID session) {
        return this.activeUsers.getIfPresent(session);
    }

    public boolean deleteUser(String email, String password) {
        try {
            PreparedStatement preparedStatement = finanzberg.getDBManager().getConnection().prepareStatement("DELETE FROM useraccount WHERE email = ? AND password = ?");
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, CipherUtils.byteToString(CipherUtils.encryptAES(password, finanzberg.getConfig().key), true));
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ignored) {
            return false;
        }
    }

    public boolean changeUser(String name, String password,String avatar) {
        try {
            PreparedStatement preparedStatement = finanzberg.getDBManager().getConnection().prepareStatement("UPDATE useraccount SET name = ?, password = ?, avatar = ? WHERE email = ?");
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, CipherUtils.byteToString(CipherUtils.encryptAES(password, finanzberg.getConfig().key), true));
            preparedStatement.setString(3, CipherUtils.byteToString(CipherUtils.encryptAES(avatar, finanzberg.getConfig().key), true));
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ignored) {
            return false;
        }
    }
}
