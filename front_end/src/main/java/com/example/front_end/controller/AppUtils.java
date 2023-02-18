package com.example.front_end.controller;

import com.example.front_end.model.Data;

import java.io.IOException;

public class AppUtils {
    public enum MessageCode{
        USERNAME_DUPLICATE, //0
        EMAIL_DUPLICATE, //1
        ACC_EXISTED, //2
        LOGIN_SUCCESS, //3
        INCORRECT_PASS, //4
        NOT_SIGN_IN, //5
        LOGOUT_SUCCESS, //6
        SIGNUP_SUCCESS, //7
        ACC_ACTIVED, //8
        ERROR_READFILE, //9
        ACC_NOTEXIST, // 10
        MISSING_DATA, //11
        LOGGED, // 12
        ERROR_RECV, // 13
        LOGIN, // 14
        LOGOUT, // 15
        CLOSE_CONNECTION, //16
        ERROR_SYSTEM, //17
        ERROR_SEND, //18
        CHAT, //19
        PING, //20
        LOGOUT_ERROR, //21
        SUCCESS, //22
        FORKING_FAIL, // 23
        WAITPID_FAIL, //24
        CONECTION_FAIL, // 25
        ERR_SERVER_NOT_FOUND, //26
        ERROR_PARAM, //27
        DISPLAY, //28
        KEY, //29
        EXIT, //30
        SIGNUP, //31
        FORGOT, // 32
        NEWS, // 33
        OPEN, // 34
        COMMENT, // 35
        FRIENDS, //36
        DELETE_FRIEND, // 37
        FOLLOWING, // 38
        FOLLOWER, // 39
        PROFILE, // 40
        POSTS, // 41
        NOTIFY, // 42
        SEARCH, // 43
        COMMENTS,
        FAIL_OPEN_FILE,
        DONE,
        OK,
        MAINTENANCE,
        CANCEL,
        EMAIL_NOT_EXIST,
        ERROR,
        DATAS,
        UNFOLLOW,
        ACCEPT,
        ACCEPTED,
        FOLLOW,
        FOLLOWED,
        EDIT_PROFILE,
        EDIT_POSTS,
        REMOVE_POSTS,
        EMPTY,
        SEEN_NOTIFI;
    }
    private static Data messageToData(String message){
        int size = 0;
        String[] arr = message.split("#");
        Data data = new Data();
        data.setMessageCode(MessageCode.valueOf(arr[0]));
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
        StringBuilder message = new StringBuilder();
        message.append(data.getMessageCode());
        message.append("#");
        for (Object item : data.getData()){
            message.append(item);
            message.append("#");
        }
        message.append(data.getSize());
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
