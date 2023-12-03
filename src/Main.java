import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Main extends Definitions {
    public static Scanner input = new Scanner(System.in);
    public static String textFile = "";
    public static ArrayList<String> misspelledWords = new ArrayList<>();

    public static void main(String[] args) {
        startProgram();

        process("LOAD DICTIONARY");
        openTextFile(textFile);
        process("SORT");
        process("UNLOAD DICTIONARY");

        performanceAnalysis();
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
                        case 1 -> textFile = US_CONSTITUTION;
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
            case "SORT" -> {
                startTime = System.currentTimeMillis();
                SortingAlgorithm.mergeSort(misspelledWords);
                endTime = System.currentTimeMillis();

                System.out.println("\nALL UNIQUE MISSPELLED WORDS (Sorted Alphabetically)\n");
                for (String word : misspelledWords) {
                    System.out.println(word);
                }

                timeSort = (endTime - startTime) / 1000.0;
            }

            case "LOAD DICTIONARY" -> {
                startTime = System.currentTimeMillis();
                boolean loaded = Speller.load(dictionary);
                endTime = System.currentTimeMillis();

                if (!loaded) {
                    System.out.println("Could not load " + dictionary + ".");
                    startProgram();
                }
                timeLoad = (endTime - startTime) / 1000.0;
            }

            case "UNLOAD DICTIONARY" -> {
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

                        if (misspelled && !misspelledWords.contains(word)) {
                            misspelledWords.add(word);
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
        System.out.print(
                "\nWORDS MISSPELLED:     " + misspellings + "\n" +
                "WORDS IN DICTIONARY:  " + Speller.size() + "\n" +
                "WORDS IN TEXT:        " + words + "\n" +
                "TIME IN load:         " + timeLoad + "\n" +
                "TIME IN check:        " + timeCheck + "\n" +
                "TIME IN sort:         " + timeSort + "\n" +
                "TIME IN size:         " + timeSize + "\n" +
                "TIME IN unload:       " + timeUnload + "\n" +
                "TIME IN TOTAL:        " + (timeLoad + timeCheck + timeSort + timeSize + timeUnload) + "\n\n");
    }
}


