package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import io.github.cdimascio.dotenv.Dotenv;

public class Admin extends User {
    public Admin(Integer id, String name, String username, String password){
        super(id, name, username, password);
    }

    public void getAllUsers(){
        Dotenv dotenv = Dotenv.load();
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/app_users", "root", dotenv.get("PASSWORD"))){
            try (PreparedStatement prepStatement = connection.prepareStatement("SELECT user_name FROM users")){
                ResultSet theResult = prepStatement.executeQuery();
                System.out.println("Všichni uživatelé:");
                while (theResult.next()) {
                    System.out.println(theResult.getString(1));
                }
            }
        } catch (SQLException exc) {

        }
    }

    public void deleteAnyAccount (){

    }

    public void changeRole(){
        
    }
}
