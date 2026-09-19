package com.talentflow.algorithms;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Resume similarity engine.
 *
 * Combines:
 * 1. Exact skill overlap
 * 2. Fuzzy skill similarity using Edit Distance
 *
 * Returns a normalized score between 0 and 100.
 */
public final class ResumeSimilarity {

    private ResumeSimilarity() {
        // Utility class
    }

    /**
     * Calculates similarity between two skill sets.
     */
    public static double skillSimilarity(
            List<String> firstSkills,
            List<String> secondSkills) {

        if (firstSkills == null || secondSkills == null) {
            return 0.0;
        }

        if (firstSkills.isEmpty() && secondSkills.isEmpty()) {
            return 100.0;
        }

        Set<String> first =
                normalize(firstSkills);

        Set<String> second =
                normalize(secondSkills);

        if (first.isEmpty() || second.isEmpty()) {
            return 0.0;
        }

        int exactMatches = 0;

        for (String skill : first) {
            if (second.contains(skill)) {
                exactMatches++;
            }
        }

        /*
         * Jaccard similarity measures exact overlap.
         */
        Set<String> union =
                new HashSet<>(first);

        union.addAll(second);

        double exactSimilarity =
                union.isEmpty()
                        ? 0.0
                        : (double) exactMatches / union.size();

        /*
         * Fuzzy similarity allows small spelling differences.
         */
        double fuzzyTotal = 0.0;
        int comparisons = 0;

        for (String firstSkill : first) {

            double bestMatch = 0.0;

            for (String secondSkill : second) {

                double similarity =
                        EditDistance.similarity(
                                firstSkill,
                                secondSkill
                        );

                bestMatch =
                        Math.max(
                                bestMatch,
                                similarity
                        );
            }

            fuzzyTotal += bestMatch;
            comparisons++;
        }

        double fuzzySimilarity =
                comparisons == 0
                        ? 0.0
                        : fuzzyTotal / comparisons;

        /*
         * Exact matching receives more weight because
         * exact technical skills are stronger evidence.
         */
        double finalScore =
                (exactSimilarity * 0.60)
                        + (fuzzySimilarity * 0.40);

        return finalScore * 100.0;
    }

    /**
     * Calculates similarity between two pieces of resume text.
     *
     * Uses token-level matching rather than comparing
     * every character in the entire document.
     */
    public static double textSimilarity(
            String firstText,
            String secondText) {

        Set<String> first =
                tokenize(firstText);

        Set<String> second =
                tokenize(secondText);

        if (first.isEmpty() && second.isEmpty()) {
            return 100.0;
        }

        if (first.isEmpty() || second.isEmpty()) {
            return 0.0;
        }

        Set<String> intersection =
                new HashSet<>(first);

        intersection.retainAll(second);

        Set<String> union =
                new HashSet<>(first);

        union.addAll(second);

        if (union.isEmpty()) {
            return 0.0;
        }

        return ((double) intersection.size()
                / union.size()) * 100.0;
    }

    /**
     * Combines skill similarity and resume-text similarity.
     */
    public static double combinedSimilarity(
            List<String> firstSkills,
            List<String> secondSkills,
            String firstText,
            String secondText) {

        double skillScore =
                skillSimilarity(
                        firstSkills,
                        secondSkills
                );

        double textScore =
                textSimilarity(
                        firstText,
                        secondText
                );

        /*
         * Skills are more important for recruitment matching.
         */
        return (skillScore * 0.70)
                + (textScore * 0.30);
    }

    private static Set<String> normalize(
            List<String> skills) {

        Set<String> result =
                new HashSet<>();

        for (String skill : skills) {

            if (skill != null
                    && !skill.isBlank()) {

                result.add(
                        skill.trim().toLowerCase()
                );
            }
        }

        return result;
    }

    private static Set<String> tokenize(
            String text) {

        Set<String> tokens =
                new HashSet<>();

        if (text == null || text.isBlank()) {
            return tokens;
        }

        String normalized =
                text.toLowerCase()
                        .replaceAll(
                                "[^a-z0-9+#.]",
                                " "
                        );

        String[] words =
                normalized.split("\\s+");

        for (String word : words) {

            /*
             * Ignore extremely short tokens and
             * common noise words.
             */
            if (word.length() >= 3
                    && !isStopWord(word)) {

                tokens.add(word);
            }
        }

        return tokens;
    }

    private static boolean isStopWord(
            String word) {

        return word.equals("the")
                || word.equals("and")
                || word.equals("for")
                || word.equals("with")
                || word.equals("from")
                || word.equals("this")
                || word.equals("that")
                || word.equals("are")
                || word.equals("was")
                || word.equals("has")
                || word.equals("have")
                || word.equals("you")
                || word.equals("your")
                || word.equals("into");
    }
}