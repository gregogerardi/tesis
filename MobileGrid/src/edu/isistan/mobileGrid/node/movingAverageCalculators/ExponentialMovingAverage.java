package edu.isistan.mobileGrid.node.movingAverageCalculators;

import edu.isistan.mobileGrid.node.ConnectionScoreCalculator;

/**
 * Exponential moving average calculator implementation.
 */
public class ExponentialMovingAverage implements ConnectionScoreCalculator {
    /**
     * reason of decay for older values in an exponential moving average model between 1 and 0
     */
    private double decayReason;
    private Double lastScore = null;
    private long lastMesuredTime = 0;

    public ExponentialMovingAverage(double decayReason) {
        this.decayReason = decayReason;
    }

    public ExponentialMovingAverage(String[] args) {
        this.decayReason = Double.parseDouble(args[0]);
    }

    @Override
    public void average(long time, boolean connectionState) {

        if (lastScore == null) {
            lastScore = 0d;
        } else {
            if (connectionState == CONNECT) {
                lastScore = (lastScore * decayReason - (time - lastMesuredTime));
            } else {
                lastScore = (lastScore * decayReason + (time - lastMesuredTime));
            }
        }
        lastMesuredTime = time;
    }

    @Override
    public double getScore() {
        return lastScore;
    }


}
