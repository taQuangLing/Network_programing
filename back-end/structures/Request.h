//
// Created by ling on 09/12/2022.
//

#ifndef LAP_TRINH_MANG_REQUEST_H
#define LAP_TRINH_MANG_REQUEST_H

#include "../lib/message.h"

typedef struct request_t{
    int size;
    message_code message;
    char **data;
}*Request;

#endif //LAP_TRINH_MANG_REQUEST_H
