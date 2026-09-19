package com.talentflow.algorithms;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * Aho-Corasick multi-pattern string matching algorithm.
 *
 * TalentFlow uses this algorithm to search resume text
 * for multiple recruiter keywords simultaneously.
 *
 * Example:
 * patterns = ["java", "python", "react"]
 *
 * The automaton can find all of them in one traversal
 * of the resume text.
 *
 * Time Complexity:
 * - Construction: O(total pattern length)
 * - Search: O(text length + number of matches)
 *
 * Space Complexity:
 * - O(total pattern length)
 */
public final class AhoCorasick {

    /**
     * Trie node used by the Aho-Corasick automaton.
     */
    private static class Node {

        private final Map<Character, Integer> children =
                new HashMap<>();

        private int failureLink = 0;

        private final List<Integer> patternIds =
                new ArrayList<>();
    }

    /**
     * Represents one pattern match in the text.
     */
    public static class Match {

        private final String pattern;
        private final int startIndex;
        private final int endIndex;

        public Match(
                String pattern,
                int startIndex,
                int endIndex) {

            this.pattern = pattern;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
        }

        public String getPattern() {
            return pattern;
        }

        public int getStartIndex() {
            return startIndex;
        }

        public int getEndIndex() {
            return endIndex;
        }

        @Override
        public String toString() {

            return "Match{" +
                    "pattern='" + pattern + '\'' +
                    ", startIndex=" + startIndex +
                    ", endIndex=" + endIndex +
                    '}';
        }
    }

    private final List<Node> nodes =
            new ArrayList<>();

    private final List<String> patterns =
            new ArrayList<>();

    private boolean built = false;

    /**
     * Creates an empty Aho-Corasick automaton.
     */
    public AhoCorasick() {
        nodes.add(new Node());
    }

    /**
     * Adds a pattern to the trie.
     */
    public void addPattern(String pattern) {

        if (pattern == null || pattern.isEmpty()) {
            return;
        }

        String normalized =
                pattern.toLowerCase();

        int nodeIndex = 0;

        for (int i = 0;
             i < normalized.length();
             i++) {

            char character =
                    normalized.charAt(i);

            Integer next =
                    nodes.get(nodeIndex)
                            .children
                            .get(character);

            if (next == null) {

                next = nodes.size();

                nodes.add(new Node());

                nodes.get(nodeIndex)
                        .children
                        .put(character, next);
            }

            nodeIndex = next;
        }

        int patternId = patterns.size();

        patterns.add(pattern);

        nodes.get(nodeIndex)
                .patternIds
                .add(patternId);

        built = false;
    }

    /**
     * Adds multiple patterns.
     */
    public void addPatterns(
            List<String> patternList) {

        if (patternList == null) {
            return;
        }

        for (String pattern : patternList) {
            addPattern(pattern);
        }
    }

    /**
     * Builds failure links.
     *
     * This must be called after all patterns have been added
     * and before searching.
     */
    public void build() {

        Queue<Integer> queue =
                new ArrayDeque<>();

        /*
         * Root children have failure link = root.
         */
        for (int child :
                nodes.get(0).children.values()) {

            nodes.get(child).failureLink = 0;

            queue.add(child);
        }

        /*
         * Breadth-first construction of failure links.
         */
        while (!queue.isEmpty()) {

            int current =
                    queue.remove();

            for (Map.Entry<Character, Integer> entry :
                    nodes.get(current)
                            .children
                            .entrySet()) {

                char character =
                        entry.getKey();

                int child =
                        entry.getValue();

                int fallback =
                        nodes.get(current)
                                .failureLink;

                while (fallback != 0
                        && !nodes.get(fallback)
                        .children
                        .containsKey(character)) {

                    fallback =
                            nodes.get(fallback)
                                    .failureLink;
                }

                Integer fallbackChild =
                        nodes.get(fallback)
                                .children
                                .get(character);

                if (fallbackChild != null
                        && fallbackChild != child) {

                    nodes.get(child).failureLink =
                            fallbackChild;

                } else {

                    nodes.get(child).failureLink = 0;
                }

                /*
                 * Patterns that end at the failure state
                 * are also matches at this state.
                 */
                List<Integer> failurePatterns =
                        nodes.get(
                                nodes.get(child)
                                        .failureLink
                        ).patternIds;

                nodes.get(child)
                        .patternIds
                        .addAll(failurePatterns);

                queue.add(child);
            }
        }

        built = true;
    }

    /**
     * Searches the text for every registered pattern.
     */
    public List<Match> search(String text) {

        List<Match> matches =
                new ArrayList<>();

        if (text == null
                || text.isEmpty()
                || patterns.isEmpty()) {

            return matches;
        }

        if (!built) {
            build();
        }

        String normalized =
                text.toLowerCase();

        int current = 0;

        for (int i = 0;
             i < normalized.length();
             i++) {

            char character =
                    normalized.charAt(i);

            /*
             * Follow failure links until a matching
             * transition is found.
             */
            while (current != 0
                    && !nodes.get(current)
                    .children
                    .containsKey(character)) {

                current =
                        nodes.get(current)
                                .failureLink;
            }

            Integer next =
                    nodes.get(current)
                            .children
                            .get(character);

            if (next != null) {
                current = next;
            } else {
                current = 0;
            }

            /*
             * Report every pattern ending at this position.
             */
            for (int patternId :
                    nodes.get(current)
                            .patternIds) {

                String pattern =
                        patterns.get(patternId);

                int start =
                        i - pattern.length() + 1;

                matches.add(
                        new Match(
                                pattern,
                                start,
                                i
                        )
                );
            }
        }

        return matches;
    }

    /**
     * Returns the number of unique patterns found.
     */
    public int countMatchedPatterns(
            String text) {

        boolean[] found =
                new boolean[patterns.size()];

        for (Match match : search(text)) {

            for (int i = 0;
                 i < patterns.size();
                 i++) {

                if (patterns.get(i)
                        .equals(match.getPattern())) {

                    found[i] = true;
                    break;
                }
            }
        }

        int count = 0;

        for (boolean value : found) {

            if (value) {
                count++;
            }
        }

        return count;
    }

    /**
     * Returns all unique patterns found in the text.
     */
    public List<String> getMatchedPatterns(
            String text) {

        List<String> result =
                new ArrayList<>();

        boolean[] found =
                new boolean[patterns.size()];

        for (Match match : search(text)) {

            for (int i = 0;
                 i < patterns.size();
                 i++) {

                if (!found[i]
                        && patterns.get(i)
                        .equals(match.getPattern())) {

                    found[i] = true;
                    result.add(
                            patterns.get(i)
                    );

                    break;
                }
            }
        }

        return result;
    }

    /**
     * Returns the number of registered patterns.
     */
    public int getPatternCount() {
        return patterns.size();
    }
}