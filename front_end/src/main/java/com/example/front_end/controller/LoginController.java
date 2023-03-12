package com.example.front_end.controller;

import com.example.front_end.Loader;
import com.example.front_end.appUtils.AppUtils;
import com.example.front_end.appUtils.GlobalVariable;
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

import static com.example.front_end.appUtils.AppUtils.clientSock;
import static java.lang.System.exit;

public class LoginController{
    @FXML
    private TextField emailInput;
    @FXML
    private TextField passwordInput;
    private Stage stage;
    @FXML
    ProgressIndicator waitingProgress;
    @FXML
    Rectangle waitingRectangle;

    public void login(ActionEvent e) throws IOException {

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                waitingProgress.setVisible(true);
                waitingRectangle.setVisible(true);
                String email = emailInput.getText();
                String password = passwordInput.getText();
                Data request = new Data(AppUtils.MessageCode.LOGIN);
                request.getData().add(email);
                request.getData().add(password);
                AppUtils.sendData(clientSock, request);
                Data response = AppUtils.recvData(clientSock);
                Message message;
                switch (response.getMessageCode()){
                    case LOGIN_SUCCESS:
                        int id = Integer.valueOf((String)response.getData().get(0));
                        GlobalVariable.getInstance().setId(id);
                        Loader.addScreen("create_post", "fxml/CreatePostScene.fxml");
                        Loader.addScreen("friends", "fxml/FriendsScene.fxml");
                        Loader.addScreen("search", "fxml/SearchScene.fxml");
                        Loader.addScreen("profile", "fxml/ProfileScene.fxml");
                        Loader.addScreen("layout", "fxml/LayoutScene.fxml");
                        GlobalVariable.getInstance().getScreenController().activate("layout");
                        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
                        stage.setScene(GlobalVariable.getInstance().getScreenController().getMain());
                        stage.show();
                        message = new Message(AppUtils.MessageCode.SUCCESS, "Đăng nhập thành công!");
                        message.alert();
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
                waitingProgress.setVisible(false);
                waitingRectangle.setVisible(false);
                return null;
            }
        };
        waitingProgress.progressProperty().bind(task.progressProperty());
        new Thread(task).start();
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
