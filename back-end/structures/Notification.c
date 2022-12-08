//
// Created by ling on 08/12/2022.
//
#include <stdlib.h>
#include <stdio.h>
#include "Notification.h"

Notification notification_create(){
    return (Notification)calloc(1, sizeof (struct notification_t));
}

void notification_print(Notification notification){
    printf("\nid = %d", notification->id);
    printf("\nto user id = %d", notification->to_user_id);
    printf("\nfrom user id = %d", notification->from_user_id);
    printf("\ncontent = %s", notification->content);
    printf("\npost id = %d", notification->post_id);
    printf("\ncomment id = %d", notification->cmt_id);
    printf("\nseen = %d", notification->seen);
    printf("\ntype = %d", notification->type);
}

void notification_free(Notification notification){
    free(notification);
}
