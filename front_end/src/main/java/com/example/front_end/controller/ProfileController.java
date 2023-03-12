package com.example.front_end.controller;

import com.example.front_end.PostCell;
import com.example.front_end.appUtils.AppUtils;
import com.example.front_end.appUtils.GlobalVariable;
import com.example.front_end.model.Data;
import com.example.front_end.model.Post;
import com.example.front_end.model.User;
import com.example.front_end.view.Message;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import lombok.SneakyThrows;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import static com.example.front_end.appUtils.AppUtils.*;
import static com.example.front_end.appUtils.AppUtils.MessageCode.*;

public class ProfileController implements Initializable {
    @FXML
    ImageView avatarImg;
    @FXML
    TextField name;
    @FXML TextField bio;
    @FXML MenuButton gender;
    @FXML
    DatePicker birthdayInput;
    @FXML Label birthdayLabel;
    @FXML TextField createdAt;
    @FXML TextField interest;
    @FXML
    ListView<Post> postListView;
    @FXML
    Pane profilePane;
    @FXML
    AnchorPane profileAnchorPane;
    @FXML
    MenuButton profileMenuButton;
    @FXML MenuButton userMenuButton;
    @FXML ImageView editAvatar;
    @FXML
    Button doneBtn;
    @FXML Label genderLabel;
    private File selectImage;
    private User user;
    private void setStyleDefault(){
        name.setStyle("-fx-border-color: #fff; -fx-font-size: 20px");
        genderLabel.setStyle("-fx-border-color: #fff; -fx-font-size: 15px; -fx-text-fill: #000000; -fx-background-color: white;");
        birthdayLabel.setStyle("-fx-border-color: #fff; -fx-font-size: 15px");
        interest.setStyle("-fx-border-color: #fff; -fx-font-size: 15px");
        bio.setStyle("-fx-border-color: #fff; -fx-font-size: 13px");
        createdAt.setStyle("-fx-border-color: #fff; -fx-font-size: 15px");
        name.setLayoutX(127);
        bio.setLayoutX(164);
        gender.setLayoutX(120);
        birthdayLabel.setLayoutX(134);
        createdAt.setLayoutX(165);
        interest.setLayoutX(116);
        name.setDisable(true);
        bio.setDisable(true);
        gender.setDisable(true);
        createdAt.setDisable(true);
        birthdayLabel.setVisible(true);
        interest.setDisable(true);
        profileMenuButton.setVisible(true);
        editAvatar.setVisible(false);
        doneBtn.setVisible(false);
        userMenuButton.setVisible(false);
        birthdayInput.setVisible(false);
        genderLabel.setVisible(true);
        gender.setVisible(false);
        interest.setPrefWidth(370);
    }
    private void setStyleEdit(){
        name.setStyle("-fx-border-color: #5792bb; -fx-font-size: 20px");
        bio.setStyle("-fx-border-color: #5792bb; -fx-font-size: 13px");
        gender.setStyle("-fx-border-color: #5792bb; -fx-font-size: 15px; -fx-text-fill: #000000; -fx-background-color: white;");
        interest.setStyle("-fx-border-color: #5792bb; -fx-font-size: 15px");
        createdAt.setStyle("-fx-border-color: #fff; -fx-font-size: 15px");
        birthdayInput.setStyle("-fx-border-color: #5792bb");
        name.setLayoutX(127);
        bio.setLayoutX(164);
        gender.setLayoutX(165);
        birthdayInput.setLayoutX(165);
        createdAt.setLayoutX(165);
        interest.setLayoutX(165);
        name.setDisable(false);
        bio.setDisable(false);
        gender.setDisable(false);
        birthdayInput.setVisible(true);
        interest.setDisable(false);
        profileMenuButton.setVisible(false);
        birthdayLabel.setVisible(false);
        gender.setVisible(true);
        genderLabel.setVisible(false);
        editAvatar.setVisible(true);
        doneBtn.setVisible(true);
        userMenuButton.setVisible(false);
        interest.setPrefWidth(315);
    }
    private ObservableList<Post> getPostFromServer() throws IOException {
        Data response = recvData(clientSock);
        int count = Integer.valueOf((String) response.getData().get(0));
        ObservableList<Post> postList = FXCollections.observableArrayList();
        for (int i = 0; i < count; i++){
            response = recvData(clientSock);
            Post post = new Post(
                    Integer.valueOf((String) response.getData().get(0)),
                    Integer.valueOf((String) response.getData().get(1)),
                    (String) response.getData().get(2),
                    (String) response.getData().get(3),
                    (String) response.getData().get(4),
                    (String) response.getData().get(5),
                    (String) response.getData().get(6)
            );
            postList.add(post);
        }
        return postList;
    }
    private void populate() throws IOException {
        setStyleDefault();
        // User user = recv_data from server
        // List<Post> posts = recv date from server
        Data request = new Data(PROFILE);
        request.getData().add(GlobalVariable.getInstance().getId());
        request.getData().add(GlobalVariable.getInstance().getId());
        sendData(clientSock, request);
        Data response = recvData(clientSock);
        user.setId(Integer.valueOf((String)response.getData().get(0)));
        user.setName((String)response.getData().get(1));
        user.setAvatar((String)response.getData().get(2));
        user.setBio((String)response.getData().get(3));
        switch (Integer.valueOf((String)response.getData().get(4))){
            case 0:
                user.setGender("Nam");
                break;
            case 1:
                user.setGender("Nữ");
                break;
            case 2:
                user.setGender("Khác");
                break;
        }
        if (response.getData().get(5) != ""){
            user.setBirthday(LocalDate.parse((String)response.getData().get(5)));
        }
        user.setCreatedAt(AppUtils.convertStringToDateTime((String)response.getData().get(6), "yyyy-MM-dd HH:mm:ss"));
        user.setInterest((String)response.getData().get(7));

        ObservableList<Post> postList = getPostFromServer();

        Image image = new Image(user.getAvatar());
        double width = image.getWidth();
        double height = image.getHeight();
        avatarImg.setImage(image);
        double ratio;
        if (image.getHeight() < image.getWidth()){
            ratio = height/110;
        }else{
            ratio = width/110;
        }
        avatarImg.setFitHeight(height/ratio);
        avatarImg.setFitWidth(width/ratio);
        AppUtils.cropCircleImageView(avatarImg);

        name.setText(user.getName());
        bio.setText(user.getBio());
        gender.setText(user.getGender());
        if (user.getBirthday() == null){
            birthdayLabel.setText("");
        }else birthdayLabel.setText(convertDateToString(user.getBirthday(), "dd-MM-yyyy"));
        createdAt.setText(convertDateToString(user.getCreatedAt().toLocalDate(), "dd-MM-yyyy"));
        interest.setText(user.getInterest());

        postListView.setCellFactory(param -> new PostCell());
        postListView.setItems(postList);
    }
    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        user = new User();
        populate();
    }
    public void editProfile(ActionEvent event){
        setStyleEdit();
        if (birthdayInput.getValue() == null)return;
        birthdayInput.setValue(convertStringToDate(birthdayLabel.getText(), "dd-MM-yyyy"));
        birthdayInput.setPromptText(birthdayLabel.getText());
    }
    public void logout(ActionEvent event){

    }
    public void done(MouseEvent event) throws IOException, URISyntaxException, InterruptedException {
        user.setName(this.name.getText());
        user.setBio(this.bio.getText());
        user.setGender(this.gender.getText());
        user.setBirthday(birthdayInput.getValue());
        user.setInterest(this.interest.getText());
        if (selectImage != null){
            user.setAvatar(UploadFileController.uploadFile(selectImage.getPath()));
        }
        Data request = new Data(EDIT_PROFILE);
        request.getData().add(GlobalVariable.getInstance().getId());
        request.getData().add(user.getName());
        request.getData().add(user.getAvatar());
        request.getData().add(user.getBio());
        request.getData().add(user.getGender());
        if (user.getBirthday() == null)request.getData().add("");
        else request.getData().add(convertDateToString(user.getBirthday(), "yyyy-MM-dd"));
        request.getData().add(user.getInterest());
        sendData(clientSock, request);
        Data response = recvData(clientSock);
        if (response.getMessageCode() == SUCCESS){
            name.setText(user.getName());
            bio.setText(user.getBio());
            genderLabel.setText(user.getGender());
            if (user.getBirthday() == null)birthdayLabel.setText("");
            else birthdayLabel.setText(convertDateToString(user.getBirthday(), "dd-MM-yyyy"));
            createdAt.setText(convertDateToString(user.getCreatedAt().toLocalDate(), "dd-MM-yyyy"));
            interest.setText(user.getInterest());
            populate();
        }else {
            Message message = new Message(ERROR);
            message.alert();
        }
    }
    public void editAvatar(MouseEvent e) throws MalformedURLException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn ảnh (< 10MB)");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Ảnh (*.png, *.jpg, *.gif)", "*.png", "*.jpg", "*.gif", "*jpeg");
        fileChooser.getExtensionFilters().add(extFilter);
        selectImage = fileChooser.showOpenDialog(profileAnchorPane.getScene().getWindow());
        if (selectImage != null) {
            Image image = new Image(selectImage.toURI().toURL().toExternalForm());
            avatarImg.setImage(image);
            if (selectImage.length() / (1024*1024) > 10){
                Message message = new Message(MessageCode.WARNING, "Vui lòng chọn ảnh có kích thước < 10MB");
                message.alert();
            }
        }

    }

    public void selectMale(ActionEvent event) {
        gender.setText("Nam");
        user.setGender("Nam");
    }

    public void selectFemale(ActionEvent event) {
        gender.setText("Nữ");
        user.setGender("Nữ");
    }

    public void selectOther(ActionEvent event) {
        gender.setText("Khác");
        user.setGender("Khác");
    }
}
