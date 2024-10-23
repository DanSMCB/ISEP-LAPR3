#include <stdio.h> 
#include <stdint.h>
#include "demo_pcg.h"

int state;
int inc;

int read_rnd(){

    uint64_t buffer [2]; 
    FILE *f;
    int result;
    f = fopen("/dev/urandom", "r"); 
    if (f == NULL) {
        printf("Error: open() failed to open /dev/random for reading\n"); 
        return 1;
    }else{
        result = fread(buffer, sizeof(uint64_t),2,f);
        if (result < 1){
            printf("failed to read initial random values\n");
        }else{
            state = buffer[0];
            inc = buffer[1];
            return 0;
        }
    }
    return 1;
}


