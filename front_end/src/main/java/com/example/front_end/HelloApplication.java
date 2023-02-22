package com.example.front_end;

import com.example.front_end.appUtils.AppUtils;
import com.example.front_end.appUtils.GlobalVariable;
import com.example.front_end.model.ClientSock;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        GlobalVariable globalVars = GlobalVariable.getInstance();
        globalVars.getScreenController().addScreen("login", FXMLLoader.load(getClass().getResource("fxml/LoginScene.fxml")));
        globalVars.getScreenController().addScreen("register", FXMLLoader.load(getClass().getResource("fxml/RegisterScene.fxml")));
        globalVars.getScreenController().addScreen("forgot_password", FXMLLoader.load(getClass().getResource("fxml/ForgotPasswordScene.fxml")));
        globalVars.getScreenController().addScreen("create_post", FXMLLoader.load(getClass().getResource("fxml/CreatePostScene.fxml")));
        globalVars.getScreenController().addScreen("layout", FXMLLoader.load(getClass().getResource("fxml/LayoutScene.fxml")));

//        globalVars.getScreenController().addScreen("post_cell", FXMLLoader.load(getClass().getResource("fxml/PostCell.fxml")));
//        globalVars.getScreenController().addScreen("posts", FXMLLoader.load(getClass().getResource("fxml/CreatePostScene.fxml")));
//        globalVars.getScreenController().addScreen("profile", FXMLLoader.load(getClass().getResource("fxml/ProfileScene.fxml")));
//        globalVars.getScreenController().addScreen("friends", FXMLLoader.load(getClass().getResource("fxml/FriendsScene.fxml")));
//        globalVars.getScreenController().addScreen("search", FXMLLoader.load(getClass().getResource("fxml/SearchScene.fxml")));
//        globalVars.getScreenController().addScreen("notification", FXMLLoader.load(getClass().getResource("fxml/NotificationScene.fxml")));

        globalVars.getScreenController().activate("layout");
        stage.setScene(globalVars.getScreenController().getMain());
        stage.show();
    }

    public static void main(String[] args) throws IOException {

//        AppUtils.clientSock = new ClientSock("127.0.0.1", 5000);
        launch();



//        ClientSock clientSock = new ClientSock();
//        try {
//            clientSock.connectServer("127.0.0.1", 5000);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        Data data = new Data(AppUtils.MessageCode.FORGOT);
//        data.getData().add("abc@gmail.com");
//        try {
//            AppUtils.sendData(clientSock, data);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        int key;
//        try {
//            Data response = AppUtils.recvData(clientSock);
////            int count = Integer.valueOf((String) response.getData().get(0));
//            key = Integer.valueOf((String) response.getData().get(0));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        data = new Data(AppUtils.MessageCode.FORGOT);
//        data.getData().add(key);
//        data.getData().add("12");
//
//        AppUtils.sendData(clientSock, data);
//        Data response = AppUtils.recvData(clientSock);
//        System.out.println(response);
//        clientSock.closeSocket();

    }
}