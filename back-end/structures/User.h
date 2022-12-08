//
// Created by ling on 02/12/2022.
//

#ifndef LAP_TRINH_MANG_USER_H
#define LAP_TRINH_MANG_USER_H

typedef struct user_t{
    int id;
    char *name;
    char *email;
    char* pwd;
    char *bio;
    char *created_at; // timestamp
    char* birthday;
    char gender;
    char *avatar;
}*User;


#endif //LAP_TRINH_MANG_USER_H
