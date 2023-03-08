#include <stdio.h>
#include <stdlib.h>
#include "lib/Data.h"
#include "lib/AppUtils.h"

int main(){
    char email[] = "abc@gmail.com";
    char password[] = "123";
    Param root = param_create();
    Param tail = root;
    param_add_str(&tail, email);
    param_add_str(&tail, password);
    Data response = data_create(root, LOGIN);
    log_data(response, "Response");

}