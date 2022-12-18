//
// Created by ling on 09/12/2022.
//

#ifndef LAP_TRINH_MANG_MESSAGE_H
#define LAP_TRINH_MANG_MESSAGE_H

typedef enum {
    error
}message_code;

void print_mess(char *s1, char *s2);

void message(message_code code, char *param);
#endif //LAP_TRINH_MANG_MESSAGE_H
