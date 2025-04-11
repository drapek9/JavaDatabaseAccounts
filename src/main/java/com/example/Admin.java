package com.example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class Admin extends UserImpl {
    public Admin(Integer id, String name, String username, String password){
        super(id, name, username, password);
        this.actions.put("DA", new Action("Delete any account (can't delete admin)", this::deleteAnyAccount));
        this.actions.put("R", new Action("Change someones role (can't change admin's role)", this::changeRole));
        this.actions.put("L", new Action("Get all usernames", this::getAllUsers));
    }

    public void getAllUsers(){
        Map<Integer, Object> myValues = new HashMap<>();
        Map<Integer, Object> myValuesGet = new HashMap<>();
        myValuesGet.put(1, "String");
        myValuesGet.put(2, "Integer");

        List<List<String>> result =  (List<List<String>>)ConnectDatabase.connectToDatabase("SELECT user_name, id FROM users", myValues, "SELECT", myValuesGet);
        System.out.println(result);
    }

    private void deleteAnyAccount (){
        Scanner sc = new Scanner(System.in, "Cp852");
        System.out.println("Zadejte uživatelské jméno:");
        String delUsername = sc.nextLine();

        // Dotenv theDotenv = Dotenv.load();

        Map<Integer, Object> myValues = new HashMap<>();
        myValues.put(1, delUsername);
        
        boolean result =  (boolean)ConnectDatabase.connectToDatabase("DELETE FROM users WHERE user_name = ? AND role != 'admin'", myValues, "DELETE", null);

        if (result){
            System.out.println("Uživatel úspěšně odstraněn");
        } else {
            System.out.println("Nepodařilo se odstranit žádného uživatele");
        }

    }

    private void changeRole(){
        Scanner sc = new Scanner(System.in, "Cp852");
        
        System.out.print("Zadejte uživatelské jméno: ");
        String userName = sc.nextLine();
        System.out.print("Zadejte novou roli: ");
        String newRole = sc.nextLine();

        Map<Integer, Object> myValues = new HashMap<>();
        myValues.put(1, newRole);
        myValues.put(2, userName);

        boolean result = (boolean)ConnectDatabase.connectToDatabase("UPDATE users SET role = ? WHERE user_name = ? AND role != 'admin'", myValues, "UPDATE", null);

        if (result){
            System.out.println(String.format("Uživateli %s byla úspěšně změněna role na %s.", userName, newRole));
        } else {
            System.out.println(String.format("Uživatel %s nebyl nalezen nebo nemohl být odstraněn!", userName));
        }

    }
}
