//
// Created by ling on 08/12/2022.
//
#include <stdlib.h>
#include <stdio.h>
#include "Post.h"

Post post_create(){
    return (Post)calloc(1, sizeof (struct post_t));
}

void post_print(Post post){
    printf("\nid = %d", post->id);
    printf("\nuser id = %d", post->user_id);
    printf("\ncontent = %s", post->content);
    printf("\nbook = %s", post->book);
    printf("\nimage = %s", post->image);
    printf("\nfile = %s", post->file);
    printf("\ncreated_at = %s", post->created_at);
    printf("\ntitle = %s", post->title);
    printf("\nsize file = %d", post->size_file);
    printf("\nsize image = %d", post->size_image);
    printf("\nstatus = %d", post->status);
}

void post_free(Post post){
    free(post);
}