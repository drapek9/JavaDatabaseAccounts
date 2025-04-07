package com.example;

public class NormalUser extends User {
    public NormalUser(Integer id, String name, String username, String password){
        super(id, name, username, password);
        actions.put("D", this::deleteOwnAccount);
        actions.put("A", this::getAllActions);
    }
    @Override
    public boolean callEvent(String typeFunction){
        if (this.actions.containsKey(typeFunction)){
            this.actions.get(typeFunction).run();
            return true;
        }
        return false;
    };
}
