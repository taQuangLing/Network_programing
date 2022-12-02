//
// Created by ling on 02/12/2022.
//

#ifndef LAP_TRINH_MANG_FOLLOW_H
#define LAP_TRINH_MANG_FOLLOW_H

typedef struct follow_t{
    int id;
    int user_id;     // user -> user1
    int follower_id; // follower -> user
}*Follow;


#endif //LAP_TRINH_MANG_FOLLOW_H
