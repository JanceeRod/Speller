import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Main extends Definitions {
    public static Scanner input = new Scanner(System.in);
    public static String textFile = "";

    public static void main(String[] args) {
        startProgram();

        process("load");
        openTextFile(textFile);
        process("unload");

        performanceAnalysis();

        misspellings = 0;
        words = 0;
    }

    public static void startProgram() {
        System.out.print(
                "--- SPELL CHECKER ---\n\n" +
                "Please pick the text file you want to spell check\n" +
                "   [1] US Constitution\n" +
                "   [2] Lalaland Manuscript\n" +
                "   [3] Shakespeare's work\n\n" +
                "   [4] Exit Program\n\n" +
                "Enter your choice: ");
        handleUserInput();
    }

    public static void handleUserInput() {
        while (true) {
            if (input.hasNextInt()) {
                int choice = input.nextInt();
                if (choice >= 1 && choice < 5) {
                    switch (choice) {
                        case 1 -> textFile = CONSTITUTION;
                        case 2 -> textFile = LALALAND;
                        case 3 -> textFile = SHAKESPEARE;
                        case 4 -> System.exit(0);
                    }
                    break;
                } else {
                    System.out.println("Invalid. Expected input: A number within 1 to 4.");
                }
            } else {
                System.out.println("Invalid Input. Expected Input: A number within 1 to 4");
                input.next();
            }
            System.out.println("\nEnter your choice: ");
        }
    }

    public static void process(String operation) {
        switch (operation) {
            case "load" -> {
                startTime = System.currentTimeMillis();
                boolean loaded = Speller.load(dictionary);
                endTime = System.currentTimeMillis();

                if (!loaded) {
                    System.out.println("Could not load " + dictionary + ".");
                    startProgram();
                }
                timeLoad = (endTime - startTime) / 1000.0;
            }

            case "unload" -> {
                startTime = System.currentTimeMillis();
                boolean unloaded = Speller.unload();
                endTime = System.currentTimeMillis();

                if (!unloaded) {
                    System.out.println("Could not unload " + dictionary + ".");
                    startProgram();
                }
                timeUnload = (endTime - startTime) / 1000.0;
            }
        }
    }

    public static void openTextFile(String text) {
        try (BufferedReader reader = new BufferedReader(new FileReader(text))) {
            System.out.println("\nMISSPELLED WORDS\n");
            String line;

            while ((line = reader.readLine()) != null) {
                StringTokenizer tokenizer = new StringTokenizer(line);

                while (tokenizer.hasMoreTokens()) {
                    String word = tokenizer.nextToken();
                    if (word.matches("[a-zA-Z]+")) {
                        words++;

                        startTime = System.currentTimeMillis();
                        boolean misspelled = !Speller.check(word);
                        endTime = System.currentTimeMillis();
                        timeCheck += (endTime - startTime) / 1000.0;

                        if (misspelled) {
                            System.out.println(word);
                            misspellings++;
                        }
                    }
                }
            }
        }
        catch (IOException e) {
            System.out.println("Error reading " + text + ".");
            e.printStackTrace();
            Speller.unload();
            startProgram();
        }
    }

    public static void performanceAnalysis() {
        System.out.println("\nWORDS MISSPELLED:     " + misspellings);
        System.out.println("WORDS IN DICTIONARY:  " + Speller.size());
        System.out.println("WORDS IN TEXT:        " + words);
        System.out.println("TIME IN load:         " + timeLoad);
        System.out.println("TIME IN check:        " + timeCheck);
        System.out.println("TIME IN size:         " + timeSize);
        System.out.println("TIME IN unload:       " + timeUnload);
        System.out.println("TIME IN TOTAL:        " + (timeLoad + timeCheck + timeSize + timeUnload) + "\n");
    }
}


