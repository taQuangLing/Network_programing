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
    int  bytes_sent, bytes_received;

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
        printf("\n============START============");
        printf("\n1. Login");
        printf("\n2. Open book");
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
            case 100:
                status = notify();
                break;
            case 111:
                status = exit_server();
                exit(0);
        }
    }

    close(client_sock);
    return 0;
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
