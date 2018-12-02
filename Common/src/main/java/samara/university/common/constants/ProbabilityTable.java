package samara.university.common.constants;

import java.util.Random;

public class ProbabilityTable {
    private static float[][] probabilities;
    private static Random random = new Random();
    private static int currentLevel = 1;

    static {
        float oneTwelfth = 1f / 12;
        float oneSixth = 1f / 6;
        float oneQuarter = 1f / 4;
        float oneThird = 1f / 3;

        probabilities = new float[][]{
                {oneThird, oneThird, oneSixth, oneTwelfth, oneTwelfth},
                {oneQuarter, oneThird, oneQuarter, oneTwelfth, oneTwelfth},
                {oneTwelfth, oneQuarter, oneThird, oneQuarter, oneTwelfth},
                {oneTwelfth, oneTwelfth, oneQuarter, oneThird, oneQuarter},
                {oneTwelfth, oneTwelfth, oneSixth, oneThird, oneThird}
        };
    }

    public static float[][] getProbabilities() {
        return probabilities.clone();
    }

    public static int nextLevel() {
        float[] oldLevelRow = probabilities[currentLevel - 1];
        float nextFloat = random.nextFloat();
        float current = 0;
        int i;
        for (i = 0; i < oldLevelRow.length && Float.compare(nextFloat, current) < 0; i++) {
            current += oldLevelRow[i];
            System.out.println("current: " + current);
        }
        return i + 1;
    }
}