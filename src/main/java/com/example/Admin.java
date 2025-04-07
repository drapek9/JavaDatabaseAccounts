package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

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
    @Override
    public boolean callEvent(String typeFunction){
        if (typeFunction.equals("DA")){
            deleteAnyAccount();
            return true;
        } else if (typeFunction.equals("R")){
            changeRole();
            return true;
        } else if (typeFunction.equals("D")){
            deleteOwnAccount();
            return true;
        }
        return false;
    }

    private void deleteAnyAccount (){
        // String delUsername = "drapek";
        Scanner sc = new Scanner(System.in, "Cp852");
        System.out.println("Zadejte uživatelské jméno:");
        String delUsername = sc.nextLine();

        Dotenv theDotenv = Dotenv.load();
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/app_users", "root", theDotenv.get("PASSWORD"))){
            try (PreparedStatement prepStatement = connection.prepareStatement("DELETE FROM users WHERE user_name = ? AND role != 'admin'")){
                prepStatement.setString(1, delUsername);

                if (prepStatement.executeUpdate() > 0){
                    System.out.println("Uživatel úspěšně odstraněn");
                } else {
                    System.out.println("Nemohli jsme najít ");
                }
            }
        } catch (SQLException exc){

        }

    }

    private void changeRole(){
        
    }
}
