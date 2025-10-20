package de.lulkas_.landeswettbewerb_2025_number_6;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Main {
    private static Map<Integer, Map<Integer, Integer>> generateIndexJumpMap() {
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

    private static Map<Integer, Integer> generateIndexMaxJumpMap(Map<Integer, Map<Integer, Integer>> INDEX_JUMP_MAP) {
        Map<Integer, Integer> TO_RETURN = new HashMap<>();

        for(int i = 0; i < 27; i++) {
            TO_RETURN.put(i, INDEX_JUMP_MAP.get(i).size() - 1);
        }

        return TO_RETURN;
    }

    private static int[] applyJumpState(int[] jumpState, Map<Integer, Map<Integer, Integer>> INDEX_JUMP_MAP) {
        int[] state = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        for(int i = 0; i < jumpState.length; i++) {
            state[INDEX_JUMP_MAP.get(i).get(jumpState[i])]++;
        }

        return state;
    }

    private static int[] updateJumpState(int[] jumpState, Map<Integer, Integer> INDEX_MAX_JUMP_MAP) throws ProgramEndException {
        int[] toReturn = jumpState.clone();

        toReturn[0]++;

        for(int i = 0; i < 27; i++) {
            if(toReturn[i] <= INDEX_MAX_JUMP_MAP.get(i)) {
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

    public static String formatPercentage(double value) {
        DecimalFormat df = new DecimalFormat("000.00");
        return df.format(value);
    }

    public static String formatNanoTime(long nanoseconds) {
        long days = TimeUnit.NANOSECONDS.toDays(nanoseconds);
        nanoseconds -= TimeUnit.DAYS.toNanos(days);

        long hours = TimeUnit.NANOSECONDS.toHours(nanoseconds);
        nanoseconds -= TimeUnit.HOURS.toNanos(hours);

        long minutes = TimeUnit.NANOSECONDS.toMinutes(nanoseconds);
        nanoseconds -= TimeUnit.MINUTES.toNanos(minutes);

        StringBuilder sb = new StringBuilder();
        if(days > 0) sb.append(days).append("d ");
        if (hours > 0) sb.append(hours).append("h ");
        if (minutes > 0) sb.append(minutes).append("m ");

        return sb.toString().trim();
    }

    public static void main(String[] args) {
        Map<Integer, Map<Integer, Integer>> INDEX_JUMP_MAP = generateIndexJumpMap();
        Map<Integer, Integer> INDEX_MAX_JUMP_MAP = generateIndexMaxJumpMap(INDEX_JUMP_MAP);

        long i = 1;
        double count = Math.pow(2, 4) * Math.pow(3, 16) * Math.pow(4, 7);

        long startTime = System.nanoTime();

        int[] jump_state = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        while (true) {
            i++;

            try {
                jump_state = updateJumpState(jump_state, INDEX_MAX_JUMP_MAP);
            } catch (ProgramEndException e) {
                break;
            }

            if(((long) (i / 100000000)) * 100000000 == i) {
                long sinceStart = System.nanoTime() - startTime;
                System.out.println(formatPercentage(i / count * 100) + "%      " + "approximately left: " + formatNanoTime((long) ((count / i - 1) * sinceStart)));
            }
        }
    }
}