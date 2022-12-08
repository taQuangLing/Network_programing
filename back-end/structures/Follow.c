//
// Created by ling on 02/12/2022.
//
#include <malloc.h>
#include "Follow.h"

Follow follow_create(){
    return (Follow)calloc(1, sizeof (struct follow_t));
}

void