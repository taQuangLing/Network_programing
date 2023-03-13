package com.example.front_end;

import com.example.front_end.appUtils.AppUtils;
import com.example.front_end.appUtils.GlobalVariable;
import com.example.front_end.controller.LayoutController;
import com.example.front_end.controller.LoginController;
import com.example.front_end.model.ClientSock;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        GlobalVariable.getInstance().getScreenController().addNameScreen("login", "fxml/LoginScene.fxml");
        GlobalVariable.getInstance().getScreenController().addNameScreen("register", "fxml/RegisterScene.fxml");
        GlobalVariable.getInstance().getScreenController().addNameScreen("forgot_password", "fxml/ForgotPasswordScene.fxml");
        GlobalVariable.getInstance().getScreenController().addNameScreen("create_post", "fxml/CreatePostScene.fxml");
        GlobalVariable.getInstance().getScreenController().addNameScreen("friends", "fxml/FriendsScene.fxml");
        GlobalVariable.getInstance().getScreenController().addNameScreen("search", "fxml/SearchScene.fxml");
        GlobalVariable.getInstance().getScreenController().addNameScreen("layout", "fxml/LayoutScene.fxml");
        GlobalVariable.getInstance().getScreenController().addNameScreen("profile", "fxml/ProfileScene.fxml");
        GlobalVariable.getInstance().getScreenController().addScreen("login", FXMLLoader.load(getClass().getResource(GlobalVariable.getInstance().getScreenController().getNameScreen().get("login"))));
        GlobalVariable.getInstance().getScreenController().addScreen("register", FXMLLoader.load(getClass().getResource(GlobalVariable.getInstance().getScreenController().getNameScreen().get("register"))));
        GlobalVariable.getInstance().getScreenController().addScreen("forgot_password", FXMLLoader.load(getClass().getResource(GlobalVariable.getInstance().getScreenController().getNameScreen().get("forgot_password"))));
        GlobalVariable.getInstance().getScreenController().activate("login");
        stage.setScene(GlobalVariable.getInstance().getScreenController().getMain());
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}