package com.example.front_end.controller;

import com.example.front_end.appUtils.AppUtils;
import com.example.front_end.appUtils.GlobalVariable;
import com.example.front_end.model.Data;
import com.example.front_end.view.Message;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

import static com.example.front_end.appUtils.AppUtils.*;
import static java.lang.System.exit;

public class CreatePostController {
    @FXML TextField titleText;
    @FXML TextArea contentText;
    @FXML MenuButton status;
    @FXML Button imageBtn;
    @FXML Button fileBtn;
    @FXML Button postBtn;
    @FXML Label imageLabel;
    @FXML Label fileLabel;
    @FXML Pane createPostPane;
    private File selectImage;
    private File selectFile;
    private Stage stage;
    private Message message;
    public void chooseImage(MouseEvent e){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn ảnh");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Ảnh (*.png, *.jpg, *.gif)", "*.png", "*.jpg", "*.gif", "*jpeg");
        fileChooser.getExtensionFilters().add(extFilter);
        selectImage = fileChooser.showOpenDialog(createPostPane.getScene().getWindow());
        if (selectImage != null) {
            String imagePath = selectImage.getAbsolutePath();
            System.out.println("Đường dẫn file ảnh: " + imagePath);
            imageLabel.setText(selectImage.getName());
        }
    }
    public void chooseFile(MouseEvent e){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn ảnh");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF (*.pdf)", "*.pdf");
        fileChooser.getExtensionFilters().add(extFilter);
        selectFile = fileChooser.showOpenDialog(createPostPane.getScene().getWindow());
        if (selectFile != null) {
            String filePath = selectFile.getAbsolutePath();
            System.out.println("Đường dẫn file ảnh: " + filePath);
            fileLabel.setText(selectFile.getName());
        }
    }
    public void post(MouseEvent e) throws IOException {
        String title = titleText.getText();
        String content = contentText.getText();
        if (title.isBlank() || content.isBlank() || selectFile == null || selectImage == null)return;
        Data request = new Data(AppUtils.MessageCode.POSTS);
//        request.getData().add(GlobalVariable.getInstance().getId());
        request.getData().add(2);
        request.getData().add(title);
        request.getData().add(content);
        if (status.getText().equals("Công khai")){
            request.getData().add(2);
        }else if (status.getText().equals("Bạn bè")){
            request.getData().add(1);
        }else if (status.getText().equals("Riêng tư")){
            request.getData().add(0);
        }
        sendData(clientSock, request);
        sendFile(selectImage);
        Data rsp = recvData(clientSock);
        switch(rsp.getMessageCode()){
            case FAIL:
                message = new Message(MessageCode.WARNING, "Lỗi gửi ảnh");
                message.alert();
                break;
            case ERROR:
                message = new Message(MessageCode.ERROR);
                message.alert();
                exit(-1);
        }
        AppUtils.sendFile(selectFile);
        rsp = recvData(clientSock);
        switch(rsp.getMessageCode()){
            case FAIL:
                message = new Message(MessageCode.WARNING, "Lỗi gửi ảnh");
                message.alert();
                break;
            case ERROR:
                message = new Message(MessageCode.ERROR);
                message.alert();
                exit(-1);
        }
        rsp = recvData(clientSock);
        if (rsp.getMessageCode() == MessageCode.SUCCESS){
            Message message = new Message(MessageCode.SUCCESS, "Bạn đã đăng bài viết thành công");
            message.alert();
            // Chuyen sang trang home
            GlobalVariable.getInstance().getScreenController().activate("layout");
            stage = (Stage)((Node)e.getSource()).getScene().getWindow();
            stage.setScene(GlobalVariable.getInstance().getScreenController().getMain());
            stage.show();
        }else{
            Message message = new Message(MessageCode.WARNING, "Vui lòng thử lại");
            message.alert();
        }
    }

    public void selectPublic(ActionEvent e){
        status.setText("Công khai");
    }
    public void selectFriend(ActionEvent e){
        status.setText("Bạn bè");
    }
    public void selectPrivate(ActionEvent e){
        status.setText("Riêng tư");
    }
}
