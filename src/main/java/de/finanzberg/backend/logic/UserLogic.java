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
    private final Cache<UUID, User> activeUsers;

    public UserLogic(Finanzberg finanzberg) {
        this.finanzberg = finanzberg;
        this.activeUsers = Caffeine.newBuilder()
                .expireAfterAccess(finanzberg.getConfig().web.sessionMaxAgeMinutes, TimeUnit.MINUTES)
                .build();
    }

    public User login(String email, String password) {
        System.out.println("mail: " + email + "   password: " + password);
        String encryptedPassword = CipherUtils.byteToString(CipherUtils.encryptAES(password, finanzberg.getConfig().key), true);
        try(PreparedStatement preparedStatement = finanzberg.getDBManager().getConnection().prepareStatement("SELECT * FROM userAccount WHERE email = ? AND password = ?")) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, encryptedPassword);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String avatar = CipherUtils.decryptAES(CipherUtils.stringToByte(resultSet.getString("avatar"), true), finanzberg.getConfig().key);
                User user = new User(email, name, password, avatar, this.finanzberg);
                this.activeUsers.put(user.getSession(), user);
                return user;
            }

        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
        return null;
    }

    public User getUser(UUID session) {
        return this.activeUsers.getIfPresent(session);
    }

    public boolean createUser(String email, String name, String password, String avatar) {
        try (PreparedStatement preparedStatement = finanzberg.getDBManager().getConnection().prepareStatement("INSERT INTO userAccount (email, name, password,avatar) VALUES (?, ?, ?,?)")) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, CipherUtils.byteToString(CipherUtils.encryptAES(password, finanzberg.getConfig().key), true));
            preparedStatement.setString(4, CipherUtils.byteToString(CipherUtils.encryptAES(avatar, finanzberg.getConfig().key), true));

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    public boolean deleteUser(String email, String password) {
        try(PreparedStatement preparedStatement = finanzberg.getDBManager().getConnection().prepareStatement("DELETE FROM userAccount WHERE email = ? AND password = ?")) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, CipherUtils.byteToString(CipherUtils.encryptAES(password, finanzberg.getConfig().key), true));
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    public boolean changeUser(String name, String password, String avatar) {
        try (PreparedStatement preparedStatement = finanzberg.getDBManager().getConnection().prepareStatement("UPDATE userAccount SET name = ?, password = ?, avatar = ? WHERE email = ?")) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, CipherUtils.byteToString(CipherUtils.encryptAES(password, finanzberg.getConfig().key), true));
            preparedStatement.setString(3, CipherUtils.byteToString(CipherUtils.encryptAES(avatar, finanzberg.getConfig().key), true));
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }
}
