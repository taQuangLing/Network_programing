//
// Created by ling on 09/12/2022.
//
#include <stdio.h>
#include <string.h>
#include "DB.h"
#include "mysql/mysql.h"
#include "AppUtils.h"
#include "config.h"


void DB_free_data(Table *data){
    for (int i = 0; i < (*data)->size; i++){
        free((*data)->data[i]);
    }
    free((*data)->header);
    free((*data)->data);
    free(*data);
}

int DB_insert(MYSQL **conn, char *table, char **key, char **value, int num_field){
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
        logger(L_ERROR, "%s; %s", (char*)mysql_error(*conn),"function: DB_insert - 41");
        return -1;
    }
    logger(L_SUCCESS, "%s", "function: DB_insert");
    return 1;
}

Table DB_get(MYSQL **conn, char *sql){
    MYSQL_FIELD *field;
    MYSQL_RES *res;
    MYSQL_ROW row;

    Table data = (Table) malloc(sizeof (struct data_t));

    if (mysql_query(*conn, sql)) {
        logger(L_ERROR, "%s; %s", (char*)mysql_error(*conn),"function: DB_get - 54");
        exit(1);
    }
    res = mysql_store_result(*conn);
    long num_row = (long)mysql_num_rows(res);
    int num_field = (int)mysql_num_fields(res);
    int i = 0, j = 0;
    data->header = (char**)malloc(sizeof (char*) * num_field);
    for (j = 0; field = mysql_fetch_field(res); j++){
        data->header[j] = field->name;
    }

    data->data = (char***)malloc(sizeof (char**) * num_row);
    while ((row = mysql_fetch_row(res))) {
        data->data[i] = (char**)malloc(sizeof (char*) * num_field);
        for (j = 0; j < num_field; j++){
            data->data[i][j] = row[j];
        }
        i++;
    }
    data->size = num_row;
    data->column = num_field;
    mysql_free_result(res);
    logger(L_SUCCESS, "%s", "function: DB_get");
    return data;
}

Table DB_get_by_id(MYSQL **conn, char *table, char *id){
    char sql[100] = "select * from ";
    int index = (int)strlen(sql);
    index = append(sql, index, table);
    index = append(sql, index, " where id = ");
    index = append(sql, index, id);
    return DB_get(conn, sql);
}

int DB_update(MYSQL **conn, char *table, char *id, char **key, char **value, int num){
    Table data = DB_get_by_id(conn, table, id);
    for (int i = 0; i < num; i++){
        for (int j = 0; j < data->column; j++){
            if (strcmp(key[i], data->header[j]) == 0){
                data->data[0][j] = value[i];
                break;
            }
        }
    }
    char sql[1000] = "update ";
    int index = (int)strlen(sql);
    index = append(sql, index, table);
    index = append(sql, index, " set ");
    for (int i = 1; i < data->column; i++){
        index = append(sql, index, data->header[i]);
        index = append(sql, index, " = \'");
        index = append(sql, index, data->data[0][i]);
        if (i == data->column - 1)index = append(sql, index, "\' ");
        else
        index = append(sql, index, "', ");
    }
    index = append(sql, index, " where id = ");
    index = append(sql, index, id);

    if (mysql_query(*conn, sql)){
        DB_free_data(&data);
        logger(L_ERROR, "%s; %s", (char*)mysql_error(*conn),"function: DB_update - 54");
        return -1;
    }
    DB_free_data(&data);
    logger(L_SUCCESS, "%s", "function: DB_update");
    return 1;
}
//int main(){
//    char *server = "localhost";
//    char *user = "root";
//    char *password = "gau.4605"; /* set me first */
//    char *database = "network_programing";
//
//    MYSQL *conn = mysql_init(NULL);
//
//    if (!mysql_real_connect(conn, server, user, password,
//                            database, 0, NULL, 0)) {
//        fprintf(stderr, "%s\n", mysql_error(conn));
//        exit(1);
//    }
//
////    ---------- Them ban ghi ------------
////    char table[] =  "comment";
////    char *key[] = {"post_id", "user_id", "created_at", "content"};
////    char *value[] = {"2", "2", "2022/9/12", "Ta Quang Linh"};
////    if (DB_insert(&conn, table, key, value, 4) == -1){
////        mysql_close(conn);
////        exit(0);
////    }
//
////      --------- Lay du lieu --------------
//
////    char *sql = "select * from comment;";
////    Table data = DB_get(&conn,sql);
////    int i, j;
////    for(i = 0; i < data->size; i++){
////        for (j = 0; j < data->column; j++) {
////            printf("%s, ", data->data[i][j]);
////        }
////        printf("\n");
////    }
////    DB_free_data(&data);
//
////  ------------ Get by ID ---------------
////    char *id = "1";
////    Table data = DB_get_by_id(&conn, "comment", id);
////    int i, j;
////    for(i = 0; i < data->size; i++){
////        for (j = 0; j < data->column; j++) {
////            printf("%s, ", data->data[i][j]);
////        }
////        printf("\n");
////    }
////    DB_free_data(&data);
//
////  ------------ Update table ----------------
//
////    char *id = "3";
////    char *table = "comment";
////    char *key[] = {"post_id", "content"};
////    char *value[] = {"111111", ""};
////    DB_update(&conn, table,id, key, value, 2);
//
//    mysql_close(conn);
//}
