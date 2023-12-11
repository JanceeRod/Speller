import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Definitions {
    public static final String DICTIONARY = "dictionaries/words_alpha.txt";
    public static final String US_CONSTITUTION = "texts/constitution.txt";
    public static final String LALALAND = "texts/lalaland.txt";
    public static final String SHAKESPEARE = "texts/shakespeare.txt";
    public static final String ESSAY = "texts/essay.txt";
    public static final String TEST = "texts/test.txt";


    public static double timeLoad = 0.0;
    public static double timeCheck = 0.0;
    public static final double timeSize = 0.0;
    public static double timeUnload = 0.0;
    public static double timeSort = 0.0;


    static int misspellings = 0, words = 0;
    static long startTime, endTime;

    static String dictionary = DICTIONARY;


    public static Scanner input = new Scanner(System.in);
    public static String textFile = "";
    public static ArrayList<String> misspelledWords = new ArrayList<>();
    public static Map<String, String> correctedWords = new HashMap<>();
}
