package com.talentflow.algorithms;

/**
 * Dynamic-programming utilities used by TalentFlow for
 * fuzzy text comparison and sequence-based matching.
 */
public final class EditDistance {

    private EditDistance() {
    }

    /**
     * Standard Wagner-Fischer edit distance.
     */
    public static int distance(
            String first,
            String second) {

        if (first == null) {
            first = "";
        }

        if (second == null) {
            second = "";
        }

        int n = first.length();
        int m = second.length();

        int[] previous = new int[m + 1];
        int[] current = new int[m + 1];

        for (int j = 0; j <= m; j++) {
            previous[j] = j;
        }

        for (int i = 1; i <= n; i++) {

            current[0] = i;

            for (int j = 1; j <= m; j++) {

                int insertion =
                        current[j - 1] + 1;

                int deletion =
                        previous[j] + 1;

                int substitution =
                        previous[j - 1]
                                + (
                                Character.toLowerCase(
                                        first.charAt(i - 1)
                                )
                                        ==
                                        Character.toLowerCase(
                                                second.charAt(j - 1)
                                        )
                                        ? 0
                                        : 1
                        );

                current[j] =
                        Math.min(
                                insertion,
                                Math.min(
                                        deletion,
                                        substitution
                                )
                        );
            }

            int[] swap = previous;
            previous = current;
            current = swap;
        }

        return previous[m];
    }

    /**
     * Returns similarity as a percentage.
     */
    public static double similarity(
            String first,
            String second) {

        if (first == null) {
            first = "";
        }

        if (second == null) {
            second = "";
        }

        if (first.isEmpty()
                && second.isEmpty()) {
            return 100.0;
        }

        int distance =
                distance(first, second);

        int maximum =
                Math.max(
                        first.length(),
                        second.length()
                );

        if (maximum == 0) {
            return 100.0;
        }

        return (
                1.0
                        - (
                        (double) distance
                                / maximum
                )
        ) * 100.0;
    }

    /**
     * Longest common subsequence length.
     *
     * This provides another dynamic-programming operation
     * useful for comparing resume text sequences.
     */
    public static int longestCommonSubsequence(
            String first,
            String second) {

        if (first == null) {
            first = "";
        }

        if (second == null) {
            second = "";
        }

        int n = first.length();
        int m = second.length();

        int[] previous =
                new int[m + 1];

        int[] current =
                new int[m + 1];

        for (int i = 1; i <= n; i++) {

            for (int j = 1; j <= m; j++) {

                if (Character.toLowerCase(
                        first.charAt(i - 1)
                ) == Character.toLowerCase(
                        second.charAt(j - 1)
                )) {

                    current[j] =
                            previous[j - 1] + 1;

                } else {

                    current[j] =
                            Math.max(
                                    previous[j],
                                    current[j - 1]
                            );
                }
            }

            int[] swap =
                    previous;

            previous =
                    current;

            current =
                    swap;
        }

        return previous[m];
    }

    /**
     * Calculates sequence similarity using LCS.
     */
    public static double sequenceSimilarity(
            String first,
            String second) {

        if (first == null) {
            first = "";
        }

        if (second == null) {
            second = "";
        }

        int lcs =
                longestCommonSubsequence(
                        first,
                        second
                );

        int maximum =
                Math.max(
                        first.length(),
                        second.length()
                );

        if (maximum == 0) {
            return 100.0;
        }

        return (
                (double) lcs
                        / maximum
        ) * 100.0;
    }
}
