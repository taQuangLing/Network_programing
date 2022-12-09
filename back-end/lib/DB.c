//
// Created by ling on 09/12/2022.
//
#include <stdio.h>
#include <string.h>
#include "DB.h"
#include "mysql/mysql.h"
#include "message.h"
#include "AppUtils.h"

int add(MYSQL **conn, char *table, char **key, char **value, int num_field){
    char sql[1000];
    int index;
    memset(sql, 0, 1000);
    index = append(sql, 0, "insert into ");
    index = append(sql, index, table);
    index = append(sql, index, " (");
    for (int j = 0; j < num_field; j++){
        index = append(sql, index, key[j]);
        if (j < num_field - 1)index = append(sql, index, ", ");
    }
    index = append(sql, index, ") values (");
    for (int j = 0; j < num_field; j++){
        index = append(sql, index, "\'");
        index = append(sql, index, value[j]);
        index = append(sql, index, "\'");
        if (j < num_field - 1)index = append(sql, index, ",");
    }
    sprintf(sql + index, ");");
    printf("sql = \"%s\"", sql);
    if (mysql_query(*conn, sql)){
        fprintf(stderr, "%s\n", mysql_error(*conn));
        return -1;
    }
}

int main(){
    char *server = "localhost";
    char *user = "root";
    char *password = "gau.4605"; /* set me first */
    char *database = "network_programing";

    MYSQL *conn = mysql_init(NULL);

    if (!mysql_real_connect(conn, server, user, password,
                            database, 0, NULL, 0)) {
        fprintf(stderr, "%s\n", mysql_error(conn));
        exit(1);
    }

    char table[] =  "comment";
    char *key[] = {"post_id", "user_id", "created_at", "content"};
    char *value[] = {"2", "2", "2022/9/12", "Ta Quang Linh"};
    if (add(&conn, table, key, value, 4) == -1){
        mysql_close(conn);
        exit(0);
    }
    mysql_close(conn);
}
