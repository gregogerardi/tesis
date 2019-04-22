package edu.isistan.mobileGrid.node.movingAverageCalculators.factory;

import edu.isistan.mobileGrid.node.ConnectionScoreCalculator;
import edu.isistan.mobileGrid.node.ConnectionScoreCalculatorFactoryInterface;
import edu.isistan.mobileGrid.node.movingAverageCalculators.Average;

/**
 * moving average calculator implementation.
 */
public class AverageFactory implements ConnectionScoreCalculatorFactoryInterface {

    public AverageFactory(String args) {
    }

    @Override
    public ConnectionScoreCalculator createConnectionScoreCalculator() {
        return new Average();
    }

}
