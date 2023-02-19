package com.example.front_end.appUtils;

import com.example.front_end.model.ClientSock;
import com.example.front_end.model.Data;

import java.io.IOException;

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
        CLOSE_CONNECTION(16), //16
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
        DISPLAY(28), //28
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
        WARNING(63);
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
    private static String dataToMessage(Data data){
        int size = 0;
        StringBuilder message = new StringBuilder();
        message.append(data.getMessageCode().value);
        message.append("#");
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
    public static void sendData(ClientSock clientSock, Data data) throws IOException {
        String message = dataToMessage(data);
        clientSock.send(message);
    }
}
