package com.example.front_end.controller;

import com.example.front_end.NotificationCell;
import com.example.front_end.PostCell;
import com.example.front_end.appUtils.AppUtils;
import com.example.front_end.appUtils.GlobalVariable;
import com.example.front_end.model.Data;
import com.example.front_end.model.Notification;
import com.example.front_end.model.Post;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static com.example.front_end.appUtils.AppUtils.*;

public class LayoutController implements Initializable{
    @FXML ListView<Post> postListView;
    @FXML ListView<Notification> notifyListView;
    @FXML Pane paneView;
    GlobalVariable globalVariable = GlobalVariable.getInstance();
    @SneakyThrows
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set cell factory for postListView
        postListView.setVisible(true);
        home();
        paneView.getChildren().add(0, globalVariable.getScreenController().getPane("create_post"));
//        System.out.println(globalVariable.getScreenController());
        paneView.getChildren().add(1, globalVariable.getScreenController().getPane("friends"));
        paneView.getChildren().add(2, globalVariable.getScreenController().getPane("search"));
        paneView.getChildren().add(3, globalVariable.getScreenController().getPane("profile"));
    }

    private ObservableList<Post> getPostFromServer() throws IOException {
        Data request = new Data(AppUtils.MessageCode.NEWS);
        request.getData().add(1);
        sendData(clientSock, request);
        Data response = recvData(clientSock);
        int count = Integer.valueOf((String) response.getData().get(0));
        ObservableList<Post> postList = FXCollections.observableArrayList();
        for (int i = 0; i < count; i++){
            response = recvData(clientSock);
            Post post = new Post(
                    Integer.valueOf((String) response.getData().get(0)),
                    Integer.valueOf((String) response.getData().get(1)),
                    (String) response.getData().get(2),
                    (String) response.getData().get(3),
                    (String) response.getData().get(4),
                    (String) response.getData().get(5),
                    (String) response.getData().get(6)
            );
            postList.add(post);
        }
        return postList;
    }
//    public void clickPost(ActionEvent e){
//        Post post = postListView.getSelectionModel().getSelectedItem();
//        if (post == null){
//            System.out.println("Post = NULL");
//        }else
//        System.out.println(post);
//    }

    public void createPostOnClick(MouseEvent e){
        System.out.println("create post click");
        notifyListView.setVisible(false);
        paneView.setVisible(true);
        paneView.getChildren().get(0).setVisible(true);
        paneView.getChildren().get(1).setVisible(false);
        paneView.getChildren().get(2).setVisible(false);
        paneView.getChildren().get(3).setVisible(false);
    }
    public void home() throws IOException {
        postListView.setCellFactory(param -> new PostCell());
        ObservableList<Post> postList;
        // get posts from server
        postList = getPostFromServer();
        postListView.setItems(postList);
    }
    public void homeOnClick(MouseEvent e) throws IOException {
        paneView.setVisible(false);
        paneView.setVisible(false);
        notifyListView.setVisible(false);
        postListView.setVisible(true);
        home();
    }
    public void friendsOnClick(MouseEvent e){
        System.out.println("Friends click");
        notifyListView.setVisible(false);
        paneView.setVisible(true);
        paneView.getChildren().get(1).setVisible(true);
        paneView.getChildren().get(0).setVisible(false);
        paneView.getChildren().get(2).setVisible(false);
        paneView.getChildren().get(3).setVisible(false);
    }
    private ObservableList<Notification> getNotificationFromServer() throws IOException {
        Data request = new Data(MessageCode.NOTIFY);
        request.getData().add(1); // add id
        sendData(clientSock, request);

        ObservableList<Notification> notifyList = FXCollections.observableArrayList();
        Data response = recvData(clientSock);
        int count = Integer.valueOf((String) response.getData().get(0));
        for (int i = 0; i < count; i++){
            Data rspItem = recvData(clientSock);
            Notification notifyItem = new Notification();
            notifyItem.setId(Integer.valueOf((String)rspItem.getData().get(0)));
            notifyItem.setUsername((String) rspItem.getData().get(1));
            notifyItem.setAvatar((String) rspItem.getData().get(2));
            notifyItem.setTitle((String) rspItem.getData().get(3));
            notifyItem.setContent((String) rspItem.getData().get(4));
            if (Integer.valueOf((String)rspItem.getData().get(5)) == 0){
                notifyItem.setSeen(false);
            }
            else notifyItem.setSeen(true);
            notifyList.add(notifyItem);
        }
        return notifyList;
    }
    public void notificationOnClick(MouseEvent event) throws IOException {
        System.out.println("Notify clicked");
        notifyListView.setVisible(true);
        paneView.setVisible(false);
        notifyListView.setCellFactory(param -> new NotificationCell());
        ObservableList<Notification> notifications = getNotificationFromServer();
        notifyListView.setItems(notifications);
    }
    public void searchOnClick(MouseEvent event){
        System.out.println("Search clicked");
        notifyListView.setVisible(false);
        paneView.setVisible(true);
        paneView.getChildren().get(0).setVisible(false);
        paneView.getChildren().get(1).setVisible(false);
        paneView.getChildren().get(2).setVisible(true);
        paneView.getChildren().get(3).setVisible(false);
    }
    public void profileOnClick(MouseEvent event){
        System.out.println("profile clicked");
        notifyListView.setVisible(false);
        paneView.setVisible(true);
        paneView.getChildren().get(0).setVisible(false);
        paneView.getChildren().get(1).setVisible(false);
        paneView.getChildren().get(2).setVisible(false);
        paneView.getChildren().get(3).setVisible(true);
    }

}
