//
// Created by ling on 16/11/2022.
//
#include <stdio.h>
#include <sys/socket.h>
#include <strings.h>
#include <netinet/in.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>
#include <arpa/inet.h>
#include <ctype.h>
#include <mysql/mysql.h>
#include "lib/http.h"
#include "structures/Data.h"
#include "lib/AppUtils.h"
#include "lib/config.h"
#include "lib/DB.h"

typedef struct sockaddr_in socketAddress;
int server_sock;
MYSQL *conn;


int login(int client, Param p){
    Data response;
    Param root, tail;
    char email[150];
    char password[PASSWORD_LEN];

    strcpy(email, p->value);
    p = p->next;
    strcpy(password, p->value);
    p = p->next;

    char sql[1000];
    refresh(sql, 1000);
    sprintf(sql, "select * from user where email = '%s' and password = '%s';", email, password);
    Table data = DB_get(&conn, sql);
    if (data->size == 0){
        logger(L_ERROR, "%s", "Email va mat khau khong hop le");
        response = data_create(NULL, INCORRECT_PASS);
        return 0;
    }else{
        logger(L_SUCCESS, "%s: %s", email, "da dang nhap");
        root = param_create(); // tail = root;
        param_add_int(&root, 1024); // nhieu hon 1 param thi truyen tail
        response = data_create(root, LOGIN_SUCCESS);
    }
    if (send_data(client, response, 0, 0) == -1){
        logger(L_ERROR, "%s", "fuction: login - 48");
        return -1;
    }
    return 1;
}


void handle_client(int client){
    Data request;
    Param p;
    int status;
    MessageCode option;
    while (0==0){
        request = recv_data(client, 0, 0);
        p = request->params;
        option = request->message;
        switch (option) {
            case LOGIN:
                status = login(client, p);
                if (status == 0)exit(0);
                break;
            case SIGNUP:
                break;
            case FORGOT:
                break;
            case NEWS:
                break;
            case OPEN:
                break;
            case COMMENT:
                break;
            case FRIEND:
                break;
            case DELETE_FRIEND:
                break;
            case FOLLOWING:
                break;
            case FOLLOWER:
                break;
            case PROFILE:
                break;
            case POST:
                break;
            case NOTIFY:
                break;
            case SEARCH:
                break;
            case EXIT:
                break;
            default:
                break;
        }
        data_free(&request);
    }
}
void server_listen() {
    // Number of child processes

    int client_sock = accept_connection(server_sock);
    handle_client(client_sock);
}
int main(int argc, char *argv[]){
    char i2s[10];
    conn = mysql_init(NULL);
    if (!mysql_real_connect(conn, SERVER, USER, PASSWORD, DATABASE, 0, NULL, 0)) {
        logger(L_ERROR, "%s", mysql_error(conn));
        exit(1);
    }
    refresh(i2s, 10);
    intTostr(PORT, i2s);
    server_sock = server_init_connect(i2s);
    printf("\t=======================\n");
    printf("\t=======================\n");
    printf("\t SERVER IS RUNNING\n");
    printf("\t=======================\n");
    printf("\t=======================\n");
    server_listen();
    return 1;
}