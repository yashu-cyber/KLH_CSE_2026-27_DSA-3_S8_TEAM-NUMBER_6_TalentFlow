package com.talentflow.controller;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.talentflow.models.Candidate;
import com.talentflow.service.CandidateService;

@RestController
@RequestMapping("/api")
public class TalentFlowFeatureController {

    private final CandidateService candidateService;

    public TalentFlowFeatureController(
            CandidateService candidateService) {

        this.candidateService = candidateService;
    }

    /*
     * =========================================================
     * DASHBOARD
     * =========================================================
     */

    @GetMapping("/dashboard")
    public ResponseEntity<?> dashboard() {

        List<Candidate> candidates =
                candidateService.getAllCandidates();

        int totalResumes =
                candidates.size();

        Set<String> uniqueCandidates =
                new HashSet<>();

        for (Candidate candidate : candidates) {

            if (candidate == null) {
                continue;
            }

            String identity =
                    safe(candidate.getName())
                            + "|"
                            + safe(candidate.getEmail());

            uniqueCandidates.add(
                    identity.toLowerCase()
            );
        }

        int duplicateCount =
                Math.max(
                        totalResumes
                                - uniqueCandidates.size(),
                        0
                );

        Map<String, Object> response =
                new LinkedHashMap<>();

        response.put(
                "success",
                true
        );

        response.put(
                "totalResumes",
                totalResumes
        );

        response.put(
                "uniqueCandidates",
                uniqueCandidates.size()
        );

        response.put(
                "duplicatesRemoved",
                duplicateCount
        );

        response.put(
                "interviewSlotsFilled",
                0
        );

        return ResponseEntity.ok(response);
    }

    /*
     * =========================================================
     * DUPLICATE DETECTION
     * =========================================================
     */

    @GetMapping("/duplicates")
    public ResponseEntity<?> duplicates() {

        List<Candidate> candidates =
                candidateService.getAllCandidates();

        Map<String, List<Candidate>> groups =
                new LinkedHashMap<>();

        for (Candidate candidate : candidates) {

            if (candidate == null) {
                continue;
            }

            String normalized =
                    normalize(
                            candidate.getResumeText()
                    );

            String hash =
                    sha256(normalized);

            groups.computeIfAbsent(
                    hash,
                    key -> new ArrayList<>()
            ).add(candidate);
        }

        List<Map<String, Object>> duplicateGroups =
                new ArrayList<>();

        int duplicateFiles = 0;

        for (Map.Entry<String, List<Candidate>> entry :
                groups.entrySet()) {

            List<Candidate> group =
                    entry.getValue();

            if (group.size() <= 1) {
                continue;
            }

            duplicateFiles +=
                    group.size() - 1;

            List<String> files =
                    new ArrayList<>();

            for (Candidate candidate : group) {

                files.add(
                        candidate.getResumeFile()
                );
            }

            Map<String, Object> duplicate =
                    new LinkedHashMap<>();

            duplicate.put(
                    "count",
                    group.size()
            );

            duplicate.put(
                    "files",
                    files
            );

            duplicateGroups.add(
                    duplicate
            );
        }

        Map<String, Object> response =
                new LinkedHashMap<>();

        response.put(
                "success",
                true
        );

        response.put(
                "duplicateGroups",
                duplicateGroups
        );

        response.put(
                "totalDuplicateGroups",
                duplicateGroups.size()
        );

        response.put(
                "totalDuplicateFiles",
                duplicateFiles
        );

        return ResponseEntity.ok(response);
    }

    /*
     * =========================================================
     * RESUME SIMILARITY
     * =========================================================
     *
     * The recruiter-facing result is intentionally simple:
     *
     * - Overall Similarity
     * - Skills Similarity
     * - Experience Similarity
     * - Common Skills
     *
     * The implementation details stay inside the backend.
     */

    @PostMapping("/similarity")
    public ResponseEntity<?> similarity(
            @RequestBody SimilarityRequest request) {

        if (request == null
                || isBlank(request.getResumeA())
                || isBlank(request.getResumeB())) {

            return ResponseEntity.badRequest().body(
                    Map.of(
                            "success",
                            false,
                            "message",
                            "Two resume files are required."
                    )
            );
        }

        List<Candidate> candidates =
                candidateService.getAllCandidates();

        Candidate first =
                findCandidate(
                        candidates,
                        request.getResumeA()
                );

        Candidate second =
                findCandidate(
                        candidates,
                        request.getResumeB()
                );

        if (first == null
                || second == null) {

            return ResponseEntity.badRequest().body(
                    Map.of(
                            "success",
                            false,
                            "message",
                            "One or both resumes could not be found."
                    )
            );
        }

        /*
         * -----------------------------------------------------
         * SKILL SIMILARITY
         * -----------------------------------------------------
         */

        Set<String> skillsA =
                normalizedSkills(
                        first.getSkills()
                );

        Set<String> skillsB =
                normalizedSkills(
                        second.getSkills()
                );

        Set<String> commonSkills =
                new LinkedHashSet<>(
                        skillsA
                );

        commonSkills.retainAll(
                skillsB
        );

        Set<String> union =
                new HashSet<>(
                        skillsA
                );

        union.addAll(
                skillsB
        );

        double skillSimilarity;

        if (union.isEmpty()) {

            skillSimilarity = 0.0;

        } else {

            skillSimilarity =
                    (
                            (double)
                                    commonSkills.size()
                                    / union.size()
                    )
                            * 100.0;
        }

        /*
         * -----------------------------------------------------
         * EXPERIENCE SIMILARITY
         * -----------------------------------------------------
         */

        double experienceA =
                Math.max(
                        first.getExperience(),
                        0.0
                );

        double experienceB =
                Math.max(
                        second.getExperience(),
                        0.0
                );

        double experienceSimilarity;

        if (experienceA == 0.0
                && experienceB == 0.0) {

            experienceSimilarity = 100.0;

        } else {

            double maximum =
                    Math.max(
                            experienceA,
                            experienceB
                    );

            double difference =
                    Math.abs(
                            experienceA
                                    - experienceB
                    );

            experienceSimilarity =
                    (
                            1.0
                                    - (
                                    difference
                                            / maximum
                            )
                    )
                            * 100.0;
        }

        /*
         * -----------------------------------------------------
         * OVERALL SIMILARITY
         * -----------------------------------------------------
         *
         * Skills are more important than experience because
         * a recruitment match should primarily reflect the
         * candidate capabilities represented in the resumes.
         */

        double overallSimilarity =
                (
                        skillSimilarity * 0.70
                )
                        + (
                        experienceSimilarity * 0.30
                );

        Map<String, Object> response =
                new LinkedHashMap<>();

        response.put(
                "success",
                true
        );

        response.put(
                "candidateA",
                first.getName()
        );

        response.put(
                "candidateB",
                second.getName()
        );

        response.put(
                "resumeA",
                first.getResumeFile()
        );

        response.put(
                "resumeB",
                second.getResumeFile()
        );

        response.put(
                "similarityScore",
                round(overallSimilarity)
        );

        response.put(
                "skillSimilarity",
                round(skillSimilarity)
        );

        response.put(
                "experienceSimilarity",
                round(experienceSimilarity)
        );

        response.put(
                "commonSkills",
                commonSkills
        );

        return ResponseEntity.ok(
                response
        );
    }

    /*
     * =========================================================
     * INTERNAL ALGORITHM REGISTRY
     * =========================================================
     *
     * Kept for development/testing.
     * The recruiter-facing frontend does not display this.
     */

    @GetMapping("/algorithms")
    public ResponseEntity<?> algorithms() {

        List<Map<String, Object>> algorithms =
                new ArrayList<>();

        algorithms.add(
                algorithm(
                        "KMP",
                        "Knuth-Morris-Pratt",
                        "Exact pattern searching",
                        "CO2",
                        "O(n + m)"
                )
        );

        algorithms.add(
                algorithm(
                        "Z-Function",
                        "Z Algorithm",
                        "Pattern searching",
                        "CO2",
                        "O(n + m)"
                )
        );

        algorithms.add(
                algorithm(
                        "Rabin-Karp",
                        "Rolling Hash",
                        "Hash-based pattern searching",
                        "CO2 + CO6",
                        "Expected O(n + m)"
                )
        );

        algorithms.add(
                algorithm(
                        "Aho-Corasick",
                        "Multi-pattern automaton",
                        "Multiple pattern searching",
                        "CO2",
                        "O(n + matches)"
                )
        );

        algorithms.add(
                algorithm(
                        "Edit Distance",
                        "Dynamic Programming",
                        "Text comparison",
                        "CO3",
                        "O(n x m)"
                )
        );

        algorithms.add(
                algorithm(
                        "Bipartite Matching",
                        "Augmenting Path Matching",
                        "Candidate-interviewer assignment",
                        "CO4",
                        "O(VE)"
                )
        );

        algorithms.add(
                algorithm(
                        "SHA-256",
                        "Cryptographic hashing",
                        "Duplicate detection",
                        "CO6",
                        "O(n)"
                )
        );

        return ResponseEntity.ok(
                Map.of(
                        "success",
                        true,

                        "totalAlgorithms",
                        algorithms.size(),

                        "algorithms",
                        algorithms,

                        "courseOutcomes",
                        List.of(
                                "CO1 - Algorithm strategy selection",
                                "CO2 - Advanced string algorithms",
                                "CO3 - Dynamic programming",
                                "CO4 - Matching and assignment",
                                "CO5 - Optimization and approximation concepts",
                                "CO6 - Randomized, hashing and scalable processing"
                        )
                )
        );
    }

    /*
     * =========================================================
     * HELPER METHODS
     * =========================================================
     */

    private Map<String, Object> algorithm(
            String id,
            String name,
            String useCase,
            String courseOutcome,
            String complexity) {

        Map<String, Object> result =
                new LinkedHashMap<>();

        result.put(
                "id",
                id
        );

        result.put(
                "name",
                name
        );

        result.put(
                "useCase",
                useCase
        );

        result.put(
                "courseOutcome",
                courseOutcome
        );

        result.put(
                "complexity",
                complexity
        );

        return result;
    }

    private Candidate findCandidate(
            List<Candidate> candidates,
            String resumeFile) {

        if (candidates == null
                || resumeFile == null) {

            return null;
        }

        for (Candidate candidate : candidates) {

            if (candidate == null) {
                continue;
            }

            if (resumeFile.equals(
                    candidate.getResumeFile()
            )) {

                return candidate;
            }
        }

        return null;
    }

    private Set<String> normalizedSkills(
            List<String> skills) {

        Set<String> result =
                new LinkedHashSet<>();

        if (skills == null) {
            return result;
        }

        for (String skill : skills) {

            if (skill == null
                    || skill.isBlank()) {
                continue;
            }

            result.add(
                    skill.trim()
                            .toLowerCase()
            );
        }

        return result;
    }

    private String normalize(
            String text) {

        if (text == null) {
            return "";
        }

        return text
                .toLowerCase()
                .replaceAll(
                        "\\s+",
                        " "
                )
                .trim();
    }

    private String sha256(
            String text) {

        try {

            MessageDigest digest =
                    MessageDigest.getInstance(
                            "SHA-256"
                    );

            byte[] hash =
                    digest.digest(
                            text.getBytes(
                                    StandardCharsets.UTF_8
                            )
                    );

            StringBuilder result =
                    new StringBuilder();

            for (byte value : hash) {

                result.append(
                        String.format(
                                "%02x",
                                value
                        )
                );
            }

            return result.toString();

        } catch (Exception e) {

            return Integer.toHexString(
                    text.hashCode()
            );
        }
    }

    private double round(
            double value) {

        return Math.round(
                value * 100.0
        ) / 100.0;
    }

    private boolean isBlank(
            String value) {

        return value == null
                || value.isBlank();
    }

    private String safe(
            String value) {

        return value == null
                ? ""
                : value;
    }

    /*
     * =========================================================
     * REQUEST MODEL
     * =========================================================
     */

    public static class SimilarityRequest {

        private String resumeA;
        private String resumeB;

        public SimilarityRequest() {
        }

        public String getResumeA() {
            return resumeA;
        }

        public void setResumeA(
                String resumeA) {

            this.resumeA = resumeA;
        }

        public String getResumeB() {
            return resumeB;
        }

        public void setResumeB(
                String resumeB) {

            this.resumeB = resumeB;
        }
    }
}
