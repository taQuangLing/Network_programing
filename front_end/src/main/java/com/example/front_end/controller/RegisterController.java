package com.example.front_end.controller;

import com.example.front_end.appUtils.AppUtils;
import com.example.front_end.appUtils.GlobalVariable;
import com.example.front_end.model.Data;
import com.example.front_end.view.Message;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
    private Stage stage;
    Message message;
    public void register(ActionEvent e) throws IOException {
        String email = emailInput.getText();
        String username = usernameInput.getText();
        String password = passwordInput.getText();
        String confirmPass = confirmPasswordInput.getText();

        if (password.equals(confirmPass) == false){
            message = new Message(AppUtils.MessageCode.WARNING, "Confirm password chưa đúng");
            message.alert();
            return;
        }
        Data request = new Data(AppUtils.MessageCode.SIGNUP);
        request.getData().add(email);
        request.getData().add(username);
        request.getData().add(password);
        AppUtils.sendData(clientSock, request);
        // waiting from server
        Data response = AppUtils.recvData(clientSock);
        System.out.println(response);
        switch (response.getMessageCode()) {
            case EMAIL_DUPLICATE:
                message = new Message(AppUtils.MessageCode.WARNING, "Email đã đăng kí tài khoản");
                message.alert();
                return;
            case SUCCESS:
                // dang ki thanh cong, chuyen trang dang nhap
                message = new Message(AppUtils.MessageCode.SUCCESS, "Đăng ký tài khoản thành công");
                message.alert();
                switchToLogin(e);
                break;
            default:
                message = new Message(AppUtils.MessageCode.ERROR);
                message.alert();
                // he thong dang bao tri
                break;
        }
    }
    public void switchToLogin(ActionEvent e){
        emailInput.setText("");
        passwordInput.setText("");
        confirmPasswordInput.setText("");
        usernameInput.setText("");
        GlobalVariable globalVars = GlobalVariable.getInstance();
        globalVars.getScreenController().activate("login");
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        stage.setScene(globalVars.getScreenController().getMain());
        stage.show();
    }
}
