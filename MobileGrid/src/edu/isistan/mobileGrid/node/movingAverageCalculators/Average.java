package edu.isistan.mobileGrid.node.movingAverageCalculators;

import edu.isistan.mobileGrid.node.ConnectionScoreCalculator;

/**
     moving average calculator implementation.
 */
public class Average implements ConnectionScoreCalculator {

    private ExponentialMovingAverage  delegate = new ExponentialMovingAverage(1d);

    @Override
    public void average(long time, boolean connect) {
        delegate.average(time,connect);
    }

    public Average(String[] args) {
    }

    public Average() {
    }

    @Override
    public double getScore() {
        return delegate.getScore();
    }

    /*  private Double lastScore = null;
    private long lastMesuredTime = 0;

    @Override
    public void average(long time, boolean connectionState) {
        if (lastScore == null) {
            lastScore = 0d;
            lastMesuredTime = time;
            return;
        }
        if (connectionState == CONNECT) {
            lastScore = (lastScore + (time - lastMesuredTime));
        } else {
            lastScore = (lastScore - (time - lastMesuredTime));
        }

    }

    @Override
    public double getScore() {
        return lastScore;
    }*/


}
