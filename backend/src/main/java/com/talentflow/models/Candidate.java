package com.talentflow.models;

import java.util.ArrayList;
import java.util.List;

public class Candidate {

    private String name;
    private String email;
    private List<String> skills;
    private double experience;
    private String resumeFile;
    private String resumeText;

    public Candidate() {
        this.skills = new ArrayList<>();
        this.resumeText = "";
    }

    public Candidate(
            String name,
            String email,
            List<String> skills,
            double experience,
            String resumeFile) {

        this.name = name;
        this.email = email;
        this.skills = skills != null
                ? new ArrayList<>(skills)
                : new ArrayList<>();

        this.experience = experience;
        this.resumeFile = resumeFile;
        this.resumeText = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills != null
                ? new ArrayList<>(skills)
                : new ArrayList<>();
    }

    public double getExperience() {
        return experience;
    }

    public void setExperience(double experience) {
        this.experience = experience;
    }

    public String getResumeFile() {
        return resumeFile;
    }

    public void setResumeFile(String resumeFile) {
        this.resumeFile = resumeFile;
    }

    public String getResumeText() {
        return resumeText;
    }

    public void setResumeText(String resumeText) {
        this.resumeText = resumeText != null
                ? resumeText
                : "";
    }

    @Override
    public String toString() {
        return "Candidate{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", skills=" + skills +
                ", experience=" + experience +
                ", resumeFile='" + resumeFile + '\'' +
                '}';
    }
}