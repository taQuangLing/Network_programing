#include <malloc.h>
#include <string.h>
#include <stdlib.h>
#include "Data.h"
#include "../lib/AppUtils.h"

//
// Created by ling on 01/02/2023.
//
Param param_create(){
    Param elm = (Param)malloc(sizeof (struct param_t));
    elm->value = NULL;
    elm->id = -1;
    elm->next = NULL;
    return elm;
}
void param_add_str(Param *tail, char *param){
    if ((*tail)->id == -1){
        char *string = (char*) malloc(strlen(param) + 1);
        strcpy(string, param);
        (*tail)->value = string;
        (*tail)->id = 0;
        return;
    }
    Param elm = param_create();
    char *string = (char*) malloc(strlen(param));
    strcpy(string, param);
    elm->value = string;
    elm->id = (*tail)->id + 1;
    (*tail)->next = elm;
    (*tail) = (*tail)->next;
}
void param_add_int(Param *tail, int param){
    char i2s[10];
    refresh(i2s, 10);
    intTostr(param, i2s);
    param_add_str(tail, i2s);
}
void param_free(Param *root){
    Param p = *root;
    while(*root != NULL){
        (*root) = (*root)->next;
        free(p->value);
        free(p);
        p = *root;
    }
}
Data data_create(Param root, MessageCode code){
    Data data = (Data)malloc(sizeof (struct data));
    data->params = root;
    data->message = code;
    return data;
}
void data_free(Data *data){
    if ((*data)->params != NULL)param_free(&((*data)->params));
    free(*data);
}
void data_print(Data data){
    printf("\nMessage = %d,\n", data->message);
    int i = 0;
    for(Param p = data->params; p != NULL; p = p->next){
        printf("Param[%d] = %s\n", i++, p->value);
    }
}
char *param_get_str(Param *p){
    Param result = *p;
    *p = (*p)->next;
    return result->value;
}
int param_get_int(Param *p){
    return atoi(param_get_str(p));
}