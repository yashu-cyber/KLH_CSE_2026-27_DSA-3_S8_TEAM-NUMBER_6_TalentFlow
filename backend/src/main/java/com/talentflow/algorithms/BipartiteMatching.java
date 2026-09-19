package com.talentflow.algorithms;

import java.util.ArrayList;
import java.util.List;

/**
 * Bipartite Maximum Matching using augmenting paths.
 *
 * TalentFlow application:
 * Automatically assigns candidates to eligible interviewers.
 *
 * Left side  -> Candidates
 * Right side -> Interviewers
 *
 * Time Complexity: O(V * E)
 * Space Complexity: O(V + E)
 */
public final class BipartiteMatching {

    private BipartiteMatching() {
        // Utility class
    }

    /**
     * Represents one candidate-interviewer assignment.
     */
    public static class Assignment {

        private final int candidateIndex;
        private final int interviewerIndex;

        public Assignment(
                int candidateIndex,
                int interviewerIndex) {

            this.candidateIndex = candidateIndex;
            this.interviewerIndex = interviewerIndex;
        }

        public int getCandidateIndex() {
            return candidateIndex;
        }

        public int getInterviewerIndex() {
            return interviewerIndex;
        }

        @Override
        public String toString() {
            return "Assignment{" +
                    "candidateIndex=" + candidateIndex +
                    ", interviewerIndex=" + interviewerIndex +
                    '}';
        }
    }

    /**
     * Finds a maximum bipartite matching.
     *
     * graph[candidate] contains the interviewer indices
     * that candidate is eligible to meet.
     */
    public static List<Assignment> maximumMatching(
            List<List<Integer>> graph,
            int interviewerCount) {

        List<Assignment> assignments =
                new ArrayList<>();

        if (graph == null
                || graph.isEmpty()
                || interviewerCount <= 0) {

            return assignments;
        }

        /*
         * match[interviewer] stores the candidate currently
         * assigned to that interviewer.
         *
         * -1 means the interviewer is free.
         */
        int[] match =
                new int[interviewerCount];

        for (int i = 0;
             i < interviewerCount;
             i++) {

            match[i] = -1;
        }

        /*
         * Try to find an augmenting path for every candidate.
         */
        for (int candidate = 0;
             candidate < graph.size();
             candidate++) {

            boolean[] visited =
                    new boolean[interviewerCount];

            findAugmentingPath(
                    candidate,
                    graph,
                    match,
                    visited
            );
        }

        /*
         * Convert interviewer -> candidate matches
         * into candidate -> interviewer assignments.
         */
        for (int interviewer = 0;
             interviewer < interviewerCount;
             interviewer++) {

            if (match[interviewer] != -1) {

                assignments.add(
                        new Assignment(
                                match[interviewer],
                                interviewer
                        )
                );
            }
        }

        return assignments;
    }

    /**
     * Finds an augmenting path using DFS.
     */
    private static boolean findAugmentingPath(
            int candidate,
            List<List<Integer>> graph,
            int[] match,
            boolean[] visited) {

        if (candidate < 0
                || candidate >= graph.size()) {

            return false;
        }

        List<Integer> interviewers =
                graph.get(candidate);

        if (interviewers == null) {
            return false;
        }

        for (Integer interviewer :
                interviewers) {

            if (interviewer == null
                    || interviewer < 0
                    || interviewer >= match.length) {

                continue;
            }

            if (visited[interviewer]) {
                continue;
            }

            visited[interviewer] = true;

            /*
             * If the interviewer is free, assign them.
             *
             * Otherwise try to move their current candidate
             * to another interviewer.
             */
            if (match[interviewer] == -1
                    || findAugmentingPath(
                            match[interviewer],
                            graph,
                            match,
                            visited)) {

                match[interviewer] =
                        candidate;

                return true;
            }
        }

        return false;
    }

    /**
     * Builds an eligibility graph based on shared skills.
     */
    public static List<List<Integer>> buildEligibilityGraph(
            List<List<String>> candidateSkills,
            List<List<String>> interviewerSkills) {

        List<List<Integer>> graph =
                new ArrayList<>();

        if (candidateSkills == null
                || interviewerSkills == null) {

            return graph;
        }

        for (List<String> candidate :
                candidateSkills) {

            List<Integer> eligibleInterviewers =
                    new ArrayList<>();

            for (int interviewer = 0;
                 interviewer < interviewerSkills.size();
                 interviewer++) {

                if (sharesSkill(
                        candidate,
                        interviewerSkills.get(interviewer))) {

                    eligibleInterviewers.add(
                            interviewer
                    );
                }
            }

            graph.add(
                    eligibleInterviewers
            );
        }

        return graph;
    }

    /**
     * Checks whether two skill sets share at least
     * one skill.
     */
    private static boolean sharesSkill(
            List<String> first,
            List<String> second) {

        if (first == null || second == null) {
            return false;
        }

        for (String firstSkill : first) {

            if (firstSkill == null) {
                continue;
            }

            for (String secondSkill : second) {

                if (secondSkill == null) {
                    continue;
                }

                if (firstSkill.trim()
                        .equalsIgnoreCase(
                                secondSkill.trim())) {

                    return true;
                }
            }
        }

        return false;
    }
}