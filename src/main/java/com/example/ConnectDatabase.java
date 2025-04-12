package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.github.cdimascio.dotenv.Dotenv;

public class ConnectDatabase {
    static Object connectToDatabase (String sqlStatemant, Map<Integer, Object> values, String typeFunction, Map<Integer, Object> valuesGet){
        Dotenv theDotenv = Dotenv.load();
        try (Connection connection = DriverManager.getConnection(theDotenv.get("CONNECTADRESS"), theDotenv.get("USER"), theDotenv.get("PASSWORD"))){
            try (PreparedStatement prepStatemant = connection.prepareStatement(sqlStatemant)){
                values.keySet().forEach((oneNumber) -> {
                    try {
                        if (values.get(oneNumber) instanceof Integer) {
                            prepStatemant.setInt(oneNumber, (Integer) values.get(oneNumber)); 
                        } else {
                            prepStatemant.setString(oneNumber, (String) values.get(oneNumber));
                        }
                        
                    } catch (SQLException exc){

                    }
                    
                });
                List<String> firstTypes = new ArrayList<>();
                firstTypes.add("DELETE");
                firstTypes.add("UPDATE");
                firstTypes.add("INSERT");
                if (firstTypes.contains(typeFunction)){
                    if (prepStatemant.executeUpdate() > 0){
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    List<List<Object>> allValues = new ArrayList<>();
                    ResultSet theResult = prepStatemant.executeQuery();
                    while (theResult.next()) {
                        allValues.add(new ArrayList<>());
                        valuesGet.keySet().forEach((key) -> {
                            try {
                                if (valuesGet.get(key) == "String"){
                                    allValues.get(allValues.size()-1).add(theResult.getString(key));
                                } else {
                                    int res = theResult.getInt(key);
                                    allValues.get(allValues.size()-1).add(res);
                                }
                               
                            } catch (SQLException exc){

                            }
                            
                        });
                    }
                    return allValues;
                }

            } catch (Exception exc){
                return false;
            }
        } catch (SQLException exc){
            return null;
        }
    }
}
