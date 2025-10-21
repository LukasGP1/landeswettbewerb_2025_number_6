package de.lulkas_.landeswettbewerb_2025_number_6;

public class CalculationThread extends Thread {
    private final int calculationCount;
    private final int[] startState;

    public CalculationThread(int calculationCount, int[] startState) {
        this.calculationCount = calculationCount;
        this.startState = startState;
    }

    @Override
    public void run() {
        long i = 1;

        int[] jump_state = startState.clone();

        int mostFreeSpaces = 0;
        int leastFreeSpaces = Integer.MAX_VALUE;

        while (i < calculationCount) {
            i++;

            try {
                jump_state = Util.updateJumpState(jump_state);
            } catch (ProgramEndException e) {
                Main.wasLastRound = true;
                break;
            }

            int freeSpaces = Util.countFreeSpaces(Util.applyJumpState(jump_state));
            if(freeSpaces > mostFreeSpaces) {
                mostFreeSpaces = freeSpaces;
            }
            if(freeSpaces < leastFreeSpaces) {
                leastFreeSpaces = freeSpaces;
            }
        }

        Main.results.add(new CalculationResult(leastFreeSpaces, mostFreeSpaces));
    }
}
