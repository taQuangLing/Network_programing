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


int id = -1;
int client_sock;


int notify();

int open_book();

int exit_server();

int sign_up();

int forgot_password();

int search();

int news();

int friends();

int following();

int follower();

int unfollow();

int accept_friend();

void get_list_user(Param p);

int interaction(MessageCode type);

int follow();

int profile();

int edit_profile();


void get_news(Param p);

int posts();

int edit_posts();

int remove_posts();

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
        printf("\n5. Search");
        printf("\n6. News");
        printf("\n7. Friends");
        printf("\n8. Following");
        printf("\n9. Follower");
        printf("\n10. Unfollow");
        printf("\n11. Accept Friend");
        printf("\n12. Follow");
        printf("\n13. Profile");
        printf("\n14. Edit Profile");
        printf("\n15. Đăng posts");
        printf("\n16. Edit Posts");
        printf("\n17. Gỡ Posts");
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
            case 5:
                status = search();
                break;
            case 6:
                status = news();
                break;
            case 7:
                status = friends();
                break;
            case 8:
                status = following();
                break;
            case 9:
                status = follower();
                break;
            case 10:
                status = unfollow();
                break;
            case 11:
                status = accept_friend();
                break;
            case 12:
                status = follow();
                break;
            case 13:
                status = profile();
                break;
            case 14:
                status = edit_profile();
                break;
            case 15:
                status = posts();
                break;
            case 16:
                status = edit_posts();
                break;
            case 17:
                status = remove_posts();
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

int remove_posts() {
    int postid;
    char enter;
    printf("\n========REMOVE_POSTS=========");
    printf("\nNhap post_id : ");
    scanf("%d", &postid);
    scanf("%c", &enter);

    Param root = param_create(), tail = root;
    param_add_int(&tail, id);
    param_add_int(&tail, postid);
    Data request = data_create(root, REMOVE_POSTS);
    send_data(client_sock, request, 0, 0);

    Data response = recv_data(client_sock, 0, 0);
    MessageCode code = response->message;
    data_free(&response);
    if (code == SUCCESS){
        return display_success();
    }else if(code == FAIL){
        logger(L_WARN, "Khong co quyen truy cap");
        return 0;
    }else{
        return display_error();
    }
}

int edit_posts() {
    char title[50] = {0}, enter, content[150] = {0}, image[80] = {0};
    int status, postid;
    printf("\n========EDIT_POSTS=========");
    printf("\nNhap post_id : ");
    scanf("%d", &postid);
    scanf("%c", &enter);
    printf("\nNhap title: ");
    scanf("%[^\n]s", title);
    scanf("%c", &enter);
    printf("\nNhap content: ");
    scanf("%[^\n]s", content);
    scanf("%c", &enter);
    printf("\nNhap image: ");
    scanf("%[^\n]s", image);
    scanf("%c", &enter);
    printf("\nNhap status(0: private, 1: friend, 2:public): ");
    scanf("%d", &status);
    scanf("%c", &enter);

    Param root = param_create(), tail = root;
    Data request, response;

    param_add_int(&tail, id);
    param_add_int(&tail, postid);
    param_add_str(&tail, title);
    param_add_str(&tail, content);
    param_add_str(&tail, image);
    param_add_int(&tail, status);
    request = data_create(root, EDIT_POSTS);
    send_data(client_sock, request, 0, 0);

    response = recv_data(client_sock, 0, 0);
    MessageCode code = response->message;
    data_free(&response);
    if (code == FAIL){
        logger(L_WARN, "Khong co quyen truy cap");
        return 0;
    }

    usleep(1000);
    char filename[50] = {0};
    char path[100] = {0};
    printf("\nNhap ten file > ");
    scanf("%[^\n]s", filename);
    scanf("%c", &enter);
    if (check_space(filename) == 0){
        request = data_create(NULL, EMPTY);
        send_data(client_sock, request, 0, 0);
        response = recv_data(client_sock, 0, 0);
        MessageCode code = response->message;
        if (code != SUCCESS){
            return display_error();
        }
        return display_success();
    }
    sprintf(path, "resource/%s", filename);
    if (send_file(client_sock, path) == -1){
        request = data_create(NULL, FAIL_OPEN_FILE);
        send_data(client_sock, request, 0, 0);
        return display_error();
    }

    response = recv_data(client_sock, 0, 0);
    code = response->message;
    if (code != SUCCESS){
        return display_error();
    }
    return display_success();
}

int posts() {
    char title[50] = {0}, enter, content[150] = {0}, image[80] = {0};
    int status;
    printf("\n========POSTS=========");
    printf("\nNhap title: ");
    scanf("%[^\n]s", title);
    scanf("%c", &enter);
    printf("\nNhap content: ");
    scanf("%[^\n]s", content);
    scanf("%c", &enter);
    printf("\nNhap image: ");
    scanf("%[^\n]s", image);
    scanf("%c", &enter);
    printf("\nNhap status(0: private, 1: friend, 2:public): ");
    scanf("%d", &status);
    scanf("%c", &enter);

    Param root = param_create(), tail = root;
    Data request, response;

    param_add_int(&tail, id);
    param_add_str(&tail, title);
    param_add_str(&tail, content);
    param_add_str(&tail, image);
    param_add_int(&tail, status);
    request = data_create(root, POSTS);
    send_data(client_sock, request, 0, 0);

    usleep(1000);
    char filename[50] = {0};
    char path[100] = {0};
    printf("\nNhap ten file > ");
    scanf("%[^\n]s", filename);
    scanf("%c", &enter);
    sprintf(path, "resource/%s", filename);
    if (send_file(client_sock, path) == -1){
        request = data_create(NULL, FAIL_OPEN_FILE);
        send_data(client_sock, request, 0, 0);
        return display_error();
    }

    response = recv_data(client_sock, 0, 0);
    MessageCode code = response->message;
    if (code != SUCCESS){
        return display_error();
    }
    return display_success();
}

int edit_profile(){
    char username[USERNAME_LEN] = {0}, image[100] = {0}, bio[100] = {0}, birthday[20] = {0}, interest[150] = {0};
    char enter;
    int gender = -1;
    printf("\n===========PROFILE==========");
    printf("\nNhap ten: ");
    scanf("%[^\n]s", username);
    if (check_space(username) == 0){
        logger(L_WARN, "\nKhong dc bo trong username!!!");
        return 0;
    }
    scanf("%c", &enter);
    printf("\nNhap image: ");
    scanf("%[^\n]s", image);
    scanf("%c", &enter);
    printf("\nNhap bio: ");
    scanf("%[^\n]s", bio);
    scanf("%c", &enter);
    printf("\nNhap gender(\"Nam\": 1, \"Nu\": 0): ");
    scanf("%d", &gender);
    scanf("%c", &enter);
    if (gender < 0 || gender > 1)gender = 0;
    printf("\nNhap birthday(yean/month/day): ");
    scanf("%[^\n]s", birthday);
    scanf("%c", &enter);
    printf("\nNhap so thich: ");
    scanf("%[^\n]s", interest);
    scanf("%c", &enter);
printf("\ngender = %d", gender);
    Param root = param_create(), tail = root;
    param_add_int(&tail, id);
    param_add_str(&tail, username);
    param_add_str(&tail, image);
    param_add_str(&tail, bio);
    param_add_int(&tail, gender);
    param_add_str(&tail, birthday);
    param_add_str(&tail, interest);
    Data request = data_create(root, EDIT_PROFILE);
    send_data(client_sock, request, 0, 0);

    Data response = recv_data(client_sock, 0 , 0);
    MessageCode code = response->message;
    if (code == SUCCESS){
        return display_success();
    }else
        return display_error();
}

int profile() {
    printf("Nhap user_id > ");
    int user_id;
    scanf("%d", &user_id);
    char enter;
    scanf("%c", &enter);

    Param root = param_create(), tail = root;
    param_add_int(&tail, id);
    param_add_int(&tail, user_id);
    Data request = data_create(root, PROFILE);
    send_data(client_sock, request, 0, 0);

    char *name, *image, *bio, *birthday, *created_at, *interest, gender_str[5] = {0};
    int gender;
    Param p;
    Data response = recv_data(client_sock, 0, 0);
    MessageCode code = response->message;
    data_free(&response);
    if (code == OK){
        response = recv_data(client_sock, 0, 0);
        p = response->params;
        user_id = param_get_int(&p);
        name = param_get_str(&p);
        image = param_get_str(&p);
        bio = param_get_str(&p);
        gender = param_get_int(&p);
        birthday = param_get_str(&p);
        created_at = param_get_str(&p);
        interest = param_get_str(&p);

        if (gender == 0)strcpy(gender_str, "Nữ");
        else
        {
            strcpy(gender_str, "Nam");
        }
        printf("\n------PROFILE------");
        printf("\n\tuserid: %d, name: %s, gender: %s, bio: %s, birthday: %s, interest: %s\n\timage: %s",
               user_id, name, gender_str, bio, birthday, interest, image);
        data_free(&response);
    }else{
        return display_error();
    }
    response = recv_data(client_sock, 0, 0);
    code = response->message;
    if (code == OK){
        get_news(response->params);
    }else return display_error();
    data_free(&response);

    return 1;
}

int follow() {
    int userid;
    char enter;
    printf("Nhap userid > ");
    scanf("%d", &userid);
    scanf("%c", &enter);

    Param root = param_create(), tail = root;
    param_add_int(&tail, id);
    param_add_int(&tail, userid);
    Data request = data_create(root, FOLLOW);
    send_data(client_sock, request, 0, 0);

    Data response = recv_data(client_sock, 0, 0);
    MessageCode code = response->message;
    data_free(&response);
    if (code == SUCCESS){
        logger(L_SUCCESS, "Theo dõi bạn bè thành công");
        return 1;
    }else if(code == FOLLOWED){
        logger(L_WARN, "Đã theo dõi người này");
        return 1;
    }
    else if (code == ACCEPTED){
        logger(L_WARN, "Đã trở thành bạn bè");
    }else{
        return display_error();
    }
}

int accept_friend() {
    int userid;
    char enter;
    printf("Nhap userid > ");
    scanf("%d", &userid);
    scanf("%c", &enter);

    Param root = param_create(), tail = root;
    param_add_int(&tail, id);
    param_add_int(&tail, userid);
    Data request = data_create(root, ACCEPT);
    send_data(client_sock, request, 0, 0);

    Data response = recv_data(client_sock, 0, 0);
    MessageCode code = response->message;
    data_free(&response);
    if (code == SUCCESS){
        logger(L_SUCCESS, "Đã chấp nhận lời mời kết bạn");
        return 1;
    }else if(code == FAIL){
        logger(L_WARN, "Người này chưa theo dõi bạn");
        return 0;
    }
    else if (code == ACCEPTED){
        logger(L_WARN, "Đã trờ thành bạn bè");
        return 0;
    }
    else{
        return display_error();
    }
}

int unfollow() {
    int userid;
    char enter;
    printf("Nhap userid > ");
    scanf("%d", &userid);
    scanf("%c", &enter);

    Param root = param_create(), tail = root;
    param_add_int(&tail, id);
    param_add_int(&tail, userid);
    Data request = data_create(root, UNFOLLOW);
    send_data(client_sock, request, 0, 0);

    Data response = recv_data(client_sock, 0, 0);
    MessageCode code = response->message;
    data_free(&response);
    if (code == SUCCESS){
        logger(L_SUCCESS, "Bỏ theo dõi thành công");
        return 1;
    }else if (code == FAIL){
        logger(L_WARN, "Bạn chưa theo dõi người này");
        return 1;
    }
    else{
        return display_error();
    }
}

int follower() {
    return interaction(FOLLOWER);
}

int interaction(MessageCode type) {
    Param root = param_create();
    param_add_int(&root, id);
    Data request = data_create(root, type);
    send_data(client_sock, request, 0, 0);

    Data response = recv_data(client_sock, 0, 0);
    if (response->message == OK){
        get_list_user(response->params);
    }else if (response->message == ERROR){
        data_free(&response);
        return display_error();
    }
    data_free(&response);
    return 1;
}

int following() {
    return interaction(FOLLOWING);
}

int friends() {
    return interaction(FRIENDS);
}

void get_list_user(Param p) {
    int count = param_get_int(&p);
    char *name, *avatar;
    int user_id, status;
    Data response;
    for (int i = 0; i < count; i++){
        response = recv_data(client_sock, 0, 0);
        p = response->params;
        user_id = param_get_int(&p);
        name = param_get_str(&p);
        avatar = param_get_str(&p);
        status = param_get_int(&p);
        printf("\nuser_id: %d, name = %s, avatar = %s, status = %d.", user_id, name, avatar, status);
        data_free(&response);
    }
}

int news() {
    printf("\n\tDAY LA TRANG BANG TIN: ");

    Param root = param_create();
    param_add_int(&root, id);
    Data request = data_create(root, NEWS);
    send_data(client_sock, request, 0, 0);

    Data response = recv_data(client_sock, 0, 0);
    if (response->message != OK){
        return display_error();
    }

    get_news(response->params);
    data_free(&response);
    return 1;
}

void get_news(Param p) {
    int postid, userid;
    char *name, *avatar, *title, *content, *image;
    Data response;
    int count = param_get_int(&p);
    for (int i = 0; i < count; i++){
        response = recv_data(client_sock, 0, 0);
        p = response->params;
        // Xu li tung ban ghi...
        postid = param_get_int(&p);
        userid = param_get_int(&p);
        name = param_get_str(&p);
        avatar = param_get_str(&p);
        title = param_get_str(&p);
        content = param_get_str(&p);
        image = param_get_str(&p);
        printf("\n-- postid: %d, userid: %d, name: %s, title: %s, content: %s\navatar: %s\nimage: %s", postid, userid, name, title, content, avatar, image);
        data_free(&response);
    }
}

int search() {
    char keyword[50], enter;
    int type;
    printf("\nNhap tu khoa muon search > ");
    scanf("%[^\n]s", keyword);
    scanf("%c", &enter);
    printf("\n0. Tim kiem sach");
    printf("\n1. Tim kiem ban be.");
    printf("\nYour choice > ");
    scanf("%d", &type);
    scanf("%c", &enter);

    Param root, tail;
    Data request, response;
    // Gui keyword
    root = param_create();
    tail = root;
    param_add_int(&tail, id);
    param_add_str(&tail, keyword);
    param_add_int(&tail, type);
    request = data_create(root, SEARCH);
    send_data(client_sock, request, 0, 0);
    // Nhan danh sach ket qua
    int postid, userid;
    char *username, *image, *title, *content;
    Param p;

    response = recv_data(client_sock, 0, 0);
    int n = param_get_int(&response->params);
    data_free(&response);
    if (type == 0){
        for (int i = 0; i < n; i++){
            response = recv_data(client_sock, 0, 0);
            p = response->params;
            postid = param_get_int(&p);
            userid = param_get_int(&p);
            username = param_get_str(&p);
            image = param_get_str(&p);
            title = param_get_str(&p);
            content = param_get_str(&p);
            printf("\npostid: %d, userid: %d, username: %s, image: %s, title: %s, content: %s", postid, userid, username, image, title, content);
            data_free(&response);
        }
    }else{
        for (int i = 0; i < n; i++){
            response = recv_data(client_sock, 0, 0);
            p = response->params;
            userid = param_get_int(&p);
            username = param_get_str(&p);
            image = param_get_str(&p);
            printf("\nuserid: %d, username: %s, avatar: %s", userid, username, image);
            data_free(&response);
        }
    }
    return 1;
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
    }else if (response->message == ERROR){
        logger(L_ERROR, "%s", "HE THONG DANG NANG CAP");
    }else{
        char path[150] = {0};
        sprintf(path, "resource");
        write_file(client_sock, path);
    }

    data_free(&response);
    return 1;
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
