#include <stdio.h>
#include "structures/Data.h"
#include "lib/AppUtils.h"

int main(){
    Param root = param_create();
    Param tail = root;
    param_add_int(&tail, 100);
    param_add_str(&tail, "Ta QUang Linh");
    param_add_str(&tail, "abccccc");
    param_add_str(&tail, "deffff");
    param_add_str(&tail, "Kml");

    Data data = data_create(root, CHAT);
    char str[100];
    refresh(str, 100);
    char *s = data_to_str(data, 0);
    data_print(data);
    printf("\nstring = %s", s);
    Data data2 = str_to_data(s, 0);
    data_print(data2);

}