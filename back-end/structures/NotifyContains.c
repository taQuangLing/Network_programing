//
// Created by ling on 08/12/2022.
//
#include <stdlib.h>
#include <stdio.h>
#include "NotifyContains.h"

NotifyContains notifyContains_create(){
    return (NotifyContains)calloc(1, sizeof (struct notify_contains_t));
}

void notifyContains_print(NotifyContains notifyContains){
    printf("\nid = %d", notifyContains->id);
    printf("\npost id = %d", notifyContains->post_id);
    printf("\ncomment id = %d", notifyContains->cmt_id);
    printf("\nnotify id = %d", notifyContains->notify_id);
}

void notifyContains_free(NotifyContains notifyContains){
    free(notifyContains);
}