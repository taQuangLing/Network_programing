//
// Created by ling on 02/12/2022.
//

#ifndef LAP_TRINH_MANG_IMAGE_H
#define LAP_TRINH_MANG_IMAGE_H
typedef struct image_t{
    int id;
    int size;
    char* path;
}*Image;

Image image_create();

void image_print(Image image);

void image_free(Image image);

#endif //LAP_TRINH_MANG_IMAGE_H
