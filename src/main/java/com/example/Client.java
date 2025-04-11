package com.example;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

public class Client {
    static Scanner sc = new Scanner(System.in, "Cp852");
    public static void main(String[] args) {
        String name = sc.nextLine();
        String password = sc.nextLine();
        try {
            UserService theService = (UserService)Naming.lookup("rmi://localhost:1099/users");
            User loggedUser = theService.logIn(name, password.toCharArray());
            if (loggedUser == null){
                System.out.println("Chybné přihlašovací udaje");
                return;
            }
        } catch (MalformedURLException | RemoteException | NotBoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
