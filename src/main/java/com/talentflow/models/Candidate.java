package com.talentflow.models;

import java.util.ArrayList;
import java.util.List;

public class Candidate {

    private String name;
    private String email;
    private String resumeFile;
    private List<String> skills;
    private double experience;
    private double matchScore;

    public Candidate(String name, String email, String resumeFile,
                     List<String> skills, double experience) {

        this.name = name;
        this.email = email;
        this.resumeFile = resumeFile;
        this.skills = new ArrayList<>(skills);
        this.experience = experience;
        this.matchScore = 0.0;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getResumeFile() {
        return resumeFile;
    }

    public List<String> getSkills() {
        return skills;
    }

    public double getExperience() {
        return experience;
    }

    public double getMatchScore() {
        return matchScore;
    }

    public void setMatchScore(double matchScore) {
        this.matchScore = matchScore;
    }

    public boolean hasSkill(String skill) {

        for (String candidateSkill : skills) {
            if (candidateSkill.equalsIgnoreCase(skill)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String toString() {

        return "Candidate{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", resumeFile='" + resumeFile + '\'' +
                ", skills=" + skills +
                ", experience=" + experience +
                ", matchScore=" + matchScore +
                '}';
    }
}