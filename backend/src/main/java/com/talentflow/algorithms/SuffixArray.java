package com.talentflow.algorithms;

import java.util.Arrays;

/**
 * Suffix Array and LCP (Kasai) implementation.
 *
 * TalentFlow uses suffix structures to analyze repeated
 * phrases and shared sections between resume documents.
 *
 * Suffix Array:
 * O(n log n log n) using prefix-doubling with sorting.
 *
 * LCP using Kasai:
 * O(n)
 *
 * Space Complexity:
 * O(n)
 */
public final class SuffixArray {

    private SuffixArray() {
        // Utility class
    }

    /**
     * Builds the suffix array of a string.
     *
     * Each returned value represents the starting position
     * of a suffix, ordered lexicographically.
     */
    public static int[] build(String text) {

        if (text == null || text.isEmpty()) {
            return new int[0];
        }

        int n = text.length();

        Integer[] suffixes = new Integer[n];

        int[] rank = new int[n];
        int[] nextRank = new int[n];

        for (int i = 0; i < n; i++) {
            suffixes[i] = i;
            rank[i] = text.charAt(i);
        }

        for (int k = 1; k < n; k *= 2) {

            /*
             * Java lambdas require captured variables to be
             * final or effectively final.
             *
             * rank is reassigned later, so create a final
             * snapshot for this sorting iteration.
             */
            final int[] currentRank = rank;
            final int length = k;

            Arrays.sort(
                    suffixes,
                    (a, b) -> {

                        if (currentRank[a]
                                != currentRank[b]) {

                            return Integer.compare(
                                    currentRank[a],
                                    currentRank[b]
                            );
                        }

                        int rankA =
                                a + length < n
                                        ? currentRank[a + length]
                                        : -1;

                        int rankB =
                                b + length < n
                                        ? currentRank[b + length]
                                        : -1;

                        return Integer.compare(
                                rankA,
                                rankB
                        );
                    }
            );

            nextRank[suffixes[0]] = 0;

            for (int i = 1; i < n; i++) {

                int previous =
                        suffixes[i - 1];

                int current =
                        suffixes[i];

                boolean different =
                        currentRank[previous]
                                != currentRank[current];

                if (!different) {

                    int previousNext =
                            previous + length < n
                                    ? currentRank[
                                            previous + length]
                                    : -1;

                    int currentNext =
                            current + length < n
                                    ? currentRank[
                                            current + length]
                                    : -1;

                    different =
                            previousNext
                                    != currentNext;
                }

                nextRank[current] =
                        nextRank[previous]
                                + (different ? 1 : 0);
            }

            /*
             * Swap rank arrays.
             */
            int[] temporary = rank;
            rank = nextRank;
            nextRank = temporary;

            /*
             * All suffixes have unique ranks.
             */
            if (rank[suffixes[n - 1]] == n - 1) {
                break;
            }

            /*
             * Prevent integer overflow when doubling k.
             */
            if (k > n / 2) {
                break;
            }
        }

        int[] result = new int[n];

        for (int i = 0; i < n; i++) {
            result[i] = suffixes[i];
        }

        return result;
    }

    /**
     * Builds the LCP array using Kasai's algorithm.
     *
     * lcp[i] is the longest common prefix between:
     *
     * suffixArray[i]
     * and
     * suffixArray[i - 1]
     *
     * lcp[0] = 0.
     */
    public static int[] buildLCP(
            String text,
            int[] suffixArray) {

        if (text == null
                || text.isEmpty()
                || suffixArray == null
                || suffixArray.length == 0) {

            return new int[0];
        }

        int n = text.length();

        int[] rank = new int[n];

        for (int i = 0; i < n; i++) {
            rank[suffixArray[i]] = i;
        }

        int[] lcp = new int[n];

        int matched = 0;

        for (int i = 0; i < n; i++) {

            int suffixRank = rank[i];

            if (suffixRank == 0) {
                matched = 0;
                continue;
            }

            int previousSuffix =
                    suffixArray[suffixRank - 1];

            while (i + matched < n
                    && previousSuffix + matched < n
                    && text.charAt(i + matched)
                    == text.charAt(
                            previousSuffix + matched)) {

                matched++;
            }

            lcp[suffixRank] = matched;

            if (matched > 0) {
                matched--;
            }
        }

        return lcp;
    }

    /**
     * Finds the longest common substring length between
     * two strings.
     */
    public static int longestCommonSubstringLength(
            String first,
            String second) {

        if (first == null
                || second == null
                || first.isEmpty()
                || second.isEmpty()) {

            return 0;
        }

        String combined =
                first
                        + '\u0000'
                        + second;

        int separator =
                first.length();

        int[] suffixArray =
                build(combined);

        int[] lcp =
                buildLCP(
                        combined,
                        suffixArray
                );

        int best = 0;

        for (int i = 1;
             i < suffixArray.length;
             i++) {

            int left =
                    suffixArray[i - 1];

            int right =
                    suffixArray[i];

            boolean leftFromFirst =
                    left < separator;

            boolean rightFromFirst =
                    right < separator;

            /*
             * Only compare suffixes belonging to
             * different documents.
             */
            if (leftFromFirst
                    != rightFromFirst) {

                best =
                        Math.max(
                                best,
                                lcp[i]
                        );
            }
        }

        return best;
    }
}