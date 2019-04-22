package edu.isistan.mobileGrid.node.movingAverageCalculators;

import edu.isistan.mobileGrid.node.ConnectionScoreCalculator;

import java.util.LinkedList;

public class LinealWeightedMovingAverage implements ConnectionScoreCalculator {

    private LinkedList<Long> lastMesures;
    private int amountOfSamples;
    private long lastMesuredTime = 0;

    public LinealWeightedMovingAverage(int amountOfSamples) {
        this.amountOfSamples = amountOfSamples;
    }

    public LinealWeightedMovingAverage(String[] args) {
        this.amountOfSamples = Integer.parseInt(args[0]);
    }

    @Override
    public void average(long time, boolean connectionState) {
        if (lastMesures == null) {
            lastMesures = new LinkedList<>();
            lastMesuredTime = time;
            return;
        }
        if (connectionState == CONNECT) {
            lastMesures.addLast(time - lastMesuredTime);
        } else {
            lastMesures.addLast(-(time - lastMesuredTime));
        }
        if (lastMesures.size() > amountOfSamples) {
            lastMesures.removeFirst();
        }

    }

    @Override
    public double getScore() {
        int weight = 1;
        long score = 0;
        for (Long l : lastMesures) {
            score += l * weight++;
        }
        return score / (factorial(weight));
    }

    public static int factorial(int n) {
        int resultado = 1;
        for (int i = 1; i <= n; i++) {
            resultado *= i;
        }
        return resultado;
    }
}
