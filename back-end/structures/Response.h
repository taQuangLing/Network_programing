//
// Created by ling on 09/12/2022.
//

#ifndef LAP_TRINH_MANG_RESPONSE_H
#define LAP_TRINH_MANG_RESPONSE_H

#include "../lib/message.h"
#include "../lib/Record.h"

typedef struct response_t{
    int size;
    message_code message;
    db_list_t data;
}*Response;

#endif //LAP_TRINH_MANG_RESPONSE_H
