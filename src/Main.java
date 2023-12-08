import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main extends Definitions {
    public static void main(String[] args) {
        programFlow();
    }

    public static void programFlow() {
        startProgram();

        process("LOAD DICTIONARY");
        openTextFile(textFile);
        process("SORT");
        process("UNLOAD DICTIONARY");

        performanceAnalysis();

        promptWhatToDo();
    }

    public static void startProgram() {
        System.out.print(
                """
                        
                    --- SPELL CHECKER ---

                    Please pick the text file you want to spell check
                       [1] US Constitution
                       [2] Lalaland Manuscript
                       [3] Shakespeare's work
                       [4] Test File

                       [5] Exit Program

                    Enter your choice:\s""");
        handleUserInput();
    }

    public static void handleUserInput() {
        while (true) {
            if (input.hasNextInt()) {
                int choice = input.nextInt();
                if (choice >= 1 && choice < 6) {
                    switch (choice) {
                        case 1 -> textFile = US_CONSTITUTION;
                        case 2 -> textFile = LALALAND;
                        case 3 -> textFile = SHAKESPEARE;
                        case 4 -> textFile = TEST;
                        case 5 -> System.exit(0);
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
                    programFlow();
                }
                timeLoad = (endTime - startTime) / 1000.0;
            }

            case "UNLOAD DICTIONARY" -> {
                startTime = System.currentTimeMillis();
                boolean unloaded = Speller.unload();
                endTime = System.currentTimeMillis();

                if (!unloaded) {
                    System.out.println("Could not unload " + dictionary + ".");
                    programFlow();
                }
                timeUnload = (endTime - startTime) / 1000.0;
            }
        }
    }

    public static void openTextFile(String text) {
        try (BufferedReader reader = new BufferedReader(new FileReader(text))) {
            String line;

            while ((line = reader.readLine()) != null) {
                // Use a regular expression to split the line into words, considering punctuation
                String[] wordsInLine = line.split("\\s+|(?=[.,;!?()\\[\\]])|(?<=[.,;!?()\\[\\]])");

                for (String word : wordsInLine) {
                    // Skip empty strings
                    if (word.trim().isEmpty()) {
                        continue;
                    }

                    // Check if the word contains only letters
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
        } catch (IOException e) {
            System.out.println("Error reading " + text + ".");
            e.printStackTrace();
            Speller.unload();
            programFlow();
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

    public static void promptWhatToDo() {
        System.out.print(
                """
                   What do you want to do?
                      [1] Spell check another file
                      [2] Correct misspelled words
                      [3] Exit Program

                   Enter your choice:\s"""
        );
        while (true) {
            if (input.hasNextInt()) {
                int nextDo = input.nextInt();
                input.nextLine();
                if (nextDo >= 1 && nextDo < 4) {
                    switch (nextDo) {
                        case 1 -> programFlow();
                        case 2 -> userCorrection();
                        case 3 -> System.exit(0);
                    }
                    break;
                } else System.out.println("Invalid. Expected input: A number within 1 to 3.");
            } else {
                System.out.println("Invalid. Expected input: A number within 1 to 3.");
            }
        }
    }

    public static void userCorrection() {
        // Create a copy of the misspelledWords list to avoid concurrent modification
        ArrayList<String> misspelledCopy = new ArrayList<>(misspelledWords);

        // Read the original text file
        try (BufferedReader reader = new BufferedReader(new FileReader(textFile))) {
            StringBuilder correctedText = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                StringTokenizer tokenizer = new StringTokenizer(line, " \t\n\r\f,;.:!?()[]");

                while (tokenizer.hasMoreTokens()) {
                    String token = tokenizer.nextToken();
                    // Separate the word and trailing punctuation
                    String word = token.replaceAll("[^a-zA-Z]", "");
                    String punctuation = token.substring(word.length());

                    // Check if the word contains only letters
                    if (word.matches("[a-zA-Z]+") && misspelledCopy.contains(word)) {
                        // Check if the corrected version is already in the map
                        String correctedVersion = correctedWords.get(word);
                        if (correctedVersion == null) {
                            // If not, prompt the user for correction
                            correctedVersion = correctWord(word);
                            // Store the corrected version in the map
                            correctedWords.put(word, correctedVersion);
                        }

                        // Use the corrected version with the original punctuation
                        correctedText.append(correctedVersion).append(punctuation).append(" ");
                    } else {
                        correctedText.append(token).append(" ");
                    }
                }
                correctedText.append("\n");
            }

            // Write the corrected text back to the original file
            Files.write(Paths.get(textFile), correctedText.toString().getBytes());
            System.out.println("\nEdited text file successfully!");

        } catch (Exception e) {
            System.out.println("Error reading or writing " + textFile + ".");
            e.printStackTrace();
            Speller.unload();
            programFlow();
        }
    }

    public static String correctWord(String wrongWord) {
        System.out.println("\nWhat is the correct spelling of " + wrongWord + "?");

        // Display suggestions
        List<String> suggestions = Levenshtein.getSuggestions(wrongWord, DICTIONARY, 20);
        System.out.println("Did you mean:");

        for (String suggestion : suggestions) {
            System.out.println("  " + suggestion);
        }

        System.out.print("Enter correct spelling: ");

        while (true) {
            if (input.hasNextLine()) {
                String correctWord = input.nextLine().trim();

                if (correctWord.equals("")) return wrongWord;

                if (correctWord.matches("[a-zA-Z]+") && Speller.check(correctWord)) {
                    return correctWord;
                } else if (!Speller.check(correctWord)) {
                    System.out.println("Word is spelled wrong");
                } else {
                    System.out.println("Please enter a valid spelling containing only letters.");
                }
            }
            System.out.print("Enter: ");
        }
    }
}


