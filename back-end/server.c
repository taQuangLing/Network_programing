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
#include "lib/Data.h"
#include "lib/AppUtils.h"
#include "lib/config.h"
#include "lib/DB.h"

typedef struct sockaddr_in socketAddress;
int server_sock;
MYSQL *conn;


int notify(int client, Param p);

int open_book(int client, Param p);

void send_file(int client, char *path);

int login(int client, Param p){
    Data response;
    Param root;
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
        for (int j = 0; j < data->column; j++){
            if (strcmp(data->header[j], "id") == 0){
                param_add_str(&root, data->data[0][j]); // nhieu hon 1 param thi truyen tail
                break;
            }
        }
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
    int status, connection = 1;
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
                status = open_book(client, p);
                break;
            case COMMENT:
                break;
            case COMMENTS:
                break;
            case FRIENDS:
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
                status = notify(client, p);
                break;
            case SEARCH:
                break;
            case EXIT:
                close(client);
                connection = 0;
                break;
            default:
                break;
        }
        data_free(&request);
        if (connection == 0)break;
    }
}

int open_book(int client, Param p) {
    Data response;
    int user_id = param_get_int(&p);
    int post_id = param_get_int(&p);

    Table data = DB_get_by_id(&conn, "post", post_id);
    int user_id_t = atoi(get_by(data, "user_id"));
    char *path;
    if (user_id != user_id_t){
        response = data_create(NULL, FAIL);
    }else{
        response = data_create(NULL, SUCCESS);
        send_data(client, response, 0, 0);
        usleep(1000);
        path = get_by(data, "path");
        send_file(client, path);
    }
    return 1;
}

void send_file(int client, char *path) {
    Data response;
    FILE *f;
    int i, n;
    if ((f = fopen(path, "rb")) == NULL){
        response = data_create(NULL, FAIL_OPEN_FILE);
        send_data(client, response, 0, 0);
        logger(L_ERROR, "Lỗi mở file %s", path);
        return;
    }
    char data[BUFF_SIZE] = {0};
    char file_name[50] = {0};
    for (i = strlen(path)-1; i >= 0; i--){
        if (path[i] == '/'){
            strcpy(file_name, path+i+1);
            break;
        }
    }
    if (i == -1){
        strcpy(file_name, path);
    }
    Param root = param_create();
    param_add_str(&root, file_name);
    response = data_create(root, OPEN);
    send_data(client, response, 0, 0);
    usleep(1000);

    while ((n = fread(data, 1, BUFF_SIZE, f)) > 0) {
        if (send(client, data, n, 0) == -1) {
            logger(L_ERROR, "function: send_file()");
            break;
        }
        usleep(1000);
        bzero(data, BUFF_SIZE);
    }
    fclose(f);
    logger(L_SUCCESS, "File %s sent successfully.", path);
}

int notify(int client, Param p) {
    Data response;
    Param root = param_create(), tail = root;
    int i, j, type, seen, link_id;
    Table result1, result2;
    int toId = param_get_int(&p);
    char username[USERNAME_LEN], avatar[150], title[20], content[40], noidung[200];
    char sql[1000];
    refresh(sql, 1000);
    sprintf(sql, "select user.name, avatar, type, seen, link_id from notification, user where from_user_id = user.id and to_user_id = %d", toId);
    result1= DB_get(&conn, sql);

    if (result1->size == 0){
        param_add_str(&root, "No record!");
        response = data_create(root, FAIL);
        if (send_data(client, response, 0, 0) == -1){
            logger(L_ERROR, "%s - function: notify() - 126");
            return -1;
        }
    }
    param_add_int(&root, result1->size);
    response = data_create(root, NOTIFY);
    send_data(client, response, 0, 0);
    usleep(500);
    for (i = 0; i < result1->size; i++){
        root = param_create();
        tail = root;
        refresh(username, USERNAME_LEN);
        refresh(avatar, 150);
        refresh(title, 20);
        refresh(content, 40);

        strcpy(username, result1->data[i][0]);
        strncpy(avatar, result1->data[i][1], 30);
        type = atoi(result1->data[i][2]);
        seen = atoi(result1->data[i][3]);
        link_id = atoi(result1->data[i][4]);
        if (type == 2){
            // thong bao ve bai post
            result2 = DB_get_by_id(&conn, "post", link_id);
            for (j = 0; j < result2->column; j++){
                if (strcmp(result2->header[j], "title") == 0){
                    strncpy(title, result2->data[0][j], 20);
                }
                if (strcmp(result2->header[j], "content") == 0){
                    strncpy(content, result2->data[0][j], 40);
                }
            }
            sprintf(noidung, "đã đăng bài viết: %s...\n\t%s...", title, content);
            DB_free_data(&result2);
        }else if(type == 3){
            // thong bao ve comment
            sprintf(sql, "select comment.content as content, title from post, comment where post.id = comment.post_id and comment.id = %d", link_id);
            result2 = DB_get_by_id(&conn, "comment", link_id);
            for (j = 0; j < result2->column; j++){
                if (strcmp(result2->header[j], "content") == 0){
                    strncpy(content, result2->data[0][j], 40);
                }
                if (strcmp(result2->header[j], "title") == 0){
                    strncpy(title, result2->data[0][j], 20);
                }
            }
            sprintf(noidung, "đã bình luận bài viết: %s...\n%s...", title, content);
            DB_free_data(&result2);
        }
        else {
            // thong bao ve loi moi ket ban
            sprintf(noidung, "đã gửi lời mời kết bạn");
        }
        param_add_str(&tail, username);
        param_add_str(&tail, avatar);
        param_add_str(&tail, noidung);
        param_add_int(&tail, seen);
        response = data_create(root, NOTIFY);
        if (send_data(client, response, 0, 0) == -1){
            logger(L_ERROR, "%s - function: notify() - 176");
            return -1;
        }
        usleep(500);
    }
    return 1;
}

void server_listen() {
    // Number of child processes

    int client_sock = accept_connection(server_sock);
    handle_client(client_sock);
}
int main(int argc, char *argv[]){
    char *port = argv[1];

    char i2s[10];
    conn = mysql_init(NULL);
    if (!mysql_real_connect(conn, SERVER, USER, PASSWORD, DATABASE, 0, NULL, 0)) {
        logger(L_ERROR, "%s", mysql_error(conn));
        exit(1);
    }
    refresh(i2s, 10);
    intTostr(PORT, i2s);
    server_sock = server_init_connect(port);
    printf("\t=======================\n");
    printf("\t=======================\n");
    printf("\t SERVER IS RUNNING\n");
    printf("\t=======================\n");
    printf("\t=======================\n");
    server_listen();
    close(server_sock);
    return 1;
}