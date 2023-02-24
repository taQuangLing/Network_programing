package com.example.front_end.controller;

import com.example.front_end.NotificationCell;
import com.example.front_end.PostCell;
import com.example.front_end.appUtils.GlobalVariable;
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

import java.net.URL;
import java.util.ResourceBundle;

public class LayoutController implements Initializable {
    @FXML
    private ListView<Post> postListView;
    @FXML
    private ListView<Notification> notifyListView;
    @FXML Pane paneView;
    GlobalVariable globalVariable = GlobalVariable.getInstance();
    @SneakyThrows
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set cell factory for postListView
        postListView.setCellFactory(param -> new PostCell());
        postListView.setVisible(true);
        notifyListView.setVisible(false);
        paneView.setVisible(false);
        ObservableList<Post> postList = FXCollections.observableArrayList();
        // get posts from server
//        Data request = new Data(AppUtils.MessageCode.NEWS);
//        request.getData().add(1);
//        sendData(clientSock, request);
//
//        Data response = recvData(clientSock);
//        int count = Integer.valueOf((String) response.getData().get(0));
//        for (int i = 0; i < count; i++){
//            response = recvData(clientSock);
//            Post post = new Post(   Integer.valueOf((String) response.getData().get(0)),
//                                    Integer.valueOf((String) response.getData().get(1)),
//                                    (String) response.getData().get(2),
//                                    (String) response.getData().get(3),
//                                    (String) response.getData().get(4),
//                                    (String) response.getData().get(5),
//                                    (String) response.getData().get(6)
//                                    );
//            postList.add(post);
//        }
        postList.add(new Post(1,1, "TQLinh",
                "https://static.wixstatic.com/media/9d8ed5_b8fcc13f08fc4374bb1f979e032b0eb7~mv2.jpg/v1/fit/w_600,h_567,al_c,q_20,enc_auto/file.jpg",
                "DAC NHAN TAM",
                "Noi dung dac nhan tam...",
                "https://salt.tikicdn.com/cache/w1200/ts/product/df/7d/da/d340edda2b0eacb7ddc47537cddb5e08.jpg",
                ""
                ));
        postList.add(new Post(1,1, "Hoang Anh Tuan",
                "https://static.wixstatic.com/media/9d8ed5_b8fcc13f08fc4374bb1f979e032b0eb7~mv2.jpg/v1/fit/w_600,h_567,al_c,q_20,enc_auto/file.jpg",
                "DAC NHANAC NHAN TAM AC NHAN TAMAC NHAN TAM AC NHAN TAM AC NHAN TAM AC NHAN TAM AC NHAN TAM TAM",
                "Noi dung dac nhan tam..oi dung dac nhan taoi dung dac nhan taoi dung dac nhan taoi dung dac nhan taoi dung dac nhan taoi dung dac nhan taoi dung dac nhan taoi dung dac nhan taoi dung dac nhan taoi dung dac nhan taoi dung dac nhan taoi dung dac nhan taoi dung dac nhan ta.",
                "https://salt.tikicdn.com/cache/w1200/ts/product/df/7d/da/d340edda2b0eacb7ddc47537cddb5e08.jpg",
                ""
        ));
        postList.add(new Post(1,1, "Mai dao tuan thanh",
                "https://static.wixstatic.com/media/9d8ed5_b8fcc13f08fc4374bb1f979e032b0eb7~mv2.jpg/v1/fit/w_600,h_567,al_c,q_20,enc_auto/file.jpg",
                "DAC NHAN TAM AC NHAN TAMAC NHAN TAM AC NHAN TAM AC NHAN TAM AC NHAN TAM AC NHAN TAM",
                "Noi duoi dung dac nhan taoi dung dac nhan taoi dung dac nhan taoi dung dac nhan taoi dung dac nhan taoi dung dac nhan taoi dung dac nhan taoi dung dac nhan taoi dung dac nhan taoi dung dac nhan taoi dung dac nhan tavng dac nhan tam",
                "https://salt.tikicdn.com/cache/w1200/ts/product/df/7d/da/d340edda2b0eacb7ddc47537cddb5e08.jpg",
                ""
        ));
        postList.add(new Post(1,1, "LVHUNG",
                "https://static.wixstatic.com/media/9d8ed5_b8fcc13f08fc4374bb1f979e032b0eb7~mv2.jpg/v1/fit/w_600,h_567,al_c,q_20,enc_auto/file.jpg",
                "DAC NHAN TAM",
                "Noi dung dac nhan tam...",
                "https://salt.tikicdn.com/cache/w1200/ts/product/df/7d/da/d340edda2b0eacb7ddc47537cddb5e08.jpg",
                ""
        ));

        paneView.getChildren().add(0, globalVariable.getScreenController().getPane("create_post"));
//        System.out.println(globalVariable.getScreenController());
        paneView.getChildren().add(1, globalVariable.getScreenController().getPane("friends"));
        paneView.getChildren().add(2, globalVariable.getScreenController().getPane("search"));
        paneView.getChildren().add(3, globalVariable.getScreenController().getPane("profile"));
        postListView.setItems(postList);
    }
//    public void clickPost(ActionEvent e){
//        Post post = postListView.getSelectionModel().getSelectedItem();
//        if (post == null){
//            System.out.println("Post = NULL");
//        }else
//        System.out.println(post);
//    }

    public void createPostOnClick(MouseEvent e){
        System.out.println("create post click");
        postListView.setVisible(false);
        notifyListView.setVisible(false);
        paneView.setVisible(true);
        paneView.getChildren().get(0).setVisible(true);
        paneView.getChildren().get(1).setVisible(false);
        paneView.getChildren().get(2).setVisible(false);
        paneView.getChildren().get(3).setVisible(false);
    }
    public void homeOnClick(MouseEvent e){
        postListView.setVisible(true);
        paneView.setVisible(false);
        notifyListView.setVisible(false);
    }
    public void friendsOnClick(MouseEvent e){
        System.out.println("Friends click");
        postListView.setVisible(false);
        notifyListView.setVisible(false);
        paneView.setVisible(true);
        paneView.getChildren().get(1).setVisible(true);
        paneView.getChildren().get(0).setVisible(false);
        paneView.getChildren().get(2).setVisible(false);
        paneView.getChildren().get(3).setVisible(false);
        new FriendsController();
    }
    public void notificationOnClick(MouseEvent event){
        System.out.println("Notify clicked");
        postListView.setVisible(false);
        notifyListView.setVisible(true);
        paneView.setVisible(false);

        notifyListView.setCellFactory(param -> new NotificationCell());
        ObservableList<Notification> notifications = FXCollections.observableArrayList();
        notifications.add(new Notification(1, "Ta Quang Linh",
                "https://demoda.vn/wp-content/uploads/2022/02/anh-meo-cute-dang-yeu.jpeg",
                "Ta Quang Ling vua follow ban",
                "",
                false));
        notifications.add(new Notification(2, "hoang Anh Tuan",
                "https://toigingiuvedep.vn/wp-content/uploads/2022/01/anh-meo-cute.jpg",
                "Hoang Anh Tuan vua dang mot bai viet: ",
                "Dac Nhan Tam" +
                        "dac nhan tam la sach hay",
                false));
        notifications.add(new Notification(3, "Mai Dao Tuan Thanh",
                "https://static.wixstatic.com/media/9d8ed5_b8fcc13f08fc4374bb1f979e032b0eb7~mv2.jpg/v1/fit/w_600,h_567,al_c,q_20,enc_auto/file.jpg",
                "Mai Dao Tuan Thanh vua chap nhan loi moi ket ban",
                "",
                true));
        notifications.add(new Notification(4, "Le trong nghia",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcShFksgubjvBsya7NsCbgby4hskXh3UBIr97uWRIoM&s",
                "Le Trong Nghia vuawf dang mot bai viet:",
                "Dac Nhan Tam" +
                        "dac nhan tam la sach hay",
                true));
        notifyListView.setItems(notifications);
    }
    public void searchOnClick(MouseEvent event){
        System.out.println("Search clicked");
        postListView.setVisible(false);
        notifyListView.setVisible(false);
        paneView.setVisible(true);
        paneView.getChildren().get(0).setVisible(false);
        paneView.getChildren().get(1).setVisible(false);
        paneView.getChildren().get(2).setVisible(true);
        paneView.getChildren().get(3).setVisible(false);
        new SearchController();
    }
    public void profileOnClick(MouseEvent event){
        System.out.println("profile clicked");
        postListView.setVisible(false);
        notifyListView.setVisible(false);
        paneView.setVisible(true);
        paneView.getChildren().get(0).setVisible(false);
        paneView.getChildren().get(1).setVisible(false);
        paneView.getChildren().get(2).setVisible(false);
        paneView.getChildren().get(3).setVisible(true);
        new ProfileController();
    }

}
