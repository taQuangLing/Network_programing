package com.example.front_end;

import com.example.front_end.appUtils.AppUtils;
import com.example.front_end.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.io.IOException;

public class UserCell extends ListCell<User> {
    @FXML Pane friendsCellPane;
    @FXML
    AnchorPane friendsCellAnchorPane;
    @FXML
    ImageView avatarImg;
    @FXML
    Text username;
    @FXML
    MenuButton friendsMenuBtn;
    @FXML
    MenuButton followerMenuBtn;
    @FXML
    MenuItem cancelFollowing;
    @FXML MenuItem follow;
    @FXML MenuItem deleteFollower;
    private FXMLLoader fxmlLoader;
    private int status;

    public UserCell(int status) {
        super();
        try {
            this.status = status;
            fxmlLoader = new FXMLLoader(getClass().getResource("fxml/FriendsCell.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        avatarImg.setOnMouseClicked(e -> {

        });
        username.setOnMouseClicked(e -> {

        });
        cancelFollowing.setOnAction(e ->{

        });
        follow.setOnAction(e -> {

        });
        deleteFollower.setOnAction(e -> {

        });
    }
    @Override
    protected void updateItem(User user, boolean empty){
        super.updateItem(user, empty);
        if (empty || user == null) {
            setText(null);
            setGraphic(null);
        } else {
            Image image = new Image(user.getAvatar());
            double width = image.getWidth();
            double height = image.getHeight();
            avatarImg.setImage(image);
            double ratio;
            if (image.getHeight() < image.getWidth()){
                ratio = height/60;
            }else{
                ratio = width/60;
            }
            avatarImg.setFitHeight(height/ratio);
            avatarImg.setFitWidth(width/ratio);
            AppUtils.cropCircleImageView(avatarImg);
            avatarImg.setStyle("-fx-shape: \"M0,0 L" + avatarImg.getFitWidth() + ",0 A" + avatarImg.getFitWidth()/2 + "," + avatarImg.getFitHeight()/2 + " 0 0,0 " + avatarImg.getFitWidth() + "," + avatarImg.getFitHeight() + " L0," + avatarImg.getFitHeight() + " A" + avatarImg.getFitWidth()/2 + "," + avatarImg.getFitHeight()/2 + " 0 0,0 0,0 Z\"");
            username.setText(user.getName());
            switch (status){
                case 0:
                    friendsMenuBtn.setVisible(false);
                    followerMenuBtn.setVisible(false);
                    break;
                case 1:
                case 2:
                    friendsMenuBtn.setVisible(true);
                    followerMenuBtn.setVisible(false);
                    break;
                case 3:
                    friendsMenuBtn.setVisible(false);
                    followerMenuBtn.setVisible(true);
                    break;
            }
            setText(null);
            setGraphic(friendsCellAnchorPane);
        }
    }
}
