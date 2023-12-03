import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Main {
    private static final String DICTIONARY = "dictionaries/large";
    private static double timeLoad = 0.0;
    private static double timeCheck = 0.0;
    private static final double timeSize = 0.0;
    private static double timeUnload = 0.0;

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
                        case 1 -> text = "texts/constitution.txt";
                        case 2 -> text = "texts/lalaland.txt";
                        case 3 -> text = "texts/shakespeare.txt";
                    }
                    break;
                } else {
                    System.out.println("Invalid. Expected input: A number within 1 to 3.");
                }
            } else {
                System.out.println("Invalid Input. Expected Input: A number within 1 to 3");
                input.next();
            }

            System.out.println("\nEnter your choice: ");
        }

        long startTime, endTime;
        String dictionary = DICTIONARY;

        startTime = System.currentTimeMillis();
        boolean loaded = Speller.load(dictionary);
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

            reader.close();

            // statzzz
            System.out.println("\nWORDS MISSPELLED:     " + misspellings);
            System.out.println("WORDS IN DICTIONARY:  " + Speller.size());
            System.out.println("WORDS IN TEXT:        " + words);
            System.out.println("TIME IN load:         " + timeLoad);
            System.out.println("TIME IN check:        " + timeCheck);
            System.out.println("TIME IN size:         " + timeSize);
            System.out.println("TIME IN unload:       " + timeUnload);
            System.out.println("TIME IN TOTAL:        " + (timeLoad + timeCheck + timeSize + timeUnload) + "\n");

        } catch (IOException e) {
            System.out.println("Error reading " + text + ".");
            e.printStackTrace();
            Speller.unload();
            System.exit(1);
        }

        startTime = System.currentTimeMillis();
        boolean unloaded = Speller.unload();
        endTime = System.currentTimeMillis();

        if (!unloaded) {
            System.out.println("Could not unload " + dictionary + ".");
            System.exit(1);
        }

        timeUnload = (endTime - startTime) / 1000.0;
    }
}
