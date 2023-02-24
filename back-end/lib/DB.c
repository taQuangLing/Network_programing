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
    if (mysql_query(*conn, sql)){
        logger(L_ERROR, "function: DB_insert, sql = %s. %s.", sql, (char*)mysql_error(*conn));
        return -1;
    }
    logger(L_INFO, "function: DB_insert, sql = %s", sql);
    return 1;
}
int DB_insert_v2(MYSQL **conn, char *sql){
    if (mysql_query(*conn, sql)){
        logger(L_ERROR, "function: DB_insert, sql = %s. %s.", sql, (char*)mysql_error(*conn));
        return -1;
    }
    logger(L_INFO, "function: DB_insert, sql = %s", sql);
    return 1;
}
Table DB_get(MYSQL **conn, char *sql){
    MYSQL_FIELD *field;
    MYSQL_RES *res;
    MYSQL_ROW row;

    Table data = (Table) malloc(sizeof (struct data_t));

    if (mysql_query(*conn, sql)) {
        logger(L_ERROR, "function: DB_get - 48. %s; %s", sql,(char*)mysql_error(*conn));
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
    logger(L_INFO, "function: DB_get. %s", sql);
    return data;
}

Table DB_get_by_id(MYSQL **conn, char *table, int id){
    char idS[10];
    refresh(idS, 10);
    intTostr(id, idS);
    char sql[100] = "select * from ";
    int index = (int)strlen(sql);
    index = append(sql, index, table);
    index = append(sql, index, " where id = ");
    index = append(sql, index, idS);
    return DB_get(conn, sql);
}

int DB_update(MYSQL **conn, char *table, int id, char **key, char **value, int num){
    char idS[10];
    refresh(idS, 10);
    intTostr(id, idS);

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
        if (data->data[0][i] == NULL)continue;
        index = append(sql, index, data->header[i]);
        index = append(sql, index, " = \'");
        index = append(sql, index, data->data[0][i]);
        index = append(sql, index, "', ");
    }
    index -= 2;
    index = append(sql, index, " where id = ");
    index = append(sql, index, idS);

    if (mysql_query(*conn, sql)){
        DB_free_data(&data);
        logger(L_ERROR, "function: update(), sql = \"%s\"; %s", sql, (char*)mysql_error(*conn));
        return -1;
    }
    DB_free_data(&data);
    logger(L_INFO, "%s. sql = \"%s\"", "function: DB_update", sql);
    return 1;
}
int DB_update_v2(MYSQL **conn, char *sql){
    if (mysql_query(*conn, sql)){
        logger(L_ERROR, "function: update(), sql = \"%s\"; %s", sql, (char*)mysql_error(*conn));
        return -1;
    }
    logger(L_INFO, "%s. sql = \"%s\"", "function: DB_update", sql);
    return 1;
}
char *DB_str_get_by(Table data, char *field){
    for (int i = 0; i < data->column; i++){
        if (strcmp(data->header[i], field) == 0)return data->data[0][i];
    }
    logger(L_ERROR, "Không tồn tại trường: %s", field);
    return NULL;
}
int DB_int_get_by(Table data, char *field){
    int id =  atoi(DB_str_get_by(data, field));
}
int DB_update_cell(Table data, char *field, char *value){
    for (int i = 0; i < data->column; i++){
        if (strcmp(data->header[i], field) == 0){
            data->data[0][i] = value;
            return 1;
        }
    }
    logger(L_ERROR, "Không tồn tại trường: %s", field);
    return -1;
}
int DB_update_v3(MYSQL **conn, Table data){
    char sql[1000] = {0};
    sprintf(sql, "update user set name = '%s', avatar = '%s', bio = '%s', gender = %d, birthday = '%s', interest = '%s' where id = %s",
            DB_str_get_by(data, "name"),
            DB_str_get_by(data, "avatar"),
            DB_str_get_by(data, "bio"),
            DB_int_get_by(data, "gender"),
            DB_str_get_by(data, "birthday"),
            DB_str_get_by(data, "interest"),
            DB_str_get_by(data, "id"));
    return DB_update_v2(conn, sql);
}
int DB_delete(MYSQL **conn, char *table, int id){
    char sql[100] = {0};
    sprintf(sql, "delete from %s where id = %d", table, id);
    if (mysql_query(*conn, sql)){
        logger(L_ERROR, "function: delete(), sql = \"%s\"; %s", sql, (char*)mysql_error(*conn));
        return -1;
    }
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
