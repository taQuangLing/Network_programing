//
// Created by ling on 09/11/2022.
//

#ifndef LAP_TRINH_MANG_MESSAGECODE_H
#define LAP_TRINH_MANG_MESSAGECODE_H

typedef enum{
    USERNAME_DUPLICATE = 0, //0
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
    SEEN_NOTIFI,
    WARNING,
    FAIL,
}MessageCode;

#endif //LAP_TRINH_MANG_MESSAGECODE_H
