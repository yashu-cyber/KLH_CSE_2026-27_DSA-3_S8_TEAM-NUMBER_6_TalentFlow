package com.talentflow.algorithms;

public class KMP {

    // Build the LPS (Longest Prefix Suffix) array
    private static int[] buildLPS(String pattern) {
        int[] lps = new int[pattern.length()];

        int length = 0;
        int i = 1;

        while (i < pattern.length()) {

            if (pattern.charAt(i) == pattern.charAt(length)) {
                length++;
                lps[i] = length;
                i++;
            } else {
                if (length != 0) {
                    length = lps[length - 1];
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }

        return lps;
    }

    // Search for a pattern inside text using KMP
    public static boolean search(String text, String pattern) {

        if (pattern == null || pattern.isEmpty()) {
            return true;
        }

        if (text == null || text.isEmpty()) {
            return false;
        }

        // Make searching case-insensitive
        text = text.toLowerCase();
        pattern = pattern.toLowerCase();

        int[] lps = buildLPS(pattern);

        int textIndex = 0;
        int patternIndex = 0;

        while (textIndex < text.length()) {

            if (text.charAt(textIndex) == pattern.charAt(patternIndex)) {
                textIndex++;
                patternIndex++;

                // Complete pattern found
                if (patternIndex == pattern.length()) {
                    return true;
                }

            } else {

                if (patternIndex != 0) {
                    patternIndex = lps[patternIndex - 1];
                } else {
                    textIndex++;
                }
            }
        }

        return false;
    }

    // Test KMP independently
    public static void main(String[] args) {

        String resumeText =
                "Experienced in Java, SQL, Data Structures and Algorithms";

        String skill = "SQL";

        boolean found = search(resumeText, skill);

        System.out.println("Resume: " + resumeText);
        System.out.println("Search: " + skill);

        if (found) {
            System.out.println("KMP Result: MATCH FOUND");
        } else {
            System.out.println("KMP Result: NO MATCH");
        }
    }
}