package com.example;
import java.util.HashMap;
import java.util.Map;

public class User {
    private Integer id;
    private String name;
    private String username;
    private String password;
    private Map<String, String> options;

    public User(Integer id, String name, String username, String password){
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
    };

    public String getPassword() {
        return password;
    }
    public User setPassword(String password) {
        this.password = password;
        return this;
    }
    public String getUsername() {
        return username;
    }
    public User setUsername(String username) {
        this.username = username;
        return this;
    }
    public int getId() {
        return id;
    }
    public User setId(int id) {
        this.id = id;
        return this;
    }
    
    public String getName() {
        return name;
    }
    public User setName(String name) {
        this.name = name;
        return this;
    }

    public void signOut(){
        System.out.println("User loged out");
    }

    public void deleteOwnAccount(){
        
    }

    // public void setPermissions(){
    //     if (id == null){
    //         this.options = new HashMap<>();
    //         this.options.put("V", "vytvořit");
    //         this.options.put("P", "Přihlásit");
    //         this.options.put("L", "List uživatelů");
    //     } else {
    //         this.options = new HashMap<>();
    //         this.options.put("O", "Odhlásit se");
    //         this.options.put("L", "List uživatelů");
    //     }
    // }

    // public void writePermissions(){
    //     System.out.println("Možnosti akcí");
    //     this.options.keySet().forEach((oneKey) -> {
    //         System.out.println(String.format("%s - %s", oneKey, this.options.get(oneKey)));
    //     });
    // }
}
