package com.talentflow.algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.talentflow.models.Candidate;

/**
 * Scalable processing utilities for TalentFlow.
 *
 * Uses:
 * - randomized sampling for lightweight resume verification
 * - parallel processing for independent candidate records
 */
public final class ScalableResumeProcessor {

    private ScalableResumeProcessor() {
    }

    /**
     * Randomly samples candidates from a collection.
     *
     * The original list is never modified.
     */
    public static List<Candidate> randomizedSample(
            List<Candidate> candidates,
            int sampleSize) {

        if (candidates == null
                || candidates.isEmpty()
                || sampleSize <= 0) {

            return new ArrayList<>();
        }

        List<Candidate> copy =
                new ArrayList<>(
                        candidates
                );

        Collections.shuffle(
                copy,
                new Random()
        );

        int size =
                Math.min(
                        sampleSize,
                        copy.size()
                );

        return new ArrayList<>(
                copy.subList(
                        0,
                        size
                )
        );
    }

    /**
     * Processes independent candidates in parallel.
     *
     * The supplied operation is executed independently for
     * each candidate.
     */
    public static <T> List<T> processInParallel(
            List<Candidate> candidates,
            CandidateProcessor<T> processor) {

        if (candidates == null
                || candidates.isEmpty()
                || processor == null) {

            return new ArrayList<>();
        }

        int availableProcessors =
                Runtime.getRuntime()
                        .availableProcessors();

        int threadCount =
                Math.max(
                        2,
                        Math.min(
                                availableProcessors,
                                candidates.size()
                        )
                );

        ExecutorService executor =
                Executors.newFixedThreadPool(
                        threadCount
                );

        try {

            List<Callable<T>> tasks =
                    new ArrayList<>();

            for (Candidate candidate :
                    candidates) {

                tasks.add(
                        () -> processor.process(
                                candidate
                        )
                );
            }

            List<Future<T>> futures =
                    executor.invokeAll(
                            tasks
                    );

            List<T> results =
                    new ArrayList<>();

            for (Future<T> future :
                    futures) {

                try {

                    results.add(
                            future.get()
                    );

                } catch (Exception e) {

                    results.add(null);
                }
            }

            return results;

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            return new ArrayList<>();

        } finally {

            executor.shutdown();
        }
    }

    /**
     * Produces a compact processing fingerprint used for
     * randomized verification.
     */
    public static int fingerprint(
            String text) {

        if (text == null
                || text.isEmpty()) {

            return 0;
        }

        Random random =
                new Random(
                        31L + text.length()
                );

        int samples =
                Math.min(
                        16,
                        text.length()
                );

        int result = 17;

        for (int i = 0;
             i < samples;
             i++) {

            int index =
                    random.nextInt(
                            text.length()
                    );

            result =
                    31 * result
                            + text.charAt(index);
        }

        return result;
    }

    @FunctionalInterface
    public interface CandidateProcessor<T> {

        T process(Candidate candidate);
    }
}
