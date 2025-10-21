package de.lulkas_.landeswettbewerb_2025_number_6;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Util {
    public static Map<Integer, Map<Integer, Integer>> generateIndexJumpMap() {
        Map<Integer, Map<Integer, Integer>> TO_RETURN = new HashMap<>(Map.of(
                0, Map.of(0, 1, 1, 3),
                1, Map.of(0, 0, 1, 4, 2, 2),
                2, Map.of(0, 1, 1, 5),
                24, Map.of(0, 21, 1, 25),
                25, Map.of(0, 22, 1, 24, 2, 26),
                26, Map.of(0, 23, 1, 25)
        ));

        for(int i = 0; i < 7; i++) {
            TO_RETURN.put((i + 1) * 3, Map.of(0, i * 3, 1, (i + 1) * 3 + 1, 2, (i + 2) * 3));
            TO_RETURN.put((i + 1) * 3 + 1, Map.of(0, i * 3 + 1, 1, (i + 1) * 3, 2, (i + 1) * 3 + 2, 3, (i + 2) * 3 + 1));
            TO_RETURN.put((i + 1) * 3 + 2, Map.of(0, i * 3 + 2, 1, (i + 1) * 3 + 1, 2, (i + 2) * 3 + 2));
        }

        return TO_RETURN;
    }

    public static Map<Integer, Integer> generateIndexMaxJumpMap() {
        Map<Integer, Integer> TO_RETURN = new HashMap<>();

        for(int i = 0; i < 27; i++) {
            TO_RETURN.put(i, Main.INDEX_JUMP_MAP.get(i).size() - 1);
        }

        return TO_RETURN;
    }

    public static int[] applyJumpState(int[] jumpState) {
        int[] state = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        for(int i = 0; i < jumpState.length; i++) {
            state[Main.INDEX_JUMP_MAP.get(i).get(jumpState[i])]++;
        }

        return state;
    }

    public static int[] updateJumpState(int[] jumpState, int updateCount) {
        int[] toReturn = jumpState.clone();
        int fromLastSpace = updateCount;

        for(int i = 0; i < 27; i++) {
            int temp = toReturn[i] + fromLastSpace;
            toReturn[i] = temp % (Main.INDEX_MAX_JUMP_MAP.get(i) + 1);
            if(i < 26) fromLastSpace = (temp - toReturn[i]) / (Main.INDEX_MAX_JUMP_MAP.get(i) + 1);
        }


        return toReturn;
    }

    public static int[] updateJumpState(int[] jumpState) throws ProgramEndException {
        return updateJumpState(jumpState, 1);
    }

    public static int[] updateJumpStateLegacy(int[] jumpState) throws ProgramEndException {
        int[] toReturn = jumpState.clone();

        toReturn[0]++;

        for(int i = 0; i < 27; i++) {
            if(toReturn[i] <= Main.INDEX_MAX_JUMP_MAP.get(i)) {
                return toReturn;
            }

            if(i == 26) {
                throw new ProgramEndException("");
            }

            toReturn[i] = 0;
            toReturn[i + 1]++;
        }

        return toReturn;
    }

    public static int countFreeSpaces(int[] state) {
        int freeSpaces = 0;

        for(int value : state) {
            if(value == 0) freeSpaces++;
        }

        return freeSpaces;
    }

    public static CalculationResult combineResults(List<CalculationResult> results) {
        CalculationResult toReturn = results.get(0);
        for(CalculationResult result : results) {
            if(result.leastFreeSpaces() < toReturn.leastFreeSpaces()) {
                toReturn = new CalculationResult(result.leastFreeSpaces(), toReturn.mostFreeSpaces());
            }
            if(result.mostFreeSpaces() > toReturn.mostFreeSpaces()) {
                toReturn = new CalculationResult(toReturn.leastFreeSpaces(), result.mostFreeSpaces());
            }
        }
        return toReturn;
    }

    public static String formatNanoTime(long nanoseconds) {
        long days = TimeUnit.NANOSECONDS.toDays(nanoseconds);
        nanoseconds -= TimeUnit.DAYS.toNanos(days);

        long hours = TimeUnit.NANOSECONDS.toHours(nanoseconds);
        nanoseconds -= TimeUnit.HOURS.toNanos(hours);

        long minutes = TimeUnit.NANOSECONDS.toMinutes(nanoseconds);

        StringBuilder sb = new StringBuilder();
        if(days > 0) sb.append(days).append("d ");
        if (hours > 0) sb.append(hours).append("h ");
        if (minutes > 0) sb.append(minutes).append("m ");

        return sb.toString().trim();
    }
}
