import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Speller {

    public static final int N = 350000;
    public static boolean check(String word) {
        int hashed = hash(word);

        // go thru that index and look for the word
        Node checker = table[hashed];
        while (checker != null) {
            if (checker.word.equalsIgnoreCase(word)) {
                return true;
            }
            checker = checker.next;
        }
        return false;
    }

    public static int hash(String word) {
        int primeMultiplier = 31;
        int index = 0;

        for (int i = 0; i < word.length(); i++) {
            index = (index * primeMultiplier) + Character.toLowerCase(word.charAt(i));
        }

        return Math.abs(index) % N;
    }

    public static int counter = 0;
    public static Node[] table = new Node[N];
    public static boolean load(String dictionary) {
        try (BufferedReader reader = new BufferedReader(new FileReader(dictionary))) {
            String word;
            while ((word = reader.readLine()) != null) {
                Node newWord = new Node(word);
                int index = hash(word);

                // add word to table[index] and make it as the start of the linked list
                newWord.next = table[index];
                table[index] = newWord;
                counter++;
            }

            return true;

        } catch (IOException e) {
            return false;
        }
    }

    public static boolean unload() {
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

    public static int size() {
        return (counter > 0) ? counter : 0;
    }
}
