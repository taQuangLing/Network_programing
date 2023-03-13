package com.example.front_end;

import com.example.front_end.appUtils.GlobalVariable;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class Loader {
    public static void addScreen(String name, String path) throws IOException {
        GlobalVariable.getInstance().getScreenController().addScreen(name, FXMLLoader.load(Loader.class.getResource(path)));
    }
    public static void load(String name) throws IOException {
        GlobalVariable.getInstance().getScreenController().removeScreen(name);
        GlobalVariable.getInstance().getScreenController().addScreen(name, FXMLLoader.load(Loader.class.getResource(GlobalVariable.getInstance().getScreenController().getNameScreen().get(name))));
    }
}
