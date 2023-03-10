//
// Created by ling on 09/12/2022.
//

#ifndef LAP_TRINH_MANG_DB_H
#define LAP_TRINH_MANG_DB_H

#include <mysql/mysql.h>

typedef struct data_t{
    long size; // so hang
    int column; // so cot
    char **header; // tieu de
    char ***data; // data
    MYSQL_RES *result;
}*Table;

int DB_insert_v2(MYSQL **conn, char *sql);

int DB_insert(MYSQL **conn, char *table, char **key, char **value, int num_field);

Table DB_get_by_id(MYSQL **conn, char *table, int id);

int DB_update(MYSQL **conn, char *table, int id, char **key, char **value, int num);

int DB_update_v2(MYSQL **conn, char *sql);

Table DB_get(MYSQL **conn, char *sql);

void DB_free_data(Table *data);

char *DB_str_get_by(Table data, char *field);

int DB_int_get_by(Table data, char *field);

int DB_update_cell(Table data, char *field, char *value);

int DB_update_v3(MYSQL **conn, Table data);

int DB_delete(MYSQL **, char *, int );
#endif //LAP_TRINH_MANG_DB_H
