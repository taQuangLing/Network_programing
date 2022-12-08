//
// Created by ling on 02/12/2022.
//

#ifndef LAP_TRINH_MANG_NOTIFYCATION_H
#define LAP_TRINH_MANG_NOTIFYCATION_H
typedef struct notification_t{
    int id;
    char *to_user_id;
    char *from_user_id;
    char *content;
    char type;
    char seen;
    int cmt_id; // default = -1
    int post_id; // default  = -1
}*Notification;

#endif //LAP_TRINH_MANG_NOTIFYCATION_H
