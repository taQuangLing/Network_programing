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

int sign_up(int client, Param pParam);

int forgot_password(int client, Param p);

int search(int client, Param p);

void send_list_data(int client, Table data);

int news(int client, Param p);

int friends(int client, Param p);

int unfollow(int client, Param p);

int following(int client, Param p);

int follower(int client, Param p);

int accept_friend(int client, Param p);

int interaction(int client, Param p, MessageCode type);

int follow(int client, Param p);

int profile(int client, Param p);

int edit_profile(int client, Param p);

int posts(int client, Param p);

int edit_posts(int client, Param p);

int remove_posts(int client, Param p);

int seen_notifi(int client, Param p);

int delete_follower(int client, Param p);

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
        logger(L_WARN, "%s", "Email va mat khau khong hop le");
        response = data_create(NULL, INCORRECT_PASS);
        send_data(client, response, 0, 0);
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
        if (request == NULL){
            option = EXIT;
        }else{
            p = request->params;
            option = request->message;
        }
        switch (option) {
            case LOGIN:
                status = login(client, p);
                if (status == -1)exit(0);
                break;
            case SIGNUP:
                status = sign_up(client, p);
                break;
            case FORGOT:
                status = forgot_password(client, p);
                break;
            case NEWS:
                status = news(client, p);
                break;
            case OPEN:
                status = open_book(client, p);
                break;
            case COMMENT:
                break;
            case COMMENTS:
                break;
            case FRIENDS:
                status = friends(client, p);
                break;
            case UNFOLLOW:
                status = unfollow(client, p);
                break;
            case FOLLOWING:
                status = following(client, p);
                break;
            case FOLLOWER:
                status = follower(client, p);
                break;
            case DELETE_FOLLOWER:
                status = delete_follower(client, p);
                break;
            case ACCEPT:
                status = accept_friend(client, p);
                break;
            case FOLLOW:
                status = follow(client, p);
                break;
            case PROFILE:
                status = profile(client, p);
                break;
            case EDIT_PROFILE:
                status = edit_profile(client, p);
                break;
            case POSTS:
                status = posts(client, p);
                break;
            case EDIT_POSTS:
                status = edit_posts(client, p);
                break;
            case REMOVE_POSTS:
                status = remove_posts(client, p);
                break;
            case NOTIFY:
                status = notify(client, p);
                break;
            case SEARCH:
                status = search(client, p);
                break;
            case SEEN_NOTIFI:
                status = seen_notifi(client, p);
                break;
            case EXIT:
                close(client);
                connection = 0;
                break;
            default:
                break;
        }
        if (request != NULL)data_free(&request);
        if (connection == 0)break;
    }
}

int delete_follower(int client, Param p) {
    Data response;
    int userid = param_get_int(&p), others_id = param_get_int(&p);
    char sql[1000] = {0};
    sprintf(sql, "select id, status from follow where user_id = %d and others_id = %d", others_id, userid);
    Table data = DB_get(&conn, sql);
    if (data == NULL){
        return send_error(client);
    }
    if (data->size == 0){
        response = data_create(NULL, FAIL);
        send_data(client, response, 0, 0);
        return 1;
    }
    int status = atoi(DB_str_get_by(data, "status"));
    if (status != 0){
        response = data_create(NULL, DELETE_FOLLOWER);
        send_data(client, response, 0, 0);
        return 1;
    }
    int id = atoi(DB_str_get_by(data, "id"));
    char *key[] = {"status"};
    char *value[] = {"-1"};
    if (DB_update(&conn, "follow", id, key, value, 1) == -1){
        response = data_create(NULL, FAIL);
        send_data(client, response, 0, 0);
        return 0;
    }
    refresh(sql, 1000);
    response = data_create(NULL, SUCCESS);
    send_data(client, response, 0, 0);
    return 1;
}

int seen_notifi(int client, Param p) {
    int userid = param_get_int(&p);
    int noti_id = param_get_int(&p);
    Table data = DB_get_by_id(&conn, "notification", noti_id);
    int userid_t = DB_int_get_by(data, "to_user_id");
    if (userid_t != userid){
        Data response = data_create(NULL, FAIL);
        send_data(client, response, 0, 0);
        return 0;
    }
    char sql[1000] = {0};
    sprintf(sql, "update notification set seen = 1 where id = %d", noti_id);
    if (DB_update_v2(&conn, sql) == -1){
        return send_error(client);
    }
    return send_success(client);
}

int remove_posts(int client, Param p) {
    int userid = param_get_int(&p);
    int postid = param_get_int(&p);
    Table data = DB_get_by_id(&conn, "post", postid);
    int userid_t = DB_int_get_by(data, "user_id");
    if (userid_t != userid){
        return send_fail(client);
    }
    if (DB_delete(&conn, "post", postid) == -1)return send_error(client);
    return send_success(client);
}

int edit_posts(int client, Param p) {
    char *title, *content, *image, time_now[20] = {0};
    int status, userid;
    userid = param_get_int(&p);
    int postid = param_get_int(&p);
    title = param_get_str(&p);
    content = param_get_str(&p);
    image = param_get_str(&p);
    status = param_get_int(&p);
    get_time_now(time_now, NULL);

    Table data = DB_get_by_id(&conn, "post", postid);
    int userid_t = DB_int_get_by(data, "user_id");

    if (userid != userid_t){
        Data response = data_create(NULL, FAIL);
        send_data(client, response, 0, 0);
        return 0;
    }
    else{
        send_success(client);
    }
    char sql[1000] = {0};
    char path[150] = {0};
    sprintf(path, "kho");
    int state = write_file(client, path);
    if (state == -1){
        return send_error(client);
    }else if (state == 0){
        sprintf(sql, "update post set title = '%s', content = '%s', image = '%s', status = %d where id = %d", title, content, image, status, postid);
        if (DB_insert_v2(&conn, sql) == -1){
            return send_error(client);
        }
        return send_success(client);
    }
    sprintf(sql, "update post set title = '%s', content = '%s', image = '%s', status = %d, path = '%s' where id = %d", title, content, image, status, path, postid);
    if (DB_insert_v2(&conn, sql) == -1){
        return send_error(client);
    }
    return send_success(client);}

int posts(int client, Param p) {
    char *title, *content, time_now[20] = {0}, *image;
    int status, userid, rsp;
    userid = param_get_int(&p);
    title = param_get_str(&p);
    content = param_get_str(&p);
    status = param_get_int(&p);
    image = param_get_str(&p);
    get_time_now(time_now, NULL);

    Table data2 = DB_get(&conn, "select max(id) as id from post");
    int post_id = DB_int_get_by(data2, "id"); // get id bai viet vua dang
    char sql[1000] = {0};
    char path[150] = {0};
    sprintf(path, "kho/%d-%d-", userid, post_id+1); // chon duong dan file
    rsp = write_file(client, path); // luu file
    usleep(1000);
    if (rsp == -1){
        return send_error(client);
    }else if (rsp == 0)return send_fail(client);
    else send_success(client); // tra ve ket qua write file
    usleep(1000);
    sprintf(sql, "insert into post (user_id, title, content, image, status, created_at, path) value"
                 "(%d, '%s', '%s', '%s', %d, '%s', '%s')", userid, title, content, image, status, time_now, path);
    if (DB_insert_v2(&conn, sql) == -1){ // insert table
        return send_error(client);
    }
    Table data;
    int others_id;
    refresh(sql, 1000);
    if (status == 1){
        // friend
        sprintf(sql, "select others_id from follow where user_id = %d and status = 1 "
                     "union select user_id as others_id from follow where others_id = %d and status = 1", userid, userid);
        data2 = DB_get(&conn, "select max(id) as id from post");
        post_id = DB_int_get_by(data2, "id");
        data = DB_get(&conn, sql);

        for (int i = 0; i < data->size; i++){
            refresh(sql, 1000);
            others_id = atoi(data->data[i][0]);
            sprintf(sql, "insert into notification (to_user_id, from_user_id, type, seen, link_id) value (%d, %d, 0, 0, %d)",
                   others_id , userid, post_id);
            DB_insert_v2(&conn, sql);
        }
        DB_free_data(&data);
        DB_free_data(&data2);
    }else if (status == 2){
        // public
        sprintf(sql, "select others_id from follow where user_id = %d and status = 1 "
                     "union select user_id as others_id from follow where others_id = %d and status = 1 "
                     "union select user_id as others_id from follow where others_id = %d and status = 0 ", userid, userid, userid);

        data2 = DB_get(&conn, "select max(id) as id from post");
        post_id = DB_int_get_by(data2, "id");
        data = DB_get(&conn, sql);
        for (int i = 0; i < data->size; i++){
            refresh(sql, 1000);
            others_id = atoi(data->data[i][0]);
            sprintf(sql, "insert into notification (to_user_id, from_user_id, type, seen, link_id) value (%d, %d, 0, 0, %d)",
                    others_id, userid, post_id);
            DB_insert_v2(&conn, sql);
        }
        DB_free_data(&data);
        DB_free_data(&data2);
    }
    return send_success(client);
}

int edit_profile(int client, Param p) {
    char *username, *image, *bio, *birthday, *interest, gender_s[2] = {0};
    int gender, userid;
    userid = param_get_int(&p);
    username = param_get_str(&p);
    image = param_get_str(&p);
    bio = param_get_str(&p);
    gender = param_get_int(&p);
    birthday = param_get_str(&p);
    interest = param_get_str(&p);
    intTostr(gender, gender_s);

    Table data = DB_get_by_id(&conn, "user", userid);
    if (check_space(username) != 0){
        DB_update_cell(data, "name", username);
    }
    DB_update_cell(data, "avatar", image);
    DB_update_cell(data, "bio", bio);
    DB_update_cell(data, "gender", gender_s);
    DB_update_cell(data, "birthday", birthday);
    DB_update_cell(data, "interest", interest);

    if (DB_update_v3(&conn,data) == -1){
        send_error(client);
    }
    return send_success(client);
}

int profile(int client, Param p) {
    char sql[1000] = {0};
    Table data;
    int userid = param_get_int(&p);
    int others_id = param_get_int(&p);

    sprintf(sql, "select id, name, avatar, bio, gender, birthday, created_at, interest from user where id = %d", others_id);
    data = DB_get(&conn, sql);
    if (data == NULL){
        return send_error(client);
    }
    send_list_data(client, data);
    sleep(100);
    DB_free_data(&data);
    // send news of user id
    if (userid == others_id){
        sprintf(sql, "select post.id as id, user.id as user_id, name, avatar, title, content, image from post, user where user_id = user.id and user_id = %d", userid);
    }else{
        sprintf(sql, "select * from follow where ((user_id = %d and others_id = %d) or (others_id = %d and user_id = %d)) and status = 1", userid, others_id, userid, others_id);
        data = DB_get(&conn, sql);
        if (data->size == 0){
            sprintf(sql, "select post.id as id, user.id as user_id, name, avatar, title, content, image from post, user where user_id = user.id and user_id = %d and status = 2", others_id);
        }else{
            sprintf(sql, "select post.id as id, user.id as user_id, name, avatar, title, content, image from post, user where user_id = user.id and user_id = %d and (status = 1 or status = 2)", others_id);
        }
        DB_free_data(&data);
    }
    data = DB_get(&conn, sql);
    if (data == NULL)return send_error(client);
    send_list_data(client, data);
    DB_free_data(&data);
    return 1;
}

int follow(int client, Param p) {
    Data response;
    int userid = param_get_int(&p), others_id = param_get_int(&p);
    char sql[1000] = {0};
    sprintf(sql, "select id, user_id, status from follow where (user_id = %d and others_id = %d) or (user_id = %d and others_id = %d)", userid, others_id, others_id, userid);
    Table data = DB_get(&conn, sql);
    int userid_t = DB_int_get_by(data, "user_id");
    if (data == NULL){
        send_error(client);
    }

    if (data->size == 0){
        sprintf(sql, "insert into follow (user_id, others_id, status) value (%d, %d, 0)", userid, others_id);
        DB_insert_v2(&conn, sql);
        response = data_create(NULL, SUCCESS);
        send_data(client, response, 0, 0);
        return 1;
    }else{
        int status = DB_int_get_by(data, "status");
        if (status == 0){
            response = data_create(NULL, FOLLOWED);
            send_data(client, response, 0, 0);
            return 1;
        }else if (status == 1){
            response = data_create(NULL, ACCEPTED);
            send_data(client, response, 0, 0);
            return 1;
        }
    }
    int id = DB_int_get_by(data, "id");
    if (userid != userid_t){
        sprintf(sql, "update follow set user_id = %d, others_id = %d, status = %d where id = %d", others_id, userid, 0, id);
    }else
        sprintf(sql, "update follow set status = %d where id = %d", 0, id);
    if (DB_update_v2(&conn, sql) == -1){
        response = data_create(NULL, FAIL);
        send_data(client, response, 0, 0);
        return 0;
    }

    refresh(sql, 1000);
    refresh(sql, 1000);
    sprintf(sql, "insert into notification (to_user_id, from_user_id, type, seen) value (%d, %d, %d, 0)", others_id, userid, 2);
    if (DB_insert_v2(&conn, sql) == -1){
        logger(L_WARN, "Function: accept_friend()");
        response = data_create(NULL, FAIL);
        send_data(client, response, 0, 0);
        return 0;
    }
    response = data_create(NULL, SUCCESS);
    send_data(client, response, 0, 0);
    return 1;
}

int accept_friend(int client, Param p) {
    Data response;
    int userid = param_get_int(&p), others_id = param_get_int(&p);
    char sql[1000] = {0};
    sprintf(sql, "select id, status from follow where user_id = %d and others_id = %d", others_id, userid);
    Table data = DB_get(&conn, sql);
    if (data == NULL){
        return send_error(client);
    }
    if (data->size == 0){
        response = data_create(NULL, FAIL);
        send_data(client, response, 0, 0);
        return 1;
    }
    int status = atoi(DB_str_get_by(data, "status"));
    if (status != 0){
        response = data_create(NULL, ACCEPTED);
        send_data(client, response, 0, 0);
        return 1;
    }
    int id = atoi(DB_str_get_by(data, "id"));
    char *key[] = {"status"};
    char *value[] = {"1"};
    if (DB_update(&conn, "follow", id, key, value, 1) == -1){
        response = data_create(NULL, FAIL);
        send_data(client, response, 0, 0);
        return 0;
    }
    refresh(sql, 1000);
    sprintf(sql, "insert into notification (to_user_id, from_user_id, type, seen) value (%d, %d, %d, 0)", others_id, userid, 3);
    if (DB_insert_v2(&conn, sql) == -1){
        logger(L_WARN, "Function: accept_friend()");
        return 0;
    }
    response = data_create(NULL, SUCCESS);
    send_data(client, response, 0, 0);
    return 1;
}

int follower(int client, Param p) {
    return interaction(client, p, FOLLOWER);
}

int following(int client, Param p) {
    return interaction(client, p, FOLLOWING);
}

int unfollow(int client, Param p) {
    Data response;
    int userid = param_get_int(&p), others_id = param_get_int(&p);
    char sql[1000] = {0};
    sprintf(sql, "select id,user_id, status from follow where (user_id = %d and others_id = %d) or (user_id = %d and others_id = %d)", userid, others_id, others_id, userid);
    Table data = DB_get(&conn, sql);
    if (data->size == 0){
        response = data_create(NULL, FAIL);
        send_data(client, response, 0, 0);
        return 0;
    }
    int id = DB_int_get_by(data, "id");
    int userid_t = DB_int_get_by(data, "user_id");
    int status = atoi(DB_str_get_by(data, "status"));

    if (data == NULL){
        send_error(client);
    }
    if (status == -1 || data->size == 0 || (status == 0 && userid != userid_t)){
        response = data_create(NULL, FAIL);
        send_data(client, response, 0, 0);
        return 0;
    }

    if (status == 1){
        if (userid == userid_t)sprintf(sql, "update follow set user_id = %d, others_id = %d, status = %d where id = %d", others_id, userid, 0, id);
        else{
            sprintf(sql, "update follow set status = %d where id = %d", 0, id);
        }
    }else{
        if (userid == userid_t)sprintf(sql, "update follow set status = %d where id = %d", -1, id);
    }
    if (DB_update_v2(&conn, sql) == -1){
        response = data_create(NULL, FAIL);
        send_data(client, response, 0, 0);
        return 0;
    }
    response = data_create(NULL, SUCCESS);
    send_data(client, response, 0, 0);
    return 1;
}

int interaction(int client, Param p, MessageCode type) {
    int userid = param_get_int(&p);
    char sql[1000] = {0};
    Data response;
    switch (type) {
        case FRIENDS:
            sprintf(sql, "select others_id, name, avatar, status from follow, user where follow.others_id = user.id and status = 1 and user_id = %d\n"
                         "union\n"
                         "select user_id as others_id, name, avatar, status from follow, user where follow.user_id = user.id and status = 1 and others_id = %d", userid, userid);
            break;
        case FOLLOWING:
//            sprintf(sql, "select others_id, name, avatar, status from follow, user where follow.others_id = user.id and user_id = %d and (status = 0 or status = 1)\n"
//                         "union\n"
//                         "select user_id as others_id, name, avatar, status from follow, user where follow.user_id = user.id and others_id = %d and status = 1", userid, userid);
            sprintf(sql, "select others_id, name, avatar, status from follow, user where follow.others_id = user.id and user_id = %d and (status = 0)", userid);
            break;
        case FOLLOWER:
//            sprintf(sql, "select user_id as others_id, name, avatar, status from follow, user where follow.user_id = user.id and others_id = %d and (status = 0 or status = 1)\n"
//                         "union\n"
//                         "select others_id, name, avatar, status from follow, user where follow.others_id = user.id and user_id = %d and status = 1", userid, userid);
            sprintf(sql, "select user_id as others_id, name, avatar, status from follow, user where follow.user_id = user.id and others_id = %d and status = 0", userid);
            break;
        default:
            logger(L_ERROR, "Lỗi nhiều hơn 3 trạng thái interaction - 187");
            return send_error(client);
    }
    Table data = DB_get(&conn, sql);
    if (data == NULL){
        response = data_create(NULL, ERROR);
        send_data(client, response, 0, 0);
        return -1;
    }
    send_list_data(client, data);
    return 1;
}

int friends(int client, Param p) {
    return interaction(client, p, FRIENDS);
}

int news(int client, Param p) {
    int userid = param_get_int(&p);

    char sql[1500] = {0};
    sprintf(sql, "select post.id, user.id, name, avatar, title, content, image from post, user where user.id = post.user_id and\n"
                 "(((select COUNT(*) from follow where ((user_id = %d and others_id = post.user_id) or (user_id = post.user_id and others_id = %d)) and status = 1) > 0 and (status = 1 or status = 2))\n"
                 "or\n"
                 "((select count(*) from follow where (user_id = %d and others_id = post.user_id) and status = 0) > 0 and status = 2))\n"
                 "order by post.created_at desc;", userid, userid, userid);
    Table data = DB_get(&conn, sql);
    send_list_data(client, data);
    DB_free_data(&data);
    return 1;
}

int search(int client, Param p) {
    char *keyword, text_search[51] = {0};
    int userid, type;
    char sql[1000] = {0};

    userid = param_get_int(&p);
    keyword = param_get_str(&p);
    type = param_get_int(&p);

    strcpy(text_search+1, keyword);
    text_search[0] = '%';
    int lenght = (int)strlen(text_search);
    text_search[lenght] = '%';
    text_search[lenght+1] = '\0';
    if (type == 0){
        // search sach
        sprintf(sql, "(select post.id, user.id, name, image, title, content from post, user where user.id = post.user_id and (title LIKE '%s' or content LIKE '%s') and "
                     "(select COUNT(*) from follow where ((user_id = %d and others_id = post.user_id) or (user_id = post.user_id and others_id = %d)) and status = 1) > 0 and (status = 1) "
                     "order by post.created_at desc) "
                     "union "
                     "(select post.id, user.id, name, image, title, content from post, user where user.id = post.user_id and (title LIKE '%s' or content LIKE '%s') and status = 2 "
                     "order by post.created_at desc) ", text_search, text_search, userid, userid, text_search, text_search);
    }else
    {
        // search nguoi
        sprintf(sql, "select id, name, avatar from user where name LIKE '%s'", text_search);
    }
    Table data = DB_get(&conn, sql);

    send_list_data(client, data);

    return 1;
}
void send_a_record(int client, Table data, int i){
    Param root, tail;
    root = param_create();
    tail = root;
    for (int j = 0; j < data->column; j++){
        if (data->data[i][j] == NULL)param_add_str(&tail, "");
        else
            param_add_str(&tail, data->data[i][j]);
    }
    Data response = data_create(root, DATAS);
    send_data(client, response, 0, 0);
}
void send_list_data(int client, Table data){
    Data response;
    Param root, tail;

    root = param_create();
    tail = root;
    param_add_int(&tail, (int)data->size);
    response = data_create(root, OK);
    send_data(client, response, 0, 0);
    usleep(1000);

    for (int i = 0; i < data->size; i++){
        send_a_record(client, data, i);
        usleep(100);
    }
}

int forgot_password(int client, Param p) {
    Param root;
    int id;
    char sql[1000] = {0};
    char *email = param_get_str(&p);
    Data response, request;

    sprintf(sql, "select * from user where email = '%s'", email);
    Table data = DB_get(&conn, sql);
    if (data->size == 1){
        id = atoi(DB_str_get_by(data, "id"));
        root = param_create();
        param_add_int(&root, id);
        response = data_create(root, OK);
        send_data(client, response, 0, 0);
        DB_free_data(&data);
    }else
    if (data->size > 1){
        logger(L_WARN, "Tai khoan trung email: %", email);
        response = data_create(NULL, MAINTENANCE);
        send_data(client, response, 0, 0);
        DB_free_data(&data);
        return 1;
    }else
    {
        response = data_create(NULL, EMAIL_NOT_EXIST);
        send_data(client, response, 0, 0);
        DB_free_data(&data);
        return 1;
    }
    request = recv_data(client, 0, 0);
    if (request->message == CANCEL)return 1;
    int key = param_get_int(&request->params);
    char *password = param_get_str(&request->params);
    char *field[] = {"password"};
    char *value[] = {password};
    if (DB_update(&conn, "user", key, field, value, 1) == 1){
        response = data_create(NULL, SUCCESS);
    }else{
        response = data_create(NULL, ERROR);
    }
    send_data(client, response, 0, 0);
    data_free(&request);
    return 0;
}

int sign_up(int client, Param p) {
    char *username, *password, *email;
    email = param_get_str(&p);
    username = param_get_str(&p);
    password = param_get_str(&p);
    Table data;
    Data response;
    char sql[1000] = {0};

    // check email
    sprintf(sql, "select * from user where email = '%s'", email);
    data = DB_get(&conn, sql);
    if (data->size > 0){
        response = data_create(NULL, EMAIL_DUPLICATE);
        send_data(client, response, 0, 0);
        DB_free_data(&data);
        return 1;
    }
    DB_free_data(&data);

    // insert
    char now[20] = {0};
    char *key[] = {"email", "name", "created_at", "password"};
    get_time_now(now, NULL);
    char *value[] = {email, username, now, password};
    if (DB_insert(&conn, "user", key, value, 4) == -1){
        mysql_close(conn);
        return -1;
    }
    response = data_create(NULL, SUCCESS);
    if (send_data(client, response, 0, 0) == -1){
        logger(L_ERROR, "function: sign_up");
        return -1;
    }
    return 1;
}

int open_book(int client, Param p) {
    Data response;
    char sql[1000] = {0};
    int user_id = param_get_int(&p);
    int post_id = param_get_int(&p);

    Table data = DB_get_by_id(&conn, "post", post_id);
    Table data2;
    int post_userid = DB_int_get_by(data, "user_id");
    int status = DB_int_get_by(data, "status");
    char *path;
    if ((path = DB_str_get_by(data, "path")) == NULL){
        return send_error(client);
    }
    if (post_userid == user_id)status = 2;
    if (status == 0) {
        // 0: private, 1: friend, 2: public
        response = data_create(NULL, FAIL);
        send_data(client, response, 0, 0);
        return 0;
    }else if(status == 1){
        sprintf(sql, "select * from follow where (user_id = %d and others_id = %d) or (user_id = %d and others_id = %d) and status = 1", user_id, post_userid, post_userid, user_id);
        data2 = DB_get(&conn, sql);
        if (data2->size == 0){
            response = data_create(NULL, FAIL);
            send_data(client, response, 0, 0);
            DB_free_data(&data2);
            return 0;
        }
        DB_free_data(&data2);
    }
    response = data_create(NULL, OK);
    send_data(client, response, 0, 0);
    usleep(1000);
    send_file(client, path);
    return 1;
}

int notify(int client, Param p) {
    Data response;
    Param root = param_create(), tail = root;
    int i, type, seen, link_id, id;
    Table result1, result2;
    int toId = param_get_int(&p);
    char *username, *avatar, *title, *content, noidung[200] = {0}, tieude[100] = {0};
    char sql[1000];
    refresh(sql, 1000);
    sprintf(sql, "select notification.id as id, user.name as name, avatar, type, seen, link_id from notification, user where from_user_id = user.id and to_user_id = %d", toId);
    result1= DB_get(&conn, sql);

    param_add_int(&root, (int)result1->size);
    response = data_create(root, NOTIFY);
    send_data(client, response, 0, 0);
    usleep(500);
    for (i = 0; i < result1->size; i++){
        root = param_create();
        tail = root;
        id = atoi(result1->data[i][0]);
        username = result1->data[i][1];
        avatar = result1->data[i][2];
        type = atoi(result1->data[i][3]);
        seen = atoi(result1->data[i][4]);
        link_id = atoi(result1->data[i][5]);
        if (type == 0){
            // thong bao ve bai post
            result2 = DB_get_by_id(&conn, "post", link_id);
            title = DB_str_get_by(result2, "title");
            sprintf(tieude, "Vừa đăng một bài viết:");
            sprintf(noidung, "%s", title);
            DB_free_data(&result2);
        }else if(type == 1){ // chua hoan thanh chuc nang comment
            // thong bao ve comment
//            sprintf(sql, "select comment.content as content, title from post, comment where post.id = comment.post_id and comment.id = %d", link_id);
//            result2 = DB_get_by_id(&conn2, "comment", link_id);
//            title = DB_str_get_by(result2, "title");
//            content = DB_str_get_by(result2, "content");
//            sprintf(noidung, "đã bình luận bài viết: %s...\n%s...", title, content);
//            DB_free_data(&result2);
        }
        else if (type == 2){
            // thong bao co nguoi khac follow
            sprintf(tieude, "Vừa follow bạn.");
            sprintf(noidung, "");
        }
        else if(type == 3){
            // thong bao chap nhan loi moi ket ban
            sprintf(tieude, "Đã chấp nhận lời mời kết bạn.");
            sprintf(noidung, "");
        }
        param_add_int(&tail, id);
        param_add_str(&tail, username);
        param_add_str(&tail, avatar);
        param_add_str(&tail, tieude);
        param_add_str(&tail, noidung);
        param_add_int(&tail, seen);
        response = data_create(root, NOTIFY);
        if (send_data(client, response, 0, 0) == -1){
            logger(L_ERROR, "%s - function: notify() - 176");
            return -1;
        }
        usleep(100);
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
    mysql_close(conn);
    return 1;
}