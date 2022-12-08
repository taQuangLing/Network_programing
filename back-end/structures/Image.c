//
// Created by ling on 08/12/2022.
//
#include <malloc.h>
#include "Image.h"

Image image_create(){
    return (Image)calloc(1, sizeof (struct image_t));
}

void image_print(Image image){
    printf("\nid = %d", image->id);
    printf("\nsize = %d", image->size);
    printf("\npath = %s", image->path);
}

void image_free(Image image){
    free(image);
}