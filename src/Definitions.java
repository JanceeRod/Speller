public class Definitions {
    public static final String DICTIONARY = "dictionaries/large";
    public static final String CONSTITUTION = "texts/constitution.txt";
    public static final String LALALAND = "texts/lalaland.txt";
    public static final String SHAKESPEARE = "texts/shakespeare.txt";


    public static double timeLoad = 0.0;
    public static double timeCheck = 0.0;
    public static final double timeSize = 0.0;
    public static double timeUnload = 0.0;


    static int misspellings = 0, words = 0;
    static long startTime, endTime;

    static String dictionary = DICTIONARY;
}
