package com.example;
import java.util.HashMap;
import java.util.Map;

public class User implements UserImplementation{
    private Integer id;
    private String name;
    private String username;
    private String password;
    protected Map<String, Runnable> actions;
    public boolean signedStatus = true;

    public User(Integer id, String name, String username, String password){
        this.actions = new HashMap<>();
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
    };

    public void getAllActions (){
        System.out.println("Funkce, které můžete volat");
        actions.keySet().forEach((oneKey) -> {
            System.out.println(oneKey);
        });
    }

    public boolean callEvent(String theType){
        return true;
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
        this.signedStatus = false;
    }

    public void deleteOwnAccount(){
        
    }
}
