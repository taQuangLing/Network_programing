//
// Created by ling on 08/12/2022.
//
#include "User.h"
#include "stdlib.h"
#include "stdio.h"

User user_create(){
    return (User)calloc(1, sizeof (struct user_t));
}

void user_print(User user){
    printf("\nid = %d", user->id);
    printf("\nname = %s", user->name);
    printf("\navatar = %s", user->avatar);
    printf("\nbio = %s", user->bio);
    printf("\nbirthday = %s", user->birthday);
    printf("\nemail = %s", user->email);
    printf("\ngender = %c", user->gender);
    printf("\npassword = %s", user->pwd);
    printf("\ncreated_at = %s", user->created_at);
}

void user_free(User user){
    free(user);
}