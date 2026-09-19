package com.talentflow.algorithms;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.talentflow.models.Candidate;

/**
 * Candidate selection and optimization utilities.
 *
 * TalentFlow uses a greedy approximation strategy to select
 * a strong candidate set when the recruiter places a limit
 * on the number of candidates to advance.
 *
 * The scoring combines:
 * - skill coverage
 * - experience
 * - resume relevance
 *
 * The implementation is intentionally independent of the UI.
 */
public final class CandidateOptimization {

    private CandidateOptimization() {
    }

    public static class RankedCandidate {

        private final Candidate candidate;
        private final double score;

        public RankedCandidate(
                Candidate candidate,
                double score) {

            this.candidate = candidate;
            this.score = score;
        }

        public Candidate getCandidate() {
            return candidate;
        }

        public double getScore() {
            return score;
        }
    }

    /**
     * Selects up to maxCandidates using a greedy approximation.
     */
    public static List<RankedCandidate> selectTopCandidates(
            List<Candidate> candidates,
            String query,
            int maxCandidates) {

        List<RankedCandidate> ranked =
                new ArrayList<>();

        if (candidates == null
                || candidates.isEmpty()
                || maxCandidates <= 0) {

            return ranked;
        }

        String normalizedQuery =
                query == null
                        ? ""
                        : query.trim().toLowerCase();

        for (Candidate candidate :
                candidates) {

            if (candidate == null) {
                continue;
            }

            double score =
                    calculateScore(
                            candidate,
                            normalizedQuery
                    );

            ranked.add(
                    new RankedCandidate(
                            candidate,
                            score
                    )
            );
        }

        ranked.sort(
                Comparator.comparingDouble(
                        RankedCandidate::getScore
                ).reversed()
        );

        if (ranked.size() > maxCandidates) {

            return new ArrayList<>(
                    ranked.subList(
                            0,
                            maxCandidates
                    )
            );
        }

        return ranked;
    }

    /**
     * Calculates a normalized recruitment score.
     */
    public static double calculateScore(
            Candidate candidate,
            String query) {

        if (candidate == null) {
            return 0.0;
        }

        double skillScore =
                calculateSkillScore(
                        candidate,
                        query
                );

        double experienceScore =
                calculateExperienceScore(
                        candidate
                );

        double relevanceScore =
                calculateTextRelevance(
                        candidate,
                        query
                );

        /*
         * Weighted score:
         *
         * Skills      -> 50%
         * Experience -> 25%
         * Relevance   -> 25%
         */

        return round(
                (
                        skillScore * 0.50
                )
                        + (
                        experienceScore * 0.25
                )
                        + (
                        relevanceScore * 0.25
                )
        );
    }

    private static double calculateSkillScore(
            Candidate candidate,
            String query) {

        if (query == null
                || query.isBlank()) {

            return candidate.getSkills() == null
                    ? 0.0
                    : Math.min(
                    candidate.getSkills().size()
                            * 10.0,
                    100.0
            );
        }

        Set<String> queryTerms =
                tokenize(query);

        if (queryTerms.isEmpty()) {
            return 0.0;
        }

        Set<String> candidateSkills =
                new HashSet<>();

        if (candidate.getSkills() != null) {

            for (String skill :
                    candidate.getSkills()) {

                if (skill != null
                        && !skill.isBlank()) {

                    candidateSkills.add(
                            skill.toLowerCase()
                    );
                }
            }
        }

        int matches = 0;

        for (String term :
                queryTerms) {

            for (String skill :
                    candidateSkills) {

                if (skill.contains(term)
                        || term.contains(skill)) {

                    matches++;
                    break;
                }
            }
        }

        return (
                (double) matches
                        / queryTerms.size()
        ) * 100.0;
    }

    private static double calculateExperienceScore(
            Candidate candidate) {

        double experience =
                Math.max(
                        candidate.getExperience(),
                        0.0
                );

        /*
         * Ten or more years reaches the maximum
         * experience component.
         */

        return Math.min(
                experience * 10.0,
                100.0
        );
    }

    private static double calculateTextRelevance(
            Candidate candidate,
            String query) {

        if (query == null
                || query.isBlank()) {

            return 0.0;
        }

        String text =
                candidate.getResumeText();

        if (text == null
                || text.isBlank()) {

            return 0.0;
        }

        String lowerText =
                text.toLowerCase();

        String[] terms =
                query.split("\\s+");

        if (terms.length == 0) {
            return 0.0;
        }

        int matchedTerms = 0;

        for (String term : terms) {

            if (!term.isBlank()
                    && lowerText.contains(term)) {

                matchedTerms++;
            }
        }

        return (
                (double) matchedTerms
                        / terms.length
        ) * 100.0;
    }

    private static Set<String> tokenize(
            String text) {

        Set<String> tokens =
                new HashSet<>();

        if (text == null) {
            return tokens;
        }

        for (String token :
                text.toLowerCase()
                        .split("[^a-z0-9+#.]+")) {

            if (!token.isBlank()) {
                tokens.add(token);
            }
        }

        return tokens;
    }

    private static double round(
            double value) {

        return Math.round(
                value * 100.0
        ) / 100.0;
    }
}
