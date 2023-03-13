package com.example.front_end.controller;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ScreenController {
    private Map<String, String> nameScreen;
    private Map<String, Pane> screenMap;
    private Scene main;

    public ScreenController() {
        screenMap = new HashMap<>();
        nameScreen = new HashMap<>();
    }
    public ScreenController(Scene main) {
        this.main = main;
        screenMap = new HashMap<>();
        nameScreen = new HashMap<>();
    }

    public void addScreen(String name, Pane pane){
        screenMap.put(name, pane);
    }
    public void addNameScreen(String name, String path){
        nameScreen.put(name, path);
    }

    public void removeScreen(String name){
        screenMap.remove(name);
    }

    public void activate(String name){
        if (main == null){
            main = new Scene(screenMap.get(name));
        }else main.setRoot(screenMap.get(name));
    }
    public Pane getPane(String name){
        return screenMap.get(name);
    }
}
