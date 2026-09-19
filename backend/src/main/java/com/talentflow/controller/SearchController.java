package com.talentflow.controller;

import java.util.ArrayList;
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
public class SearchController {

    private final CandidateService candidateService;

    private final ResumeMatchingService matchingService;

    public SearchController(
            CandidateService candidateService,
            ResumeMatchingService matchingService) {

        this.candidateService =
                candidateService;

        this.matchingService =
                matchingService;
    }

    @PostMapping("/search")
    public ResponseEntity<?> search(
            @RequestBody SearchRequest request) {

        if (request == null
                || request.getQuery() == null
                || request.getQuery().isBlank()) {

            return ResponseEntity.badRequest().body(
                    Map.of(
                            "success",
                            false,

                            "message",
                            "Search query is required."
                    )
            );
        }

        String query =
                request.getQuery()
                        .trim();

        List<Candidate> candidates =
                candidateService.getAllCandidates();

        List<Map<String, Object>> results =
                new ArrayList<>();

        for (Candidate candidate :
                candidates) {

            if (candidate == null) {
                continue;
            }

            MatchResult match =
                    matchingService.analyze(
                            candidate,
                            query
                    );

            if (!match.isMatched()) {
                continue;
            }

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
                    "occurrences",
                    match.getOccurrences()
            );

            result.put(
                    "matchScore",
                    match.getScore()
            );

            result.put(
                    "matchedTerms",
                    match.getMatchedTerms()
            );

            results.add(result);
        }

        results.sort(
                (first, second) ->
                        Double.compare(
                                ((Number)
                                        second.get(
                                                "matchScore"
                                        ))
                                        .doubleValue(),

                                ((Number)
                                        first.get(
                                                "matchScore"
                                        ))
                                        .doubleValue()
                        )
        );

        return ResponseEntity.ok(
                Map.of(
                        "success",
                        true,

                        "query",
                        query,

                        "totalMatches",
                        results.size(),

                        "results",
                        results
                )
        );
    }

    public static class SearchRequest {

        private String query;

        public SearchRequest() {
        }

        public String getQuery() {
            return query;
        }

        public void setQuery(
                String query) {

            this.query = query;
        }
    }
}