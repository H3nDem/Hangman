import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Random;
import java.util.stream.Stream;

public class Hangman {
    private final List<String> dict = new ArrayList<>();
    private String word = "";
    private String wordDisplay = "";
    private int tries = 6;
    private final Random generator = new Random();


    public Hangman() throws FileNotFoundException {
        loadDict();
    }

    public void start() throws IOException {
        selectWord();
        initWordDisplay();
        printStatus();
        
        Scanner input = new Scanner(System.in);
        displayWord();

        while (this.tries > 0) {
            System.out.print("Make your guess: ");
            String guess = input.nextLine();
            System.out.println();

            if (guess.isBlank() || guess.isEmpty()) {
                System.out.println("You entered nothing, please write something");
            }
            else if (guess.length() == 1) {
                if (fillBlanks(guess.charAt(0))) {
                    System.out.println(guess.charAt(0) + " was in the word, nice!");
                } else {
                    System.out.println(guess.charAt(0) + " wasn't in the word");
                    this.tries--;
                }
            }
            else if (guess.equals(this.word)) {
                System.out.println("You guess the word !! GG");
                break;
            } else {
                System.out.println("Not the right guess, try again");
                this.tries--;
            }

            System.out.println(this.tries + " left");
            displayWord();
        }
        hasWon();
        retry();
    }
    private void hasWon() throws IOException {
        if (this.tries > 0) {
            System.out.println("You win !");
            addScoreToFile();
        } else {
            System.out.println("You lost... better luck next time !");
        }
    }

    private void retry() throws IOException {
        Scanner in = new Scanner(System.in);
        System.out.print("Wanna retry ?: ");
        String answer = in.nextLine();
        System.out.println();
        if (answer.isEmpty() || answer.isBlank() || (!answer.equals("n")&&!answer.equals("y"))) {
            System.out.println("Enter 'y' or 'n' to answer");
            retry();
        }
        else if (answer.equals("y")) {
            reset();
            start();
        }
        else {
            System.exit(11);
        }
    }
    private void reset() {
        this.word = "";
        this.wordDisplay = "";
        this.tries = 6;
    }

    private void loadDict() throws FileNotFoundException {
        Scanner fileIn = new Scanner(new File("../minidico.txt"));
        while (fileIn.hasNextLine()) {
            this.dict.add(fileIn.nextLine());
        }
        fileIn.close();
    }

    private void selectWord() {
        int rand = this.generator.nextInt(this.dict.size());
        this.word = this.dict.get(rand);
    }
    private void initWordDisplay() {
        for (int i = 0; i < this.word.length(); i++) {
            this.wordDisplay += "_";
        }
    }

    private void printStatus() {
        System.out.println(
                "Dict: " + this.dict + '\n' +
                "DictSize: " + this.dict.size() + '\n' +
                "Word: " + this.word + '\n' +
                "WordSize: " + this.word.length() + '\n'
        );
    }
    private void displayWord() {
        for (int i = 0; i < this.wordDisplay.length(); i++) {
            System.out.print(this.wordDisplay.charAt(i));
        }
        System.out.println();
    }

    private boolean fillBlanks(Character letter) {
        String updated = "";
        for (int i = 0; i < this.wordDisplay.length(); i++) {
            if (this.word.charAt(i) == letter) {
                updated += letter;
            }
            else {
                updated += this.wordDisplay.charAt(i);
            }
        }
        this.wordDisplay = updated;
        return this.wordDisplay.contains(letter.toString());
    }

    public void addScoreToFile() throws IOException {
        String[] newFile = new String[6];
        Scanner fileIn = new Scanner(new File("../score.txt"));
        int lineCount = 0;

        while (fileIn.hasNext()) {
            String line = fileIn.nextLine();
            if (lineCount == 3) {
                String sub = line.substring(0,7);
                int score = Integer.parseInt(line.substring(7)) + 1;
                String updatedLine = sub + score;
                newFile[lineCount] = updatedLine;
            } else {
                newFile[lineCount] = line;
            }
            lineCount++;
        }
        fileIn.close();

        BufferedWriter fileOut = new BufferedWriter(new FileWriter("../score.txt"));
        for (int i = 0; i < newFile.length; i++) {
            fileOut.write(newFile[i]+'\n');
        }
        fileOut.close();
    }
}
