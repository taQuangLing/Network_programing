//
// Created by ling on 02/12/2022.
//
#include <malloc.h>
#include "File.h"

File file_create(){
    return (File)calloc(1, sizeof (struct file_t));
}

void file_print(File file){
    printf("\nid = %d", file->id);
    printf("\npath = %s", file->path);
    printf("\nsize = %d", file->size);
}

void file_free(File file){
    free(file);
}