package com.talentflow.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.talentflow.models.Candidate;

public class ResumeParser {

    private static final List<String> KNOWN_SKILLS = Arrays.asList(
            "Java",
            "Python",
            "C",
            "C++",
            "C#",
            "SQL",
            "MySQL",
            "MongoDB",
            "PostgreSQL",
            "JavaScript",
            "TypeScript",
            "HTML",
            "CSS",
            "React",
            "Angular",
            "Node.js",
            "Express",
            "Flask",
            "Django",
            "Spring",
            "Spring Boot",
            "Data Structures",
            "Algorithms",
            "DSA",
            "Machine Learning",
            "Artificial Intelligence",
            "Deep Learning",
            "NLP",
            "Computer Vision",
            "Git",
            "GitHub",
            "Linux",
            "Docker",
            "Kubernetes",
            "AWS",
            "Azure",
            "GCP",
            "REST API",
            "Figma",
            "Firebase"
    );

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile(
                    "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}"
            );

    private static final Pattern EXPERIENCE_PATTERN =
            Pattern.compile(
                    "(\\d+(?:\\.\\d+)?)\\s*\\+?\\s*(?:years?|yrs?)",
                    Pattern.CASE_INSENSITIVE
            );

    private ResumeParser() {
        // Utility class
    }

    /**
     * Extract all readable text from a PDF.
     */
    public static String extractText(String filePath) {

        if (filePath == null || filePath.isBlank()) {
            return "";
        }

        try (PDDocument document =
                     Loader.loadPDF(new File(filePath))) {

            PDFTextStripper stripper =
                    new PDFTextStripper();

            return stripper.getText(document);

        } catch (Exception e) {

            System.err.println(
                    "Could not extract text from "
                            + filePath
                            + ": "
                            + e.getMessage()
            );

            return "";
        }
    }

    /**
     * Parse a complete resume into a Candidate object.
     */
    public static Candidate parseResume(String filePath) {

        String text = extractText(filePath);

        if (text == null || text.isBlank()) {
            return null;
        }

        String name = extractName(text);
        String email = extractEmail(text);
        List<String> skills = extractSkills(text);
        double experience = extractExperience(text);

        Candidate candidate =
                new Candidate(
                        name,
                        email,
                        skills,
                        experience,
                        new File(filePath).getName()
                );

        /*
         * Keep the complete extracted resume text.
         *
         * This allows the TalentFlow search engine to search
         * the complete resume instead of relying only on
         * the detected skills.
         */
        candidate.setResumeText(text);

        return candidate;
    }

    /**
     * Extract the most likely candidate name.
     */
    private static String extractName(String text) {

        String[] lines = text.split("\\R");

        for (String rawLine : lines) {

            String line = rawLine.trim();

            if (line.isEmpty()) {
                continue;
            }

            String lower = line.toLowerCase();

            if (lower.contains("resume")
                    || lower.contains("curriculum vitae")
                    || lower.contains("@")
                    || lower.contains("phone")
                    || lower.contains("mobile")
                    || lower.matches(".*\\d.*")) {

                continue;
            }

            /*
             * Candidate names are normally short and contain
             * alphabetic characters, spaces, apostrophes,
             * periods, or hyphens.
             */
            if (line.length() <= 60
                    && line.matches("[A-Za-z][A-Za-z .'-]*")) {

                return line;
            }
        }

        return "Unknown Candidate";
    }

    /**
     * Extract the first email address.
     */
    private static String extractEmail(String text) {

        Matcher matcher =
                EMAIL_PATTERN.matcher(text);

        if (matcher.find()) {
            return matcher.group();
        }

        return "";
    }

    /**
     * Detect known technical skills.
     *
     * LinkedHashSet prevents duplicate skills while
     * preserving detection order.
     */
    private static List<String> extractSkills(String text) {

        Set<String> foundSkills =
                new LinkedHashSet<>();

        String lowerText =
                text.toLowerCase();

        for (String skill : KNOWN_SKILLS) {

            String lowerSkill =
                    skill.toLowerCase();

            if (lowerText.contains(lowerSkill)) {
                foundSkills.add(skill);
            }
        }

        return new ArrayList<>(foundSkills);
    }

    /**
     * Extract the highest explicit experience value.
     *
     * Examples:
     * 2 years
     * 3.5 years
     * 4 yrs
     * 5+ years
     */
    private static double extractExperience(String text) {

        Matcher matcher =
                EXPERIENCE_PATTERN.matcher(text);

        double maximumExperience = 0.0;

        while (matcher.find()) {

            try {

                double value =
                        Double.parseDouble(
                                matcher.group(1)
                        );

                maximumExperience =
                        Math.max(
                                maximumExperience,
                                value
                        );

            } catch (NumberFormatException ignored) {
                // Ignore malformed values.
            }
        }

        return maximumExperience;
    }

    /**
     * Parse every PDF in a directory.
     */
    public static List<Candidate> parseAllResumes(
            String folderPath) {

        List<Candidate> candidates =
                new ArrayList<>();

        File folder =
                new File(folderPath);

        if (!folder.exists()
                || !folder.isDirectory()) {

            return candidates;
        }

        File[] files =
                folder.listFiles(
                        file -> file.isFile()
                                && file.getName()
                                .toLowerCase()
                                .endsWith(".pdf")
                );

        if (files == null) {
            return candidates;
        }

        for (File file : files) {

            Candidate candidate =
                    parseResume(
                            file.getAbsolutePath()
                    );

            if (candidate != null) {
                candidates.add(candidate);
            }
        }

        return candidates;
    }
}