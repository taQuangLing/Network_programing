//
// Created by ling on 02/12/2022.
//
#include "AppUtils.h"
#include <time.h>
#include <stdio.h>
#include <malloc.h>
#include <stdlib.h>
#include <ctype.h>
#include <stdarg.h>
#include <sys/socket.h>
#include <unistd.h>
#include "string.h"
#include "config.h"

char *convert_timestamp(char *timestamp){
    time_t t = timestamp;
    const char format[] = "%H:%M:%S %Y:%m:%d";

    struct tm lt;
    char res[32];

    (void) localtime_r(&t, &lt);

    if (strftime(res, sizeof(res), format, &lt) == 0) {
        (void) fprintf(stderr,  "strftime(3): cannot format supplied "
                                "date/time into buffer of size %lu "
                                "using: '%s'\n",
                       sizeof(res), format);
        return NULL;
    }

    return res;
}
Data str_to_data(char *str, int key){
    // mess_code*payload_size*param*...
    Param root = param_create();
    char decode_str[255];
    Param tail = root;
    char param[255];
    Data data;
    int flag = 0, i, code, size, size_m = 0;
    char message_code[10], sizes[10];
    for (i = 0;; i++){
        refresh(param, 255);
        if (str[i] == '*'){
            if (flag == 0){
                strncpy(message_code, str, i);
                code = atoi(message_code);
            }else{
                refresh(decode_str, 255);
                strncpy(param, str+flag, i-flag);
                size_m += (int)strlen(param);
                ceaser_decode(param, decode_str, key);
                param_add_str(&tail, decode_str);
            }
            flag = i+1;
        }else
        if (str[i] == '\0'){
            strncpy(sizes, str+flag, i - flag);
            size = atoi(sizes);
            break;
        }
    }
    if (size != size_m){
        logger(L_WARN, "%s", "Missing data");
    }
    data = data_create(root, code);

    return data;
}

int checkChar(char ch){

    if (   (ch < '0')
           || (ch > '9' && ch < 'A')
           || (ch > 'Z' && ch < 'a')
           || ch > 'z')return 0;
    return 1;
}
char *data_to_str(Data data, int key){
    //code-param1-param2-...-param3-size
    char str_res[255];
    char i2s[10];
    int payload_size = 0, request_size;
    int count = 1;
    for (Param param = data->params; param != NULL; param = param->next){
        payload_size += (int)strlen(param->value);
        count++;
    }

    intTostr(data->message, i2s);
    request_size = payload_size + count + (int)strlen(i2s) + 2;
    intTostr(payload_size, i2s);
    request_size += strlen(i2s);
    char *request_str = (char*)malloc(request_size+1); // 10 = size(count), 2 = size(key)
    intTostr(data->message, i2s);
    int index = append(request_str, 0, i2s); // add message code
    index = append(request_str, index, "*"); // add character

    for (Param p = data->params;p != NULL; p = p->next){
        refresh(str_res, 255);
        ceaser_encode(p->value, str_res, key);
        index = append(request_str, index, str_res);
        index = append(request_str, index, "*");
    }
    intTostr(payload_size, i2s);
    index = append(request_str, index, i2s); // add payload length
    request_str[request_size-1] = '\0';
    return request_str;
}

int append(char *string, int index, char *data){
    return index + sprintf(string + index, "%s", data);
}
void intTostr(int num, char * str){
    refresh(str, 10);
    sprintf(str, "%d", num);
}
void refresh(char *str, int num){
    memset(str, 0, num);
}
int send_data(int sock, Data data, int flag, int key){
    log_data(data, "Response");
    char buff[BUFF_SIZE];
    refresh(buff, BUFF_SIZE);
    strcpy(buff, data_to_str(data, key));
//    print_mess(buff, "");
    data_free(&data);
    int bytes_sent = send(sock, buff, strlen(buff), flag);
    bzero(buff, BUFF_SIZE);
    if(bytes_sent == -1){
        logger(L_ERROR, "%s", "Error sending data");
        return -1;
    }else if(bytes_sent < strlen(buff)){
        logger(L_WARN, "%s", "Missing data");
        return -1;
    }
    return 1;
}
Data recv_data(int sock, int flag, int key){
    char buff[BUFF_SIZE];
    int byte_recv = recv(sock, buff, BUFF_SIZE,0);
    buff[byte_recv] = '\0';
    if(byte_recv == -1){
        logger(L_ERROR, "%s", "Error recv data");
        return NULL;
    }
    Data data = str_to_data(buff, key);
    bzero(buff, BUFF_SIZE);
    log_data(data, "Request");
    return data;
}
int ping(int sock, int flag){
    Data data = data_create(NULL,PING);
    return send_data(sock, data, flag, 0);
}
int send_error(int client){
    Data response = data_create(NULL, ERROR);
    send_data(client, response, 0, 0);
    return 1;
}
void get_time_now(char *res, const char *format){
    time_t rawtime;
    struct tm * timeinfo;
    time ( &rawtime );
    timeinfo = localtime ( &rawtime );

    if (format == NULL){
        strftime(res, 32, "%Y-%m-%d %H:%M:%S", timeinfo);
    }
    else
        strftime(res, 32, format, timeinfo);

}
void ceaser_encode(char srcStr[], char encodeStr[], int key)
{
    int i;
    unsigned char ch;
    for(i = 0; srcStr[i] != '\0'; ++i){
        ch = srcStr[i];
        if(ch >= 'a' && ch <= 'z'){
            ch = ch + key;
            if(ch > 'z'){
                ch = ch - 'z' + 'a' - 1;
            }
        }
        else if(ch >= 'A' && ch <= 'Z'){
            ch = ch + key;
            if(ch > 'Z'){
                ch = ch - 'Z' + 'A' - 1;
            }
        }
        encodeStr[i] = ch;
    }
}
void ceaser_decode(char srcStr[], char decodeStr[], int key)
{
    int i;
    unsigned char ch;
    for(i = 0; srcStr[i] != '\0'; ++i){
        ch = srcStr[i];
        if(ch >= 'a' && ch <= 'z'){
            ch = ch - key;
            if(ch < 'a'){
                ch = ch + 'z' - 'a' + 1;
            }
        }
        else if(ch >= 'A' && ch <= 'Z'){
            ch = ch - key;
            if(ch < 'A'){
                ch = ch + 'Z' - 'A' + 1;
            }
        }
        decodeStr[i] = ch;
    }
}
int check_space(char *str) {

    if( str == NULL ) { return -1; }
    if( str[0] == '\0' ) { return 0; }

    size_t len = 0;
    char *frontp = str;
    char *endp = NULL;

    len = strlen(str);
    endp = str + len;

    /*
     * Move the front and back pointers to address the first non-whitespace
     * characters from each end.
     */
    while(isspace((unsigned char) *frontp)) { ++frontp; }
    if( endp != frontp ) {
        while( isspace((unsigned char) *(--endp)) && endp != frontp ) {}
    }

    if( frontp != str && endp == frontp )
        *str = '\0';
    else if( str + len - 1 != endp )
        *(endp + 1) = '\0';

    /*
     * Shift the string so that it starts at str so that if it's dynamically
     * allocated, we can still free it on the returned pointer.  Note the reuse
     * of endp to mean the front of the string buffer now.
     */
    endp = str;
    if( frontp != str ) {
        while( *frontp ) { *endp++ = *frontp++; }
        *endp = '\0';
    }
    return strcmp(str, "");
}
void encode_password(char *pass){
    // coding
}
int random_between(int from, int to){
    return rand() % to + from;
}
void logger(char *type, const char *format, ...) {
    va_list args;

    if(strcmp(type, L_ERROR) == 0)   printf("\x1b[1;38;5;196m[x]  ");
    if(strcmp(type, L_SUCCESS) == 0) printf("\x1b[1;38;5;47m[\xE2\x9C\x93]  ");
    if(strcmp(type, L_WARN) == 0)    printf("\x1b[1;38;5;226m[!]  ");
    if(strcmp(type, L_INFO) == 0)    printf("\x1b[1;38;5;014m[i]  ");

    va_start(args, format);
    vprintf(format, args);
    va_end(args);
    printf("\x1b[0m\n");
    fflush(stdout);
}
int display_error() {
    logger(L_ERROR, "HE THONG DANG NANG CAP");
    return -1;
}
int display_success(){
    logger(L_SUCCESS, "Thanh cong");
    return 1;
}
int send_success(int client){
    Data response = data_create(NULL, SUCCESS);
    send_data(client, response, 0, 0);
    return 1;
}
int send_fail(int client){
    Data response = data_create(NULL, FAIL);
    send_data(client, response, 0, 0);
    return 1;
}
int send_file(int client, char *path) {
    Data response;
    FILE *f;
    int i, n;
    if ((f = fopen(path, "rb")) == NULL){
        response = data_create(NULL, FAIL_OPEN_FILE);
        send_data(client, response, 0, 0);
        logger(L_ERROR, "Lỗi mở file %s", path);
        return -1;
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
    return 1;
}
int write_file(int sock, char *path) {
    int n;
    FILE *fp;
    char *filename;
    char buffer[BUFF_SIZE];
//  get file name
    Data response = recv_data(sock, 0, 0);
    if (response->message == EMPTY){
        data_free(&response);
        return 0;
    }
    filename = param_get_str(&response->params);
    sprintf(path + strlen(path), "%s", filename);
    data_free(&response);
//  get data
    fp = fopen(path, "wb");
    while (1) {
        n = recv(sock, buffer, BUFF_SIZE, 0);
        if (fwrite(buffer, 1, n, fp) < n){
            logger(L_ERROR, "function: write_file()");
            fclose(fp);
            return -1;
        }
        bzero(buffer, BUFF_SIZE);
        printf("\nn = %d", n);
        if (n < BUFF_SIZE)break;
    }
    fclose(fp);
    return 1;
    //    char command[100] = {0};
//    sprintf(command, "gopen \"%s\"", filename);
//    system(command);
}