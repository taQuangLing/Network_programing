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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static com.example.front_end.appUtils.AppUtils.getPostFromServer;

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
        paneView.getChildren().add(1, globalVariable.getScreenController().getPane("friends"));
        paneView.getChildren().add(2, globalVariable.getScreenController().getPane("search"));
        paneView.getChildren().add(3, globalVariable.getScreenController().getPane("profile"));
    }
    public void createPostOnClick(MouseEvent e) throws IOException {
        System.out.println("create post click");
        Loader.load("create_post");
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
    public void friendsOnClick(MouseEvent e) throws IOException {
        System.out.println("Friends click");
        Loader.load("friends");
        notifyListView.setVisible(false);
        paneView.setVisible(true);
        paneView.getChildren().get(1).setVisible(true);
        paneView.getChildren().get(0).setVisible(false);
        paneView.getChildren().get(2).setVisible(false);
        paneView.getChildren().get(3).setVisible(false);
    }
    public void notificationOnClick(MouseEvent event) throws IOException {
        System.out.println("Notify clicked");
        notifyListView.setVisible(true);
        paneView.setVisible(false);
        notifyListView.setCellFactory(param -> new NotificationCell());
        ObservableList<Notification> notifications = AppUtils.getNotificationFromServer();
        notifyListView.setItems(notifications);
    }
    public void searchOnClick(MouseEvent event) throws IOException {
        System.out.println("Search clicked");
        Loader.load("search");
        notifyListView.setVisible(false);
        paneView.setVisible(true);
        paneView.getChildren().get(0).setVisible(false);
        paneView.getChildren().get(1).setVisible(false);
        paneView.getChildren().get(2).setVisible(true);
        paneView.getChildren().get(3).setVisible(false);
    }
    public void profileOnClick(MouseEvent event) throws IOException {
        System.out.println("profile clicked");
        Loader.load("profile");
        paneView.getChildren().set(3, globalVariable.getScreenController().getPane("profile"));
        paneView.requestLayout();
        paneView.layout();
        notifyListView.setVisible(false);
        paneView.setVisible(true);
        paneView.getChildren().get(0).setVisible(false);
        paneView.getChildren().get(1).setVisible(false);
        paneView.getChildren().get(2).setVisible(false);
        paneView.getChildren().get(3).setVisible(true);
        Button reload = (Button) paneView.getChildren().get(3)
                .lookup("#profileAnchorPane")
                .lookup("#profilePane1")
                .lookup("#profileVbox")
                .lookup("#profilePane2")
                .lookup("#reload");
//        reload.setOnAction((EventHandler<ActionEvent>) event);
    }

}
