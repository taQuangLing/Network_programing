package com.example.front_end;

import com.example.front_end.appUtils.AppUtils;
import com.example.front_end.model.Post;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.io.IOException;

public class SearchBookCell extends ListCell<Post> {
    @FXML
    ImageView bookImg;
    @FXML
    Text title;
    @FXML Text content;
    @FXML Text source;
    @FXML
    AnchorPane bookSearchAnchorPane;
    private FXMLLoader fxmlLoader;
    public SearchBookCell() {
        super();
        try {
            fxmlLoader = new FXMLLoader(getClass().getResource("fxml/SearchBookCell.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    protected void updateItem(Post post, boolean empty){
        super.updateItem(post, empty);
        if (empty || post == null) {
            setText(null);
            setGraphic(null);
        } else {
            Image image = new Image(post.getImage());
            double width = image.getWidth();
            double height = image.getHeight();
            bookImg.setImage(image);
            if ((width/88)/(height/122) < 1){
                double ratio = height/122;
                bookImg.setFitHeight(height/ratio);
                bookImg.setFitWidth(width/ratio);
            }else {
                double ratio = width/88;
                bookImg.setFitHeight(height/ratio);
                bookImg.setFitWidth(width/ratio);
            }

            title.setText(post.getTitle());
            title.setWrappingWidth(307);
            if (title.getText().length() > 23){
                title.setText(title.getText().substring(0, 23) + "...");
            }
            content.setText(post.getContent());
            content.setWrappingWidth(307);
            if (content.getText().length() > 185){
                content.setText(content.getText().substring(0, 185) + "...");
            }
            source.setText("Nguồn: " + post.getUsername());
            setText(null);
            setGraphic(bookSearchAnchorPane);
        }
    }
}
