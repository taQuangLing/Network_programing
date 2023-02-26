//
// Created by ling on 04/01/2023.
//

#ifndef LAP_TRINH_MANG_HTTP_H
#define LAP_TRINH_MANG_HTTP_H

#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include <stdbool.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <netdb.h>

bool compare_sockaddr(const struct sockaddr *, const struct sockaddr *);
char *get_socketaddr(const struct sockaddr *);
void requestify(char *, char *);
int server_init_connect(char *);
int client_init_connect(char *, char *);
int accept_connection(int);
void http_clear(char *, char *, char *);

#endif //LAP_TRINH_MANG_HTTP_H
