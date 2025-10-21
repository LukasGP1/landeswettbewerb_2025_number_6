package de.lulkas_.landeswettbewerb_2025_number_6;

import java.util.*;

public class Main {
    private static final int threadCount = 10;
    private static final int calculationsPerThreadPerRound = 1_000_000;

    public static List<CalculationResult> results;
    public static boolean wasLastRound;
    public static Map<Integer, Map<Integer, Integer>> INDEX_JUMP_MAP;
    public static Map<Integer, Integer> INDEX_MAX_JUMP_MAP;

    public static void main(String[] args) throws InterruptedException {
        INDEX_JUMP_MAP = Util.generateIndexJumpMap();
        INDEX_MAX_JUMP_MAP = Util.generateIndexMaxJumpMap();
        results = new ArrayList<>();
        wasLastRound = false;

        CalculationResult result = new CalculationResult(Integer.MAX_VALUE, 0);
        int[] jumpState = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        long round = 0;
        long totalRounds = (long) Math.ceil(11_284_439_629_824L / (double) (threadCount * calculationsPerThreadPerRound));

        long startTime = System.nanoTime();
        while (!wasLastRound) {
            for (int i = 0; i < threadCount; i++) {
                (new CalculationThread(calculationsPerThreadPerRound, jumpState)).start();
                jumpState = Util.updateJumpState(jumpState, calculationsPerThreadPerRound);
            }

            while (results.size() != threadCount) {Thread.sleep(1);}
            results.add(result);
            result = Util.combineResults(results);
            results.clear();

            System.out.println((round + 1) + " rounds of " + totalRounds + " done");
            System.out.println("Most free spaces found: " + result.mostFreeSpaces());
            System.out.println("Least free spaces found: " + result.leastFreeSpaces());
            System.out.println("Approximately left: " + Util.formatNanoTime((System.nanoTime() - startTime) / (round + 1) * (totalRounds - round - 1)));

            round++;
        }
    }
}