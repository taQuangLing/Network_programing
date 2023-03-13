package com.example.front_end.controller;

import com.example.front_end.appUtils.AppUtils;
import com.example.front_end.appUtils.GlobalVariable;
import com.example.front_end.model.Data;
import com.example.front_end.view.Message;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

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
    @FXML
    AnchorPane createPostAnchorPane;
    private File selectImage;
    private File selectFile;
    private Message message;
    @FXML Label titleStar;
    @FXML Label contentStar;
    @FXML Label imageStar;
    @FXML Label fileStar;
    @FXML Label processingLabel;
    @FXML
    Rectangle createPostRectangle;
    @FXML ProgressBar createPostProgressBar;
    public void chooseImage(MouseEvent e){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn ảnh (< 10MB)");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Ảnh (*.png, *.jpg, *.gif)", "*.png", "*.jpg", "*.gif", "*jpeg");
        fileChooser.getExtensionFilters().add(extFilter);
        selectImage = fileChooser.showOpenDialog(createPostAnchorPane.getScene().getWindow());
        if (selectImage != null) {
            String imagePath = selectImage.getAbsolutePath();
            if (selectImage.length() / (1024*1024) > 10){
                message = new Message(MessageCode.WARNING, "Vui lòng chọn ảnh có kích thước < 10MB");
                message.alert();
                return;
            }
            System.out.println("Đường dẫn file ảnh: " + imagePath);
            imageLabel.setText(selectImage.getName());
            imageLabel.setVisible(true);
        }
    }
    public void chooseFile(MouseEvent e) throws IOException, InterruptedException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn File");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF (*.pdf)", "*.pdf");
        fileChooser.getExtensionFilters().add(extFilter);
        selectFile = fileChooser.showOpenDialog(createPostAnchorPane.getScene().getWindow());
        if (selectFile != null) {
            String filePath = selectFile.getAbsolutePath();
            System.out.println("Đường dẫn file ảnh: " + filePath);
            fileLabel.setText(selectFile.getName());
            fileLabel.setVisible(true);
        }
    }
    public void postTask(Data request) {
        createPostRectangle.setVisible(true);
        createPostProgressBar.setManaged(true);
        createPostProgressBar.setVisible(true);
        processingLabel.setVisible(true);
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                // handle
                sendData(clientSock, request);
                Thread.sleep(100);
                AppUtils.sendFile(selectFile);

                Data rsp = recvData(clientSock);
                switch (rsp.getMessageCode()) {
                    case FAIL:
                        message = new Message(MessageCode.WARNING, "Lỗi gửi file");
                        return null;
                    case ERROR:
                        message = new Message(MessageCode.ERROR);
                        return null;
                }
                rsp = recvData(clientSock);
                if (rsp.getMessageCode() == MessageCode.SUCCESS) {
                    // Chuyen sang trang home
                    message = new Message(MessageCode.SUCCESS, "Tạo bài viết thành công");
                    titleText.setText("");
                    contentText.setText("");
                    selectImage = null;
                    selectFile = null;
                    fileStar.setVisible(false);
                    imageStar.setVisible(false);
                    titleStar.setVisible(false);
                    contentStar.setVisible(false);
                    fileLabel.setVisible(false);
                    imageLabel.setVisible(false);
                    createPostAnchorPane.getParent().setVisible(false);

                } else {
                    message = new Message(MessageCode.WARNING, "Vui lòng thử lại");
                }
                return null;
            }
        };
        createPostProgressBar.progressProperty().bind(task.progressProperty());
        Thread createPostThread = new Thread(task);
        createPostThread.start();
        task.setOnSucceeded(e -> {
            message.alert();
            if (message.getCode() == MessageCode.ERROR)exit(0);
            createPostRectangle.setVisible(false);
            createPostProgressBar.setManaged(false);
            createPostProgressBar.setVisible(false);
            processingLabel.setVisible(false);
        });
    }
    public void post(MouseEvent e) throws IOException, InterruptedException, URISyntaxException {
        String title = titleText.getText();
        if (title.length() > 70){
            message = new Message(MessageCode.WARNING, "Số ký tự tiêu đề không vượt quá 70");
            message.alert();
            return;
        }
        if (title.isBlank()){
            titleStar.setVisible(true);
            return;
        }else titleStar.setVisible(false);
        String content = contentText.getText();
        if (content.length() > 400){
            message = new Message(MessageCode.WARNING, "Số ký tự nội dung không vượt quá 400");
            message.alert();
            return;
        }
        if (content.isBlank()){
            contentStar.setVisible(true);
            return;
        }else contentStar.setVisible(false);
        if (selectImage == null){
            imageStar.setVisible(true);
            return;
        }else imageStar.setVisible(false);
        if (selectFile == null){
            fileStar.setVisible(true);
            return;
        }else fileStar.setVisible(false);

        String image = UploadFileController.uploadFile(selectImage.getPath());
        if (title.isBlank() || content.isBlank() || selectFile == null || selectImage == null)return;
        Data request = new Data(AppUtils.MessageCode.POSTS);
        request.getData().add(GlobalVariable.getInstance().getId());
        request.getData().add(title);
        request.getData().add(content);
        if (status.getText().equals("Công khai")){
            request.getData().add(2);
        }else if (status.getText().equals("Bạn bè")){
            request.getData().add(1);
        }else if (status.getText().equals("Riêng tư")){
            request.getData().add(0);
        }
        request.getData().add(image);
        postTask(request);

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
