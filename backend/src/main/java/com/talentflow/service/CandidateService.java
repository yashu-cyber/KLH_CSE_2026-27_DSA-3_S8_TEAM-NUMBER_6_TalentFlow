package com.talentflow.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.talentflow.models.Candidate;
import com.talentflow.parser.ResumeParser;

@Service
public class CandidateService {

    private static final String RESUME_FOLDER = "resumes";

    public List<Candidate> getAllCandidates() {
        return ResumeParser.parseAllResumes(RESUME_FOLDER);
    }

    public List<Candidate> getCandidatesSortedByName() {

        List<Candidate> candidates =
                new ArrayList<>(getAllCandidates());

        candidates.sort(
                Comparator.comparing(
                        Candidate::getName,
                        String.CASE_INSENSITIVE_ORDER
                )
        );

        return candidates;
    }

    public int getCandidateCount() {
        return getAllCandidates().size();
    }

    public List<Candidate> findByKeyword(String keyword) {

        List<Candidate> matches =
                new ArrayList<>();

        if (keyword == null || keyword.isBlank()) {
            return matches;
        }

        String query =
                keyword.trim().toLowerCase();

        for (Candidate candidate : getAllCandidates()) {

            if (candidate == null) {
                continue;
            }

            if (candidateMatches(candidate, query)) {
                matches.add(candidate);
            }
        }

        return matches;
    }

    private boolean candidateMatches(
            Candidate candidate,
            String query) {

        String resumeText =
                candidate.getResumeText();

        if (resumeText != null
                && resumeText.toLowerCase().contains(query)) {

            return true;
        }

        List<String> skills =
                candidate.getSkills();

        if (skills != null) {

            for (String skill : skills) {

                if (skill != null
                        && skill.toLowerCase().contains(query)) {

                    return true;
                }
            }
        }

        return false;
    }

    public File getResumeDirectory() {
        return new File(RESUME_FOLDER);
    }
}
