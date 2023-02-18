package com.example.front_end;

import com.example.front_end.model.ClientSock;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import static com.example.front_end.appUtils.AppUtils.clientSock;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("fxml/LoginScene.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws IOException {
//        ClientSock clientSock = new ClientSock();
//        clientSock.connectServer("127.0.0.1", 5000);
        clientSock = new ClientSock("127.0.0.1", 5000);
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