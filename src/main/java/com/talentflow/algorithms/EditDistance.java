package com.talentflow.algorithms;

public class EditDistance {

    // Wagner-Fischer algorithm
    public static int calculate(String first, String second) {

        first = first.toLowerCase().trim();
        second = second.toLowerCase().trim();

        int m = first.length();
        int n = second.length();

        int[][] dp = new int[m + 1][n + 1];

        // Base cases
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i;
        }

        for (int j = 0; j <= n; j++) {
            dp[0][j] = j;
        }

        // Fill DP table
        for (int i = 1; i <= m; i++) {

            for (int j = 1; j <= n; j++) {

                if (first.charAt(i - 1) == second.charAt(j - 1)) {

                    dp[i][j] = dp[i - 1][j - 1];

                } else {

                    int insert = dp[i][j - 1];
                    int delete = dp[i - 1][j];
                    int replace = dp[i - 1][j - 1];

                    dp[i][j] = 1 + Math.min(
                            insert,
                            Math.min(delete, replace)
                    );
                }
            }
        }

        return dp[m][n];
    }

    // Fuzzy matching using Edit Distance
    public static boolean fuzzyMatch(
            String query,
            String skill,
            int threshold) {

        int distance = calculate(query, skill);

        return distance <= threshold;
    }

    // Find the closest matching skill
    public static String findClosestSkill(
            String query,
            String[] skills,
            int threshold) {

        String bestMatch = null;
        int bestDistance = Integer.MAX_VALUE;

        for (String skill : skills) {

            int distance = calculate(query, skill);

            if (distance < bestDistance) {
                bestDistance = distance;
                bestMatch = skill;
            }
        }

        if (bestDistance <= threshold) {
            return bestMatch;
        }

        return null;
    }

    public static void main(String[] args) {

        String recruiterQuery = "DSQ";

        String[] knownSkills = {
                "DSA",
                "Java",
                "SQL",
                "Python",
                "Machine Learning"
        };

        String closestSkill =
                findClosestSkill(recruiterQuery, knownSkills, 1);

        System.out.println("Recruiter Search: " + recruiterQuery);

        if (closestSkill != null) {

            int distance =
                    calculate(recruiterQuery, closestSkill);

            System.out.println("Closest Skill: " + closestSkill);
            System.out.println("Edit Distance: " + distance);
            System.out.println("Fuzzy Match: FOUND");

        } else {

            System.out.println("Fuzzy Match: NOT FOUND");
        }
    }
}