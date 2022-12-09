//
// Created by ling on 02/12/2022.
//
#include "AppUtils.h"
#include <time.h>
#include <stdio.h>
#include <malloc.h>
#include "string.h"

char *convert_timestamp(char *timestamp){
    time_t t = timestamp;
    const char format[] = "%H:%M:%S %Y:%m:%d";

    struct tm lt;
    char res[32];

    (void) localtime_r(&t, &lt);

    if (strftime(res, sizeof(res), format, &lt) == 0) {
        (void) fprintf(stderr,  "strftime(3): cannot format supplied "
                                "date/time into buffer of size %lu "
                                "using: '%s'\n",
                       sizeof(res), format);
        return NULL;
    }

    return res;
}

int append(char *string, int index, char *data){
    return index + sprintf(string + index, "%s", data);
}