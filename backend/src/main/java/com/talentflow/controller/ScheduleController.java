package com.talentflow.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.talentflow.models.Candidate;
import com.talentflow.scheduling.InterviewScheduler;
import com.talentflow.scheduling.InterviewScheduler.CandidateRequest;
import com.talentflow.scheduling.InterviewScheduler.Interviewer;
import com.talentflow.scheduling.InterviewScheduler.ScheduledInterview;
import com.talentflow.service.CandidateService;

@RestController
@RequestMapping("/api")
public class ScheduleController {

    private final CandidateService candidateService;

    public ScheduleController(
            CandidateService candidateService) {

        this.candidateService = candidateService;
    }

    @PostMapping("/schedule")
    public ResponseEntity<?> scheduleInterviews(
            @RequestBody ScheduleRequest request) {

        if (request == null
                || request.getInterviewers() == null
                || request.getInterviewers().isEmpty()) {

            return ResponseEntity.badRequest().body(
                    Map.of(
                            "success",
                            false,
                            "message",
                            "At least one interviewer is required."
                    )
            );
        }

        /*
         * -----------------------------------------------------
         * LOAD CANDIDATES
         * -----------------------------------------------------
         *
         * If the frontend provides candidate names, we use
         * those candidates.
         *
         * If no candidates are provided, TalentFlow uses the
         * complete current candidate pool.
         */

        List<Candidate> availableCandidates =
                candidateService.getAllCandidates();

        List<Candidate> selectedCandidates =
                new ArrayList<>();

        if (request.getCandidates() == null
                || request.getCandidates().isEmpty()) {

            selectedCandidates.addAll(
                    availableCandidates
            );

        } else {

            for (CandidateInput input :
                    request.getCandidates()) {

                if (input == null
                        || input.getName() == null
                        || input.getName().isBlank()) {
                    continue;
                }

                Candidate candidate =
                        findCandidate(
                                availableCandidates,
                                input.getName(),
                                input.getResumeFile()
                        );

                if (candidate != null) {
                    selectedCandidates.add(
                            candidate
                    );
                }
            }
        }

        if (selectedCandidates.isEmpty()) {

            return ResponseEntity.badRequest().body(
                    Map.of(
                            "success",
                            false,
                            "message",
                            "No candidates are available for scheduling."
                    )
            );
        }

        /*
         * -----------------------------------------------------
         * BUILD INTERNAL REQUEST
         * -----------------------------------------------------
         */

        List<CandidateRequest> candidates =
                new ArrayList<>();

        for (Candidate candidate :
                selectedCandidates) {

            candidates.add(
                    new CandidateRequest(
                            candidate.getName(),
                            candidate.getSkills()
                    )
            );
        }

        List<Interviewer> interviewers =
                new ArrayList<>();

        for (InterviewerInput input :
                request.getInterviewers()) {

            if (input == null
                    || input.getName() == null
                    || input.getName().isBlank()) {
                continue;
            }

            interviewers.add(
                    new Interviewer(
                            input.getName(),
                            input.getSkills()
                    )
            );
        }

        if (interviewers.isEmpty()) {

            return ResponseEntity.badRequest().body(
                    Map.of(
                            "success",
                            false,
                            "message",
                            "No valid interviewers were provided."
                    )
            );
        }

        /*
         * -----------------------------------------------------
         * INTERNAL ASSIGNMENT
         * -----------------------------------------------------
         *
         * The matching implementation remains hidden from
         * the recruiter-facing product.
         */

        List<ScheduledInterview> assignments =
                InterviewScheduler.schedule(
                        candidates,
                        interviewers
                );

        /*
         * -----------------------------------------------------
         * RECRUITER-FACING RESPONSE
         * -----------------------------------------------------
         */

        List<Map<String, String>> result =
                new ArrayList<>();

        for (ScheduledInterview assignment :
                assignments) {

            result.add(
                    Map.of(
                            "candidate",
                            assignment.getCandidate(),

                            "interviewer",
                            assignment.getInterviewer()
                    )
            );
        }

        Map<String, Object> response =
                Map.of(
                        "success",
                        true,

                        "totalCandidates",
                        candidates.size(),

                        "totalInterviewers",
                        interviewers.size(),

                        "assigned",
                        assignments.size(),

                        "unassigned",
                        candidates.size()
                                - assignments.size(),

                        "assignments",
                        result
                );

        return ResponseEntity.ok(response);
    }

    /*
     * =========================================================
     * FIND CANDIDATE
     * =========================================================
     */

    private Candidate findCandidate(
            List<Candidate> candidates,
            String name,
            String resumeFile) {

        if (candidates == null) {
            return null;
        }

        /*
         * Prefer the exact resume file when supplied.
         */

        if (resumeFile != null
                && !resumeFile.isBlank()) {

            for (Candidate candidate :
                    candidates) {

                if (candidate == null) {
                    continue;
                }

                if (resumeFile.equals(
                        candidate.getResumeFile()
                )) {

                    return candidate;
                }
            }
        }

        /*
         * Otherwise match by candidate name.
         */

        for (Candidate candidate :
                candidates) {

            if (candidate == null) {
                continue;
            }

            if (candidate.getName() != null
                    && candidate.getName()
                    .equalsIgnoreCase(name)) {

                return candidate;
            }
        }

        return null;
    }

    /*
     * =========================================================
     * REQUEST MODELS
     * =========================================================
     */

    public static class ScheduleRequest {

        private List<CandidateInput> candidates;

        private List<InterviewerInput> interviewers;

        public ScheduleRequest() {
        }

        public List<CandidateInput> getCandidates() {
            return candidates;
        }

        public void setCandidates(
                List<CandidateInput> candidates) {

            this.candidates = candidates;
        }

        public List<InterviewerInput> getInterviewers() {
            return interviewers;
        }

        public void setInterviewers(
                List<InterviewerInput> interviewers) {

            this.interviewers = interviewers;
        }
    }

    public static class CandidateInput {

        private String name;

        private String resumeFile;

        public CandidateInput() {
        }

        public String getName() {
            return name;
        }

        public void setName(
                String name) {

            this.name = name;
        }

        public String getResumeFile() {
            return resumeFile;
        }

        public void setResumeFile(
                String resumeFile) {

            this.resumeFile = resumeFile;
        }
    }

    public static class InterviewerInput {

        private String name;

        /*
         * Interviewer skills are internal scheduling data.
         * They are not exposed as a recruiter-facing
         * configuration requirement.
         */

        private List<String> skills;

        public InterviewerInput() {
        }

        public String getName() {
            return name;
        }

        public void setName(
                String name) {

            this.name = name;
        }

        public List<String> getSkills() {
            return skills;
        }

        public void setSkills(
                List<String> skills) {

            this.skills = skills;
        }
    }
}
