package com.example.front_end.controller;

import com.example.front_end.Loader;
import com.example.front_end.appUtils.AppUtils;
import com.example.front_end.appUtils.GlobalVariable;
import com.example.front_end.model.ClientSock;
import com.example.front_end.model.Data;
import com.example.front_end.view.Message;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;

import static com.example.front_end.appUtils.AppUtils.MessageCode.ERROR;
import static com.example.front_end.appUtils.AppUtils.clientSock;
import static java.lang.System.exit;

public class LoginController{
    @FXML
    private TextField emailInput;
    @FXML
    private TextField passwordInput;
    private Stage stage;
    private Message message;
    @FXML
    ProgressIndicator waitingProgress;
    @FXML
    Rectangle waitingRectangle;
    public void login(ActionEvent e) throws IOException {
        AppUtils.clientSock = new ClientSock("0.tcp.ap.ngrok.io", 11993);
        waitingProgress.setVisible(true);
        waitingRectangle.setVisible(true);
        waitingProgress.setManaged(true);
        waitingProgress.setProgress(-1.0f);
        Task<AppUtils.MessageCode> task = new Task<>() {
            @Override
            protected AppUtils.MessageCode call() throws Exception {
                String email = emailInput.getText();
                String password = passwordInput.getText();
                Data request = new Data(AppUtils.MessageCode.LOGIN);
                request.getData().add(email);
                request.getData().add(password);
                AppUtils.sendData(clientSock, request);
                Data response = AppUtils.recvData(clientSock);
                switch (response.getMessageCode()){
                    case LOGIN_SUCCESS:
                        int id = Integer.valueOf((String)response.getData().get(0));
                        GlobalVariable.getInstance().setToken((String)response.getData().get(1));
                        GlobalVariable.getInstance().setId(id);
                        return AppUtils.MessageCode.SUCCESS;
                    case INCORRECT_PASS:
                        return AppUtils.MessageCode.INCORRECT_PASS;
                    default:
                        return ERROR;
                }
            }
        };
        Thread loginThread = new Thread(task);
        loginThread.start();
        task.setOnSucceeded(event -> {
            waitingProgress.setManaged(false);
            waitingProgress.setVisible(false);
            waitingRectangle.setVisible(false);
            emailInput.setText("");
            passwordInput.setText("");
            switch (task.getValue()){
                case SUCCESS:
                    try {
                        Loader.addScreen("friends", GlobalVariable.getInstance().getScreenController().getNameScreen().get("friends"));
                        Loader.addScreen("create_post", GlobalVariable.getInstance().getScreenController().getNameScreen().get("create_post"));
                        Loader.addScreen("search", GlobalVariable.getInstance().getScreenController().getNameScreen().get("search"));
                        Loader.addScreen("profile", GlobalVariable.getInstance().getScreenController().getNameScreen().get("profile"));
                        Loader.addScreen("layout", GlobalVariable.getInstance().getScreenController().getNameScreen().get("layout"));
                        GlobalVariable.getInstance().getScreenController().activate("layout");
                        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
                        stage.setScene(GlobalVariable.getInstance().getScreenController().getMain());
                        stage.show();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    message = new Message(AppUtils.MessageCode.SUCCESS, "Đăng nhập thành công!");
                    message.alert();
                    break;
                case INCORRECT_PASS:
                    message = new Message(AppUtils.MessageCode.WARNING, "Email hoặc mật khẩu không chính xác!");
                    message.alert();
                    break;
                case ERROR:
                    message = new Message(ERROR);
                    message.alert();
                    exit(-1);
            }
        });
    }
    public void changeRegister(ActionEvent e) {
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
