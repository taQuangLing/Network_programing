package com.example.front_end.controller;

import com.example.front_end.PostCell;
import com.example.front_end.appUtils.AppUtils;
import com.example.front_end.model.Data;
import com.example.front_end.model.Post;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import lombok.SneakyThrows;

import java.net.URL;
import java.util.ResourceBundle;

import static com.example.front_end.appUtils.AppUtils.*;

public class NewsController implements Initializable {
    @FXML
    private ListView<Post> postListView;
    private ObservableList<Post> postList = FXCollections.observableArrayList();
    @SneakyThrows
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set cell factory for postListView
        postListView.setCellFactory(param -> new PostCell());
        // get posts from server
        Data request = new Data(AppUtils.MessageCode.NEWS);
        request.getData().add(1);
        sendData(clientSock, request);

        Data response = recvData(clientSock);
        int count = Integer.valueOf((String) response.getData().get(0));
        for (int i = 0; i < count; i++){
            response = recvData(clientSock);
            Post post = new Post(   Integer.valueOf((String) response.getData().get(0)),
                                    Integer.valueOf((String) response.getData().get(1)),
                                    (String) response.getData().get(2),
                                    (String) response.getData().get(3),
                                    (String) response.getData().get(4),
                                    (String) response.getData().get(5),
                                    (String) response.getData().get(6)
                                    );
            postList.add(post);
        }
        postListView.setItems(postList);
    }

}
