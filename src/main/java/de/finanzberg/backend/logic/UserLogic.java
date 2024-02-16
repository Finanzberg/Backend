package de.finanzberg.backend.logic;

import de.finanzberg.backend.Finanzberg;
import de.finanzberg.backend.db.User;
import de.finanzberg.backend.util.CipherUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserLogic {
    private final Finanzberg finanzberg;

    public UserLogic(Finanzberg finanzberg) {
        this.finanzberg = finanzberg;
    }


    public User login(String email, String passwort){
       passwort =  CipherUtils.byteToString(CipherUtils.encryptAES(passwort, finanzberg.getConfig().key), true);
        try {
            PreparedStatement preparedStatement = finanzberg.getDBManager().getConnection().prepareStatement("SELECT * FROM useraccount WHERE email = ? AND password = ?");
            preparedStatement.setString(1,email);
            preparedStatement.setString(2,passwort);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                return new User(resultSet.getString("email"),resultSet.getString("name"),  CipherUtils.decryptAES(CipherUtils.stringToByte(resultSet.getString("password"),true), finanzberg.getConfig().key),finanzberg);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
