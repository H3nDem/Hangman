#include <stdio.h>
#include <string.h>
#include <stdlib.h>


int fill_string(char* str, char letter) {
    for (int i = 0; i < strlen(str); i++) {
        if (strcmp(str[i], letter) == 0) {
            str = letter;
            printf ("fill letter %c to pos %d\n", letter, i); 
        }
    }
    
}


int main() 
{
    FILE* filePointer;
    int bufferLength = 255;
    char buffer[bufferLength];
    int line_number = 0;
    int dict_size = 10;
    char **dict = (char**) malloc(dict_size*sizeof(char*)); // Allocate the size of the dict
    for (int i = 0; i < dict_size; i++) {
        dict[i] = (char*) malloc(30*sizeof(char)); // Allocate 30 characters for each string
    }
    
    
    filePointer = fopen("../minidico.txt", "r");
    if (filePointer == NULL) { printf("Cannot open the dict file\n"); exit(-1); }

    while (fgets(buffer, bufferLength, filePointer)) {
        strcpy(dict[line_number], buffer);
        if (line_number+1 == dict_size) {
            dict = (char**) realloc(dict, (dict_size + 10)*sizeof(char*));
            dict_size += 10;
            for (int i = line_number+1; i < dict_size; i++) {
                dict[i] = (char*) malloc(30*sizeof(char)); // Allocate 30 characters for each string
            }
        }
        line_number++;
    }
    int diff = (dict_size-line_number);
    dict = (char**) realloc(dict, (dict_size + 10)*sizeof(char*)); // Allocate the exact amout of space
    for (int i=dict_size-diff; i < dict_size; i++) {
        free(dict[i]);
    }
    dict_size -= diff;

    fclose(filePointer);
    
    srand(time(NULL));
    char guess[30];
    int is_runnning = 1;
    char* word = dict[rand() % dict_size-1];

    printf("The word is: %s\n", word);
    word[strlen(word) - 1] = 0; // Trunc the last annoying character we got from file
    
   
    char* word_display = (char *) malloc(strlen(word));
    strcpy(word_display, word);
    for (int i = 0; i < strlen(word); i++) {
        word_display[i] = '_';
    }
    
    

    while (is_runnning) {
        printf("Dsiplay: %s\n\n", word_display);

        printf("Take your guess: ");
        gets(guess);
        printf("Word is: %s\n", guess);

        if (strlen(guess) == 1) {
            //char* cpy;
            //strcpy(cpy, word_display);
            printf("Dsiplay: %s\n\n", "BALBLAL");
            for (int j = 0; j < strlen(word); j++) {
                if (strcmp(word[j], guess) == 0) {
                    word_display[j] = guess;
                }
            }
        }
        else if (strcmp(guess, word) == 0) { // strcmp == 0 -> same
            printf("YOU GUESSED IT, BRAVO !\n");
            is_runnning = 0;
        }

    }
    printf ("The word was: %s\n", word); 
    
    return 0;
}