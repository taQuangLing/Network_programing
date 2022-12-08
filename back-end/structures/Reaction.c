//
// Created by ling on 08/12/2022.
//

#include <stdlib.h>
#include <stdio.h>
#include "Reaction.h"

Reaction reaction_create(){
    return (Reaction)calloc(1, sizeof (struct reaction_t));
}

void reaction_print(Reaction reaction){
    printf("\nid = %d", reaction->id);
    printf("\nuser id = %d", reaction->user_id);
    printf("\npost id = %d", reaction->post_id);
}

void reaction_free(Reaction reaction){
    free(reaction);
}