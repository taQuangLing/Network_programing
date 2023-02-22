package com.example.front_end;

import com.example.front_end.model.Post;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Ellipse;
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
                titleId.setText(post.getTitle());
                contentId.setText(post.getContent());
                double heightContent = contentId.getLayoutBounds().getHeight() - this.heightContent;
                double heightTitle = titleId.getLayoutBounds().getHeight() - this.heightTitle;
                contentId.setY(contentId.getY() + heightTitle);
                imageId.setY(imageId.getY() + heightContent + heightTitle + 10);
                rectangleId.setHeight(imageId.getFitHeight() + contentId.getLayoutBounds().getHeight() + titleId.getLayoutBounds().getHeight() - 70);
                scrollId.setPrefHeight(rectangleId.getHeight() + avatarId.getFitHeight());
                rectangleId.setOpacity(0.18);
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
            // Load and set the first image URL in the ImageView
            Image image = new Image(post.getImage());
            imageId.setImage(image);
            Image avatar = new Image(post.getAvatar());
            avatarId.setImage(avatar);
            titleId.setText(post.getTitle());
            titleId.setTextAlignment(TextAlignment.JUSTIFY);
            titleId.setWrappingWidth(380);
            if (titleId.getText().length() > 28){
                titleId.setText(titleId.getText().substring(0, 28) + "...");
            }else {
                titleId.setText(titleId.getText());
            }
            heightTitle = titleId.getLayoutBounds().getHeight();
            contentId.setText(post.getContent());
            contentId.setTextAlignment(TextAlignment.JUSTIFY);
            contentId.setWrappingWidth(421);
            if (contentId.getText().length() > 210){
                contentId.setText(contentId.getText().substring(0, 210) + "...");
            }else {
                contentId.setText(contentId.getText());
            }
            contentId.setY(contentId.getY()+heightTitle+10);
            heightContent = contentId.getLayoutBounds().getHeight();
            imageId.setY(imageId.getY() + heightContent + heightTitle + 20);
            usernameId.setText(post.getUsername());
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
