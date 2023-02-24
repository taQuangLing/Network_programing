package com.example.front_end.controller;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import java.util.HashMap;

public class ScreenController {
    private HashMap<String, Pane> screenMap;
    private Scene main;

    public ScreenController() {
        screenMap = new HashMap<>();
    }
    public ScreenController(Scene main) {
        this.main = main;
        screenMap = new HashMap<>();
    }

    public void addScreen(String name, Pane pane){
        screenMap.put(name, pane);
    }

    public void removeScreen(String name){
        screenMap.remove(name);
    }

    public void activate(String name){
        if (main == null){
            main = new Scene(screenMap.get(name));
        }else main.setRoot(screenMap.get(name));
    }
    public Scene getMain(){
        return main;
    }
    public Pane getPane(String name){
        return screenMap.get(name);
    }
}
