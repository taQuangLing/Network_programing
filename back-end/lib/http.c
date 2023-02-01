//
// Created by ling on 04/01/2023.
//
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <stdbool.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <netdb.h>
#include <time.h>

#include "http.h"
#include "error.h"
#include "config.h"
#include "MessageCode.h"

#define NUM_OF_CODE 10

static const int MAX_PENDING = 5; // Maximum outstanding connection requests

void http_clear(char *method, char *request, char *response) {
    memset(method, 0, 10);
    memset(request, 0, MAX_REQUEST_LENGTH);
    memset(response, 0, MAX_RESPONSE_LENGTH);
}

void print_socket_addr(const struct sockaddr *address, FILE *stream) {
    // Test for address and stream
    if (address == NULL || stream == NULL)
        return;
    void *numericAddress; // Pointer to binary address
    // Buffer to contain result (IPv6 sufficient to hold IPv4)
    char addrBuffer[INET6_ADDRSTRLEN];
    in_port_t port; // Port to print
    // Set pointer to address based on address family
    switch (address->sa_family) {
        case AF_INET:
            numericAddress = &((struct sockaddr_in *) address)->sin_addr;
            port = ntohs(((struct sockaddr_in *) address)->sin_port);
            break;
        case AF_INET6:
            numericAddress = &((struct sockaddr_in6 *) address)->sin6_addr;
            port = ntohs(((struct sockaddr_in6 *) address)->sin6_port);
            break;
        default:
            fputs("[unknown type]", stream);
            // Unhandled type
            return;
    }
    // Convert binary to printable address
    if (inet_ntop(address->sa_family, numericAddress, addrBuffer, sizeof(addrBuffer)) == NULL)
        fputs("[invalid address]", stream); // Unable to convert
    else {
        fprintf(stream, "%s", addrBuffer);
        if (port != 0)
            // Zero not valid in any socket addr
            fprintf(stream, "-%u", port);
    }
}

bool compare_sockaddr(const struct sockaddr *addr1, const struct sockaddr *addr2) {
    if (addr1 == NULL || addr2 == NULL)
        return addr1 == addr2;
    else if (addr1->sa_family != addr2->sa_family)
        return false;
    else if (addr1->sa_family == AF_INET) {
        struct sockaddr_in *ipv4Addr1 = (struct sockaddr_in *) addr1;
        struct sockaddr_in *ipv4Addr2 = (struct sockaddr_in *) addr2;
        return ipv4Addr1->sin_addr.s_addr == ipv4Addr2->sin_addr.s_addr && ipv4Addr1->sin_port == ipv4Addr2->sin_port;
    }
    else if (addr1->sa_family == AF_INET6) {
        struct sockaddr_in6 *ipv6Addr1 = (struct sockaddr_in6 *) addr1;
        struct sockaddr_in6 *ipv6Addr2 = (struct sockaddr_in6 *) addr2;
        return memcmp(&ipv6Addr1->sin6_addr, &ipv6Addr2->sin6_addr, sizeof(struct in6_addr)) == 0 && ipv6Addr1->sin6_port == ipv6Addr2->sin6_port;
    }
    else return false;
}

char *get_socketaddr(const struct sockaddr *address) {
    // Test for address and stream
    if (address == NULL)
        return NULL;

    void *numericAddress; // Pointer to binary address
    // Buffer to contain result (IPv6 sufficient to hold IPv4)
    char addrBuffer[INET6_ADDRSTRLEN];
    in_port_t port; // Port to print
    // Set pointer to address based on address family
    switch (address->sa_family) {
        case AF_INET:
            numericAddress = &((struct sockaddr_in *) address)->sin_addr;
            port = ntohs(((struct sockaddr_in *) address)->sin_port);
            break;
        case AF_INET6:
            numericAddress = &((struct sockaddr_in6 *) address)->sin6_addr;
            port = ntohs(((struct sockaddr_in6 *) address)->sin6_port);
            break;
        default:
            return "unknown";
    }

    // Convert binary to printable address
    if (inet_ntop(address->sa_family, numericAddress, addrBuffer, sizeof(addrBuffer)) == NULL)
        return "unknown";
    else {
        char *addr = (char*)calloc(BUFFER, sizeof(char));
        sprintf(addr, "%s:%u", addrBuffer, port);
        return addr;
    }
}

void requestify(char *method, char *request) {
    /* TEMPLATE: METHOD REQUEST */
    char request_tpl[MAX_REQUEST_LENGTH];
    sprintf(request_tpl, "%s %s", method, request);
    strcpy(request, request_tpl);
}

void parse_request(char *method, char *request) {
    sscanf(request, "%s %[^\n]s", method, request);
}

int server_init_connect(char *service) {
    // Config the server address structure
    struct addrinfo addrConfig;
    memset(&addrConfig, 0, sizeof(addrConfig)); // Zero out structure
    addrConfig.ai_family = AF_INET; // IPc4 address family
    addrConfig.ai_flags = AI_PASSIVE; // Accept on any address/port
    addrConfig.ai_socktype = SOCK_STREAM; // Only stream socket
    addrConfig.ai_protocol = IPPROTO_TCP; // Only TCP socket

    struct addrinfo *server;
    if (getaddrinfo(NULL, service, &addrConfig, &server) != 0) {
        send_message(ERR_SERVER_NOT_FOUND, "");
        exit(FAIL);
    }

    int server_fd = -1;
    for(struct addrinfo *addr = server; addr != NULL; addr = addr->ai_next) {
        // Create a TCP socket
        server_fd = socket(addr->ai_family, addr->ai_socktype, addr->ai_protocol);

        // Socket creation failed -> try next address
        if (server_fd < 0) continue;

        // Bind to the local address and listen incoming request
        if ((bind(server_fd, addr->ai_addr, addr->ai_addrlen) == 0) && (listen(server_fd, MAX_PENDING) == 0)) {
            struct sockaddr_storage localAddr;
            socklen_t addrSize = sizeof(localAddr);
            if(getsockname(server_fd, (struct sockaddr *) &localAddr, &addrSize) < 0) {
                printf("getsockname() failed\n");
                exit(FAIL);
            }
            fputs("Binding to ", stdout);
            print_socket_addr((struct sockaddr *) &localAddr, stdout);
            fputc('\n', stdout);

            // Bind and listen successfully
            break;
        }

        close(server_fd);
        server_fd = -1;
    }

    freeaddrinfo(server);
    return server_fd;
}

int client_init_connect(char *server, char *port) {
    // Tell the system what kind(s) of address info we want - Config address
    struct addrinfo addrConfig; // Criteria for address match
    memset(&addrConfig, 0, sizeof(addrConfig)); // Zero out structure
    addrConfig.ai_family = AF_INET; // IPv4 address family
    addrConfig.ai_socktype = SOCK_STREAM; // Only stream sockets
    addrConfig.ai_protocol = IPPROTO_TCP; // Only TCP protocol

    // Get server information via server
    struct addrinfo *servAddr;
    if (getaddrinfo(server, port, &addrConfig, &servAddr) != 0) {
        send_message(ERR_SERVER_NOT_FOUND, "");
        exit(FAIL);
    }

    int sock = -1;
    // Create a stream TCP socket
    for(struct addrinfo *addr = servAddr; addr != NULL; addr = addr->ai_next) {
        // Create a reliable, stram socket using TCP
        sock = socket(addr->ai_family, addr->ai_socktype, addr->ai_protocol); // Socket descriptor for client
        if (sock < 0) continue;

        // Establish the connection to the server
        if (connect(sock, addr->ai_addr, addr->ai_addrlen) == 0)
            break;

        close(sock);
        sock = -1;
    }

    freeaddrinfo(servAddr);
    return sock;
}

int accept_connection(int sock) {
    // Client address
    struct sockaddr_storage client;

    // Set Length of client address structure (in-out parameter)
    socklen_t clientAddrLen = sizeof(client);

    // Wait for a client to connect
    int client_fd = accept(sock, (struct sockaddr *) &client, &clientAddrLen);
    if (client_fd < 0) {
        send_message(CONECTION_FAIL, "");
        exit(FAIL);
    }

    fputs("Handling client ", stdout);
    print_socket_addr((struct sockaddr *) &client, stdout);
    fputc('\n', stdout);
    return client_fd;
}
