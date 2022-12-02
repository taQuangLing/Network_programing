//
// Created by ling on 02/12/2022.
//
#include "Comment.h"

Comment comment_create(){
    Comment comment = (Comment)malloc(sizeof (struct comment_t));
    comment->id = 0;
    comment->content = NULL;
    comment->post_id = 0;
    comment->user_id = 0;
    comment->created_at = NULL;
    return comment;
}

void comment_print(Comment comment){
    printf("\nid = %d", comment->id);
    printf("\npost id = %d", comment->post_id);
    printf("\nuser id = %d", comment->user_id);
    printf("\ncontent = %s", comment->content);
    printf("\ncreated at = %s", comment->created_at);
}

void comment_free(Comment comment){
    free(comment);
}