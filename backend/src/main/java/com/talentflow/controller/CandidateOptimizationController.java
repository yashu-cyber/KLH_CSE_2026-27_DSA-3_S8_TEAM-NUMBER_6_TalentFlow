package com.talentflow.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.talentflow.models.Candidate;
import com.talentflow.service.CandidateService;
import com.talentflow.service.ResumeMatchingService;
import com.talentflow.service.ResumeMatchingService.MatchResult;

@RestController
@RequestMapping("/api")
public class CandidateOptimizationController {

    private final CandidateService candidateService;

    private final ResumeMatchingService matchingService;

    public CandidateOptimizationController(
            CandidateService candidateService,
            ResumeMatchingService matchingService) {

        this.candidateService =
                candidateService;

        this.matchingService =
                matchingService;
    }

    @PostMapping("/optimize")
    public ResponseEntity<?> optimize(
            @RequestBody OptimizationRequest request) {

        if (request == null
                || request.getQuery() == null
                || request.getQuery().isBlank()) {

            return ResponseEntity.badRequest().body(
                    Map.of(
                            "success",
                            false,

                            "message",
                            "Role requirements are required."
                    )
            );
        }

        String query =
                request.getQuery()
                        .trim();

        int limit =
                request.getLimit();

        if (limit <= 0) {
            limit = 5;
        }

        limit =
                Math.min(
                        limit,
                        20
                );

        List<Candidate> candidates =
                candidateService.getAllCandidates();

        List<OptimizedCandidate> matches =
                new ArrayList<>();

        for (Candidate candidate :
                candidates) {

            if (candidate == null) {
                continue;
            }

            MatchResult result =
                    matchingService.analyze(
                            candidate,
                            query
                    );

            if (!result.isMatched()) {
                continue;
            }

            matches.add(
                    new OptimizedCandidate(
                            candidate,
                            result
                    )
            );
        }

        matches.sort(
                Comparator
                        .comparingDouble(
                                OptimizedCandidate::score
                        )
                        .reversed()
                        .thenComparingDouble(
                                value ->
                                        value
                                                .candidate()
                                                .getExperience()
                        )
                        .reversed()
        );

        int totalCandidates =
                candidates.size();

        List<Map<String, Object>> results =
                new ArrayList<>();

        int count =
                Math.min(
                        limit,
                        matches.size()
                );

        for (int i = 0;
             i < count;
             i++) {

            OptimizedCandidate item =
                    matches.get(i);

            Candidate candidate =
                    item.candidate();

            MatchResult match =
                    item.match();

            Map<String, Object> result =
                    new LinkedHashMap<>();

            result.put(
                    "name",
                    candidate.getName()
            );

            result.put(
                    "email",
                    candidate.getEmail()
            );

            result.put(
                    "skills",
                    candidate.getSkills()
            );

            result.put(
                    "experience",
                    candidate.getExperience()
            );

            result.put(
                    "resumeFile",
                    candidate.getResumeFile()
            );

            result.put(
                    "score",
                    match.getScore()
            );

            result.put(
                    "matchedTerms",
                    match.getMatchedTerms()
            );

            results.add(result);
        }

        return ResponseEntity.ok(
                Map.of(
                        "success",
                        true,

                        "query",
                        query,

                        "totalCandidates",
                        totalCandidates,

                        "totalMatches",
                        matches.size(),

                        "results",
                        results
                )
        );
    }

    private record OptimizedCandidate(
            Candidate candidate,
            MatchResult match) {

        private double score() {
            return match.getScore();
        }
    }

    public static class OptimizationRequest {

        private String query;

        private int limit = 5;

        public OptimizationRequest() {
        }

        public String getQuery() {
            return query;
        }

        public void setQuery(
                String query) {

            this.query = query;
        }

        public int getLimit() {
            return limit;
        }

        public void setLimit(
                int limit) {

            this.limit = limit;
        }
    }
}