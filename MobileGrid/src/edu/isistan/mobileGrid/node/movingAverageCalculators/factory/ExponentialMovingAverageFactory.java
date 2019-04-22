package edu.isistan.mobileGrid.node.movingAverageCalculators.factory;

import edu.isistan.mobileGrid.node.ConnectionScoreCalculator;
import edu.isistan.mobileGrid.node.ConnectionScoreCalculatorFactoryInterface;
import edu.isistan.mobileGrid.node.movingAverageCalculators.ExponentialMovingAverage;

/**
 * Exponential moving average calculator implementation.
 */
public class ExponentialMovingAverageFactory implements ConnectionScoreCalculatorFactoryInterface {

    private double decayReason;

    @Override
    public ConnectionScoreCalculator createConnectionScoreCalculator() {
        return new ExponentialMovingAverage(decayReason);
    }

    public ExponentialMovingAverageFactory(String args) {
        this.decayReason = Double.parseDouble(args);
    }
}
