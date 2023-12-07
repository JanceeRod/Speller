import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Levenshtein {
    public static List<String> getSuggestions(String misspelledWord, String dictionaryFile, int maxSuggestions) {
        List<String> suggestions = new ArrayList<>();

        try (Scanner scanner = new Scanner(Paths.get(dictionaryFile))) {
            while (scanner.hasNext()) {
                String word = scanner.next().replaceAll("[^a-zA-Z]", "");
                if (!word.isEmpty()) {
                    int distance = levenshteinDistance(misspelledWord, word);

                    // Add the word to suggestions if it is within an acceptable edit distance
                    if (distance <= 3) {  // Adjust the threshold as needed
                        suggestions.add(word);
                    }

                    // Sort the suggestions by edit distance (closer words first)
                    suggestions.sort(Comparator.comparingInt(w -> levenshteinDistance(misspelledWord, w)));

                    // Limit the list to maxSuggestions number of suggestions
                    if (suggestions.size() >= maxSuggestions) {
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return suggestions;
    }

    private static int levenshteinDistance(String word1, String word2) {
        int m = word1.length();
        int n = word2.length();

        int[][] dp = new int[m + 1][n + 1];

        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= n; j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    dp[i][j] = min(
                            dp[i - 1][j] + 1,
                            dp[i][j - 1] + 1,
                            dp[i - 1][j - 1] + (word1.charAt(i - 1) == word2.charAt(j - 1) ? 0 : 1)
                    );
                }
            }
        }
        return dp[m][n];
    }

    private static int min(int a, int b, int c) {
        return Math.min(Math.min(a, b), c);
    }
}
