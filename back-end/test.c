#include "lib/db_linklist.h"
typedef struct user{
    char *username;
    char *pwd;
}*User;
void user_print(User user){
    printf("\nusername = %s", user->username);
    printf("\npassword = %s", user->pwd);
}
int main(){
    char username[] = "linh";
    char password[] = "linh";
    char username2[] = "tuan";
    char password2[] = "tuan";
    User user = (User) malloc(sizeof (struct user));
    User user2 = (User) malloc(sizeof (struct user));
    user->username = username;
    user2->username = username2;
    user->pwd = password;
    user2->pwd = password2;
    db_list_t *root = db_list_create();
    __db_list_insert_after(&root, root->limit_size, user);
    __db_list_insert_after(&root, root->limit_size, user2);
    __db_list_travel(root, (void *)user_print);
    __db_list_destory(root);
    free(user);
    free(user2);
}