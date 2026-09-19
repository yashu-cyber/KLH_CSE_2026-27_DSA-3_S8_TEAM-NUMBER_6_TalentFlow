package com.talentflow.algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class AhoCorasick {

    // Node of the Aho-Corasick trie
    private static class Node {

        Map<Character, Node> children = new HashMap<>();

        Node failure;

        List<String> outputs = new ArrayList<>();
    }

    private final Node root = new Node();

    // Add a search pattern to the trie
    public void addPattern(String pattern) {

        pattern = pattern.toLowerCase().trim();

        Node current = root;

        for (char ch : pattern.toCharArray()) {

            current.children.putIfAbsent(ch, new Node());

            current = current.children.get(ch);
        }

        current.outputs.add(pattern);
    }

    // Build failure links using BFS
    public void buildFailureLinks() {

        Queue<Node> queue = new LinkedList<>();

        root.failure = root;

        // First-level nodes point back to root
        for (Node child : root.children.values()) {

            child.failure = root;

            queue.add(child);
        }

        while (!queue.isEmpty()) {

            Node current = queue.poll();

            for (Map.Entry<Character, Node> entry
                    : current.children.entrySet()) {

                char ch = entry.getKey();
                Node child = entry.getValue();

                Node failure = current.failure;

                while (failure != root
                        && !failure.children.containsKey(ch)) {

                    failure = failure.failure;
                }

                if (failure.children.containsKey(ch)
                        && failure.children.get(ch) != child) {

                    child.failure = failure.children.get(ch);

                } else {

                    child.failure = root;
                }

                // Inherit patterns from failure node
                child.outputs.addAll(
                        child.failure.outputs
                );

                queue.add(child);
            }
        }
    }

    // Search for all patterns in text
    public List<String> search(String text) {

        List<String> matches = new ArrayList<>();

        text = text.toLowerCase();

        Node current = root;

        for (char ch : text.toCharArray()) {

            while (current != root
                    && !current.children.containsKey(ch)) {

                current = current.failure;
            }

            if (current.children.containsKey(ch)) {

                current = current.children.get(ch);

            } else {

                current = root;
            }

            if (!current.outputs.isEmpty()) {

                matches.addAll(current.outputs);
            }
        }

        return matches;
    }

    // Test Aho-Corasick
    public static void main(String[] args) {

        AhoCorasick aho = new AhoCorasick();

        // Recruiter's required skills
        aho.addPattern("DSA");
        aho.addPattern("SQL");
        aho.addPattern("Python");
        aho.addPattern("Java");

        aho.buildFailureLinks();

        String resumeText =
                "Experienced in Python, Java and SQL. "
                + "Strong knowledge of DSA and algorithms.";

        List<String> matches =
                aho.search(resumeText);

        System.out.println(
                "Recruiter Search Patterns: "
                + "[DSA, SQL, Python, Java]"
        );

        System.out.println(
                "Resume: " + resumeText
        );

        System.out.println(
                "Aho-Corasick Matches: " + matches
        );

        System.out.println(
                "Number of Skills Found: "
                        + matches.size()
        );
    }
}