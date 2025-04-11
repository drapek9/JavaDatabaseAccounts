package com.example;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface UserService extends Remote {
    User logIn (String name, char[] password) throws RemoteException;
    
}
