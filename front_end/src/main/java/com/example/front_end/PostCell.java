package com.example.front_end;

import com.example.front_end.appUtils.AppUtils;
import com.example.front_end.appUtils.GlobalVariable;
import com.example.front_end.model.Data;
import com.example.front_end.model.Post;
import com.example.front_end.view.Message;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.io.IOException;

import static com.example.front_end.appUtils.AppUtils.MessageCode.*;
import static com.example.front_end.appUtils.AppUtils.*;

public class PostCell extends ListCell<Post> {
    @FXML
    ImageView imageId;
    @FXML ImageView avatarId;
    @FXML Text titleId;
    @FXML Text contentId;
    @FXML Text usernameId;
    @FXML Pane paneId;
    @FXML Rectangle rectangleId;
    @FXML ScrollPane scrollId;
    @FXML
    AnchorPane postAnchorPane;
    private double heightContent;
    private double heightTitle;

    private FXMLLoader fxmlLoader;
    public PostCell(){
        super();
        try {
            fxmlLoader = new FXMLLoader(getClass().getResource("fxml/PostCell.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        avatarId.setOnMouseClicked(event -> {
            int index = getIndex();
            if (index >= 0 && index < getListView().getItems().size()) {
                Post post = getListView().getItems().get(index);
                // chuyen den profile
                System.out.println("Avatar: " + post.getAvatar());
            }
        });
        usernameId.setOnMouseClicked(mouseEvent -> {
            int index = getIndex();
            if (index >= 0 && index < getListView().getItems().size()) {
                // chuyen den profile
                System.out.println("Username clicked");
            }
        });
        imageId.setOnMouseClicked(mouseEvent -> {
            int index = getIndex();
            if (index >= 0 && index < getListView().getItems().size()) {
                // open book
                System.out.println("Image clicked");
                Data request = new Data(AppUtils.MessageCode.OPEN);
                request.getData().add(GlobalVariable.getInstance().getId());
                request.getData().add(getListView().getItems().get(index).getId());
                try {
                    sendData(clientSock, request);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Data response;
                String filename;
                try {
                    response = recvData(clientSock);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                if (response.getMessageCode() == FAIL){
                    Message message = new Message(WARNING, "Bạn không có quyền truy cập");
                    message.alert();
                }else if (response.getMessageCode() == OK) {
                    try {
                        response = recvData(clientSock);

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    filename = (String) response.getData().get(0);
                    try {
                        writeFile(filename);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }else{
                    Message message = new Message(ERROR);
                }
            }
        });
        rectangleId.setOnMouseClicked(mouseEvent -> {
            int index = getIndex();
            if (index >= 0 && index < getListView().getItems().size()) {
                Post post = getListView().getItems().get(index);
                // Mo rong sach
                System.out.println("rectangle clicked");
                rectangleId.toBack();
                rectangleId.setDisable(true);
                titleId.setText(post.getTitle());
                contentId.setText(post.getContent());
                heightTitle = titleId.getLayoutBounds().getHeight();
                heightContent = contentId.getLayoutBounds().getHeight();
                contentId.setLayoutY(titleId.getLayoutY() + heightTitle);
                imageId.setLayoutY(contentId.getLayoutY() + heightContent);
                scrollId.setPrefHeight(60 + heightTitle + heightContent + imageId.getFitHeight() + 50);
                rectangleId.setHeight(scrollId.getPrefHeight() - 75);
                rectangleId.setOpacity(0.1);
            }
        });
    }
    @Override
    protected void updateItem(Post post, boolean empty){
        super.updateItem(post, empty);
        if (empty || post == null) {
            setText(null);
            setGraphic(null);
        } else {
            scrollId.setPannable(false);
            scrollId.setFitToHeight(true);
            titleId.setWrappingWidth(400);
            contentId.setWrappingWidth(400);
            avatarId.setLayoutY(10);
            titleId.setLayoutY(100);
            rectangleId.setLayoutY(80);
            paneId.setLayoutY(0);
            scrollId.setLayoutY(0);
            rectangleId.setDisable(false);
            rectangleId.setOpacity(0.6);
            rectangleId.toFront();
            imageId.setStyle("-fx-border-color: #333");
            // Load and set the first image URL in the ImageView
            Image image;
            try {
                image = new Image(post.getImage());
                imageId.setImage(image);
                imageId.setFitHeight(image.getHeight()*400/image.getWidth());
            }catch (Exception e){
                System.out.println("id = "+post.getId());
            }
            imageId.setFitWidth(400);
            Image avatar = new Image(post.getAvatar());
            avatarId.setFitWidth(60);
            avatarId.setFitHeight(60);
            avatarId.setImage(avatar);
            AppUtils.cropCircleImageView(avatarId);
            titleId.setText(post.getTitle());
            if (titleId.getText().length() > 28){
                titleId.setText(titleId.getText().substring(0, 28) + "...");
            }
            heightTitle = titleId.getLayoutBounds().getHeight();
            contentId.setText(post.getContent());
            if (contentId.getText().length() > 110){
                contentId.setText(contentId.getText().substring(0, 110) + "...");
            }
            heightContent = contentId.getLayoutBounds().getHeight();
            contentId.setLayoutY(titleId.getLayoutY() + heightTitle + 10);
            imageId.setLayoutY(contentId.getLayoutY() + heightContent - 5);
            usernameId.setText(post.getUsername());
            if (60 + heightTitle + heightContent + imageId.getFitHeight() + 50 > 460){
                scrollId.setPrefHeight(460);
                rectangleId.setHeight(385);
            }
            else{
                scrollId.setPrefHeight(60 + heightTitle + heightContent + imageId.getFitHeight() + 50);
                rectangleId.setHeight(scrollId.getPrefHeight() - 75);
            }
            postAnchorPane.setPrefHeight(postAnchorPane.getPrefHeight() + 20);
            setText(null);
            setGraphic(paneId);

        }
    }
//    @Override
//    public void updateSelected(boolean selected){
//        super.updateSelected(selected);
//        if (selected) {
//            Post post = getItem();
//            // Xử lý sự kiện tại đây khi phần tử được chọn
//            System.out.println(post);
//        }
//    }
}
