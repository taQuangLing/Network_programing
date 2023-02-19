package com.example.front_end;

import com.example.front_end.model.Post;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.io.IOException;

public class PostCell extends ListCell<Post> {
    @FXML
    ImageView imageId;
    @FXML ImageView avatarId;
    @FXML Text titleId;
    @FXML Text contentId;
    @FXML Text usernameId;
    @FXML ScrollPane scrollId;
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
            contentId.setText(post.getContent());
            usernameId.setText(post.getUsername());

            setText(null);
            setGraphic(scrollId);
        }
    }

}
