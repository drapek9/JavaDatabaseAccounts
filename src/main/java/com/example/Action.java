package com.example;

public class Action {
    private String description;
    private Runnable runFunction;

    public Runnable getRunFunction() {
        return runFunction;
    }
    public String getDescription() {
        return description;
    }

    Action(String desValue, Runnable action){
        this.description = desValue;
        this.runFunction = action;
    }

    
}
