package com.talentflow.algorithms;

/**
 * Z-Function string matching algorithm.
 *
 * TalentFlow uses Z-Function as an alternative exact
 * pattern-search strategy for resume text.
 *
 * Time Complexity: O(n + m)
 * Space Complexity: O(n + m)
 *
 * where:
 * n = text length
 * m = pattern length
 */
public final class ZFunction {

    private ZFunction() {
        // Utility class
    }

    /**
     * Finds the first occurrence of pattern in text.
     *
     * @return starting index, or -1 when not found
     */
    public static int search(String text, String pattern) {

        if (text == null || pattern == null) {
            return -1;
        }

        if (pattern.isEmpty()) {
            return 0;
        }

        if (text.isEmpty()) {
            return -1;
        }

        String combined =
                pattern + '\u0000' + text;

        int[] z = buildZArray(combined);

        int patternLength = pattern.length();

        for (int i = patternLength + 1;
             i < combined.length();
             i++) {

            if (z[i] == patternLength) {
                return i - patternLength - 1;
            }
        }

        return -1;
    }

    /**
     * Checks whether pattern occurs in text.
     */
    public static boolean contains(
            String text,
            String pattern) {

        return search(text, pattern) != -1;
    }

    /**
     * Builds the Z-array.
     *
     * z[i] represents the length of the longest substring
     * starting at i that matches the prefix of the string.
     */
    public static int[] buildZArray(String text) {

        if (text == null || text.isEmpty()) {
            return new int[0];
        }

        int n = text.length();

        int[] z = new int[n];

        int left = 0;
        int right = 0;

        for (int i = 1; i < n; i++) {

            if (i <= right) {
                z[i] =
                        Math.min(
                                right - i + 1,
                                z[i - left]
                        );
            }

            while (i + z[i] < n
                    && text.charAt(z[i])
                    == text.charAt(i + z[i])) {

                z[i]++;
            }

            if (i + z[i] - 1 > right) {

                left = i;
                right = i + z[i] - 1;
            }
        }

        return z;
    }
}