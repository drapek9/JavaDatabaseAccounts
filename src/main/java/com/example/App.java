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
        Scanner sc = new Scanner(System.in, "Cp852");
        do {
            System.out.println("Chcete se přihlásit (P) nebo si vytvořit účet (V)?: ");
            theAction = sc.nextLine().replaceAll("[^a-zA-Z]", "");
        // } while (!(theAction == "P" || theAction == "V"));
        } while (!(theAction.equals("P") || theAction.equals("V")));
        if (theAction.equals("V")){
            boolean result =  createUser(); // true pro nás znamená, bud vytvoren nebo jiz existoval (bylo již vypsáno) ... false je neočekávaná chyba (vypíšeme potom)
            if (!result){
                writeError("Nepodařilo se vytvořit účet!!!");
            }
        } else {
            Boolean result = signToUser();

            if (result == null){
                writeError("Účet nebo heslo se neschoduje");
            } else if (result){
                System.out.println("Byl jsi úspěšně přihlášen");
            } else {
                writeError("Něco se pokazilo");
            }
        }
        sc.close();
    }

    public static Boolean signToUser(){

        Map<String, String> logIn = new LinkedHashMap<>();
        logIn.put("user_name", null);
        logIn.put("password", null);

        Scanner sc = new Scanner(System.in, "Cp852");

        logIn.keySet().forEach((key) -> {
            System.out.print(String.format("Zadejte %s: ", key == "password" ? "heslo" : "uživatelské jméno"));
            logIn.replace(key, sc.nextLine());
        });

        sc.close();
        Dotenv dotenv = Dotenv.load();
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/app_users", "root", dotenv.get("PASSWORD"))){
            try (PreparedStatement prepStatemant = connection.prepareStatement("SELECT id, name, user_name, password FROM users WHERE user_name = ? AND password = ?")){
                prepStatemant.setString(1, logIn.get("user_name"));
                prepStatemant.setString(2, logIn.get("password"));

                ResultSet selectResult = prepStatemant.executeQuery();

                System.out.println(selectResult);

                int countResults = 0;
                while (selectResult.next()){
                    countResults ++;
                }

                if (countResults == 0){
                    return null;
                }
            }
        } catch (SQLException exc){
            return false;
        }

        return true;
    }

    public static boolean createUser(){

        Map<String, String> reqTypeInf = new LinkedHashMap<>();

        reqTypeInf.put("name", null);
        reqTypeInf.put("user_name", null);
        reqTypeInf.put("password", null);
        Dotenv dotenv = Dotenv.load();
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/app_users", "root", dotenv.get("PASSWORD"))){
            try (PreparedStatement prepStatement = connection.prepareStatement("INSERT INTO users(name, user_name, password) VALUES(?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)){

                Scanner sc = new Scanner(System.in, "Cp852");

                reqTypeInf.keySet().forEach((key) -> {
                    System.out.print(String.format("Enter information for %s: ", key));
                    do {
                        reqTypeInf.replace(key, sc.nextLine());
                    } while (reqTypeInf.get(key) == null || reqTypeInf.get(key) == "");
                });
                sc.close();

                User firstUser = new User().setName(reqTypeInf.get("name")).setUsername(reqTypeInf.get("user_name")).setPassword(reqTypeInf.get("password"));
                
                prepStatement.setString(1, firstUser.getName());
                prepStatement.setString(2, firstUser.getUsername());
                prepStatement.setString(3, firstUser.getPassword());

                if (prepStatement.executeUpdate() > 0){
                    try (ResultSet ids = prepStatement.getGeneratedKeys()){
                        ids.next();
                        firstUser.setId(ids.getInt(1));
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
