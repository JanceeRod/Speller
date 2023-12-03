import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Speller {
    private static final String DICTIONARY = "dictionaries/large";

    // time trackers
    private static double timeLoad = 0.0, timeCheck = 0.0, timeSize = 0.0, timeUnload = 0.0;

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.print(
                "--- SPELL CHECKER ---\n\n" +
                "Please pick the text file you want to spell check\n" +
                "   [1] US Constitution\n" +
                "   [2] Lalaland Manuscript\n" +
                "   [3] Shakespeare's work\n\n" +
                "Enter your choice: ");

        String text = "";
        while (true) {
            if (input.hasNextInt()) {
                int choice = input.nextInt();
                if (choice >= 1 && choice < 4) {
                    System.out.println("yey");
                    switch (choice) {
                        case 1: text = "texts/constitution.txt";
                                break;
                        case 2: text = "texts/lalaland.txt";
                                break;
                        case 3: text = "texts/shakespeare.txt";
                                break;
                    }
                    break;
                }
                else {
                    System.out.println("Invalid. Expected input: A number within 1 to 3.");
                }
            }
            else {
                System.out.println("Invalid Input. Expected Input: A number within 1 to 3");
                input.next();
            }

            System.out.println("\nEnter your choice: ");
        }

        long startTime, endTime;
        String dictionary = DICTIONARY;

        startTime = System.currentTimeMillis();
        boolean loaded = load(dictionary);
        endTime = System.currentTimeMillis();

        if (!loaded) {
            System.out.println("Could not load " + dictionary + ".");
            System.exit(1);
        }

        timeLoad = (endTime - startTime) / 1000.0;

        // attempt to open text
        try (BufferedReader reader = new BufferedReader(new FileReader(text))) {
            System.out.println("\nMISSPELLED WORDS\n");

            int misspellings = 0, words = 0;
            String line;

            while ((line = reader.readLine()) != null) {
                StringTokenizer tokenizer = new StringTokenizer(line);

                while (tokenizer.hasMoreTokens()) {
                    String word = tokenizer.nextToken();
                    if (word.matches("[a-zA-Z]+")) {
                        words++;

                        startTime = System.currentTimeMillis();
                        boolean misspelled = !check(word);
                        endTime = System.currentTimeMillis();

                        timeCheck += (endTime - startTime) / 1000.0;

                        if (misspelled) {
                            System.out.println(word);
                            misspellings++;
                        }
                    }
                }
            }

            reader.close();

            // statzzz
            System.out.println("\nWORDS MISSPELLED:     " + misspellings);
            System.out.println("WORDS IN DICTIONARY:  " + size());
            System.out.println("WORDS IN TEXT:        " + words);
            System.out.println("TIME IN load:         " + timeLoad);
            System.out.println("TIME IN check:        " + timeCheck);
            System.out.println("TIME IN size:         " + timeSize);
            System.out.println("TIME IN unload:       " + timeUnload);
            System.out.println("TIME IN TOTAL:        " + (timeLoad + timeCheck + timeSize + timeUnload) + "\n");

        } catch (IOException e) {
            System.out.println("Error reading " + text + ".");
            e.printStackTrace();
            unload();
            System.exit(1);
        }

        startTime = System.currentTimeMillis();
        boolean unloaded = unload();
        endTime = System.currentTimeMillis();

        if (!unloaded) {
            System.out.println("Could not unload " + dictionary + ".");
            System.exit(1);
        }

        timeUnload = (endTime - startTime) / 1000.0;
    }

//
//
//    CORE METHODS
//
//

    private static boolean check(String word) {
        int hashed = hash(word);

        Node checker = table[hashed];
        while (checker != null) {
            if (checker.word.equalsIgnoreCase(word)) {
                return true;
            }
            checker = checker.next;
        }

        return false;
    }

    private static final int N = 78;
    private static int hash(String word) {
        int index = 0;

        for (int i = 0; i < 4; i++) {
            if (i >= word.length()) {
                break;
            }

            index <<= 8;
            index += Character.toLowerCase(word.charAt(i));
        }

        return index % N;
    }

    // hashmap / hashtable
    private static int counter = 0;
    private static Node[] table = new Node[N];
    private static boolean load(String dictionary) {
        try (BufferedReader reader = new BufferedReader(new FileReader(dictionary))) {
            String word;
            while ((word = reader.readLine()) != null) {
                Node newWord = new Node(word);
                int index = hash(word);

                newWord.next = table[index];
                table[index] = newWord;
                counter++;
            }

            return true;

        } catch (IOException e) {
            return false;
        }
    }

    private static int size() {
        return (counter > 0) ? counter : 0;
    }

    private static boolean unload() {
        for (int i = 0; i < N; i++) {
            Node freeThis = table[i];

            while (freeThis != null) {
                Node temp = freeThis;

                freeThis = freeThis.next;
                temp = null;
            }
        }
        return true;
    }
}
