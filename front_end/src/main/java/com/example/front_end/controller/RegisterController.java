package com.example.front_end.controller;

import com.example.front_end.appUtils.AppUtils;
import com.example.front_end.model.Data;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;

import static com.example.front_end.appUtils.AppUtils.clientSock;

public class RegisterController {
    @FXML
    private TextField emailInput;
    @FXML
    private TextField usernameInput;
    @FXML
    private TextField passwordInput;
    @FXML
    private TextField confirmPasswordInput;
    public void register(ActionEvent e) throws IOException {
        String email = emailInput.getText();
        String username = usernameInput.getText();
        String password = passwordInput.getText();
        String confirmPass = confirmPasswordInput.getText();

        if (password.equals(confirmPass) == false){
            // log
            return;
        }
        Data request = new Data(AppUtils.MessageCode.LOGIN);
        request.getData().add(email);
        request.getData().add(username);
        request.getData().add(password);
        AppUtils.sendData(clientSock, request);
        // waiting from server
        Data response = AppUtils.recvData(clientSock);
        System.out.println(response);
        if (response.getMessageCode() == AppUtils.MessageCode.EMAIL_DUPLICATE){
            // log trung email
            return;
        }else if (response.getMessageCode() == AppUtils.MessageCode.SUCCESS){
            // dang ki thanh cong, chuyen trang dang nhap
        }
        else{
            // he thong dang bao tri
        }
    }
}
