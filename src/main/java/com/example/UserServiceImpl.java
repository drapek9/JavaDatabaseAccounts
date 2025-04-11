package com.example;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import io.github.cdimascio.dotenv.Dotenv;

public class UserServiceImpl extends UnicastRemoteObject implements UserService {

    protected UserServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public User logIn(String username, char[] password) throws RemoteException {
        Dotenv theDotEnv = Dotenv.load();
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/app_users", "root", theDotEnv.get("PASSWORD"))){
            try (PreparedStatement prepStatement = connection.prepareStatement("SELECT id, name, user_name, password FROM users WHERE user_name = ?")){
                prepStatement.setString(1, username);
                try (ResultSet theResult = prepStatement.executeQuery()){
                    if (theResult.next() && theResult.getString(4) == new String(password)){
                        return new UserImpl(theResult.getInt(1), theResult.getString(2), theResult.getString(3), "");
                    } else {
                        return null;
                    }
                }
            }
        } catch (SQLException exc){

        }
        throw new UnsupportedOperationException("Unimplemented method 'logIn'");
    }
    
}
