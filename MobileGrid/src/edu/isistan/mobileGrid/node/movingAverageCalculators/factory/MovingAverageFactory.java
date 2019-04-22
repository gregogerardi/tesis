package edu.isistan.mobileGrid.node.movingAverageCalculators.factory;

import edu.isistan.mobileGrid.node.ConnectionScoreCalculator;
import edu.isistan.mobileGrid.node.ConnectionScoreCalculatorFactoryInterface;
import edu.isistan.mobileGrid.node.movingAverageCalculators.MovingAverage;

public class MovingAverageFactory implements ConnectionScoreCalculatorFactoryInterface {

    private int amountOfSamples;

    @Override
    public ConnectionScoreCalculator createConnectionScoreCalculator() {
        return new MovingAverage(amountOfSamples);
    }

    public MovingAverageFactory(String args) {
        this.amountOfSamples = Integer.parseInt(args);
    }
}
