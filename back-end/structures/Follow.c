//
// Created by ling on 02/12/2022.
//
#include <malloc.h>
#include "Follow.h"

Follow follow_create(){
    return (Follow)calloc(1, sizeof (struct follow_t));
}

void follow_print(Follow follow){
    printf("\nid = %d", follow->id);
    printf("\nuser id = %d", follow->user_id);
    printf("\nfollow id = %d", follow->follower_id);
}

void follow_free(Follow follow){
    free(follow);
}