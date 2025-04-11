package com.example;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    public static void main(String[] args) {
        try {
            UserService userService = new UserServiceImpl();
            Registry myRegistry = LocateRegistry.createRegistry(1099);
            myRegistry.rebind("users", userService);
            System.out.println("Hotovooo!");
        } catch (RemoteException exc){
            exc.printStackTrace();
        }
        
    }
}
