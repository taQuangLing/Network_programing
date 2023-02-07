#include <stdio.h>
#include <stdlib.h>
#include "structures/Data.h"
#include "lib/AppUtils.h"

int main(){
    FILE *f, *fw;
    int n;
    if ((f = fopen("database/Ai lay mieng phomat cua roi.pdf", "rb")) == NULL){
        printf("ERROR OPEN FILE");
        exit(0);
    }
    fw = fopen("Ai lay mieng phomat cua roi.pdf", "wb");
    char buff[1024] = {0};
    while ((n = fread(buff, 1, 1024, f)) > 0) {
        if (fwrite(buff, 1, 1024, fw) < 1024)printf("error");
        refresh(buff, 1024);
    }

}