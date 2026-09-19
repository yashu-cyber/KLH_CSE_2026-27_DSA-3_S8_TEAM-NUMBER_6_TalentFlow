package com.talentflow.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.talentflow.models.Candidate;

public class ResumeParser {

    // Extract text from a PDF resume
    public static String extractText(String filePath) {

        File file = new File(filePath);

        try (PDDocument document = Loader.loadPDF(file)) {

            PDFTextStripper stripper = new PDFTextStripper();

            return stripper.getText(document);

        } catch (Exception e) {

            System.out.println("Error reading resume: " + e.getMessage());
            return "";
        }
    }

    // Extract candidate name
    private static String extractName(String text) {

        String[] lines = text.split("\\R");

        for (String line : lines) {

            line = line.trim();

            if (!line.isEmpty()
                    && !line.contains("@")
                    && !line.toLowerCase().contains("resume")
                    && line.length() < 50) {

                return line;
            }
        }

        return "Unknown Candidate";
    }

    // Extract email
    private static String extractEmail(String text) {

        Pattern pattern = Pattern.compile(
                "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}"
        );

        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {
            return matcher.group();
        }

        return "Not available";
    }

    // Detect common technical skills
    private static List<String> extractSkills(String text) {

        String lowerText = text.toLowerCase();

        List<String> knownSkills = Arrays.asList(
                "Java",
                "Python",
                "C",
                "C++",
                "SQL",
                "MySQL",
                "MongoDB",
                "JavaScript",
                "HTML",
                "CSS",
                "DSA",
                "Data Structures",
                "Algorithms",
                "Machine Learning",
                "Artificial Intelligence",
                "Git",
                "GitHub",
                "Linux",
                "React",
                "Flask",
                "Spring"
        );

        List<String> detectedSkills = new ArrayList<>();

        for (String skill : knownSkills) {

            if (lowerText.contains(skill.toLowerCase())) {
                detectedSkills.add(skill);
            }
        }

        return detectedSkills;
    }

    // Extract experience when explicitly mentioned
    private static double extractExperience(String text) {

        Pattern pattern = Pattern.compile(
                "(\\d+(?:\\.\\d+)?)\\s*\\+?\\s*years?\\s+(?:of\\s+)?experience",
                Pattern.CASE_INSENSITIVE
        );

        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {

            try {
                return Double.parseDouble(matcher.group(1));
            } catch (NumberFormatException e) {
                return 0.0;
            }
        }

        return 0.0;
    }

    // Convert one PDF into a Candidate
    public static Candidate parseResume(String filePath) {

        String resumeText = extractText(filePath);

        if (resumeText.isEmpty()) {
            return null;
        }

        String name = extractName(resumeText);
        String email = extractEmail(resumeText);
        List<String> skills = extractSkills(resumeText);
        double experience = extractExperience(resumeText);

        return new Candidate(
                name,
                email,
                filePath,
                skills,
                experience
        );
    }

    // Read every PDF inside the resumes folder
    public static List<Candidate> parseAllResumes(String folderPath) {

        List<Candidate> candidates = new ArrayList<>();

        File folder = new File(folderPath);

        if (!folder.exists() || !folder.isDirectory()) {

            System.out.println("Resume folder not found: " + folderPath);
            return candidates;
        }

        File[] files = folder.listFiles(
                (dir, name) -> name.toLowerCase().endsWith(".pdf")
        );

        if (files == null) {
            return candidates;
        }

        for (File file : files) {

            Candidate candidate = parseResume(file.getPath());

            if (candidate != null) {
                candidates.add(candidate);
            }
        }

        return candidates;
    }

    // Test all resume files
    public static void main(String[] args) {

        String folderPath = "resumes";

        List<Candidate> candidates =
                parseAllResumes(folderPath);

        System.out.println("===== TALENTFLOW RESUME IMPORT =====");

        System.out.println(
                "Resumes found: " + candidates.size()
        );

        for (Candidate candidate : candidates) {

            System.out.println("\n------------------------------");

            System.out.println(
                    "Name: " + candidate.getName()
            );

            System.out.println(
                    "Email: " + candidate.getEmail()
            );

            System.out.println(
                    "File: " + candidate.getResumeFile()
            );

            System.out.println(
                    "Skills: " + candidate.getSkills()
            );

            System.out.println(
                    "Experience: "
                            + candidate.getExperience()
                            + " years"
            );
        }
    }
}