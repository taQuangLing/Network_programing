package com.example.front_end.controller;

import com.example.front_end.appUtils.AppUtils;
import com.example.front_end.appUtils.GlobalVariable;
import com.example.front_end.model.Data;
import com.example.front_end.view.Message;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

import static com.example.front_end.appUtils.AppUtils.clientSock;
import static java.lang.System.exit;

public class LoginController{
    @FXML
    private TextField emailInput;
    @FXML
    private TextField passwordInput;
    private Scene scene;
    private Parent root;
    private Stage stage;

    public void login(ActionEvent e) throws IOException {
        String email = emailInput.getText();
        String password = passwordInput.getText();
        Data request = new Data(AppUtils.MessageCode.LOGIN);
        request.getData().add(email);
        request.getData().add(password);
        AppUtils.sendData(clientSock, request);
        Data response = AppUtils.recvData(clientSock);
        System.out.println(response);
        Message message;
        switch (response.getMessageCode()){
            case LOGIN_SUCCESS:
                message = new Message(AppUtils.MessageCode.SUCCESS, "Đăng nhập thành công!");
                message.alert();
                // switch to news
                break;
            case INCORRECT_PASS:
                message = new Message(AppUtils.MessageCode.WARNING, "Email hoặc mật khẩu không chính xác!");
                message.alert();
                break;
            default:
                message = new Message(AppUtils.MessageCode.ERROR);
                message.alert();
                exit(-1);
        }
    }
    public void changeRegister(ActionEvent e) throws IOException {
        emailInput.setText("");
        passwordInput.setText("");
        GlobalVariable globalVars = GlobalVariable.getInstance();
        globalVars.getScreenController().activate("register");
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        stage.setScene(globalVars.getScreenController().getMain());
        stage.show();
    }
    public void switchToForgotPassword(ActionEvent e){
        emailInput.setText("");
        passwordInput.setText("");
        GlobalVariable globalVars = GlobalVariable.getInstance();
        globalVars.getScreenController().activate("forgot_password");
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        stage.setScene(globalVars.getScreenController().getMain());
        stage.show();
    }
}
