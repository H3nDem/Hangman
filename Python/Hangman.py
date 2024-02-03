from random import randint

class Hangman():
    def __init__(self):
        self.dict = []
        self.dict_size = 0
        
        self.word = None
        self.word_size = 0
        self.word_display = []
        
        self.tries = 6
        self.load_dict()
        
    def load_dict(self):
        file = open('../minidico.txt', 'r')
        line_count = 0
        for line in file:
            self.dict.append(line)
            line_count += 1 
        self.dict_size = line_count   
        file.close()
     
    def select_word(self):
        self.word = self.dict[randint(0, self.dict_size)]
        self.word_size = len(self.word)-1
        for i in range (0,self.word_size):
            self.word_display.append('_')
    
    def check_guess(self, guess):
        if (guess == self.word[0:len(self.word)-1]): # we remove the \n is last pos because of the file reading, tampering with results
            return True
        else:
            return False
        
    def fill_blanks(self, letter):
        modified = False
        for i in range (0, self.word_size):
            if (self.word[i] == letter):
                self.word_display[i] = letter
                modified = True
        if (not modified):
            print(letter + " wasn't in the word")
        else:
            print(letter + " was in the word, nice guess !")
        return modified
    
    def reset(self):
        self.word = None
        self.word_size = 0
        self.word_display = []
        self.tries = 6

    def print_status(self):
        print(
            "dict_size :", self.dict_size, '\n',
            "word      :", self.word,
            "word_size :", self.word_size, '\n',
            "tries left :", self.tries, '\n'
            )
        self.display_guess()
        
    def display_guess(self):
        print("".join(self.word_display))
            
    def has_won(self):
        if (self.tries <= 0):
            print("You lost, the word was, ", self.word)
            return False
        else:
            print("You won !!")
            return True
        
    def increment_score(self):
        print("+1")
        file_in = open("../score.txt", 'r')
        lines = file_in.readlines()
        
        line = lines[2]
        language = line[0:9]
        score = int(line[9:len(line)]) + 1
        lines[2] = language + str(score) +'\n'
        
        file_out = open("../score.txt", 'w')
        file_out.writelines(lines)
        
        file_in.close()
        file_out.close()
        
    def retry(self):
        x = input("Wanna play again ? (y/n): ")
        if (x == 'y'):
            self.reset()
            self.select_word()
            self.start()

    def start(self):
        self.select_word()
        
        while(self.tries > 0):
            self.print_status()
            guess = input("Make a guess : ")
            
            if (len(guess) == 1):
                if (self.fill_blanks(guess) == False):
                    self.tries -= 1
            elif (self.check_guess(guess)):
                break
            else :
                self.tries -= 1
            
        if (self.has_won()):
            self.increment_score()
        
        self.retry()
            
h = Hangman()
h.start()