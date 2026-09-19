package com.talentflow.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;

import com.talentflow.models.Candidate;

/**
 * Common resume matching service used by candidate search
 * and candidate optimization.
 *
 * The recruiter sees only useful recruitment results.
 *
 * Internally this service uses dynamic-programming based
 * edit distance to tolerate small spelling mistakes.
 */
@Service
public class ResumeMatchingService {

    public MatchResult analyze(
            Candidate candidate,
            String query) {

        if (candidate == null
                || query == null
                || query.isBlank()) {

            return new MatchResult(
                    false,
                    0.0,
                    0,
                    List.of()
            );
        }

        String resumeText =
                candidate.getResumeText();

        if (resumeText == null
                || resumeText.isBlank()) {

            return new MatchResult(
                    false,
                    0.0,
                    0,
                    List.of()
            );
        }

        List<String> queryTerms =
                tokenize(query);

        if (queryTerms.isEmpty()) {

            return new MatchResult(
                    false,
                    0.0,
                    0,
                    List.of()
            );
        }

        List<String> resumeTokens =
                tokenize(resumeText);

        if (resumeTokens.isEmpty()) {

            return new MatchResult(
                    false,
                    0.0,
                    0,
                    List.of()
            );
        }

        /*
         * Remove duplicate search terms.
         *
         * Example:
         * "python sql sql"
         *
         * becomes:
         * "python", "sql"
         */
        queryTerms =
                new ArrayList<>(
                        new LinkedHashSet<>(
                                queryTerms
                        )
                );

        List<String> matchedTerms =
                new ArrayList<>();

        double totalScore = 0.0;
        int occurrences = 0;

        /*
         * Every search requirement must match.
         *
         * Example:
         *
         * python java
         *
         * A resume containing only Python
         * will not qualify.
         */
        for (String queryTerm : queryTerms) {

            TermMatch bestMatch =
                    findBestMatch(
                            queryTerm,
                            resumeTokens
                    );

            if (!bestMatch.matched()) {

                return new MatchResult(
                        false,
                        0.0,
                        0,
                        matchedTerms
                );
            }

            totalScore +=
                    bestMatch.score();

            occurrences +=
                    bestMatch.occurrences();

            matchedTerms.add(
                    queryTerm
            );
        }

        double score =
                totalScore
                        / queryTerms.size();

        return new MatchResult(
                true,
                round(score),
                occurrences,
                matchedTerms
        );
    }

    private TermMatch findBestMatch(
            String queryTerm,
            List<String> resumeTokens) {

        if (queryTerm == null
                || queryTerm.isBlank()) {

            return new TermMatch(
                    false,
                    0.0,
                    0
            );
        }

        int allowedDistance =
                allowedDistance(
                        queryTerm.length()
                );

        int bestDistance =
                Integer.MAX_VALUE;

        int occurrences = 0;

        for (String resumeToken :
                resumeTokens) {

            if (resumeToken == null
                    || resumeToken.isBlank()) {
                continue;
            }

            /*
             * Exact match.
             */
            if (resumeToken.equals(queryTerm)) {

                occurrences++;

                bestDistance = 0;

                continue;
            }

            /*
             * Prefix/containment is useful for
             * things such as:
             *
             * node -> node.js
             * react -> reactjs
             */
            if (resumeToken.contains(queryTerm)
                    || queryTerm.contains(
                            resumeToken
                    )) {

                int distance =
                        editDistance(
                                queryTerm,
                                resumeToken
                        );

                if (distance <=
                        allowedDistance
                                + 1) {

                    occurrences++;

                    bestDistance =
                            Math.min(
                                    bestDistance,
                                    distance
                            );
                }

                continue;
            }

            /*
             * Fuzzy matching.
             *
             * javq -> java
             *
             * edit distance = 1
             */
            int distance =
                    editDistance(
                            queryTerm,
                            resumeToken
                    );

            if (distance <=
                    allowedDistance) {

                occurrences++;

                bestDistance =
                        Math.min(
                                bestDistance,
                                distance
                        );
            }
        }

        if (bestDistance ==
                Integer.MAX_VALUE) {

            return new TermMatch(
                    false,
                    0.0,
                    0
            );
        }

        int denominator =
                Math.max(
                        queryTerm.length(),
                        queryTerm.length()
                                + bestDistance
                );

        double score =
                100.0
                        * (
                        1.0
                                - (
                                (double)
                                        bestDistance
                                        / denominator
                        )
                );

        return new TermMatch(
                true,
                score,
                occurrences
        );
    }

    /**
     * Allowed edit distance is intentionally conservative.
     *
     * One-letter searches such as "C" must remain precise.
     */
    private int allowedDistance(
            int length) {

        if (length <= 1) {
            return 0;
        }

        if (length <= 3) {
            return 1;
        }

        if (length <= 6) {
            return 1;
        }

        return 2;
    }

    /**
     * Wagner-Fischer / Levenshtein
     * dynamic programming.
     */
    private int editDistance(
            String first,
            String second) {

        if (first == null
                || second == null) {

            return Integer.MAX_VALUE;
        }

        int m = first.length();
        int n = second.length();

        if (m == 0) {
            return n;
        }

        if (n == 0) {
            return m;
        }

        int[] previous =
                new int[n + 1];

        int[] current =
                new int[n + 1];

        for (int j = 0;
             j <= n;
             j++) {

            previous[j] = j;
        }

        for (int i = 1;
             i <= m;
             i++) {

            current[0] = i;

            for (int j = 1;
                 j <= n;
                 j++) {

                int substitutionCost =
                        first.charAt(i - 1)
                                == second.charAt(j - 1)
                                ? 0
                                : 1;

                current[j] =
                        Math.min(
                                Math.min(
                                        current[j - 1] + 1,
                                        previous[j] + 1
                                ),
                                previous[j - 1]
                                        + substitutionCost
                        );
            }

            int[] temporary =
                    previous;

            previous =
                    current;

            current =
                    temporary;
        }

        return previous[n];
    }

    private List<String> tokenize(
            String text) {

        if (text == null
                || text.isBlank()) {

            return Collections.emptyList();
        }

        String normalized =
                text.toLowerCase(
                        Locale.ROOT
                )
                .replaceAll(
                        "[^a-z0-9+#.]+",
                        " "
                )
                .trim();

        if (normalized.isBlank()) {
            return Collections.emptyList();
        }

        return Arrays.asList(
                normalized.split("\\s+")
        );
    }

    private double round(
            double value) {

        return Math.round(
                value * 100.0
        ) / 100.0;
    }

    public static class MatchResult {

        private final boolean matched;
        private final double score;
        private final int occurrences;
        private final List<String> matchedTerms;

        public MatchResult(
                boolean matched,
                double score,
                int occurrences,
                List<String> matchedTerms) {

            this.matched = matched;
            this.score = score;
            this.occurrences = occurrences;

            this.matchedTerms =
                    matchedTerms != null
                            ? new ArrayList<>(
                                    matchedTerms
                            )
                            : new ArrayList<>();
        }

        public boolean isMatched() {
            return matched;
        }

        public double getScore() {
            return score;
        }

        public int getOccurrences() {
            return occurrences;
        }

        public List<String> getMatchedTerms() {
            return new ArrayList<>(
                    matchedTerms
            );
        }
    }

    private record TermMatch(
            boolean matched,
            double score,
            int occurrences) {
    }
}