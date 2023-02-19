package com.example.front_end.controller;

import com.example.front_end.appUtils.AppUtils;
import com.example.front_end.appUtils.GlobalVariable;
import com.example.front_end.model.Data;
import com.example.front_end.view.Message;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

import static com.example.front_end.appUtils.AppUtils.*;
import static java.lang.System.exit;

public class ForgotPasswordController {
    private Stage stage;
    @FXML TextField emailInput;
    @FXML TextField passwordInput;
    @FXML TextField passwordConfirmInput;
    @FXML Pane pane1;
    @FXML Pane pane2;
    private int key;
    private Message message;
    public void forgotPassword(ActionEvent e) throws IOException {
        String email = emailInput.getText();
        Data request  = new Data(AppUtils.MessageCode.FORGOT);
        request.getData().add(email);
        AppUtils.sendData(clientSock, request);

        Data response = recvData(clientSock);
        switch (response.getMessageCode()){
            case OK:
                key = Integer.valueOf((String) response.getData().get(0));
                pane1.setVisible(false);
                pane2.setVisible(true);
                break;
            case EMAIL_NOT_EXIST:
                // log
                message = new Message(MessageCode.WARNING, "Email không tồn tại");
                message.alert();
                break;
            default:
                // log bao tri
                message = new Message(MessageCode.ERROR);
                message.alert();
                exit(-1);
        }
    }
    public void confirm(ActionEvent e) throws IOException {
        String password;
        while(true){
            password = passwordInput.getText();
            String passwordConfirm = passwordConfirmInput.getText();
            if (password.equals(passwordConfirm) == true)break;
            // alert nhap sai
            message = new Message(AppUtils.MessageCode.WARNING, "Confirm password chưa đúng");
            message.alert();
            return;
        }
        Data request = new Data(AppUtils.MessageCode.FORGOT);
        request.getData().add(key);
        request.getData().add(password);
        sendData(clientSock, request);

        Data response = recvData(clientSock);
        switch (response.getMessageCode()){
            case SUCCESS:
                // alert success -> switch to login
                message = new Message(MessageCode.SUCCESS, "Thay đổi mật khẩu thành công");
                message.alert();
                switchToLogin(e);
                break;
            default:
                message = new Message(MessageCode.ERROR);
                message.alert();
                exit(-1);
        }
    }
    public void switchToLogin(ActionEvent e){
        emailInput.setText("");
        GlobalVariable globalVars = GlobalVariable.getInstance();
        globalVars.getScreenController().activate("login");
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        stage.setScene(globalVars.getScreenController().getMain());
        stage.show();
    }
    public void cancel(ActionEvent e) throws IOException {
        passwordInput.setText("");
        passwordConfirmInput.setText("");
        Data request = new Data(MessageCode.CANCEL);
        sendData(clientSock, request);
        pane1.setVisible(true);
        pane2.setVisible(false);
        key = -1;
        switchToLogin(e);
    }
}
