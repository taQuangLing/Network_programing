package com.example.front_end.appUtils;

import com.example.front_end.controller.ScreenController;
import javafx.scene.Scene;
import lombok.Data;

@Data
public class GlobalVariable {
    private static GlobalVariable instance = new GlobalVariable();
    private ScreenController screenController;
    private Integer id = 0;
    private Integer switchToOthersId = 0;

    public Integer getId() {
        return id;
    }

    private String token;

    public GlobalVariable() {
        screenController = new ScreenController();
    }


    public static GlobalVariable getInstance() {
        return instance;
    }

    public ScreenController getScreenController() {
        return screenController;
    }

    public void setCount(ScreenController screenController) {
        this.screenController = screenController;
    }
}
