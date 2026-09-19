package com.talentflow.scheduling;

import java.util.ArrayList;
import java.util.List;

import com.talentflow.algorithms.BipartiteMatching;

/**
 * TalentFlow interview scheduling engine.
 *
 * Converts candidate/interviewer skill information into
 * an eligibility graph and uses maximum bipartite matching
 * to produce assignments.
 */
public final class InterviewScheduler {

    private InterviewScheduler() {
        // Utility class
    }

    /**
     * Represents an interviewer available for assignment.
     */
    public static class Interviewer {

        private final String name;
        private final List<String> skills;

        public Interviewer(
                String name,
                List<String> skills) {

            this.name = name;
            this.skills = skills != null
                    ? new ArrayList<>(skills)
                    : new ArrayList<>();
        }

        public String getName() {
            return name;
        }

        public List<String> getSkills() {
            return new ArrayList<>(skills);
        }
    }

    /**
     * Represents a candidate waiting for an interview.
     */
    public static class CandidateRequest {

        private final String name;
        private final List<String> skills;

        public CandidateRequest(
                String name,
                List<String> skills) {

            this.name = name;
            this.skills = skills != null
                    ? new ArrayList<>(skills)
                    : new ArrayList<>();
        }

        public String getName() {
            return name;
        }

        public List<String> getSkills() {
            return new ArrayList<>(skills);
        }
    }

    /**
     * Represents the final scheduling result.
     */
    public static class ScheduledInterview {

        private final String candidate;
        private final String interviewer;

        public ScheduledInterview(
                String candidate,
                String interviewer) {

            this.candidate = candidate;
            this.interviewer = interviewer;
        }

        public String getCandidate() {
            return candidate;
        }

        public String getInterviewer() {
            return interviewer;
        }
    }

    /**
     * Automatically assigns candidates to interviewers.
     *
     * A candidate is connected to an interviewer when
     * they share at least one technical skill.
     */
    public static List<ScheduledInterview> schedule(
            List<CandidateRequest> candidates,
            List<Interviewer> interviewers) {

        List<ScheduledInterview> result =
                new ArrayList<>();

        if (candidates == null
                || interviewers == null
                || candidates.isEmpty()
                || interviewers.isEmpty()) {

            return result;
        }

        /*
         * Extract skill lists for the matching algorithm.
         */
        List<List<String>> candidateSkills =
                new ArrayList<>();

        for (CandidateRequest candidate :
                candidates) {

            candidateSkills.add(
                    candidate.getSkills()
            );
        }

        List<List<String>> interviewerSkills =
                new ArrayList<>();

        for (Interviewer interviewer :
                interviewers) {

            interviewerSkills.add(
                    interviewer.getSkills()
            );
        }

        /*
         * Build the bipartite eligibility graph.
         */
        List<List<Integer>> graph =
                BipartiteMatching.buildEligibilityGraph(
                        candidateSkills,
                        interviewerSkills
                );

        /*
         * Find the maximum number of valid assignments.
         */
        List<BipartiteMatching.Assignment> assignments =
                BipartiteMatching.maximumMatching(
                        graph,
                        interviewers.size()
                );

        /*
         * Convert algorithm indices into meaningful
         * TalentFlow objects.
         */
        for (BipartiteMatching.Assignment assignment :
                assignments) {

            int candidateIndex =
                    assignment.getCandidateIndex();

            int interviewerIndex =
                    assignment.getInterviewerIndex();

            if (candidateIndex >= 0
                    && candidateIndex < candidates.size()
                    && interviewerIndex >= 0
                    && interviewerIndex < interviewers.size()) {

                result.add(
                        new ScheduledInterview(
                                candidates
                                        .get(candidateIndex)
                                        .getName(),

                                interviewers
                                        .get(interviewerIndex)
                                        .getName()
                        )
                );
            }
        }

        return result;
    }
}