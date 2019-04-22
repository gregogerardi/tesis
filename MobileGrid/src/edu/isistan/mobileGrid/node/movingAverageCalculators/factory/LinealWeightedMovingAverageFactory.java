package edu.isistan.mobileGrid.node.movingAverageCalculators.factory;

import edu.isistan.mobileGrid.node.ConnectionScoreCalculator;
import edu.isistan.mobileGrid.node.ConnectionScoreCalculatorFactoryInterface;

public class LinealWeightedMovingAverageFactory implements ConnectionScoreCalculatorFactoryInterface {

    private int amountOfSamples;
    @Override
    public ConnectionScoreCalculator createConnectionScoreCalculator() {
        return new edu.isistan.mobileGrid.node.movingAverageCalculators.LinealWeightedMovingAverage(amountOfSamples);
    }

    public LinealWeightedMovingAverageFactory(String args) {
        this.amountOfSamples = Integer.parseInt(args);
    }
}
