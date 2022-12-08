//
// Created by ling on 02/12/2022.
//

#ifndef LAP_TRINH_MANG_POST_H
#define LAP_TRINH_MANG_POST_H
typedef struct post_t{
    int id;
    char *book;
    char *title;
    char *created_at; // timestamp
    char *content;
    int user_id;
    char status; // 0: private, 1:public
    int size_image;
    char *image; // path
    int size_file;
    char *file; // path
}*Post;

Post post_create();

void post_print(Post post);

void post_free(Post post);

#endif //LAP_TRINH_MANG_POST_H
