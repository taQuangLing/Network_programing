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
        GlobalVariable.getInstance().getScreenController().addScreen("login", FXMLLoader.load(getClass().getResource("fxml/LoginScene.fxml")));
        GlobalVariable.getInstance().getScreenController().addScreen("register", FXMLLoader.load(getClass().getResource("fxml/RegisterScene.fxml")));
        GlobalVariable.getInstance().getScreenController().addScreen("forgot_password", FXMLLoader.load(getClass().getResource("fxml/ForgotPasswordScene.fxml")));
        GlobalVariable.getInstance().getScreenController().activate("login");
        stage.setScene(GlobalVariable.getInstance().getScreenController().getMain());
        stage.show();
    }

    public static void main(String[] args) throws IOException {
        AppUtils.clientSock = new ClientSock("127.0.0.1", 5000);
        launch();
    }
}