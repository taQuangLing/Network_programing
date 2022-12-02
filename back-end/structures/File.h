//
// Created by ling on 02/12/2022.
//

#ifndef LAP_TRINH_MANG_FILE_H
#define LAP_TRINH_MANG_FILE_H
typedef struct file_t{
    int id;
    int size;
    char* path;
}*File;

File file_create();

void file_print(File file);

void file_free(File file);

#endif //LAP_TRINH_MANG_FILE_H
