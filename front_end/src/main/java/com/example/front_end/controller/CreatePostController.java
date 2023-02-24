package com.example.front_end.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class CreatePostController {
    @FXML Text titleText;
    @FXML TextArea contentText;
    @FXML RadioButton status;
    @FXML Button imageBtn;
    @FXML Button fileBtn;
    @FXML Button postBtn;
    @FXML Label imageLabel;
    @FXML Label fileLabel;
    @FXML Pane createPostPane;
    private FXMLLoader fxmlLoader;
//    public CreatePost() {
//        super();
//        try {
//            fxmlLoader = new FXMLLoader(getClass().getResource("fxml/CreatePostScene.fxml"));
//            fxmlLoader.setController(this);
//            fxmlLoader.load();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//    }
//    @Override
//    protected void updateItem(Post post, boolean empty){
//        super.updateItem(post, empty);
//        if (empty || post == null) {
//            setText(null);
//            setGraphic(null);
//        } else {
//            setText(null);
//            setGraphic(createPostPane);
//        }
//    }

}
