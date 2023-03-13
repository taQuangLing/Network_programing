package com.example.front_end.controller;

import com.example.front_end.UserCell;
import com.example.front_end.appUtils.AppUtils;
import com.example.front_end.appUtils.GlobalVariable;
import com.example.front_end.model.Data;
import com.example.front_end.model.User;
import com.example.front_end.view.Message;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static com.example.front_end.appUtils.AppUtils.MessageCode.*;
import static com.example.front_end.appUtils.AppUtils.clientSock;
import static com.example.front_end.appUtils.AppUtils.recvData;
import static java.lang.System.exit;

@NoArgsConstructor
public class FriendsController implements Initializable{
    @FXML ListView<User> friendListView;
    @FXML Pane friendsPane;
    @FXML
    HBox friendsHbox;
    @FXML
    AnchorPane friendsAnchorPane;
    @FXML Button friendsBtn;
    @FXML Button followingBtn;
    @FXML Button followerBtn;
    private GlobalVariable globalVariable;
    public ObservableList<User> getUserList(int count) throws IOException {
        ObservableList<User> userList = FXCollections.observableArrayList();

        for (int i = 0; i < count; i++){
            Data rspItem = recvData(clientSock);
            User user = new User();
            user.setId(Integer.valueOf((String) rspItem.getData().get(0)));
            user.setName((String) rspItem.getData().get(1));
            user.setAvatar((String) rspItem.getData().get(2));
            userList.add(user);
        }
        return userList;
    }
    public void friendsOnClick(MouseEvent e) throws IOException {
        friendsBtn.setStyle("-fx-font-weight: bold;");
        followingBtn.setStyle("-fx-font-weight: normal;");
        followerBtn.setStyle("-fx-font-weight: normal;");
        friendsBtn.setTextFill(Paint.valueOf("#3465A4"));
        followingBtn.setTextFill(Paint.valueOf("#000"));
        followerBtn.setTextFill(Paint.valueOf("#000"));
        friendListView.setCellFactory(param -> new UserCell(1));
        ObservableList<User> userList = getUserFromServer(FRIENDS);
        if (userList == null){
            Message message = new Message(ERROR);
            message.alert();
        }else{
            friendListView.setItems(userList);
        }
    }
    public void followingOnClick(MouseEvent e) throws IOException {
        followingBtn.setStyle("-fx-font-weight: bold;");
        friendsBtn.setStyle("-fx-font-weight: normal;");
        followerBtn.setStyle("-fx-font-weight: normal;");
        friendsBtn.setTextFill(Paint.valueOf("#000"));
        followingBtn.setTextFill(Paint.valueOf("#3465A4"));
        followerBtn.setTextFill(Paint.valueOf("#000"));
        friendListView.setCellFactory(param -> new UserCell(2));
        ObservableList<User> userList = getUserFromServer(FOLLOWING);
        if (userList == null){
            Message message = new Message(ERROR);
            message.alert();
        }else{
            friendListView.setItems(userList);
        }
    }
    public void followerOnClick(MouseEvent e) throws IOException {
        followerBtn.setStyle("-fx-font-weight: bold;");
        friendsBtn.setStyle("-fx-font-weight: normal;");
        followingBtn.setStyle("-fx-font-weight: normal;");
        friendsBtn.setTextFill(Paint.valueOf("#000"));
        followingBtn.setTextFill(Paint.valueOf("#000"));
        followerBtn.setTextFill(Paint.valueOf("#3465A4"));
        friendListView.setCellFactory(param -> new UserCell(3));
        ObservableList<User> userList = getUserFromServer(FOLLOWER);
        if (userList == null){
            Message message = new Message(ERROR);
            message.alert();
        }else{
            friendListView.setItems(userList);
        }
    }
    public ObservableList<User> getUserFromServer(AppUtils.MessageCode messageCode) throws IOException {
        ObservableList<User> userList = FXCollections.observableArrayList();
        Data request = new Data(messageCode);
        request.getData().add(GlobalVariable.getInstance().getId());
        AppUtils.sendData(clientSock, request); // send data
        Data response = AppUtils.recvData(clientSock); // recv count
        int count = Integer.valueOf((String) response.getData().get(0));
        if (response.getMessageCode() == OK){
            return getUserList(count);
        }else{
            return null;
        }
    }
    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        friendsBtn.setStyle("-fx-font-weight: bold;");
        friendsBtn.setTextFill(Paint.valueOf("#3465A4"));
        friendListView.setCellFactory(param -> new UserCell(1));
        ObservableList<User> userList = getUserFromServer(FRIENDS);
        if (userList == null){
            Message message = new Message(ERROR);
            message.alert();
        }else{
            friendListView.setItems(userList);
        }
    }
}
