package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

import io.github.cdimascio.dotenv.Dotenv;

public class App {
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex){
            ex.printStackTrace();
        }
        String theAction = "";

        UserImpl userAccount = null;

        String userResponse = "";
        Scanner sc = new Scanner(System.in, "Cp852");
        do {
            if (userAccount != null && !userAccount.signedStatus){
                userAccount = null;
            }

            if (userAccount == null){
                do {
                    System.out.println("Chcete se přihlásit (P) nebo si vytvořit účet (V)?: ");
                    theAction = sc.nextLine();
                    // sc.close();
                } while (!(theAction.equals("P") || theAction.equals("V")));
                if (theAction.equals("V")){
                    boolean result =  createUser(); // true pro nás znamená, bud vytvoren nebo jiz existoval (bylo již vypsáno) ... false je neočekávaná chyba (vypíšeme potom)
                    if (!result){
                        writeError("Nepodařilo se vytvořit účet!!!");
                    }
                } else {
                    userAccount = signToUser();
                    if (userAccount == null){
                        writeError("Účet nebo heslo se neshoduje");
                    } else {
                        System.out.println("Byl jsi úspěšně přihlášen");
                    } 
            }
                
            } else {
                System.out.println("---Příkazem 'A' můžete získat seznam možných Akcí---");
                userResponse = sc.nextLine();
                boolean result = userAccount.callEvent(userResponse);
                if (result){
                    System.out.println("---success---");
                } else {
                    System.out.println("---mistake---");
                }
                // sc.close();
            }

        } while (!userResponse.equals("END"));

    }

    public static UserImpl signToUser(){

        Map<String, String> logIn = new LinkedHashMap<>();
        logIn.put("user_name", null);
        logIn.put("password", null);

        Scanner sc = new Scanner(System.in, "Cp852");

        logIn.keySet().forEach((key) -> {
            System.out.print(String.format("Zadejte %s: ", key == "password" ? "heslo" : "uživatelské jméno"));
            logIn.replace(key, sc.nextLine());
        });

        // sc.close();
        Dotenv dotenv = Dotenv.load();

        UserImpl finalUser = null;
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/app_users", "root", dotenv.get("PASSWORD"))){
            try (PreparedStatement prepStatemant = connection.prepareStatement("SELECT id, name, user_name, password, role FROM users WHERE user_name = ? AND password = ?")){
                prepStatemant.setString(1, logIn.get("user_name"));
                prepStatemant.setString(2, logIn.get("password"));

                Map<String, String> allInf = new LinkedHashMap<>();

                ResultSet selectResult = prepStatemant.executeQuery();


                int countResults = 0;
                while (selectResult.next()){
                    allInf.put("id", selectResult.getString(1));
                    allInf.put("name", selectResult.getString(2));
                    allInf.put("user_name", selectResult.getString(3));
                    allInf.put("password", selectResult.getString(4));
                    allInf.put("role", selectResult.getString(5));
                    countResults ++;
                }


                if (countResults == 0){
                    return null;
                } else {
                    if (allInf.get("role").equals("admin")){
                        finalUser = new Admin(Integer.parseInt(allInf.get("id")), allInf.get("name"), allInf.get("user_name"), allInf.get("password"));
                    } else {
                        finalUser = new NormalUser(Integer.parseInt(allInf.get("id")), allInf.get("name"), allInf.get("user_name"), allInf.get("password"));
                    }
                }
            }
        } catch (SQLException exc){
            return null;
        }

        return finalUser;
    }

    public static boolean createUser(){

        Map<String, String> reqTypeInf = new LinkedHashMap<>();

        reqTypeInf.put("name", null);
        reqTypeInf.put("user_name", null);
        reqTypeInf.put("password", null);
        Dotenv dotenv = Dotenv.load();
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/app_users", "root", dotenv.get("PASSWORD"))){
            try (PreparedStatement prepStatement = connection.prepareStatement("INSERT INTO users(name, user_name, password, role) VALUES(?, ?, ?, 'normal')", PreparedStatement.RETURN_GENERATED_KEYS)){

                Scanner sc = new Scanner(System.in, "Cp852");

                reqTypeInf.keySet().forEach((key) -> {
                    System.out.print(String.format("Enter information for %s: ", key));
                    do {
                        reqTypeInf.replace(key, sc.nextLine());
                    } while (reqTypeInf.get(key) == null || reqTypeInf.get(key) == "");
                });

                
                prepStatement.setString(1, reqTypeInf.get("name"));
                prepStatement.setString(2, reqTypeInf.get("user_name"));
                prepStatement.setString(3, reqTypeInf.get("password"));

                if (prepStatement.executeUpdate() > 0){
                    try (ResultSet ids = prepStatement.getGeneratedKeys()){
                        ids.next();
                    }
                } else {
                    return false;
                }
            } catch (SQLIntegrityConstraintViolationException exc) {
                writeError("Uživatelské jméno již existuje!");
                return true;
            }
        } catch (SQLException exc){
            return false;
        }

        System.out.println("Účet úspěšně vytvořen!");
        return true;
    }

    public static void writeError(String errorText){
        System.out.println(errorText);
    }
}
