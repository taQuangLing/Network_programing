//
// Created by ling on 02/12/2022.
//

#ifndef LAP_TRINH_MANG_REACTION_H
#define LAP_TRINH_MANG_REACTION_H

typedef struct reaction_t{
    int id;
    int user_id;
    int post_id;
}*Reaction;

Reaction reaction_create();

void reaction_print(Reaction reaction);

void reaction_free(Reaction reaction);

#endif //LAP_TRINH_MANG_REACTION_H
