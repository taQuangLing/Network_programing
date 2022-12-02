//
// Created by ling on 02/12/2022.
//

#ifndef LAP_TRINH_MANG_COMMENT_H
#define LAP_TRINH_MANG_COMMENT_H

#include <malloc.h>

typedef struct comment_t{
    int id;
    int user_id;
    int post_id;
    char *created_at; // timestamp
    char *content;
}*Comment;

Comment comment_create();

void comment_free(Comment comment);

#endif //LAP_TRINH_MANG_COMMENT_H
