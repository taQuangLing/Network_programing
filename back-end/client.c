//
// Created by Tạ Quang Linh on 16/11/2022.
//
#include <stdio.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <sys/socket.h>
#include <string.h>
#include <unistd.h>
#include <stdlib.h>
#include "lib/MessageCode.h"
#include "lib/config.h"
#include "lib/AppUtils.h"


int id;
int client_sock;


int notify();

int open_book();

void write_file();

int exit_server();

int sign_up();

int forgot_password();

int login(){
    char enter;
    char i2s[10];
    char email[EMAIL_LEN];
    char password[PASSWORD_LEN];
    Data request, response;
    Param root = param_create();
    Param tail = root, param;
    refresh(email, EMAIL_LEN);
    refresh(password, PASSWORD_LEN);
    refresh(i2s, 10);


    printf("\nNhap email: ");
    scanf("%s", email);
    scanf("%c", &enter);
    printf("\nNhap password: ");
    scanf("%s", password);
    scanf("%c", &enter);

    param_add_str(&tail, email);
    param_add_str(&tail, password);
    request = data_create(root, LOGIN);

    if (send_data(client_sock, request, 0, 0) == -1){
        logger(L_ERROR, "%s", "login - 36");
        return -1;
    }
    response = recv_data(client_sock, 0, 0);
    param = response->params;
    if (response->message == LOGIN_SUCCESS){
        strcpy(i2s, param->value);
        id = atoi(i2s);
        logger(L_SUCCESS, "%s: %s", "login success", email);
        return 1;
    }else{
        logger(L_SUCCESS, "%s: %s", "Sai email hoac mat khau", email);
        return 0;
    }
    // 1 - thanh cong, 0 - that bai, -1 - loi
}
int main(int argc, char *argv[]){

    char idAddress[] = "127.0.0.1";
    int port = atoi(argv[1]);

    struct sockaddr_in server_addr; /* server's address information */
    int  bytes_sent, bytes_received, connection = 0;

    //Step 1: Construct socket
    client_sock = socket(AF_INET,SOCK_STREAM,0);

    //Step 2: Specify server address
    server_addr.sin_family = AF_INET;
    server_addr.sin_port = htons(port);
    server_addr.sin_addr.s_addr = inet_addr(idAddress);

    //Step 3: Request to connect server
    if(connect(client_sock, (struct sockaddr*)&server_addr, sizeof(struct sockaddr)) < 0){
        printf("\nError!Can not connect to sever! Client exit imediately! ");
        return 0;
    }
    //Step 4: Communicate with server

    int menu;
    char enter;
    int status;
    while (1){
        connection = 0;
        printf("\n============START============");
        printf("\n1. Login");
        printf("\n2. Open book");
        printf("\n3. Sign up");
        printf("\n4. Forgot password");
        printf("\n100. Notification");
        printf("\nYour choice: ");

        scanf("%d", &menu);
        scanf("%c", &enter);
        switch (menu) {
            case 1:
                status = login();
                break;
            case 2:
                status = open_book();
                break;
            case 3 :
                status = sign_up();
                break;
            case 4:
                status = forgot_password();
                break;
            case 100:
                status = notify();
                break;
            case 111:
                status = exit_server();
                connection = 1;
                break;
        }
        if (connection == 1)break;
    }

    close(client_sock);
    return 0;
}

int forgot_password() {
    char email[EMAIL_LEN] = {0}, password[PASSWORD_LEN] = {0}, confirm_pwd[PASSWORD_LEN];
    char enter;
    printf("\nNhap email > ");
    scanf("%s", email);
    scanf("%c", &enter);
    Param root = param_create(), tail;

    param_add_str(&root, email);
    Data request = data_create(root, FORGOT);
    if (send_data(client_sock, request, 0, 0) == -1){
        logger(L_ERROR, "function: forgot_password()");
        return -1;
    }
    // Waiting key
    Data response = recv_data(client_sock, 0, 0);
    if (response->message == MAINTENANCE){
        logger(L_INFO, "He thong dang bao tri!");
        data_free(&response);
        return -1;
    }if (response->message == EMAIL_NOT_EXIST){
        logger(L_INFO, "Email khong ton tai!");
        data_free(&response);
        return -1;
    }
    int key = param_get_int(&response->params);
    printf("\nThiet lap lai mat khau!");
    while(1){
        printf("\nNhap password: ");
        scanf("%s", password);
        scanf("%c", &enter);
        printf("\nNhap confirm password: ");
        scanf("%s", confirm_pwd);
        scanf("%c", &enter);
        if (check_space(password) == 0 ||
            check_space(confirm_pwd) == 0){
            printf("Khong duoc bo trong!");
            continue;
        }
        if (strcmp(password, confirm_pwd) != 0){
            printf("Mat khau xac nhan khong dung!");
            continue;
        }
        break;
    }
    root = param_create();
    tail = root;
    param_add_int(&tail, key);
    param_add_str(&tail, password);
    request = data_create(root, FORGOT);
    send_data(client_sock, request, 0, 0);
    data_free(&response);

    response = recv_data(client_sock, 0, 0);
    if (response->message == SUCCESS){
        logger(L_SUCCESS, "Cap nhat mat khau moi thanh cong!");
    }
    else
    if (response->message == ERROR){
        logger(L_ERROR, "Loi he thong");
    }
    data_free(&response);
    return 1;
}

int sign_up() {
    char enter;
    char email[EMAIL_LEN] = {0}, password[PASSWORD_LEN] = {0}, confirm_pwd[PASSWORD_LEN] = {0}, username[USERNAME_LEN] = {0};
    int status;

    while(1){
        printf("\nNhap email: ");
        scanf("%s", email);
        scanf("%c", &enter);
        printf("\nNhap username: ");
        scanf("%s", username);
        scanf("%c", &enter);
        printf("\nNhap password: ");
        scanf("%s", password);
        scanf("%c", &enter);
        printf("\nNhap confirm password: ");
        scanf("%s", confirm_pwd);
        scanf("%c", &enter);
        if (check_space(email) == 0 ||
            check_space(username) == 0 ||
            check_space(password) == 0 ||
            check_space(confirm_pwd) == 0){
            printf("Khong duoc bo trong!");
            continue;
        }
        if (strcmp(password, confirm_pwd) != 0){
            printf("Mat khau xac nhan khong dung!");
            continue;
        }
        break;

    }

    Param root = param_create(), tail = root;
    param_add_str(&tail, email);
    param_add_str(&tail, username);
    param_add_str(&tail, password);

    Data request = data_create(root, SIGNUP);
    if (send_data(client_sock, request, 0, 0) == -1){
        logger(L_ERROR, "function: sign_up() - 169");
        return -1;
    }
    Data response = recv_data(client_sock, 0, 0);

    if (response->message == EMAIL_DUPLICATE){
        logger(L_WARN, "Email da duoc dang ky!");
        status = 0;
    }else{
        logger(L_SUCCESS, "Dang ky thanh cong!");
        status = 1;
    }
    data_free(&response);
    return status;
}

int exit_server() {
    Data response = data_create(NULL, EXIT);
    if (send_data(client_sock, response, 0, 0) == -1){
        logger(L_ERROR, "function: exit_server() - 126");
        return -1;
    }
    return 1;
}

int open_book() {
    int post_id;
    char enter;
    printf("Nhap id post(sach) = ");
    scanf("%d", &post_id);
    scanf("%c", &enter);

    Param root = param_create(), tail = root;
    param_add_int(&tail, id);
    param_add_int(&tail, post_id);
    Data request = data_create(root, OPEN);
    send_data(client_sock, request, 0, 0);

    Data response = recv_data(client_sock, 0, 0);
    if (response->message == FAIL){
        logger(L_WARN, "%s", "Lỗi không có quyền truy cập");
    }else{
        write_file();
    }

    data_free(&response);
    return 1;
}

void write_file() {
    int n;
    FILE *fp;
    char *filename;
    char buffer[BUFF_SIZE];

    Data response = recv_data(client_sock, 0, 0);
    filename = param_get_str(&response->params);
    data_free(&response);

    fp = fopen(filename, "wb");
    while (1) {
        n = recv(client_sock, buffer, BUFF_SIZE, 0);
        if (fwrite(buffer, 1, n, fp) < n)
            logger(L_ERROR, "function: write_file()");
        bzero(buffer, BUFF_SIZE);
        printf("\nn = %d", n);
        if (n < BUFF_SIZE)break;
    }
    fclose(fp);
    char command[100] = {0};
    sprintf(command, "gopen %s", filename);
    system(command);
}

int notify() {
    int count;
    Data request, response;
    Param root = param_create();
    param_add_int(&root, id);
    request = data_create(root, NOTIFY);
    send_data(client_sock, request, 0, 0);
    // waiting from server
    response = recv_data(client_sock, 0, 0);

    if (response->message == FAIL){
        logger(L_WARN, "%s", response->params->value);
        return 0;
    }
    int seen, i;
    char *content, *username, *image;
    count = param_get_int(&response->params);
    data_free(&response);
    for (i = 0; i < count; i++){
        response = recv_data(client_sock, 0, 0);

        Param p = response->params;
        username = param_get_str(&p);
        image = param_get_str(&p);
        content = param_get_str(&p);
        seen = param_get_int(&p);

        if (seen == 1){
            printf("\n\t%s(img=\"%s\") %s (đã xem)\n", username, image, content);
        }else
            printf("\n\t%s(img=\"%s\")\n\t %s (chưa xem)\n",  username, image, content);

        data_free(&response);
    }
    return 1;
}
