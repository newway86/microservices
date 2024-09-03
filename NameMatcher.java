import java.util.Scanner;

public class NameMatcher {

    // Soundex encoding algorithm
    public static String soundex(String name) {
        char[] nameChars = name.toUpperCase().toCharArray();
        char firstChar = nameChars[0];
        StringBuilder encoded = new StringBuilder().append(firstChar);

        int lastCode = getSoundexCode(firstChar);

        for (int i = 1; i < nameChars.length; i++) {
            char currentChar = nameChars[i];
            int currentCode = getSoundexCode(currentChar);

            if (currentCode != 0 && currentCode != lastCode) {
                encoded.append(currentCode);
            }
            lastCode = currentCode;
        }

        // Pad with zeros or truncate to make sure the result is exactly 4 characters
        while (encoded.length() < 4) {
            encoded.append('0');
        }
        return encoded.substring(0, 4);
    }

    private static int getSoundexCode(char c) {
        switch (c) {
            case 'B': case 'F': case 'P': case 'V': return 1;
            case 'C': case 'G': case 'J': case 'K': case 'Q': case 'S': case 'X': case 'Z': return 2;
            case 'D': case 'T': return 3;
            case 'L': return 4;
            case 'M': case 'N': return 5;
            case 'R': return 6;
            default: return 0;
        }
    }

    // Calculate Levenshtein distance
    public static int getLevenshteinDistance(String s1, String s2) {
        int len1 = s1.length();
        int len2 = s2.length();
        int[][] dp = new int[len1 + 1][len2 + 1];

        for (int i = 0; i <= len1; i++) {
            for (int j = 0; j <= len2; j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;
                    dp[i][j] = Math.min(Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1), dp[i - 1][j - 1] + cost);
                }
            }
        }
        return dp[len1][len2];
    }

    // Calculate similarity percentage
    public static double getSimilarityPercentage(String s1, String s2) {
        int distance = getLevenshteinDistance(s1, s2);
        int maxLen = Math.max(s1.length(), s2.length());
        return (1.0 - (double) distance / maxLen) * 100;
    }

    // Determine match type
    public static String getMatchType(String s1, String s2) {
        // Directly check for full match
        if (s1.equalsIgnoreCase(s2)) {
            return "Full Match";
        }

        // Use Levenshtein distance for partial match
        double similarityPercentage = getSimilarityPercentage(s1, s2);
        if (similarityPercentage >= 80) { // Adjusted threshold for partial match
            return "Partial Match";
        }

        // Check Soundex for phonetic similarity
        if (soundex(s1).equals(soundex(s2))) {
            return "Partial Match";
        }

        return "No Match";
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Input names
        System.out.println("Enter the first name:");
        String name1 = scanner.nextLine();
        System.out.println("Enter the second name:");
        String name2 = scanner.nextLine();

        // Determine and print the match type
        String matchType = getMatchType(name1, name2);
        System.out.println("Match Type: " + matchType);

        scanner.close();
    }
}
