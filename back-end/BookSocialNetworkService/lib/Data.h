//
// Created by ling on 09/12/2022.
//

#ifndef LAP_TRINH_MANG_DATA_H
#define LAP_TRINH_MANG_DATA_H

#include "MessageCode.h"

typedef  struct param_t{
    int id; // so thu tu bat dau tu 0
    char* value; // gia tri tham so
    struct param_t *next;
}*Param;
typedef struct data{
    char token[21];
    MessageCode message;
    Param params; // danh sach tham so
}*Data;

Param param_create();
void param_add_str(Param *tail, char *str);
void param_add_int(Param *tail, int param);
void param_free(Param *root);
Data data_create(Param root, MessageCode code);
Data data_create_v2(Param root, MessageCode code, char token[]);
void data_free(Data *data);
void log_data(Data data, char*);
char *param_get_str(Param *p);
int param_get_int(Param *p);
#endif //LAP_TRINH_MANG_DATA_H

// Param root = param_create(); Param tail = root;
// param_add(&tail, param);
// data_create(root, code);
