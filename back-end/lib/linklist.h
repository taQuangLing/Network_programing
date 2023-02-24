/*
# This program is free software; you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation; either version 2 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU Library General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program; if not, write to the Free Software
# Foundation, Inc.
#
# See the COPYING file for license information.
#
#Copyright (c) 2013  sim szm <xianszm007@gmail.com>
*/
#ifndef DB_LINKLIST_H
#define DB_LINKLIST_H

#include <errno.h>
#include <stdio.h>
#include <stdlib.h>

extern int errno;

typedef struct db_lnode{
	void* data;
	struct db_lnode* prev;
	struct db_lnode* next;
}node;

typedef struct db_list{
	int limit_size;
	node* head;
	node* tail;
}list;

list* list_create(void );
int list_insert_before(list** ,int ,void* );

int list_insert_after(list** ,int ,void* );

void lnode_flush(list* ,int ,void* );

void list_delete(list** ,int );

void list_destory(list* );

void* list_visit(list* ,int );

void list_travel(list* ,void(*do_function)(void* ));

int list_search(list** ,void* ,int(*compare)(void* ,void* ));

void list_free(list * root);

#endif
