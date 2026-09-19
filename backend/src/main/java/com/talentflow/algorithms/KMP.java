package com.talentflow.algorithms;

/**
 * Knuth-Morris-Pratt (KMP) string matching algorithm.
 *
 * TalentFlow uses KMP for exact keyword/pattern searching
 * inside extracted resume text.
 *
 * Time Complexity:
 * - Prefix table construction: O(m)
 * - Pattern search: O(n)
 *
 * Space Complexity:
 * - O(m)
 *
 * where:
 * n = length of text
 * m = length of pattern
 */
public final class KMP {

    private KMP() {
        // Utility class
    }

    /**
     * Searches for the first occurrence of a pattern
     * inside the given text.
     *
     * @param text text to search
     * @param pattern pattern to find
     * @return starting index of the pattern, or -1 if not found
     */
    public static int search(String text, String pattern) {

        if (text == null || pattern == null) {
            return -1;
        }

        if (pattern.isEmpty()) {
            return 0;
        }

        if (text.isEmpty() || pattern.length() > text.length()) {
            return -1;
        }

        int[] lps = buildLPS(pattern);

        int textIndex = 0;
        int patternIndex = 0;

        while (textIndex < text.length()) {

            if (text.charAt(textIndex)
                    == pattern.charAt(patternIndex)) {

                textIndex++;
                patternIndex++;

                if (patternIndex == pattern.length()) {
                    return textIndex - patternIndex;
                }

            } else if (patternIndex > 0) {

                patternIndex =
                        lps[patternIndex - 1];

            } else {

                textIndex++;
            }
        }

        return -1;
    }

    /**
     * Checks whether a pattern occurs inside the text.
     */
    public static boolean contains(
            String text,
            String pattern) {

        return search(text, pattern) != -1;
    }

    /**
     * Counts the number of occurrences of a pattern.
     *
     * Overlapping occurrences are counted.
     *
     * Example:
     * text    = "AAAA"
     * pattern = "AA"
     *
     * occurrences = 3
     */
    public static int countOccurrences(
            String text,
            String pattern) {

        if (text == null
                || pattern == null
                || pattern.isEmpty()
                || text.isEmpty()
                || pattern.length() > text.length()) {

            return 0;
        }

        int[] lps = buildLPS(pattern);

        int textIndex = 0;
        int patternIndex = 0;
        int count = 0;

        while (textIndex < text.length()) {

            if (text.charAt(textIndex)
                    == pattern.charAt(patternIndex)) {

                textIndex++;
                patternIndex++;

                if (patternIndex == pattern.length()) {

                    count++;

                    /*
                     * Continue from the longest possible
                     * overlapping prefix.
                     */
                    patternIndex =
                            lps[patternIndex - 1];
                }

            } else if (patternIndex > 0) {

                patternIndex =
                        lps[patternIndex - 1];

            } else {

                textIndex++;
            }
        }

        return count;
    }

    /**
     * Builds the Longest Prefix Suffix (LPS) table.
     *
     * lps[i] stores the length of the longest proper prefix
     * of pattern[0...i] that is also a suffix.
     */
    public static int[] buildLPS(String pattern) {

        if (pattern == null || pattern.isEmpty()) {
            return new int[0];
        }

        int[] lps =
                new int[pattern.length()];

        int prefixLength = 0;
        int index = 1;

        while (index < pattern.length()) {

            if (pattern.charAt(index)
                    == pattern.charAt(prefixLength)) {

                prefixLength++;
                lps[index] = prefixLength;
                index++;

            } else if (prefixLength > 0) {

                prefixLength =
                        lps[prefixLength - 1];

            } else {

                lps[index] = 0;
                index++;
            }
        }

        return lps;
    }
}