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
}*Table;

int DB_insert(MYSQL **conn, char *table, char **key, char **value, int num_field);

Table DB_get_by_id(MYSQL **conn, char *table, int id);

int DB_update(MYSQL **conn, char *table, int id, char **key, char **value, int num);

Table DB_get(MYSQL **conn, char *sql);

void DB_free_data(Table *data);

#endif //LAP_TRINH_MANG_DB_H
