package com.example;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


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
                        writeError("Nepodařilo se vytvořit účet! Uživatelské jméno buď existuje a nebo nastala chyba na naší straně.");
                    } else {
                        System.out.println("Účet úspěšně vytvořen!");
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
        Map<Integer, Object> myValues = new HashMap<>();
        myValues.put(1, null);
        myValues.put(2, null);

        Scanner sc = new Scanner(System.in, "Cp852");

        myValues.keySet().forEach((key) -> {
            System.out.print(String.format("Zadejte %s: ", key == 1 ? "heslo" : "uživatelské jméno"));
            myValues.replace(key, sc.nextLine());
        });

        Map<Integer, Object> myValuesGet = new HashMap<>();

        myValuesGet.put(1, "Integer");
        myValuesGet.put(2, "String");
        myValuesGet.put(3, "String");
        myValuesGet.put(4, "String");
        myValuesGet.put(5, "String");

        UserImpl finalUser = null;

        List<List<Object>> result =  (List<List<Object>>)ConnectDatabase.connectToDatabase("SELECT id, name, user_name, password, role FROM users WHERE user_name = ? AND password = ?", myValues, "SELECT", myValuesGet);
        System.out.println(result);
        if (!result.isEmpty()){
            List<Object> oneResult = result.get(0);
            if (oneResult.get(4).toString().equals("admin")){
                finalUser = new Admin(Integer.parseInt(oneResult.get(0).toString()), oneResult.get(1).toString(), oneResult.get(3).toString(), oneResult.get(2).toString());
            } else {
                finalUser = new NormalUser(Integer.parseInt(oneResult.get(0).toString()), oneResult.get(1).toString(), oneResult.get(3).toString(), oneResult.get(2).toString());
            }
        } else {
            return null;
        }
        return finalUser;

        // sc.close();
        // Dotenv dotenv = Dotenv.load();

        // UserImpl finalUser = null;
        // try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/app_users", "root", dotenv.get("PASSWORD"))){
        //     try (PreparedStatement prepStatemant = connection.prepareStatement("SELECT id, name, user_name, password, role FROM users WHERE user_name = ? AND password = ?")){
        //         prepStatemant.setString(1, logIn.get("user_name"));
        //         prepStatemant.setString(2, logIn.get("password"));

        //         Map<String, String> allInf = new LinkedHashMap<>();

        //         ResultSet selectResult = prepStatemant.executeQuery();


        //         int countResults = 0;
        //         while (selectResult.next()){
        //             allInf.put("id", selectResult.getString(1));
        //             allInf.put("name", selectResult.getString(2));
        //             allInf.put("user_name", selectResult.getString(3));
        //             allInf.put("password", selectResult.getString(4));
        //             allInf.put("role", selectResult.getString(5));
        //             countResults ++;
        //         }


        //         if (countResults == 0){
        //             return null;
        //         } else {
        //             if (allInf.get("role").equals("admin")){
        //                 finalUser = new Admin(Integer.parseInt(allInf.get("id")), allInf.get("name"), allInf.get("user_name"), allInf.get("password"));
        //             } else {
        //                 finalUser = new NormalUser(Integer.parseInt(allInf.get("id")), allInf.get("name"), allInf.get("user_name"), allInf.get("password"));
        //             }
        //         }
        //     }
        // } catch (SQLException exc){
        //     return null;
        // }

        // return finalUser;
    }

    public static boolean createUser(){
        Map<Integer, Object> myValues = new HashMap<>();

        Scanner sc = new Scanner(System.in, "Cp852");

        myValues.put(1, null);
        myValues.put(2, null);
        myValues.put(3, null);

        myValues.keySet().forEach((key) -> {
            System.out.print(String.format("Enter information for %s: ", key == 1 ? "name" : key == 2 ? "username" : "password"));
            do {
                myValues.replace(key, sc.nextLine());
            } while (myValues.get(key) == null || myValues.get(key) == "");
        });


        boolean result = (boolean)ConnectDatabase.connectToDatabase("INSERT INTO users(name, user_name, password, role) VALUES(?, ?, ?, 'normal')", myValues, "INSERT", null);

        System.out.println(result);

        // Dotenv dotenv = Dotenv.load();
        // try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/app_users", "root", dotenv.get("PASSWORD"))){
        //     try (PreparedStatement prepStatement = connection.prepareStatement("INSERT INTO users(name, user_name, password, role) VALUES(?, ?, ?, 'normal')", PreparedStatement.RETURN_GENERATED_KEYS)){

        //         Scanner sc = new Scanner(System.in, "Cp852");

        //         reqTypeInf.keySet().forEach((key) -> {
        //             System.out.print(String.format("Enter information for %s: ", key));
        //             do {
        //                 reqTypeInf.replace(key, sc.nextLine());
        //             } while (reqTypeInf.get(key) == null || reqTypeInf.get(key) == "");
        //         });

                
        //         prepStatement.setString(1, reqTypeInf.get("name"));
        //         prepStatement.setString(2, reqTypeInf.get("user_name"));
        //         prepStatement.setString(3, reqTypeInf.get("password"));

        //         if (prepStatement.executeUpdate() > 0){
        //             try (ResultSet ids = prepStatement.getGeneratedKeys()){
        //                 ids.next();
        //             }
        //         } else {
        //             return false;
        //         }
        //     } catch (SQLIntegrityConstraintViolationException exc) {
        //         writeError("Uživatelské jméno již existuje!");
        //         return true;
        //     }
        // } catch (SQLException exc){
        //     return false;
        // }
        return result;
    }

    public static void writeError(String errorText){
        System.out.println(errorText);
    }
}
