package com.example.front_end;

import com.example.front_end.appUtils.AppUtils;
import com.example.front_end.model.Post;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.io.IOException;

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
            }
        });
        rectangleId.setOnMouseClicked(mouseEvent -> {
            int index = getIndex();
            if (index >= 0 && index < getListView().getItems().size()) {
                Post post = getListView().getItems().get(index);
                // Mo rong sach
                System.out.println("rectangle clicked");
//                rectangleId.setVisible(false);
                rectangleId.toBack();
                rectangleId.toBack();
                rectangleId.toBack();
                rectangleId.setDisable(true);
                titleId.setText(post.getTitle());
                contentId.setText(post.getContent());
                double heightContent = contentId.getLayoutBounds().getHeight() - this.heightContent;
                double heightTitle = titleId.getLayoutBounds().getHeight() - this.heightTitle;
                contentId.setY(contentId.getY() + heightTitle);
                imageId.setY(imageId.getY() + heightContent + heightTitle + 10);
                rectangleId.setHeight(imageId.getFitHeight() + contentId.getLayoutBounds().getHeight() + titleId.getLayoutBounds().getHeight() - 70);
                scrollId.setPrefHeight(rectangleId.getHeight() + avatarId.getFitHeight());
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
            int index = getIndex();
            // Load and set the first image URL in the ImageView
            Image image = new Image(post.getImage());
            imageId.setImage(image);
            Image avatar = new Image(post.getAvatar());
            avatarId.setFitWidth(60);
            avatarId.setFitHeight(60);
            avatarId.setImage(avatar);
            AppUtils.cropCircleImageView(avatarId);
            titleId.setText(post.getTitle());
            titleId.setTextAlignment(TextAlignment.JUSTIFY);
            titleId.setWrappingWidth(380);
            if (titleId.getText().length() > 28){
                titleId.setText(titleId.getText().substring(0, 28) + "...");
            }
            heightTitle = titleId.getLayoutBounds().getHeight();
            contentId.setText(post.getContent());
            contentId.setTextAlignment(TextAlignment.JUSTIFY);
            contentId.setWrappingWidth(421);
            if (contentId.getText().length() > 210){
                contentId.setText(contentId.getText().substring(0, 210) + "...");
            }
            contentId.setY(contentId.getY()+heightTitle+10);
            heightContent = contentId.getLayoutBounds().getHeight();
            imageId.setY(imageId.getY() + heightContent + heightTitle + 20);
            usernameId.setText(post.getUsername());
            if (index == 0){
                scrollId.setLayoutY(scrollId.getLayoutY() + 70);
            }
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
