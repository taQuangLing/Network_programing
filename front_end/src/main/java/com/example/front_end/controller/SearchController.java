package com.example.front_end.controller;

import com.example.front_end.UserCell;
import com.example.front_end.SearchBookCell;
import com.example.front_end.appUtils.AppUtils;
import com.example.front_end.appUtils.GlobalVariable;
import com.example.front_end.model.Data;
import com.example.front_end.model.Post;
import com.example.front_end.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.io.IOException;

import static com.example.front_end.appUtils.AppUtils.*;

public class SearchController{
    enum Type{
        USER,
        POST;
    }
    @FXML
    TextField searchInput;
    @FXML
    Button bookFilterBtn;
    @FXML
    Button userFilterBtn;
    @FXML
    Label processingLabel;
    @FXML
    Rectangle searchRectangle;
    @FXML ListView<Post> bookListView;
    @FXML ListView<User> userListView;
    private ObservableList<User> getUserListFromServer() throws IOException {
        ObservableList<User> userList = FXCollections.observableArrayList();
        Data response = recvData(clientSock);
        int count = Integer.valueOf((String) response.getData().get(0));
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
    private ObservableList<Post> getPostListFromServer() throws IOException {
        ObservableList<Post> postList = FXCollections.observableArrayList();
        Data response = recvData(clientSock);
        int count = Integer.valueOf((String) response.getData().get(0));
        for (int i = 0; i < count; i++){
            Data rspItem = recvData(clientSock);
            Post post = new Post();
            post.setId(Integer.valueOf((String) rspItem.getData().get(0)));
            post.setUserId(Integer.valueOf((String) rspItem.getData().get(1)));
            post.setUsername((String) rspItem.getData().get(2));
            post.setImage((String) rspItem.getData().get(3));
            post.setTitle((String) rspItem.getData().get(4));
            post.setContent((String) rspItem.getData().get(5));
            postList.add(post);
        }
        return postList;
    }
    public void search(String textSeach, Type type) throws IOException {
//        searchRectangle.setVisible(true);
//        processingLabel.setVisible(true);
        ObservableList<Post> bookList;
        ObservableList<User> userList;
        if (textSeach == null)textSeach = searchInput.getText();
        // ============== Xu li du lieu va gui di server
        Data request = new Data(AppUtils.MessageCode.SEARCH);
        request.getData().add(GlobalVariable.getInstance().getId()); // id
        request.getData().add(textSeach);
        if (type == Type.USER)request.getData().add(1);
        else request.getData().add(0);
        sendData(clientSock, request);
        // ============== Dau ra la booklist
        if (type == Type.POST){
            bookListView.setCellFactory(param -> new SearchBookCell());
            bookList = getPostListFromServer();
            bookListView.setItems(bookList);
        }else{
            userListView.setCellFactory(param -> new UserCell(0));
            userList = getUserListFromServer();
            userListView.setItems(userList);
        }
        searchRectangle.setVisible(false);
        processingLabel.setVisible(false);
    }
    public void searchEnter(ActionEvent e) throws IOException {
        bookListView.setVisible(true);
        userListView.setVisible(false);
        userFilterBtn.setTextFill(Paint.valueOf("#000"));
        bookFilterBtn.setTextFill(Paint.valueOf("#3465A4"));
        userFilterBtn.setStyle("-fx-font-weight: normal;");
        bookFilterBtn.setStyle("-fx-font-weight: bold;");
        search(null, Type.POST);
    }
    public void searchOnClick(MouseEvent e) throws IOException {
        bookListView.setVisible(true);
        userListView.setVisible(false);
        userFilterBtn.setTextFill(Paint.valueOf("#000"));
        bookFilterBtn.setTextFill(Paint.valueOf("#3465A4"));
        userFilterBtn.setStyle("-fx-font-weight: normal;");
        bookFilterBtn.setStyle("-fx-font-weight: bold;");
        search(null, Type.POST);
    }
    public void bookFilterOnClick(MouseEvent e) throws IOException {
        bookListView.setVisible(true);
        userListView.setVisible(false);
        userFilterBtn.setTextFill(Paint.valueOf("#000"));
        bookFilterBtn.setTextFill(Paint.valueOf("#3465A4"));
        userFilterBtn.setStyle("-fx-font-weight: normal;");
        bookFilterBtn.setStyle("-fx-font-weight: bold;");
        String textSearch = searchInput.getText();
        search(textSearch, Type.POST);
        userListView.getItems().clear();
    }
    public void userFilterOnClick(MouseEvent event) throws IOException {
        bookListView.setVisible(false);
        userListView.setVisible(true);
        userFilterBtn.setTextFill(Paint.valueOf("#3465A4"));
        bookFilterBtn.setTextFill(Paint.valueOf("#000"));
        userFilterBtn.setStyle("-fx-font-weight: bold;");
        bookFilterBtn.setStyle("-fx-font-weight: normal;");
        String textSearch = searchInput.getText();
        search(textSearch, Type.USER);
        bookListView.getItems().clear();
    }
}
