//
// Created by ling on 02/12/2022.
//

#ifndef LAP_TRINH_MANG_NOTIFICATION_H
#define LAP_TRINH_MANG_NOTIFICATION_H
typedef struct notification_t{
    int id;
    int to_user_id;
    int from_user_id;
    char *content;
    char type;
    char seen;
    int cmt_id; // default = -1
    int post_id; // default  = -1
}*Notification;

Notification notification_create();

void notification_print(Notification notification);

void notification_free(Notification notification);

#endif //LAP_TRINH_MANG_NOTIFICATION_H
