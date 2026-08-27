package com.talentflow.ranking;

import java.util.Comparator;
import java.util.List;

import com.talentflow.algorithms.EditDistance;
import com.talentflow.algorithms.KMP;
import com.talentflow.models.Candidate;
import com.talentflow.parser.ResumeParser;

public class CandidateRanking {

    // Rank candidates based on recruiter search
    public static void rankCandidates(
            List<Candidate> candidates,
            String recruiterQuery) {

        String[] searchSkills =
                recruiterQuery.toLowerCase().trim().split("\\s+");

        for (Candidate candidate : candidates) {

            double score = 0;

            for (String query : searchSkills) {

                boolean exactMatch = false;
                boolean fuzzyMatch = false;

                for (String skill : candidate.getSkills()) {

                    // KMP exact matching
                    if (KMP.search(
                            skill.toLowerCase(),
                            query)) {

                        exactMatch = true;
                        break;
                    }

                    // Edit Distance fuzzy matching
                    if (EditDistance.fuzzyMatch(
                            query,
                            skill,
                            1)) {

                        fuzzyMatch = true;
                    }
                }

                if (exactMatch) {
                    score += 50;
                } else if (fuzzyMatch) {
                    score += 35;
                }
            }

            // Experience contributes to ranking
            score += Math.min(
                    candidate.getExperience() * 5,
                    25
            );

            candidate.setMatchScore(
                    Math.min(score, 100)
            );
        }

        // Highest score first
        candidates.sort(
                Comparator.comparingDouble(
                        Candidate::getMatchScore
                ).reversed()
        );
    }

    // Display ranked candidates
    public static void displayRanking(
            List<Candidate> candidates) {

        System.out.println(
                "\n===== TALENTFLOW SEARCH RESULTS ====="
        );

        if (candidates.isEmpty()) {

            System.out.println(
                    "No resumes found."
            );

            return;
        }

        int position = 1;

        for (Candidate candidate : candidates) {

            System.out.printf(
                    "%d. %s | Match Score: %.1f%% | Skills: %s%n",
                    position,
                    candidate.getName(),
                    candidate.getMatchScore(),
                    candidate.getSkills()
            );

            position++;
        }
    }

    public static void main(String[] args) {

        // Load real resumes from the resumes folder
        String resumeFolder = "resumes";

        List<Candidate> candidates =
                ResumeParser.parseAllResumes(
                        resumeFolder
                );

        System.out.println(
                "Resumes loaded: " + candidates.size()
        );

        // Simulated recruiter search
        String recruiterQuery = "DSA SQL";

        System.out.println(
                "Recruiter Search: " + recruiterQuery
        );

        // Rank real candidates
        rankCandidates(
                candidates,
                recruiterQuery
        );

        // Display results
        displayRanking(candidates);
    }
}