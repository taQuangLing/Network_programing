package com.example.front_end.controller;

import com.example.front_end.UserCell;
import com.example.front_end.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import lombok.NoArgsConstructor;

import java.net.URL;
import java.util.ResourceBundle;
@NoArgsConstructor
public class FriendsController implements Initializable{
    @FXML ListView<User> friendListView;
    @FXML Pane friendsPane;
    @FXML Button friendsBtn;
    @FXML Button followingBtn;
    @FXML Button followerBtn;
    public void friendsOnClick(MouseEvent e){
        friendsBtn.setStyle("-fx-font-weight: bold;");
        followingBtn.setStyle("-fx-font-weight: normal;");
        followerBtn.setStyle("-fx-font-weight: normal;");
        friendsBtn.setTextFill(Paint.valueOf("#3465A4"));
        followingBtn.setTextFill(Paint.valueOf("#000"));
        followerBtn.setTextFill(Paint.valueOf("#000"));
        friendListView.setCellFactory(param -> new UserCell(1));
        ObservableList<User> userList = FXCollections.observableArrayList();
        userList.add(new User(1,
                "Ta Quang Linh",
                "https://toigingiuvedep.vn/wp-content/uploads/2022/01/anh-meo-cute.jpg",
                "Toi la Ling",
                "Nam",
                null,
                null,
                "Bong da"));
        userList.add(new User(2,
                "hoang Anh Tuan",
                "https://demoda.vn/wp-content/uploads/2022/02/anh-meo-cute-dang-yeu.jpeg",
                "Toi la Ling",
                "Nam",
                null,
                null,
                "Bong da"));
        userList.add(new User(3,
                "Le Viet Hung",
                "https://static.wixstatic.com/media/9d8ed5_b8fcc13f08fc4374bb1f979e032b0eb7~mv2.jpg/v1/fit/w_600,h_567,al_c,q_20,enc_auto/file.jpg",
                "Toi la Ling",
                "Nam",
                null,
                null,
                "Bong da"));
        userList.add(new User(1,
                "Mai Dao Tuan Thanh",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcShFksgubjvBsya7NsCbgby4hskXh3UBIr97uWRIoM&s",
                "Toi la Ling",
                "Nam",
                null,
                null,
                "Bong da"));
        friendListView.setItems(userList);

    }
    public void followingOnClick(MouseEvent e){
        followingBtn.setStyle("-fx-font-weight: bold;");
        friendsBtn.setStyle("-fx-font-weight: normal;");
        followerBtn.setStyle("-fx-font-weight: normal;");
        friendsBtn.setTextFill(Paint.valueOf("#000"));
        followingBtn.setTextFill(Paint.valueOf("#3465A4"));
        followerBtn.setTextFill(Paint.valueOf("#000"));
        friendListView.setCellFactory(param -> new UserCell(2));
        ObservableList<User> userList = FXCollections.observableArrayList();
        userList.add(new User(1,
                "Ta Quang Linh",
                "https://toigingiuvedep.vn/wp-content/uploads/2022/01/anh-meo-cute.jpg",
                "Toi la Ling",
                "Nam",
                null,
                null,
                "Bong da"));
        userList.add(new User(2,
                "hoang Anh Tuan",
                "https://demoda.vn/wp-content/uploads/2022/02/anh-meo-cute-dang-yeu.jpeg",
                "Toi la Ling",
                "Nam",
                null,
                null,
                "Bong da"));
        userList.add(new User(3,
                "Le Viet Hung",
                "https://static.wixstatic.com/media/9d8ed5_b8fcc13f08fc4374bb1f979e032b0eb7~mv2.jpg/v1/fit/w_600,h_567,al_c,q_20,enc_auto/file.jpg",
                "Toi la Ling",
                "Nam",
                null,
                null,
                "Bong da"));
        userList.add(new User(1,
                "Mai Dao Tuan Thanh",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcShFksgubjvBsya7NsCbgby4hskXh3UBIr97uWRIoM&s",
                "Toi la Ling",
                "Nam",
                null,
                null,
                "Bong da"));
        friendListView.setItems(userList);
    }
    public void followerOnClick(MouseEvent e){
        followerBtn.setStyle("-fx-font-weight: bold;");
        friendsBtn.setStyle("-fx-font-weight: normal;");
        followingBtn.setStyle("-fx-font-weight: normal;");
        friendsBtn.setTextFill(Paint.valueOf("#000"));
        followingBtn.setTextFill(Paint.valueOf("#000"));
        followerBtn.setTextFill(Paint.valueOf("#3465A4"));
        friendListView.setCellFactory(param -> new UserCell(3));
        ObservableList<User> userList = FXCollections.observableArrayList();
        userList.add(new User(1,
                "Ta Quang Linh",
                "https://toigingiuvedep.vn/wp-content/uploads/2022/01/anh-meo-cute.jpg",
                "Toi la Ling",
                "Nam",
                null,
                null,
                "Bong da"));
        userList.add(new User(2,
                "hoang Anh Tuan",
                "https://demoda.vn/wp-content/uploads/2022/02/anh-meo-cute-dang-yeu.jpeg",
                "Toi la Ling",
                "Nam",
                null,
                null,
                "Bong da"));
        userList.add(new User(3,
                "Le Viet Hung",
                "https://static.wixstatic.com/media/9d8ed5_b8fcc13f08fc4374bb1f979e032b0eb7~mv2.jpg/v1/fit/w_600,h_567,al_c,q_20,enc_auto/file.jpg",
                "Toi la Ling",
                "Nam",
                null,
                null,
                "Bong da"));
        userList.add(new User(1,
                "Mai Dao Tuan Thanh",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcShFksgubjvBsya7NsCbgby4hskXh3UBIr97uWRIoM&s",
                "Toi la Ling",
                "Nam",
                null,
                null,
                "Bong da"));
        friendListView.setItems(userList);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        friendsBtn.setStyle("-fx-font-weight: bold;");
        friendsBtn.setTextFill(Paint.valueOf("#3465A4"));
        friendListView.setCellFactory(param -> new UserCell(1));
        ObservableList<User> userList = FXCollections.observableArrayList();
        userList.add(new User(1,
                "Ta Quang Linh",
                "https://toigingiuvedep.vn/wp-content/uploads/2022/01/anh-meo-cute.jpg",
                "Toi la Ling",
                "Nam",
                null,
                null,
                "Bong da"));
        userList.add(new User(2,
                "hoang Anh Tuan",
                "https://demoda.vn/wp-content/uploads/2022/02/anh-meo-cute-dang-yeu.jpeg",
                "Toi la Ling",
                "Nam",
                null,
                null,
                "Bong da"));
        userList.add(new User(3,
                "Le Viet Hung",
                "https://static.wixstatic.com/media/9d8ed5_b8fcc13f08fc4374bb1f979e032b0eb7~mv2.jpg/v1/fit/w_600,h_567,al_c,q_20,enc_auto/file.jpg",
                "Toi la Ling",
                "Nam",
                null,
                null,
                "Bong da"));
        userList.add(new User(1,
                "Mai Dao Tuan Thanh",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcShFksgubjvBsya7NsCbgby4hskXh3UBIr97uWRIoM&s",
                "Toi la Ling",
                "Nam",
                null,
                null,
                "Bong da"));
        friendListView.setItems(userList);
    }
}
