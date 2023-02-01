//
// Created by ling on 09/11/2022.
//
#include "MessageCode.h"
#include "stdio.h"

void print_mess(char *s1, char *s2){
    printf("\n\t%s, %s\n", s1, s2);
}
void send_message(MessageCode mess, char *param){
    switch (mess)
    {
        case USER_DUPLICATE:
            print_mess("Tai khoan da ton tai", param);
            break;
        case ACC_BLOCKED:
            print_mess("Tai khoan da bi khoa", param);
            break;
        case ACC_EXISTED:
            print_mess("Tai khoan khong ton tai", param);
            break;
        case LOGIN_SUCCESS:
            print_mess("DANG NHAP THANH CONG", param);
            break;
        case INCORRECT_PASS:
            print_mess("\nSai mat khau lan", param);
            break;
        case NOT_SIGN_IN:
            print_mess("Tai khoan chua duoc dang nhap", param);
            break;
        case LOGOUT_SUCCESS:
            print_mess("GOODBYE", param);
            break;
        case SIGNUP_SUCCESS:
            print_mess("Dang ki tai khoan thanh cong", param);
            break;
        case ACC_ACTIVED:
            print_mess("Tai khoan da duoc kich hoat", param);
            break;
        case ERROR_READFILE:
            print_mess("Loi doc file:", param);
            break;
        case ACC_NOTEXIST:
            print_mess("Tai khoan khong ton tai", param);
            break;
        case ERROR_PARAM:
            print_mess(param, "");
            break;
        default:
            break;
    }
}