#include <stdio.h>

void checkMaxValue(double *currentMaxValue, double newValue) {
    if (newValue > *currentMaxValue)
        *currentMaxValue = newValue;
}

void checkMinValue(double *currentMinValue, double newValue) {
    if (newValue < *currentMinValue)
        *currentMinValue = newValue;
}

void calculateAvg(int nValues, double *currentAvg, double newValue) {
    *currentAvg = (*currentAvg * ((double) nValues - 1) / (double) nValues + newValue * (1 / (double) nValues));
}

void printMatrix(double matrix[6][3]) {

    //matriz final
    printf("\n\nFinal:\n");
    for(int i = 0; i< 6; i++)
    {
        for(int j = 0; j<3; j++)
        {
            printf("%lf  ", matrix[i][j]);
        }
        printf("\n");
    }
}

void updateMatrix(double matrix[6][3], unsigned short *readings, unsigned long readings_size, unsigned short id) {
    double *p = readings;
    int i, j;
    for (i = 0; i < readings_size; i++) {
        for (j = 0; j < 3; j++) {
            switch (j) {
                case 0:
                    if (*(p + i) != -99999)
                        checkMaxValue(&*(matrix[id] + j), *(p + i));
                    break;
                case 1:
                    if (*(p + i) != -99999)
                        checkMinValue(&*(matrix[id] + j), *(p + i));
                    break;
                case 2:
                    if (*(p + i) != -99999)
                        calculateAvg(readings_size, &*(matrix[id] + j), *(p + i));
                    break;
                default:
                    break;
            }
        }
    }
    printMatrix(matrix);
}


