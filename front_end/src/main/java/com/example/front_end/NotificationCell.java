package com.example.front_end;

import com.example.front_end.appUtils.AppUtils;
import com.example.front_end.model.Data;
import com.example.front_end.model.Notification;
import com.example.front_end.view.Message;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.io.IOException;

import static com.example.front_end.appUtils.AppUtils.*;

public class NotificationCell extends ListCell<Notification> {
    @FXML
    ImageView avatarImg;
    @FXML Text username;
    @FXML Text title;
    @FXML Text content;
    @FXML Circle seen;
    @FXML Pane notifyPane;
    @FXML
    AnchorPane notificationAnchorPane;
    private FXMLLoader fxmlLoader;
    public NotificationCell(){
        super();
        try {
            fxmlLoader = new FXMLLoader(getClass().getResource("fxml/NotificationCell.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        avatarImg.setOnMouseClicked(e -> {

        });
        username.setOnMouseClicked(event -> {

        });
        notifyPane.setOnMouseClicked(event -> {
            // update seen ve server
            int index = getIndex();
            Notification notification = getListView().getItems().get(index);
            if (notification.isSeen())return;
            notification.setSeen(true);
            Data request = new Data(AppUtils.MessageCode.SEEN_NOTIFI);
            request.getData().add(1);
            request.getData().add(notification.getId());
            try {
                sendData(clientSock, request);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Data response;
            try {
                response = recvData(clientSock);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (response.getMessageCode() == MessageCode.ERROR){
                Message message = new Message(MessageCode.ERROR);
                message.alert();
                return;
            }else if(response.getMessageCode() == MessageCode.FAIL){
                Message message = new Message(MessageCode.WARNING, "Lỗi quyền truy cập");
                message.alert();
                return;
            }
            seen.setFill(Paint.valueOf("#E0E0E0"));
            seen.setStroke(Paint.valueOf("#E0E0E0"));
        });
    }
    @Override
    protected void updateItem(Notification notification, boolean empty){
        super.updateItem(notification, empty);
        if (empty || notification == null) {
            setText(null);
            setGraphic(null);
        } else {
            avatarImg.setImage(new Image(notification.getAvatar()));
            AppUtils.cropCircleImageView(avatarImg);
            username.setText(notification.getUsername());
            title.setText(notification.getTitle());
            content.setText(notification.getContent());
            if (notification.isSeen() == true){
                // color = trang
                seen.setFill(Paint.valueOf("#E0E0E0"));
                seen.setStroke(Paint.valueOf("#E0E0E0"));
            }else{
                // color = xanh
                seen.setFill(Paint.valueOf("green"));
                seen.setStroke(Paint.valueOf("#E0E0E0"));
            }
            setText(null);
            setGraphic(notificationAnchorPane);
        }
    }
}
