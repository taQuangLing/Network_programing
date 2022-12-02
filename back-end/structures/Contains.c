//
// Created by ling on 02/12/2022.
//
#include <malloc.h>
#include "Contains.h"

Contains contains_create(){
    Contains contains = (Contains)malloc(sizeof (struct contains_t));
    contains->post_id = 0;
    contains->id = 0;
    contains->file_id = 0;
    contains->image_id = 0;
    return contains;
}

void contains_print(Contains contains){
    printf("\nid = %d", contains->id);
    printf("\npost id = %d", contains->post_id);
    printf("\nimage id = %d", contains->image_id);
    printf("\nfile id = %d", contains->file_id);
}

void contains_free(Contains contains){
    free(contains);
}