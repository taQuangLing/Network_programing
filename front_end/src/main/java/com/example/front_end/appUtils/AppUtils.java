package com.example.front_end.appUtils;

import com.example.front_end.model.ClientSock;
import com.example.front_end.model.Data;
import com.example.front_end.model.Notification;
import com.example.front_end.model.Post;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import static com.example.front_end.appUtils.AppUtils.MessageCode.CANCEL;
import static com.example.front_end.appUtils.AppUtils.MessageCode.OK;

public class AppUtils {

    public static ClientSock clientSock;
    public enum MessageCode{
        USERNAME_DUPLICATE(0), //0
        EMAIL_DUPLICATE(1), //1
        ACC_EXISTED(2), //2
        LOGIN_SUCCESS(3), //3
        INCORRECT_PASS(4), //4
        NOT_SIGN_IN(5), //5
        LOGOUT_SUCCESS(6), //6
        SIGNUP_SUCCESS(7), //7
        ACC_ACTIVED(8), //8
        ERROR_READFILE(9), //9
        ACC_NOTEXIST(10), // 10
        MISSING_DATA(11), //11
        LOGGED(12), // 12
        ERROR_RECV(13), // 13
        LOGIN(14), // 14
        LOGOUT(15), // 15
        TOKEN_EXPIRED(16), //16
        ERROR_SYSTEM(17), //17
        ERROR_SEND(18), //18
        CHAT(19), //19
        PING(20), //20
        LOGOUT_ERROR(21), //21
        SUCCESS(22), //22
        FORKING_FAIL(23), // 23
        WAITPID_FAIL(24), //24
        CONECTION_FAIL(25), // 25
        ERR_SERVER_NOT_FOUND(26), //26
        ERROR_PARAM(27), //27
        TOKEN_NOTCORRECT(28), //28
        KEY(29), //29
        EXIT(30), //30
        SIGNUP(31), //31
        FORGOT(32), // 32
        NEWS(33), // 33
        OPEN(34), // 34
        COMMENT(35), // 35
        FRIENDS(36), //36
        DELETE_FRIEND(37), // 37
        FOLLOWING(38), // 38
        FOLLOWER(39), // 39
        PROFILE(40), // 40
        POSTS(41), // 41
        NOTIFY(42), // 42
        SEARCH(43), // 43
        COMMENTS(44),
        FAIL_OPEN_FILE(45),
        DONE(46),
        OK(47),
        MAINTENANCE(48),
        CANCEL(49),
        EMAIL_NOT_EXIST(50),
        ERROR(51),
        DATAS(52),
        UNFOLLOW(53),
        ACCEPT(54),
        ACCEPTED(55),
        FOLLOW(56),
        FOLLOWED(57),
        EDIT_PROFILE(58),
        EDIT_POSTS(59),
        REMOVE_POSTS(60),
        EMPTY(61),
        SEEN_NOTIFI(62),
        WARNING(63),
        FAIL(64),
        DELETE_FOLLOWER(65);
        private int value;
        MessageCode(int value) {
            this.value = value;
        }
    }
    public static MessageCode getMessageCode(int value){
        for (MessageCode messageCode : MessageCode.values()){
            if (value == messageCode.value)return messageCode;
        }
        return null;
    }
    private static Data messageToData(String message){
        int size = 0;
        String[] arr = message.split("#");
        Data data = new Data();
        data.setMessageCode(getMessageCode(Integer.parseInt(arr[0])));
        for (int i = 1; i < arr.length-1; i++){
            data.getData().add(arr[i]);
            size += arr[i].length();
        }
        data.setSize(Integer.parseInt(arr[arr.length-1]));
        if (size != data.getSize()){
            System.out.println("Missing Data");
            return null;
        }
        return data;
    }
    private static boolean checkMessageCode(MessageCode messageCode){
        switch (messageCode) {
            case LOGIN:
            case FORGOT:
            case CANCEL:
            case SIGNUP:
                return true;
            default:
                return false;
        }
    }
    private static String dataToMessage(Data data){
        int size = 0;
        StringBuilder message = new StringBuilder();
        message.append(data.getMessageCode().value);
        message.append("#");
        if (checkMessageCode(data.getMessageCode()) == false){
            message.append(GlobalVariable.getInstance().getToken());
            message.append("#");
        }
        for (Object item : data.getData()){
            message.append(item.toString());
            size += item.toString().length();
            message.append("#");
        }
        message.append(size);
        return message.toString();
    }
    public static Data recvData(ClientSock clientSock) throws IOException {
        String message = clientSock.recv();
        return messageToData(message);
    }
    public static byte[] recvDataV2(ClientSock clientSock) throws IOException {
        return  clientSock.recvV2();
    }
    public static void sendDataV2(ClientSock clientSock, byte[] data, int length) throws IOException {
        clientSock.sendV2(data, length);
    }
    public static void sendData(ClientSock clientSock, Data data) throws IOException {
        String message = dataToMessage(data);
        System.out.println(message);
        clientSock.send(message);
    }
    public static void cropCircleImageView(ImageView imageView) {
        // Tạo một hình tròn mới
        Circle circle = new Circle();
        double radius = Math.min(imageView.getFitWidth(), imageView.getFitHeight()) / 2;
        circle.setRadius(radius);
        circle.setCenterX(radius);
        circle.setCenterY(radius);

        // Sử dụng hình tròn mới như một mặt nạ để cắt ImageView
        imageView.setClip(circle);
    }
    public static void cropSquareImageView(ImageView imageView) {
        double width = imageView.getBoundsInParent().getWidth();
        double height = imageView.getBoundsInParent().getHeight();

        double size = Math.min(width, height);
        double x = (width - size) / 2;
        double y = (height - size) / 2;

        imageView.setFitWidth(size);
        imageView.setFitHeight(size);
        imageView.setTranslateX(x);
        imageView.setTranslateY(y);

        Rectangle clip = new Rectangle(size, size);
        clip.setArcWidth(10);
        clip.setArcHeight(10);
        imageView.setClip(clip);
    }
    public static void cropRectangleImageView(ImageView imageView, int width, int height) {
        double x = (imageView.getFitWidth() - width) / 2;
        double y = (imageView.getFitHeight() - height) / 2;
        imageView.setTranslateX(x);
        imageView.setTranslateY(y);
        Rectangle clip = new Rectangle(width, height);
        clip.setArcWidth(10);
        clip.setArcHeight(10);
        imageView.setClip(clip);
        imageView.setX(0);
        imageView.setY(0);
    }
    public static void writeFile(String filename) throws IOException {
        Stage stage = new Stage();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Directory");
        File initialDirectory = new File(System.getProperty("user.home"));
        directoryChooser.setInitialDirectory(initialDirectory);

        // Show directory chooser dialog
        File selectedDirectory = directoryChooser.showDialog(stage);

        if (selectedDirectory != null) {
            System.out.println("Selected directory: " + selectedDirectory.getAbsolutePath());
            // Download image from URL using the selected directory path
            Data request = new Data(OK);
            sendData(clientSock, request);
            File file = new File(selectedDirectory.getAbsolutePath() + '/' + filename);
            FileOutputStream fos = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fos);

            // Ghi dữ liệu vào tệp tin
            while (true) {
                byte[] data = recvDataV2(clientSock);
                bos.write(data);
                if (data.length < 1024) {
                    break;
                }
            }
            ProcessBuilder pb = new ProcessBuilder("evince", file.getPath());
            pb.start();
//            Desktop.getDesktop().open(file);
            // Đóng tệp tin
            bos.close();
            fos.close();
        }
        else{
            Data request = new Data(CANCEL);
            sendData(clientSock, request);
        }
    }
    public static void sendFile(File file) throws IOException, InterruptedException {
        Data request;
        FileInputStream fis = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(fis);
        if (file == null){
            request = new Data(AppUtils.MessageCode.EMPTY);
            sendData(clientSock, request);
        }else{
            request = new Data(AppUtils.MessageCode.OK);
            request.getData().add(file.getName());
            sendData(clientSock, request);
            Thread.sleep(100);
            byte[] buffer = new byte[1024];
            while(true){
                int bytesRead = bis.read(buffer);
                if (bytesRead == -1)break;
                sendDataV2(clientSock, buffer, bytesRead);
                Arrays.fill(buffer, (byte) 0);
                Thread.sleep(100);
            }
        }

        bis.close();
    }
    public static ObservableList<Post> getPostFromServer() throws IOException {
        Data request = new Data(AppUtils.MessageCode.NEWS);
        request.getData().add(GlobalVariable.getInstance().getId());
        sendData(clientSock, request);
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
    public static ObservableList<Notification> getNotificationFromServer() throws IOException {
        Data request = new Data(MessageCode.NOTIFY);
        request.getData().add(GlobalVariable.getInstance().getId()); // add id
        sendData(clientSock, request);

        ObservableList<Notification> notifyList = FXCollections.observableArrayList();
        Data response = recvData(clientSock);
        int count = Integer.valueOf((String) response.getData().get(0));
        for (int i = 0; i < count; i++){
            Data rspItem = recvData(clientSock);
            System.out.println(rspItem);
            Notification notifyItem = new Notification();
            System.out.println(notifyItem);
            notifyItem.setId(Integer.valueOf((String)rspItem.getData().get(0)));
            notifyItem.setUsername((String) rspItem.getData().get(1));
            notifyItem.setAvatar((String) rspItem.getData().get(2));
            notifyItem.setTitle((String) rspItem.getData().get(3));
            notifyItem.setContent((String) rspItem.getData().get(4));
            if (Integer.valueOf((String)rspItem.getData().get(5)) == 0){
                notifyItem.setSeen(false);
            }
            else notifyItem.setSeen(true);
            notifyList.add(notifyItem);
        }
        return notifyList;
    }
    public static LocalDateTime convertStringToDateTime(String time, String format){
        // create DateTimeFormatter object with pattern matching the String format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);

        // parse the String to LocalDateTime object
        LocalDateTime dateTime = LocalDateTime.parse(time, formatter);
        return dateTime;
    }
    public static LocalDate convertStringToDate(String time, String format){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format); // create a formatter
        return LocalDate.parse(time, formatter); // parse the string into a LocalDate object
    }
    public static String convertDateToString(LocalDate date, String format){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format); // create a formatter
        return date.format(formatter); // format the date as a string
    }
}
