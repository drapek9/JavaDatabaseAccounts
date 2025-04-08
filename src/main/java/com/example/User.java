package com.example;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import io.github.cdimascio.dotenv.Dotenv;

public class User implements UserImplementation{
    private Integer id;
    private String name;
    private String username;
    private String password;
    protected Map<String, Action> actions;
    public boolean signedStatus = true;

    public User(Integer id, String name, String username, String password){
        this.actions = new HashMap<>();
        this.actions.put("SO", new Action("Sign Out", this::signOut));
        this.actions.put("A", new Action("List possible actions", this::getAllActions));
        this.actions.put("D", new Action("Delete own account", this::deleteOwnAccount));
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
    };

    public void getAllActions (){
        System.out.println("Funkce, které můžete volat: ");
        actions.keySet().forEach((oneKey) -> {
            System.out.println(String.format("%s - %s", oneKey, actions.get(oneKey).getDescription()));
        });
    }

    public boolean callEvent(String typeFunction){
        if (this.actions.containsKey(typeFunction)){
            this.actions.get(typeFunction).getRunFunction().run();
            return true;
        }
        return false;
    }

    public String getPassword() {
        return password;
    }
    public String getUsername() {
        return username;
    }
    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }

    public void signOut(){
        this.signedStatus = false;
    }

    public void deleteOwnAccount(){
        String response = "";
        Scanner sc = new Scanner(System.in, "Cp852");
        do{
            System.out.println("Opravdu chcete, abychom váš účet smazali? (ANO/NE)");
            response = sc.nextLine();
        } while(!(response.toLowerCase().equals("ano") || response.toLowerCase().equals("ne")));

        if (response.equals("ano")){
            Dotenv theDotEnv = Dotenv.load();
            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/app_users", "root", theDotEnv.get("PASSWORD"))){
                try (PreparedStatement prepStatement = connection.prepareStatement("DELETE FROM users WHERE user_name = ?")){
                    prepStatement.setString(1, this.username);
                    if (prepStatement.executeUpdate() > 0){
                        System.out.println("Váš účet úspěšně smazán");
                        this.signOut();
                    } else {
                        System.out.println(this.username);
                    }
                } catch (Exception exc){
                    System.out.println(exc);
                }
            } catch (SQLException exc){
                System.out.println(exc);
            }
        } else {
            System.out.println("Váš účet nebyl smazán");
        }

        
    }
}
