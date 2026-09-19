package com.talentflow.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.talentflow.models.Candidate;
import com.talentflow.service.CandidateService;

@RestController
@RequestMapping("/api")
public class CandidatesController {

    private final CandidateService candidateService;

    public CandidatesController(
            CandidateService candidateService) {

        this.candidateService = candidateService;
    }

    /**
     * Returns every parsed candidate.
     *
     * GET /api/candidates
     */
    @GetMapping("/candidates")
    public ResponseEntity<?> getCandidates() {

        List<Candidate> candidates =
                candidateService.getAllCandidates();

        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "totalCandidates",
                        candidates.size(),
                        "candidates",
                        candidates
                )
        );
    }

    /**
     * Searches candidates by a keyword.
     *
     * GET /api/candidates/search?keyword=java
     */
    @GetMapping("/candidates/search")
    public ResponseEntity<?> searchCandidates(
            @RequestParam String keyword) {

        if (keyword == null
                || keyword.isBlank()) {

            return ResponseEntity.badRequest().body(
                    Map.of(
                            "success", false,
                            "message",
                            "Search keyword is required."
                    )
            );
        }

        List<Candidate> matches =
                candidateService.findByKeyword(
                        keyword
                );

        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "keyword", keyword,
                        "totalMatches",
                        matches.size(),
                        "candidates",
                        matches
                )
        );
    }
}