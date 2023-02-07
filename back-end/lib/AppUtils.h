//
// Created by ling on 02/12/2022.
//

#ifndef LAP_TRINH_MANG_APPUTILS_H
#define LAP_TRINH_MANG_APPUTILS_H
#include "Data.h"
char *data_to_str(Data data, int key);
Data str_to_data(char *str, int key);
int append(char *string, int index, char *data);
char *convert_timestamp(char *timestamp);
void intTostr(int num, char *str);
void refresh(char *str, int num);
int ping(int sock, int flag);
Data recv_data(int sock, int flag, int key);
int send_data(int sock, Data request, int flag, int key);
int checkChar(char ch);
int send_error(int sock, int flag);
void get_time_now(char *res);
void ceaser_decode(char srcStr[], char decodeStr[], int key);
void ceaser_encode(char srcStr[], char encodeStr[], int key);
int check_space(char *str);
void encode_password(char *pass);
int random_between(int from, int to);
void logger(char *type, const char *format, ...);
#endif //LAP_TRINH_MANG_APPUTILS_H
