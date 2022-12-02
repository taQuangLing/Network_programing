//
// Created by ling on 02/12/2022.
//
#include <malloc.h>
#include "File.h"

File file_create(){
    File file = (File)malloc(sizeof (struct file_t));
    file->id = 0;
    file->path = NULL;
    file->size = 0;
    return file;
}

void file_print(File file){
    printf("\nid = %d", file->id);
    printf("\npath = %s", file->path);
    printf("\nsize = %d", file->size);
}

void file_free(File file){
    free(file);
}