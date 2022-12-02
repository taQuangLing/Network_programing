//
// Created by ling on 02/12/2022.
//

#ifndef LAP_TRINH_MANG_CONTAINS_H
#define LAP_TRINH_MANG_CONTAINS_H
typedef struct contains_t{
    int id;
    int image_id;
    int post_id;
    int file_id;
}*Contains;

Contains contains_create();

void contains_print(Contains contains);

void contains_free(Contains contains);

#endif //LAP_TRINH_MANG_CONTAINS_H
