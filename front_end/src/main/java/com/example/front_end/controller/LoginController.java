package com.example.front_end.controller;

import com.example.front_end.appUtils.AppUtils;
import com.example.front_end.model.Data;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;

import static com.example.front_end.appUtils.AppUtils.clientSock;

public class LoginController{
    @FXML
    private TextField emailInput;
    @FXML
    private TextField passwordInput;
    public void login(ActionEvent e) throws IOException {
        String email = emailInput.getText();
        String password = passwordInput.getText();
        Data request = new Data(AppUtils.MessageCode.LOGIN);
        request.getData().add(email);
        request.getData().add(password);
        AppUtils.sendData(clientSock, request);
        Data response = AppUtils.recvData(clientSock);
        System.out.println(response);
    }
    public void changeRegister(ActionEvent e){

    }
}
