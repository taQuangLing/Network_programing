//
// Created by ling on 02/12/2022.
//

#ifndef LAP_TRINH_MANG_NOTIFYCONTAINS_H
#define LAP_TRINH_MANG_NOTIFYCONTAINS_H
typedef struct notify_contains_t{
    int id;
    int notify_id;
    int cmt_id;
    int post_id;
}*NotifyContains;

NotifyContains notifyContains_create();

void notifyContains_print(NotifyContains notifyContains);

void notifyContains_free(NotifyContains notifyContains);

#endif //LAP_TRINH_MANG_NOTIFYCONTAINS_H
