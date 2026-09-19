package com.talentflow.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.talentflow.models.Candidate;
import com.talentflow.parser.ResumeParser;

@RestController
@RequestMapping("/api")
public class ResumeUploadController {

    private static final Path RESUME_FOLDER =
            Paths.get("resumes");

    @PostMapping("/upload")
    public ResponseEntity<?> uploadResume(
            @RequestParam("file") MultipartFile file) {

        try {

            // Validate upload
            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest().body(
                        Map.of(
                                "success", false,
                                "message",
                                "Please upload a resume."
                        )
                );
            }

            // Only PDF files are supported
            String originalName =
                    file.getOriginalFilename();

            if (originalName == null
                    || !originalName
                    .toLowerCase()
                    .endsWith(".pdf")) {

                return ResponseEntity.badRequest().body(
                        Map.of(
                                "success", false,
                                "message",
                                "Only PDF resumes are supported."
                        )
                );
            }

            // Create resume storage directory
            Files.createDirectories(
                    RESUME_FOLDER
            );

            /*
             * Use a timestamp to avoid overwriting files
             * with identical names.
             */
            String safeFileName =
                    Paths.get(originalName)
                            .getFileName()
                            .toString();

            String storedFileName =
                    System.currentTimeMillis()
                            + "_"
                            + safeFileName;

            Path destination =
                    RESUME_FOLDER.resolve(
                            storedFileName
                    );

            // Save uploaded PDF
            Files.write(
                    destination,
                    file.getBytes()
            );

            // Parse the resume
            Candidate candidate =
                    ResumeParser.parseResume(
                            destination.toString()
                    );

            if (candidate == null) {

                // Remove unreadable file
                Files.deleteIfExists(
                        destination
                );

                return ResponseEntity.badRequest().body(
                        Map.of(
                                "success", false,
                                "message",
                                "Could not read this resume PDF."
                        )
                );
            }

            return ResponseEntity.ok(
                    Map.of(
                            "success", true,
                            "message",
                            "Resume processed successfully.",
                            "candidate",
                            candidate
                    )
            );

        } catch (IOException e) {

            return ResponseEntity
                    .internalServerError()
                    .body(
                            Map.of(
                                    "success", false,
                                    "message",
                                    "Error saving resume: "
                                            + e.getMessage()
                            )
                    );

        } catch (Exception e) {

            return ResponseEntity
                    .internalServerError()
                    .body(
                            Map.of(
                                    "success", false,
                                    "message",
                                    "Error processing resume: "
                                            + e.getMessage()
                            )
                    );
        }
    }
}