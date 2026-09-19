package com.talentflow.algorithms;

/**
 * Rabin-Karp string matching algorithm.
 *
 * TalentFlow uses rolling hashes to quickly search resume
 * text for exact keywords and patterns.
 *
 * Time Complexity:
 * - Average: O(n + m)
 * - Worst case: O(n * m)
 *
 * Space Complexity: O(1)
 *
 * n = text length
 * m = pattern length
 */
public final class RabinKarp {

    private static final long BASE = 256;
    private static final long MOD = 1_000_000_007L;

    private RabinKarp() {
        // Utility class
    }

    /**
     * Finds the first occurrence of pattern in text.
     *
     * @return starting index, or -1 if not found
     */
    public static int search(String text, String pattern) {

        if (text == null || pattern == null) {
            return -1;
        }

        if (pattern.isEmpty()) {
            return 0;
        }

        if (text.isEmpty()
                || pattern.length() > text.length()) {
            return -1;
        }

        int patternLength = pattern.length();

        long patternHash = 0;
        long windowHash = 0;
        long highestPower = 1;

        /*
         * highestPower becomes BASE^(m-1).
         */
        for (int i = 0; i < patternLength - 1; i++) {
            highestPower =
                    (highestPower * BASE) % MOD;
        }

        /*
         * Calculate initial hashes.
         */
        for (int i = 0; i < patternLength; i++) {

            patternHash =
                    (patternHash * BASE
                            + pattern.charAt(i))
                            % MOD;

            windowHash =
                    (windowHash * BASE
                            + text.charAt(i))
                            % MOD;
        }

        /*
         * Slide the pattern window through the text.
         */
        for (int i = 0;
             i <= text.length() - patternLength;
             i++) {

            /*
             * Hash match is only a candidate match.
             * Verify characters to avoid hash collisions.
             */
            if (patternHash == windowHash
                    && matchesAt(
                            text,
                            pattern,
                            i)) {

                return i;
            }

            /*
             * Remove the leftmost character and add
             * the next character to the rolling hash.
             */
            if (i < text.length() - patternLength) {

                long removed =
                        (text.charAt(i)
                                * highestPower)
                                % MOD;

                windowHash =
                        (windowHash - removed + MOD)
                                % MOD;

                windowHash =
                        (windowHash * BASE
                                + text.charAt(
                                        i + patternLength))
                                % MOD;
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
     * Counts exact occurrences.
     *
     * Overlapping occurrences are included.
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

        int patternLength = pattern.length();

        long patternHash = 0;
        long windowHash = 0;
        long highestPower = 1;

        for (int i = 0; i < patternLength - 1; i++) {
            highestPower =
                    (highestPower * BASE) % MOD;
        }

        for (int i = 0; i < patternLength; i++) {

            patternHash =
                    (patternHash * BASE
                            + pattern.charAt(i))
                            % MOD;

            windowHash =
                    (windowHash * BASE
                            + text.charAt(i))
                            % MOD;
        }

        int count = 0;

        for (int i = 0;
             i <= text.length() - patternLength;
             i++) {

            if (patternHash == windowHash
                    && matchesAt(
                            text,
                            pattern,
                            i)) {

                count++;
            }

            if (i < text.length() - patternLength) {

                long removed =
                        (text.charAt(i)
                                * highestPower)
                                % MOD;

                windowHash =
                        (windowHash - removed + MOD)
                                % MOD;

                windowHash =
                        (windowHash * BASE
                                + text.charAt(
                                        i + patternLength))
                                % MOD;
            }
        }

        return count;
    }

    /**
     * Verifies a possible hash match character-by-character.
     */
    private static boolean matchesAt(
            String text,
            String pattern,
            int startIndex) {

        for (int j = 0;
             j < pattern.length();
             j++) {

            if (text.charAt(startIndex + j)
                    != pattern.charAt(j)) {

                return false;
            }
        }

        return true;
    }
}